package cn.com.yusys.yusp.uaa.controller.vo;

/**
 * @author yinjun
 * @version 1.2
 * @description: 用户信息
 * @date 2022/3/11
 **/
public class UserInfoVo {
    private String clientId;
    private String userId;
    private String loginCode;
    private String organizationId;
    private String roleId;

    public UserInfoVo(String clientId, String userId, String loginCode, String organizationId, String roleId) {
        this.clientId = clientId;
        this.userId = userId;
        this.loginCode = loginCode;
        this.organizationId = organizationId;
        this.roleId = roleId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
