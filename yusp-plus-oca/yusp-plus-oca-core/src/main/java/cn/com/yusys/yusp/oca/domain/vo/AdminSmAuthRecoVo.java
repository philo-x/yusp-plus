package cn.com.yusys.yusp.oca.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 资源对象授权记录表(含菜单、控制点、数据权限)
 *
 * @author wujp4
 * @email wujp4@yusys.com.cn
 * @date 2020-11-19 17:43:42
 */
public class AdminSmAuthRecoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
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
     * 最新变更用户
     */
    private String lastChgUsr;
    /**
     * 最新变更时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;
    /**
     * 菜单ID
     */
    private String menuId;

    public AdminSmAuthRecoVo() {
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

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
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

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

}
