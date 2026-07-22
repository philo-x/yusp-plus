package cn.com.yusys.yusp.oca.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * 部门树Vo
 *
 * @author: wujp4
 * @create: 2020-11-19 09:36
 */
public class DptTreeVo {

	/**
	 * 记录编号
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
	private String belongOrgId;
	/**
	 * 上级部门记录编号
	 */
	private String upDptId;
	/**
	 * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
	 */
	private String dptSts;
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

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<DptTreeVo> children;

    public String getDptId() {
        return this.dptId;
    }

    public String getDptCode() {
        return this.dptCode;
    }

    public String getDptName() {
        return this.dptName;
    }

    public String getBelongOrgId() {
        return this.belongOrgId;
    }

    public String getUpDptId() {
        return this.upDptId;
    }

    public String getDptSts() {
        return this.dptSts;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public List<DptTreeVo> getChildren() {
        return this.children;
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

    public void setBelongOrgId(String belongOrgId) {
        this.belongOrgId = belongOrgId;
    }

    public void setUpDptId(String upDptId) {
        this.upDptId = upDptId;
    }

    public void setDptSts(String dptSts) {
        this.dptSts = dptSts;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public void setChildren(List<DptTreeVo> children) {
        this.children = children;
    }
}
