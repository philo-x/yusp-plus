package cn.com.yusys.yusp.oca.annotation;

import java.lang.annotation.*;

/**
 * 登录前注解
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginBeforeStrategy {
    String loginCode () default "";
}
