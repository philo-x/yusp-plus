package cn.com.yusys.yusp.oca.domain.vo;
/**
 * 系统功能控制点关联的数据模板
 *
 * @author zhangyt12
 * @date 2020-11-27 14:57:08
 */
public class AdminDataAuthVo {
    /**
     * 记录编号
     */
    private String authId;
    /**
     * 控制点的id
     */
    private String contrId;
    /**
     * 数据模板的id
     */
    private String authTmplId;
    /**
     * 数据模板的名称
     */
    private String authTmplName;
    /**
     * sql名称
     */
    private String sqlName;
    /**
     * sql语句
     */
    private String sqlString;
    /**
     * 数据模板的优先级
     */
    private String priority;

    private int state = 0;

    public AdminDataAuthVo() {
    }

    public String getAuthId() {
        return this.authId;
    }

    public String getContrId() {
        return this.contrId;
    }

    public String getAuthTmplId() {
        return this.authTmplId;
    }

    public String getAuthTmplName() {
        return this.authTmplName;
    }

    public String getSqlName() {
        return this.sqlName;
    }

    public String getSqlString() {
        return this.sqlString;
    }

    public String getPriority() {
        return this.priority;
    }

    public int getState() {
        return this.state;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public void setAuthTmplId(String authTmplId) {
        this.authTmplId = authTmplId;
    }

    public void setAuthTmplName(String authTmplName) {
        this.authTmplName = authTmplName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setState(int state) {
        this.state = state;
    }
}
