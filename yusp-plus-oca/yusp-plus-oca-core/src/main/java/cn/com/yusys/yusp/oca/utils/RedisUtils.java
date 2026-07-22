package cn.com.yusys.yusp.oca.utils;

import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: yusp-plus
 * @description: Redis工具类
 * @author: wujiangpeng
 * @email: wujp4@yusys.com.cn
 * @create: 2021-02-20 16:09
 */
@Component
public class RedisUtils {

    private final StringRedisTemplate stringRedisTemplate;

    private final CustomCacheService customCacheService;

    public RedisUtils(StringRedisTemplate stringRedisTemplate, CustomCacheService customCacheService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.customCacheService = customCacheService;
    }

    /**
     * 删除redis中Hash结构缓存，如果删除失败，点击界面上手动刷新缓存
     */
    public void delRedisCache(String redisKey) {
        HashOperations<String, Object, Object> operations = stringRedisTemplate.opsForHash();
        //2.使用scan查询,再删除
        Cursor<Map.Entry<Object, Object>> dictCursor = operations.scan(redisKey, ScanOptions.NONE);
        StringBuffer stringBuffer = new StringBuffer();
        AtomicInteger atomicInt = new AtomicInteger(0);
        while (dictCursor.hasNext()) {
            int i = atomicInt.addAndGet(1);
            stringBuffer.append(dictCursor.next().getKey() + ",");
            if (i % 50 == 0 || !dictCursor.hasNext()) {
                operations.delete(redisKey, stringBuffer.toString().split(","));
            }
        }
        //3.lua 脚本删除
    }

    /**
     * 添加redis Hash结构缓存
     */
    public void addRedisCache(Map<String, String> map, String redisKey) {
        customCacheService.hashPut(redisKey,redisKey,map,24*60*60);
    }
}
