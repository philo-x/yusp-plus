package cn.com.yusys.yusp.single.service;

import cn.com.yusys.yusp.common.utils.Constant;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.constants.ResponseAndMessageEnum;
import cn.com.yusys.yusp.oca.domain.vo.UserEntityVo;
import cn.com.yusys.yusp.oca.domain.vo.UserInfoForPasswordDto;
import cn.com.yusys.yusp.oca.service.AdminSmCrelStraService;
import cn.com.yusys.yusp.oca.service.LoginCheckSecretService;
import cn.com.yusys.yusp.oca.service.ThirdPartyLoginService;
import cn.com.yusys.yusp.uaa.exception.UserVerificationException;
import cn.com.yusys.yusp.uaa.grant.oca.OcaLoginUserInfo;
import cn.com.yusys.yusp.uaa.pojo.TokenParamDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * @author lty
 * @description OcaLoginUserInfo的默认实现
 * @date 2020/12/29
 */
@Service
public class SingleLoginUserInfoImpl implements OcaLoginUserInfo {
    private final Logger log = LoggerFactory.getLogger(SingleLoginUserInfoImpl.class);

    @Value("${yusp.filter.sso.enabled:false}")
    public boolean ssoEnable;

    @Value("${uaa.image.code:IMAGE_CODE_REDIS_KEY}")
    public String imageCodeRedisKey;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    LoginCheckSecretService loginCheckSecretService;
    @Autowired
    private ThirdPartyLoginService thirdPartyLoginService;

    @Autowired
    private AdminSmCrelStraService adminSmCrelStraService;
    @Override
    public TokenParamDto getLoginUserInfo(String username, String password) {
        JClientRspEntity<?> jClientRspEntity;
        if (ssoEnable) {
            jClientRspEntity = thirdPartyLoginService.getUserInfoWithThirdParty(username);
        } else {
            // 获取LoginUserInfo信息
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            password = httpServletRequest.getParameter("password");
            String seq = httpServletRequest.getParameter("seq");
            jClientRspEntity = loginCheckSecretService.queryUserAndCheckSecret(username, password, seq);
            if (jClientRspEntity.getHead().getRetCode().equals(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode())) {
                jClientRspEntity = adminSmCrelStraService.passwordErrorLimit(username, "");
            }
        }

        // 3)判断请求是否有效
        String code = jClientRspEntity.getHead().getRetCode();
        String msg = jClientRspEntity.getHead().getRetMsg();
        if (!Constant.SUCCESS_CODE.equals(code) &&
                !ResponseAndMessageEnum.FIRSTLOGIN.getCode().equals(code)) {
            throw new UserVerificationException(msg);
        }

        // 4)封装token参数
        TokenParamDto tokenParamDto = new TokenParamDto();

        if (jClientRspEntity.getBody() instanceof UserEntityVo) {
            UserEntityVo userEntityVo = (UserEntityVo) (jClientRspEntity.getBody());
            tokenParamDto.setBusinessCode(code);
            tokenParamDto.setLoginCode(userEntityVo.getLoginCode());
            tokenParamDto.setOrgId(userEntityVo.getOrgId());
            tokenParamDto.setUserId(userEntityVo.getUserId());
            tokenParamDto.setLoginSingleAgent(userEntityVo.getLoginSingleAgent());
            tokenParamDto.setPassword(password);
        } else {
            UserInfoForPasswordDto dto = (UserInfoForPasswordDto) jClientRspEntity.getBody();
            tokenParamDto.setBusinessCode(code);
            tokenParamDto.setLoginCode(dto.getLoginCode());
            tokenParamDto.setOrgId(dto.getOrgId());
            tokenParamDto.setUserId(dto.getUserId());
            tokenParamDto.setLoginSingleAgent(dto.getLoginSingleAgent());
            tokenParamDto.setPassword(password);
        }

        return tokenParamDto;
    }

    @Override
    public TokenParamDto getLoginUserInfo(String username) {
        JClientRspEntity<UserInfoForPasswordDto> loginOcaJClientRspEntity = loginCheckSecretService.getUserInfoWithForPassword(username);
        TokenParamDto tokenParamDto = new TokenParamDto();
        UserInfoForPasswordDto userInfoForPasswordDto = loginOcaJClientRspEntity.getBody();
        tokenParamDto.setBusinessCode(loginOcaJClientRspEntity.getHead().getRetCode());
        tokenParamDto.setLoginCode(userInfoForPasswordDto.getLoginCode());
        tokenParamDto.setOrgId(userInfoForPasswordDto.getOrgId());
        tokenParamDto.setUserId(userInfoForPasswordDto.getUserId());
        tokenParamDto.setPassword(userInfoForPasswordDto.getUserPassword());
        tokenParamDto.setLoginSingleAgent(userInfoForPasswordDto.getLoginSingleAgent());

        return tokenParamDto;
    }
}
