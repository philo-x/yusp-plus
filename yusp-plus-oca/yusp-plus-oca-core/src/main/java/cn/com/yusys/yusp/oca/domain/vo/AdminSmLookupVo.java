package cn.com.yusys.yusp.oca.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;


/**
 * 数据字典VO
 * @author zhanyq
 * @date 2021-06-30 17:29
 */
public class AdminSmLookupVo {

    /**
     * 记录编号
     */
    private String lookupId;
    /**
     * 金融机构编号
     */
    private String instuId;
    /**
     * 所属目录编号
     */
    private String lookupTypeId;
    /**
     * 字典类别英文别名
     */
    private String lookupCode;
    /**
     * 字典类别名称
     */
    private String lookupName;
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

    public AdminSmLookupVo() {
    }

    public String getLookupId() {
        return this.lookupId;
    }

    public String getInstuId() {
        return this.instuId;
    }

    public String getLookupTypeId() {
        return this.lookupTypeId;
    }

    public String getLookupCode() {
        return this.lookupCode;
    }

    public String getLookupName() {
        return this.lookupName;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public void setLookupId(String lookupId) {
        this.lookupId = lookupId;
    }

    public void setInstuId(String instuId) {
        this.instuId = instuId;
    }

    public void setLookupTypeId(String lookupTypeId) {
        this.lookupTypeId = lookupTypeId;
    }

    public void setLookupCode(String lookupCode) {
        this.lookupCode = lookupCode;
    }

    public void setLookupName(String lookupName) {
        this.lookupName = lookupName;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }


}
