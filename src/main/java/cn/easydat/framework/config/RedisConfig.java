package cn.easydat.framework.config;

import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.redisson.api.RedissonClient;
import org.redisson.solon.RedissonSupplier;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.SaTokenDaoRedissonJackson;
import cn.easydat.common.utils.RedisUtil;

/**
 * Redis配置
 */
@Configuration
public class RedisConfig {

	/**
	 * RedissonClient
	 * 
	 * @param supplier
	 * @return
	 */
	@Bean
	public RedissonClient redissonClient(@Inject("${redis.cache_source}") RedissonSupplier supplier) {
		return supplier.get();
	}

	/**
	 * SaTokenDao
	 * 
	 * @param redissonClient
	 * @return
	 */
	@Bean
	@Condition(onClass = RedissonClient.class)
	public SaTokenDao saTokenDaoInit(RedissonClient redissonClient) {
		return new SaTokenDaoRedissonJackson(redissonClient);
	}

	/**
	 * RedisUtil
	 * 
	 * @return
	 */
	@Bean
	@Condition(onClass = RedissonClient.class)
	public RedisUtil redisUtil(RedissonClient redissonClient) {
		return new RedisUtil(redissonClient);
	}

}
