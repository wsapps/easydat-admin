package cn.easydat.system.domain;

import cn.hutool.json.JSONUtil;

/**
 * 角色和菜单关联 sys_role_menu
 */
public class SysRoleMenu
{
    /** 角色ID */
    private Long roleId;
    
    /** 菜单ID */
    private Long menuId;

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getMenuId()
    {
        return menuId;
    }

    public void setMenuId(Long menuId)
    {
        this.menuId = menuId;
    }

    @Override
	public String toString() {
		return JSONUtil.toJsonStr(this);
	}
}
