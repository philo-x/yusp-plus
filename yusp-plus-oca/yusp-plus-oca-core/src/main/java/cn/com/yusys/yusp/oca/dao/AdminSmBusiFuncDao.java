package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmBusiFuncEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmBusiFuncVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 系统业务功能表
 *
 * @author wujp4
 * @email wujp4@yusys.com.cn
 * @date 2020-11-20 13:43:51
 */

public interface AdminSmBusiFuncDao extends BaseMapper<AdminSmBusiFuncEntity> {


    /**
     * 业务功能管理列表查询，带权限
     *
     * @param page          分页对象
     * @param busiFuncQuery 查询参数
     * @return 分页数据对象
     * @author zhanyq
     * @date 2021-06-24 14:56
     */
    Page<AdminSmBusiFuncVo> queryPageWithCondition(Page<AdminSmBusiFuncVo> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmBusiFuncVo> busiFuncQuery);

    /**
     * 业务功能分页，菜单管理页面使用，无权限
     *
     * @param page          分页对象
     * @param busiFuncQuery 查询参数
     * @return 分页数据对象
     * @author zhanyq
     * @date 2021-06-24 14:54
     */
    Page<AdminSmBusiFuncVo> getFuncInfoWithConditionDao(Page<AdminSmBusiFuncVo> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmBusiFuncVo> busiFuncQuery);
}
