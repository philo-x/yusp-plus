package cn.com.yusys.yusp.oca.domain.vo;

import java.io.Serializable;

/**
 * @author lty
 * @description oca返回封装
 * @date 2021/6/24
 */
public class UserInfoForPasswordDto implements Serializable {

    /**
     * userId
     */
    private String userId;

    /**
     * 登陆码
     */
    private String loginCode;

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 渠道互斥标识
     */
    private Boolean loginSingleAgent;

    public UserInfoForPasswordDto() {
    }

    public String getUserId() {
        return this.userId;
    }

    public String getLoginCode() {
        return this.loginCode;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public Boolean getLoginSingleAgent() {
        return this.loginSingleAgent;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setLoginSingleAgent(Boolean loginSingleAgent) {
        this.loginSingleAgent = loginSingleAgent;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}