package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmRoleEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmRoleDetailVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统角色表
 * 
 * @author wujp4
 * @date 2020-11-18 18:06:35
 */

public interface AdminSmRoleDao extends BaseMapper<AdminSmRoleEntity> {
    /**
     *查询对应条件下的角色信息
     * @param queryWrapper 条件构造器
     * @return 用户列表
     */
    List<AdminSmRoleVo> selectAllRole(@Param(Constants.WRAPPER) QueryWrapper<AdminSmRoleVo> queryWrapper);

    /**
     * 详情
     *
     * @param roleId 角色id
     * @return 角色详情
     */
    AdminSmRoleDetailVo selectDetailById(String roleId);

    /**
     *工作流获取角色信息
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 工作流角色列表
     */
    Page<AdminSmRoleVo> getRolesForWf(Page<AdminSmRoleVo> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmRoleVo> queryWrapper);

    /**
     *获取用户角色
     * @param loginCode 账号
     * @return 用户角色列表
     */
    List<AdminSmRoleVo> getUserRoleByLoginCode(String loginCode);


    /**
     * 查询条件
     * @param page 分页
     * @param wrapper 查询条件
     * @return 查询条件
     */
    Page<AdminSmRoleVo> selectOrgAccessible(Page<AdminSmRoleVo> page,@Param(Constants.WRAPPER)QueryWrapper<AdminSmRoleVo> wrapper);

    /**
     * 登录用户有权限查看的角色列表
     * @param page
     * @param wrapper
     * @return
     */
    Page<AdminSmRoleVo> getAccessRoleList(Page<AdminSmRoleVo> page, @Param(Constants.WRAPPER)QueryWrapper<AdminSmRoleVo> wrapper);
}
