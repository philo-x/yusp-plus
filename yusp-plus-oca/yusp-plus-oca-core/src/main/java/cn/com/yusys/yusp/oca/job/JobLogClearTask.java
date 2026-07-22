package cn.com.yusys.yusp.oca.job;

import cn.com.yusys.yusp.commons.exception.BizException;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.job.core.service.ScheduleJobLogService;
import cn.com.yusys.yusp.job.core.task.ITask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时清理日志
 *
 *@author mengxz3
 *@since 2021/12/21 16:10
 */
@Component("jobLogClearTask")
public class JobLogClearTask implements ITask {

    private static final Logger log = LoggerFactory.getLogger(JobLogClearTask.class);

    @Autowired
    private ScheduleJobLogService scheduleJobLogService;

    @Override
    public void run(String params) {
        if (StringUtils.isBlank(params)) {
            throw BizException.error("", "", "必须指定参数为数字类型");
        }
        int day;
        try {
            day = Integer.parseInt(params);
        } catch (NumberFormatException e) {
            throw BizException.error("", "", "参数转换为数字类型异常");
        }
        log.info("logClearTask定时任务正在执行，参数为：{}", params);
        scheduleJobLogService.clearScheduleJobLog(day);
    }
}
