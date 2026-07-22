package cn.com.yusys.yusp.oca.service;


import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.vo.UserEntityVo;
import cn.com.yusys.yusp.oca.domain.vo.UserInfoForPasswordDto;

/**
 * 登录接口
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public interface LoginCheckSecretService {
    /**
     * 校验用户密码
     *
     * @param loginCode 登录名
     * @param password  密码
     * @param num num
     * @return 用户信息
     */
    JClientRspEntity<?> queryUserAndCheckSecret(String loginCode, String password, String num);

    /**
     * 密码模式获取用户信息
     * @param loginCode
     * @return
     */
    JClientRspEntity<UserInfoForPasswordDto> getUserInfoWithForPassword(String loginCode);
}
