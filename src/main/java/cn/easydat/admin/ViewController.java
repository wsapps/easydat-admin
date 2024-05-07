package cn.easydat.admin;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;

@Controller
@Mapping("/test")
public class ViewController {

	@Get
	@Mapping("/view")
	public String view() throws Exception {

		return "ok";
	}

}
