package cn.com.yusys.yusp.job.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lty
 * @description 调度的entity
 * @date 2021/3/1
 */
@TableName("schedule_job")
public class ScheduleJobEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 任务调度参数key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    /**
     * 任务id
     */
    @TableId(type = IdType.INPUT)
    private Long jobId;

    /**
     * spring bean名称
     */
    @NotBlank(message = "bean名称不能为空")
    private String beanName;

    /**
     * 参数
     */
    private String params;

    /**
     * cron表达式
     */
    @NotBlank(message = "cron表达式不能为空")
    private String cronExpression;

    /**
     * 任务状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public ScheduleJobEntity() {
    }

    public Long getJobId() {
        return this.jobId;
    }

    public @NotBlank(message = "bean名称不能为空") String getBeanName() {
        return this.beanName;
    }

    public String getParams() {
        return this.params;
    }

    public @NotBlank(message = "cron表达式不能为空") String getCronExpression() {
        return this.cronExpression;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getRemark() {
        return this.remark;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public void setBeanName(@NotBlank(message = "bean名称不能为空") String beanName) {
        this.beanName = beanName;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setCronExpression(@NotBlank(message = "cron表达式不能为空") String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
