package cn.easydat.system.domain;

import org.noear.solon.validation.annotation.Length;
import org.noear.solon.validation.annotation.NotBlank;

import com.mybatisflex.annotation.Id;

import cn.easydat.common.core.domain.BaseEntity;
import cn.hutool.core.annotation.Alias;
import cn.hutool.json.JSONUtil;

/**
 * 参数配置表 sys_config
 */
public class SysConfig extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 参数主键 */
	@Alias("参数主键")
	@Id
	private Long configId;

	/** 参数名称 */
	@Alias("参数名称")
	@NotBlank(message = "参数名称不能为空")
	@Length(max = 100, message = "参数名称不能超过100个字符")
	private String configName;

	/** 参数键名 */
	@Alias("参数键名")
	@NotBlank(message = "参数键名长度不能为空")
	@Length(max = 100, message = "参数键名长度不能超过100个字符")
	private String configKey;

	/** 参数键值 */
	@Alias("参数键值")
	@NotBlank(message = "参数键值不能为空")
	@Length(max = 500, message = "参数键值长度不能超过500个字符")
	private String configValue;

	/** 系统内置（Y是 N否） */
	@Alias("系统内置（Y是 N否）")
	private String configType;

	public Long getConfigId() {
		return configId;
	}

	public void setConfigId(Long configId) {
		this.configId = configId;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	@Override
	public String toString() {
		return JSONUtil.toJsonStr(this);
	}
}
