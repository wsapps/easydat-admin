package cn.easydat.system.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;

import cn.easydat.common.core.domain.AjaxResult;
import cn.easydat.system.service.CaptchaService;
import cn.hutool.core.util.IdUtil;

@Controller
@Mapping
@SuppressWarnings("deprecation")
public class CaptchaController {
	
	@Inject
	private CaptchaService captchaService;

	@Get
	@Mapping("/captchaImage")
	public AjaxResult getCaptchaImage() {

		AjaxResult ajax = AjaxResult.success();
		boolean captchaEnabled = captchaService.isCaptchaEnabled();
		ajax.put("captchaEnabled", captchaEnabled);
		if (captchaEnabled) {
			String uuid = IdUtil.simpleUUID();
			String imageBase64 = captchaService.getCaptcha(uuid);
			ajax.put("uuid", uuid);
			ajax.put("img", imageBase64);
		} else {

		}
		
		return ajax;
	}
}
