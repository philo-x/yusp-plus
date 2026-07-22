package cn.com.yusys.yusp.oca.annotation;

import java.lang.annotation.*;

/**
 * @author yinjun
 * @version 1.2
 * @description: 数据加密注解 (只能加在String数据类型字段上)
 * @date 2022/3/7
 **/

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonEncrypt {

    /**
     * 加密替换字符
     *
     * @return String
     */
    String value() default "*";

    /**
     * 开始下标位置
     *
     * @return int
     */
    int beginIdx() default 0;

    /**
     * 结束下标位置
     *
     * @return int
     */
    int endIdx() default 0;

    /**
     * 分隔符,如果配置分隔符，以第一个匹配分隔符下标位置为结束下标位置,配置的结束下标位置无效
     * 例如邮箱test@qq.com配置分隔符为"@",返回数据为****@qq.com
     *
     * @return String
     */
    String splitChar() default "";
}
