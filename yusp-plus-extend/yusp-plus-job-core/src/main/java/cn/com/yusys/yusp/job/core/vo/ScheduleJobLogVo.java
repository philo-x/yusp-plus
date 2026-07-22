package cn.com.yusys.yusp.job.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 任务日志vo
 *
 * @Author: lty
 * @Date: 2021/6/28
 */
public class ScheduleJobLogVo {
    /**
     * 日志id
     */
    private Long logId;

    /**
     * 任务id
     */
    private Long jobId;

    /**
     * spring bean名称
     */
    private String beanName;

    /**
     * 参数
     */
    private String params;

    /**
     * 任务状态    0：成功    1：失败
     */
    private Integer status;

    /**
     * 失败信息
     */
    private String error;

    /**
     * 耗时(单位：毫秒)
     */
    private Integer times;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public ScheduleJobLogVo() {
    }

    public Long getLogId() {
        return this.logId;
    }

    public Long getJobId() {
        return this.jobId;
    }

    public String getBeanName() {
        return this.beanName;
    }

    public String getParams() {
        return this.params;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getError() {
        return this.error;
    }

    public Integer getTimes() {
        return this.times;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
