package cn.com.yusys.yusp.oca.controller;


import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.annotation.LoginBeforeStrategy;
import cn.com.yusys.yusp.oca.annotation.LoginSuccessStrategy;
import cn.com.yusys.yusp.oca.domain.vo.LoginUserDto;
import cn.com.yusys.yusp.oca.domain.vo.UserEntityVo;
import cn.com.yusys.yusp.oca.domain.vo.UserInfoForPasswordDto;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import cn.com.yusys.yusp.oca.service.ThirdPartyLoginService;
import cn.com.yusys.yusp.oca.service.impl.LoginCheckSecretServiceImpl;
import cn.com.yusys.yusp.oca.utils.I18nMessageByCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 登录controller
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Tag(name = "用户登陆")
@RestController
@RequestMapping("/api/login")
public class LoginCheckSecretController {

    private static final Logger log = LoggerFactory.getLogger(LoginCheckSecretController.class);

    @Autowired
    private LoginCheckSecretServiceImpl loginCheckSecretService;

    @Autowired
    private AdminSmUserService adminSmUserService;

    @Autowired
    private ThirdPartyLoginService thirdPartyLoginService;

    @Autowired
    I18nMessageByCode i18nMessageByCode;

    /**
     * 根据用户名密码验密
     * @param req 登录用户信息
     * @return 登录结果及用户信息
     */
    @Operation(summary = "验证用户密码并查询用户信息", description = "验证用户密码并查询用户信息")
    @PostMapping("/queryuserandchecksecret")
    @LoginBeforeStrategy
    @LoginSuccessStrategy
    public JClientRspEntity<?> queryUserAndCheckSecret(@Valid @RequestBody JClientReqEntity<LoginUserDto> req) {
        LoginUserDto loginUserDto = req.getBody();
        return loginCheckSecretService.queryUserAndCheckSecret(loginUserDto.getLoginCode(), loginUserDto.getPassword(), loginUserDto.getNum());
    }

    /**
     * 第三方登录获取用户信息
     * @param req login_code 登陆用户名
     * @return 返回 AdminSmUserEntity
     */
    @PostMapping("/getuserinfowiththirdparty")
    public JClientRspEntity<?> getUserInfoWithThirdParty(@RequestBody JClientReqEntity<LoginUserDto> req) {
        LoginUserDto loginUserDto = req.getBody();
        log.info("调用：第三方登录获取用户信息 接口，获取用户信息，{}", loginUserDto);
        return thirdPartyLoginService.getUserInfoWithThirdParty(loginUserDto.getLoginCode());
    }

    /**
     * 第三方登录获取用户信息
     * @param loginCode login_code 登陆用户名
     * @return 返回 AdminSmUserEntity
     */
    @PostMapping("/getuserinfoforpassword/{loginCode}")
    public JClientRspEntity<UserInfoForPasswordDto> getUserInfoWithForPassword(@PathVariable String loginCode) {
        log.info("调用：第三方登录获取用户信息 接口，获取用户信息，{}", loginCode);
        return loginCheckSecretService.getUserInfoWithForPassword(loginCode);
    }
}
