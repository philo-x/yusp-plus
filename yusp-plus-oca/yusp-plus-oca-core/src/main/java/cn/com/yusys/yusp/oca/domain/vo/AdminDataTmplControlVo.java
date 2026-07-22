package cn.com.yusys.yusp.oca.domain.vo;
/**
 * 数据权限模板VO
 * @author zhanyq
 * @date 2021-06-30 17:18
 */
public class AdminDataTmplControlVo {

    private String authId;

    private String sysId;

    private String contrId;

    private String contrUrl;

    private String authTmplId;

    private String sqlString;

    private String sqlName;

    private String priority;

    public AdminDataTmplControlVo() {
    }

    public String getAuthId() {
        return this.authId;
    }

    public String getSysId() {
        return this.sysId;
    }

    public String getContrId() {
        return this.contrId;
    }

    public String getContrUrl() {
        return this.contrUrl;
    }

    public String getAuthTmplId() {
        return this.authTmplId;
    }

    public String getSqlString() {
        return this.sqlString;
    }

    public String getSqlName() {
        return this.sqlName;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public void setContrUrl(String contrUrl) {
        this.contrUrl = contrUrl;
    }

    public void setAuthTmplId(String authTmplId) {
        this.authTmplId = authTmplId;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


}
