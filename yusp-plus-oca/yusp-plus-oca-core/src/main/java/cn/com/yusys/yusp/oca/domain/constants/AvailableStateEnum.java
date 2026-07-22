package cn.com.yusys.yusp.oca.domain.constants;

import cn.com.yusys.yusp.common.config.BaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 状态枚举
 *
 * @author Wujp4
 * date 2020-11-28 18:06:35
 */

public enum AvailableStateEnum implements BaseEnum {
    /**
     * 状态枚举
     */
    UNENABLED("W", "待启用"),
    ENABLED("A", "启用"),
    DISABLED("I", "停用"),
    FREEZING("F", "停用");

    @JsonValue
    @EnumValue
    private String code;

    private String message;


    @Override
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

    AvailableStateEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
