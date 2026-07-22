package cn.com.yusys.yusp.oca.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;


/**
 * 资源控制点数据模板VO
 *
 * @author: wujp4
 * @create: 2020-11-19 11:45
 */
public class ResControlDataAuthTmplVo {

	/**
	 * 数据授权记录id
	 */
	private String authId;
	/**
	 * 控制点id
	 */
	private String contrId;
	/**
	 * 数据授权模板id
	 */
	private String authTmplId;
	/**
	 * 数据权限模板名
	 */
	private String authTmplName;
	/**
	 * 数据权限SQL条件
	 */
	private String sqlString;
	/**
	 * 最新变更用户
	 */
	private String lastChgName;
	/**
	 * 最新变更时间
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;

    public String getAuthId() {
        return this.authId;
    }

    public String getContrId() {
        return this.contrId;
    }

    public String getAuthTmplId() {
        return this.authTmplId;
    }

    public String getAuthTmplName() {
        return this.authTmplName;
    }

    public String getSqlString() {
        return this.sqlString;
    }

    public String getLastChgName() {
        return this.lastChgName;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public void setAuthTmplId(String authTmplId) {
        this.authTmplId = authTmplId;
    }

    public void setAuthTmplName(String authTmplName) {
        this.authTmplName = authTmplName;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }

    public void setLastChgName(String lastChgName) {
        this.lastChgName = lastChgName;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }
}
