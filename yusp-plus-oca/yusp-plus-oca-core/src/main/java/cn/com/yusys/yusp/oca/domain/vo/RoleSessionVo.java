package cn.com.yusys.yusp.oca.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * session中role信息
 *
 * @author: wujp4
 * @create: 2020-11-19 11:30
 */
public class RoleSessionVo {

    /**
     * 记录编号
     */
    @JsonProperty(value = "id")
    private String roleId;
    /**
     * 角色代码
     */
    @JsonProperty(value = "code")
    private String roleCode;
    /**
     * 角色名称
     */
    @JsonProperty(value = "name")
    private String roleName;

    public String getRoleId() {
        return this.roleId;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public String getRoleName() {
        return this.roleName;
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
}
