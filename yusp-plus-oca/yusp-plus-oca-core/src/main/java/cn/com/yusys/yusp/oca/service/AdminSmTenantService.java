package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmTenantEntity;
import cn.com.yusys.yusp.oca.domain.query.AdminSmTenantQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmTenantVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * 租户管理表
 *
 * @author zhanyq
 * @date 2021-09-17 18:29:55
 */
public interface AdminSmTenantService extends IService<AdminSmTenantEntity> {

    /**
     * 分页查询租户信息
     * @param params 查询条件
     * @return 分页信息
     * @author zhanyq
     * @date 2021-09-18 10:34
     */
    Page<AdminSmTenantEntity> queryPage(AdminSmTenantQuery params);


    /**
     * 保存基本信息，包括租户机构，租户管理员角色，租户管理员用户
     * @param adminSmTenantVo 保存基本信息，包括租户机构，租户管理员角色，租户管理员用户
     * @return void
     * @author zhanyq
     * @date 2021-09-18 10:35
     */
    AdminSmTenantVo saveBaseInfo(AdminSmTenantVo adminSmTenantVo);


    /**
     * 查询租户基础详情信息
     * @param tenantId 租户管理员ID
     * @return 租户基础详情包含 租户基本信息，租户顶级机构信息，租户管理员角色信息，租户管理员用户信息
     * @author zhanyq
     * @date 2021-09-18 17:07
     */
    AdminSmTenantVo getBaseDetail(String tenantId);


    /**
     * 修改基本信息
     * @param adminSmTenantVo 要修改的对象
     * @return void
     * @author zhanyq
     * @date 2021-09-18 17:49
     */
    void updateBaseInfo(AdminSmTenantVo adminSmTenantVo);

    /**
     * 修改租户状态
     * @param tenantIds 要修改的租户ID
     * @param tenantSts 租户状态
     * @return Void
     * @author zhanyq
     * @date 2021-09-22 14:52
     */
    void updateTenantSts(String[] tenantIds, String tenantSts);

    /**
     * 获取登录用户的租户id
     * @return 租户id
     */
    String getTanentId();

    /**
     * 获取用户可操作的租户系统租户id
     * @return 租户id合集
     */
    Set<String> getCanOperatTenants();


}

