package cn.easydat.common.core.controller;

import java.util.List;

import org.noear.solon.core.handle.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.easydat.common.constant.EasydatConstant;
import cn.easydat.common.core.domain.AjaxResult;
import cn.easydat.common.core.domain.model.LoginUser;
import cn.easydat.common.core.page.TableDataInfo;
import cn.easydat.common.utils.SecurityUtil;
import cn.easydat.common.utils.SqlUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

/**
 * web层通用数据处理
 */
@Deprecated
public class AjaxResultBaseController {
	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	/**
     * 当前记录起始索引
     */
	protected static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
	protected static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
	protected static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
	protected static final String IS_ASC = "isAsc";

    /**
     * 分页参数合理化
     */
	protected static final String REASONABLE = "reasonable";
    
	/**
	 * 设置请求分页数据
	 */
	protected void startPage() {
		Integer pageNum = Convert.toInt(Context.current().param(PAGE_NUM), 1);
		Integer pageSize = Convert.toInt(Context.current().param(PAGE_SIZE), 10);
		String orderBy = SqlUtil.escapeOrderBySql(Context.current().param(ORDER_BY_COLUMN));
		// String isAsc = Context.current().param(IS_ASC);
		Boolean reasonable = Convert.toBool((Context.current().param(REASONABLE)));
		PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
	}

	/**
	 * 设置请求排序数据
	 */
	protected void startOrderBy() {
		String orderBy = SqlUtil.escapeOrderBySql(Context.current().param(ORDER_BY_COLUMN));
		if (StrUtil.isNotEmpty(orderBy)) {
			PageHelper.orderBy(orderBy);
		}
	}

	/**
	 * 清理分页的线程变量
	 */
	protected void clearPage() {
		PageHelper.clearPage();
	}

	/**
	 * 响应请求分页数据
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> TableDataInfo<T> getDataTable(List<T> list) {
		TableDataInfo rspData = new TableDataInfo();
		rspData.setCode(EasydatConstant.SUCCESS);
		rspData.setMsg("查询成功");
		rspData.setRows(list);
		rspData.setTotal(new PageInfo(list).getTotal());
		return rspData;
	}

	/**
	 * 返回成功
	 */
	protected AjaxResult success() {
		return AjaxResult.success();
	}

	/**
	 * 返回失败消息
	 */
	protected AjaxResult error() {
		return AjaxResult.error();
	}

	/**
	 * 返回成功消息
	 */
	protected AjaxResult success(String message) {
		return AjaxResult.success(message);
	}

	/**
	 * 返回成功消息
	 */
	protected AjaxResult success(Object data) {
		return AjaxResult.success(data);
	}

	/**
	 * 返回失败消息
	 */
	protected AjaxResult error(String message) {
		return AjaxResult.error(message);
	}

	/**
	 * 返回警告消息
	 */
	protected AjaxResult warn(String message) {
		return AjaxResult.warn(message);
	}

	/**
	 * 响应返回结果
	 * 
	 * @param rows 影响行数
	 * @return 操作结果
	 */
	protected AjaxResult toAjax(int rows) {
		return rows > 0 ? AjaxResult.success() : AjaxResult.error();
	}

	/**
	 * 响应返回结果
	 * 
	 * @param result 结果
	 * @return 操作结果
	 */
	protected AjaxResult toAjax(boolean result) {
		return result ? success() : error();
	}

	/**
	 * 页面跳转
	 */
	protected String redirect(String url) {
		return StrUtil.format("redirect:{}", url);
	}

	/**
	 * 获取用户缓存信息
	 */
	protected LoginUser getLoginUser() {
		return SecurityUtil.getLoginUser();
	}

	/**
	 * 获取登录用户id
	 */
	protected Long getUserId() {
		return getLoginUser().getUserId();
	}

	/**
	 * 获取登录部门id
	 */
	protected Long getDeptId() {
		return getLoginUser().getDeptId();
	}

	/**
	 * 获取登录用户名
	 */
	protected String getUsername() {
		return getLoginUser().getUsername();
	}
	
	protected <T> void export(List<T> list) {
		Context ctx = Context.current();
		ctx.contentType("application/x-www-form-urlencoded");
		// 通过工具类创建 writer，默认创建 xls格式
		ExcelWriter writer = ExcelUtil.getWriter();
		// 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
		writer.setOnlyAlias(false);
		try {
			// 一次性写出内容，使用默认样式，强制输出标题
			writer.write(list, true);
			// 写出到目标流
			writer.flush(ctx.outputStream());
		} catch (Exception e) {
			LOGGER.error("export error", e);
		} finally {
			writer.close();
		}
    }
}
