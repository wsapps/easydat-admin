package cn.easydat.framework.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.noear.solon.annotation.Component;

import cn.dev33.satoken.stp.StpInterface;
import cn.easydat.common.core.domain.model.LoginUser;
import cn.easydat.common.utils.SecurityUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;

/**
 * 获取自定义权限和角色列表
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 权限列表
     * @param loginId
     * @param loginType
     * @return
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> list = new ArrayList<>();
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if(ObjectUtil.isNotNull(loginUser)) {
            Set<String> permissions = loginUser.getPermissions();
            if(CollectionUtil.isNotEmpty(permissions)) {
                list = Convert.toList(String.class, permissions);
            }
        }
        return list;
    }

    /**
     * 角色列表
     * @param loginId
     * @param loginType
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> list = new ArrayList<>();
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if(ObjectUtil.isNotNull(loginUser)) {
            Set<String> roles = loginUser.getRoles();
            if(CollectionUtil.isNotEmpty(roles)) {
                list = Convert.toList(String.class, roles);
            }
        }
        return list;
    }

}
