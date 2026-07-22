package cn.com.yusys.yusp.uaa.pojo;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author lty
 * @description 生成token所需信息 userId,orgId,loginCode
 * @date 2020/12/29
 */
public class LoginUserInfo extends User {

    /**
     * 用户id
     */
    private String userId;

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

    public LoginUserInfo(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public LoginUserInfo(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public Boolean getLoginSingleAgent() {
        return loginSingleAgent;
    }

    public void setLoginSingleAgent(Boolean loginSingleAgent) {
        this.loginSingleAgent = loginSingleAgent;
    }
}
