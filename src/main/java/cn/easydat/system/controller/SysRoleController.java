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
import cn.easydat.common.core.domain.model.LoginUser;
import cn.easydat.common.core.page.TableDataInfo;
import cn.easydat.common.enums.BusinessType;
import cn.easydat.common.utils.SecurityUtil;
import cn.easydat.system.domain.SysDept;
import cn.easydat.system.domain.SysRole;
import cn.easydat.system.domain.SysUser;
import cn.easydat.system.domain.SysUserRole;
import cn.easydat.system.service.LoginService;
import cn.easydat.system.service.SysDeptService;
import cn.easydat.system.service.SysRoleService;
import cn.easydat.system.service.SysUserService;

/**
 * 角色信息
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("system/role")
public class SysRoleController extends AjaxResultBaseController {
	@Inject
	private SysRoleService roleService;

	@Inject
	private LoginService loginService;

	@Inject
	private SysUserService userService;

	@Inject
	private SysDeptService deptService;

	@AuthPermissions("system:role:list")
	@Get
	@Mapping("list")
	public TableDataInfo<SysRole> list(SysRole role) {
		startPage();
		List<SysRole> list = roleService.selectRoleList(role);
		return getDataTable(list);
	}

	@AuthPermissions("system:role:export")
	@Log(title = "角色管理", businessType = BusinessType.EXPORT)
	@Post
	@Mapping("export")
	public void export(SysRole role) {
		List<SysRole> list = roleService.selectRoleList(role);
		export(list);
	}

	/**
	 * 根据角色编号获取详细信息
	 */
	@AuthPermissions("system:role:query")
	@Get
	@Mapping("{roleId}")
	public AjaxResult getInfo(@Path Long roleId) {
		roleService.checkRoleDataScope(roleId);
		return success(roleService.selectRoleById(roleId));
	}

	/**
	 * 新增角色
	 */
	@AuthPermissions("system:role:add")
	@Log(title = "角色管理", businessType = BusinessType.INSERT)
	@Post
	@Mapping
	public AjaxResult add(@Validated @Body SysRole role) {
		if (!roleService.checkRoleNameUnique(role)) {
			return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
		} else if (!roleService.checkRoleKeyUnique(role)) {
			return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
		}
		role.setCreateBy(getUsername());
		return toAjax(roleService.insertRole(role));

	}

	/**
	 * 修改保存角色
	 */
	@AuthPermissions("system:role:edit")
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@Put
	@Mapping
	public AjaxResult edit(@Validated @Body SysRole role) {
		roleService.checkRoleAllowed(role);
		roleService.checkRoleDataScope(role.getRoleId());
		if (!roleService.checkRoleNameUnique(role)) {
			return error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
		} else if (!roleService.checkRoleKeyUnique(role)) {
			return error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
		}
		role.setUpdateBy(getUsername());

		if (roleService.updateRole(role) > 0) {
			// 更新缓存用户权限
			LoginUser loginUser = getLoginUser();
			if (null != loginUser.getUser() && !loginUser.getUser().isAdmin()) {
				loginUser.setPermissions(loginService.getMenuPermission(loginUser.getUser()));
				loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
				SecurityUtil.setLoginUser(loginUser);
			}
			return success();
		}
		return error("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
	}

	/**
	 * 修改保存数据权限
	 */
	@AuthPermissions("system:role:edit")
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@Put
	@Mapping("/dataScope")
	public AjaxResult dataScope(@Body SysRole role) {
		roleService.checkRoleAllowed(role);
		roleService.checkRoleDataScope(role.getRoleId());
		return toAjax(roleService.authDataScope(role));
	}

	/**
	 * 状态修改
	 */
	@AuthPermissions("system:role:edit")
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@Put
	@Mapping("/changeStatus")
	public AjaxResult changeStatus(@Body SysRole role) {
		roleService.checkRoleAllowed(role);
		roleService.checkRoleDataScope(role.getRoleId());
		role.setUpdateBy(getUsername());
		return toAjax(roleService.updateRoleStatus(role));
	}

	/**
	 * 删除角色
	 */
	@AuthPermissions("system:role:remove")
	@Log(title = "角色管理", businessType = BusinessType.DELETE)
	@Delete
	@Mapping("{roleIds}")
	public AjaxResult remove(@Path Long[] roleIds) {
		return toAjax(roleService.deleteRoleByIds(roleIds));
	}

	/**
	 * 获取角色选择框列表
	 */
	@AuthPermissions("system:role:query")
	@Get
	@Mapping("/optionselect")
	public AjaxResult optionselect() {
		return success(roleService.selectRoleAll());
	}

	/**
	 * 查询已分配用户角色列表
	 */
	@AuthPermissions("system:role:list")
	@Get
	@Mapping("/authUser/allocatedList")
	public TableDataInfo<SysUser> allocatedList(SysUser user) {
		startPage();
		List<SysUser> list = userService.selectAllocatedList(user);
		return getDataTable(list);
	}

	/**
	 * 查询未分配用户角色列表
	 */
	@AuthPermissions("system:role:list")
	@Get
	@Mapping("/authUser/unallocatedList")
	public TableDataInfo<SysUser> unallocatedList(SysUser user) {
		startPage();
		List<SysUser> list = userService.selectUnallocatedList(user);
		return getDataTable(list);
	}

	/**
	 * 取消授权用户
	 */
	@AuthPermissions("system:role:edit")
	@Log(title = "角色管理", businessType = BusinessType.GRANT)
	@Put
	@Mapping("/authUser/cancel")
	public AjaxResult cancelAuthUser(@Body SysUserRole userRole) {
		return toAjax(roleService.deleteAuthUser(userRole));
	}

	/**
	 * 批量取消授权用户
	 */
	@AuthPermissions("system:role:edit")
	@Log(title = "角色管理", businessType = BusinessType.GRANT)
	@Put
	@Mapping("/authUser/cancelAll")
	public AjaxResult cancelAuthUserAll(Long roleId, Long[] userIds) {
		return toAjax(roleService.deleteAuthUsers(roleId, userIds));
	}

	/**
	 * 批量选择用户授权
	 */
	@AuthPermissions("system:role:edit")
	@Log(title = "角色管理", businessType = BusinessType.GRANT)
	@Put
	@Mapping("/authUser/selectAll")
	public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds) {
		roleService.checkRoleDataScope(roleId);
		return toAjax(roleService.insertAuthUsers(roleId, userIds));
	}

	/**
	 * 获取对应角色部门树列表
	 */
	@AuthPermissions("system:role:query")
	@Get
	@Mapping(value = "/deptTree/{roleId}")
	public AjaxResult deptTree(@Path("roleId") Long roleId) {
		AjaxResult ajax = AjaxResult.success();
		ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
		ajax.put("depts", deptService.selectDeptTreeList(new SysDept()));
		return ajax;
	}
}
