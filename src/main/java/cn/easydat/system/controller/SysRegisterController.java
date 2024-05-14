//package cn.easydat.system.controller;
//
//import org.noear.solon.annotation.Body;
//import org.noear.solon.annotation.Controller;
//import org.noear.solon.annotation.Inject;
//import org.noear.solon.annotation.Mapping;
//import org.noear.solon.annotation.Post;
//
//import cn.easydat.common.core.controller.AjaxResultBaseController;
//import cn.easydat.common.core.domain.AjaxResult;
//import cn.easydat.system.service.SysConfigService;
//import cn.hutool.core.util.StrUtil;
//
///**
// * 注册验证
// */
//@SuppressWarnings("deprecation")
//@Controller
//@Mapping
//public class SysRegisterController extends AjaxResultBaseController {
//	@Inject
//	private SysRegisterService registerService;
//
//	@Inject
//	private SysConfigService configService;
//
//	@Post
//	@Mapping("/register")
//	public AjaxResult register(@Body RegisterBody user) {
//		if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
//			return error("当前系统没有开启注册功能！");
//		}
//		String msg = registerService.register(user);
//		return StrUtil.isEmpty(msg) ? success() : error(msg);
//	}
//}
