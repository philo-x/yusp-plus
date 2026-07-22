package cn.com.yusys.yusp.job.core.service;

import cn.com.yusys.yusp.job.core.entity.ScheduleJobLogEntity;
import cn.com.yusys.yusp.job.core.query.ScheduleJobLogQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 定时任务日志接口
 *
 * @author lty
 * @since 2021/2/25
 */
public interface ScheduleJobLogService extends IService<ScheduleJobLogEntity> {

    /**
     * 分页查询日志
     *
     * @param scheduleJobLogQuery 条件
     * @return 分页查询结果
     */
    Page<ScheduleJobLogEntity> queryJobLogPage(ScheduleJobLogQuery scheduleJobLogQuery);

    /**
     * 清除指定天数前定时任务日志
     *
     * @param day
     */
    void clearScheduleJobLog(int day);

}
