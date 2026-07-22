package cn.com.yusys.yusp.oca.domain.form;
/**
 * 数据权限模板查询表单
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
public class AdminSmDataAuthTmplForm {
    /**
     * 数据权限模板名称
     */
    private String authTmplName;
    /**
     * 数据权限SQL条件
     */
    private String sqlName;
    /**
     * 查询关键字
     */
    private String keyWord;
    /**
     * 当前查询的页数
     */
    private Long page;
    /**
     * 当前查询的页面的条数
     */
    private Long size;

    public AdminSmDataAuthTmplForm() {
    }

    public String getAuthTmplName() {
        return this.authTmplName;
    }

    public String getSqlName() {
        return this.sqlName;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public Long getPage() {
        return this.page;
    }

    public Long getSize() {
        return this.size;
    }

    public void setAuthTmplName(String authTmplName) {
        this.authTmplName = authTmplName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
