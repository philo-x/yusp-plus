package cn.com.yusys.yusp.oca.domain.vo;

/**
 * 数据权限模板表的模板id和模板名称
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
public class AdminSmDataTmplVo {
    /**
     * 数据权限模板主键
     */
    private String authTmplId;
    /**
     * 数据权限模板名
     */
    private String authTmplName;

    public AdminSmDataTmplVo() {
    }

    public String getAuthTmplId() {
        return this.authTmplId;
    }

    public String getAuthTmplName() {
        return this.authTmplName;
    }

    public void setAuthTmplId(String authTmplId) {
        this.authTmplId = authTmplId;
    }

    public void setAuthTmplName(String authTmplName) {
        this.authTmplName = authTmplName;
    }
}
