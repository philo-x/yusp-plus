package cn.com.yusys.yusp.oca.domain.vo;

/**
 * 用户所在机构可关联的角色列表以及实际关联状态
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class UserRoleRelVo {
    private String roleId;
    private String roleCode;
    private String roleName;
    private Boolean checked=false;

    public String getRoleId() {
        return this.roleId;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public Boolean getChecked() {
        return this.checked;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
