package cn.easydat.system.controller;

import java.util.List;

import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Delete;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Path;
import org.noear.solon.annotation.Post;
import org.noear.solon.annotation.Put;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.validation.annotation.Validated;

import cn.easydat.common.annotation.Log;
import cn.easydat.common.core.controller.AjaxResultBaseController;
import cn.easydat.common.core.domain.AjaxResult;
import cn.easydat.common.core.page.TableDataInfo;
import cn.easydat.common.enums.BusinessType;
import cn.easydat.system.domain.SysNotice;
import cn.easydat.system.service.SysNoticeService;

/**
 * 公告 信息操作处理
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("system/notice")
public class SysNoticeController extends AjaxResultBaseController {
	@Inject
	private SysNoticeService noticeService;

	/**
	 * 获取通知公告列表
	 */
	@AuthPermissions("system:notice:list")
	@Get
	@Mapping("list")
	public TableDataInfo<SysNotice> list(SysNotice notice) {
		startPage();
		List<SysNotice> list = noticeService.selectNoticeList(notice);
		return getDataTable(list);
	}

	/**
	 * 根据通知公告编号获取详细信息
	 */
	@AuthPermissions("system:notice:query")
	@Get
	@Mapping("{noticeId}")
	public AjaxResult getInfo(@Path Long noticeId) {
		return success(noticeService.selectNoticeById(noticeId));
	}

	/**
	 * 新增通知公告
	 */
	@AuthPermissions("system:notice:add")
	@Log(title = "通知公告", businessType = BusinessType.INSERT)
	@Post
	@Mapping
	public AjaxResult add(@Validated @Body SysNotice notice) {
		notice.setCreateBy(getUsername());
		return toAjax(noticeService.insertNotice(notice));
	}

	/**
	 * 修改通知公告
	 */
	@AuthPermissions("system:notice:edit")
	@Log(title = "通知公告", businessType = BusinessType.UPDATE)
	@Put
	@Mapping
	public AjaxResult edit(@Validated @Body SysNotice notice) {
		notice.setUpdateBy(getUsername());
		return toAjax(noticeService.updateNotice(notice));
	}

	/**
	 * 删除通知公告
	 */
	@AuthPermissions("system:notice:remove")
	@Log(title = "通知公告", businessType = BusinessType.DELETE)
	@Delete
	@Mapping("{noticeIds}")
	public AjaxResult remove(@Path Long[] noticeIds) {
		return toAjax(noticeService.deleteNoticeByIds(noticeIds));
	}
}
