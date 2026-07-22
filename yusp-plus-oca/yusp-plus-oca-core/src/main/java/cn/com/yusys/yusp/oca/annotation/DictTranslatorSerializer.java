package cn.com.yusys.yusp.oca.annotation;

import cn.com.yusys.yusp.commons.util.SpringContextUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * 基于的vo字段翻译，比如 userId 40 翻译为userName 系统管理员
 * @author danyu
 * @date 2021-06-09 10:00:00
 */
public class DictTranslatorSerializer extends JsonSerializer<String> implements ContextualSerializer {

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 缓存key, 在fieldName没有值的情况下也代表要翻译到哪个字段
     */
    private String key;

    /**
     * 在注解中给fieldName属性赋了值的情况下，翻译结果字段名取fieldName的值，否则翻译结果属性名取redisCacheKey的值
     */
    private String fieldName;


    /**
     * 默认构造器，必须定义
     */
    public DictTranslatorSerializer() {
    }

    public DictTranslatorSerializer(String cacheName, String key, String fieldName) {
        this.cacheName = cacheName;
        this.key = key;
        this.fieldName = fieldName;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String dictValue = "";
        if (!StringUtils.isEmpty (value)) {
            // 使用hash类型存储字典 例如: key -> userName  HashKey -> 40 HashValue -> 系统管理员
            CustomCacheService cacheService = SpringContextUtils.getBean(CustomCacheService.class);
            dictValue = cacheService.hashGet(cacheName,key,value);
        }
        jsonGenerator.writeString(value);
        jsonGenerator.writeStringField(StringUtils.isEmpty(fieldName)?key:fieldName, dictValue);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        // 为空直接跳过
        if (beanProperty != null) {
            // 非 String 类直接跳过
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                // 取注解对象
                DictTranslator dictTranslator = beanProperty.getAnnotation(DictTranslator.class);
                if (dictTranslator == null) {
                    dictTranslator = beanProperty.getContextAnnotation(DictTranslator.class);
                }
                // 如果能得到注解，就将注解的 cacheName, key fieldName 传入 DictTranslatorSerializer
                if (dictTranslator != null) {
                    return new DictTranslatorSerializer(dictTranslator.cacheName(),dictTranslator.key(),dictTranslator.fieldName());
                }
            }

            return serializerProvider.findValueSerializer (beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer (null);
    }
}
