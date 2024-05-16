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
import cn.easydat.system.domain.SysLogininfor;
import cn.easydat.system.service.SysLogininforService;

/**
 * 系统访问记录
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("/monitor/logininfor")
public class SysLogininforController extends AjaxResultBaseController {
	@Inject
	private SysLogininforService logininforService;

//	@Inject
//	private SysPasswordService passwordService;

	@AuthPermissions("monitor:logininfor:list")
	@Get
	@Mapping("/list")
	public TableDataInfo<SysLogininfor> list(SysLogininfor logininfor) {
		startPage();
		List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
		return getDataTable(list);
	}

	@Log(title = "登录日志", businessType = BusinessType.EXPORT)
	@AuthPermissions("monitor:logininfor:export")
	@Post
	@Mapping("/export")
	public void export(SysLogininfor logininfor) {
		List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
		export(list);
	}

	@AuthPermissions("monitor:logininfor:remove")
	@Log(title = "登录日志", businessType = BusinessType.DELETE)
	@Delete
	@Mapping("/{infoIds}")
	public AjaxResult remove(@Path Long[] infoIds) {
		return toAjax(logininforService.deleteLogininforByIds(infoIds));
	}

	@AuthPermissions("monitor:logininfor:remove")
	@Log(title = "登录日志", businessType = BusinessType.CLEAN)
	@Delete
	@Mapping("/clean")
	public AjaxResult clean() {
		logininforService.cleanLogininfor();
		return success();
	}

	//TODO 账户解锁
//	@AuthPermissions("monitor:logininfor:unlock")
//	@Log(title = "账户解锁", businessType = BusinessType.OTHER)
//	@Get
//	@Mapping("/unlock/{userName}")
//	public AjaxResult unlock(@Path("userName") String userName) {
//		passwordService.clearLoginRecordCache(userName);
//		return success();
//	}
}
