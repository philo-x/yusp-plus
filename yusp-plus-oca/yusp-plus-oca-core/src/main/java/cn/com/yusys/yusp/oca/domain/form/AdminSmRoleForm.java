package cn.com.yusys.yusp.oca.domain.form;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @author danyu
 */
public class AdminSmRoleForm {

    /**
     * {"roleCode":"R112","roleName":"测试角色2","orgId":"100","roleSts":"A","lastChgUsr":"40"}
     *
     */
    /**
     * 角色代码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 所属机构编号
     */
    private String orgId;
    /**
     * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
     */
    private String roleSts;
    /**
     * 最新变更用户
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String lastChgUsr;

    private String roleId;
    private String roleLevel;

    public AdminSmRoleForm() {
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getRoleSts() {
        return this.roleSts;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public String getRoleLevel() {
        return this.roleLevel;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setRoleSts(String roleSts) {
        this.roleSts = roleSts;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }

}
