package cn.easydat.common.core.domain.model;

import org.noear.solon.annotation.Note;

import cn.easydat.common.exception.EasydatException;

/**
 * 状态码定义
 */
public class ApiCodes {

    /**
     * 成功
     */
    @Note("成功")
    public static final EasydatException CODE_200 = new EasydatException(200, "成功");

    /**
     * 失败，未知错误
     */
    @Note("失败，未知错误")
    public static final EasydatException CODE_400 = new EasydatException(400, "未知错误");

    /**
     * 登录已失效
     */
    @Note("登录已失效或未登录")
    public static final EasydatException CODE_401 = new EasydatException(401, "登录已失效");

    /**
     * 请求的接口不存在或不再支持
     */
    @Note("请求的接口不存在或不再支持")
    public static final EasydatException CODE_404 = new EasydatException(404, "请求的接口不存在或不再支持");

    /**
     * 未知错误
     */
    @Note("未知错误")
    public static EasydatException CODE_400(Throwable cause) {
        return new EasydatException(cause);
    }

    /**
     * 未知错误
     */
    @Note("未知错误")
    public static EasydatException CODE_400(String description) {
        return new EasydatException(400, description);
    }

}
