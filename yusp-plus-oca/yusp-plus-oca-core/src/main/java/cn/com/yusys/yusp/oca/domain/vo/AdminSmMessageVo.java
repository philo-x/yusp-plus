package cn.com.yusys.yusp.oca.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * 系统提示消息
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class AdminSmMessageVo {
    /**
     * 消息编号
     */
    private String messageId;
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
    /**
     * 最后修改用户
     */
    private String lastChgUsr;
    /**
     * 最后修改时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;


    public String getMessageId() {
        return this.messageId;
    }

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

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }
}
