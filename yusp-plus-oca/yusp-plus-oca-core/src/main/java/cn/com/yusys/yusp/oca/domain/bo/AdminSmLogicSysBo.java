package cn.com.yusys.yusp.oca.domain.bo;

/**
 * 系统逻辑 前端传入Bo
 *
 * @author wujp4
 * @date 2020-11-19 14:30:22
 */
public class AdminSmLogicSysBo {

    /**
     * 记录编号
     */
    private String sysId;
    /**
     * 认证类型
     */
    private String authId;
    /**
     * 版本号
     */
    private String sysVersion;
    /**
     * 逻辑系统名称
     */
    private String sysName;
    /**
     * 逻辑系统描述
     */
    private String sysDesc;
    /**
     * 逻辑系统状态
     */
    private String sysSts;
    /**
     * 是否单点登录
     */
    private String isSso;
    /**
     * 系统简称
     */
    private String sysCode;
    /**
     * 国际化key值
     */
    private String i18nKey;

    private String userId;

    private String funcId;

    private String roleId;


    public String getSysId() {
        return this.sysId;
    }

    public String getAuthId() {
        return this.authId;
    }

    public String getSysVersion() {
        return this.sysVersion;
    }

    public String getSysName() {
        return this.sysName;
    }

    public String getSysDesc() {
        return this.sysDesc;
    }

    public String getSysSts() {
        return this.sysSts;
    }

    public String getIsSso() {
        return this.isSso;
    }

    public String getSysCode() {
        return this.sysCode;
    }

    public String getI18nKey() {
        return this.i18nKey;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getFuncId() {
        return this.funcId;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public void setSysDesc(String sysDesc) {
        this.sysDesc = sysDesc;
    }

    public void setSysSts(String sysSts) {
        this.sysSts = sysSts;
    }

    public void setIsSso(String isSso) {
        this.isSso = isSso;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public void setI18nKey(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }


}
