package cn.easydat.system.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import cn.easydat.common.utils.SecurityUtil;
import cn.easydat.system.domain.SysDept;
import cn.easydat.system.domain.SysRole;
import cn.easydat.system.domain.SysUser;
import cn.easydat.system.service.SysDeptService;
import cn.easydat.system.service.SysPostService;
import cn.easydat.system.service.SysRoleService;
import cn.easydat.system.service.SysUserService;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 用户信息
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("system/user")
public class SysUserController extends AjaxResultBaseController {
	@Inject
	private SysUserService userService;

	@Inject
	private SysRoleService roleService;

	@Inject
	private SysDeptService deptService;

	@Inject
	private SysPostService postService;

	/**
	 * 获取用户列表
	 */
	@AuthPermissions("system:user:list")
	@Get
	@Mapping("list")
	public TableDataInfo<SysUser> list(SysUser user) {
		startPage();
		List<SysUser> list = userService.selectUserList(user);
		return getDataTable(list);
	}

	/**
	 * 导出用户列表
	 */
	@AuthPermissions("system:user:export")
	@Log(title = "用户管理", businessType = BusinessType.EXPORT)
	@Post
	@Mapping("export")
	public void export(SysUser user) {
		List<SysUser> list = userService.selectUserList(user);
		export(list);
	}

	// TODO importData
//	@Log(title = "用户管理", businessType = BusinessType.IMPORT)
//	@AuthPermissions("system:user:import")
//	@Post
//	@Mapping("/importData")
//	public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
//		ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
//		List<SysUser> userList = util.importExcel(file.getInputStream());
//		String operName = getUsername();
//		String message = userService.importUser(userList, updateSupport, operName);
//		return success(message);
//	}

	@Post
	@Mapping("/importTemplate")
	public void importTemplate() {
		List<SysUser> list = new ArrayList<>();
		export(list);
	}

	/**
	 * 根据用户编号获取详细信息
	 */
	@AuthPermissions("system:user:query")
	@Get
	@Mapping("{userId}")
	public AjaxResult getInfo(@Path Long userId) {
		userService.checkUserDataScope(userId);
		AjaxResult ajax = AjaxResult.success();
		List<SysRole> roles = roleService.selectRoleAll();
		ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
		ajax.put("posts", postService.selectPostAll());
		if (null != userId) {
			SysUser sysUser = userService.selectUserById(userId);
			ajax.put(AjaxResult.DATA_TAG, sysUser);
			ajax.put("postIds", postService.selectPostListByUserId(userId));
			ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
		}
		return ajax;
	}

	/**
	 * 新增用户
	 */
	@AuthPermissions("system:user:add")
	@Log(title = "用户管理", businessType = BusinessType.INSERT)
	@Post
	@Mapping
	public AjaxResult add(@Validated @Body SysUser user) {
		if (!userService.checkUserNameUnique(user)) {
			return error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
		} else if (StrUtil.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
			return error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
		} else if (StrUtil.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
			return error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
		}
		user.setCreateBy(getUsername());
		user.setPassword(SecurityUtil.encryptPassword(user.getPassword()));
		return toAjax(userService.insertUser(user));
	}

	/**
	 * 修改用户
	 */
	@AuthPermissions("system:user:edit")
	@Log(title = "用户管理", businessType = BusinessType.UPDATE)
	@Put
	@Mapping
	public AjaxResult edit(@Validated @Body SysUser user) {
		userService.checkUserAllowed(user);
		userService.checkUserDataScope(user.getUserId());
		if (!userService.checkUserNameUnique(user)) {
			return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
		} else if (StrUtil.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
			return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
		} else if (StrUtil.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
			return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
		}
		user.setUpdateBy(getUsername());
		return toAjax(userService.updateUser(user));
	}

	/**
	 * 删除用户
	 */
	@AuthPermissions("system:user:remove")
	@Log(title = "用户管理", businessType = BusinessType.DELETE)
	@Delete
	@Mapping("{userIds}")
	public AjaxResult remove(@Path Long[] userIds) {
		if (ArrayUtil.contains(userIds, getUserId())) {
			return error("当前用户不能删除");
		}
		return toAjax(userService.deleteUserByIds(userIds));
	}

	/**
	 * 重置密码
	 */
	@AuthPermissions("system:user:resetPwd")
	@Log(title = "用户管理", businessType = BusinessType.UPDATE)
	@Put
	@Mapping("resetPwd")
	public AjaxResult resetPwd(@Body SysUser user) {
		userService.checkUserAllowed(user);
		userService.checkUserDataScope(user.getUserId());
		user.setPassword(SecurityUtil.encryptPassword(user.getPassword()));
		user.setUpdateBy(getUsername());
		return toAjax(userService.resetPwd(user));
	}

	/**
	 * 状态修改
	 */
	@AuthPermissions("system:user:edit")
	@Log(title = "用户管理", businessType = BusinessType.UPDATE)
	@Put
	@Mapping("changeStatus")
	public AjaxResult changeStatus(@Body SysUser user) {
		userService.checkUserAllowed(user);
		userService.checkUserDataScope(user.getUserId());
		user.setUpdateBy(getUsername());
		return toAjax(userService.updateUserStatus(user));
	}

	/**
	 * 根据用户编号获取授权角色
	 */
	@AuthPermissions("system:user:query")
	@Get
	@Mapping("authRole/{userId}")
	public AjaxResult authRole(@Path("userId") Long userId) {
		AjaxResult ajax = AjaxResult.success();
		SysUser user = userService.selectUserById(userId);
		List<SysRole> roles = roleService.selectRolesByUserId(userId);
		ajax.put("user", user);
		ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
		return ajax;
	}

	/**
	 * 用户授权角色
	 */
	@AuthPermissions("system:user:edit")
	@Log(title = "用户管理", businessType = BusinessType.GRANT)
	@Put
	@Mapping("authRole")
	public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
		userService.checkUserDataScope(userId);
		userService.insertUserAuth(userId, roleIds);
		return success();
	}

	/**
	 * 获取部门树列表
	 */
	@AuthPermissions("system:user:list")
	@Get
	@Mapping("/deptTree")
	public AjaxResult deptTree(SysDept dept) {
		return success(deptService.selectDeptTreeList(dept));
	}
}
