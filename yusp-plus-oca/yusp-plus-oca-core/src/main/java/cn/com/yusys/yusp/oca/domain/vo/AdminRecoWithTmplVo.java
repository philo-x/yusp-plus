package cn.com.yusys.yusp.oca.domain.vo;
/**
 * 数据权限模板表与控制点的展示
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
public class AdminRecoWithTmplVo {

    private String authRecoId;

    private String authobjId;

    private String menuId;

    private String authTmplId;

    private String authTmplName;

    public AdminRecoWithTmplVo() {
    }

    public String getAuthRecoId() {
        return this.authRecoId;
    }

    public String getAuthobjId() {
        return this.authobjId;
    }

    public String getMenuId() {
        return this.menuId;
    }

    public String getAuthTmplId() {
        return this.authTmplId;
    }

    public String getAuthTmplName() {
        return this.authTmplName;
    }

    public void setAuthRecoId(String authRecoId) {
        this.authRecoId = authRecoId;
    }

    public void setAuthobjId(String authobjId) {
        this.authobjId = authobjId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public void setAuthTmplId(String authTmplId) {
        this.authTmplId = authTmplId;
    }

    public void setAuthTmplName(String authTmplName) {
        this.authTmplName = authTmplName;
    }
}
