package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.common.utils.Constant;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspHeader;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.notice.controller.AdminSmRicheditFileInfoController;
import cn.com.yusys.yusp.oca.annotation.Strategy;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.constants.ResponseAndMessageEnum;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.domain.vo.UserEntityVo;
import cn.com.yusys.yusp.oca.domain.vo.UserInfoForPasswordDto;
import cn.com.yusys.yusp.oca.service.AdminSmCrelStraService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import cn.com.yusys.yusp.oca.service.LoginCheckSecretService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import cn.com.yusys.yusp.oca.utils.I18nMessageByCode;
import cn.com.yusys.yusp.oca.utils.PasswordUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 登录业务类
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Service
public class LoginCheckSecretServiceImpl implements LoginCheckSecretService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AdminSmRicheditFileInfoController.class);

    private static final String ACCESS = "access:";
    @Autowired
    AdminSmUserService adminSmUserService;
    @Autowired
    PasswordUtils passwordUtils;
    @Autowired
    private AdminSmCrelStraService adminSmCrelStraService;
    @Autowired
    I18nMessageByCode i18nMessageByCode;
    @Autowired
    private CustomCacheService customCacheService;



    /**
     * 校验用户密码
     *
     * @param loginCode 登录名
     * @param password 密码
     * @return 用户信息
     */
    @Override
    @Strategy
    public JClientRspEntity<?> queryUserAndCheckSecret(String loginCode, String password, String seq) {
        //查询用户信息
        AdminSmUserEntity userEntity = adminSmUserService.getOne(new QueryWrapper<AdminSmUserEntity>().eq("LOGIN_CODE", loginCode));

        if (Objects.nonNull(userEntity)) {
            // 优先判断是否存在登录时间段限制，如果存在，则判断是否在登录时间段内
            JClientRspEntity<String> checkLoginTimeResult = adminSmCrelStraService.checkLoginTime(userEntity.getUserId());
            if (!checkLoginTimeResult.getHead().getRetCode().equals(Constant.SUCCESS_CODE)) {
                return JClientRspEntity.buildFail(checkLoginTimeResult.getHead().getRetCode(), checkLoginTimeResult.getHead().getRetMsg());
            }

            //判断用户是否有效
            if (StringUtils.nonEmpty(userEntity.getUserSts().getCode()) && !StringUtils.equals(AvailableStateEnum.ENABLED.getCode(), userEntity.getUserSts().getCode())) {
                return JClientRspEntity.buildFail(ResponseAndMessageEnum.USER_INVALID.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.USER_INVALID.getCode()));
            }else if(!ObjectUtils.isEmpty(userEntity.getDeadline())){
                Date now=new Date();
                if((now.getTime()-userEntity.getDeadline().getTime()) > 0){
                    return JClientRspEntity.buildFail(ResponseAndMessageEnum.EXPIRED.getCode(),i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.EXPIRED.getCode()));
                }
            }

            UserEntityVo userEntityVoDb = new UserEntityVo();
            BeanUtils.copyProperties(userEntity, userEntityVoDb);
            //校验密码
            return validPassword(password, seq, userEntity, userEntityVoDb);
        } else {
            return JClientRspEntity.buildFail(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode()));
        }
    }

    private JClientRspEntity<? extends Serializable> validPassword(String password, String seq, AdminSmUserEntity userEntity, UserEntityVo userEntityVoDb) {
        boolean isValid = passwordUtils.checkSecret(userEntity.getUserPassword(), password, seq);
        if (isValid) {
            //查看是否有设置人数限制
            int loginPersonLimit = adminSmCrelStraService.getLoginPersonLimit();
            log.info("是否有人数限制的设定："+loginPersonLimit);
            if (loginPersonLimit > 0){
                //判断现在登录人数是否大于等于人数限制
                int accountLoginPerson=customCacheService.nameCountKey(ACCESS+"*");
                log.info("现在登录人数"+accountLoginPerson);
                if(accountLoginPerson >= loginPersonLimit){
                    return JClientRspEntity.buildFail(ResponseAndMessageEnum.GREEN.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.GREEN.getCode()));
                }
            }

            //设置渠道互斥标识
            userEntityVoDb.setLoginSingleAgent(adminSmCrelStraService.getLoginSingleAgentEnabled());
            //增加首次登录强制改密码判断20220907
            if (ObjectUtils.isEmpty(userEntity.getLastLoginTime())){
                JClientRspHeader jClientRspHeader=new JClientRspHeader("10000002","首次登录");
                return new JClientRspEntity<>(jClientRspHeader, userEntityVoDb);
            }else{
                return JClientRspEntity.buildSuccess(userEntityVoDb);
            }
        } else {
            return JClientRspEntity.buildFail(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode()));
        }
    }

    /**
     * 密码模式获取用户信息
     * @param loginCode
     * @return
     */
    @Override
    public JClientRspEntity<UserInfoForPasswordDto> getUserInfoWithForPassword(String loginCode) {
        //查询用户信息
        AdminSmUserEntity userEntity = adminSmUserService.getOne(new QueryWrapper<AdminSmUserEntity>().eq("LOGIN_CODE", loginCode));
        UserInfoForPasswordDto passwordDto = new UserInfoForPasswordDto();
        BeanUtils.copyProperties(userEntity, passwordDto);
        return JClientRspEntity.buildSuccess(passwordDto);
    }
}
