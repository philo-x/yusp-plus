package cn.com.yusys.yusp.utrace.core.service;


import cn.com.yusys.yusp.utrace.core.domain.entity.ModifyTraceEntity;
import cn.com.yusys.yusp.utrace.core.domain.vo.TraceQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 小U留痕记录表
 *
 * @author zhanyq
 * @date 2021-05-17 15:13:18
 */
public interface ModifyTraceService extends IService<ModifyTraceEntity> {


    /**
     * 列表查询，分页
     * @param params 查询参数
     * @return 分页数据对象
     * @date 2021-06-24 13:52
     * @author zhanyq
     */
    Page<ModifyTraceEntity> queryPage(TraceQueryVo params);

    /**
     * 查询所有更改记录
     * @param params 查询参数
     * @return 更改数据集合
     * @date 2021-06-24 13:52
     * @author zhanyq
     */
    List<ModifyTraceEntity> queryTraceByPk(TraceQueryVo params);

    /**
     * 保存更改记录
     * @param modifyTraceEntityList 保存数据实体
     * @return 无
     * @date 2021-06-24 13:52
     * @author zhanyq
     */
    void addTrace(List<ModifyTraceEntity> modifyTraceEntityList);
}

