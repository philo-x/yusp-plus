package cn.com.yusys.yusp.common.config;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.HashMap;
import java.util.Map;

/**
 * code -> 枚举类型的转换器
 * SpringMVC会在反序列化的时候自动调用
 *
 * @author terry
 * @date 2020-11-26 14:46:26
 */
public class CodeToEnumConverter<T extends BaseEnum> implements Converter<String, T> {
    /**
     * 存储枚举类型的map
     * key： 枚举的code  value: T extends BaseEnum
     */
    private Map<String, T> enumMap = new HashMap<>(8);

    /**
     * 初始化enumMap, SpringMVC自动调用
     * @param enumType <T extends BaseEnum>
     */
    public CodeToEnumConverter(Class<T> enumType) {
        T[] enums = enumType.getEnumConstants();
        for (T e : enums) {
            enumMap.put(e.getCode(), e);
        }
    }

    /**
     * 根据枚举code转换为枚举类型，SpringMVC在反序列化的时候自动调用
     * @param source 枚举的code
     * @return <T extends BaseEnum>
     */
    @Override
    public T convert(String source) {
        T t = enumMap.get(source);
        if (ObjectUtils.isNull(t)) {
            throw new IllegalArgumentException("无法匹配对应的枚举类型");
        }
        return t;
    }
}
