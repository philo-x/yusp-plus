package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.oca.annotation.DictTranslator;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统信息
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@TableName("admin_sm_prop")
public class AdminSmPropEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId
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
    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "userName")
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private String lastChgUsr;
    /**
     * 最新变更时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;

    /**
     * 数据所属租户ID
     */
    private String dataTenantId;

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

    public String getLastChgUsr() {
        return lastChgUsr;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public Date getLastChgDt() {
        return lastChgDt;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public String getDataTenantId() {
        return dataTenantId;
    }

    public void setDataTenantId(String dataTenantId) {
        this.dataTenantId = dataTenantId;
    }
}
