package cn.com.yusys.yusp.oca.domain.vo;

/**
 * 已被授权的数据权限模板
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
public class AdminTmplAndRecoVo {
    /**
     * 关联的控制点 Id
     */
    private String contrId;
    /**
     * 授权记录Id
     */
    private String authRecoId;
    /**
     * 记录编号
     */
    private String authTmplId;
    /**
     * 数据权限模板名
     */
    private String authTmplName;
    /**
     * 数据权限SQL条件
     */
    private String sqlString;
    /**
     * SQL占位符名称
     */
    private String sqlName;
    /**
     * 用于表示该数据模板有没有被控制点关联，0未关联，1关联
     */
    private int status;
    /**
     * 优先级,值越小优先级越高
     */
    private String priority;

    public AdminTmplAndRecoVo() {
    }

    public String getContrId() {
        return this.contrId;
    }

    public String getAuthRecoId() {
        return this.authRecoId;
    }

    public String getAuthTmplId() {
        return this.authTmplId;
    }

    public String getAuthTmplName() {
        return this.authTmplName;
    }

    public String getSqlString() {
        return this.sqlString;
    }

    public String getSqlName() {
        return this.sqlName;
    }

    public int getStatus() {
        return this.status;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public void setAuthRecoId(String authRecoId) {
        this.authRecoId = authRecoId;
    }

    public void setAuthTmplId(String authTmplId) {
        this.authTmplId = authTmplId;
    }

    public void setAuthTmplName(String authTmplName) {
        this.authTmplName = authTmplName;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
