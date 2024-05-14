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
import cn.easydat.system.domain.SysPost;
import cn.easydat.system.service.SysPostService;

/**
 * 岗位信息操作处理
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("system/post")
public class SysPostController extends AjaxResultBaseController {
	@Inject
	private SysPostService postService;

	/**
	 * 获取岗位列表
	 */
	@AuthPermissions("system:post:list")
	@Get
	@Mapping("list")
	public TableDataInfo<SysPost> list(SysPost post) {
		startPage();
		List<SysPost> list = postService.selectPostList(post);
		return getDataTable(list);
	}

	@AuthPermissions("system:post:export")
	@Log(title = "岗位管理", businessType = BusinessType.EXPORT)
	@Post
	@Mapping("export")
	public void export(SysPost post) {
		List<SysPost> list = postService.selectPostList(post);
		export(list);
	}

	/**
	 * 根据岗位编号获取详细信息
	 */
	@AuthPermissions("system:post:query")
	@Get
	@Mapping("{postId}")
	public AjaxResult getInfo(@Path Long postId) {
		return success(postService.selectPostById(postId));
	}

	/**
	 * 新增岗位
	 */
	@AuthPermissions("system:post:add")
	@Log(title = "岗位管理", businessType = BusinessType.INSERT)
	@Post
	@Mapping
	public AjaxResult add(@Validated @Body SysPost post) {
		if (!postService.checkPostNameUnique(post)) {
			return error("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
		} else if (!postService.checkPostCodeUnique(post)) {
			return error("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
		}
		post.setCreateBy(getUsername());
		return toAjax(postService.insertPost(post));
	}

	/**
	 * 修改岗位
	 */
	@AuthPermissions("system:post:edit")
	@Log(title = "岗位管理", businessType = BusinessType.UPDATE)
	@Put
	@Mapping
	public AjaxResult edit(@Validated @Body SysPost post) {
		if (!postService.checkPostNameUnique(post)) {
			return error("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
		} else if (!postService.checkPostCodeUnique(post)) {
			return error("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
		}
		post.setUpdateBy(getUsername());
		return toAjax(postService.updatePost(post));
	}

	/**
	 * 删除岗位
	 */
	@AuthPermissions("system:post:remove")
	@Log(title = "岗位管理", businessType = BusinessType.DELETE)
	@Delete
	@Mapping("{postIds}")
	public AjaxResult remove(@Path Long[] postIds) {
		return toAjax(postService.deletePostByIds(postIds));
	}

	/**
	 * 获取岗位选择框列表
	 */
	@Get
	@Mapping("/optionselect")
	public AjaxResult optionselect() {
		List<SysPost> posts = postService.selectPostAll();
		return success(posts);
	}
}
