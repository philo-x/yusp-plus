package cn.com.yusys.yusp.job.core.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.job.core.entity.ScheduleJobEntity;
import cn.com.yusys.yusp.job.core.query.ScheduleJobIdQuery;
import cn.com.yusys.yusp.job.core.query.ScheduleJobQuery;
import cn.com.yusys.yusp.job.core.service.ScheduleJobService;
import cn.com.yusys.yusp.job.core.utils.SubStringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description quartz的调度controller
 * @author lty
 * @date 2021/3/1　　
 */
@RestController
@RequestMapping("/api/schedule")
public class ScheduleJobController {
	@Autowired
	private ScheduleJobService scheduleJobService;

	/**
	 * 定时任务列表
	 */
	@PostMapping("/list")
	public JClientRspEntity<Page<ScheduleJobEntity>> list(@RequestBody JClientReqEntity<ScheduleJobQuery> scheduleJobQuery){
		Page<ScheduleJobEntity> page = scheduleJobService.queryPage(scheduleJobQuery.getBody());
		return JClientRspEntity.buildSuccess(page);
	}
	
	/**
	 * 定时任务信息
	 */
	@GetMapping("/info/{jobId}")
	public JClientRspEntity<ScheduleJobEntity> info(@PathVariable Long jobId){
		ScheduleJobEntity schedule = scheduleJobService.getById(jobId);
		return JClientRspEntity.buildSuccess(schedule);
	}
	
	/**
	 * 保存定时任务
	 */
	@PostMapping("/save")
	public JClientRspEntity<String> save(@RequestBody @Valid JClientReqEntity<ScheduleJobEntity> scheduleJob){
		scheduleJobService.saveJob(scheduleJob.getBody());
		return JClientRspEntity.buildSuccess("成功");
	}
	
	/**
	 * 修改定时任务
	 */
	@PostMapping("/update")
	public JClientRspEntity<String> update(@RequestBody @Valid JClientReqEntity<ScheduleJobEntity> scheduleJob){
		scheduleJobService.update(scheduleJob.getBody());
		return JClientRspEntity.buildSuccess("成功");
	}
	
	/**
	 * 删除定时任务
	 */
	@PostMapping("/delete")
	public JClientRspEntity<String> delete(@RequestBody JClientRspEntity<ScheduleJobIdQuery> scheduleJobIdQuery){
		Long[] jobId = SubStringUtils.stringToLongArray(scheduleJobIdQuery.getBody().getJobIds());
		scheduleJobService.deleteBatch(jobId);
		
		return JClientRspEntity.buildSuccess("成功");
	}
	/**
	 * 立即执行任务
	 */
	@PostMapping("/run")
	public JClientRspEntity<String> run(@RequestBody JClientRspEntity<ScheduleJobIdQuery> scheduleJobIdQuery){
		Long[] jobId = SubStringUtils.stringToLongArray(scheduleJobIdQuery.getBody().getJobIds());
		scheduleJobService.run(jobId);
		return JClientRspEntity.buildSuccess("成功");
	}
	/**
	 * 暂停定时任务
	 */

	@PostMapping("/pause")
	public JClientRspEntity<String> pause(@RequestBody  JClientRspEntity<ScheduleJobIdQuery> scheduleJobIdQuery){
		Long[] jobId = SubStringUtils.stringToLongArray(scheduleJobIdQuery.getBody().getJobIds());
		scheduleJobService.pause(jobId);
		
		return JClientRspEntity.buildSuccess("成功");
	}
	
	/**
	 * 恢复定时任务
	 */
	@PostMapping("/resume")
	public JClientRspEntity<String> resume(@RequestBody  JClientRspEntity<ScheduleJobIdQuery> scheduleJobIdQuery){
		Long[] jobId = SubStringUtils.stringToLongArray(scheduleJobIdQuery.getBody().getJobIds());
		scheduleJobService.resume(jobId);
		
		return JClientRspEntity.buildSuccess("成功");
	}

}
