package cn.easydat.common.core.domain.entity;

import java.util.Set;

import org.noear.solon.validation.annotation.Length;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;

import com.mybatisflex.annotation.Id;

import cn.easydat.common.core.domain.BaseEntity;
import cn.hutool.core.annotation.Alias;
import cn.hutool.json.JSONUtil;

/**
 * 角色表 sys_role
 * 
 * @author ruoyi
 */
public class SysRole extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 角色ID */
	@Alias("角色序号")
	@Id
	private Long roleId;

	/** 角色名称 */
	@Alias("角色名称")
	@NotBlank(message = "角色名称不能为空")
	@Length(max = 30, message = "角色名称长度不能超过30个字符")
	private String roleName;

	/** 角色权限 */
	@Alias("角色名称")
	@NotBlank(message = "角色名称不能为空")
	@Length(max = 30, message = "角色名称长度不能超过30个字符")
	private String roleKey;

	/** 角色排序 */
	@Alias("角色排序")
	@NotNull(message = "显示顺序不能为空")
	private Integer roleSort;

	/** 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限） */
	@Alias("数据范围")
	private String dataScope;

	/** 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示） */
	private boolean menuCheckStrictly;

	/** 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ） */
	private boolean deptCheckStrictly;

	/** 角色状态（0正常 1停用） */
	@Alias("角色状态（0正常 1停用）")
	private String status;

	/** 删除标志（0代表存在 2代表删除） */
	private String delFlag;

	/** 用户是否存在此角色标识 默认不存在 */
	private boolean flag = false;

	/** 菜单组 */
	private Long[] menuIds;

	/** 部门组（数据权限） */
	private Long[] deptIds;

	/** 角色菜单权限 */
	private Set<String> permissions;

	public SysRole() {

	}

	public SysRole(Long roleId) {
		this.roleId = roleId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public boolean isAdmin() {
		return isAdmin(this.roleId);
	}

	public static boolean isAdmin(Long roleId) {
		return roleId != null && 1L == roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}

	@NotNull(message = "显示顺序不能为空")
	public Integer getRoleSort() {
		return roleSort;
	}

	public void setRoleSort(Integer roleSort) {
		this.roleSort = roleSort;
	}

	public String getDataScope() {
		return dataScope;
	}

	public void setDataScope(String dataScope) {
		this.dataScope = dataScope;
	}

	public boolean isMenuCheckStrictly() {
		return menuCheckStrictly;
	}

	public void setMenuCheckStrictly(boolean menuCheckStrictly) {
		this.menuCheckStrictly = menuCheckStrictly;
	}

	public boolean isDeptCheckStrictly() {
		return deptCheckStrictly;
	}

	public void setDeptCheckStrictly(boolean deptCheckStrictly) {
		this.deptCheckStrictly = deptCheckStrictly;
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

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public Long[] getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(Long[] menuIds) {
		this.menuIds = menuIds;
	}

	public Long[] getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(Long[] deptIds) {
		this.deptIds = deptIds;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return JSONUtil.toJsonStr(this);
	}

}
