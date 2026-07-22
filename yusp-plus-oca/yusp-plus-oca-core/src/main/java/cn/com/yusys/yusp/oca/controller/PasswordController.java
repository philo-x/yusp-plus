package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.session.SessionService;
import cn.com.yusys.yusp.commons.session.user.User;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.oca.domain.bo.PasswordCheckBo;
import cn.com.yusys.yusp.oca.domain.bo.PasswordModifyBo;
import cn.com.yusys.yusp.oca.domain.bo.PasswordResetBo;
import cn.com.yusys.yusp.oca.domain.constants.MessageEnums;
import cn.com.yusys.yusp.oca.service.PasswordService;
import cn.com.yusys.yusp.oca.utils.I18nMessageByCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 密码controller
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Tag(name = "密码相关请求")
@RestController
@RequestMapping("/api")
public class PasswordController {

    @Autowired
    PasswordService passwordService;
    @Autowired
    SessionService sessionService;
    @Autowired
    I18nMessageByCode i18nMessageByCode;

    /**
     * 用户密码校验
     *
     * @param passwordCheckBo 密码校验参数
     * @return 密码校验结果
     */
    @Operation(summary = "密码验密", description = "密码验密")
    @PostMapping("/passwordcheck/checkpwd")
    public JClientRspEntity<String> checkPasswrod(@Valid @RequestBody JClientReqEntity<PasswordCheckBo> passwordCheckBo) {
        return passwordService.checkPasswrod(passwordCheckBo.getBody());
    }


    /**
     * 密码修改
     *
     * @param passwordModifyBo 密码修改参数
     * @return 密码修改结果
     */
    @Operation(summary = "密码修改", description = "密码修改")
    @PostMapping("/password/passwordmodification")
    public JClientRspEntity<String> passwordModification(@RequestBody @Validated JClientReqEntity<PasswordModifyBo> passwordModifyBo) {
        User userInfo = sessionService.getUserInfo(SessionUtils.getClientId(), SessionUtils.getUserId());
        if (passwordModifyBo.getBody().getLoginCode().equals(userInfo.getLoginCode())){
            return passwordService.passwordModification(passwordModifyBo.getBody());
        }else{
            return JClientRspEntity.buildFail(MessageEnums.PASSWORD_MISMATCH.getCode(), i18nMessageByCode.getMessageByCode(MessageEnums.PASSWORD_MISMATCH.getCode()));
        }
    }

    /**
     * 密码重置,密码重置为123456
     *
     * @param passwordResetBo 密码重置参数
     * @return 密码重置结果
     */
    @Operation(summary = "密码重置", description = "密码重置")
    @PostMapping("/password/resetpassword")
    public JClientRspEntity<String> resetPassword(@Valid @RequestBody JClientReqEntity<PasswordResetBo> passwordResetBo) {
        return passwordService.resetPassword(passwordResetBo.getBody());
    }
}
