package cn.com.yusys.yusp.oca.domain.form;
/**
 * 查询菜单页面的条件封装类
 *
 * @author zhangyt12
 * @date 2020-11-27 14:57:08
 */
public class AdminSmMenuConditionForm {
    /**
     * 如果是修改，上次选择菜单的 id
     */
    private String lastMenuId;
    /**
     * 关键字查询
     */
    private String keyWord;
    /**
     * 1：复选框选择
     * 0：复选框未选
     */
    private int check;
    /**
     * 页码
     */
    private int page;
    /**
     * 每页大小
     */
    private int size;

    public AdminSmMenuConditionForm() {
    }

    public String getLastMenuId() {
        return this.lastMenuId;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public int getCheck() {
        return this.check;
    }

    public int getPage() {
        return this.page;
    }

    public int getSize() {
        return this.size;
    }

    public void setLastMenuId(String lastMenuId) {
        this.lastMenuId = lastMenuId;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
