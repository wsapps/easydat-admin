package cn.easydat.framework.interceptor;

import java.util.Map;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.solon.core.handle.Result;
import org.noear.solon.core.route.RouterInterceptor;
import org.noear.solon.core.route.RouterInterceptorChain;

import cn.easydat.common.core.domain.AjaxResult;
import cn.easydat.common.exception.EasydatException;
import cn.easydat.common.utils.XssUtil;
import cn.easydat.framework.config.XssConfig;
import cn.hutool.core.util.StrUtil;

/**
 * 全局路由拦截器
 */
@SuppressWarnings("deprecation")
@Component
public class GlobalRouterInterceptor implements RouterInterceptor {

	@Inject
	XssConfig xssConfig;

	// 以下前缀的路径不统一返回结果（监控、接口文档）
	private static final String[] ignorePaths = { "/solon-admin", "/swagger" };

	@Override
	public void doIntercept(Context ctx, Handler mainHandler, RouterInterceptorChain chain) throws Throwable {
		// XSS
		if (mainHandler != null && xssConfig.getEnabled() && !xssConfig.getExcludes().contains(ctx.pathAsLower())) {
			// 请求头
			for (Map.Entry<String, String> kv : ctx.headerMap().entrySet()) {
				kv.setValue(cleanXss(kv.getValue()));
			}
			// 请求参数
			for (Map.Entry<String, String> kv : ctx.paramMap().entrySet()) {
				kv.setValue(cleanXss(kv.getValue()));
			}
			// 请求体
			if (ctx.contentType() != null && ctx.contentType().contains("json")) {
				ctx.bodyNew(cleanXss(ctx.body()));
			}
		}
		chain.doIntercept(ctx, mainHandler);
	}

	/**
	 * 统一的返回结果（将所有返回结果统一为Result，render执行前调用）
	 */
	@Override
	public Object postResult(Context ctx, Object obj) throws Throwable {
		// 部分路径不统一返回结果
		if (StrUtil.startWithAny(ctx.pathAsLower(), ignorePaths)) {
			return obj;
		}
		
		if(obj instanceof AjaxResult) {
			return obj;
		}
		
		// 如果没有按Result tyle 渲染
		if (obj == null && ctx.status() == 404) {
			obj = new EasydatException(404, "请求的接口不存在或不再支持");
		}
		Result<?> result = null;
		if (obj instanceof EasydatException) {
			// 处理标准的状态码
			EasydatException apiCode = (EasydatException) obj;
			result = Result.failure(apiCode.getCode(), apiCode.getDescription());
		} else if (obj instanceof Throwable) {
			// 处理异常
			Throwable throwable = (Throwable) obj;
			throwable.printStackTrace(); // 打印异常
			EasydatException apiCode = new EasydatException(throwable);
			result = Result.failure(apiCode.getCode(), apiCode.getDescription());
		} else if (obj instanceof Result) {
			// 处理Result结构
			result = (Result<?>) obj;
		} else {
			// 处理java bean数据（为扩展新的）
			result = Result.succeed(obj);
		}
		return result;
	}

	private String cleanXss(String input) {
		if (StrUtil.isBlankOrUndefined(input)) {
			return input;
		}
		input = XssUtil.removeEvent(input);
		input = XssUtil.removeScript(input);
		input = XssUtil.removeEval(input);
		input = XssUtil.swapJavascript(input);
		input = XssUtil.swapVbscript(input);
		input = XssUtil.swapLivescript(input);
		input = XssUtil.encode(input);
		return input;
	}

}
