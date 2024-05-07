package cn.easydat.system.domain;

import java.util.ArrayList;
import java.util.List;

import org.noear.solon.validation.annotation.Length;
import org.noear.solon.validation.annotation.NotBlank;
import org.noear.solon.validation.annotation.NotNull;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;

import cn.easydat.common.core.domain.BaseEntity;
import cn.hutool.json.JSONUtil;

/**
 * 菜单权限表 sys_menu
 * 
 * @author ruoyi
 */
public class SysMenu extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** 菜单ID */
	@Id
	private Long menuId;

	/** 菜单名称 */
	@NotBlank(message = "菜单名称不能为空")
	@Length(max = 50, message = "菜单名称长度不能超过50个字符")
	private String menuName;

	/** 父菜单名称 */
	private String parentName;

	/** 父菜单ID */
	@Column(ignore = true)
	private Long parentId;

	/** 显示顺序 */
	@NotNull(message = "显示顺序不能为空")
	private Integer orderNum;

	/** 路由地址 */
	@Length(max = 200, message = "路由地址不能超过200个字符")
	private String path;

	/** 组件路径 */
	@Length(max = 200, message = "组件路径不能超过200个字符")
	private String component;

	/** 路由参数 */
	private String query;

	/** 是否为外链（0是 1否） */
	private String isFrame;

	/** 是否缓存（0缓存 1不缓存） */
	private String isCache;

	/** 类型（M目录 C菜单 F按钮） */
	@NotBlank(message = "菜单名称不能为空")
	private String menuType;

	/** 显示状态（0显示 1隐藏） */
	private String visible;

	/** 菜单状态（0正常 1停用） */
	private String status;

	/** 权限字符串 */
	@Length(max = 100, message = "权限标识长度不能超过100个字符")
	private String perms;

	/** 菜单图标 */
	private String icon;

	/** 子菜单 */
	@Column(ignore = true)
	private List<SysMenu> children = new ArrayList<SysMenu>();

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getIsFrame() {
		return isFrame;
	}

	public void setIsFrame(String isFrame) {
		this.isFrame = isFrame;
	}

	public String getIsCache() {
		return isCache;
	}

	public void setIsCache(String isCache) {
		this.isCache = isCache;
	}

	@NotBlank(message = "菜单类型不能为空")
	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<SysMenu> getChildren() {
		return children;
	}

	public void setChildren(List<SysMenu> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return JSONUtil.toJsonStr(this);
	}

}
