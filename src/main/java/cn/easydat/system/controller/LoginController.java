package cn.easydat.system.controller;

import java.util.List;
import java.util.Set;

import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Post;
import org.noear.solon.core.handle.Result;

import cn.easydat.common.core.domain.AjaxResult;
import cn.easydat.common.core.domain.model.LoginBody;
import cn.easydat.common.utils.SecurityUtil;
import cn.easydat.system.domain.SysMenu;
import cn.easydat.system.domain.SysUser;
import cn.easydat.system.service.CaptchaService;
import cn.easydat.system.service.LoginService;
import cn.easydat.system.service.SysMenuService;

@Controller
@Mapping
@SuppressWarnings("deprecation")
public class LoginController {

	@Inject
	private CaptchaService captchaService;

	@Inject
	private LoginService loginService;
	
	@Inject
	private SysMenuService menuService;

	@Post
	@Mapping("/login")
	public AjaxResult login(@Body LoginBody loginBody) {
		AjaxResult ajax = AjaxResult.success();
		Result<String> captchaResult = Result.succeed();
		if (captchaService.isCaptchaEnabled()) {
			captchaResult = captchaService.checkCapcha(loginBody.getCode(), loginBody.getUuid());
		}

		if (captchaResult.getCode() == Result.SUCCEED_CODE) {
			String token = loginService.login(loginBody);
			ajax.put("token", token);
		}

		return ajax;
	}

	/**
	 * 登出
	 */
	@Post
	@Mapping("logout")
	public AjaxResult logout() {
		loginService.logout();
		return AjaxResult.success();
	}

	@Get
	@Mapping("/getInfo")
	public AjaxResult getInfo() {
		SysUser user = SecurityUtil.getLoginUser().getUser();
		// 角色集合
		Set<String> roles = loginService.getRolePermission(user);
		// 权限集合
		Set<String> permissions = loginService.getMenuPermission(user);
		AjaxResult ajax = AjaxResult.success();
		ajax.put("user", user);
		ajax.put("roles", roles);
		ajax.put("permissions", permissions);
		return ajax;
	}

	@Get
	@Mapping("/getRouters")
	public AjaxResult getRouters() {
		Long userId = SecurityUtil.getUserId();
		List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
		return AjaxResult.success(menuService.buildMenus(menus));
	}
}
