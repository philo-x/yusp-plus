package cn.com.yusys.yusp.job.core.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.job.core.entity.ScheduleJobLogEntity;
import cn.com.yusys.yusp.job.core.query.ScheduleJobLogQuery;
import cn.com.yusys.yusp.job.core.service.ScheduleJobLogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description 任务调度日志的controller
 * @author lty
 * @date 2021/3/1　　
 */
@RestController
@RequestMapping("/api/scheduleLog")
public class ScheduleJobLogController {
	@Autowired
	private ScheduleJobLogService scheduleJobLogService;
	
	/**
	 * 定时任务日志列表
	 */
	@RequestMapping("/list")

	public JClientRspEntity<Page<ScheduleJobLogEntity>> list(ScheduleJobLogQuery scheduleJobLogQuery){
		Page<ScheduleJobLogEntity> page = scheduleJobLogService.queryJobLogPage(scheduleJobLogQuery);

		return JClientRspEntity.buildSuccess(page);
	}
	
	/**
	 * 定时任务日志信息
	 */
	@RequestMapping("/info/{logId}")
	public JClientRspEntity<ScheduleJobLogEntity> info(@PathVariable Long logId){
		ScheduleJobLogEntity log = scheduleJobLogService.getById(logId);
		return JClientRspEntity.buildSuccess(log);
	}
}
