package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmRoleEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmPasteRoleQuery;
import cn.com.yusys.yusp.oca.domain.query.AdminSmRoleQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmRoleDetailVo;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmRoleVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统角色表
 *
 * @author terry
 * @date 2020-11-18 18:06:35
 */
public interface AdminSmRoleService extends IService<AdminSmRoleEntity> {
    /**
     * 列表
     *
     * @param query 分页查询条件
     * @return 分页查询结果
     */
    Page<AdminSmRoleVo> queryPage(AdminSmRoleQuery query);
    /**
     * 详情
     *
     * @param roleId 角色id
     * @return 角色详情
     */
    AdminSmRoleDetailVo getDetailById(String roleId);
    /**
     * 批量停用角色
     *
     * @param ids 角色id数组
     */
    void batchDisable(String[] ids);
    /**
     * 批量启用角色
     *
     * @param ids 角色id数组
     */
    void batchEnable(String[] ids);
    /**
     * 批量删除角色
     *
     * @param ids 角色id数组
     */
    void batchDelete(String[] ids);
    /**
     * 功能授权 粘贴用
     * 分页查询当前用户有权访问的所有角色列表,排除指定roleId，按最后修改时间降序
     *
     * @param query 用户粘贴列表查询条件
     * @return 用户列表
     */
    Page<AdminSmRoleVo> queryPageExcept(AdminSmPasteRoleQuery query);

    /**
    * 工作流获取角色信息
    * @param query 查询条件
    * @return 角色信息
    */
    Page<AdminSmRoleVo> getRolesForWf(AdminSmRoleQuery query);

    /**
     *获取用户角色
     * @param loginCode 账号
     * @return 用户角色列表
     */
    List<AdminSmRoleVo> getUserRoleByLoginCode(String loginCode);
}

