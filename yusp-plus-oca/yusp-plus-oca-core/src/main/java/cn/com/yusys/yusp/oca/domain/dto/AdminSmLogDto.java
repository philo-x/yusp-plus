package cn.com.yusys.yusp.oca.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author danyu
 */
@Schema(description = "日志对象Dto", name = "日志对象")
public class AdminSmLogDto {

	/**
	 * 记录编号
	 */
	@Schema(name = "logId", description = "日志id", required = true)
	private String logId;
	/**
	 * 用户ID
	 */
	@Schema(name = "userId", description = "用户id", required = true)
	private String userId;
	/**
	 * 操作时间
	 */
	@Schema(name = "operTime", description = "操作时间", required = true)
	private String operTime;
	/**
	 * 操作对象ID
	 */
	@Schema(name = "operObjId", description = "操作对象id", required = true)
	private String operObjId;
	/**
	 * 操作前值
	 */
	@Schema(name = "beforeValue", description = "操作前值", required = false)
	private String beforeValue;
	/**
	 * 操作后值
	 */
	@Schema(name = "afterValue", description = "操作后值", required = false)
	private String afterValue;
	/**
	 * 操作标志
	 */
	@Schema(name = "operFlag", description = "操作标志", required = true)
	private String operFlag;
	/**
	 * 日志类型
	 */
	@Schema(name = "logTypeId", description = "日志类型", required = true)
	private String logTypeId;
	/**
	 * 日志内容
	 */
	@Schema(name = "content", description = "日志内容", required = true)
	private String content;
	/**
	 * 操作者机构
	 */
	@Schema(name = "orgId", description = "操作者机构", required = true)
	private String orgId;
	/**
	 * 登录IP
	 */
	@Schema(name = "loginIp", description = "登录IP", required = true)
	private String loginIp;

    public AdminSmLogDto() {
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
