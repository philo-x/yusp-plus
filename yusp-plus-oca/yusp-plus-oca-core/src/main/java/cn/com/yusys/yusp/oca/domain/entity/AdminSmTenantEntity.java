package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * 租户管理表
 *
 * @author zhanyq
 * @date 2021-09-17 18:29:55
 */
@TableName("admin_sm_tenant")
public class AdminSmTenantEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    @TableId
    private String tenantId;
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 租户单位名称
     */
    private String companyName;
    /**
     * 租户状态 0:启用，1停用
     */
    private String tenantSts;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTenantSts() {
        return tenantSts;
    }

    public void setTenantSts(String tenantSts) {
        this.tenantSts = tenantSts;
    }


}
