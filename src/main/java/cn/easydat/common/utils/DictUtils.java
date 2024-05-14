package cn.easydat.common.utils;

import java.util.List;

import org.noear.solon.Solon;

import cn.easydat.common.constant.CacheConstant;
import cn.easydat.system.domain.SysDictData;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 字典工具类
 */
public class DictUtils {
	/**
	 * 分隔符
	 */
	public static final String SEPARATOR = ",";

	/**
	 * 设置字典缓存
	 * 
	 * @param key       参数键
	 * @param dictDatas 字典数据列表
	 */
	public static void setDictCache(String key, List<SysDictData> dictDatas) {
		Solon.context().getBean(RedisUtil.class).setCacheObject(getCacheKey(key), dictDatas);
	}

	/**
	 * 获取字典缓存
	 * 
	 * @param key 参数键
	 * @return dictDatas 字典数据列表
	 */
	public static List<SysDictData> getDictCache(String key) {
		List<SysDictData> sysDictDatas = Solon.context().getBean(RedisUtil.class).getCacheObject(getCacheKey(key));
		return sysDictDatas;
	}

	/**
	 * 根据字典类型和字典值获取字典标签
	 * 
	 * @param dictType  字典类型
	 * @param dictValue 字典值
	 * @return 字典标签
	 */
	public static String getDictLabel(String dictType, String dictValue) {
		return getDictLabel(dictType, dictValue, SEPARATOR);
	}

	/**
	 * 根据字典类型和字典标签获取字典值
	 * 
	 * @param dictType  字典类型
	 * @param dictLabel 字典标签
	 * @return 字典值
	 */
	public static String getDictValue(String dictType, String dictLabel) {
		return getDictValue(dictType, dictLabel, SEPARATOR);
	}

	/**
	 * 根据字典类型和字典值获取字典标签
	 * 
	 * @param dictType  字典类型
	 * @param dictValue 字典值
	 * @param separator 分隔符
	 * @return 字典标签
	 */
	public static String getDictLabel(String dictType, String dictValue, String separator) {
		StringBuilder propertyString = new StringBuilder();
		List<SysDictData> datas = getDictCache(dictType);

		if (null != datas) {
			if (StrUtil.containsAny(separator, dictValue)) {
				for (SysDictData dict : datas) {
					for (String value : dictValue.split(separator)) {
						if (value.equals(dict.getDictValue())) {
							propertyString.append(dict.getDictLabel()).append(separator);
							break;
						}
					}
				}
			} else {
				for (SysDictData dict : datas) {
					if (dictValue.equals(dict.getDictValue())) {
						return dict.getDictLabel();
					}
				}
			}
		}
		return StrUtil.stripIgnoreCase(propertyString, null, separator);
	}

	/**
	 * 根据字典类型和字典标签获取字典值
	 * 
	 * @param dictType  字典类型
	 * @param dictLabel 字典标签
	 * @param separator 分隔符
	 * @return 字典值
	 */
	public static String getDictValue(String dictType, String dictLabel, String separator) {
		StringBuilder propertyString = new StringBuilder();
		List<SysDictData> datas = getDictCache(dictType);

		if (StrUtil.containsAny(separator, dictLabel) && CollectionUtil.isNotEmpty(datas)) {
			for (SysDictData dict : datas) {
				for (String label : dictLabel.split(separator)) {
					if (label.equals(dict.getDictLabel())) {
						propertyString.append(dict.getDictValue()).append(separator);
						break;
					}
				}
			}
		} else {
			for (SysDictData dict : datas) {
				if (dictLabel.equals(dict.getDictLabel())) {
					return dict.getDictValue();
				}
			}
		}
		return StrUtil.stripIgnoreCase(propertyString, null, separator);
	}

	/**
	 * 删除指定字典缓存
	 * 
	 * @param key 字典键
	 */
	public static void removeDictCache(String key) {
		Solon.context().getBean(RedisUtil.class).deleteCacheObject(getCacheKey(key));
	}

	/**
	 * 清空字典缓存
	 */
	public static void clearDictCache() {
		Solon.context().getBean(RedisUtil.class).deleteCacheObjectAll(CacheConstant.SYS_DICT_KEY + "*");
	}

	/**
	 * 设置cache key
	 * 
	 * @param configKey 参数键
	 * @return 缓存键key
	 */
	public static String getCacheKey(String configKey) {
		return CacheConstant.SYS_DICT_KEY + configKey;
	}
}
