package cn.easydat.system.controller.monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Delete;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Path;
import org.noear.solon.auth.annotation.AuthPermissions;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.easydat.common.annotation.Log;
import cn.easydat.common.constant.CacheConstant;
import cn.easydat.common.core.controller.AjaxResultBaseController;
import cn.easydat.common.core.domain.AjaxResult;
import cn.easydat.common.core.domain.model.LoginUser;
import cn.easydat.common.core.page.TableDataInfo;
import cn.easydat.common.enums.BusinessType;
import cn.easydat.common.utils.SecurityUtil;
import cn.easydat.system.domain.SysUserOnline;
import cn.easydat.system.service.SysUserOnlineService;
import cn.hutool.core.util.StrUtil;

/**
 * 在线用户监控
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("/monitor/online")
public class SysUserOnlineController extends AjaxResultBaseController {
	@Inject
	private SysUserOnlineService userOnlineService;

	@AuthPermissions("monitor:online:list")
	@Get
	@Mapping("/list")
	public TableDataInfo<SysUserOnline> list(String ipaddr, String userName) {
		Collection<String> keys = StpUtil.searchSessionId("", 0, -1, false);

		List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
		for (String key : keys) {
			SaSession session = StpUtil.getSessionBySessionId(key);
			LoginUser user = session.getModel(CacheConstant.LOGIN_USER_KEY, LoginUser.class);
			StpUtil.getSessionBySessionId(key);
			if (StrUtil.isNotEmpty(ipaddr) && StrUtil.isNotEmpty(userName)) {
				userOnlineList.add(userOnlineService.selectOnlineByInfo(ipaddr, userName, user));
			} else if (StrUtil.isNotEmpty(ipaddr)) {
				userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipaddr, user));
			} else if (StrUtil.isNotEmpty(userName) && null != user.getUser()) {
				userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
			} else {
				userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
			}
		}
		Collections.reverse(userOnlineList);
		userOnlineList.removeAll(Collections.singleton(null));
		return getDataTable(userOnlineList);
	}

	/**
	 * 强退用户
	 */
	@AuthPermissions("monitor:online:forceLogout")
	@Log(title = "在线用户", businessType = BusinessType.FORCE)
	@Delete
	@Mapping("/{tokenId}")
	public AjaxResult forceLogout(@Path String tokenId) {
		SecurityUtil.logout(CacheConstant.LOGIN_USER_KEY + tokenId);
		return success();
	}
}
