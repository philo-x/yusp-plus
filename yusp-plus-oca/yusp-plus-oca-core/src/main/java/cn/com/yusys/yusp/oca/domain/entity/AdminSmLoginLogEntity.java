package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 登录记录表
 * @author: zhangsong
 * @date: 2021/4/1
 */
@TableName("admin_sm_login_log")
public class AdminSmLoginLogEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String logId;
    /**
     * 交易全局流水
     */
    private String tradeId;
    /**
     * 用户id
     */
    private String loginCode;
    /**
     * 渠道编号
     */
    private String chnlNo;
    /**
     * 客户端IP
     */
    private String ipAddress;
    /**
     * MAC地址/系统唯一标识
     */
    private String deviceId;
    /**
     * 交易码/服务名，可以是restful的URL
     */
    private String tradeCode;
    /**
     * 操作结果。0：成功，1：失败
     */
    private String operResult;
    /**
     * 记录失败原因详情
     */
    private String operDetail;
    /**
     * 操作日期
     */
    private Date operDate;
    /**
     * 操作时间
     */
    private Timestamp operTime;

    public AdminSmLoginLogEntity() {
    }

    public String getLogId() {
        return this.logId;
    }

    public String getTradeId() {
        return this.tradeId;
    }

    public String getLoginCode() {
        return this.loginCode;
    }

    public String getChnlNo() {
        return this.chnlNo;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getTradeCode() {
        return this.tradeCode;
    }

    public String getOperResult() {
        return this.operResult;
    }

    public String getOperDetail() {
        return this.operDetail;
    }

    public Date getOperDate() {
        return this.operDate;
    }

    public Timestamp getOperTime() {
        return this.operTime;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public void setChnlNo(String chnlNo) {
        this.chnlNo = chnlNo;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public void setOperResult(String operResult) {
        this.operResult = operResult;
    }

    public void setOperDetail(String operDetail) {
        this.operDetail = operDetail;
    }

    public void setOperDate(Date operDate) {
        this.operDate = operDate;
    }

    public void setOperTime(Timestamp operTime) {
        this.operTime = operTime;
    }

}
