package cn.com.yusys.yusp.oca.service.cache;

import cn.com.yusys.yusp.commons.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * cache的redis 实现
 *
 * @Author lty
 * @Date 2021/6/29
 */
public class RedisCacheImpl extends AbstractCustomCacheService {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheImpl.class);

    private static final String ERROR_MSG_NAME = "redis cacheName can not be null";

    private static final String ERROR_MSG_KEY = "redis cacheKey can not be null";

    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * String 类型的缓存put操作
     * @param name 缓存名称
     * @param key 缓存 key
     * @param value 缓存值
     * @param expireTime 过期时间
     */
    @Override
    public void stringPut(String name, String key, String value, int expireTime) {
        Asserts.nonBlank(name, ERROR_MSG_NAME);
        Asserts.nonBlank(key, ERROR_MSG_KEY);
        name=getResetName(name);
        redisTemplate.opsForValue().set(name + ":" + key, value, expireTime, TimeUnit.SECONDS);

    }

    @Override
    public void hashPut(String name, String key, Map value, int expireTime) {
        Asserts.nonBlank(name, ERROR_MSG_NAME);
        Asserts.nonBlank(key, ERROR_MSG_KEY);
        name=getResetName(name);
        redisTemplate.opsForHash().getOperations().boundHashOps(name + ":" + key).putAll(value);
        redisTemplate.expire(name + ":" + key, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public String stringGet(String name, String key) {
        Asserts.nonBlank(name, ERROR_MSG_NAME);
        Asserts.nonBlank(key, ERROR_MSG_KEY);
        name=getResetName(name);
        return (String) redisTemplate.opsForValue().get(name + ":" + key);
    }

    @Override
    public String hashGet(String name, String key, String hashKey) {
        Asserts.nonBlank(name, ERROR_MSG_NAME);
        Asserts.nonBlank(key, ERROR_MSG_KEY);
        name=getResetName(name);
        return (String) redisTemplate.opsForHash().get(name + ":" + key, hashKey);
    }

    @Override
    public Map hashGet(String name, String key) {
        Asserts.nonBlank(name, ERROR_MSG_NAME);
        Asserts.nonBlank(key, ERROR_MSG_KEY);
        name=getResetName(name);
        return redisTemplate.opsForHash().entries(name + ":" + key);
    }

    @Override
    public void clear(String name, String key) {
        Asserts.nonBlank(name, ERROR_MSG_NAME);
        Asserts.nonBlank(key, ERROR_MSG_KEY);
        name=getResetName(name);
        redisTemplate.delete(name + ":" + key);
    }
    /**
     * 清楚缓存
     * @param name-缓存名称
     * @param dataGroup-缓存数据分组
     * @param key-缓存key
     */
    @Override
    public void clear(String name, String dataGroup, String key) {
        name=name+":"+dataGroup;
        Asserts.nonBlank(name, ERROR_MSG_NAME);
        Asserts.nonBlank(key, ERROR_MSG_KEY);
        redisTemplate.delete(name + ":" + key);
    }
    /**
     * Hash 类型的缓存put操作
     * @param name 缓存名称
     * @param dataGroup -缓存数据分组
     * @param key 缓存 key
     * @param value 缓存值
     * @param expireTime 过期时间
     */
    @Override
    public void hashPut(String name, String dataGroup, String key, Map<String, String> value, int expireTime) {
        name=name+":"+dataGroup;
        Asserts.nonBlank(name, ERROR_MSG_NAME);
        Asserts.nonBlank(key, ERROR_MSG_KEY);
        redisTemplate.opsForHash().getOperations().boundHashOps(name + ":" + key).putAll(value);
        redisTemplate.expire(name + ":" + key, expireTime, TimeUnit.SECONDS);
    }

    /**
     * Hash类型的缓存delete小key
     * @param name 缓存名称
     * @param hashKey 缓存key
     * @param smallKey 缓存值map中的key
     */
    @Override
    public void hashKeyDelete(String name, String hashKey, String smallKey) {
        Asserts.nonBlank(name, ERROR_MSG_NAME);
        Asserts.nonBlank(hashKey, ERROR_MSG_KEY);
        name=getResetName(name);
        redisTemplate.opsForHash().getOperations().boundHashOps(name + ":" + hashKey).delete(smallKey);
    }

    @Override
    public void hashPut(String key, Map<String, String> value, int expireTime) {
        Asserts.nonBlank(key, ERROR_MSG_KEY);
        redisTemplate.opsForHash().getOperations().boundHashOps(key).putAll(value);
        if(expireTime>0){
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }

    }

    @Override
    public String hashSingleKeyGet(String key, String hashKey) {
        Asserts.nonBlank(key, ERROR_MSG_KEY);
        return (String) redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public void clear(String key) {
        Asserts.nonBlank(key, ERROR_MSG_KEY);
        redisTemplate.delete(key);
    }

    @Override
    public int nameCountKey(String name) {
        Asserts.nonBlank(name, ERROR_MSG_KEY);
        return redisTemplate.keys(name).size();

    }

}
