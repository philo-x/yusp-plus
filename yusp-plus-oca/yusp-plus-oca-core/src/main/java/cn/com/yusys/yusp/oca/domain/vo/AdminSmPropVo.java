package cn.com.yusys.yusp.oca.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;


/**
 * 系统信息
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class AdminSmPropVo {
    private String propId;
    /**
     * 属性名
     */
    private String propName;
    /**
     * 属性描述
     */
    private String propDesc;
    /**
     * 属性值
     */
    private String propValue;
    /**
     * 备注
     */
    private String propRemark;
    /**
     * 金融机构编号
     */
    private String instuId;
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


    public String getPropId() {
        return this.propId;
    }

    public String getPropName() {
        return this.propName;
    }

    public String getPropDesc() {
        return this.propDesc;
    }

    public String getPropValue() {
        return this.propValue;
    }

    public String getPropRemark() {
        return this.propRemark;
    }

    public String getInstuId() {
        return this.instuId;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public void setPropId(String propId) {
        this.propId = propId;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public void setPropDesc(String propDesc) {
        this.propDesc = propDesc;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public void setPropRemark(String propRemark) {
        this.propRemark = propRemark;
    }

    public void setInstuId(String instuId) {
        this.instuId = instuId;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }
}
