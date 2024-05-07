package cn.easydat.system.domain;

import java.util.ArrayList;
import java.util.List;

import org.noear.solon.validation.annotation.Email;
import org.noear.solon.validation.annotation.Length;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;

import cn.easydat.common.core.domain.BaseEntity;
import cn.hutool.json.JSONUtil;

/**
 * 部门表 sys_dept
 * 
 * @author ruoyi
 */
public class SysDept extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 部门ID */
	private Long deptId;

	/** 父部门ID */
	private Long parentId;

	/** 祖级列表 */
	private String ancestors;

	/** 部门名称 */
	@NotBlank(message = "部门名称不能为空")
    @Length(max = 30, message = "部门名称长度不能超过30个字符")
	private String deptName;

	/** 显示顺序 */
	private Integer orderNum;

	/** 负责人 */
	private String leader;

	/** 联系电话 */
	@Length(max = 11, message = "联系电话长度不能超过11个字符")
	private String phone;

	/** 邮箱 */
	@Email(message = "邮箱格式不正确")
    @Length(max = 50, message = "邮箱长度不能超过50个字符")
	private String email;

	/** 部门状态:0正常,1停用 */
	private String status;

	/** 删除标志（0代表存在 2代表删除） */
	private String delFlag;

	/** 父部门名称 */
	private String parentName;

	/** 子部门 */
	private List<SysDept> children = new ArrayList<SysDept>();

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getAncestors() {
		return ancestors;
	}

	public void setAncestors(String ancestors) {
		this.ancestors = ancestors;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@NotNull(message = "显示顺序不能为空")
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<SysDept> getChildren() {
		return children;
	}

	public void setChildren(List<SysDept> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return JSONUtil.toJsonStr(this);
	}
}
