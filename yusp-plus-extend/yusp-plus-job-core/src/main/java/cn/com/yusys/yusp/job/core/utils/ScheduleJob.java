package cn.com.yusys.yusp.job.core.utils;


import cn.com.yusys.yusp.commons.util.SpringContextUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.job.core.entity.ScheduleJobEntity;
import cn.com.yusys.yusp.job.core.entity.ScheduleJobLogEntity;
import cn.com.yusys.yusp.job.core.service.ScheduleJobLogService;
import cn.com.yusys.yusp.job.core.service.impl.LongRandomServiceImpl;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.util.Date;


/**

 * @description 定时任务操作
 * @author lty
 * @date 2021/3/1　　
 */
public class ScheduleJob extends QuartzJobBean {
	private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void executeInternal(JobExecutionContext context) {
        ScheduleJobEntity scheduleJob = (ScheduleJobEntity) context.getMergedJobDataMap()
        		.get(ScheduleJobEntity.JOB_PARAM_KEY);
        
        // 获取spring bean
        ScheduleJobLogService scheduleJobLogService = SpringContextUtils.getBean("scheduleJobLogService");
        
        // 数据库保存执行记录
        ScheduleJobLogEntity log = new ScheduleJobLogEntity();


		LongRandomServiceImpl longRandomService = new LongRandomServiceImpl();
		log.setLogId(longRandomService.getLongRandom());
        log.setJobId(scheduleJob.getJobId());
        log.setBeanName(scheduleJob.getBeanName());
        log.setParams(scheduleJob.getParams());
        log.setCreateTime(new Date());
        
        // 任务开始时间
        long startTime = System.currentTimeMillis();
        
        try {
            // 执行任务
        	logger.debug("任务准备执行，任务ID：" + scheduleJob.getJobId());

			Object target = SpringContextUtils.getBean(scheduleJob.getBeanName());
			Method method = target.getClass().getDeclaredMethod("run", String.class);
			method.invoke(target, scheduleJob.getParams());
			
			// 任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			log.setTimes((int)times);
			// 任务状态    0：成功    1：失败
			log.setStatus(0);
			
			logger.debug("任务执行完毕，任务ID：" + scheduleJob.getJobId() + "  总共耗时：" + times + "毫秒");
		} catch (Exception e) {
			logger.error("任务执行失败，任务ID：" + scheduleJob.getJobId(), e);
			
			// 任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			log.setTimes((int)times);
			
			// 任务状态    0：成功    1：失败
			log.setStatus(1);
			log.setError(StringUtils.substring(e.toString(), 0, 2000));
		}finally {
			scheduleJobLogService.save(log);
		}
    }
}
