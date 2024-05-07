package cn.easydat.system.domain;

import org.noear.solon.validation.annotation.Length;
import org.noear.solon.validation.annotation.NotBlank;

import com.mybatisflex.annotation.Id;

import cn.easydat.common.core.domain.BaseEntity;
import cn.hutool.core.annotation.Alias;
import cn.hutool.json.JSONUtil;

/**
 * 字典数据表 sys_dict_data
 * 
 * @author ruoyi
 */
public class SysDictData extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 字典编码 */
	@Alias("字典编码")
	@Id
	private Long dictCode;

	/** 字典排序 */
	@Alias("字典排序")
	private Long dictSort;

	/** 字典标签 */
	@Alias("字典标签")
	@NotBlank(message = "字典标签不能为空")
	@Length(max = 100, message = "字典标签长度不能超过100个字符")
	private String dictLabel;

	/** 字典键值 */
	@Alias("字典键值")
	@NotBlank(message = "字典键值不能为空")
	@Length(max = 100, message = "字典键值长度不能超过100个字符")
	private String dictValue;

	/** 字典类型 */
	@Alias("字典类型")
	@NotBlank(message = "字典类型不能为空")
	@Length(max = 100, message = "字典类型长度不能超过100个字符")
	private String dictType;

	/** 样式属性（其他样式扩展） */
	@Length(max = 100, message = "样式属性长度不能超过100个字符")
	private String cssClass;

	/** 表格字典样式 */
	private String listClass;

	/** 是否默认（Y是 N否） */
	@Alias("是否默认（Y是 N否）")
	private String isDefault;

	/** 状态（0正常 1停用） */
	@Alias("状态（0正常 1停用）")
	private String status;

	public Long getDictCode() {
		return dictCode;
	}

	public void setDictCode(Long dictCode) {
		this.dictCode = dictCode;
	}

	public Long getDictSort() {
		return dictSort;
	}

	public void setDictSort(Long dictSort) {
		this.dictSort = dictSort;
	}

	public String getDictLabel() {
		return dictLabel;
	}

	public void setDictLabel(String dictLabel) {
		this.dictLabel = dictLabel;
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getListClass() {
		return listClass;
	}

	public void setListClass(String listClass) {
		this.listClass = listClass;
	}

//	public boolean getDefault() {
//		return UserConstants.YES.equals(this.isDefault);
//	}

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
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
