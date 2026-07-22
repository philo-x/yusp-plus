package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.oca.annotation.DictTranslator;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * 部门管理分页查询显示层对象
 *
 * @author danyu
 * @date 2020-11-12 10:47:26
 */
public class AdminSmDptVo {
    /**
     * 部门ID
     */
    private String dptId;
    /**
     * 部门代码
     */
    private String dptCode;
    /**
     * 部门名称
     */
    private String dptName;
    /**
     * 所属机构编号
     */
    private String orgId;
    /**
     * 上级部门记录编号
     */
    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_DPT_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "upDptName")
    private String upDptId;
    /**
     * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
     */
    private AvailableStateEnum dptSts;
    /**
     * 最新变更用户
     */
    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "lastChgName")
    private String lastChgUsr;
    /**
     * 最新变更时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;

    /**
     * 所属机构名称
     */
    private String orgName;

    /**
     * 机构序列
     */
    private String orgSeq;

    public AdminSmDptVo() {
    }

    public String getDptId() {
        return this.dptId;
    }

    public String getDptCode() {
        return this.dptCode;
    }

    public String getDptName() {
        return this.dptName;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getUpDptId() {
        return this.upDptId;
    }

    public AvailableStateEnum getDptSts() {
        return this.dptSts;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public String getOrgName() {
        return this.orgName;
    }


    public void setDptId(String dptId) {
        this.dptId = dptId;
    }

    public void setDptCode(String dptCode) {
        this.dptCode = dptCode;
    }

    public void setDptName(String dptName) {
        this.dptName = dptName;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setUpDptId(String upDptId) {
        this.upDptId = upDptId;
    }

    public void setDptSts(AvailableStateEnum dptSts) {
        this.dptSts = dptSts;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }


    public String getOrgSeq() {
        return orgSeq;
    }

    public void setOrgSeq(String orgSeq) {
        this.orgSeq = orgSeq;
    }
}
