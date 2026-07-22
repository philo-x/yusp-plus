package cn.com.yusys.yusp.job.core.service.impl;

import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.job.core.dao.ScheduleJobDao;
import cn.com.yusys.yusp.job.core.entity.ScheduleJobEntity;
import cn.com.yusys.yusp.job.core.query.ScheduleJobQuery;
import cn.com.yusys.yusp.job.core.service.LongRandomService;
import cn.com.yusys.yusp.job.core.service.ScheduleJobService;
import cn.com.yusys.yusp.job.core.utils.Constant;
import cn.com.yusys.yusp.job.core.utils.ScheduleUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.*;

/**
 * @author lty
 * @description 调度的服务实现
 * @date 2021/3/1
 */
@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobDao, ScheduleJobEntity> implements ScheduleJobService {

    @Qualifier("schedulerFactoryBean")
    @Autowired
    private Scheduler scheduler;

    @Autowired
    LongRandomService longRandomService;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() {
        List<ScheduleJobEntity> scheduleJobList = this.list();
        for (ScheduleJobEntity scheduleJob : scheduleJobList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
            //  如果不存在，则创建
            if (cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            } else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
        }
    }

    /**
     * @description：模糊查询所有的任务
     * @author： lty
     * @date： 2021/2/25
     */
    @Override
    public Page<ScheduleJobEntity> queryPage(ScheduleJobQuery scheduleJobQuery) {
        String beanName = scheduleJobQuery.getBeanName();

        int limit = scheduleJobQuery.getSize();
        int curPage = scheduleJobQuery.getPage();
        // 获取分页参数(curPage当前页, limit每页大小)
        Page<ScheduleJobEntity> page = new Page<>(curPage, limit);
        // 创建查询wrapper
        QueryWrapper<ScheduleJobEntity> wrapper = new QueryWrapper<>();
        // 添加查询条件
        wrapper.like(!StringUtils.isEmpty(beanName), "bean_name", beanName);
        // 分页查询
        return getBaseMapper().selectPage(page, wrapper);


    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveJob(ScheduleJobEntity scheduleJob) {
        scheduleJob.setCreateTime(new Date());
        scheduleJob.setStatus(Constant.ScheduleStatus.NORMAL.getValue());

        scheduleJob.setJobId(longRandomService.getLongRandom());
        getBaseMapper().insert(scheduleJob);

        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScheduleJobEntity scheduleJob) {
        ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);

        this.updateById(scheduleJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtils.deleteScheduleJob(scheduler, jobId);
        }

        // 删除数据
        this.removeByIds(Arrays.asList(jobIds));
    }

    @Override
    public int updateBatch(Long[] jobIds, int status) {
        List<Long> idJobs = Arrays.asList(jobIds);
        Map<String, Object> map = new HashMap<>(2);
        map.put("list", idJobs);
        map.put("status", status);
        return getBaseMapper().updateBatch(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtils.run(scheduler, this.getById(jobId));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtils.pauseJob(scheduler, jobId);
        }

        updateBatch(jobIds, Constant.ScheduleStatus.PAUSE.getValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtils.resumeJob(scheduler, jobId);
        }

        updateBatch(jobIds, Constant.ScheduleStatus.NORMAL.getValue());
    }

}
