package cn.com.yusys.yusp.oca.domain.bo;

/**
 * @author xufy1@yusys.com.cn
 * @desc 拖拽菜单
 * @date 2021-01-14 18:54
 */
public class DragMenuBo {

    /**
     * 拖动的菜单
     */
    private String dragMenuId;
    /**
     * 参照的菜单
     */
    private String refMenuId;
    /**
     * 相对于参照菜单的前后顺序标识
     */
    private String menuOrder;

    public DragMenuBo() {
    }

    public String getDragMenuId() {
        return this.dragMenuId;
    }

    public String getRefMenuId() {
        return this.refMenuId;
    }

    public String getMenuOrder() {
        return this.menuOrder;
    }

    public void setDragMenuId(String dragMenuId) {
        this.dragMenuId = dragMenuId;
    }

    public void setRefMenuId(String refMenuId) {
        this.refMenuId = refMenuId;
    }

    public void setMenuOrder(String menuOrder) {
        this.menuOrder = menuOrder;
    }
}
