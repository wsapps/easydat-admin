package cn.easydat.framework.config;

import java.util.List;

import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

/**
 * XSS配置
 */
@Configuration
@Inject(value = "${easydat.xss}")
public class XssConfig {

	/**
	 * 过滤开关
	 */
	private Boolean enabled;

	/**
	 * 排除路径
	 */
	private List<String> excludes;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getExcludes() {
		return excludes;
	}

	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}

}
