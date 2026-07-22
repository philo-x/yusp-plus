package cn.com.yusys.yusp.oca.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * 系统金融机构表拓展实体
 *
 * @author terry
 * @date 2020-12-2 18:06:35
 */
public class InstuExtVo {
    private String instuId;
    private String sysId;
    private String instuCde;
    private String instuName;
    private String joinDt;
    private String instuAddr;
    private String zipCde;
    private String contTel;
    private String contUsr;
    private String instuSts;
    private String lastChgUsr;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;
    private String userName;

    public String getInstuId() {
        return this.instuId;
    }

    public String getSysId() {
        return this.sysId;
    }

    public String getInstuCde() {
        return this.instuCde;
    }

    public String getInstuName() {
        return this.instuName;
    }

    public String getJoinDt() {
        return this.joinDt;
    }

    public String getInstuAddr() {
        return this.instuAddr;
    }

    public String getZipCde() {
        return this.zipCde;
    }

    public String getContTel() {
        return this.contTel;
    }

    public String getContUsr() {
        return this.contUsr;
    }

    public String getInstuSts() {
        return this.instuSts;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setInstuId(String instuId) {
        this.instuId = instuId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setInstuCde(String instuCde) {
        this.instuCde = instuCde;
    }

    public void setInstuName(String instuName) {
        this.instuName = instuName;
    }

    public void setJoinDt(String joinDt) {
        this.joinDt = joinDt;
    }

    public void setInstuAddr(String instuAddr) {
        this.instuAddr = instuAddr;
    }

    public void setZipCde(String zipCde) {
        this.zipCde = zipCde;
    }

    public void setContTel(String contTel) {
        this.contTel = contTel;
    }

    public void setContUsr(String contUsr) {
        this.contUsr = contUsr;
    }

    public void setInstuSts(String instuSts) {
        this.instuSts = instuSts;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
