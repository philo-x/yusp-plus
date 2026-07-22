package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 资源对象授权记录表(含菜单、控制点、数据权限)
 *
 * @author zhangyt12
 * @date 2020-11-27 14:57:08
 */
@TableName("admin_sm_auth_reco")
public class AdminSmAuthRecoEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId
    private String authRecoId;
    /**
     * 逻辑系统记录编号
     */
    private String sysId;
    /**
     * 授权对象类型（R-角色，U-用户，D-部门，G-机构，OU-对象组）
     */
    private String authobjType;
    /**
     * 授权对象记录编号
     */
    private String authobjId;
    /**
     * 授权资源类型（M-菜单，C-控制点，D-数据权限）
     */
    private String authresType;
    /**
     * 授权资源记录编号
     */
    private String authresId;

    /**
     * 菜单ID
     */
    private String menuId;

    public AdminSmAuthRecoEntity() {
    }

    public String getAuthRecoId() {
        return this.authRecoId;
    }

    public String getSysId() {
        return this.sysId;
    }

    public String getAuthobjType() {
        return this.authobjType;
    }

    public String getAuthobjId() {
        return this.authobjId;
    }

    public String getAuthresType() {
        return this.authresType;
    }

    public String getAuthresId() {
        return this.authresId;
    }



    public String getMenuId() {
        return this.menuId;
    }

    public void setAuthRecoId(String authRecoId) {
        this.authRecoId = authRecoId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setAuthobjType(String authobjType) {
        this.authobjType = authobjType;
    }

    public void setAuthobjId(String authobjId) {
        this.authobjId = authobjId;
    }

    public void setAuthresType(String authresType) {
        this.authresType = authresType;
    }

    public void setAuthresId(String authresId) {
        this.authresId = authresId;
    }


    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminSmAuthRecoEntity that = (AdminSmAuthRecoEntity) o;
        return Objects.equals(authRecoId, that.authRecoId) && Objects.equals(sysId, that.sysId) && Objects.equals(authobjType, that.authobjType) && Objects.equals(authobjId, that.authobjId) && Objects.equals(authresType, that.authresType) && Objects.equals(authresId, that.authresId)  && Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authRecoId, sysId, authobjType, authobjId, authresType, authresId,menuId);
    }

}
