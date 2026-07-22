package cn.com.yusys.yusp.oca.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 登录成功注解
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RestApiResponseAdvice.class})
public @interface LoginSuccessStrategy {
}
