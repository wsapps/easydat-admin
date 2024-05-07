package cn.easydat.common.core.domain.model;

import java.util.Set;

import cn.easydat.common.core.domain.entity.SysUser;
import cn.hutool.json.JSONUtil;

/**
 * 登录用户身份权限
 * 
 * @author ruoyi
 */
public class LoginUser {

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 账号
	 */
	private String username;

	/**
	 * 部门ID
	 */
	private Long deptId;

	/**
	 * 用户唯一标识
	 */
	private String token;

	/**
	 * 登录时间
	 */
	private Long loginTime;

	/**
	 * 过期时间
	 */
	private Long expireTime;

	/**
	 * 登录IP地址
	 */
	private String ipaddr;

	/**
	 * 登录地点
	 */
	private String loginLocation;

	/**
	 * 浏览器类型
	 */
	private String browser;

	/**
	 * 操作系统
	 */
	private String os;

	/**
	 * 权限列表
	 */
	private Set<String> permissions;
	
	/**
     * 角色列表
     */
    private Set<String> roles;

	/**
	 * 用户信息
	 */
	private SysUser user;

	public LoginUser() {
	}

	public LoginUser(SysUser user, Set<String> permissions) {
		this.user = user;
		this.permissions = permissions;
	}

	public LoginUser(Long userId, Long deptId, SysUser user, Set<String> permissions) {
		this.userId = userId;
		this.deptId = deptId;
		this.user = user;
		this.permissions = permissions;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Long loginTime) {
		this.loginTime = loginTime;
	}

	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

	public String getLoginLocation() {
		return loginLocation;
	}

	public void setLoginLocation(String loginLocation) {
		this.loginLocation = loginLocation;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public Long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return JSONUtil.toJsonStr(this);
	}

}
