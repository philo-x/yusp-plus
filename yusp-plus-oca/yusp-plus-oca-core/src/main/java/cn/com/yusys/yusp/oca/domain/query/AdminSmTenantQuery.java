package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmTenantEntity;

/**
 * @description:
 * @author: Zhan YongQiang
 * @date: 2021/9/18 9:52
 */
public class AdminSmTenantQuery extends PageQuery<AdminSmTenantEntity> {

    /**
     * 关键字
     */
    private String tenantName;

    /**
     * 单位名称
     */
    private String companyName;

    /**
     * 租户状态 0:启用，1停用
     */
    private String tenantSts;


    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantSts() {
        return tenantSts;
    }

    public void setTenantSts(String tenantSts) {
        this.tenantSts = tenantSts;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
