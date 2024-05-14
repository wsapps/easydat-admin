package cn.easydat.system.service.impl;

import java.time.Duration;

import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.handle.Result;

import cn.easydat.common.constant.CacheConstant;
import cn.easydat.common.constant.EasydatConstant;
import cn.easydat.common.utils.RedisUtil;
import cn.easydat.framework.config.CaptchaConfig;
import cn.easydat.system.service.CaptchaService;
import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.math.Calculator;
import cn.hutool.core.util.StrUtil;

@Component
public class CaptchaServiceImpl implements CaptchaService {

	// 图形验证码宽高
	private static final int CAPTCHA_WIDTH = 160;
	private static final int CAPTCHA_HEIGHT = 60;

	@Inject
	private CaptchaConfig captchaConfig;

	@Inject
	private RedisUtil redisUtil;

	@Override
	public String getCaptcha(String uuid) {
		// 干扰类型
		String invadeType = captchaConfig.getInvadeType();
		String captcha = null;
		if ("circle".equals(invadeType)) {
			// 圆圈干扰验证码（宽、高、验证码字符数、干扰元素个数）
			CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, 4, 10);
			captcha = createCaptcha(circleCaptcha, uuid);
		} else if ("shear".equals(invadeType)) {
			// 扭曲干扰验证码（宽、高、验证码字符数、干扰线宽度）
			ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, 4, 5);
			captcha = createCaptcha(shearCaptcha, uuid);
		} else {
			// 线段干扰验证码（宽、高、验证码字符数、干扰元素个数）
			LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, 4, 15);
			captcha = createCaptcha(lineCaptcha, uuid);
		}
		return captcha;
	}

	@Override
	public Result<String> checkCapcha(String code, String uuid) {
		Result<String> result = Result.succeed();
		if (!StrUtil.isAllNotEmpty(code, uuid)) {
			result = Result.failure(400, "验证码必填");
		}
		String codeKey = CacheConstant.CAPTCHA_CODE_KEY + uuid;
		// 校验
		String cacheCode = redisUtil.getCacheObject(codeKey);
		if (StrUtil.isEmpty(cacheCode)) {
			result = Result.failure(400, "验证码已失效");
		}
		redisUtil.deleteCacheObject(codeKey);
		if (!StrUtil.equalsIgnoreCase(code, cacheCode)) {
			result = Result.failure(400, "验证码错误");
		}

		return result;
	}

	/**
	 * 生成验证码
	 * 
	 * @param t
	 * @param map
	 * @param <T>
	 * @return
	 */
	private <T extends AbstractCaptcha> String createCaptcha(T t, String uuid) {
		String codeKey = CacheConstant.CAPTCHA_CODE_KEY + uuid;
		// 验证码类型
		String type = captchaConfig.getType();
		// 验证码的内容（如果是字符串类型，则验证码的内容就是结果）
		String code = t.getCode();
		// 如果是四则运算
		if ("math".equals(type)) {
			t.setGenerator(new MathGenerator(1));
			// 重新生成code
			t.createCode();
			// 运算结果
			int result = (int) Calculator.conversion(t.getCode());
			code = String.valueOf(result);
		}

		// 将结果放入缓存
		redisUtil.setCacheObject(codeKey, code, Duration.ofMinutes(EasydatConstant.CAPTCHA_CODE_EXPIRATION));

		String img = t.getImageBase64();
		return img;
	}

	@Override
	public boolean isCaptchaEnabled() {
		return captchaConfig.getEnabled();
	}

}
