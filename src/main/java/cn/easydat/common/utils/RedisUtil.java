package cn.easydat.common.utils;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;

/**
 * Redis操作
 */
public class RedisUtil {

    RedissonClient redissonClient;

    public RedisUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    // ============================================= Object ============================================================
    /**
     * 缓存基本的对象，String、Integer、实体类等
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        RBucket<T> bucket = redissonClient.getBucket(key, new JsonJacksonCodec());
        bucket.set(value);
    }

    /**
     * 缓存基本的对象，String、Integer、实体类等
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param duration 有效时间
     */
    public <T> void setCacheObject(final String key, final T value, final Duration duration) {
        RBucket<T> bucket = redissonClient.getBucket(key, new JsonJacksonCodec());
        bucket.set(value);
        bucket.expire(duration);
    }

    /**
     * 获取缓存的基本对象
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        RBucket<T> bucket = redissonClient.getBucket(key, new JsonJacksonCodec());
        return bucket.get();
    }

    /**
     * 获取缓存的基本对象剩余时间
     * @param key
     * @param <T>
     * @return 剩余时间（单位：毫秒）
     */
    public <T> long getCacheObjectExpire(final String key) {
        RBucket<T> bucket = redissonClient.getBucket(key, new JsonJacksonCodec());
        return bucket.remainTimeToLive() == -2 ? 0L : bucket.remainTimeToLive();
    }

    /**
     * 删除缓存的基本对象
     * @param key 缓存的键值
     * @return 缓存的对象
     */
    public <T> boolean deleteCacheObject(final String key) {
        RBucket<T> bucket = redissonClient.getBucket(key, new JsonJacksonCodec());
        return bucket.delete();
    }

    /**
     * 删除所有缓存的基本对象
     * @param pattern 缓存的键值正则表达式
     * @return 缓存的对象
     */
    public <T> void deleteCacheObjectAll(final String pattern) {
        Iterable<String> keysByPattern = getKeys(pattern);
        for (String key : keysByPattern) {
            RBucket<T> bucket = redissonClient.getBucket(key, new JsonJacksonCodec());
            bucket.delete();
        }
    }

    // ============================================== List =============================================================
    /**
     * 缓存List数据（注意是addAll，一直往list中存放数据）
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> boolean setCacheList(final String key, final List<T> dataList) {
        RList<T> list = redissonClient.getList(key, new JsonJacksonCodec());
        return list.addAll(dataList);
    }

    /**
     * 缓存List数据（注意是addAll，一直往list中存放数据）
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @param duration 有效时间
     * @return 缓存的对象
     */
    public <T> boolean setCacheList(final String key, final List<T> dataList, final Duration duration) {
        RList<T> list = redissonClient.getList(key, new JsonJacksonCodec());
        boolean b = list.addAll(dataList);
        boolean expire = list.expire(duration);
        return b && expire;
    }

    /**
     * 获取缓存的List数据
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        RList<T> list = redissonClient.getList(key, new JsonJacksonCodec());
        return list.readAll();
    }

    /**
     * 删除缓存的List数据
     * @param key 缓存的键值
     * @return 缓存的对象
     */
    public <T> boolean deleteCacheList(final String key) {
        RList<T> list = redissonClient.getList(key, new JsonJacksonCodec());
        return list.delete();
    }

    /**
     * 删除所有缓存的List数据
     * @param pattern 缓存的键值正则表达式
     * @return 缓存的对象
     */
    public <T> void deleteCacheListAll(final String pattern) {
        Iterable<String> keysByPattern = getKeys(pattern);
        for (String key : keysByPattern) {
            RList<T> list = redissonClient.getList(key, new JsonJacksonCodec());
            list.delete();
        }
    }

    // ============================================== Set ==============================================================
    /**
     * 缓存Set数据（注意是addAll，一直往set中存放数据）
     * @param key 缓存的键值
     * @param dataSet 待缓存的Set数据
     * @return 缓存的对象
     */
    public <T> boolean setCacheSet(final String key, final Set<T> dataSet) {
        RSet<T> set = redissonClient.getSet(key, new JsonJacksonCodec());
        return set.addAll(dataSet);
    }

    /**
     * 缓存Set数据（注意是addAll，一直往set中存放数据）
     * @param key 缓存的键值
     * @param dataSet 待缓存的Set数据
     * @param duration 有效时间
     * @return 缓存的对象
     */
    public <T> boolean setCacheSet(final String key, final Set<T> dataSet, final Duration duration) {
        RSet<T> set = redissonClient.getSet(key, new JsonJacksonCodec());
        boolean b = set.addAll(dataSet);
        boolean expire = set.expire(duration);
        return b && expire;
    }

    /**
     * 获取缓存的Set数据
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> Set<T> getCacheSet(final String key) {
        RSet<T> set = redissonClient.getSet(key, new JsonJacksonCodec());
        return set.readAll();
    }

    /**
     * 删除缓存的Set数据
     * @param key 缓存的键值
     * @return 缓存的对象
     */
    public <T> boolean deleteCacheSet(final String key) {
        RSet<T> set = redissonClient.getSet(key, new JsonJacksonCodec());
        return set.delete();
    }

    // ============================================== Map ==============================================================
    /**
     * 缓存Map数据（注意是putAll，一直往map中存放数据）
     * @param key 缓存的键值
     * @param dataMap 待缓存的Map数据
     * @return 缓存的对象
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        RMap<String, T> map = redissonClient.getMap(key, new JsonJacksonCodec());
        map.putAll(dataMap);
    }

    /**
     * 缓存Map数据（注意是putAll，一直往map中存放数据）
     * @param key 缓存的键值
     * @param dataMap 待缓存的Map数据
     * @param duration 有效时间
     * @return 缓存的对象
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap, final Duration duration) {
        RMap<String, T> map = redissonClient.getMap(key, new JsonJacksonCodec());
        map.putAll(dataMap);
        map.expire(duration);
    }

    /**
     * 获取缓存的Map数据
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        RMap<String, T> map = redissonClient.getMap(key, new JsonJacksonCodec());
        return map.readAllMap();
    }

    /**
     * 删除缓存的Map数据
     * @param key 缓存的键值
     * @return 缓存的对象
     */
    public <T> boolean deleteCacheMap(final String key) {
        RMap<String, T> map = redissonClient.getMap(key, new JsonJacksonCodec());
        return map.delete();
    }

    /**
     * 正则匹配所有key
     * @param pattern 缓存的键值正则表达式
     * @return
     */
    private Iterable<String> getKeys(final String pattern) {
        RKeys keys = redissonClient.getKeys();
        return keys.getKeysByPattern(pattern);
    }

}
