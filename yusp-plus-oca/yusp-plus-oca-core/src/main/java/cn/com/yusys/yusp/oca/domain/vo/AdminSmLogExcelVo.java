package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.commons.excelcsv.annotation.ExcelCsv;
import cn.com.yusys.yusp.commons.excelcsv.annotation.ExcelField;

/**
 * 操作日志导出对象
 *
 * @author yangzai
 * @since 2022/1/11
 **/
@ExcelCsv(namePrefix = "操作日志", fileType = ExcelCsv.ExportFileType.XLSX)
public class AdminSmLogExcelVo {

    @ExcelField(title = "操作用户", dictCode = "USER_ID", viewLength = 15)
    private String userId;

    @ExcelField(title = "所属机构", dictCode = "ORG_ID", viewLength = 20)
    private String orgId;

    @ExcelField(title = "日志类型", viewLength = 15)
    private String logTypeId;

    @ExcelField(title = "操作标志", viewLength = 15)
    private String operFlag;

    @ExcelField(title = "操作对象", viewLength = 15)
    private String operObjId;

    @ExcelField(title = "登陆Ip", viewLength = 30)
    private String loginIp;

    @ExcelField(title = "操作时间", viewLength = 25)
    private String operTime;

    @ExcelField(title = "内容", viewLength = 110)
    private String content;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getLogTypeId() {
        return logTypeId;
    }

    public void setLogTypeId(String logTypeId) {
        this.logTypeId = logTypeId;
    }

    public String getOperFlag() {
        return operFlag;
    }

    public void setOperFlag(String operFlag) {
        this.operFlag = operFlag;
    }

    public String getOperObjId() {
        return operObjId;
    }

    public void setOperObjId(String operObjId) {
        this.operObjId = operObjId;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
