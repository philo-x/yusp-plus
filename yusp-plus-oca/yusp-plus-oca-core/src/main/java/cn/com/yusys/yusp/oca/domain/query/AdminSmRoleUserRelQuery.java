package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.vo.UserRelationshipVo;

import jakarta.validation.constraints.NotEmpty;

/**
 * 用角色去查用户列表
 *
 * @author terry
 * @date 2021-01-13 19:54:35
 */
public class AdminSmRoleUserRelQuery extends PageQuery<UserRelationshipVo> {
    /**
     * 记录编号
     */
    @NotEmpty
    private String roleId;
    /**
     * 员工号
     */
    private String userCode;
    /**
     * 账号
     */
    private String loginCode;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 所属机构编号
     */
    private String orgId;
    /**
     * 关联信息状态
     */
    private Boolean checked;

    public AdminSmRoleUserRelQuery() {
    }


    public @NotEmpty String getRoleId() {
        return this.roleId;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public String getLoginCode() {
        return this.loginCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public Boolean getChecked() {
        return this.checked;
    }

    public void setRoleId(@NotEmpty String roleId) {
        this.roleId = roleId;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

}
