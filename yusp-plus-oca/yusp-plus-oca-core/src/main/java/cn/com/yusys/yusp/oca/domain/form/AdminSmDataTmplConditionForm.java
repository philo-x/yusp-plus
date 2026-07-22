package cn.com.yusys.yusp.oca.domain.form;

import java.util.List;
/**
 *查询控制点可授权的数据权限模板列表条件封装类
 *
 * @author zhangyt12
 * @date 2020-11-27 14:57:08
 */
public class AdminSmDataTmplConditionForm {
    /**
     * 查询的控制点 id
     */
    private String contrId;
    /**
     * 用于前端复选框反选
     */
    private List<String> lastTmplIds;
    /**
     * 关键字
     */
    private String keyWord;
    /**
     * 1：复选框选择
     * 0：复选框未选
     */
    private int check;

    private int page;

    private int size;

    /**
     * 租户ID
     */
    private String dataTenantId;

    public AdminSmDataTmplConditionForm() {
    }

    public String getContrId() {
        return this.contrId;
    }

    public List<String> getLastTmplIds() {
        return this.lastTmplIds;
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

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public void setLastTmplIds(List<String> lastTmplIds) {
        this.lastTmplIds = lastTmplIds;
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

    public String getDataTenantId() {
        return dataTenantId;
    }

    public void setDataTenantId(String dataTenantId) {
        this.dataTenantId = dataTenantId;
    }
}
