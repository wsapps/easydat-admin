package cn.easydat.system.controller;

import java.util.List;

import org.noear.solon.annotation.Body;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Delete;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Path;
import org.noear.solon.annotation.Post;
import org.noear.solon.annotation.Put;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.noear.solon.validation.annotation.Validated;

import cn.easydat.common.annotation.Log;
import cn.easydat.common.core.controller.AjaxResultBaseController;
import cn.easydat.common.core.domain.AjaxResult;
import cn.easydat.common.enums.BusinessType;
import cn.easydat.system.domain.SysMenu;
import cn.easydat.system.service.SysMenuService;
import cn.hutool.core.util.StrUtil;

/**
 * 菜单信息
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("system/menu")
public class SysMenuController extends AjaxResultBaseController {
	@Inject
	private SysMenuService menuService;

	/**
	 * 获取菜单列表
	 */
	@AuthPermissions("system:menu:list")
	@Get
	@Mapping("list")
	public AjaxResult list(SysMenu menu) {
		List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
		return success(menus);
	}

	/**
	 * 根据菜单编号获取详细信息
	 */
	@AuthPermissions("system:menu:query")
	@Get
	@Mapping("{menuId}")
	public AjaxResult getInfo(@Path Long menuId) {
		return success(menuService.selectMenuById(menuId));
	}

	/**
	 * 获取菜单下拉树列表
	 */
	@Get
	@Mapping("treeselect")
	public AjaxResult treeselect(SysMenu menu) {
		List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
		return success(menuService.buildMenuTreeSelect(menus));
	}

	/**
	 * 加载对应角色菜单列表树
	 */
	@Get
	@Mapping("roleMenuTreeselect/{roleId}")
	public AjaxResult roleMenuTreeselect(@Path("roleId") Long roleId) {
		List<SysMenu> menus = menuService.selectMenuList(getUserId());
		AjaxResult ajax = AjaxResult.success();
		ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
		ajax.put("menus", menuService.buildMenuTreeSelect(menus));
		return ajax;
	}

	/**
	 * 新增菜单
	 */
	@AuthPermissions("system:menu:add")
	@Log(title = "菜单管理", businessType = BusinessType.INSERT)
	@Post
	@Mapping
	public AjaxResult add(@Validated @Body SysMenu menu) {
		if (!menuService.checkMenuNameUnique(menu)) {
			return error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
		} else if ("0".equals(menu.getIsFrame()) && !StrUtil.startWithAny(menu.getPath(), "http", "https")) {
			return error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
		}
		menu.setCreateBy(getUsername());
		return toAjax(menuService.insertMenu(menu));
	}

	/**
	 * 修改菜单
	 */
	@AuthPermissions("system:menu:edit")
	@Log(title = "菜单管理", businessType = BusinessType.UPDATE)
	@Put
	@Mapping
	public AjaxResult edit(@Validated @Body SysMenu menu) {
		if (!menuService.checkMenuNameUnique(menu)) {
			return error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
		} else if ("0".equals(menu.getIsFrame()) && !StrUtil.startWithAny(menu.getPath(), "http", "https")) {
			return error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
		} else if (menu.getMenuId().equals(menu.getParentId())) {
			return error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
		}
		menu.setUpdateBy(getUsername());
		return toAjax(menuService.updateMenu(menu));
	}

	/**
	 * 删除菜单
	 */
	@AuthPermissions("system:menu:remove")
	@Log(title = "菜单管理", businessType = BusinessType.DELETE)
	@Delete
	@Mapping("{menuId}")
	public AjaxResult remove(@Path("menuId") Long menuId) {
		if (menuService.hasChildByMenuId(menuId)) {
			return warn("存在子菜单,不允许删除");
		}
		if (menuService.checkMenuExistRole(menuId)) {
			return warn("菜单已分配,不允许删除");
		}
		return toAjax(menuService.deleteMenuById(menuId));
	}
}