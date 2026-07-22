package cn.com.yusys.yusp.common.exception;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.exception.XssException;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.web.filter.XssAndSqlFilter;
import cn.com.yusys.yusp.commons.web.filter.XssHttpServletRequestWrapper;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author wujiangpeng
 * @Order(value = Integer.MIN_VALUE+1) 配置了最高优先级，同时有多个的时候优先走这个统一异常处理
 * @date 2020-11-19 15:30
 */
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE + 1)
public class PlusExceptionHandler {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PlusExceptionHandler.class);

    /**
     * 处理全局异常
     */
    @ExceptionHandler(Exception.class)
    public JClientRspEntity<?> handleException(Exception e) {
        String message = e.getMessage();
        if(!StringUtils.isEmpty(message)&&"WorkflowException".equals(e.getClass().getSimpleName())){
            log.error("统一异常处理：WorkflowException, 异常原因：", e);
            return JClientRspEntity.buildFail("500", message);
        }
        log.error("统一异常处理：Exception, 异常原因：", e);
        return JClientRspEntity.buildFail("500", "系统业务繁忙，请稍后再试");
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BizException.class)
    public JClientRspEntity<?> handleBizException(BizException e) {
        JClientRspEntity<String> jClientRspEntity = JClientRspEntity.buildFail(e.getMessage());
        log.error("统一异常处理：BizException, 异常原因：", e);
        return StringUtils.isEmpty(e.getErrorCode())
                ? JClientRspEntity.buildFail(e.getMessage())
                : JClientRspEntity.buildFail(e.getErrorCode(), e.getMessage());
    }
    @ExceptionHandler(XssException.class)
    public JClientRspEntity<?> handleXssException(XssException e) {
        log.error("统一异常处理：XssException, 异常原因：", e);
        return JClientRspEntity.buildFail("400", "请求数据异常");
    }

    /**
     * 前端表单对象校验异常处理
     * MethodArgumentNotValidException适用于@Valid注解抛出的异常
     *
     * <pre>@Valid</pre>注解用于校验@RequestBody
     *
     * @param ex 异常
     * @return JClientRspEntity 返回JClientRspEntity对象
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public JClientRspEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("统一异常处理：MethodArgumentNotValidException, 异常原因：", ex);

        String defaultMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        // 防止抛出空指针异常
        assert defaultMessage != null;
        String[] split = defaultMessage.split("&");

        if (split.length > 1) {
            return JClientRspEntity.buildFail(split[0], split[1]);
        } else {
            return JClientRspEntity.buildFail("400", ex.getBindingResult().getAllErrors().stream().map((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                String code = error.getCode();
                return fieldName + ": " + errorMessage + ":" + code;
            }).collect(Collectors.joining()));
        }
    }

    /**
     * URL变量，参数校验异常处理
     * ValidationException适用于@Validated注解抛出的异常
     *
     * <pre>@Validated</pre>注解用于校验@Pathvariable和@RequestParam注解, 该注解需要添加到Controller类上
     *
     * @param ex 异常
     * @return JClientRspEntity 返回对象
     */
    @ExceptionHandler({ConstraintViolationException.class})
public JClientRspEntity<?> handleConstraintViolationExceptions(ConstraintViolationException ex) {

        log.error("统一异常处理：ConstraintViolationException, 异常原因：", ex);

        return JClientRspEntity.buildFail("400", ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(",")));
    }

}
