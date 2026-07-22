package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.commons.excelcsv.annotation.ExcelField;

import java.io.Serializable;

/**
 * @author danyu
 */
public class AdminSmLogPojo implements Serializable {

    @ExcelField(title = "日志id", order = 0)
    private String logId;

    @ExcelField(title = "操作后值", order = 1)
    private String afterValue;

    @ExcelField(title = "操作前值", order = 2)
    private String beforeValue;

    @ExcelField(title = "内容", order = 3)
    private String content;

    @ExcelField(title = "日志类型", order = 4, dictCode = "LOG_TYPE")
    private String logTypeId;

    @ExcelField(title = "登陆Ip", order = 5)
    private String loginIp;

    @ExcelField(title = "操作标志", order = 6)
    private String operFlag;

    @ExcelField(title = "操作对象", order = 7)
    private String operObjId;

    @ExcelField(title = "操作时间", order = 8)
    private String operTime;

    @ExcelField(title = "所属机构", dictCode = "ORG_ID", order = 9)
    private String orgId;

    @ExcelField(title = "操作用户", order = 10)
    private String userId;

    public AdminSmLogPojo() {
    }

    public String getLogId() {
        return this.logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getAfterValue() {
        return this.afterValue;
    }

    public void setAfterValue(String afterValue) {
        this.afterValue = afterValue;
    }

    public String getBeforeValue() {
        return this.beforeValue;
    }

    public void setBeforeValue(String beforeValue) {
        this.beforeValue = beforeValue;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLogTypeId() {
        return this.logTypeId;
    }

    public void setLogTypeId(String logTypeId) {
        this.logTypeId = logTypeId;
    }

    public String getLoginIp() {
        return this.loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getOperFlag() {
        return this.operFlag;
    }

    public void setOperFlag(String operFlag) {
        this.operFlag = operFlag;
    }

    public String getOperObjId() {
        return this.operObjId;
    }

    public void setOperObjId(String operObjId) {
        this.operObjId = operObjId;
    }

    public String getOperTime() {
        return this.operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AdminSmLogPojo{" + "logId='" + logId + '\'' + ", afterValue='" + afterValue + '\'' + ", " +
                "beforeValue='" + beforeValue + '\'' + ", content='" + content + '\'' + ", logTypeId='" + logTypeId +
                '\'' + ", loginIp='" + loginIp + '\'' + ", operFlag='" + operFlag + '\'' + ", operObjId='" +
                operObjId + '\'' + ", operTime='" + operTime + '\'' + ", orgId='" + orgId + '\'' + ", userId='" +
                userId + '\'' + '}';
    }
}