package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.oca.dao.AdminSmTenantUserRelDao;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmTenantUserRelEntity;
import cn.com.yusys.yusp.oca.service.AdminSmTenantService;
import cn.com.yusys.yusp.oca.service.AdminSmTenantUserRelService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 租户超级用户关联表
 *
 * @author chenjing
 * @date 20202-03-31 19:06:35
 */

@Service("adminSmTenantUserRelService")
public class AdminSmTenantUserRelServiceImpl extends ServiceImpl<AdminSmTenantUserRelDao, AdminSmTenantUserRelEntity> implements AdminSmTenantUserRelService {



    @Autowired
    private AdminSmTenantService adminSmTenantService;
    @Override
    public AdminSmTenantUserRelEntity getTenantUserRelInfo(String tenantId) {
        QueryWrapper<AdminSmTenantUserRelEntity> tenantRoleRelWrapper = new QueryWrapper<>();
        tenantRoleRelWrapper.eq("data_tenant_id",tenantId);
        return this.getOne(tenantRoleRelWrapper);
    }

    @Override
    public String getTenantUserId() {
        String tanentId = adminSmTenantService.getTanentId();

        if(tanentId == null){
            tanentId="1";
        }
        AdminSmTenantUserRelEntity tenanteInfo = this.getTenantUserRelInfo(tanentId);

        return tenanteInfo.getUserId();
    }
}