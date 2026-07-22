package cn.com.yusys.yusp.oca.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 注解在vo需要翻译的字段上，比如注解在userId字段上 可以把userId 翻译为 userName
 * 减少表关联查询，跨库join
 *
 * @author danyu
 * @date 2021-06-09 10:00:00
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = RedisDictTranslatorSerializer.class)
public @interface RedisDictTranslator {

    /**
     * 翻译数据源redis保存时的key
     *
     * @return 翻译数据源redis保存时的key
     */
    String redisCacheKey() default "";

    /**
     * fieldName翻译的字段名
     *
     * @return fieldName翻译的字段名
     */
    String fieldName() default "";
}
