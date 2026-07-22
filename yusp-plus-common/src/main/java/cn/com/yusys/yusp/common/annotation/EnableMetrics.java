package cn.com.yusys.yusp.common.annotation;

import cn.com.yusys.yusp.common.aspect.MetricsAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启日志切面，此注解添加到Spring配置类上
 *
 * @author danyubin
 * @date 2020-11-26 14:46:26
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@Import({MetricsAspect.class})
public @interface EnableMetrics {
}
