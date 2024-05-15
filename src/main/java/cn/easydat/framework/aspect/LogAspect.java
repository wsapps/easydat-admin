package cn.easydat.framework.aspect;

import org.noear.solon.Solon;
import org.noear.solon.core.NvMap;
import org.noear.solon.core.aspect.Interceptor;
import org.noear.solon.core.aspect.Invocation;
import org.noear.solon.core.handle.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.easydat.common.annotation.Log;
import cn.easydat.common.core.domain.model.LoginUser;
import cn.easydat.common.enums.BusinessStatus;
import cn.easydat.common.enums.HttpMethod;
import cn.easydat.common.utils.SecurityUtil;
import cn.easydat.system.domain.SysOperLog;
import cn.easydat.system.domain.SysUser;
import cn.easydat.system.service.SysOperLogService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

public class LogAspect implements Interceptor {

	private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);
	
	@Override
	public Object doIntercept(Invocation inv) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object result = null;
		Throwable throwable = null;

		try {
			result = inv.invoke();
		} catch (Throwable e) {
			throwable = e;
		}

		long endTime = System.currentTimeMillis();
		try {
			handleLog(inv, throwable, startTime, endTime, result);
		} catch (Throwable e) {
			LOG.error("doIntercept error", e);
		}

		return result;
	}

	private void handleLog(Invocation inv, final Throwable e, long startTime, long endTime, Object result) {
		// 获取当前的用户
		LoginUser loginUser = SecurityUtil.getLoginUser();

		// *========数据库日志=========*//
		SysOperLog operLog = new SysOperLog();
		operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
		// 请求的地址
		String ip = Context.current().realIp();
		operLog.setOperIp(ip);
		operLog.setOperUrl(StrUtil.sub(Context.current().path(), 0, 255));

		if (loginUser != null) {
			operLog.setOperName(loginUser.getUsername());
			SysUser currentUser = loginUser.getUser();
			if (null != currentUser && null != currentUser.getDept()) {
				operLog.setDeptName(currentUser.getDept().getDeptName());
			}
		}

		if (e != null) {
			operLog.setStatus(BusinessStatus.FAIL.ordinal());
			operLog.setErrorMsg(StrUtil.sub(e.getMessage(), 0, 2000));
		}

		// 设置方法名称
		String className = inv.getTargetClz().getName();
		String methodName = inv.method().getMethod().getName();
		operLog.setMethod(className + "." + methodName + "()");
		// 设置请求方式
		operLog.setRequestMethod(Context.current().method());

		// 处理设置注解上的参数

		Log log = inv.method().getAnnotation(Log.class);
		// 设置action动作
		operLog.setBusinessType(log.businessType().ordinal());
		// 设置标题
		operLog.setTitle(log.title());
		// 设置操作人类别
		operLog.setOperatorType(log.operatorType().ordinal());
		// 是否需要保存request，参数和值
		if (log.isSaveRequestData()) {
			// 获取参数的信息，传入到数据库中。
			// setRequestValue(joinPoint, operLog, log.excludeParamNames());
			String requestMethod = operLog.getRequestMethod();
			NvMap paramsMap = Context.current().paramMap();
			if (paramsMap.isEmpty() && (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))) {
				String params = JSONUtil.toJsonStr(inv.args());
				operLog.setOperParam(StrUtil.sub(params, 0, 2000));
			} else {
				operLog.setOperParam(StrUtil.sub(JSONUtil.toJsonStr(paramsMap), 0, 2000));
			}
		}
		// 是否需要保存response，参数和值
		if (log.isSaveResponseData() && null != result) {
			operLog.setJsonResult(StrUtil.sub(JSONUtil.toJsonStr(result), 0, 2000));
		}

		// 设置消耗时间
		operLog.setCostTime(endTime - startTime);
		saveLog(operLog);
	}

	private void saveLog(SysOperLog operLog) {
		SysOperLogService sysOperLogService = Solon.context().getBean(SysOperLogService.class);
		sysOperLogService.insertOperlog(operLog);
		
	}

}
