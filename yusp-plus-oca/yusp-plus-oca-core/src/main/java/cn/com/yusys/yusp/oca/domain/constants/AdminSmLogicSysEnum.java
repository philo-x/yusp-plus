package cn.com.yusys.yusp.oca.domain.constants;

/**
 * @类名称: LogicSysEnum
 * @类描述: 逻辑系统枚举类
 * @author xufy1@yusys.com.cn
 * @创建时间: 2020-11-24 15:33
 * @修改备注:
 * @修改日期 修改人员    修改原因
 * ----------  ---------  -----------------------------
 * @Version 1.0.0
 * @Copyright (c) 2018宇信科技-版权所有
 */
public enum AdminSmLogicSysEnum {

    /**
     * 逻辑系统sysId已生效,不能再次生效
     */
    LOGIC_SYS_EXIT_NO_VALIDATE(20100017, "逻辑系统sysId已生效,不能再次生效!"),

    /**
     * 逻辑系统sysId已失效,不能再次失效!
     */
    LOGIC_SYS_NO_EXIT_NO_VALIDATE(20100018, "逻辑系统sysId已失效,不能再次失效!"),

    /**
     * 逻辑系统sysId已生效
     */
    LOGIC_SYS_EXIT(20100019, "逻辑系统sysId已生效!"),

    /**
     * 逻辑系统sysId已失效
     */
    LOGIC_SYS_NO_EXIT(20100020, "逻辑系统sysId已失效!");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    AdminSmLogicSysEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
