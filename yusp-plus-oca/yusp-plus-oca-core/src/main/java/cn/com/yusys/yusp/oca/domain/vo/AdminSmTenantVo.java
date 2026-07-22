package cn.com.yusys.yusp.oca.domain.vo;

/**
 * @description:
 * @author: Zhan YongQiang
 * @date: 2021/9/18 9:54
 */
public class AdminSmTenantVo {

    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 租户单位名称
     */
    private String companyName;
    /**
     * 租户状态 0:启用，1停用
     */
    private String tenantSts;

    /**
     * 机构ID
     */
    private String orgId;

    /**
     * 机构代码
     */
    private String orgName;

    /**
     * 机构名称
     */
    private String orgCode;

    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 角色代码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 用户ID
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

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTenantSts() {
        return tenantSts;
    }

    public void setTenantSts(String tenantSts) {
        this.tenantSts = tenantSts;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
