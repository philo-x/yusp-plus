package cn.com.yusys.yusp.uaa.pojo;

import java.io.Serializable;

/**
 * @author lty
 * @description oca返回封装
 * @date 2021/6/24
 */
public class UserInfoForOcaDto implements Serializable {

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
    private String password;

    /**
     * 渠道互斥标识
     */
    private Boolean loginSingleAgent;

    public UserInfoForOcaDto() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}