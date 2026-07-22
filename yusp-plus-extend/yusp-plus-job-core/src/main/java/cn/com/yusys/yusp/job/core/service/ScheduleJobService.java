package cn.com.yusys.yusp.job.core.service;

import cn.com.yusys.yusp.job.core.entity.ScheduleJobEntity;
import cn.com.yusys.yusp.job.core.query.ScheduleJobQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @description 调度的接口
 * @author lty
 * @date 2021/3/1　　
 */
public interface ScheduleJobService extends IService<ScheduleJobEntity> {

    /**
     * 任务的分页查询
     *
     * @param scheduleJobQuery 条件
     * @return 分页插叙结果
     */
    Page<ScheduleJobEntity> queryPage(ScheduleJobQuery scheduleJobQuery);

    /**
     * 保存定时任务信息到数据库
     *
     * @param scheduleJob 任务信息
     */
    void saveJob(ScheduleJobEntity scheduleJob);

    /**
     * 更新任务
     *
     * @param scheduleJob 任务信息
     */
    void update(ScheduleJobEntity scheduleJob);

    /**
     * 删除任务
     * @param jobIds 任务id
     */
    void deleteBatch(Long[] jobIds);

    /**
     * 批量更新任务状态
     * @param jobIds 任务id
     * @param status 状态
     * @return 1表示成功
     */
    int updateBatch(Long[] jobIds, int status);

    /**
     * 立即运行
     *
     * @param jobIds 任务id
     */
    void run(Long[] jobIds);


    /**
     * 暂停
     * @param jobIds 任务id
     */
    void pause(Long[] jobIds);

    /**
     * 恢复运行
     * @param jobIds 任务id
     */
    void resume(Long[] jobIds);
}
