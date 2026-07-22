package cn.com.yusys.yusp.oca.domain.vo;

/**
 * 菜单信息实体
 *
 * @author zhanyq
 * @date 2021-06-24 16:19
 */
public class AdminSmMenuVo {
    /**
     * 菜单编号
     */
    private String menuId;
    /**
     * 上级菜单编号
     */
    private String upMenuId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 上级菜单名称
     */
    private String upMenuName;
    /**
     * 业务功能编号
     */
    private String funcId;
    /**
     * 是否已经选择的字段
     */
    private int mark;

    public AdminSmMenuVo() {
    }

    public String getMenuId() {
        return this.menuId;
    }

    public String getUpMenuId() {
        return this.upMenuId;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public String getUpMenuName() {
        return this.upMenuName;
    }

    public String getFuncId() {
        return this.funcId;
    }

    public int getMark() {
        return this.mark;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public void setUpMenuId(String upMenuId) {
        this.upMenuId = upMenuId;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setUpMenuName(String upMenuName) {
        this.upMenuName = upMenuName;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
