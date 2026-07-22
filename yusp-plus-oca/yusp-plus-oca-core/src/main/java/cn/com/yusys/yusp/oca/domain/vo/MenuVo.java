package cn.com.yusys.yusp.oca.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
/**
 * 菜单信息实体
 * @author zhanyq
 * @date 2021-06-24 15:51
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuVo {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 菜单ID
     */
    private String menuId;

    /**
     * 逻辑系统记录编号
     */
    private String sysId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 上级菜单编号
     */
    private String upMenuId;

    /**
     * 顺序
     */
    private Integer menuOrder;

    /**
     * 图标
     */
    private String menuIcon;

    /**
     * 业务功能编号
     */
    private String funcId;

    /**
     * 国际化key值
     */
    private String i18nKey;

    /**
     * 说明(菜单描述)
     */
    private String menuTip;

    /**
     * 菜单分类，0 菜单， 1是菜单目录
     */
    private String menuClassify;

    /**
     * 上级菜单名称
     */
    private String upMenuName;

    /**
     * 业务功能URL
     */
    private String funcUrl;

    /**
     * 子菜单集合
     */
    private List<MenuVo> childrenList;


    public String getId() {
        return this.id;
    }

    public String getMenuId() {
        return this.menuId;
    }

    public String getSysId() {
        return this.sysId;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public String getUpMenuId() {
        return this.upMenuId;
    }

    public Integer getMenuOrder() {
        return this.menuOrder;
    }

    public String getMenuIcon() {
        return this.menuIcon;
    }

    public String getFuncId() {
        return this.funcId;
    }

    public String getI18nKey() {
        return this.i18nKey;
    }


    public String getMenuTip() {
        return this.menuTip;
    }

    public String getMenuClassify() {
        return this.menuClassify;
    }

    public String getUpMenuName() {
        return this.upMenuName;
    }

    public String getFuncUrl() {
        return this.funcUrl;
    }

    public List<MenuVo> getChildrenList() {
        return this.childrenList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setUpMenuId(String upMenuId) {
        this.upMenuId = upMenuId;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public void setI18nKey(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    public void setMenuTip(String menuTip) {
        this.menuTip = menuTip;
    }

    public void setMenuClassify(String menuClassify) {
        this.menuClassify = menuClassify;
    }

    public void setUpMenuName(String upMenuName) {
        this.upMenuName = upMenuName;
    }

    public void setFuncUrl(String funcUrl) {
        this.funcUrl = funcUrl;
    }

    public void setChildrenList(List<MenuVo> childrenList) {
        this.childrenList = childrenList;
    }

}
