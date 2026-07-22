package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.notice.vo.AdminSmReciveVo;
import cn.com.yusys.yusp.oca.domain.bo.PasswordResetBo;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserDutyRelEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserMgrOrgEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserRoleRelEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmPasteUserQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmUserDutyRelQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmUserQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmUserRoleRelQuery;
import cn.com.yusys.yusp.oca.domain.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;


/**
 * 系统用户表
 *
 * @author terry
 * @date 2020-11-16 15:31:54
 */
public interface AdminSmUserService extends IService<AdminSmUserEntity> {
    /**
     * 分页查询orgId下所有用户列表,按最后修改时间降序
     *
     * @param query 分页查询条件
     * @return 用户列表
     */
    Page<AdminSmUserVo> queryPage(AdminSmUserQuery query);

    /**
     * 详情-限制只能查本机构和子机构的，并且不能查超管，跟page接口限制保持一致
     *
     * @param userId 用户id
     * @return 用户详情
     */
    AdminSmUserDetailVo getDetailByIdAndOrg(String userId);

    /**
     * 详情
     *
     * @param userId 用户id
     * @return 用户详情
     */
    AdminSmUserDetailVo getDetailById(String userId);

    /**
     * 批量停用
     *
     * @param ids 用户id数组
     */
    void batchDisable(String[] ids);

    /**
     * 批量删除
     *
     * @param ids 用户id数组
     */
    void batchDelete(String[] ids);

    /**
     * 批量启用
     *
     * @param ids 用户id数组
     */
    void batchEnable(String[] ids);

    /**
     * 查询用户所在机构及其所有上级机构下可选的所有角色列表
     *
     * @param query 查询用户角色列表条件
     * @return 用户角色列表
     */
    List<UserRoleRelVo> getUserRoleList(AdminSmUserRoleRelQuery query);

    /**
     * 批量保存用户角色关联数据
     *
     * @param userId 被关联的用户id
     * @param list   用户集合
     */
    void saveUserRoleList(String userId, List<AdminSmUserRoleRelEntity> list);

    /**
     * 查询用户所在机构及其所有上级机构下可选的所有岗位列表
     *
     * @param query 查询用户岗位列表条件
     * @return 附带该用户与这些岗位的关联状态
     */
    List<UserDutyRelVo> getUserDutyList(AdminSmUserDutyRelQuery query);

    /**
     * 批量保存用户岗位关联数据
     *
     * @param userId 被关联的用户id
     * @param list   用户集合
     */
    void saveUserDutyList(String userId, List<AdminSmUserDutyRelEntity> list);

    /**
     * 查询用户可授权机构树
     *
     * @param userId 用户id
     * @return 附带该用户与这些机构的授权状态
     */
    List<UserMgrOrgVo> getUserMgrOrgList(String userId);

    /**
     * 批量保存用户授权机构数据
     *
     * @param userId 被授权的用户id
     * @param list   用户信息
     */
    void saveUserMgrOrg(String userId, List<AdminSmUserMgrOrgEntity> list);

    /**
     * 根据authorization查询用户信息
     *
     * @param userId   用户id
     * @param clientId 客户端Id
     * @return 用户vo
     */
    UserSessionVo getUserByAuthorization(String userId, String clientId);

    /**
     * 用户密码修改
     *
     * @param password  密码
     * @param loginCode 账号
     * @return 成功返回 code = 10100001，message"密码策略通过",失败返回 code = 10000001，message"用户名或密码错误"
     */
    JClientRspEntity<String> modifyPassword(String password, String loginCode);

    /**
     * 密码重置
     *
     * @param passwordResetBo 密码重置Bo
     * @return 成功返回 code = 0,失败返回 code = 10100004
     */
    JClientRspEntity<String> resetPassword(PasswordResetBo passwordResetBo);

    /**
     * 查询所有用户
     *
     * @return 用户编号，用户姓名
     */
    List<UserSubscribeVo> selectUserSubscribeVoList();


    /**
     * 更新用户最近登录时间信息
     *
     * @param userId        用户id
     * @param lastLoginTime 最近登录时间
     */
    void updateLoginTime(String userId, Date lastLoginTime);

    /**
     * 功能授权 粘贴用
     * 分页查询当前用户有权访问的所有用户列表,排除指定userId，按最后修改时间降序
     *
     * @param query 用户粘贴列表查询条件
     * @return 用户列表
     */
    Page<AdminSmUserVo> queryPageExcept(AdminSmPasteUserQuery query);


    /**
     * 配合导出，使用用户名称模糊查询用户id
     *
     * @param userName 用户名
     * @return 用户id
     */
    List<AdminSmUserEntity> selectIdLikeName(String userName);

    /**
     * 工作流获取用户分页信息
     *
     * @param query 分页查询实体类
     * @return 用户分页信息
     */
    Page<AdminSmUserVo> getUsersForWf(AdminSmUserQuery query);

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
     * 获取用户姓名
     *
     * @param userId 用户id
     * @return 用户姓名
     */
    String getUserNameById(String userId);

    /**
     * 根据手机号获取用户信息
     *
     * @param userMobilephone 手机号
     * @return 用户信息
     */
    JClientRspEntity<UserEntityVo> getByPhoneNumber(String userMobilephone);

    /**
     * 判断是否存在用户
     * @param entity 新增用户信息
     * @return boolean，是否存在用户
     */
    boolean judgeExistEntity(AdminSmUserEntity entity);

    /**
     * 跟新userName中entity的缓存
     * @param entity 需要添加或者跟新的entity
     */
    void updateUserCache(AdminSmUserEntity entity);

    /**
     * 删除userName缓存中key为ids
     *
     * @param ids ids
     */
    void deletePartUserCache(String[] ids);

    /**
     * 获取用户对应所有机构和角色
     *
     * @param loginCode
     * @return List<AdminSmOrgRoleVo>
     */
    List<AdminSmOrgRoleVo> listOrgRolesByCode(String loginCode);
}

