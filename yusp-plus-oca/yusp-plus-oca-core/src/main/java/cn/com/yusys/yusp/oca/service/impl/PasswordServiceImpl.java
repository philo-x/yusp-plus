package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.domain.bo.PasswordCheckBo;
import cn.com.yusys.yusp.oca.domain.bo.PasswordModifyBo;
import cn.com.yusys.yusp.oca.domain.bo.PasswordResetBo;
import cn.com.yusys.yusp.oca.domain.constants.MessageEnums;
import cn.com.yusys.yusp.oca.domain.constants.PasswordEnum;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import cn.com.yusys.yusp.oca.service.PasswordService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import cn.com.yusys.yusp.oca.utils.I18nMessageByCode;
import cn.com.yusys.yusp.oca.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 密码业务类
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Service
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    private PasswordUtils passwordUtils;

    @Autowired
    private AdminSmUserService adminSmUserService;

    @Autowired
    I18nMessageByCode i18nMessageByCode;

    @Autowired
    private CustomCacheService customCacheService;

    /**
     * 密码验证
     *
     * @param passwordCheckBo 密码参数
     * @return 密码验证码
     */
    @Override
    public JClientRspEntity<String> checkPasswrod(PasswordCheckBo passwordCheckBo) {
        passwordUtils.dePassword(passwordCheckBo.getPwd());
        return JClientRspEntity.buildFail(MessageEnums.PASSWORD_COMPLEX_SUCCESS.getCode(), i18nMessageByCode.getMessageByCode(MessageEnums.PASSWORD_COMPLEX_SUCCESS.getCode()));
    }

    /**
     * 密码修改
     *
     * @param passwordModifyBo 密码修改参数
     * @return 密码修改码
     */
    @Override
    public JClientRspEntity<String> passwordModification(PasswordModifyBo passwordModifyBo) {
        //匹配登录密码
        boolean isMatchUserPassword;
        isMatchUserPassword = passwordUtils.matchUserPassword(passwordModifyBo.getRawPassword(), passwordModifyBo.getLoginCode(), passwordModifyBo.getSeq());
        if (!isMatchUserPassword) {
            return JClientRspEntity.buildFail(MessageEnums.PASSWORD_MISMATCH.getCode(), i18nMessageByCode.getMessageByCode(MessageEnums.PASSWORD_MISMATCH.getCode()));
        }
        //密码修改
        JClientRspEntity<String> r = new JClientRspEntity<>();
        if (StringUtils.nonEmpty(passwordModifyBo.getPassword()) && StringUtils.nonEmpty(passwordModifyBo.getLoginCode())) {
            //密码修改
            r = adminSmUserService.modifyPassword(passwordModifyBo.getPassword(), passwordModifyBo.getLoginCode());
        }
        if (StringUtils.nonEmpty(r.getHead().getRetCode()) && "0000".equals(r.getHead().getRetCode())) {
            return JClientRspEntity.buildSuccess("重置密码成功");
        } else {
            return JClientRspEntity.buildFail(MessageEnums.PASSWORD_MODIFY_FAILED.getCode(), i18nMessageByCode.getMessageByCode(MessageEnums.PASSWORD_MODIFY_FAILED.getCode()));
        }
    }

    /**
     * 密码重置
     *
     * @param passwordResetBo 密码重置参数
     * @return 重置结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JClientRspEntity<String> resetPassword(PasswordResetBo passwordResetBo) {

        //删除缓存中因为密码输入错误次数过多，而禁止登录的数据
        String loginCodeCountCacheKey = "loginErrorCount:loginCode_%s".formatted(passwordResetBo.getLoginCode());
        //删除错误次数
        customCacheService.clear(loginCodeCountCacheKey,loginCodeCountCacheKey);

        return adminSmUserService.resetPassword(passwordResetBo);
    }
}
