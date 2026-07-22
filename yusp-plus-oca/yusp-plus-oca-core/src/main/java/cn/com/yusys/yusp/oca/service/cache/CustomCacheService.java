package cn.com.yusys.yusp.oca.service.cache;

import java.util.Map;

/**
 * 自定义缓存接口，oca中缓存统一使用该接口
 * @author yusys
 */
public interface CustomCacheService {

    /**
     * String 类型的缓存put操作
     * @param name 缓存名称
     * @param key 缓存 key
     * @param value 缓存值
     * @param expireTime 过期时间
     */
    void stringPut(String name,String key,String value,int expireTime);

    /**
     * Hash 类型的缓存put操作
     * @param name 缓存名称
     * @param key 缓存 key
     * @param value 缓存值
     * @param expireTime 过期时间
     */
    void hashPut(String name, String key, Map<String,String> value, int expireTime);


    /**
     * String 类型缓存获取
     * @param name 缓存名称
     * @param key 缓存 key
     * @return
     */
    String stringGet(String name, String key);

    /**
     * Hash 类型缓存获取
     * @param name 缓存名称
     * @param key 缓存 key
     * @param hashKey 缓存 Hash 中的key
     * @return
     */
    String hashGet(String name, String key, String hashKey);

    /**
     * Hash 类型获取整个缓存hash对象
     * @param name 缓存名称
     * @param key 缓存key
     * @return
     */
    Map hashGet(String name, String key);

    /**
     * 清除缓存
     * @param name 缓存名称
     * @param key 缓存key
     */
    void clear(String name, String key);

    /**
     * 清楚缓存
     * @param name-缓存名称
     * @param dataGroup-缓存数据分组
     * @param key-缓存key
     */
    void clear(String name,String dataGroup,String key);


    /**
     * Hash 类型的缓存put操作
     * @param name 缓存名称
     * @param dataGroup -缓存数据分组
     * @param key 缓存 key
     * @param value 缓存值
     * @param expireTime 过期时间
     */
    void hashPut(String name, String dataGroup,String key, Map<String,String> value, int expireTime);

    /**
     * Hash类型的缓存delete小key
     * @param name 缓存名称
     * @param hashKey 缓存key
     * @param smallKey 缓存值map中的key
     */
    void hashKeyDelete(String name,String hashKey,String smallKey);

    /**
     * 单层key的情况
     * @param key 缓存key
     * @param value 缓存值
     * @param expireTime 过期时间
     */
    void hashPut(String key,Map<String,String> value, int expireTime);

    /**
     * 单层key Hash 类型缓存获取
     * @param key 缓存 key
     * @param hashKey 缓存 Hash 中的key
     * @return
     */
    String hashSingleKeyGet(String key, String hashKey);

    /**
     * 单层key-清除缓存
     * @param key 缓存key
     */
    void clear(String key);

    /**
     * 获取缓存名称下有多少个key
     * @param name 缓存名称
     * @return key的个数
     */
    int nameCountKey(String name);
}
