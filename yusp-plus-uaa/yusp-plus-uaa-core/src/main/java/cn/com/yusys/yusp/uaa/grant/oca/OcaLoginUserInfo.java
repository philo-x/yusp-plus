package cn.com.yusys.yusp.uaa.grant.oca;

import cn.com.yusys.yusp.uaa.pojo.TokenParamDto;

/**
 * @author lty
 * @description 获取Oca的UserInfo信息
 * @date 2020/12/29
 */
public interface OcaLoginUserInfo {

    /**
     * oca模式下的用户信息认证
     *
     * @param username 用户名
     * @param password 密码
     * @return TokenParamDto oca认证的返回信息
     */
    TokenParamDto getLoginUserInfo(String username, String password);

    /**
     * oca刷新模式下的用户信息
     * @param username 用户名
     * @return 用户信息
     */
    TokenParamDto getLoginUserInfo(String username);

}
