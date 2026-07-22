package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.validation.Insert;
import cn.com.yusys.yusp.oca.validation.Update;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统角色表
 * 
 * @author wujp4
 * @date 2020-11-18 18:06:35
 */
@TableName("admin_sm_role")
public class AdminSmRoleEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 记录编号
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	@NotBlank(groups = Update.class,message="roleId can not be empty!")
	private String roleId;
	/**
	 * 角色代码
	 */
	@NotBlank(groups = Insert.class,message="roleCode can not be empty!")
	private String roleCode;
	/**
	 * 角色名称
	 */
	@NotBlank(groups = Insert.class,message="roleName can not be empty!")
	private String roleName;
	/**
	 * 所属机构编号
	 */
	@NotBlank(groups = Insert.class,message="orgId can not be empty!")
	private String orgId;
	/**
	 * 角色层级
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer roleLevel;
	/**
	 * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
	 */
	private AvailableStateEnum roleSts;

    public AdminSmRoleEntity() {
    }

    public @NotBlank(groups = Update.class, message = "roleId can not be empty!") String getRoleId() {
        return this.roleId;
    }

    public @NotBlank(groups = Insert.class, message = "roleCode can not be empty!") String getRoleCode() {
        return this.roleCode;
    }

    public @NotBlank(groups = Insert.class, message = "roleName can not be empty!") String getRoleName() {
        return this.roleName;
    }

    public @NotBlank(groups = Insert.class, message = "orgId can not be empty!") String getOrgId() {
        return this.orgId;
    }

    public Integer getRoleLevel() {
        return this.roleLevel;
    }

    public AvailableStateEnum getRoleSts() {
        return this.roleSts;
    }


    public void setRoleId(@NotBlank(groups = Update.class, message = "roleId can not be empty!") String roleId) {
        this.roleId = roleId;
    }

    public void setRoleCode(@NotBlank(groups = Insert.class, message = "roleCode can not be empty!") String roleCode) {
        this.roleCode = roleCode;
    }

    public void setRoleName(@NotBlank(groups = Insert.class, message = "roleName can not be empty!") String roleName) {
        this.roleName = roleName;
    }

    public void setOrgId(@NotBlank(groups = Insert.class, message = "orgId can not be empty!") String orgId) {
        this.orgId = orgId;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public void setRoleSts(AvailableStateEnum roleSts) {
        this.roleSts = roleSts;
    }

}
