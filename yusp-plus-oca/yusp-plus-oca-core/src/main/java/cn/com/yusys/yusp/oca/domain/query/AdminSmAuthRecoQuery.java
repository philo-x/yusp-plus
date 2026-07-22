package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmAuthRecoEntity;
/**
 * 授权记录查询条件
 * @author zhanyq
 * @date 2021-06-30 17:20
 */
public class AdminSmAuthRecoQuery extends PageQuery<AdminSmAuthRecoEntity> {

    /**
     * 逻辑系统记录编号
     */
    private String sysId;
    /**
     * 授权对象类型（R-角色，U-用户，D-部门，G-机构，OU-对象组）
     */
    private String objectType;
    /**
     * 授权对象记录编号
     */
    private String objectId;
    /**
     * 授权资源类型（M-菜单，C-控制点，D-数据权限）
     */
    private String[] resourceType;

    public AdminSmAuthRecoQuery() {
    }

    public String getSysId() {
        return this.sysId;
    }

    public String getObjectType() {
        return this.objectType;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public String[] getResourceType() {
        return this.resourceType;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setResourceType(String[] resourceType) {
        this.resourceType = resourceType;
    }


}

