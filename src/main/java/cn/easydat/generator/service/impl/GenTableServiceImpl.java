package cn.easydat.generator.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;
import org.noear.solon.data.annotation.Tran;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.easydat.common.utils.SecurityUtil;
import cn.easydat.generator.config.GenConfig;
import cn.easydat.generator.constant.GenConstants;
import cn.easydat.generator.domain.GenTable;
import cn.easydat.generator.domain.GenTableColumn;
import cn.easydat.generator.mapper.GenTableColumnMapper;
import cn.easydat.generator.mapper.GenTableMapper;
import cn.easydat.generator.service.GenTableService;
import cn.easydat.generator.util.GenUtils;
import cn.easydat.generator.util.VelocityInitializer;
import cn.easydat.generator.util.VelocityUtils;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 业务 服务层实现
 */
@Component
public class GenTableServiceImpl implements GenTableService {
	private static final Logger log = LoggerFactory.getLogger(GenTableServiceImpl.class);

	@Inject
	private GenTableMapper genTableMapper;

	@Inject
	private GenTableColumnMapper genTableColumnMapper;
	
	@Inject
	private GenConfig genConfig;

	/**
	 * 查询业务信息
	 * 
	 * @param id 业务ID
	 * @return 业务信息
	 */
	@Override
	public GenTable selectGenTableById(Long id) {
		GenTable genTable = genTableMapper.selectGenTableById(id);
		setTableFromOptions(genTable);
		return genTable;
	}

	/**
	 * 查询业务列表
	 * 
	 * @param genTable 业务信息
	 * @return 业务集合
	 */
	@Override
	public List<GenTable> selectGenTableList(GenTable genTable) {
		return genTableMapper.selectGenTableList(genTable);
	}

	/**
	 * 查询据库列表
	 * 
	 * @param genTable 业务信息
	 * @return 数据库表集合
	 */
	@Override
	public List<GenTable> selectDbTableList(GenTable genTable) {
		return genTableMapper.selectDbTableList(genTable);
	}

	/**
	 * 查询据库列表
	 * 
	 * @param tableNames 表名称组
	 * @return 数据库表集合
	 */
	@Override
	public List<GenTable> selectDbTableListByNames(String[] tableNames) {
		return genTableMapper.selectDbTableListByNames(tableNames);
	}

	/**
	 * 查询所有表信息
	 * 
	 * @return 表信息集合
	 */
	@Override
	public List<GenTable> selectGenTableAll() {
		return genTableMapper.selectGenTableAll();
	}

	/**
	 * 修改业务
	 * 
	 * @param genTable 业务信息
	 * @return 结果
	 */
	@Override
	@Tran
	public void updateGenTable(GenTable genTable) {
		String options = JSONUtil.toJsonStr(genTable.getParams());
		genTable.setOptions(options);
		int row = genTableMapper.updateGenTable(genTable);
		if (row > 0) {
			for (GenTableColumn cenTableColumn : genTable.getColumns()) {
				genTableColumnMapper.updateGenTableColumn(cenTableColumn);
			}
		}
	}

	/**
	 * 删除业务对象
	 * 
	 * @param tableIds 需要删除的数据ID
	 * @return 结果
	 */
	@Override
	@Tran
	public void deleteGenTableByIds(Long[] tableIds) {
		genTableMapper.deleteGenTableByIds(tableIds);
		genTableColumnMapper.deleteGenTableColumnByIds(tableIds);
	}

	/**
	 * 导入表结构
	 * 
	 * @param tableList 导入表列表
	 */
	@Override
	@Tran
	public void importGenTable(List<GenTable> tableList) {
		String operName = SecurityUtil.getUserName();
		try {
			for (GenTable table : tableList) {
				String tableName = table.getTableName();
				GenUtils.initTable(table, operName, genConfig);
				int row = genTableMapper.insertGenTable(table);
				if (row > 0) {
					// 保存列信息
					List<GenTableColumn> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
					for (GenTableColumn column : genTableColumns) {
						GenUtils.initColumnField(column, table);
						genTableColumnMapper.insertGenTableColumn(column);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("导入失败",e);
		}
	}

	/**
	 * 预览代码
	 * 
	 * @param tableId 表编号
	 * @return 预览数据列表
	 */
	@Override
	public Map<String, String> previewCode(Long tableId) {
		Map<String, String> dataMap = new LinkedHashMap<>();
		// 查询表信息
		GenTable table = genTableMapper.selectGenTableById(tableId);
		// 设置主子表信息
		setSubTable(table);
		// 设置主键列信息
		setPkColumn(table);
		VelocityInitializer.initVelocity();

		VelocityContext context = VelocityUtils.prepareContext(table);

		// 获取模板列表
		List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory(), table.getTplWebType());
		for (String template : templates) {
			// 渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, "UTF-8");
			tpl.merge(context, sw);
			dataMap.put(template, sw.toString());
		}
		return dataMap;
	}

	/**
	 * 生成代码（下载方式）
	 * 
	 * @param tableName 表名称
	 * @return 数据
	 */
	@Override
	public byte[] downloadCode(String tableName) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		generatorCode(tableName, zip);
		IoUtil.close(zip);
		return outputStream.toByteArray();
	}

	/**
	 * 生成代码（自定义路径）
	 * 
	 * @param tableName 表名称
	 */
	@Override
	public void generatorCode(String tableName) {
		// 查询表信息
		GenTable table = genTableMapper.selectGenTableByName(tableName);
		// 设置主子表信息
		setSubTable(table);
		// 设置主键列信息
		setPkColumn(table);

		VelocityInitializer.initVelocity();

		VelocityContext context = VelocityUtils.prepareContext(table);

		// 获取模板列表
		List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory(), table.getTplWebType());
		for (String template : templates) {
			if (!StrUtil.containsAny(template, "sql.vm", "api.js.vm", "index.vue.vm", "index-tree.vue.vm")) {
				// 渲染模板
				StringWriter sw = new StringWriter();
				Template tpl = Velocity.getTemplate(template, "UTF-8");
				tpl.merge(context, sw);
				String path = getGenPath(table, template);
				FileUtil.writeString(sw.toString(), new File(path), "UTF-8");
			}
		}
	}

	/**
	 * 同步数据库
	 * 
	 * @param tableName 表名称
	 */
	@Override
	@Tran
	public void synchDb(String tableName) {
		GenTable table = genTableMapper.selectGenTableByName(tableName);
		List<GenTableColumn> tableColumns = table.getColumns();
		Map<String, GenTableColumn> tableColumnMap = tableColumns.stream().collect(Collectors.toMap(GenTableColumn::getColumnName, Function.identity()));

		List<GenTableColumn> dbTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
		if (null == dbTableColumns || dbTableColumns.isEmpty()) {
			throw new RuntimeException("同步数据失败，原表结构不存在");
		}
		List<String> dbTableColumnNames = dbTableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

		dbTableColumns.forEach(column -> {
			GenUtils.initColumnField(column, table);
			if (tableColumnMap.containsKey(column.getColumnName())) {
				GenTableColumn prevColumn = tableColumnMap.get(column.getColumnName());
				column.setColumnId(prevColumn.getColumnId());
				if (column.isList()) {
					// 如果是列表，继续保留查询方式/字典类型选项
					column.setDictType(prevColumn.getDictType());
					column.setQueryType(prevColumn.getQueryType());
				}
				if (StrUtil.isNotEmpty(prevColumn.getIsRequired()) && !column.isPk() && (column.isInsert() || column.isEdit()) && ((column.isUsableColumn()) || (!column.isSuperColumn()))) {
					// 如果是(新增/修改&非主键/非忽略及父属性)，继续保留必填/显示类型选项
					column.setIsRequired(prevColumn.getIsRequired());
					column.setHtmlType(prevColumn.getHtmlType());
				}
				genTableColumnMapper.updateGenTableColumn(column);
			} else {
				genTableColumnMapper.insertGenTableColumn(column);
			}
		});

		List<GenTableColumn> delColumns = tableColumns.stream().filter(column -> !dbTableColumnNames.contains(column.getColumnName())).collect(Collectors.toList());
		if (null != delColumns && !delColumns.isEmpty()) {
			genTableColumnMapper.deleteGenTableColumns(delColumns);
		}
	}

	/**
	 * 批量生成代码（下载方式）
	 * 
	 * @param tableNames 表数组
	 * @return 数据
	 */
	@Override
	public byte[] downloadCode(String[] tableNames) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		for (String tableName : tableNames) {
			generatorCode(tableName, zip);
		}
		IoUtil.close(zip);
		return outputStream.toByteArray();
	}

	/**
	 * 查询表信息并生成代码
	 */
	private void generatorCode(String tableName, ZipOutputStream zip) {
		// 查询表信息
		GenTable table = genTableMapper.selectGenTableByName(tableName);
		// 设置主子表信息
		setSubTable(table);
		// 设置主键列信息
		setPkColumn(table);

		VelocityInitializer.initVelocity();

		VelocityContext context = VelocityUtils.prepareContext(table);

		// 获取模板列表
		List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory(), table.getTplWebType());
		for (String template : templates) {
			// 渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, "UTF-8");
			tpl.merge(context, sw);
			try {
				// 添加到zip
				zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));

				IoUtil.write(zip, Charset.forName("UTF-8"), false, sw.toString());
				IoUtil.close(sw);
				zip.flush();
				zip.closeEntry();
			} catch (IOException e) {
				log.error("渲染模板失败，表名：" + table.getTableName(), e);
			}
		}
	}

	/**
	 * 修改保存参数校验
	 * 
	 * @param genTable 业务信息
	 */
	@Override
	public void validateEdit(GenTable genTable) {
		if (GenConstants.TPL_TREE.equals(genTable.getTplCategory())) {
			String options = JSONUtil.toJsonStr(genTable.getParams());
			JSONObject paramsObj = JSONUtil.parseObj(options);
			if (StrUtil.isEmpty(paramsObj.getStr(GenConstants.TREE_CODE))) {
				throw new RuntimeException("树编码字段不能为空");
			} else if (StrUtil.isEmpty(paramsObj.getStr(GenConstants.TREE_PARENT_CODE))) {
				throw new RuntimeException("树父编码字段不能为空");
			} else if (StrUtil.isEmpty(paramsObj.getStr(GenConstants.TREE_NAME))) {
				throw new RuntimeException("树名称字段不能为空");
			} else if (GenConstants.TPL_SUB.equals(genTable.getTplCategory())) {
				if (StrUtil.isEmpty(genTable.getSubTableName())) {
					throw new RuntimeException("关联子表的表名不能为空");
				} else if (StrUtil.isEmpty(genTable.getSubTableFkName())) {
					throw new RuntimeException("子表关联的外键名不能为空");
				}
			}
		}
	}

	/**
	 * 设置主键列信息
	 * 
	 * @param table 业务表信息
	 */
	public void setPkColumn(GenTable table) {
		for (GenTableColumn column : table.getColumns()) {
			if (column.isPk()) {
				table.setPkColumn(column);
				break;
			}
		}
		if (null == table.getPkColumn()) {
			table.setPkColumn(table.getColumns().get(0));
		}
		if (GenConstants.TPL_SUB.equals(table.getTplCategory())) {
			for (GenTableColumn column : table.getSubTable().getColumns()) {
				if (column.isPk()) {
					table.getSubTable().setPkColumn(column);
					break;
				}
			}
			if (null == table.getSubTable().getPkColumn()) {
				table.getSubTable().setPkColumn(table.getSubTable().getColumns().get(0));
			}
		}
	}

	/**
	 * 设置主子表信息
	 * 
	 * @param table 业务表信息
	 */
	public void setSubTable(GenTable table) {
		String subTableName = table.getSubTableName();
		if (StrUtil.isNotEmpty(subTableName)) {
			table.setSubTable(genTableMapper.selectGenTableByName(subTableName));
		}
	}

	/**
	 * 设置代码生成其他选项值
	 * 
	 * @param genTable 设置后的生成对象
	 */
	public void setTableFromOptions(GenTable genTable) {
		JSONObject paramsObj = JSONUtil.parseObj(genTable.getOptions());
		if (null != paramsObj) {
			String treeCode = paramsObj.getStr(GenConstants.TREE_CODE);
			String treeParentCode = paramsObj.getStr(GenConstants.TREE_PARENT_CODE);
			String treeName = paramsObj.getStr(GenConstants.TREE_NAME);
			String parentMenuId = paramsObj.getStr(GenConstants.PARENT_MENU_ID);
			String parentMenuName = paramsObj.getStr(GenConstants.PARENT_MENU_NAME);

			genTable.setTreeCode(treeCode);
			genTable.setTreeParentCode(treeParentCode);
			genTable.setTreeName(treeName);
			genTable.setParentMenuId(parentMenuId);
			genTable.setParentMenuName(parentMenuName);
		}
	}

	/**
	 * 获取代码生成地址
	 * 
	 * @param table    业务表信息
	 * @param template 模板文件路径
	 * @return 生成地址
	 */
	public static String getGenPath(GenTable table, String template) {
		String genPath = table.getGenPath();
		if (StrUtil.equals(genPath, "/")) {
			return System.getProperty("user.dir") + File.separator + "src" + File.separator + VelocityUtils.getFileName(template, table);
		}
		return genPath + File.separator + VelocityUtils.getFileName(template, table);
	}
}