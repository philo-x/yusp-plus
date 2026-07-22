package cn.com.yusys.yusp.oca.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @类名称: AdminSmCrelStraVo
 * @类描述: 认证策略vo
 * @author xufy1@yusys.com.cn
 * @创建时间: 2020-11-24 16:58
 * @修改备注:
 * @修改日期 修改人员    修改原因
 * ----------  ---------  -----------------------------
 * @Version 1.0.0
 * @Copyright (c) 2018宇信科技-版权所有
 */
public class AdminSmCrelStraVo implements Serializable {

    /**
     * 记录编号
     */
    private String crelId;
    /**
     * 逻辑系统编号
     */
    private String sysId;
    /**
     * 策略名称
     */
    private String cerlName;
    /**
     * 是否启用 1:是 2:否
     */
    private String enableFlag;
    /**
     * 策略明细
     */
    private String crelDetail;
    /**
     * 执行动作1: 冻结用户 2:禁止 3：警告
     */
    private String actionType;
    /**
     * 最新变更用户
     */
    private String lastChgUsr;
    /**
     * 最新变更时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;

    public AdminSmCrelStraVo() {
    }

    public String getCrelId() {
        return this.crelId;
    }

    public String getSysId() {
        return this.sysId;
    }

    public String getCerlName() {
        return this.cerlName;
    }

    public String getEnableFlag() {
        return this.enableFlag;
    }

    public String getCrelDetail() {
        return this.crelDetail;
    }

    public String getActionType() {
        return this.actionType;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public void setCrelId(String crelId) {
        this.crelId = crelId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setCerlName(String cerlName) {
        this.cerlName = cerlName;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public void setCrelDetail(String crelDetail) {
        this.crelDetail = crelDetail;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }


}
