package cn.easydat.system.service.impl;

import java.util.List;

import org.apache.ibatis.solon.annotation.Db;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Inject;

import cn.easydat.common.constant.CacheConstant;
import cn.easydat.common.constant.UserConstant;
import cn.easydat.common.utils.RedisUtil;
import cn.easydat.system.domain.SysConfig;
import cn.easydat.system.mapper.SysConfigMapper;
import cn.easydat.system.service.SysConfigService;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 参数配置 服务层实现
 */
@Component
public class SysConfigServiceImpl implements SysConfigService {
	@Db
	private SysConfigMapper configMapper;

	@Inject
	private RedisUtil redisCache;

	/**
	 * 查询参数配置信息
	 * 
	 * @param configId 参数配置ID
	 * @return 参数配置信息
	 */
	@Override
	public SysConfig selectConfigById(Long configId) {
		SysConfig config = new SysConfig();
		config.setConfigId(configId);
		return configMapper.selectConfig(config);
	}

	/**
	 * 根据键名查询参数配置信息
	 * 
	 * @param configKey 参数key
	 * @return 参数键值
	 */
	@Override
	public String selectConfigByKey(String configKey) {
		String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
		if (StrUtil.isNotEmpty(configValue)) {
			return configValue;
		}
		SysConfig config = new SysConfig();
		config.setConfigKey(configKey);
		SysConfig retConfig = configMapper.selectConfig(config);
		if (ObjectUtil.isNotNull(retConfig)) {
			redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
			return retConfig.getConfigValue();
		}
		return StrUtil.EMPTY;
	}

	/**
	 * 获取验证码开关
	 * 
	 * @return true开启，false关闭
	 */
	@Override
	public boolean selectCaptchaEnabled() {
		String captchaEnabled = selectConfigByKey("sys.account.captchaEnabled");
		if (StrUtil.isEmpty(captchaEnabled)) {
			return true;
		}
		return Convert.toBool(captchaEnabled);
	}

	/**
	 * 查询参数配置列表
	 * 
	 * @param config 参数配置信息
	 * @return 参数配置集合
	 */
	@Override
	public List<SysConfig> selectConfigList(SysConfig config) {
		return configMapper.selectConfigList(config);
	}

	/**
	 * 新增参数配置
	 * 
	 * @param config 参数配置信息
	 * @return 结果
	 */
	@Override
	public int insertConfig(SysConfig config) {
		int row = configMapper.insertConfig(config);
		if (row > 0) {
			redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
		}
		return row;
	}

	/**
	 * 修改参数配置
	 * 
	 * @param config 参数配置信息
	 * @return 结果
	 */
	@Override
	public int updateConfig(SysConfig config) {
		SysConfig temp = configMapper.selectConfigById(config.getConfigId());
		if (!StrUtil.equals(temp.getConfigKey(), config.getConfigKey())) {
			redisCache.deleteCacheObject(getCacheKey(temp.getConfigKey()));
		}

		int row = configMapper.updateConfig(config);
		if (row > 0) {
			redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
		}
		return row;
	}

	/**
	 * 批量删除参数信息
	 * 
	 * @param configIds 需要删除的参数ID
	 */
	@Override
	public void deleteConfigByIds(Long[] configIds) {
		for (Long configId : configIds) {
			SysConfig config = selectConfigById(configId);
			if (StrUtil.equals(UserConstant.YES, config.getConfigType())) {
				throw new RuntimeException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
			}
			configMapper.deleteConfigById(configId);
			redisCache.deleteCacheObject(getCacheKey(config.getConfigKey()));
		}
	}

	/**
	 * 加载参数缓存数据
	 */
	@Override
	public void loadingConfigCache() {
		List<SysConfig> configsList = configMapper.selectConfigList(new SysConfig());
		for (SysConfig config : configsList) {
			redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
		}
	}

	/**
	 * 清空参数缓存数据
	 */
	@Override
	public void clearConfigCache() {
		redisCache.deleteCacheObjectAll(CacheConstant.SYS_CONFIG_KEY + "*");
	}

	/**
	 * 重置参数缓存数据
	 */
	@Override
	public void resetConfigCache() {
		clearConfigCache();
		loadingConfigCache();
	}

	/**
	 * 校验参数键名是否唯一
	 * 
	 * @param config 参数配置信息
	 * @return 结果
	 */
	@Override
	public boolean checkConfigKeyUnique(SysConfig config) {
		Long configId = null == config.getConfigId() ? -1L : config.getConfigId();
		SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
		if (ObjectUtil.isNotNull(info) && info.getConfigId().longValue() != configId.longValue()) {
			return UserConstant.NOT_UNIQUE;
		}
		return UserConstant.UNIQUE;
	}

	/**
	 * 设置cache key
	 * 
	 * @param configKey 参数键
	 * @return 缓存键key
	 */
	private String getCacheKey(String configKey) {
		return CacheConstant.SYS_CONFIG_KEY + configKey;
	}
}
