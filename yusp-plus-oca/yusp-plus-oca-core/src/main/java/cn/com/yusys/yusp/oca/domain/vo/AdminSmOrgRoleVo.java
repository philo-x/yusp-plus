package cn.com.yusys.yusp.oca.domain.vo;

/**
 * 用户机构角色
 *
 * @author yinjun
 * @date 2022-4-25
 */
public class AdminSmOrgRoleVo {
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 角色名称
     */
    private String roleName;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
