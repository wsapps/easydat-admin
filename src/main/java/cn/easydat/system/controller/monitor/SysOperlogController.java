package cn.easydat.system.controller.monitor;

import java.util.List;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Delete;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Path;
import org.noear.solon.annotation.Post;
import org.noear.solon.auth.annotation.AuthPermissions;

import cn.easydat.common.annotation.Log;
import cn.easydat.common.core.controller.AjaxResultBaseController;
import cn.easydat.common.core.domain.AjaxResult;
import cn.easydat.common.core.page.TableDataInfo;
import cn.easydat.common.enums.BusinessType;
import cn.easydat.system.domain.SysOperLog;
import cn.easydat.system.service.SysOperLogService;

/**
 * 操作日志记录
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("/monitor/operlog")
public class SysOperlogController extends AjaxResultBaseController {
	@Inject
	private SysOperLogService operLogService;

	@AuthPermissions("monitor:operlog:list")
	@Get
	@Mapping("/list")
	public TableDataInfo<SysOperLog> list(SysOperLog operLog) {
		startPage();
		List<SysOperLog> list = operLogService.selectOperLogList(operLog);
		return getDataTable(list);
	}

	@Log(title = "操作日志", businessType = BusinessType.EXPORT)
	@AuthPermissions("monitor:operlog:export")
	@Post
	@Mapping("/export")
	public void export(SysOperLog operLog) {
		List<SysOperLog> list = operLogService.selectOperLogList(operLog);
		export(list);
	}

	@Log(title = "操作日志", businessType = BusinessType.DELETE)
	@AuthPermissions("monitor:operlog:remove")
	@Delete
	@Mapping("/{operIds}")
	public AjaxResult remove(@Path Long[] operIds) {
		return toAjax(operLogService.deleteOperLogByIds(operIds));
	}

	@Log(title = "操作日志", businessType = BusinessType.CLEAN)
	@AuthPermissions("monitor:operlog:remove")
	@Delete
	@Mapping("/clean")
	public AjaxResult clean() {
		operLogService.cleanOperLog();
		return success();
	}
}
