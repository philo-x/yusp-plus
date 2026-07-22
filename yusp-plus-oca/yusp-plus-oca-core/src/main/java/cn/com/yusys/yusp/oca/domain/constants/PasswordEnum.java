package cn.com.yusys.yusp.oca.domain.constants;

/**
 * @Classname PasswordEnum
 * @Description 密码相关枚举类
 * @Date 2020/11/3 17:58
 * @author wujp4@yusys.com.cn
 */
public enum PasswordEnum {


    /**
     * 重置密码成功
     */
    SUCCESSFULLY_CHECKED("00000000","重置密码成功"),

    /**
     * 没有相应的密码策略，暂不需要校验密码
     */
    NO_STRATEGY("1002","没有相应的密码策略，暂不需要校验密码"),

    /**
     * 没有通过校验策略
     */
    UNSUCCESSFULLY_CHECKED("1003","没有通过校验策略"),

    /**
     * 密文解密失败
     */
    UNSUCCESSFULLY_DECRYPTED("1004","密文解密失败"),

    /**
     * 密码类型为密文
     */
    PASSWORD_CIPHER("2","密码类型为密文"),

    /**
     * 用户默认密码
     */
    INITIAL_PASSWORD("R/Wny5UD6luZ9SqOFd9xQesW1VZ0py/Zjpab9QLVuFFr6GYa52Je/nV+tjNfsH4TyhDNPTGS3YNax0GMuFQT3N3XbuTqF7JJXkexotmNBmpdHUnOXTpMYnicYek0AoHq3FR3rh2qrKgT/jFJQgCWJeVqXyrLGo7Zno4abi4HULo=","用户默认密码");

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



    PasswordEnum(String code, String message){ //加上public void 上面定义枚举会报错 The constructor Color(int, String) is undefined
        this.code=code;
        this.message=message;

    }
}
