package cn.com.yusys.yusp.oca.annotation;

import cn.com.yusys.yusp.commons.util.SpringContextUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.Objects;

/**
 * 基于redis的vo字段翻译，比如 userId 40 翻译为userName 系统管理员
 *
 * @author danyu
 * @date 2021-06-09 10:00:00
 */
@Configuration
public class RedisDictTranslatorSerializer extends JsonSerializer<String> implements ContextualSerializer {

    /**
     * 代表redisCacheKey, 在fieldName没有值的情况下也代表要翻译到哪个字段
     */
    private String redisCacheKey;

    /**
     * 在注解中给fieldName属性赋了值的情况下，翻译结果字段名取fieldName的值，否则翻译结果属性名取redisCacheKey的值
     */
    private String fieldName;

    /**
     * 必须要保留无参构造方法
     */
    public RedisDictTranslatorSerializer() {
        this("");
    }

    public RedisDictTranslatorSerializer(String redisCacheKey) {
        this.redisCacheKey = redisCacheKey;
    }

    public RedisDictTranslatorSerializer(String redisCacheKey, String fieldName) {
        this.redisCacheKey = redisCacheKey;
        this.fieldName = fieldName;
    }

    @Override
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (!StringUtils.isEmpty(value)) {
            Environment environment = SpringContextUtils.getBean("environment");
            // 使用hash类型存储字典 例如: redisKey -> userName  HashKey -> 40 HashValue -> 系统管理员
            StringRedisTemplate stringRedisTemplate = SpringContextUtils.getBean("stringRedisTemplate");

            BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash().getOperations().boundHashOps(environment.getProperty("spring.application.name") + redisCacheKey);
            String name = hashOps.get(value);
            jsonGenerator.writeString(value);
            jsonGenerator.writeStringField(StringUtils.isEmpty(fieldName) ? redisCacheKey : fieldName, name);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        // 为空直接跳过
        if (beanProperty != null) {
            // 非 String 类直接跳过
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                // 取注解对象
                RedisDictTranslator redisDictTranslator = beanProperty.getAnnotation(RedisDictTranslator.class);
                if (redisDictTranslator == null) {
                    redisDictTranslator = beanProperty.getContextAnnotation(RedisDictTranslator.class);
                }
                // 如果能得到注解，就将注解的 redisCacheKey 传入 RedisDictTranslatorSerializer
                if (redisDictTranslator != null) {
                    return new RedisDictTranslatorSerializer(redisDictTranslator.redisCacheKey(), redisDictTranslator.fieldName());
                }
            }

            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);
    }
}
