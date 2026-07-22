package cn.com.yusys.yusp.oca.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;


/**
 * 系统用户 excel解析实体
 * @author zhanyq
 * @date 2021-06-30 17:40
 */
public class AdminSmUserExcelVo {


    private String userId;

    @ExcelProperty("账号")
    private String loginCode;

    @ExcelProperty("用户名")
    private String userName;

    @ExcelProperty("工号")
    private String userCode;

    @ExcelProperty("机构代码")
    private String orgId;

    @ExcelProperty("性别")
    private String userSex;

    @ExcelProperty("角色代码")
    private String roleId;

    public AdminSmUserExcelVo() {
    }


    public String getUserId() {
        return this.userId;
    }

    public String getLoginCode() {
        return this.loginCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getUserSex() {
        return this.userSex;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }


}
