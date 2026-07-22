package cn.com.yusys.yusp.oca.domain.form;
/**
 * 授权树实体类
 *
 * @author zhanyq
 * @date 2021-06-24 16:23
 */
public class AdminSmAuthTreeForm {
    /**
     * 被授权对象 id
     */
    private String authObjId;
    /**
     * 被授权对象的类型
     */
    private String authObjType;
    /**
     * 授权角色 id
     */
    private String userRoleId;
    /**
     * 模糊查询关键字
     */
    private String keyWord;

    /**
     * 租户ID，这个是在租户管理页面修改时需要用到的
     */
    private String dataTenantId;

    /**
     * 当前页码
     */
    private int page;
    /**
     * 分页记录条数
     */
    private int size;

    public AdminSmAuthTreeForm() {
    }

    public String getAuthObjId() {
        return this.authObjId;
    }

    public String getAuthObjType() {
        return this.authObjType;
    }

    public String getUserRoleId() {
        return this.userRoleId;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public int getPage() {
        return this.page;
    }

    public int getSize() {
        return this.size;
    }

    public void setAuthObjId(String authObjId) {
        this.authObjId = authObjId;
    }

    public void setAuthObjType(String authObjType) {
        this.authObjType = authObjType;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
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
