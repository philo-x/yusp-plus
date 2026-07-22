package cn.com.yusys.yusp.oca.annotation;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 字典翻译注解
 *
 * @author danyu
 * @date 2021-06-09 10:00:00
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = DictTranslatorSerializer.class)
public @interface DictTranslator {

    /**
     * 缓存名称，必传
     * @return 缓存名
     */
    String cacheName() default "";
    /**
     * 缓存key，必传
     * @return 缓存key
     */
    String key() default "";

    /**
     * 翻译后字段名，非必传，建议传
     * @return 翻译后字段名
     */
    String fieldName() default "";
}
