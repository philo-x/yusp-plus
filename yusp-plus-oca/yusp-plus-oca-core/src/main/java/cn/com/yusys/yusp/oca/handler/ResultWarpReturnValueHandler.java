package cn.com.yusys.yusp.oca.handler;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.annotation.JsonEncrypt;
import cn.com.yusys.yusp.oca.utils.EncryptViewUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author yinjun
 * @version 1.2
 * @description: 返回结果加密处理器
 * @date 2022/3/7
 **/
public class ResultWarpReturnValueHandler implements HandlerMethodReturnValueHandler {
    private static final Logger log = LoggerFactory.getLogger(ResultWarpReturnValueHandler.class);

    private final HandlerMethodReturnValueHandler delegate;

    public ResultWarpReturnValueHandler(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        // 判断是否支持处理该方法参数,只要接口有@ResponseBody就会通过handleReturnValue处理返回数据
        return delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        delegate.handleReturnValue(convertReturnValue(returnValue), returnType, mavContainer, webRequest);
    }

    private Object convertReturnValue(Object source) {
        if (null != source) {
            jsonEncrypt(source);
        }
        return source;
    }

    private void jsonEncrypt(Object source) {
        if (source instanceof Iterable iterable) {
            for (Object object : iterable) {
                doJsonEncrypt(object);
            }
        } else if (source instanceof JClientRspEntity<?> result) {
            if (null != result.getBody()) {
                doJsonEncrypt(result.getBody());
            }
        }
    }

    private void doJsonEncrypt(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        if (ArrayUtils.isNotEmpty(fields)) {
            for (Field field : fields) {
                JsonEncrypt jsonEncrypt = field.getAnnotation(JsonEncrypt.class);
                if (null != jsonEncrypt) {
                    doJsonEncrypt(field, jsonEncrypt, object);
                }
            }
        }
    }

    private void doJsonEncrypt(Field field, JsonEncrypt jsonEncrypt, Object object) {
        try {
            field.setAccessible(true);
            Object val = field.get(object);
            if (null != val) {
                // 加密值
                String strVal = String.valueOf(val);

                // 加密参数
                String spanChar = jsonEncrypt.value();
                // 分隔符
                String splitChar = jsonEncrypt.splitChar();
                int beginIdx = jsonEncrypt.beginIdx(), endIdx = jsonEncrypt.endIdx();

                // 设置加密后的值
                field.set(object, EncryptViewUtils.toEncryptView(strVal, spanChar, splitChar, beginIdx, endIdx));
            }
        } catch (IllegalAccessException e) {
            log.error("json encrypt error,", e);
        }
    }
}
