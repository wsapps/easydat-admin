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
import cn.easydat.system.domain.SysDept;
import cn.easydat.system.service.SysDeptService;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 部门信息
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("/system/dept")
public class SysDeptController extends AjaxResultBaseController {
	@Inject
	private SysDeptService deptService;

	/**
	 * 获取部门列表
	 */
	@AuthPermissions("system:dept:list")
	@Get
	@Mapping("list")
	public AjaxResult list(SysDept dept) {
		List<SysDept> depts = deptService.selectDeptList(dept);
		return success(depts);
	}

	/**
	 * 查询部门列表（排除节点）
	 */
	@AuthPermissions("system:dept:list")
	@Get
	@Mapping("list/exclude/{deptId}")
	public AjaxResult excludeChild(@Path(value = "deptId") Long deptId) {
		List<SysDept> depts = deptService.selectDeptList(new SysDept());
		depts.removeIf(d -> d.getDeptId().intValue() == deptId || ArrayUtil.contains(StrUtil.splitToArray(d.getAncestors(), ","), deptId + ""));
		return success(depts);
	}

	/**
	 * 根据部门编号获取详细信息
	 */
	@AuthPermissions("system:dept:query")
	@Get
	@Mapping("{deptId}")
	public AjaxResult getInfo(@Path Long deptId) {
		deptService.checkDeptDataScope(deptId);
		return success(deptService.selectDeptById(deptId));
	}

	/**
	 * 新增部门
	 */
	@AuthPermissions("system:dept:add")
	@Log(title = "部门管理", businessType = BusinessType.INSERT)
	@Post
	@Mapping
	public AjaxResult add(@Body @Validated SysDept dept) {
		if (!deptService.checkDeptNameUnique(dept)) {
			return error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
		}
		dept.setCreateBy(getUsername());
		return toAjax(deptService.insertDept(dept));
	}

	/**
	 * 修改部门
	 */
	@AuthPermissions("system:dept:edit")
	@Log(title = "部门管理", businessType = BusinessType.UPDATE)
	@Put
	@Mapping
	public AjaxResult edit(@Body @Validated SysDept dept) {
		Long deptId = dept.getDeptId();
		deptService.checkDeptDataScope(deptId);
		if (!deptService.checkDeptNameUnique(dept)) {
			return error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
		} else if (dept.getParentId().equals(deptId)) {
			return error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
		} else if (StrUtil.equals("1", dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0) {
			return error("该部门包含未停用的子部门！");
		}
		dept.setUpdateBy(getUsername());
		return toAjax(deptService.updateDept(dept));
	}

	/**
	 * 删除部门
	 */
	@AuthPermissions("system:dept:remove")
	@Log(title = "部门管理", businessType = BusinessType.DELETE)
	@Delete
	@Mapping("{deptId}")
	public AjaxResult remove(@Path Long deptId) {
		if (deptService.hasChildByDeptId(deptId)) {
			return warn("存在下级部门,不允许删除");
		}
		if (deptService.checkDeptExistUser(deptId)) {
			return warn("部门存在用户,不允许删除");
		}
		deptService.checkDeptDataScope(deptId);
		return toAjax(deptService.deleteDeptById(deptId));
	}
}
