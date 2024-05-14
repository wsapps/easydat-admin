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
import cn.easydat.common.core.page.TableDataInfo;
import cn.easydat.common.enums.BusinessType;
import cn.easydat.system.domain.SysDictType;
import cn.easydat.system.service.SysDictTypeService;

/**
 * 数据字典信息
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("system/dict/type")
public class SysDictTypeController extends AjaxResultBaseController {
	@Inject
	private SysDictTypeService dictTypeService;

	@AuthPermissions("system:dict:list")
	@Get
	@Mapping("list")
	public TableDataInfo<SysDictType> list(SysDictType dictType) {
		startPage();
		List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
		return getDataTable(list);
	}

	@AuthPermissions("system:dict:export")
	@Log(title = "字典类型", businessType = BusinessType.EXPORT)
	@Post
	@Mapping("export")
	public void export(SysDictType dictType) {
		List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
		export(list);
	}

	/**
	 * 查询字典类型详细
	 */
	@AuthPermissions("system:dict:query")
	@Get
	@Mapping("{dictId}")
	public AjaxResult getInfo(@Path Long dictId) {
		return success(dictTypeService.selectDictTypeById(dictId));
	}

	/**
	 * 新增字典类型
	 */
	@AuthPermissions("system:dict:add")
	@Log(title = "字典类型", businessType = BusinessType.INSERT)
	@Post
	@Mapping
	public AjaxResult add(@Validated @Body SysDictType dict) {
		if (!dictTypeService.checkDictTypeUnique(dict)) {
			return error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
		}
		dict.setCreateBy(getUsername());
		return toAjax(dictTypeService.insertDictType(dict));
	}

	/**
	 * 修改字典类型
	 */
	@AuthPermissions("system:dict:edit")
	@Log(title = "字典类型", businessType = BusinessType.UPDATE)
	@Put
	@Mapping
	public AjaxResult edit(@Validated @Body SysDictType dict) {
		if (!dictTypeService.checkDictTypeUnique(dict)) {
			return error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
		}
		dict.setUpdateBy(getUsername());
		return toAjax(dictTypeService.updateDictType(dict));
	}

	/**
	 * 删除字典类型
	 */
	@AuthPermissions("system:dict:remove")
	@Log(title = "字典类型", businessType = BusinessType.DELETE)
	@Delete
	@Mapping("{dictIds}")
	public AjaxResult remove(@Path Long[] dictIds) {
		dictTypeService.deleteDictTypeByIds(dictIds);
		return success();
	}

	/**
	 * 刷新字典缓存
	 */
	@AuthPermissions("system:dict:remove")
	@Log(title = "字典类型", businessType = BusinessType.CLEAN)
	@Delete
	@Mapping("refreshCache")
	public AjaxResult refreshCache() {
		dictTypeService.resetDictCache();
		return success();
	}

	/**
	 * 获取字典选择框列表
	 */
	@Get
	@Mapping("optionselect")
	public AjaxResult optionselect() {
		List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
		return success(dictTypes);
	}
}
