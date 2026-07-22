package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.oca.annotation.DictTranslator;
import cn.com.yusys.yusp.oca.domain.constants.Constants;

/**
 * 数据权限模板表
 *
 * @author danyb1
 * @date 2020-12-08 18:53:56
 */
public class AdminSmDataAuthTmplVo {

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
     * 可用的控制点记录编号(*表示都可用)
     */
    private int status;
    /**
     * 最新变更用户
     */
    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "lastChgName")
    private String lastChgUsr;
    /**
     * 最新变更时间
     */
    private String lastChgDt;
    /**
     * 优先级,值越小优先级越高
     */
    private String priority;

    public AdminSmDataAuthTmplVo() {
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

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public String getLastChgDt() {
        return this.lastChgDt;
    }

    public String getPriority() {
        return this.priority;
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

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(String lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


}
