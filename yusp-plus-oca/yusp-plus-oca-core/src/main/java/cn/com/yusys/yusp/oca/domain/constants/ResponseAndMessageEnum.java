package cn.com.yusys.yusp.oca.domain.constants;

/**
 * @Classname ResponseAndMessageEnum
 * @Description 用户返回code和对应的信息
 * @Date 2020/11/2 17:23
 * @author wujp4@yusys.com.cn
 */
public enum ResponseAndMessageEnum {

    /**
     * 登录成功
     */
    SUCCESS("00000000","登录成功！"),

    /**
     * 用户名或密码错误
     */
    BAD_CREDENTIALS("10000001","用户名或密码错误"),

    /**
     * 手机号不存在
     */
    NON_PHONENUMBER("10000005","手机号不存在"),

    /**
     * 用户名或密码错误
     */
    NON_USER("10101111","用户名或密码错误"),

    /**
     * 首次登录请修改密码
     */
    FIRSTLOGIN("10000002","首次登录请修改密码"),

    /**
     * 您好，该时间段不允许登录系统，请您谅解
     */
    LOGIN_TIMES_ERROR("10200005", "您好，该时间段不允许登录系统，请您谅解！"),

    /**
     * 您的IP地址异常
     */
    IPERROR("10300005","当前登录IP异常"),

    /**
     * 密码逾期失效,请联系管理员
     */
    PASSWORD_NEED_CHANGE("10300006", "密码逾期失效,请联系管理员!"),

    /**
     * 用户未生效
     */
    USER_INVALID("10300007","用户未生效"),

    /**
     * 密码输入错误次数超过上限,请半个小时后再试！
     */
    USER_FORBIDDEN_LOGIN("10300008","密码输入错误次数超过上限,请半个小时后再试！"),

    /**
     * 剩余次数
     */
    REDUNDANCY("50000001","%s！剩余次数：%d"),

    /**
     * 当前在线人数已达上限
     */
    GREEN("10300004","当前在线人数已达上限！"),

    /**
     * 当前用户已失效
     */
    EXPIRED("10000004","当前用户已失效!");


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



    ResponseAndMessageEnum(String code, String message){ //加上public void 上面定义枚举会报错 The constructor Color(int, String) is undefined
        this.code=code;
        this.message=message;

    }
}
