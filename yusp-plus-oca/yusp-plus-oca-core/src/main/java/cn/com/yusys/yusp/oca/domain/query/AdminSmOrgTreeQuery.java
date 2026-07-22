package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;


/**
 * 机构树查询实体
 *
 * @author terry
 * @date 2020-12-11 18:06:35
 */
public class AdminSmOrgTreeQuery {
    /**
     * 记录编号
     */
    private String orgId;
    /**
     * 状态 A：启用 I：停用 W：待启用
     */
    private AvailableStateEnum orgSts;

    public AdminSmOrgTreeQuery() {
    }

    public String getOrgId() {
        return this.orgId;
    }

    public AvailableStateEnum getOrgSts() {
        return this.orgSts;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setOrgSts(AvailableStateEnum orgSts) {
        this.orgSts = orgSts;
    }

}
