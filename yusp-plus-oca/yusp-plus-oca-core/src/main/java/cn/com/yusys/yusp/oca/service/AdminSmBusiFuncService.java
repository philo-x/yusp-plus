package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.bo.AdminSmBusiFuncBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmBusiFuncEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmBusiFuncQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmBusiFuncVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 系统业务功能表
 *
 * @author wujp4
 * @email wujp4@yusys.com.cn
 * @date 2020-11-20 13:43:51
 */
public interface AdminSmBusiFuncService extends IService<AdminSmBusiFuncEntity> {


    /**
     * 业务功能分页查询
     *
     * @param adminSmBusiFuncQuery 业务功能分页查询参数
     * @return 分页查询结果数据
     * @author zhanyq
     * @date 2021-06-24 14:44
     */
    Page<AdminSmBusiFuncVo> queryPageWithCondition(AdminSmBusiFuncQuery adminSmBusiFuncQuery);

    /**
     * 批量删除业务功能
     *
     * @param funcIds 业务功能ID
     * @return void
     * @author zhanyq
     * @date 2021-06-24 14:44
     */
    void removeFuncByIds(String[] funcIds);

    /**
     * 业务功能分页查询，在菜单管理中使用，查询语句与 queryPageWithCondition 不同
     *
     * @param adminSmBusiFuncQuery 查询参数
     * @return 分页数据对象
     * @author zhanyq
     * @date 2021-06-24 14:45
     */
    Page<AdminSmBusiFuncVo> getFuncInfoWithCondition(AdminSmBusiFuncQuery adminSmBusiFuncQuery);

    /**
     * 保存业务功能
     *
     * @param adminSmBusiFuncBo 要保存的数据对象
     * @return 成功条数
     * @author zhanyq
     * @date 2021-06-24 14:51
     */
    int saveBusiFuncByBo(AdminSmBusiFuncBo adminSmBusiFuncBo);

    /**
     * 修改业务功能
     *
     * @param adminSmBusiFuncBo
     * @author yinjun
     */
    void updateBusiFunc(AdminSmBusiFuncBo adminSmBusiFuncBo);
}

