package cn.com.yusys.yusp.utrace.core.dao;

import cn.com.yusys.yusp.utrace.core.domain.entity.ModifyTraceEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 小U留痕记录dao操作类
 *
 * @author zhanyq
 * @date 2021-05-17 15:13:18
 */
@Mapper
public interface ModifyTraceDao extends BaseMapper<ModifyTraceEntity> {

    /**
     * 根据 数据主键 mPkV查询是否有修改记录
     * @param queryWrapper 查询参数
     * @return 修改记录
     */
    List<ModifyTraceEntity> selectListByPk(@Param(Constants.WRAPPER)QueryWrapper<ModifyTraceEntity> queryWrapper);

    /**
     * 小U留痕列表分页查询
     * @param iPage 分页参数
     * @param wrapper 查询参数
     * @return 修改列表
     */
    Page<ModifyTraceEntity> selectByPage(Page<ModifyTraceEntity> iPage, @Param(Constants.WRAPPER)QueryWrapper<ModifyTraceEntity> wrapper);
}
