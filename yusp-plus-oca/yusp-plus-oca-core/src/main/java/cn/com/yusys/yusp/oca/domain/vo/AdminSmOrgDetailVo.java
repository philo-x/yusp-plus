package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 系统机构详情表现层实体
 *
 * @author terry
 * @date 2020-11-27 18:06:35
 */
public class AdminSmOrgDetailVo {

    private String orgId;
    /**
     * 金融机构编号
     */
    private String instuId;
    /**
     * 机构代码
     */
    private String orgCode;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 上级机构记录编号
     */
    private String upOrgId;
    /**
     * 上级机构名称
     */
    private String upOrgName;
    /**
     * 上级机构码
     */
    private String upOrgCode;
    /**
     * 机构层级
     */
    private Integer orgLevel;
    /**
     * 地址
     */
    private String orgAddr;
    /**
     * 邮编
     */
    private String zipCde;
    /**
     * 联系电话
     */
    private String contTel;
    /**
     * 联系人
     */
    private String contUsr;
    /**
     * 状态：对应字典项=NORM_STS A：启用 I：停用 W：待启用
     */
    private AvailableStateEnum orgSts;
    /**
     * 最新变更用户
     */
    private String lastChgUsr;
    /**
     * 最新变更用户名称
     */
    private String lastChgName;
    /**
     * 最新变更时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    private Date lastChgDt;

    public AdminSmOrgDetailVo() {
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getInstuId() {
        return this.instuId;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public String getUpOrgId() {
        return this.upOrgId;
    }

    public String getUpOrgName() {
        return this.upOrgName;
    }

    public String getUpOrgCode() {
        return this.upOrgCode;
    }

    public Integer getOrgLevel() {
        return this.orgLevel;
    }

    public String getOrgAddr() {
        return this.orgAddr;
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

    public AvailableStateEnum getOrgSts() {
        return this.orgSts;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public String getLastChgName() {
        return this.lastChgName;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setInstuId(String instuId) {
        this.instuId = instuId;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setUpOrgId(String upOrgId) {
        this.upOrgId = upOrgId;
    }

    public void setUpOrgName(String upOrgName) {
        this.upOrgName = upOrgName;
    }

    public void setUpOrgCode(String upOrgCode) {
        this.upOrgCode = upOrgCode;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public void setOrgAddr(String orgAddr) {
        this.orgAddr = orgAddr;
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

    public void setOrgSts(AvailableStateEnum orgSts) {
        this.orgSts = orgSts;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgName(String lastChgName) {
        this.lastChgName = lastChgName;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }
}
