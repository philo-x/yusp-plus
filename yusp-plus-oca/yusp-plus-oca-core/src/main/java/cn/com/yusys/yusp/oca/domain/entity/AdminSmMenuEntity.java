package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 系统菜单表
 *
 * @author wujp4
 * @date 2020-11-19 16:32:07
 */
@TableName("admin_sm_menu")
public class AdminSmMenuEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 菜单编号
     */
    @TableId
    private String menuId;
    /**
     * 逻辑系统记录编号
     */
    private String sysId;
    /**
     * 业务功能编号
     */
    @TableField(value = "func_id", updateStrategy = FieldStrategy.ALWAYS)
    private String funcId;
    /**
     * 上级菜单编号
     */
    private String upMenuId;
    /**
     * 菜单名称
     */
    @NotBlank(message = "menuName can not be empty!")
    private String menuName;
    /**
     * 顺序
     */
    private Integer menuOrder;
    /**
     * 图标
     */
    private String menuIcon;
    /**
     * 说明(菜单描述)
     */
    private String menuTip;
    /**
     * 国际化key值
     */
    private String i18nKey;
    /**
     * 最新变更用户
     */

    private String lastChgUsr;

    @Override
    public String getLastChgUsr() {
        return lastChgUsr;
    }

    @Override
    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    @Override
    public Date getLastChgDt() {
        return lastChgDt;
    }

    @Override
    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    /**
     * 最新变更时间
     */
    private Date lastChgDt;
    /**
     * 菜单分类
     */
    @NotBlank(message = "menuClassify can not be empty!")
    private String menuClassify;
    /**
     * 逻辑删除；
     * 去掉@TableLogic不使用逻辑删除，改用物理删除 -zhanyq -20210713
     */
    private Integer deleted;

    public AdminSmMenuEntity() {
    }

    public String getMenuId() {
        return this.menuId;
    }

    public String getSysId() {
        return this.sysId;
    }

    public String getFuncId() {
        return this.funcId;
    }

    public String getUpMenuId() {
        return this.upMenuId;
    }

    public @NotBlank(message = "menuName can not be empty!") String getMenuName() {
        return this.menuName;
    }

    public Integer getMenuOrder() {
        return this.menuOrder;
    }

    public String getMenuIcon() {
        return this.menuIcon;
    }

    public String getMenuTip() {
        return this.menuTip;
    }

    public String getI18nKey() {
        return this.i18nKey;
    }


    public @NotBlank(message = "menuClassify can not be empty!") String getMenuClassify() {
        return this.menuClassify;
    }

    public Integer getDeleted() {
        return this.deleted;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public void setUpMenuId(String upMenuId) {
        this.upMenuId = upMenuId;
    }

    public void setMenuName(@NotBlank(message = "menuName can not be empty!") String menuName) {
        this.menuName = menuName;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public void setMenuTip(String menuTip) {
        this.menuTip = menuTip;
    }

    public void setI18nKey(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    public void setMenuClassify(@NotBlank(message = "menuClassify can not be empty!") String menuClassify) {
        this.menuClassify = menuClassify;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminSmMenuEntity that = (AdminSmMenuEntity) o;
        return Objects.equals(menuId, that.menuId) && Objects.equals(sysId, that.sysId) && Objects.equals(funcId, that.funcId) && Objects.equals(upMenuId, that.upMenuId) && Objects.equals(menuName, that.menuName) && Objects.equals(menuOrder, that.menuOrder) && Objects.equals(menuIcon, that.menuIcon) && Objects.equals(menuTip, that.menuTip) && Objects.equals(i18nKey, that.i18nKey) && Objects.equals(menuClassify, that.menuClassify) && Objects.equals(deleted, that.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, sysId, funcId, upMenuId, menuName, menuOrder, menuIcon, menuTip, i18nKey, menuClassify, deleted);
    }
}
