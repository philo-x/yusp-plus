package cn.com.yusys.yusp.job.core.service.impl;

import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.commons.util.date.DateUtils;
import cn.com.yusys.yusp.job.core.dao.ScheduleJobLogDao;
import cn.com.yusys.yusp.job.core.entity.ScheduleJobLogEntity;
import cn.com.yusys.yusp.job.core.query.ScheduleJobLogQuery;
import cn.com.yusys.yusp.job.core.service.ScheduleJobLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 调度日志的服务实现
 * @author lty
 * @since 2021/3/1
 */
@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogDao, ScheduleJobLogEntity> implements ScheduleJobLogService {

    @Override
    public Page<ScheduleJobLogEntity> queryJobLogPage(ScheduleJobLogQuery scheduleJobLogQuery) {

        String jobId = scheduleJobLogQuery.getJobId();

        // 获取分页参数(curPage当前页, limit每页大小)
        Page<ScheduleJobLogEntity> page = new Page<>(scheduleJobLogQuery.getPage(), scheduleJobLogQuery.getSize());
        // 创建查询wrapper
        QueryWrapper<ScheduleJobLogEntity> wrapper = new QueryWrapper<>();
        // 添加查询条件
        wrapper.eq(!StringUtils.isEmpty(jobId), "job_id", jobId);
        wrapper.orderByDesc("create_time");
        // 分页查询
        return getBaseMapper().selectPage(page, wrapper);
    }

    /**
     * 清除指定天数前定时任务日志
     *
     * @param day
     */
    @Override
    public void clearScheduleJobLog(int day) {
        //当前时间
        String nowStr = DateUtils.formatDate("yyyy-MM-dd");
        Date now = DateUtils.parseDate(nowStr, "yyyy-MM-dd");
        Date before = DateUtils.addDay(now, (-day));
        //清理定时任务日志
        LambdaQueryWrapper<ScheduleJobLogEntity> scheduleJobLogWrapper = new LambdaQueryWrapper<>();
        scheduleJobLogWrapper.lt(ScheduleJobLogEntity::getCreateTime, before);
        this.remove(scheduleJobLogWrapper);
    }
}
