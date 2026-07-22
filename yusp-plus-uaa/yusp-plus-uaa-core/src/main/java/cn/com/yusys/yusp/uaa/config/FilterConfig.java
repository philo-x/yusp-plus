package cn.com.yusys.yusp.uaa.config;

import cn.com.yusys.yusp.commons.starter.config.WebProperties;
import cn.com.yusys.yusp.uaa.filter.SingleSessionFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

/**
 * 配置获取token拦截的过滤器，设置顺序为1，必须在AccessFilter之前执行，否则拿不到userId,权限拦截失败
 *
 * @author danyu
 * @auhtor yinjun
 * @date 2021-07-03
 */
@Configuration
public class FilterConfig {

    public static final int FIRST_ORDER = 10;

    private final JwtDecoder jwtDecoder;

    public FilterConfig(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Bean
    public FilterRegistrationBean<Filter> singleSessionFilter(WebProperties webProperties, ObjectMapper objectMapper) {
        FilterRegistrationBean<Filter> registration;
        registration = new FilterRegistrationBean<>(new SingleSessionFilter(objectMapper, jwtDecoder));
        registration.setOrder(FIRST_ORDER - 11);
        registration.addUrlPatterns("/api/*");
        registration.addInitParameter(SingleSessionFilter.IGNORE_URLS_KEY, webProperties.getIgnoreUrls());
        return registration;
    }
}
