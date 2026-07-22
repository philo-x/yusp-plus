package cn.com.yusys.yusp.oca.domain.form;
/**
 * 系统功能控制点查询信息的封装
 *
 * @author zhangyt12
 * @date 2020-11-27 14:57:08
 */
public class AdminSmDataAuthForm {

	/**
	 * 记录编号
	 */
	private String authId;
	/**
	 * 控制点记录编号(为*时表示默认过滤器)
	 */
	private String contrId;
	/**
	 * 权限模板编号
	 */
	private String authTmplId;

    public AdminSmDataAuthForm() {
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

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public void setAuthTmplId(String authTmplId) {
        this.authTmplId = authTmplId;
    }
}
