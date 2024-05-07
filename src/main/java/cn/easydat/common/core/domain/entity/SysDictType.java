package cn.easydat.common.core.domain.entity;

import org.noear.solon.validation.annotation.Length;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.Pattern;

import com.mybatisflex.annotation.Id;

import cn.easydat.common.core.domain.BaseEntity;
import cn.hutool.core.annotation.Alias;
import cn.hutool.json.JSONUtil;

/**
 * 字典类型表 sys_dict_type
 * 
 * @author ruoyi
 */
public class SysDictType extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 字典主键 */
	@Id
	@Alias("字典编码")
	private Long dictId;

	/** 字典名称 */
	@Alias("字典名称")
	@NotBlank(message = "字典名称不能为空")
	@Length(max = 100, message = "字典类型名称长度不能超过100个字符")
	private String dictName;

	/** 字典类型 */
	@Alias("字典类型")
	@NotBlank(message = "字典类型不能为空")
	@Length(max = 100, message = "字典类型类型长度不能超过100个字符")
	@Pattern(value = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下划线）")
	private String dictType;

	/** 状态（0正常 1停用） */
	@Alias("状态（0正常 1停用）")
	private String status;

	public Long getDictId() {
		return dictId;
	}

	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return JSONUtil.toJsonStr(this);
	}

}
