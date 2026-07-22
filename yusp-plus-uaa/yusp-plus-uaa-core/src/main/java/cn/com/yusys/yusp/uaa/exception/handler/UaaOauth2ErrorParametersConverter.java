package cn.com.yusys.yusp.uaa.exception.handler;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义的异常处理返回，处理 OAuth2Error，并将其封装为研发平台需要的格式进行返回
 * <p>
 * 原格式:
 * <pre>{@code
 * {
 * "error": "invalid_request",
 * "error_description": "Invalid request"
 * }
 * }</pre>
 * <p>
 * 修改后格式:
 * <pre>{@code
 * {
 * "code": "10000001",
 * "message": "用户名或密码错误"
 * }
 * }</pre>
 *
 * 默认实现为：{@link OAuth2ErrorHttpMessageConverter#setErrorParametersConverter} 内部的 OAuth2ErrorParametersConverter
 * @author dlf
 * @version 1.0
 * @since 2025/8/30 12:39
 */
public class UaaOauth2ErrorParametersConverter implements Converter<OAuth2Error, Map<String, String>> {

    @Override
    public Map<String, String> convert(OAuth2Error oauth2Error) {
        Map<String, String> parameters = new HashMap<>(8);
        parameters.put("code", oauth2Error.getErrorCode());
        if (StringUtils.hasText(oauth2Error.getDescription())) {
            parameters.put("message", oauth2Error.getDescription());
        } else {
            parameters.put("message", "系统繁忙，请稍后再试");
        }
        return parameters;
    }
}
