package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.constants.ResponseAndMessageEnum;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.domain.vo.UserInfoForPasswordDto;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import cn.com.yusys.yusp.oca.service.ThirdPartyLoginService;
import cn.com.yusys.yusp.oca.utils.I18nMessageByCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author yusys
 */
@Service
public class ThirdPartyLoginServiceImpl implements ThirdPartyLoginService {

    @Autowired
    private AdminSmUserService adminSmUserService;

    @Autowired
    I18nMessageByCode i18nMessageByCode;

    @Override
    public JClientRspEntity<?> getUserInfoWithThirdParty(String loginCode) {

        QueryWrapper<AdminSmUserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("login_code", loginCode);
        AdminSmUserEntity userEntity = adminSmUserService.getOne(wrapper);

        if (Objects.nonNull(userEntity)) {
            //判断用户是否有效
            if (StringUtils.nonEmpty(userEntity.getUserSts().getCode()) && !StringUtils.equals(AvailableStateEnum.ENABLED.getCode(), userEntity.getUserSts().getCode())) {
                return JClientRspEntity.buildFail(ResponseAndMessageEnum.USER_INVALID.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.USER_INVALID.getCode()));
            }

            UserInfoForPasswordDto passwordDto = new UserInfoForPasswordDto();
            passwordDto.setUserPassword(userEntity.getUserPassword());
            BeanUtils.beanCopy(userEntity, passwordDto);
            return JClientRspEntity.buildSuccess(passwordDto);

        } else {
            return JClientRspEntity.buildFail(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode()));
        }
    }
}
