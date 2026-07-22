package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmTenantUserRelEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 租户超级用户关联表
 *
 * @author chenjing
 * @date 20202-03-31 19:06:35
 */
public interface AdminSmTenantUserRelService extends IService<AdminSmTenantUserRelEntity> {
    /**
     * 获取租户的超级管理员用户
     * @param tenantId 租户id
     * @return 租户关联的超级用户
     */
    AdminSmTenantUserRelEntity getTenantUserRelInfo(String tenantId);

    /**
     * getTenantUserId
     * @return
     */
    String getTenantUserId();

}

