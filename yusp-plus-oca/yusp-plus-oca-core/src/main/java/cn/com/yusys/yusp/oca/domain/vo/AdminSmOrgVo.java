package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.oca.annotation.DictTranslator;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 系统机构表拓展实体
 *  *
 * @author terry
 * @date 2020-11-27 18:06:35
 */
public class AdminSmOrgVo {
    /**
     * 记录编号
     */
    private String orgId;
    /**
     * 机构代码
     */
    private String orgCode;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 机构层级
     */
    private Integer orgLevel;
    /**
     * 状态：对应字典项=NORM_STS A：启用 I：停用 W：待启用
     */
    private String orgSts;
    /**
     * 金融机构编号
     */
    private String instuId;
    /**
     * 金融机构名称
     */
    private String instuName;
    /**
     * 金融机构状态
     */
    private AvailableStateEnum instuSts;
    /**
     * 上级机构记录编号
     */
    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_ORG_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "upOrgName")
    private String upOrgId;
    /**
     * 上级机构码
     */
    private String upOrgCode;
    /**
     * 最新变更时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastChgDt;
    /**
     * 最新变更用户
     */
    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "lastChgName")
    private String lastChgUsr;
    /**
     * 机构序列
     */
    private String orgSeq;

    public AdminSmOrgVo() {
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public Integer getOrgLevel() {
        return this.orgLevel;
    }

    public String getOrgSts() {
        return this.orgSts;
    }

    public String getInstuId() {
        return this.instuId;
    }

    public String getInstuName() {
        return this.instuName;
    }

    public AvailableStateEnum getInstuSts() {
        return this.instuSts;
    }

    public String getUpOrgId() {
        return this.upOrgId;
    }


    public String getUpOrgCode() {
        return this.upOrgCode;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }


    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public void setOrgSts(String orgSts) {
        this.orgSts = orgSts;
    }

    public void setInstuId(String instuId) {
        this.instuId = instuId;
    }

    public void setInstuName(String instuName) {
        this.instuName = instuName;
    }

    public void setInstuSts(AvailableStateEnum instuSts) {
        this.instuSts = instuSts;
    }

    public void setUpOrgId(String upOrgId) {
        this.upOrgId = upOrgId;
    }

    public void setUpOrgCode(String upOrgCode) {
        this.upOrgCode = upOrgCode;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public String getOrgSeq() {
        return orgSeq;
    }

    public void setOrgSeq(String orgSeq) {
        this.orgSeq = orgSeq;
    }
}
