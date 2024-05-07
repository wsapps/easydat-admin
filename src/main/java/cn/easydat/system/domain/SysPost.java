package cn.easydat.system.domain;

import org.noear.solon.validation.annotation.Length;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;

import cn.easydat.common.core.domain.BaseEntity;
import cn.hutool.core.annotation.Alias;
import cn.hutool.json.JSONUtil;

/**
 * 岗位表 sys_post
 */
public class SysPost extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 岗位序号 */
	@Alias("岗位序号")
	@Id
	private Long postId;

	/** 岗位编码 */
	@Alias("岗位编码")
	@NotBlank(message = "岗位编码不能为空")
	@Length(max = 64, message = "岗位编码长度不能超过64个字符")
	private String postCode;

	/** 岗位名称 */
	@Alias("岗位名称")
	@NotBlank(message = "岗位名称不能为空")
	@Length(max = 50, message = "岗位名称长度不能超过50个字符")
	private String postName;

	/** 岗位排序 */
	@Alias("岗位排序")
	@NotNull(message = "显示顺序不能为空")
	private Integer postSort;

	/** 状态（0正常 1停用） */
	@Alias("状态（0正常 1停用）")
	private String status;

	/** 用户是否存在此岗位标识 默认不存在 */
	@Column(ignore = true)
	private boolean flag = false;

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public Integer getPostSort() {
		return postSort;
	}

	public void setPostSort(Integer postSort) {
		this.postSort = postSort;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return JSONUtil.toJsonStr(this);
	}
}
