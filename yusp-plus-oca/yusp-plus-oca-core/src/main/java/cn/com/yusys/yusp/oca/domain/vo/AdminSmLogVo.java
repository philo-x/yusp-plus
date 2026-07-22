package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.oca.annotation.DictTranslator;
import cn.com.yusys.yusp.oca.domain.constants.Constants;

/**
 * 系统日志VO
 * @author zhanyq
 * @date 2021-06-30 17:26
 */
public class AdminSmLogVo {

    /**
     * 记录编号
     */
    private String logId;
    /**
     * 用户ID
     */
    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "userName" )
    private String userId;
    /**
     * 操作时间
     */
    private String operTime;
    /**
     * 操作对象ID
     */
    private String operObjId;
    /**
     * 操作前值
     */
    private String beforeValue;
    /**
     * 操作后值
     */
    private String afterValue;
    /**
     * 操作标志
     */
    private String operFlag;
    /**
     * 日志类型
     */
    private String logTypeId;
    /**
     * 日志内容
     */
    private String content;
    /**
     * 操作者机构
     */
    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_ORG_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "orgName" )
    private String orgId;
    /**
     * 登录IP
     */
    private String loginIp;

    public AdminSmLogVo() {
    }

    public String getLogId() {
        return this.logId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getOperTime() {
        return this.operTime;
    }

    public String getOperObjId() {
        return this.operObjId;
    }

    public String getBeforeValue() {
        return this.beforeValue;
    }

    public String getAfterValue() {
        return this.afterValue;
    }

    public String getOperFlag() {
        return this.operFlag;
    }

    public String getLogTypeId() {
        return this.logTypeId;
    }

    public String getContent() {
        return this.content;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getLoginIp() {
        return this.loginIp;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public void setOperObjId(String operObjId) {
        this.operObjId = operObjId;
    }

    public void setBeforeValue(String beforeValue) {
        this.beforeValue = beforeValue;
    }

    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }

    public void setOperFlag(String operFlag) {
        this.operFlag = operFlag;
    }

    public void setLogTypeId(String logTypeId) {
        this.logTypeId = logTypeId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }
}
