package cn.com.yusys.yusp.common.aspect;

import cn.com.yusys.yusp.common.annotation.Metrics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * 日志增强切面类，主要增强功能为：
 * 必须配置 application.metrics.enable=true才生效
 * 1. 请求参数和返回值打印
 * 2. 请求响应时间打印
 *
 * @author danyubin
 * @date 2020-11-26 14:46:26
 */
@ConditionalOnProperty(value = "application.metrics.enable", havingValue =
        "true")
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MetricsAspect {

    private static final Logger log = LoggerFactory.getLogger(MetricsAspect.class);

    private static final Map<Class<?>, Object> DEFAULT_VALUES = Stream
            .of(boolean.class, byte.class, char.class, double.class, float.class, int.class, long.class, short.class)
            .collect(toMap(clazz -> (Class<?>) clazz, clazz -> Array.get(Array.newInstance(clazz, 1), 0)));

    @Autowired
    ObjectMapper objectMapper;

    /**
     * Part of HTTP content type header.
     */
    public static final String MULTIPART = "multipart/";

    public static <T> T getDefaultValue(Class<T> clazz) {
        return (T) DEFAULT_VALUES.get(clazz);
    }

    @Pointcut("@annotation(cn.com.yusys.yusp.common.annotation.Metrics)")
    public void withMetricsAnnotation() {

    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController" +
            " *)")
    public void controllerBean() {

    }

    @Around("controllerBean() || withMetricsAnnotation()")
    public Object metrics(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 尝试获取当前方法的类名和方法名
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String classAndMethodName = "【%s】".formatted(
                methodSignature.toLongString());
        // 获取方法上的 Metrics 注解
        Metrics metrics = getMetrics(methodSignature);

        // 对于web项目我们可以从上下文中获取到额外的一些信息来丰富我们的日志
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 是否是文件上传请求
        boolean isFileUploadRequest = false;
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            classAndMethodName += "【%s】".formatted(
                    request.getRequestURI());
            isFileUploadRequest = isMultipartContent(request);

        }

        // 实现的是入参日志的输出, 过滤文件上传请求
        if (metrics.logParameters() && !isFileUploadRequest) {
            try {
                log.info("调用 {} 的【入参】：【{}】", classAndMethodName,
                        objectMapper.writeValueAsString(proceedingJoinPoint.getArgs()));
            } catch (JsonProcessingException e) {
                log.warn("调用 {} 的request对象JSON序列化出错，忽略异常，请开发人员关注此错误：{}", classAndMethodName, e);
            }
        }

        // 执行实际方法，并且获取返回值
        Object returnValue = proceedAndGetReturnValue(proceedingJoinPoint, metrics, classAndMethodName, methodSignature);

        // 实现了返回值的输出
        if (metrics.logReturn()) {
            log.info("调用 {} 的【返回值】： 【{}】", classAndMethodName,
                    objectMapper.writeValueAsString(returnValue));
        }

        return returnValue;
    }

    /**
     * 执行实际方法，并且获取返回值
     */
    private static Object proceedAndGetReturnValue(ProceedingJoinPoint proceedingJoinPoint, Metrics metrics, String classAndMethodName, MethodSignature methodSignature) throws Throwable {
        Object returnValue;
        Instant start = Instant.now();
        try {
            returnValue = proceedingJoinPoint.proceed();

            if (metrics.recordSuccessMetrics()) {
                log.info("调用 {} 成功，耗时：{} ms", classAndMethodName, Duration.between(start, Instant.now()).toMillis());
            }
        } catch (Exception e) {
            // 实现的是错误日志的输出
            if (metrics.recordFailMetrics()) {
                log.info("调用 {} 失败，耗时：{} ms", classAndMethodName, Duration.between(start, Instant.now()).toMillis());
            }

            // 实现的是异常日志的输出
            if (metrics.logException()) {
                log.error("调用 {} 出现异常", classAndMethodName, e);
            }

            // 如果忽略异常输出默认值
            if (metrics.ignoreException()) {
                returnValue = getDefaultValue(methodSignature.getReturnType());
            } else {
                throw e;
            }
        }
        return returnValue;
    }


    /**
     * 获取方法上的 Metrics 注解
     *
     * @param methodSignature 方法签名
     * @return 方法上的 Metrics 注解，如果不存在，就获取当前类上的 Metrics 注解
     * 如果两个都不存在，就返回一个临时的 Metrics 注解，避免为空
     */
    private Metrics getMetrics(MethodSignature methodSignature) {
        Metrics metrics =
                methodSignature.getMethod().getAnnotation(Metrics.class);
        if (metrics == null) {
            metrics =
                    methodSignature.getMethod().getDeclaringClass().getAnnotation(Metrics.class);
        }

        // 对于Controller和Repository，我们需要初始化一个@Metrics注解出来
        if (metrics == null) {
            @Metrics
            final class TempClass {
            }

            metrics = TempClass.class.getAnnotation(Metrics.class);
        }
        return metrics;
    }

    /**
     * 是否是文件上传请求
     *
     * @param request HttpServletRequest
     * @return true or false
     */
    private boolean isMultipartContent(HttpServletRequest request) {
        String contentType = request.getContentType();

        if (contentType == null) {
            return false;
        }

        return contentType.toLowerCase(Locale.ENGLISH).startsWith(MULTIPART);
    }
}
