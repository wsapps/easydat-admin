package cn.easydat.framework.security;

import org.noear.solon.auth.AuthProcessor;
import org.noear.solon.auth.annotation.Logical;

import cn.easydat.common.utils.SecurityUtil;
import cn.hutool.core.util.ArrayUtil;

/**
 * 鉴权处理器
 */
public class AuthProcessorImpl implements AuthProcessor {

    /** IP黑名单 */
    private String[] ipBlackList;

    public void setIpBlackList(String[] ipBlackList) {
        this.ipBlackList = ipBlackList;
    }

    /**
     * 验证IP，是否有权访问
     * @param ip
     * @return
     */
    @Override
    public boolean verifyIp(String ip) {
        return !ArrayUtil.contains(ipBlackList, ip);
    }

    /**
     * 验证登录状态，用户是否已登录
     * @return
     */
    @Override
    public boolean verifyLogined() {
        return SecurityUtil.isLogin();
    }

    /**
     * 验证路径，用户可访问
     * @param path
     * @param method
     * @return
     */
    @Override
    public boolean verifyPath(String path, String method) {
        return true;
    }

    /**
     * 验证特定权限，用户是否有权限（verifyLogined为 true，才会触发）
     * @param permissions
     * @param logical
     * @return
     */
    @Override
    public boolean verifyPermissions(String[] permissions, Logical logical) {
        return SecurityUtil.hasPermission(permissions, Logical.AND == logical);
    }

    /**
     * 验证特定角色，用户是否有角色（verifyLogined为 true，才会触发）
     * @param roles
     * @param logical
     * @return
     */
    @Override
    public boolean verifyRoles(String[] roles, Logical logical) {
        return SecurityUtil.hasRole(roles, Logical.AND == logical);
    }

}
