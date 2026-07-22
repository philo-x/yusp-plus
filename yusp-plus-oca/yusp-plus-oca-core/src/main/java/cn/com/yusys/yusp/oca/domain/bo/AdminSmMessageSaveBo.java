package cn.com.yusys.yusp.oca.domain.bo;

import jakarta.validation.constraints.NotEmpty;

/**
 * 系统提示消息保存参数
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class AdminSmMessageSaveBo {
    /**
     * 信息码
     */
    @NotEmpty(message = "信息码不能为空")
    private String code;
    /**
     * 信息级别:success成功 info信息 warning警告 error错误
     */
    @NotEmpty(message = "信息码不能为空")
    private String messageLevel;
    /**
     * 提示内容
     */
    @NotEmpty(message = "信息码不能为空")
    private String message;
    /**
     * 消息类别：COMINFO系统级通用提示 DBERR数据库错误提示 MODULEINFO模块提示
     */
    @NotEmpty(message = "信息码不能为空")
    private String messageType;
    /**
     * 所属模块名称
     */
    private String funcName;

    public @NotEmpty(message = "信息码不能为空") String getCode() {
        return this.code;
    }

    public @NotEmpty(message = "信息码不能为空") String getMessageLevel() {
        return this.messageLevel;
    }

    public @NotEmpty(message = "信息码不能为空") String getMessage() {
        return this.message;
    }

    public @NotEmpty(message = "信息码不能为空") String getMessageType() {
        return this.messageType;
    }

    public String getFuncName() {
        return this.funcName;
    }

    public void setCode(@NotEmpty(message = "信息码不能为空") String code) {
        this.code = code;
    }

    public void setMessageLevel(@NotEmpty(message = "信息码不能为空") String messageLevel) {
        this.messageLevel = messageLevel;
    }

    public void setMessage(@NotEmpty(message = "信息码不能为空") String message) {
        this.message = message;
    }

    public void setMessageType(@NotEmpty(message = "信息码不能为空") String messageType) {
        this.messageType = messageType;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }


}
