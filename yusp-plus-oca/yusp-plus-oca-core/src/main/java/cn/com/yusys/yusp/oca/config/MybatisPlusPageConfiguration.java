/**
 * Copyright (C), 2014-2021
 * FileName: MybatisPlusPageConfiguration
 * Author: Administrator
 * Date: 2021/3/18 19:47
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Administrator 19:47 1.0.0 新建类
 */

package cn.com.yusys.yusp.oca.config;

import cn.com.yusys.yusp.commons.autoconfigure.mybatis.MybatisAutoConfiguration;
import cn.com.yusys.yusp.commons.autoconfigure.mybatis.PageHelperProperties;
import cn.com.yusys.yusp.commons.autoconfigure.mybatis.plus.MybatisPlusAutoConfiguration;
import cn.com.yusys.yusp.commons.autoconfigure.mybatis.plus.MybatisPlusProperties;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Mybatisplus分页配置
 *
 * @author Administrator
 * @date 2021/3/18
 */

@Configuration
@AutoConfigureAfter({MybatisAutoConfiguration.class, MybatisAutoConfiguration.SequenceAdapterConfiguration.class})
@AutoConfigureBefore({MybatisPlusAutoConfiguration.class})
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class, MybatisSqlSessionFactoryBean.class})
@EnableConfigurationProperties({MybatisPlusProperties.class, PageHelperProperties.class})
public class MybatisPlusPageConfiguration {

    @Bean
    @Order(-20)
    public InnerInterceptor paginationInnerInterceptor(MybatisPlusProperties properties) {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(properties.getPage().getMaxLimit());
        paginationInnerInterceptor.setOverflow(properties.getPage().isOverflow());
        return paginationInnerInterceptor;
    }
}
