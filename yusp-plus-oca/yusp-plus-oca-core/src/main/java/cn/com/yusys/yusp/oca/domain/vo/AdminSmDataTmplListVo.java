package cn.com.yusys.yusp.oca.domain.vo;
/**
 * 数据权限模板表与控制点的展示
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
public class AdminSmDataTmplListVo {
    /**
     * 数据权限模板的记录编号
     */
    private String authTmplId;
    /**
     * 数据权限模板的名称
     */
    private String authTmplName;
    /**
     * sql语句
     */
    private String sqlString;
    /**
     * sql名称
     */
    private String sqlName;
    /**
     * 用于标记是否关联，0是未关联，1是已关联
     */
    private int mark;
    /**
     * 资源控制点ID
     */
    private String authRecoId;
    /**
     * 控制点id
     */
    private String contrId;

    public AdminSmDataTmplListVo() {
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

    public int getMark() {
        return this.mark;
    }

    public String getAuthRecoId() {
        return this.authRecoId;
    }

    public String getContrId() {
        return this.contrId;
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

    public void setMark(int mark) {
        this.mark = mark;
    }

    public void setAuthRecoId(String authRecoId) {
        this.authRecoId = authRecoId;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }
}
