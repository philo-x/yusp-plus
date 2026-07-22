package cn.com.yusys.yusp.job.core.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.job.core.vo.ScheduleJobVo;

/**
 * @description 定时任务条件
 * @Author: lty
 * @Date: 2021/6/28
 */
public class ScheduleJobQuery extends PageQuery<ScheduleJobVo> {
    private String beanName;

    public ScheduleJobQuery() {
    }

    public String getBeanName() {
        return this.beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

}
