package cn.com.yusys.yusp.oca.config;

import cn.com.yusys.yusp.oca.interceptor.LoginInterceptor;
import cn.com.yusys.yusp.oca.interceptor.RequestFilter;
import cn.com.yusys.yusp.oca.service.AdminSmCrelStraService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import cn.com.yusys.yusp.oca.utils.I18nMessageByCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 策略配置
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Configuration
public class StrategyConfig implements WebMvcConfigurer {
    @Autowired
    private AdminSmCrelStraService adminSmCrelStraService;
    @Autowired
    private CustomCacheService customCacheService;
    @Autowired
    private I18nMessageByCode i18nMessageByCode;

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(new LoginInterceptor(adminSmCrelStraService, customCacheService, i18nMessageByCode)).addPathPatterns("/api/login/**");
    }

    @Bean
    LoginInterceptor loginInterceptor() {
        return new LoginInterceptor(adminSmCrelStraService, customCacheService, i18nMessageByCode);
    }

    @Bean
    public FilterRegistrationBean httpServletRequestReplacedRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RequestFilter());
        registration.addUrlPatterns("/api/login/*");
        registration.setOrder(1);
        return registration;
    }
}
