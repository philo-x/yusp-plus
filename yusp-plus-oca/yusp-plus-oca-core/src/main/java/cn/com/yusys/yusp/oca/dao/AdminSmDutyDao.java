package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmDutyEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDutyVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统岗位表
 * 
 * @author danyb1
 * @date 2020-12-01 21:55:19
 */

public interface AdminSmDutyDao extends BaseMapper<AdminSmDutyEntity> {
    /**
     * 查询所有的岗位
     * @param queryWrapper 条件构造器
     * @return 岗位列表
     */
    List<AdminSmDutyVo> selectAllDuty(@Param(Constants.WRAPPER) QueryWrapper<AdminSmDutyVo> queryWrapper);

    /**
     * getDutysForWf
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<AdminSmDutyVo> getDutysForWf(Page<AdminSmDutyVo> page ,@Param(Constants.WRAPPER) QueryWrapper<AdminSmDutyVo> queryWrapper);

    /**
     * 根据用户id查询岗位代码
     *
     * @param userId 用户id
     * @return 岗位代码
     */
    List<String> getDutysByUserIdForWf(String userId);
    /**
     * 获取有权限的岗位列表
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 获取有权限的岗位列表
     */
    Page<AdminSmDutyVo> selectOrgAccessibleDuty(Page<AdminSmDutyVo> page, @Param(Constants.WRAPPER)QueryWrapper<AdminSmDutyVo> queryWrapper);
}
