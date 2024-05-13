package cn.easydat.framework.config;

import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;

/**
 * 验证码配置
 * 
 * @author xm
 */
@Configuration
@Inject(value = "${easydat.captcha}")
public class CaptchaConfig {

	/**
	 * 验证码开关
	 */
	private Boolean enabled;

	/**
	 * 干扰类型: line 线段干扰、circle 圆圈干扰、shear 扭曲干扰
	 */
	private String invadeType;

	/**
	 * 验证码类型: math 四则运算、char 字符
	 */
	private String type;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getInvadeType() {
		return invadeType;
	}

	public void setInvadeType(String invadeType) {
		this.invadeType = invadeType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
