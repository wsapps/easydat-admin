package cn.easydat;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	public static void main(String[] args) {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		String host = "47.97.105.19";
		int port = 10086;
		String password = "easydat#2024";
		int database = 2;
		
		jedisClusterNodes.add(new HostAndPort(host, port));
//		1
//		JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().password(password).build();
//		ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();
//		JedisCluster jedis = new JedisCluster(jedisClusterNodes, null,password);
		
//		2
//		JedisCluster jedis = new JedisCluster(jedisClusterNodes);
		HostAndPort hostAndPort = new HostAndPort(host, port);
		JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().password(password).database(database).build();
		JedisPool jedisPool = new JedisPool(hostAndPort,clientConfig);
		
		Jedis jedis = jedisPool.getResource();
		jedis.info();
		System.out.println(jedis.info());
		
		jedisPool.close();
	}
}
