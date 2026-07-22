package cn.com.yusys.yusp.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志增强注解类，定义了注解的若干属性，可以标记在类和方法上
 * 默认开启入参打印、出参打印、成功埋点、失败埋点
 *
 * @author danyubin
 * @date 2020-11-26 14:46:26
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Metrics {

    /**
     * 是否在成功执行方法后打点
     *
     * @return true 记录 false 不记录
     */
    boolean recordSuccessMetrics() default true;

    /**
     * 是否在执行方法出错时打点
     *
     * @return true 记录 false 不记录
     */
    boolean recordFailMetrics() default true;

    /**
     * 是否记录请求参数
     *
     * @return true 记录 false 不记录
     */
    boolean logParameters() default false;

    /**
     * 是否记录返回值
     *
     * @return true 记录 false 不记录
     */
    boolean logReturn() default true;

    /**
     * 是否记录异常
     *
     * @return true 记录 false 不记录
     */
    boolean logException() default true;

    /**
     * 是否屏蔽异常返回默认值
     *
     * @return false 不屏蔽异常 true 屏蔽异常返回默认值
     */
    boolean ignoreException() default false;
}
