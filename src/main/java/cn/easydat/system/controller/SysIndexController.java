package cn.easydat.system.controller;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Mapping;

import cn.hutool.core.util.StrUtil;

/**
 * 首页
 */
@Controller
@Mapping
public class SysIndexController
{

    /**
     * 访问首页，提示语
     */
	@Get
	@Mapping("/")
    public String index()
    {
        return StrUtil.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", "", "1.0.0");
    }
}
