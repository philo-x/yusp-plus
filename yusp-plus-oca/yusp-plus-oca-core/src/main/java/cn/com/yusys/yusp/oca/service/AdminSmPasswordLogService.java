package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.oca.domain.dto.PasswordLogDto;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.com.yusys.yusp.common.utils.PageUtils;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmPasswordLogEntity;

import java.util.Map;

/**
 * 密码修改记录表
 *
 * @author danyb1
 * @email danyb1@yusys.com.cn
 * @date 2020-12-02 15:07:44
 */
public interface AdminSmPasswordLogService extends IService<AdminSmPasswordLogEntity> {

    /**
     * queryPage
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 更新用户密码修改记录
     * @param passwordLogDto
     */
    void updatePwdLog(PasswordLogDto passwordLogDto);

    /**
     * 获取最后一次变更记录
     * @param userId
     * @return
     */
    AdminSmPasswordLogEntity getLastChangeLog(String userId);

}

