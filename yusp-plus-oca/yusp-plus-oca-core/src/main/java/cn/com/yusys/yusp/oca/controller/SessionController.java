package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.session.SessionService;
import cn.com.yusys.yusp.commons.session.user.User;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * session的controller
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@RestController
@RequestMapping("api/session")
public class SessionController {

    @Autowired
    SessionService sessionService;
    @Autowired
    private CustomCacheService customCacheService;

    /**
     * 根据authorization查询用户信息
     *
     * @return 用户信息
     */
    @PostMapping(value = "/info")
    public JClientRspEntity<User> getUserByAuthorization() {
        customCacheService.clear("Session", SessionUtils.getClientId() + "-" + SessionUtils.getUserId());
        return JClientRspEntity.buildSuccess(sessionService.getUserInfo(SessionUtils.getClientId(), SessionUtils.getUserId()));
    }
}
