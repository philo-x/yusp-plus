package cn.com.yusys.yusp.oca.annotation;

import cn.com.yusys.yusp.common.utils.Constant;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.constants.ResponseAndMessageEnum;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.service.AdminSmCrelStraService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * 策略切面
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@ConditionalOnBean(name = "userDetailsServiceImpl")
@Aspect
@Component
public class StrategyAspect {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(StrategyAspect.class);
    @Autowired
    private CustomCacheService customCacheService;
    @Autowired
    private AdminSmCrelStraService adminSmCrelStraService;
    @Autowired
    private AdminSmUserService adminSmUserService;

    @Pointcut("@annotation(cn.com.yusys.yusp.oca.annotation.Strategy)")
    public void executeStrategy() {
    }

    @Around(value = "executeStrategy() && args(loginCode,password)", argNames = "proceedingJoinPoint,loginCode,password")
    public JClientRspEntity<?> assertAround(ProceedingJoinPoint proceedingJoinPoint, String loginCode, String password) {

        //检查是否因为密码输入错误次数过多，而禁止登录
        String loginCodeCountCacheKey = "loginErrorCount:loginCode_%s".formatted(loginCode);
        JClientRspEntity<String> userForbiddenLogin = checkLoginPasswordErrorCount(loginCodeCountCacheKey);
        if (userForbiddenLogin != null) {
            return userForbiddenLogin;
        }

        //执行登录前的认证策略
        //查询登录策略,若有登录时间段策略，执行登录时间段策略
        JClientRspEntity<String> checkResult = adminSmCrelStraService.checkBeforeLogin(loginCode);

        if (!checkResult.getHead().getRetCode().equals(Constant.SUCCESS_CODE)) {
            return JClientRspEntity.buildFail(checkResult.getHead().getRetCode(), checkResult.getHead().getRetMsg());
        }

        //执行登录验密流程
        Object returnValue;

        JClientRspEntity<?> jClientRspEntity;

        try {
            returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            if (returnValue instanceof JClientRspEntity<?> resultRspEntity) {
                jClientRspEntity = resultRspEntity;
            } else {
                return JClientRspEntity.buildFail(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode(), ResponseAndMessageEnum.BAD_CREDENTIALS.getMessage());
            }
        } catch (Throwable e) {
            log.error("登录方法出错: " + e);
            return JClientRspEntity.buildFail(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode(), ResponseAndMessageEnum.BAD_CREDENTIALS.getMessage());
        }
        // 执行认证策略
        return executeAuthenticationStrategy(loginCode, jClientRspEntity, loginCodeCountCacheKey);
    }

    private JClientRspEntity<String> checkLoginPasswordErrorCount(String loginCodeCountCacheKey) {
        //判断是否存在key,次数不小于限定次数--禁止登录
        String countStr = customCacheService.stringGet(loginCodeCountCacheKey, loginCodeCountCacheKey);
        if (StringUtils.nonEmpty(countStr)) {

            if (Integer.parseInt(countStr) >= Constants.SystemUserConstance.DEFAULT_MAX_PASSWORD_ERROR) {
                return JClientRspEntity.buildFail(ResponseAndMessageEnum.USER_FORBIDDEN_LOGIN.getCode(), ResponseAndMessageEnum.USER_FORBIDDEN_LOGIN.getMessage());
            }

        }
        return null;
    }

    private JClientRspEntity<?> executeAuthenticationStrategy(String loginCode, JClientRspEntity<?> jClientRspEntity, String loginCodeCountCacheKey) {
        JClientRspEntity<String> checkResult;

        if (jClientRspEntity.getHead().getRetCode().equals(Constant.SUCCESS_CODE)) {
            AdminSmUserEntity adminSmUserEntity = adminSmUserService.getOne(new QueryWrapper<AdminSmUserEntity>().eq("LOGIN_CODE", loginCode));
            if (Objects.nonNull(adminSmUserEntity)) {
                String userId = adminSmUserEntity.getUserId();
                JClientRspEntity<?> jClientRspEntity1 = adminSmCrelStraService.checkSuccessLogin(userId, null);

                if (!jClientRspEntity1.getHead().getRetCode().equals(Constant.SUCCESS_CODE)) {
                    return jClientRspEntity1;
                }

                //更新用户最近登录时间信息
                if (jClientRspEntity1.getHead().getRetCode().equals(Constant.SUCCESS_CODE)) {
                    adminSmUserService.updateLoginTime(userId, new Date());
                }

                //查询用户loginCode
                //删除缓存中因为密码输入错误次数过多，而禁止登录的数据
                //判断是否存在key,有--删除
                customCacheService.clear(loginCodeCountCacheKey, loginCodeCountCacheKey);
            }
        } else {
            //验密出错，增加错误次数
            //密码错误次数校验
            if (jClientRspEntity.getHead().getRetCode().equals(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode())) {

                checkResult = adminSmCrelStraService.passwordErrorLimit(loginCode, null);

                return checkResult;
            }

            return jClientRspEntity;
        }
        return jClientRspEntity;
    }
}
