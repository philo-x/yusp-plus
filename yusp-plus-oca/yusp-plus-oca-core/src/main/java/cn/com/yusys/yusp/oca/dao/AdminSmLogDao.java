package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmLogEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmLogVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 系统操作日志表
 * 
 * @author danyb1
 * @date 2020-12-02 22:18:19
 */
public interface AdminSmLogDao extends BaseMapper<AdminSmLogEntity> {

    /**
     * 分页条件查询日志数据
     *
     * @param page         分页对象
     * @param queryWrapper 查询条件对象
     * @return 泛型为日志vo的分页对象
     */
    IPage<AdminSmLogVo> pageLogByCondition(Page<AdminSmLogVo> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmLogVo> queryWrapper);

    /**
     * 分页条件查询日志数据
     *
     * @param page         分页对象
     * @param queryWrapper 查询条件对象
     * @return 泛型为日志vo的分页对象
     */
    IPage<AdminSmLogEntity> findLogByLogForm(Page<AdminSmLogEntity> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmLogEntity> queryWrapper);

}