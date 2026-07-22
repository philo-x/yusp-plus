package cn.com.yusys.yusp.oca.domain.vo;

/**
 * 模板授权树Vo
 * @author zhanyq
 * @date 2021-06-30 17:36
 */
public class AdminSmTmplAuthTreeVo extends AdminSmAuthTreeVo {

    private String sqlName;

    private String sqlString;

    public AdminSmTmplAuthTreeVo() {
    }

    public String getSqlName() {
        return this.sqlName;
    }

    public String getSqlString() {
        return this.sqlString;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }


}
