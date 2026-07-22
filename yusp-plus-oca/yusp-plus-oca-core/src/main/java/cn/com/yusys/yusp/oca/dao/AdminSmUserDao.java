package cn.com.yusys.yusp.oca.dao;

import cn.com.yusys.yusp.notice.vo.AdminSmReciveVo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmOrgRoleVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserDetailVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserVo;
import cn.com.yusys.yusp.oca.domain.vo.UserSubscribeVo;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统用户表
 *
 * @author wujp4
 * @date 2020-11-16 15:31:54
 */

public interface AdminSmUserDao extends BaseMapper<AdminSmUserEntity> {
    /**
     * 查询所有用户
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合
     */
    List<AdminSmUserVo> selectAllUser(@Param(Constants.WRAPPER) LambdaQueryWrapper<AdminSmUserVo> queryWrapper);

    /**
     * 详情
     *
     * @param userId 用户id
     * @param orgSeq    机构序列
     * @return 用户详情
     */
    AdminSmUserDetailVo getDetailByIdAndOrg(@Param("userId") String userId, @Param("orgSeq")String orgSeq);
    /**
     * 详情
     *
     * @param userId 用户id
     * @return 用户详情
     */
    AdminSmUserDetailVo getDetailById(String userId);

    /**
     * 查询所有用户
     *
     * @return 用户编号，用户姓名
     */
    List<UserSubscribeVo> selectUserSubscribeVoList();

    /**
     * 关联的角色、岗位、授权机构的数量
     *
     * @param userId 用户id
     * @return 关联的角色、岗位、授权机构的数量
     */
    Integer countRel(String userId);

    /**
     * 更新用户最近登录时间信息
     *
     * @param userId        用户id
     * @param lastLoginTime 最近登录时间
     */
    void updateLoginTime(@Param("userId") String userId, @Param("lastLoginTime") Date lastLoginTime);

    /**
     * 更新用户状态
     *
     * @param userId    用户id
     * @param userState 用户状态
     */
    void updateState(@Param("userId") String userId, @Param("userState") String userState);

    /**
     * 工作流获取用户分页信息
     *
     * @param page    分页模型
     * @param wrapper 条件构造器
     * @return 用户分页信息
     */
    Page<AdminSmUserVo> getUsersForWf(Page<AdminSmUserVo> page, @Param(Constants.WRAPPER) QueryWrapper<AdminSmUserVo> wrapper);

    /**
     * 工作流通过机构号获取用户信息
     *
     * @param orgId 机构号
     * @return 用户信息
     */
    List<AdminSmUserVo> getUsersByOrgForWf(String orgId);

    /**
     * 工作流通过部门id获取用户信息
     *
     * @param deptId 部门id
     * @return 用户信息
     */
    List<AdminSmUserVo> getUsersByDeptForWf(String deptId);

    /**
     * 工作流通过岗位id获取用户信息
     *
     * @param dutyId 岗位id
     * @return 用户信息
     */
    List<AdminSmUserVo> getUsersByDutyForWf(String dutyId);

    /**
     * 工作流通过角色id获取用户信息
     *
     * @param roleId 角色id
     * @return 用户信息
     */
    List<AdminSmUserVo> getUsersByRoleForWf(String roleId);

    /**
     * 工作流通过userId获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    AdminSmUserVo getUserInfoForWf(String userId);

    /**
     * 获取用户的 角色编号 和 机构编号
     *
     * @param userId 用户id
     * @return 用户的角色编号和机构编号
     */
    List<AdminSmReciveVo> selectRoleAndObj(String userId);
    /**
     * 用户按租户id进行分组，获取租户id和租户id对应的用户数
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<Map<String, Object>> groupDataTenantCount();

    /**
     * 根据user_code和login_code判断用户是否已存在
     * @param userCode 用户工号
     * @param loginCode 用户账号
     * @return 用户信息
     */
    @InterceptorIgnore(tenantLine = "true")
    AdminSmUserEntity getUserInfoByCode(@Param("userCode")String userCode, @Param("loginCode") String loginCode);
    /**
     * 查询机构序列orgSeq及以下所有机构的用户
     * @param page 分页
     * @param queryWrapper 查询条件
     * @return 用户列表
     */
    Page<AdminSmUserVo> selectOrgAccessibleUser(Page<AdminSmUserVo> page, @Param(Constants.WRAPPER)QueryWrapper<AdminSmUserVo> queryWrapper);

    /**
     * listOrgRolesByCode
     * @param loginCode
     * @return
     */
    List<AdminSmOrgRoleVo> listOrgRolesByCode(String loginCode);
}
