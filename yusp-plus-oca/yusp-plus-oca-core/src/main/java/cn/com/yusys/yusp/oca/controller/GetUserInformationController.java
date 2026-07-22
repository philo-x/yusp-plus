package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.vo.UserEntityVo;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 获取用户信息
 *
 * @author: wujp4
 * @date: 2021-03-30 09:53
 */
@RestController
@RequestMapping("/api/userinfo")
public class GetUserInformationController {

    @Autowired
    private AdminSmUserService adminSmUserService;

    /**
     * 根据用户手机号获取用户信息
     * @param req 用户手机号
     * @return 用户信息
     */
    @PostMapping("/getbyphonenumber")
    public JClientRspEntity<UserEntityVo> getByPhoneNumber(@RequestBody JClientReqEntity<Map<String,String>> req) {
        Map<String,String> params = req.getBody();
        return adminSmUserService.getByPhoneNumber(params.get("userMobilephone"));
    }
}
