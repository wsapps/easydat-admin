package cn.easydat.system.service.impl;

import org.noear.solon.annotation.Component;

import cn.easydat.common.core.domain.model.LoginUser;
import cn.easydat.system.domain.SysUserOnline;
import cn.easydat.system.service.SysUserOnlineService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 在线用户 服务层处理
 */
@Component
public class SysUserOnlineServiceImpl implements SysUserOnlineService {
	/**
	 * 通过登录地址查询信息
	 * 
	 * @param ipaddr 登录地址
	 * @param user   用户信息
	 * @return 在线用户信息
	 */
	@Override
	public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user) {
		if (StrUtil.equals(ipaddr, user.getIpaddr())) {
			return loginUserToUserOnline(user);
		}
		return null;
	}

	/**
	 * 通过用户名称查询信息
	 * 
	 * @param userName 用户名称
	 * @param user     用户信息
	 * @return 在线用户信息
	 */
	@Override
	public SysUserOnline selectOnlineByUserName(String userName, LoginUser user) {
		if (StrUtil.equals(userName, user.getUsername())) {
			return loginUserToUserOnline(user);
		}
		return null;
	}

	/**
	 * 通过登录地址/用户名称查询信息
	 * 
	 * @param ipaddr   登录地址
	 * @param userName 用户名称
	 * @param user     用户信息
	 * @return 在线用户信息
	 */
	@Override
	public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user) {
		if (StrUtil.equals(ipaddr, user.getIpaddr()) && StrUtil.equals(userName, user.getUsername())) {
			return loginUserToUserOnline(user);
		}
		return null;
	}

	/**
	 * 设置在线用户信息
	 * 
	 * @param user 用户信息
	 * @return 在线用户
	 */
	@Override
	public SysUserOnline loginUserToUserOnline(LoginUser user) {
		if (ObjectUtil.isNull(user) || ObjectUtil.isNull(user.getUser())) {
			return null;
		}
		SysUserOnline sysUserOnline = new SysUserOnline();
		sysUserOnline.setTokenId(user.getToken());
		sysUserOnline.setUserName(user.getUsername());
		sysUserOnline.setIpaddr(user.getIpaddr());
		sysUserOnline.setLoginLocation(user.getLoginLocation());
		sysUserOnline.setBrowser(user.getBrowser());
		sysUserOnline.setOs(user.getOs());
		sysUserOnline.setLoginTime(user.getLoginTime());
		if (ObjectUtil.isNotNull(user.getUser().getDept())) {
			sysUserOnline.setDeptName(user.getUser().getDept().getDeptName());
		}
		return sysUserOnline;
	}
}
