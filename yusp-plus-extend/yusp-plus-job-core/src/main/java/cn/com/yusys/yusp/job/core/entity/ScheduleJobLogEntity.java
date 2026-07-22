package cn.com.yusys.yusp.job.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lty
 * @description 调度日志的entity
 * @date 2021/3/1
 */
@TableName("schedule_job_log")
public class ScheduleJobLogEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日志id
     */
    @TableId(type = IdType.INPUT)
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

    public ScheduleJobLogEntity() {
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
