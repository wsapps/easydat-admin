package cn.easydat.system.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.ibatis.solon.annotation.Db;
import org.noear.solon.annotation.Component;
import org.noear.solon.data.annotation.Tran;

import cn.easydat.common.constant.UserConstant;
import cn.easydat.common.utils.DictUtils;
import cn.easydat.system.domain.SysDictData;
import cn.easydat.system.domain.SysDictType;
import cn.easydat.system.mapper.SysDictDataMapper;
import cn.easydat.system.mapper.SysDictTypeMapper;
import cn.easydat.system.service.SysDictTypeService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * 字典 业务层处理
 */
@Component
public class SysDictTypeServiceImpl implements SysDictTypeService {
	@Db
	private SysDictTypeMapper dictTypeMapper;

	@Db
	private SysDictDataMapper dictDataMapper;

	/**
	 * 根据条件分页查询字典类型
	 * 
	 * @param dictType 字典类型信息
	 * @return 字典类型集合信息
	 */
	@Override
	public List<SysDictType> selectDictTypeList(SysDictType dictType) {
		return dictTypeMapper.selectDictTypeList(dictType);
	}

	/**
	 * 根据所有字典类型
	 * 
	 * @return 字典类型集合信息
	 */
	@Override
	public List<SysDictType> selectDictTypeAll() {
		return dictTypeMapper.selectDictTypeAll();
	}

	/**
	 * 根据字典类型查询字典数据
	 * 
	 * @param dictType 字典类型
	 * @return 字典数据集合信息
	 */
	@Override
	public List<SysDictData> selectDictDataByType(String dictType) {
		List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
		if (CollectionUtil.isNotEmpty(dictDatas)) {
			return dictDatas;
		}
		dictDatas = dictDataMapper.selectDictDataByType(dictType);
		if (CollectionUtil.isNotEmpty(dictDatas)) {
			DictUtils.setDictCache(dictType, dictDatas);
			return dictDatas;
		}
		return null;
	}

	/**
	 * 根据字典类型ID查询信息
	 * 
	 * @param dictId 字典类型ID
	 * @return 字典类型
	 */
	@Override
	public SysDictType selectDictTypeById(Long dictId) {
		return dictTypeMapper.selectDictTypeById(dictId);
	}

	/**
	 * 根据字典类型查询信息
	 * 
	 * @param dictType 字典类型
	 * @return 字典类型
	 */
	@Override
	public SysDictType selectDictTypeByType(String dictType) {
		return dictTypeMapper.selectDictTypeByType(dictType);
	}

	/**
	 * 批量删除字典类型信息
	 * 
	 * @param dictIds 需要删除的字典ID
	 */
	@Override
	public void deleteDictTypeByIds(Long[] dictIds) {
		for (Long dictId : dictIds) {
			SysDictType dictType = selectDictTypeById(dictId);
			if (dictDataMapper.countDictDataByType(dictType.getDictType()) > 0) {
				throw new RuntimeException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
			}
			dictTypeMapper.deleteDictTypeById(dictId);
			DictUtils.removeDictCache(dictType.getDictType());
		}
	}

	/**
	 * 加载字典缓存数据
	 */
	@Override
	public void loadingDictCache() {
		SysDictData dictData = new SysDictData();
		dictData.setStatus("0");
		Map<String, List<SysDictData>> dictDataMap = dictDataMapper.selectDictDataList(dictData).stream().collect(Collectors.groupingBy(SysDictData::getDictType));
		for (Map.Entry<String, List<SysDictData>> entry : dictDataMap.entrySet()) {
			DictUtils.setDictCache(entry.getKey(), entry.getValue().stream().sorted(Comparator.comparing(SysDictData::getDictSort)).collect(Collectors.toList()));
		}
	}

	/**
	 * 清空字典缓存数据
	 */
	@Override
	public void clearDictCache() {
		DictUtils.clearDictCache();
	}

	/**
	 * 重置字典缓存数据
	 */
	@Override
	public void resetDictCache() {
		clearDictCache();
		loadingDictCache();
	}

	/**
	 * 新增保存字典类型信息
	 * 
	 * @param dict 字典类型信息
	 * @return 结果
	 */
	@Override
	public int insertDictType(SysDictType dict) {
		int row = dictTypeMapper.insertDictType(dict);
		if (row > 0) {
			DictUtils.setDictCache(dict.getDictType(), null);
		}
		return row;
	}

	/**
	 * 修改保存字典类型信息
	 * 
	 * @param dict 字典类型信息
	 * @return 结果
	 */
	@Override
	@Tran
	public int updateDictType(SysDictType dict) {
		SysDictType oldDict = dictTypeMapper.selectDictTypeById(dict.getDictId());
		dictDataMapper.updateDictDataType(oldDict.getDictType(), dict.getDictType());
		int row = dictTypeMapper.updateDictType(dict);
		if (row > 0) {
			List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(dict.getDictType());
			DictUtils.setDictCache(dict.getDictType(), dictDatas);
		}
		return row;
	}

	/**
	 * 校验字典类型称是否唯一
	 * 
	 * @param dict 字典类型
	 * @return 结果
	 */
	@Override
	public boolean checkDictTypeUnique(SysDictType dict) {
		Long dictId = null == dict.getDictId() ? -1L : dict.getDictId();
		SysDictType dictType = dictTypeMapper.checkDictTypeUnique(dict.getDictType());
		if (ObjectUtil.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue()) {
			return UserConstant.NOT_UNIQUE;
		}
		return UserConstant.UNIQUE;
	}
}
