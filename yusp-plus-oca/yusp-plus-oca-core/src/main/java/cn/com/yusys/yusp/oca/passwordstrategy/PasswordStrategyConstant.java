package cn.com.yusys.yusp.oca.passwordstrategy;

/**
 * 密码策略常量
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class PasswordStrategyConstant {
    /**
     * 密码中需要有数字
     */
    public static final String NUMBER = "number";
    /**
     * 密码中需要有大写字母
     */
    public static final String UPPERCASE = "uppercase";
    /**
     * 密码中需要有小写字母
     */
    public static final String LOWERCASE = "lowercase";
    /**
     * 密码中需要有特殊字符
     */
    public static final String SPECIAL = "specialCharacters";
    /**
     * 密码中默认字符个数
     */
    public static final int CHAR_NUMBER = 1;
    /**
     * 密码长度必须在 {0} - {1}个字符之间
     */
    public static final String PASSWD_LENGTH_ERROR = "10101000";
    /**
     * 密码连续字符不能超过 {0} 个
     */
    public static final String PASSWD_CONTINUOUS_ERROR = "10101001";
    /**
     * 密码包含 {1} 个 {0}, 但是至多只允许 {2} 个
     */
    public static final String PASSWD_REPEAT_ERROR = "10101002";
    /**
     * 密码中必须有 {0} 个数字
     */
    public static final String PASSWD_DIGIT_ERROR = "10101003";
    /**
     * 密码中必须有 {0} 个大写
     */
    public static final String PASSWD_UPPERCASE_ERROR = "10101004";
    /**
     * 密码中必须有 {0} 个小写
     */
    public static final String PASSWD_LOWERCASE_ERROR = "10101005";
    /**
     * 密码中必须有 {0} 个特殊字符
     */
    public static final String PASSWD_SPECIAL_ERROR = "10101006";
    /**
     * 密码默认最小长度
     */
    public static final int PASSWD_MIN_LENGTH = 8;
    /**
     * 密码默认最大长度
     */
    public static final int PASSWD_MAX_LENGTH = 24;
}
