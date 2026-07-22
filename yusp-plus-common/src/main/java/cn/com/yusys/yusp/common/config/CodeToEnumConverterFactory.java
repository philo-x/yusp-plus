package cn.com.yusys.yusp.common.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * 工厂类：枚举code转换为枚举类型
 *
 * @author terry
 * @date 2020-11-26 14:46:26
 */
public class CodeToEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

    /**
     * 存储枚举类型转换器
     * key: Class<<T extends BaseEnum>> value:  CodeToEnumConverter<T extends BaseEnum>
     */
    private static final Map<Class, Converter> CONVERTERS = new HashMap<>(8);

    /**
     * 获取一个从 code 转化为 T 的转换器，T <T extends BaseEnum>
     *
     * @param targetEnumType 转换后的类型
     * @return 返回一个转化器Converter<String, T> Converter<String, T>
     */
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetEnumType) {
        CONVERTERS.computeIfAbsent(targetEnumType, CodeToEnumConverter::new);
        return CONVERTERS.get(targetEnumType);
    }
}