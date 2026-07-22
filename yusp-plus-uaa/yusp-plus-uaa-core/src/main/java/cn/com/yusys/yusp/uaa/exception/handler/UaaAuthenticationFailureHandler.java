package cn.com.yusys.yusp.uaa.exception.handler;

import cn.com.yusys.yusp.uaa.enumerate.BusinessCodeForExceptionEnum;
import cn.com.yusys.yusp.uaa.exception.UaaOauth2AuthenticationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dlf
 * @version 1.0
 * @since 2025/9/5 15:18
 */
public class UaaAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final Log logger = LogFactory.getLog(getClass());

    private static final Map<Class<?>, BusinessCodeForExceptionEnum> EXCEPTION_ENUM_MAP = new HashMap<>(32);


    static {
        EXCEPTION_ENUM_MAP.put(BadCredentialsException.class, BusinessCodeForExceptionEnum.BAD_CLIENT_CREDENTIALS);
        EXCEPTION_ENUM_MAP.put(InsufficientAuthenticationException.class, BusinessCodeForExceptionEnum.INSUFFICIENT_SCOPE);
        EXCEPTION_ENUM_MAP.put(UsernameNotFoundException.class, BusinessCodeForExceptionEnum.USERNAME_NOT_FOUND);
        EXCEPTION_ENUM_MAP.put(LockedException.class, BusinessCodeForExceptionEnum.LOCKED_EXCEPTION);
        EXCEPTION_ENUM_MAP.put(DisabledException.class, BusinessCodeForExceptionEnum.DISABLED_EXCEPTION);
        EXCEPTION_ENUM_MAP.put(AccountExpiredException.class, BusinessCodeForExceptionEnum.ACCOUNT_EXPIRED_EXCEPTION);
        EXCEPTION_ENUM_MAP.put(CredentialsExpiredException.class, BusinessCodeForExceptionEnum.CREDENTIALS_EXPIRED_EXCEPTION);
    }

    private final HttpMessageConverter<OAuth2Error> errorResponseConverter;

    public UaaAuthenticationFailureHandler() {
        OAuth2ErrorHttpMessageConverter converter = new OAuth2ErrorHttpMessageConverter();
        converter.setErrorParametersConverter(new UaaOauth2ErrorParametersConverter());
        errorResponseConverter = converter;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException authenticationException) throws IOException, ServletException {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
        // 根据异常情况，来设置响应码
        OAuth2Error error;
        if (authenticationException instanceof OAuth2AuthenticationException oauthException) {
            if (oauthException instanceof UaaOauth2AuthenticationException uaaAuthenticationException) {
                // 如果是研发平台自定义的异常，就重新设置响应码
                httpResponse.setStatusCode(uaaAuthenticationException.getHttpStatus());
            }
            error = oauthException.getError();
        } else {
            BusinessCodeForExceptionEnum exceptionEnum = EXCEPTION_ENUM_MAP.getOrDefault(authenticationException.getClass(), BusinessCodeForExceptionEnum.SYSTEM_ERROR);
            if (exceptionEnum != BusinessCodeForExceptionEnum.SYSTEM_ERROR) {
                httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            }
            error = new OAuth2Error(exceptionEnum.getCode(), exceptionEnum.getMessage(), null);
        }
        this.errorResponseConverter.write(error, null, httpResponse);
    }
}
