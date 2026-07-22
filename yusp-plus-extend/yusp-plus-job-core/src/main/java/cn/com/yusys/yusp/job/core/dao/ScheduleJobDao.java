package cn.com.yusys.yusp.job.core.dao;


import cn.com.yusys.yusp.commons.mybatisplus.mapper.BaseMapper;
import cn.com.yusys.yusp.job.core.entity.ScheduleJobEntity;

import java.util.Map;

/**
 * @author lty
 * @description 调度的dao
 * @date 2021/3/1
 */
public interface ScheduleJobDao extends BaseMapper<ScheduleJobEntity> {


    /**
     * 批量更新
     *
     * @param map 接收参数
     * @return 1表示成功
     */
    int updateBatch(Map<String, Object> map);
}
