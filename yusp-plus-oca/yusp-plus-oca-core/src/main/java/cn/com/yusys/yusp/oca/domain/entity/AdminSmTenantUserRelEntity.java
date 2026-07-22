package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.oca.validation.Insert;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;

/**
 * 租户超级用户关联表
 *
 * @author 陈静
 * @date 2022-03-31 18:06:35
 */
@TableName("admin_sm_tenant_user_rel")
public class AdminSmTenantUserRelEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String tenantUserRelId;
    /**
     * 用户编号
     */
    @NotBlank(groups = Insert.class, message = "userId can not be empty!")
    private String userId;
    /**
     * 角色编号
     */
    @NotBlank(groups = Insert.class, message = "dataTenantId can not be empty!")
    private String dataTenantId;



    public AdminSmTenantUserRelEntity(@NotBlank(groups = Insert.class, message = "userId can not be empty!") String userId, @NotBlank(groups = Insert.class, message = "dataTenantId can not be empty!") String dataTenantId) {
        this.userId = userId;
        this.dataTenantId = dataTenantId;
    }

    public AdminSmTenantUserRelEntity() {
    }

    public String getTenantUserRelId() {
        return tenantUserRelId;
    }

    public void setTenantUserRelId(String tenantUserRelId) {
        this.tenantUserRelId = tenantUserRelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getdataTenantId() {
        return dataTenantId;
    }

    public void setdataTenantId(String dataTenantId) {
        this.dataTenantId = dataTenantId;
    }
}
