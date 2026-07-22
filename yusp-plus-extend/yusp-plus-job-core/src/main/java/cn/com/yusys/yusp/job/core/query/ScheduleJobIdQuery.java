package cn.com.yusys.yusp.job.core.query;

/**
 * @description 定时任务条件
 * @Author: lty
 * @Date: 2021/6/28
 */
public class ScheduleJobIdQuery {
    private String jobIds;

    public ScheduleJobIdQuery() {
    }

    public String getJobIds() {
        return this.jobIds;
    }

    public void setJobIds(String jobIds) {
        this.jobIds = jobIds;
    }

}
