package cn.com.yusys.yusp.job.core.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.job.core.vo.ScheduleJobVo;

/**
 * @description 定时任务日志条件
 * @Author: lty
 * @Date: 2021/6/28
 */
public class ScheduleJobLogQuery extends PageQuery<ScheduleJobVo> {
    private String jobId;

    public ScheduleJobLogQuery() {
    }

    public String getJobId() {
        return this.jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }


}
