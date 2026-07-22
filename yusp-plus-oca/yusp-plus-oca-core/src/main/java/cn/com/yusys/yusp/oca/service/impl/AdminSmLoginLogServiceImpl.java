package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.oca.dao.AdminSmLoginLogDao;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmLoginLogEntity;
import cn.com.yusys.yusp.oca.service.AdminSmLoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @description: 类说明
 * @author: zhangsong
 * @date: 2021/4/1
 */
@Service("adminSmLoginLogService")
public class AdminSmLoginLogServiceImpl extends ServiceImpl<AdminSmLoginLogDao, AdminSmLoginLogEntity> implements AdminSmLoginLogService {
    @Override
    public void saveLog(AdminSmLoginLogEntity adminSmLoginLogEntity) {
        getBaseMapper().insert(adminSmLoginLogEntity);
    }

    @Override
    public AdminSmLoginLogEntity getLastSuccessLogin(String loginCode) {
        return getBaseMapper().getLastLoginLog(loginCode, Constants.LoginLogConstance.SUCCESS);
    }
}
