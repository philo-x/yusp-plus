package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserRoleRelEntity;
import cn.com.yusys.yusp.oca.domain.vo.UserRelationshipVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联表
 * 
 * @author wujp4
 * @date 2020-11-18 18:06:35
 */

public interface AdminSmUserRoleRelDao extends BaseMapper<AdminSmUserRoleRelEntity> {
    /**
     * 查询用户信息
     * @param queryWrapper 条件构造器
     * @param roleId 角色id
     * @return 用户信息
     */
    List<UserRelationshipVo> selectUserPage(@Param(Constants.WRAPPER) QueryWrapper<UserRelationshipVo> queryWrapper, @Param("roleId") String roleId);

    /**
     * 查询角色关联的有权限的用户
     * @param roleId 角色id
     * @param partAllUserIds 角色机构及下级机构的所有用户
     * @return
     */
    List<String> selectCheckedUserIds(@Param("roleId")String roleId, @Param("partAllUserIds") List<List<String>> partAllUserIds);


    /**
     * 查询用户关联的角色id
     * @param userId 用户id
     * @return 已关联角色id集合
     */
    List<String> findUserRoleRelsByUser(@Param("userId")String userId);

    /**
     * 角色关联的用户
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 角色关联的用户
     */
    Page<UserRelationshipVo> selectRoleRelUserList(Page<UserRelationshipVo> page,@Param(Constants.WRAPPER) QueryWrapper<UserRelationshipVo> queryWrapper);

    /**
     * 查询角色有权限的未关联用户表
     * @param userWrapper 查询条件
     * @param roleId 角色id
     * @return 未关联用户列表
     */
    List<UserRelationshipVo> allRoleUnRelUserList(@Param(Constants.WRAPPER)QueryWrapper<UserRelationshipVo> userWrapper, @Param("roleId")String roleId);

    /**
     * 切换用户角色
     * @param userId 用户id
     * @param roleId 角色id
     */
    void updateCheckedRole(@Param("userId") String userId, @Param("roleId") String roleId);
}

