package cn.easydat.generator.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.noear.solon.core.handle.Context;
import org.noear.solon.validation.annotation.Validated;

import cn.easydat.common.annotation.Log;
import cn.easydat.common.core.controller.AjaxResultBaseController;
import cn.easydat.common.core.domain.AjaxResult;
import cn.easydat.common.core.page.TableDataInfo;
import cn.easydat.common.enums.BusinessType;
import cn.easydat.generator.domain.GenTable;
import cn.easydat.generator.domain.GenTableColumn;
import cn.easydat.generator.service.GenTableColumnService;
import cn.easydat.generator.service.GenTableService;
import cn.hutool.core.convert.Convert;

/**
 * 代码生成 操作处理
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("/tool/gen")
public class GenController extends AjaxResultBaseController {
	@Inject
	private GenTableService genTableService;

	@Inject
	private GenTableColumnService genTableColumnService;

	/**
	 * 查询代码生成列表
	 */
	@AuthPermissions("tool:gen:list")
	@Get
	@Mapping("/list")
	public TableDataInfo<GenTable> genList(GenTable genTable) {
		startPage();
		List<GenTable> list = genTableService.selectGenTableList(genTable);
		return getDataTable(list);
	}

	/**
	 * 修改代码生成业务
	 */
	@AuthPermissions("tool:gen:query")
	@Get
	@Mapping(value = "/{tableId}")
	public AjaxResult getInfo(@Path Long tableId) {
		GenTable table = genTableService.selectGenTableById(tableId);
		List<GenTable> tables = genTableService.selectGenTableAll();
		List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("info", table);
		map.put("rows", list);
		map.put("tables", tables);
		return success(map);
	}

	/**
	 * 查询数据库列表
	 */
	@AuthPermissions("tool:gen:list")
	@Get
	@Mapping("/db/list")
	public TableDataInfo<GenTable> dataList(GenTable genTable) {
		startPage();
		List<GenTable> list = genTableService.selectDbTableList(genTable);
		return getDataTable(list);
	}

	/**
	 * 查询数据表字段列表
	 */
	@AuthPermissions("tool:gen:list")
	@Get
	@Mapping(value = "/column/{tableId}")
	public TableDataInfo<GenTableColumn> columnList(Long tableId) {
		TableDataInfo<GenTableColumn> dataInfo = new TableDataInfo<GenTableColumn>();
		List<GenTableColumn> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
		dataInfo.setRows(list);
		dataInfo.setTotal(list.size());
		return dataInfo;
	}

	/**
	 * 导入表结构（保存）
	 */
	@AuthPermissions("tool:gen:import")
	@Log(title = "代码生成", businessType = BusinessType.IMPORT)
	@Post
	@Mapping("/importTable")
	public AjaxResult importTableSave(String tables) {
		String[] tableNames = Convert.toStrArray(tables);
		// 查询表信息
		List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
		genTableService.importGenTable(tableList);
		return success();
	}

	/**
	 * 修改保存代码生成业务
	 */
	@AuthPermissions("tool:gen:edit")
	@Log(title = "代码生成", businessType = BusinessType.UPDATE)
	@Put
	@Mapping
	public AjaxResult editSave(@Validated @Body GenTable genTable) {
		genTableService.validateEdit(genTable);
		genTableService.updateGenTable(genTable);
		return success();
	}

	/**
	 * 删除代码生成
	 */
	@AuthPermissions("tool:gen:remove")
	@Log(title = "代码生成", businessType = BusinessType.DELETE)
	@Delete
	@Mapping("/{tableIds}")
	public AjaxResult remove(@Path Long[] tableIds) {
		genTableService.deleteGenTableByIds(tableIds);
		return success();
	}

	/**
	 * 预览代码
	 */
	@AuthPermissions("tool:gen:preview")
	@Get
	@Mapping("/preview/{tableId}")
	public AjaxResult preview(@Path("tableId") Long tableId) throws IOException {
		Map<String, String> dataMap = genTableService.previewCode(tableId);
		return success(dataMap);
	}

	/**
	 * 生成代码（下载方式）
	 */
	@AuthPermissions("tool:gen:code")
	@Log(title = "代码生成", businessType = BusinessType.GENCODE)
	@Get
	@Mapping("/download/{tableName}")
	public void download(@Path("tableName") String tableName) throws IOException {
		byte[] data = genTableService.downloadCode(tableName);
		genCode(data);
	}

	/**
	 * 生成代码（自定义路径）
	 */
	@AuthPermissions("tool:gen:code")
	@Log(title = "代码生成", businessType = BusinessType.GENCODE)
	@Get
	@Mapping("/genCode/{tableName}")
	public AjaxResult genCode(@Path("tableName") String tableName) {
		genTableService.generatorCode(tableName);
		return success();
	}

	/**
	 * 同步数据库
	 */
	@AuthPermissions("tool:gen:edit")
	@Log(title = "代码生成", businessType = BusinessType.UPDATE)
	@Get
	@Mapping("/synchDb/{tableName}")
	public AjaxResult synchDb(@Path("tableName") String tableName) {
		genTableService.synchDb(tableName);
		return success();
	}

	/**
	 * 批量生成代码
	 */
	@AuthPermissions("tool:gen:code")
	@Log(title = "代码生成", businessType = BusinessType.GENCODE)
	@Get
	@Mapping("/batchGenCode")
	public void batchGenCode(String tables) throws IOException {
		String[] tableNames = Convert.toStrArray(tables);
		byte[] data = genTableService.downloadCode(tableNames);
		genCode(data);
	}

	/**
	 * 生成zip文件
	 */
	private void genCode(byte[] data) throws IOException {
		Context.current().headerAdd("Access-Control-Allow-Origin", "*");
		Context.current().headerAdd("Access-Control-Expose-Headers", "Content-Disposition");
		Context.current().headerSet("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
		Context.current().headerAdd("Content-Length", "" + data.length);
		Context.current().contentType("application/octet-stream; charset=UTF-8");
		Context.current().output(data);
	}
}