package cn.com.yusys.yusp.oca.config;

import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import cn.com.yusys.yusp.oca.service.cache.RedisCacheImpl;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义缓存配置，两种不同的实现
 * 1. 默认redis缓存
 * 2. 若不用redis开启Caffeine缓存，需要引入相应依赖
 * <dependency>
 *             <groupId>com.github.ben-manes.caffeine</groupId>
 *             <artifactId>caffeine</artifactId>
 *             <version>2.6.2</version>
 *         </dependency>
 *
 * @author lty
 * @date 2020-11-26 14:46:26
 */
@Configuration
public class CacheConfig {

    @Bean
    public CustomCacheService cache(){

        return new RedisCacheImpl();
    }

//    @Bean
//    public CustomCacheService cache(){
//
//        return new CaffeineCacheImpl();
//    }

    @Bean
    public SingletonBeanRegistry singletonBeanRegistry(){
        return new DefaultSingletonBeanRegistry();
    }
}
