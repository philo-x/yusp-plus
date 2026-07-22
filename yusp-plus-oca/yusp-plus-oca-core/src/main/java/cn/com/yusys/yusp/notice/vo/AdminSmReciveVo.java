package cn.com.yusys.yusp.notice.vo;

/**
 * 系统公告表接收连表查询返回数据的 vo 对象
 * @author zhangyt12
 * @date 2021/6/24 15:43
 */
public class AdminSmReciveVo {
    /**
     * 发布对象角色 id
     */
    private String roleId;
    /**
     * 发布对像机构 id
     */
    private String orgId;

    public AdminSmReciveVo() {
    }

    public String getRoleId() {
        return this.roleId;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
