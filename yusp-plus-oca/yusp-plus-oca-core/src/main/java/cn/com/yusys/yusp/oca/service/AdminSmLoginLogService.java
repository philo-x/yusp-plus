package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.entity.AdminSmLoginLogEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @description: 登录记录
 * @author zhangsong
 * @date: 2021/4/1
 */
public interface AdminSmLoginLogService extends IService<AdminSmLoginLogEntity> {

    /**
     * saveLog
     * @param adminSmLoginLogEntity
     */
    void saveLog(AdminSmLoginLogEntity adminSmLoginLogEntity);


    /**
     * getLastSuccessLogin
     * @param loginCode
     * @return
     */
    AdminSmLoginLogEntity getLastSuccessLogin(String loginCode);
}
