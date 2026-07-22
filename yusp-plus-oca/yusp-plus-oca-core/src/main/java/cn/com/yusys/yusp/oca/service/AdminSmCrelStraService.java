package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.dto.AdminSmCrelStraDto;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmCrelStraEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 认证策略参数表
 *
 * @author wujp4
 * @date 2021-03-30 11:27:32
 */
public interface AdminSmCrelStraService extends IService<AdminSmCrelStraEntity> {

    /**
     * 登录检查
     *
     * @param loginCode 账号
     * @return 检查结果
     * @author zhanyq
     * @date 2021-06-24 17:49
     */
    JClientRspEntity<String> checkBeforeLogin(String loginCode);

    /**
     * 登录时间段校验
     *
     * @param loginCode 账号
     * @return 校验结果
     */
    JClientRspEntity<String> checkLoginTime(String loginCode);

    /**
     * 密码错误次数校验
     *
     * @param loginCode 账号
     * @param ip        ip地址
     * @return 校验结果
     * @author zhanyq
     * @date 2021-06-24 17:50
     */
    JClientRspEntity<String> passwordErrorLimit(String loginCode, String ip);

    /**
     * 登录成功后校验
     *
     * @param userId 用户ID
     * @param ip     ip地址
     * @return 校验结果
     * @author zhanyq
     * @date 2021-06-24 17:55
     */
    JClientRspEntity<?> checkSuccessLogin(String userId, String ip);

    /**
     * 渠道互斥登录策略校验
     *
     * @return 校验结果
     * @author zhanyq
     * @date 2021-06-24 17:57
     */
    boolean getLoginSingleAgentEnabled();


    /**
     * 批量修改认证策略
     *
     * @param adminSmCrelStraDto 被修改的策略信息
     * @return void
     * @author zhanyq
     * @date 2021-06-24 17:59
     */
    void updateById(AdminSmCrelStraDto adminSmCrelStraDto);

    /**
     * 获取登录人数限制策略
     * @return 登录人数限制策略
     */
    int getLoginPersonLimit();
}

