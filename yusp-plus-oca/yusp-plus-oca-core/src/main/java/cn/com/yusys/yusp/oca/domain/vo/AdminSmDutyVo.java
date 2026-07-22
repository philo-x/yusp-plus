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
 * 系统岗位表
 * 
 * @author danyb1
 * @date 2020-12-01 21:55:19
 */
public class AdminSmDutyVo {

	private String dutyId;
	/**
	 * 岗位代码
	 */
	private String dutyCode;
	/**
	 * 岗位名称
	 */
	private String dutyName;
	/**
	 * 所属机构编号
	 */
	private String orgId;
	/**
	 * 备注
	 */
	private String dutyRemark;
	/**
	 * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
	 */
	private AvailableStateEnum dutySts;
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

    public AdminSmDutyVo() {
    }

    public String getDutyId() {
        return this.dutyId;
    }

    public String getDutyCode() {
        return this.dutyCode;
    }

    public String getDutyName() {
        return this.dutyName;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getDutyRemark() {
        return this.dutyRemark;
    }

    public AvailableStateEnum getDutySts() {
        return this.dutySts;
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


    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public void setDutyCode(String dutyCode) {
        this.dutyCode = dutyCode;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setDutyRemark(String dutyRemark) {
        this.dutyRemark = dutyRemark;
    }

    public void setDutySts(AvailableStateEnum dutySts) {
        this.dutySts = dutySts;
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


}
