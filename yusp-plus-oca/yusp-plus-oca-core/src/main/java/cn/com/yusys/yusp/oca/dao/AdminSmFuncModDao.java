package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmFuncModEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmFuncModVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 系统功能模块表
 *
 * @author wujp4
 * @date 2020-11-26 10:50:57
 */

public interface AdminSmFuncModDao extends BaseMapper<AdminSmFuncModEntity> {


    /**
     * 系统业务功能分页查询
     * @param page 分页对象
     * @param queryWrapper 查询条件
     * @return 分页查询数据
     * @author zhanyq
     * @date 2021-06-25 10:04
     */
    Page<AdminSmFuncModVo> queryPageWithCondition(IPage<AdminSmFuncModVo> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmFuncModVo> queryWrapper);


}
