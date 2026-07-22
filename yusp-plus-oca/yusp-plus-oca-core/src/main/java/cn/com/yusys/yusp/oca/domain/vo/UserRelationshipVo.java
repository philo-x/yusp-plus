package cn.com.yusys.yusp.oca.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 带关联状态的用户列表（机构、角色、岗位）
 *
 * @author terry
 * @date 2020-11-11 18:38:35
 */
public class UserRelationshipVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    private String userId;
    /**
     * 账号
     */
    private String loginCode;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 员工号
     */
    private String userCode;
    /**
     * 有效期到
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deadline;
    /**
     * 所属机构编号
     */
    private String orgId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 关联信息状态
     */
    private Boolean checked = false;

    /**
     * 最新变更用户
     */
    private String lastChgUsr;
    /**
     * 最新变更时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastChgDt;


    public String getUserId() {
        return this.userId;
    }

    public String getLoginCode() {
        return this.loginCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public Date getDeadline() {
        return this.deadline;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public Boolean getChecked() {
        return this.checked;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

}
