package cn.easydat.system.controller.monitor;

import java.util.ArrayList;
import java.util.List;

import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Delete;
import org.noear.solon.annotation.Get;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Path;
import org.noear.solon.auth.annotation.AuthPermissions;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import cn.easydat.common.constant.CacheConstant;
import cn.easydat.common.core.domain.AjaxResult;
import cn.easydat.system.domain.SysCache;

/**
 * 缓存监控
 */
@SuppressWarnings("deprecation")
@Controller
@Mapping("/monitor/cache")
public class CacheController {
	@Inject
	private RedissonClient redissonClient;

	private final static List<SysCache> caches = new ArrayList<SysCache>();
	{
		caches.add(new SysCache(CacheConstant.LOGIN_USER_KEY, "用户信息"));
		caches.add(new SysCache(CacheConstant.SYS_CONFIG_KEY, "配置信息"));
		caches.add(new SysCache(CacheConstant.SYS_DICT_KEY, "数据字典"));
		caches.add(new SysCache(CacheConstant.CAPTCHA_CODE_KEY, "验证码"));
		caches.add(new SysCache(CacheConstant.REPEAT_SUBMIT_KEY, "防重提交"));
		caches.add(new SysCache(CacheConstant.RATE_LIMIT_KEY, "限流处理"));
		caches.add(new SysCache(CacheConstant.PWD_ERR_CNT_KEY, "密码错误次数"));
	}

	@AuthPermissions("monitor:cache:list")
	@Get
	@Mapping
	public AjaxResult getInfo() throws Exception {
		//TODO reids info
//		Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
//		Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
//		Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());
//
//		Map<String, Object> result = new HashMap<>(3);
//		result.put("info", info);
//		result.put("dbSize", dbSize);
//
//		List<Map<String, String>> pieList = new ArrayList<>();
//		commandStats.stringPropertyNames().forEach(key -> {
//			Map<String, String> data = new HashMap<>(2);
//			String property = commandStats.getProperty(key);
//			data.put("name", StrUtil.removePrefix(key, "cmdstat_"));
//			data.put("value", StrUtil.subBetween(property, "calls=", ",usec"));
//			pieList.add(data);
//		});
//		result.put("commandStats", pieList);
//		return AjaxResult.success(result);
		return AjaxResult.success();
	}

	@AuthPermissions("monitor:cache:list")
	@Get
	@Mapping("/getNames")
	public AjaxResult cache() {
		return AjaxResult.success(caches);
	}

	@AuthPermissions("monitor:cache:list")
	@Get
	@Mapping("/getKeys/{cacheName}")
	public AjaxResult getCacheKeys(@Path String cacheName) {
		Iterable<String> cacheKeys = redissonClient.getKeys().getKeysByPattern(cacheName + "*");
		return AjaxResult.success(cacheKeys);
	}

	@AuthPermissions("monitor:cache:list")
	@Get
	@Mapping("/getValue/{cacheName}/{cacheKey}")
	public AjaxResult getCacheValue(@Path String cacheName, @Path String cacheKey) {
		redissonClient.getBucket(cacheKey).get();
		RBucket<String> rBucket = redissonClient.getBucket(cacheKey);
		SysCache sysCache = new SysCache(cacheName, cacheKey, rBucket.get());
		return AjaxResult.success(sysCache);
	}

	@AuthPermissions("monitor:cache:list")
	@Delete
	@Mapping("/clearCacheName/{cacheName}")
	public AjaxResult clearCacheName(@Path String cacheName) {
		Iterable<String> cacheKeys = redissonClient.getKeys().getKeysByPattern(cacheName + "*");
		for (String key : cacheKeys) {
			RBucket<?> bucket = redissonClient.getBucket(key);
            bucket.delete();
		}
		return AjaxResult.success();
	}

	@AuthPermissions("monitor:cache:list")
	@Delete
	@Mapping("/clearCacheKey/{cacheKey}")
	public AjaxResult clearCacheKey(@Path String cacheKey) {
		RBucket<?> bucket = redissonClient.getBucket(cacheKey);
        bucket.delete();
		return AjaxResult.success();
	}

	@AuthPermissions("monitor:cache:list")
	@Delete
	@Mapping("/clearCacheAll")
	public AjaxResult clearCacheAll() {
		Iterable<String> cacheKeys = redissonClient.getKeys().getKeysByPattern("*");
		for (String key : cacheKeys) {
			RBucket<?> bucket = redissonClient.getBucket(key);
            bucket.delete();
		}
		return AjaxResult.success();
	}
}
