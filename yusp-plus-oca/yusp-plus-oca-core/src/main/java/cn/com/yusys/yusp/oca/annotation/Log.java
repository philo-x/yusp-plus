package cn.com.yusys.yusp.oca.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    //日志类型
    String type() default "4";

    //操作标志
    String operFlag() default "按钮";

    //日志内容
    String content();

    //操作对象ID
    String operObjId();
}
