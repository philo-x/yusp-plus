package cn.com.yusys.yusp.oca.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * excel入库-角色Vo
 * @author zhanyq
 * @date 2021-06-30 17:34
 */
public class AdminSmRoleExcelVo {

    @ExcelProperty("角色代码")
    private String roleCode;

    @ExcelProperty("角色名称")
    private String roleName;

    @ExcelProperty("机构代码")
    private String orgId;

    @ExcelProperty("角色级别")
    private Integer roleLevel;

    public AdminSmRoleExcelVo() {
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

    public Integer getRoleLevel() {
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

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }


}
