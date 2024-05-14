package cn.easydat.system.controller;

import java.util.ArrayList;
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
import cn.easydat.system.domain.SysDictData;
import cn.easydat.system.service.SysDictDataService;
import cn.easydat.system.service.SysDictTypeService;

/**
 * 数据字典信息
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("system/dict/data")
public class SysDictDataController extends AjaxResultBaseController {
	@Inject
	private SysDictDataService dictDataService;

	@Inject
	private SysDictTypeService dictTypeService;

	@AuthPermissions("system:dict:list")
	@Get
	@Mapping("list")
	public TableDataInfo<SysDictData> list(SysDictData dictData) {
		startPage();
		List<SysDictData> list = dictDataService.selectDictDataList(dictData);
		return getDataTable(list);
	}

	@AuthPermissions("system:dict:export")
	@Log(title = "字典数据", businessType = BusinessType.EXPORT)
	@Post
	@Mapping("export")
	public void export(SysDictData dictData) {
		List<SysDictData> list = dictDataService.selectDictDataList(dictData);
		export(list);
	}

	/**
	 * 查询字典数据详细
	 */
	@AuthPermissions("system:dict:query")
	@Get
	@Mapping("{dictCode}")
	public AjaxResult getInfo(@Path Long dictCode) {
		return success(dictDataService.selectDictDataById(dictCode));
	}

	/**
	 * 根据字典类型查询字典数据信息
	 */
	@Get
	@Mapping("type/{dictType}")
	public AjaxResult dictType(@Path String dictType) {
		List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
		if (null == data) {
			data = new ArrayList<SysDictData>();
		}
		return success(data);
	}

	/**
	 * 新增字典类型
	 */
	@AuthPermissions("system:dict:add")
	@Log(title = "字典数据", businessType = BusinessType.INSERT)
	@Post
	@Mapping
	public AjaxResult add(@Validated @Body SysDictData dict) {
		dict.setCreateBy(getUsername());
		return toAjax(dictDataService.insertDictData(dict));
	}

	/**
	 * 修改保存字典类型
	 */
	@AuthPermissions("system:dict:edit")
	@Log(title = "字典数据", businessType = BusinessType.UPDATE)
	@Put
	@Mapping
	public AjaxResult edit(@Validated @Body SysDictData dict) {
		dict.setUpdateBy(getUsername());
		return toAjax(dictDataService.updateDictData(dict));
	}

	/**
	 * 删除字典类型
	 */
	@AuthPermissions("system:dict:remove")
	@Log(title = "字典类型", businessType = BusinessType.DELETE)
	@Delete
	@Mapping("{dictCodes}")
	public AjaxResult remove(@Path Long[] dictCodes) {
		dictDataService.deleteDictDataByIds(dictCodes);
		return success();
	}
}
