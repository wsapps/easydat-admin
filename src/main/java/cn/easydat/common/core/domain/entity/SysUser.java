package cn.easydat.common.core.domain.entity;

import java.util.Date;
import java.util.List;

import org.noear.solon.validation.annotation.Email;
import org.noear.solon.validation.annotation.Length;
import org.noear.solon.validation.annotation.NotBlank;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.ColumnMask;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.core.mask.Masks;

import cn.easydat.common.core.domain.BaseEntity;
import cn.hutool.core.annotation.Alias;
import cn.hutool.json.JSONUtil;

/**
 * 用户对象 sys_user
 * 
 * @author ruoyi
 */
public class SysUser extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 用户ID */
	@Alias("用户序号")
	@Id
	private Long userId;

	/** 部门ID */
	private Long deptId;

	/** 用户账号 */
	@Alias("账号")
	@NotBlank(message = "账号不能为空")
	@Length(min = 2, max = 20, message = "账号长度应在2和20之间")
	private String userName;

	/** 用户昵称 */
	@Alias("用户昵称")
	@Length(max = 30, message = "用户昵称长度不能超过30个字符")
	private String nickName;

	/** 用户邮箱 */
	@Alias("用户邮箱")
	@Email(message = "邮箱格式不正确")
	@Length(max = 50, message = "邮箱长度不能超过50个字符")
	private String email;

	/** 手机号码 */
	@Alias("手机号码")
	@Length(max = 11, message = "手机号码长度不能超过11个字符")
	private String phonenumber;

	/** 用户性别 */
	@Alias("用户性别（0男 1女）")
	private String sex;

	/** 用户头像 */
	private String avatar;

	/** 密码 */
	@ColumnMask(Masks.PASSWORD)
	private String password;

	/** 帐号状态（0正常 1停用） */
	@Alias("帐号状态（0正常 1停用）")
	private String status;

	/** 删除标志（0代表存在 2代表删除） */
	private String delFlag;

	/** 最后登录IP */
	@Alias("最后登录IP")
	private String loginIp;

	/** 最后登录时间 */
	@Alias("最后登录时间")
	private Date loginDate;

	/** 部门对象 */
	@Column(ignore = true)
	private SysDept dept;

	/** 角色对象 */
	@Column(ignore = true)
	private List<SysRole> roles;

	/** 角色组 */
	private Long[] roleIds;

	/** 岗位组 */
	private Long[] postIds;

	/** 角色ID */
	private Long roleId;

	public SysUser() {

	}

	public SysUser(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean isAdmin() {
		return isAdmin(this.userId);
	}

	public static boolean isAdmin(Long userId) {
		return userId != null && 1L == userId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public SysDept getDept() {
		return dept;
	}

	public void setDept(SysDept dept) {
		this.dept = dept;
	}

	public List<SysRole> getRoles() {
		return roles;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}

	public Long[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}

	public Long[] getPostIds() {
		return postIds;
	}

	public void setPostIds(Long[] postIds) {
		this.postIds = postIds;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return JSONUtil.toJsonStr(this);
	}

}
