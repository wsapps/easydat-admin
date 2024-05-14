package cn.easydat.system.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Context;

import cn.easydat.common.core.domain.model.LoginBody;
import cn.easydat.common.core.domain.model.LoginUser;
import cn.easydat.common.utils.SecurityUtil;
import cn.easydat.framework.security.BCryptPasswordEncoder;
import cn.easydat.system.domain.SysRole;
import cn.easydat.system.domain.SysUser;
import cn.easydat.system.service.LoginService;
import cn.easydat.system.service.SysMenuService;
import cn.easydat.system.service.SysRoleService;
import cn.easydat.system.service.SysUserService;
import cn.hutool.core.collection.CollectionUtil;

@Component
public class LoginServiceImpl implements LoginService {

	@Inject
	private SysUserService userService;

	@Inject
	private SysRoleService roleService;

	@Inject
	private SysMenuService menuService;
	
	@Inject
	private LoginService loginService;
	

	@Override
	public String login(LoginBody loginBody) {
		String token = null;
		SysUser sysUser = userService.selectUserByUserName(loginBody.getUsername());

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		boolean pwdValidatePassed = passwordEncoder.matches(loginBody.getPassword(), sysUser.getPassword());
		if (pwdValidatePassed) {
			LoginUser loginUser = new LoginUser();
			loginUser.setUser(sysUser);
			loginUser.setUserId(sysUser.getUserId());
			String username = sysUser.getUserName();
			loginUser.setUsername(username);
			loginUser.setIpaddr(Context.current().realIp());
			
			// 角色集合
	        Set<String> roles = loginService.getRolePermission(sysUser);
	        loginUser.setRoles(roles);
	        // 权限集合
	        Set<String> permissions = loginService.getMenuPermission(sysUser);
	        loginUser.setPermissions(permissions);
			
			LoginUser login = SecurityUtil.login(loginUser);
			token = login.getToken();
		} else {

		}
		return token;
	}

	@Override
	public void logout() {
		//String userName = SecurityUtil.getUserName();
		//recordLoginLog(userName, LOGIN_FAIL, "登出成功");
		SecurityUtil.logout();
	}

	@Override
	public Set<String> getRolePermission(SysUser user) {
		Set<String> roles = new HashSet<String>();
		// 管理员拥有所有权限
		if (user.isAdmin()) {
			roles.add("admin");
		} else {
			roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
		}
		return roles;
	}

	@Override
	public Set<String> getMenuPermission(SysUser user) {
		Set<String> perms = new HashSet<String>();
		// 管理员拥有所有权限
		if (user.isAdmin()) {
			perms.add("*:*:*");
		} else {
			List<SysRole> roles = user.getRoles();
			if (!CollectionUtil.isEmpty(roles)) {
				// 多角色设置permissions属性，以便数据权限匹配权限
				for (SysRole role : roles) {
					Set<String> rolePerms = menuService.selectMenuPermsByRoleId(role.getRoleId());
					role.setPermissions(rolePerms);
					perms.addAll(rolePerms);
				}
			} else {
				perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
			}
		}
		return perms;
	}

}
