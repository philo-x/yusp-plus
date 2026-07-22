package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import cn.com.yusys.yusp.oca.validation.Insert;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户角色关联表
 *
 * @author wujp4
 * @date 2020-11-18 18:06:35
 */
@TableName("admin_sm_user_role_rel")
public class AdminSmUserRoleRelEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String userRoleRelId;
    /**
     * 用户编号
     */
    @NotBlank(groups = Insert.class, message = "userId can not be empty!")
    private String userId;
    /**
     * 角色编号
     */
    @NotBlank(groups = Insert.class, message = "roleId can not be empty!")
    private String roleId;


    /**
     * 用户选中的角色，0未选择，1选中；默认为0
     */
    private int checked;

    public AdminSmUserRoleRelEntity(@NotBlank(groups = Insert.class, message = "userId can not be empty!") String userId, @NotBlank(groups = Insert.class, message = "roleId can not be empty!") String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public AdminSmUserRoleRelEntity() {
    }

    public String getUserRoleRelId() {
        return this.userRoleRelId;
    }

    public @NotBlank(groups = Insert.class, message = "userId can not be empty!") String getUserId() {
        return this.userId;
    }

    public @NotBlank(groups = Insert.class, message = "roleId can not be empty!") String getRoleId() {
        return this.roleId;
    }

    public void setUserRoleRelId(String userRoleRelId) {
        this.userRoleRelId = userRoleRelId;
    }

    public void setUserId(@NotBlank(groups = Insert.class, message = "userId can not be empty!") String userId) {
        this.userId = userId;
    }

    public void setRoleId(@NotBlank(groups = Insert.class, message = "roleId can not be empty!") String roleId) {
        this.roleId = roleId;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

}
