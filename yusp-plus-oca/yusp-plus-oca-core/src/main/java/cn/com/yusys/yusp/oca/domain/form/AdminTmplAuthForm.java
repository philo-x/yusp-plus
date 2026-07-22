package cn.com.yusys.yusp.oca.domain.form;
/**
 * 数据模板授权
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
public class AdminTmplAuthForm {
    /**
     * 之前的数据模板授权的数据 id
     */
    private String lastAuthresId;
    /**
     * 授权对象 id
     */
    private String authobjId;
    /**
     * 授权对象类型
     */
    private String authobjType;
    /**
     * 新授权的数据模板 id
     */
    private String authresId;
    /**
     * 相关联的控制点 id
     */
    private String contrId;

    /**
     * 租户ID
     */
    private String dataTenantId;

    public AdminTmplAuthForm() {
    }

    public String getLastAuthresId() {
        return this.lastAuthresId;
    }

    public String getAuthobjId() {
        return this.authobjId;
    }

    public String getAuthobjType() {
        return this.authobjType;
    }

    public String getAuthresId() {
        return this.authresId;
    }

    public String getContrId() {
        return this.contrId;
    }

    public void setLastAuthresId(String lastAuthresId) {
        this.lastAuthresId = lastAuthresId;
    }

    public void setAuthobjId(String authobjId) {
        this.authobjId = authobjId;
    }

    public void setAuthobjType(String authobjType) {
        this.authobjType = authobjType;
    }

    public void setAuthresId(String authresId) {
        this.authresId = authresId;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public String getDataTenantId() {
        return dataTenantId;
    }

    public void setDataTenantId(String dataTenantId) {
        this.dataTenantId = dataTenantId;
    }
}
