package cn.com.yusys.yusp.uaa.pojo;

/**
 * @author lty
 * @description 用于接收userId，orgId，LoginCode，businessCode
 * @date 2020/12/29
 */
public class TokenParamDto {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 密码
     */
    private String password;

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 登陆码
     */
    private String loginCode;

    /**
     * 业务状态码
     */
    private String businessCode;

    /**
     * 互斥标识
     */
    private Boolean loginSingleAgent;

    public TokenParamDto() {
    }


    public String getUserId() {
        return this.userId;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getLoginCode() {
        return this.loginCode;
    }

    public String getBusinessCode() {
        return this.businessCode;
    }

    public Boolean getLoginSingleAgent() {
        return this.loginSingleAgent;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
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