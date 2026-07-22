package cn.com.yusys.yusp.oca.domain.constants;

/**
 * 返回码
 *
 * @author wujp4
 * @date 2020-11-04 10:10:35
 */
public enum MessageEnums {

    /**
     * 不匹配登录用户当前密码，请检查原密码
     */
    PASSWORD_MISMATCH("10100000", "不匹配登录用户当前密码，请检查原密码"),

    /**
     * 密码解析失败
     */
    PASSWORD_RESOLUTION_FAILED("10100004", "密码解析失败"),

    /**
     * 密码加密失败
     */
    PASSWORD_ENCODE_FAILED("10100005", "密码加密失败"),

    /**
     * 密码策略通过
     */
    PASSWORD_COMPLEX_SUCCESS("10100001", "密码策略通过"),

    /**
     * 密码修改失败
     */
    PASSWORD_MODIFY_FAILED("10101112", "密码修改失败"),

    /**
     * 密码长度不够
     */
    PASSWORD_LENGTH_NOT_ENOUGH("10100021", "密码不能少于位数:"),

    /**
     * 密码不能与最近多少次的密码重复
     */
    PASSWORD_REPEAT_NUMBERS("10100022", "密码不能与最近多少次的密码重复:"),

    /**
     * 密码连续重复的字符数超过最大长度
     */
    PASSWORD_REPEAT_LETTER_LIMIT("10100023", "密码连续重复的字符数超过最大长度:"),

    /**
     * 密码连续字符的字符数超过最大长度
     */
    PASSWORD_CONTINUE_LETTER_LIMIT("10100024", "密码连续字符的字符数超过最大长度:"),

    /**
     * 无密码策略
     */
    NOT_PASSWORD_STRATEGY("10100002", "无密码策略");


    private String code;
    private String message;

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


    MessageEnums(String code, String message) { //加上public void 上面定义枚举会报错 The constructor Color(int, String) is undefined
        this.code = code;
        this.message = message;

    }
}
