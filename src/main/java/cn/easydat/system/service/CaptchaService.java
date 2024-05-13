package cn.easydat.system.service;

import org.noear.solon.core.handle.Result;

public interface CaptchaService {

	boolean isCaptchaEnabled();

	String getCaptcha(String uuid);
	
	Result<String> checkCapcha(String code, String uuid);
	
}
