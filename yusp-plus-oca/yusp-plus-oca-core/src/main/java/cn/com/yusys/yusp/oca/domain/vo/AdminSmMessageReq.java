package cn.com.yusys.yusp.oca.domain.vo;

/**
 * 系统提示消息返回参数
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class AdminSmMessageReq {
    /**
     * 信息码
     */
    private String code;
    /**
     * 信息级别:success成功 info信息 warning警告 error错误
     */
    private String messageLevel;
    /**
     * 提示内容
     */
    private String message;
    /**
     * 消息类别：COMINFO系统级通用提示 DBERR数据库错误提示 MODULEINFO模块提示
     */
    private String messageType;
    /**
     * 所属模块名称
     */
    private String funcName;


    public String getCode() {
        return this.code;
    }

    public String getMessageLevel() {
        return this.messageLevel;
    }

    public String getMessage() {
        return this.message;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public String getFuncName() {
        return this.funcName;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessageLevel(String messageLevel) {
        this.messageLevel = messageLevel;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }
}
