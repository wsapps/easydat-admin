package cn.easydat.system.controller.monitor;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.auth.annotation.AuthPermissions;

import cn.easydat.common.core.domain.AjaxResult;
import cn.easydat.common.core.domain.Server;

/**
 * 服务器监控
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("/monitor/server")
public class ServerController {
	@AuthPermissions("monitor:server:list")
	@Get
	@Mapping
	public AjaxResult getInfo() throws Exception {
		Server server = new Server();
		server.copyTo();
		return AjaxResult.success(server);
	}
}
