package cn.com.yusys.yusp.uaa.exception;

import cn.com.yusys.yusp.uaa.enumerate.BusinessCodeForExceptionEnum;
import org.springframework.http.HttpStatus;

/**
 * @author dlf
 * @version 1.0
 * @since 2025/9/9 16:54
 */
public class UserVerificationException extends UaaOauth2AuthenticationException {
    public UserVerificationException(String message) {
        super(BusinessCodeForExceptionEnum.USER_VERIFY_ERROR.getCode(), message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }


}
