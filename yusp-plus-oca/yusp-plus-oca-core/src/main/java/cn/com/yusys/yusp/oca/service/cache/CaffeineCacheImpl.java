package cn.com.yusys.yusp.oca.service.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * cache 的caffeine实现
 *
 * @author lty
 * @since 2021/6/29
 */
public class CaffeineCacheImpl extends AbstractCustomCacheService {

    private static final Logger log = LoggerFactory.getLogger(CaffeineCacheImpl.class);

    @Autowired
    SingletonBeanRegistry singletonBeanRegistry;

    public void initStringBean(String name, int expireTime) {
        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterAccess(expireTime, TimeUnit.SECONDS)
                .maximumSize(50000)
                .build();
        singletonBeanRegistry.registerSingleton(name, cache);
    }

    public void initHashBean(String name, int expireTime) {
        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterAccess(expireTime, TimeUnit.SECONDS)
                .maximumSize(16)
                .build();
        singletonBeanRegistry.registerSingleton(name, cache);
    }

    /**
     * String 类型的缓存put操作
     *
     * @param name       缓存名称
     * @param key        缓存 key
     * @param value      缓存值
     * @param expireTime 过期时间
     */
    @Override
    public void stringPut(String name, String key, String value, int expireTime) {
        name = getResetName(name);
        if (null == singletonBeanRegistry.getSingleton(name)) {
            initStringBean(name, expireTime);
        }
        Cache cache = (Cache) singletonBeanRegistry.getSingleton(name);
        cache.put(key, value);
    }

    @Override
    public void hashPut(String name, String key, Map<String, String> value, int expireTime) {
        name = getResetName(name);
        if (null == singletonBeanRegistry.getSingleton(name)) {
            initHashBean(name, expireTime);
        }
        Cache cache = (Cache) singletonBeanRegistry.getSingleton(name);
        cache.put(key, value);

        log.error(cache.getIfPresent(key).toString());
    }

    @Override
    public String stringGet(String name, String key) {
        name = getResetName(name);
        Cache cache = (Cache) singletonBeanRegistry.getSingleton(name);
        if (null == cache) {
            return null;
        }
        String ifPresent = (String) cache.getIfPresent(key);
        return ifPresent;
    }

    @Override
    public String hashGet(String name, String key, String hashKey) {
        name = getResetName(name);
        Cache cache = (Cache) singletonBeanRegistry.getSingleton(name);
        if (null == cache) {
            return null;
        }
        Map<String, String> ifPresent = (Map<String, String>) cache.getIfPresent(key);
        if (ifPresent == null) {
            return null;
        }
        return ifPresent.get(hashKey);
    }

    @Override
    public Map<String, String> hashGet(String name, String key) {
        name = getResetName(name);
        Cache cache = (Cache) singletonBeanRegistry.getSingleton(name);
        if (null == cache) {
            return null;
        }
        Map<String, String> ifPresent = (Map<String, String>) cache.getIfPresent(key);
        return ifPresent;
    }

    @Override
    public void clear(String name, String key) {
        name = getResetName(name);
        Cache cache = (Cache) singletonBeanRegistry.getSingleton(name);
        if (null == cache) {
            return;
        }
        cache.invalidate(key);
    }

    /**
     * 清楚缓存
     *
     * @param name-缓存名称
     * @param dataGroup-缓存数据分组
     * @param key-缓存key
     */
    @Override
    public void clear(String name, String dataGroup, String key) {
        name = name + ":" + dataGroup;
        Cache cache = (Cache) singletonBeanRegistry.getSingleton(name);
        if (null == cache) {
            return;
        }
        cache.invalidate(key);
    }

    /**
     * Hash 类型的缓存put操作
     *
     * @param name       缓存名称
     * @param dataGroup  -缓存数据分组
     * @param key        缓存 key
     * @param value      缓存值
     * @param expireTime 过期时间
     */
    @Override
    public void hashPut(String name, String dataGroup, String key, Map<String, String> value, int expireTime) {
        name = name + ":" + dataGroup;
        if (null == singletonBeanRegistry.getSingleton(name)) {
            initHashBean(name, expireTime);
        }
        Cache cache = (Cache) singletonBeanRegistry.getSingleton(name);
        cache.put(key, value);

        log.error(cache.getIfPresent(key).toString());
    }

    /**
     * Hash类型的缓存delete小key
     *
     * @param name     缓存名称
     * @param hashKey  缓存key
     * @param smallKey 缓存值map中的key
     */
    @Override
    public void hashKeyDelete(String name, String hashKey, String smallKey) {
        name = getResetName(name);
        Cache cache = (Cache) singletonBeanRegistry.getSingleton(name);
        if (null == cache) {
            return;
        }
        Map<String, String> ifPresent = (Map<String, String>) cache.getIfPresent(hashKey);
        if (ifPresent == null) {
            return;
        }
        ifPresent.remove(hashKey);
    }

    @Override
    public void hashPut(String key, Map<String, String> value, int expireTime) {
        // Caffeine 不支持该情况，如有需求再添加
    }

    @Override
    public String hashSingleKeyGet(String key, String hashKey) {
        // Caffeine 不支持该情况，如有需求再添加
        return null;
    }

    @Override
    public void clear(String key) {
        // Caffeine 不支持该情况，如有需求再添加
    }

    @Override
    public int nameCountKey(String name) {
        return 0;
    }


}
