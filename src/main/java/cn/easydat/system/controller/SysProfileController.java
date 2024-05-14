package cn.easydat.system.controller;

import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Put;
import org.noear.solon.core.handle.UploadedFile;

import cn.easydat.common.annotation.Log;
import cn.easydat.common.core.controller.AjaxResultBaseController;
import cn.easydat.common.core.domain.AjaxResult;
import cn.easydat.common.core.domain.model.LoginUser;
import cn.easydat.common.enums.BusinessType;
import cn.easydat.common.utils.SecurityUtil;
import cn.easydat.system.domain.SysUser;
import cn.easydat.system.service.SysUserService;
import cn.hutool.core.util.StrUtil;

/**
 * 个人信息 业务处理
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("/system/user/profile")
public class SysProfileController extends AjaxResultBaseController {
	@Inject
	private SysUserService userService;

	/**
	 * 个人信息
	 */
	@Get
	@Mapping
	public AjaxResult profile() {
		LoginUser loginUser = getLoginUser();
		SysUser user = loginUser.getUser();
		AjaxResult ajax = AjaxResult.success(user);
		ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
		ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
		return ajax;
	}

	/**
	 * 修改用户
	 */
	@Log(title = "个人信息", businessType = BusinessType.UPDATE)
	@Put
	@Mapping
	public AjaxResult updateProfile(@Body SysUser user) {
		LoginUser loginUser = getLoginUser();
		SysUser currentUser = loginUser.getUser();
		currentUser.setNickName(user.getNickName());
		currentUser.setEmail(user.getEmail());
		currentUser.setPhonenumber(user.getPhonenumber());
		currentUser.setSex(user.getSex());
		if (StrUtil.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(currentUser)) {
			return error("修改用户'" + loginUser.getUsername() + "'失败，手机号码已存在");
		}
		if (StrUtil.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(currentUser)) {
			return error("修改用户'" + loginUser.getUsername() + "'失败，邮箱账号已存在");
		}
		if (userService.updateUserProfile(currentUser) > 0) {
			// 更新缓存用户信息
			SecurityUtil.setLoginUser(loginUser);
			return success();
		}
		return error("修改个人信息异常，请联系管理员");
	}

	/**
	 * 重置密码
	 */
	@Log(title = "个人信息", businessType = BusinessType.UPDATE)
	@Put
	@Mapping("/updatePwd")
	public AjaxResult updatePwd(String oldPassword, String newPassword) {
		LoginUser loginUser = getLoginUser();
		String userName = loginUser.getUsername();
		SysUser sysUser = userService.selectUserByUserName(loginUser.getUsername());
		String password = sysUser.getPassword();
		if (!SecurityUtil.matchesPassword(oldPassword, password)) {
			return error("修改密码失败，旧密码错误");
		}
		if (SecurityUtil.matchesPassword(newPassword, password)) {
			return error("新密码不能与旧密码相同");
		}
		newPassword = SecurityUtil.encryptPassword(newPassword);
		if (userService.resetUserPwd(userName, newPassword) > 0) {
			// 更新缓存用户密码
			loginUser.getUser().setPassword(newPassword);
			SecurityUtil.setLoginUser(loginUser);
			return success();
		}
		return error("修改密码异常，请联系管理员");
	}

	//TODO avatar
	/**
	 * 头像上传
	 */
	@Log(title = "用户头像", businessType = BusinessType.UPDATE)
	@Put
	@Mapping("/avatar")
	public AjaxResult avatar(UploadedFile file) throws Exception {
		if (!file.isEmpty()) {
			LoginUser loginUser = getLoginUser();
			file.getContent();
			//String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
			String avatar = null;
			
			if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
				AjaxResult ajax = AjaxResult.success();
				ajax.put("imgUrl", avatar);
				// 更新缓存用户头像
				loginUser.getUser().setAvatar(avatar);
				SecurityUtil.setLoginUser(loginUser);
				return ajax;
			}
		}
		return error("上传图片异常，请联系管理员");
	}
}
