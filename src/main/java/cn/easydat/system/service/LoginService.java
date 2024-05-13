package cn.easydat.system.service;

import java.util.Set;

import cn.easydat.common.core.domain.model.LoginBody;
import cn.easydat.system.domain.SysUser;

public interface LoginService {

	String login(LoginBody loginBody);
	
	void logout();

	Set<String> getRolePermission(SysUser user);

	Set<String> getMenuPermission(SysUser user);
}
