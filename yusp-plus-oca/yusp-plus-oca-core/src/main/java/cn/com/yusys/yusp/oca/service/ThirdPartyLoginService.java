package cn.com.yusys.yusp.oca.service;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.vo.UserInfoForPasswordDto;

/**
 * 第三发登录用户信息查询 service
 * @author zhangyt12
 * @date 2021/7/19 10:33
 */
public interface ThirdPartyLoginService {

    /**
     * 使用用户登录码查询用户信息
     * @param loginCode 用户登录码
     * @return 返回响应类
     */
    JClientRspEntity<?> getUserInfoWithThirdParty(String loginCode);
}
