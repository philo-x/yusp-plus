package cn.com.yusys.yusp.oca.domain.vo;


import java.io.Serializable;

/**
 * 用户返回信息
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class UserEntityVo implements Serializable {

    private String userId;

    private String loginCode;

    private String orgId;
    /**
     * 渠道互斥标识
     */
    private Boolean loginSingleAgent;

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
}
