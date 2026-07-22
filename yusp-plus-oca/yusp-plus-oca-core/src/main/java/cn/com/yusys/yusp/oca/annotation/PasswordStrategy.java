package cn.com.yusys.yusp.oca.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 密码策略
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PasswordStrategyCheck.class)
public @interface PasswordStrategy {
    String message() default "Invaid Password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
