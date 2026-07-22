package cn.com.yusys.yusp.uaa.enumerate;

import cn.com.yusys.yusp.uaa.config.CommonConfig;

/**
 * 自定义异常业务码
 * @author zhangyt12
 * @date 2021/9/9 9:46
 */
public enum BusinessCodeForExceptionEnum {
    /**
     * 认证成功枚举
     */
    SUCCESSFULLY_CHECKED("00000000","认证成功"),
    /**
     * 首次登录成功枚举
     */
    FIRST_LOGIN("10000002","首次登录请修改密码"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR("80000000", "System error"),
    /**
     * 用户信息认证异常
     */
    AUTHENTICATION_FAILURE("80000001", "Bad credentials"),
    /**
     * 客户端认证异常
     */
    BAD_CLIENT_CREDENTIALS("80000002", "Bad client credentials"),
    /**
     * 客户端认证异常
     */
    AUTHENTICATION_CLIENT_FAILURE("80000003", "Client error"),
    /**
     * 验证码异常
     */
    IMAGE_CODE_ERROR("80000004", "Image captcha verification failed"),
    /**
     * 无效的方法类型
     */
    UNSUPPORTED_GRANT_TYPE("80000005", "Unsupported grant type"),
    /**
     * 范围不足异常
     */
    INSUFFICIENT_SCOPE("80000006", "Insufficient scope Exception"),
    /**
     * 无效的客户端异常
     */
    INVALID_CLIENT("80000006", "Client was unable to authenticate"),
    /**
     * 无效的授权类型
     */
    INVALID_GRANT("80000007", "Grant type was unable to authenticate"),
    /**
     * 无效请求异常
     */
    INVALID_REQUEST("80000008", "Request was unable to authenticate"),
    /**
     * 无效范围异常
     */
    INVALID_SCOPE("80000009", "Scope was unable to authenticate"),
    /**
     * 无效令牌异常
     */
    INVALID_TOKEN("80000010", "Token was unable to authenticate"),
    /**
     * 重定向不匹配异常
     */
    REDIRECT_MISMATCH("80000011", "Redirect mismatch"),
    /**
     * 在问题序列化/反序列化期间抛出
     */
    SERIALIZATION_EXCEPTION("80000012", "Redirect mismatch"),
    /**
     * 未经批准的客户端身份验证异常
     */
    UNAPPROVED_CLIENT_AUTHENTICATION("80000013", "Unapproved client authentication"),
    /**
     * 当客户端无法进行身份验证时抛出异常
     */
    UNAUTHORIZED_CLIENT("80000014", "Unauthorized client authentication"),
    /**
     * 当客户端无法进行身份验证时抛出异常
     */
    UNAUTHORIZED_USER("80000015", "Unauthorized user authentication"),
    /**
     * 不支持的响应类型异常
     */
    UNSUPPORTED_RESPONSE("80000016", "Unsupported response"),
    /**
     * 用户拒绝授权异常
     */
    USER_DENIED_AUTHORIZATION("80000017", "User denied authorization"),
    /**
     * 权限不够
     */
    FORBIDDEN_EXCEPTION("80000018", "Forbidden Exception"),
    /**
     * 服务器错误异常
     */
    SERVER_ERROR_EXCEPTION("80000019", "Server error exception"),
    /**
     * 未经授权的例外
     */
    UNAUTHORIZED_EXCEPTION("80000020", "Unauthorized exception"),
    /**
     * 用户拒绝授权异常
     */
    METHOD_NOT_ALLOWED("80000021", "Method not allowed"),
    /**
     * 用户拒绝授权异常
     */
    USERNAME_NOT_FOUND(BusinessErrorCode.ERR_CODE_80000022, "user not found"),
    /**
     * 用户拒绝授权异常
     */
    LOCKED_EXCEPTION(BusinessErrorCode.ERR_CODE_80000022, "User account is locked"),
    /**
     * 用户拒绝授权异常
     */
    DISABLED_EXCEPTION(BusinessErrorCode.ERR_CODE_80000022, "User is disabled"),
    /**
     * 用户拒绝授权异常
     */
    ACCOUNT_EXPIRED_EXCEPTION(BusinessErrorCode.ERR_CODE_80000022, "User account has expired"),
    /**
     * 用户拒绝授权异常
     */
    CREDENTIALS_EXPIRED_EXCEPTION(BusinessErrorCode.ERR_CODE_80000022, "User credentials have expired"),
    /**
     * feign 调用 oca 异常
     */
    OCA_FEIGN_ERROR("80000023", "oca exception"),
    /**
     * oca 查询校验用户异常
     */
    USER_VERIFY_ERROR("80000024", "user verification error with oca"),
    /**
     * oca 查询校验用户异常
     */
    AUTH_CODE_VERIFY_TOKEN_ERROR("80000025", "The authorization code mode verification token is abnormal, Different login_code."),
    /**
     * 手机验证码错误消息
     */
    SMS_SENT("88000001", "验证码已发送，请勿重复点击");

    private String code;
    private String message;

    BusinessCodeForExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
