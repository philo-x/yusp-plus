package cn.com.yusys.yusp.uaa.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * @author dlf
 * @version 1.0
 * @since 2025/7/30 15:53
 */
public class UaaOauth2AuthenticationException extends OAuth2AuthenticationException {


    public UaaOauth2AuthenticationException(String code, String message) {
        super(new OAuth2Error(code, message, null));
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }


}
