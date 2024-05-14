package cn.easydat.common.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.easydat.common.constant.CacheConstant;
import cn.easydat.common.constant.EasydatConstant;
import cn.easydat.common.core.domain.model.LoginUser;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * 鉴权 工具类
 */
public class SecurityUtil {

    /**
     * 登录
     * @param loginUser
     */
    public static LoginUser login(LoginUser loginUser) {
        StpUtil.login(loginUser.getUserId());

        long loginTime = DateUtil.date().getTime() / 1000;
        loginUser.setLoginTime(loginTime);
        long tokenTimeout = StpUtil.getTokenTimeout();
        long expireTime = -1;
        if(tokenTimeout != -1) {
            expireTime = loginTime + tokenTimeout;
        }
        loginUser.setExpireTime(expireTime);
        loginUser.setToken(getTokenValue());

        setLoginUser(loginUser);
        return loginUser;
    }

    /**
     * 设置用户数据
     * @param loginUser
     */
    public static void setLoginUser(LoginUser loginUser) {
        StpUtil.getTokenSession().set(CacheConstant.LOGIN_USER_KEY, loginUser);
    }

    /**
     * 获取用户数据
     */
    public static LoginUser getLoginUser() {
        try {
            return Convert.convert(LoginUser.class, StpUtil.getTokenSession().get(CacheConstant.LOGIN_USER_KEY), null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取 token-name（HttpConstant.TOKEN_NAME）
     */
    public static String getTokenName() {
        return StpUtil.getTokenName();
    }

    /**
     * 获取 token的值
     */
    public static String getTokenValue() {
        return StpUtil.getTokenValue();
    }

    /**
     * 获取 token剩余有效期（单位：秒，返回 -1代表永久有效）
     */
    public static long getTokenTimeOut() {
        return StpUtil.getTokenTimeout();
    }

    /**
     * 获取用户 ID
     * @return
     */
    public static long getUserId() {
        return StpUtil.getLoginIdAsLong();
    }

    /**
     * 获取账号
     * @return
     */
    public static String getUserName() {
        LoginUser loginUser = getLoginUser();
        return ObjectUtil.isNotNull(loginUser) ? loginUser.getUsername() : null;
    }

    /**
     * 登出
     */
    public static void logout() {
        StpUtil.logout();
    }

    /**
     * 是否登录
     * @return
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }

    /**
     * 是否为超级管理员
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && userId == EasydatConstant.ADMIN_USER_ID;
    }

    /**
     * 是否拥有某项权限
     * @param permissions 权限数组
     * @param and   and/or
     * @return 结果
     */
    public static boolean hasPermission(String[] permissions, boolean and) {
        return and ? StpUtil.hasPermissionAnd(permissions) : StpUtil.hasPermissionOr(permissions);
    }

    /**
     * 是否拥有某项角色
     * @param roles 角色数组
     * @param and   and/or
     * @return 结果
     */
    public static boolean hasRole(String[] roles, boolean and) {
        return and ? StpUtil.hasRoleAnd(roles) : StpUtil.hasRoleOr(roles);
    }

    /**
     * 加密登录密码
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        // 使用 MD5摘要算法加密
        return SecureUtil.md5(password);
    }

    /**
     * 判断密码是否相同
     * @param rawPassword 明文真实密码
     * @param encodedPassword 加密后密码
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return StrUtil.equals(encryptPassword(rawPassword), encodedPassword);
    }

}
