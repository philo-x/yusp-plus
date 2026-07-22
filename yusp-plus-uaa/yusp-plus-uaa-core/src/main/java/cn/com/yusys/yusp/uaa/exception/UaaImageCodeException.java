package cn.com.yusys.yusp.uaa.exception;

import cn.com.yusys.yusp.uaa.enumerate.BusinessCodeForExceptionEnum;
import org.springframework.http.HttpStatus;

/**
 * @author dlf
 * @version 1.0
 * @since 2025/9/9 17:07
 */
public class UaaImageCodeException extends UaaOauth2AuthenticationException {

    public UaaImageCodeException(String message) {
        super(BusinessCodeForExceptionEnum.IMAGE_CODE_ERROR.getCode(), message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
