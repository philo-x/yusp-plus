package cn.com.yusys.yusp.oca.annotation;

import cn.com.yusys.yusp.common.utils.Constant;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.ResultDto;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.constants.ResponseAndMessageEnum;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.service.AdminSmCrelStraService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import cn.com.yusys.yusp.oca.utils.IpUtil;
import cn.com.yusys.yusp.oca.utils.JsonUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 登录返回值处理
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@ControllerAdvice
public class RestApiResponseAdvice implements ResponseBodyAdvice<JClientRspEntity> {
    
    @Autowired
    private AdminSmCrelStraService adminSmCrelStraService;
    @Autowired
    private AdminSmUserService adminSmUserService;
    @Autowired
    private CustomCacheService customCacheService;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        LoginSuccessStrategy loginSuccessStrategy = methodParameter.getMethod().getAnnotation(LoginSuccessStrategy.class);
        return loginSuccessStrategy != null;
    }

    @Override
    public JClientRspEntity beforeBodyWrite(JClientRspEntity resultDto, MethodParameter methodParameter, MediaType mediaType,
                                     Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        JClientRspEntity checkResult = null;
        String ip = IpUtil.getIpAddr(serverHttpRequest);
        if (!resultDto.getHead().getRetCode().equals(Constant.SUCCESS_CODE)) {
            return passwordErrorLimitCheck(resultDto, ip);
        }

        HashMap resultMap = JsonUtil.parseJson(JSON.toJSONString(resultDto.getBody()), HashMap.class);
        String userId = resultMap.get("userId") == null ? "" : resultMap.get("userId").toString();

        if (StringUtils.isBlank(userId)) {
            return resultDto;
        }

        checkResult = adminSmCrelStraService.checkSuccessLogin(userId, ip);
        JClientRspEntity resultDto1 = getjClientRspEntity(resultDto, checkResult);
        if (resultDto1 != null) {
            return resultDto1;
        }

        updateLoginTimeAndCleanCache(resultDto, userId);

        return resultDto;
    }

    private JClientRspEntity passwordErrorLimitCheck(JClientRspEntity resultDto, String ip) {
        //密码错误次数校验
        if (resultDto.getHead().getRetCode().equals(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode())) {
            String loginCode = resultDto.getBody() == null ? null : ((Map) resultDto.getBody()).get("loginCode") == null ? null : ((Map) resultDto.getBody()).get("loginCode").toString();
            return adminSmCrelStraService.passwordErrorLimit(loginCode, ip);
        }
        return resultDto;
    }

    private static JClientRspEntity getjClientRspEntity(JClientRspEntity resultDto, JClientRspEntity checkResult) {
        if (!checkResult.getHead().getRetCode().equals(Constant.SUCCESS_CODE)) {
            //警告信息不影响用户返回
            if (checkResult.getBody() != null && ((Map) checkResult.getBody()).get("actionType") != null
                    && ((Map) checkResult.getBody()).get("actionType").toString().equals(Constants.SystemUserConstance.USER_STATE_WARNING)) {
                checkResult.setBody(null);
                resultDto.setBody(JsonUtil.parseJson(JSON.toJSONString(checkResult), HashMap.class));

                return resultDto;
            }

            checkResult.setBody(resultDto.getBody());

            return checkResult;
        }
        return null;
    }

    private void updateLoginTimeAndCleanCache(JClientRspEntity resultDto, String userId) {
        //更新用户最近登录时间信息
        if (resultDto.getHead().getRetCode().equals(Constant.SUCCESS_CODE)) {
            adminSmUserService.updateLoginTime(userId, new Date());
        }

        //查询用户loginCode
        AdminSmUserEntity byId = adminSmUserService.getById(userId);
        if (Objects.nonNull(byId)) {
            //删除缓存中因为密码输入错误次数过多，而禁止登录的数据
            String loginCodeCountCacheKey = "loginErrorCount:loginCode_%s".formatted(byId.getLoginCode());
            //判断是否存在key,有--删除
            customCacheService.clear(loginCodeCountCacheKey, loginCodeCountCacheKey);
        }
    }
}
