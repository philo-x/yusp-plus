package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 系统操作日志表
 *
 * @author danyb1@yusys.com.cn
 * @date 2020-12-02 22:18:19
 */
@TableName("admin_sm_log")
public class AdminSmLogEntity extends BaseEntity implements Serializable {

    /**
     * 日志id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String logId;

    /**
     * 操作后值
     */
    private String afterValue;

    /**
     * 操作前值
     */
    private String beforeValue;

    /**
     * 内容
     */
    private String content;

    /**
     * 日志类型
     */
    private String logTypeId;

    /**
     * 登陆Ip
     */
    private String loginIp;

    /**
     * 操作标志
     */
    private String operFlag;

    /**
     * 操作对象
     */
    private String operObjId;

    /**
     * 操作时间
     */
    private String operTime;

    /**
     * 所属机构
     */
    private String orgId;

    /**
     * 操作用户
     */
    private String userId;

    public AdminSmLogEntity() {

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