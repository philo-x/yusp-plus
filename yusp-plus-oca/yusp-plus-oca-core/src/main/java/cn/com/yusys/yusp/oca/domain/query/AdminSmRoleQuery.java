package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmRoleVo;

import java.util.List;

/**
 * 分页查询条件实体类
 *
 * @author terry
 * @date 2020-11-18 18:06:35
 */
public class AdminSmRoleQuery extends PageQuery<AdminSmRoleVo> {
    /**
     * 所属机构编号
     */
    private String orgId;
    /**
     * 关键字
     */
    private String keyWord;
    /**
     * 角色代码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 状态
     */
    private AvailableStateEnum roleSts;
    /**
     * 需要排除的角色id
     */
    private List<String> expectedRoleId;

    public AdminSmRoleQuery() {
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public AvailableStateEnum getRoleSts() {
        return this.roleSts;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleSts(AvailableStateEnum roleSts) {
        this.roleSts = roleSts;
    }

    public List<String> getExpectedRoleId() {
        return expectedRoleId;
    }

    public void setExpectedRoleId(List<String> expectedRoleId) {
        this.expectedRoleId = expectedRoleId;
    }
}
