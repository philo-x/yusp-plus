package cn.com.yusys.yusp.notice.vo;

/**
 * 系统公告表接收连表查询返回数据的 vo 对象
 * @author zhangyt12
 * @date 2021/6/24 15:43
 */
public class AdminSmNoticeReciveVo {
    /**
     * 记录编号
     */
    private String reciveId;
    /**
     * 对象类型
     */
    private String reciveType;
    /**
     * 对象记录编号
     */
    private String reciveOgjId;
    /**
     * 发布对象角色 id
     */
    private String roleId;
    /**
     * 发布对象角色名称
     */
    private String roleName;
    /**
     * 发布对象机构 id
     */
    private String orgId;
    /**
     * 发布对象机构名称
     */
    private String orgName;

    public AdminSmNoticeReciveVo() {
    }

    public String getReciveId() {
        return this.reciveId;
    }

    public String getReciveType() {
        return this.reciveType;
    }

    public String getReciveOgjId() {
        return this.reciveOgjId;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public void setReciveId(String reciveId) {
        this.reciveId = reciveId;
    }

    public void setReciveType(String reciveType) {
        this.reciveType = reciveType;
    }

    public void setReciveOgjId(String reciveOgjId) {
        this.reciveOgjId = reciveOgjId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
