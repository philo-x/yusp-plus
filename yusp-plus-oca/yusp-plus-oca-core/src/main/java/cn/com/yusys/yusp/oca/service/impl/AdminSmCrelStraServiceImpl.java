package cn.com.yusys.yusp.oca.service.impl;

import cn.com.yusys.yusp.common.utils.Constant;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.BeanUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.dao.AdminSmCrelStraDao;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.constants.ResponseAndMessageEnum;
import cn.com.yusys.yusp.oca.domain.dto.AdminSmCrelStraDto;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmCrelStraEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmLoginLogEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmPasswordLogEntity;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmUserEntity;
import cn.com.yusys.yusp.oca.domain.vo.UserEntityVo;
import cn.com.yusys.yusp.oca.service.AdminSmCrelStraService;
import cn.com.yusys.yusp.oca.service.AdminSmLoginLogService;
import cn.com.yusys.yusp.oca.service.AdminSmPasswordLogService;
import cn.com.yusys.yusp.oca.service.AdminSmUserService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import cn.com.yusys.yusp.oca.utils.I18nMessageByCode;
import cn.com.yusys.yusp.oca.utils.JsonUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 认证策略业务实现类
 *
 * @author zhanyq
 * @date 2021-06-24 18:01
 */
@Service("adminSmCrelStraService")
public class AdminSmCrelStraServiceImpl extends ServiceImpl<AdminSmCrelStraDao, AdminSmCrelStraEntity> implements AdminSmCrelStraService {

    @Autowired
    private AdminSmUserService adminSmUserService;
    @Autowired
    private AdminSmLoginLogService adminSmLoginLogService;
    @Autowired
    private AdminSmPasswordLogService adminSmPasswordLogService;
    @Autowired
    private CustomCacheService customCacheService;
    @Autowired
    private I18nMessageByCode i18nMessageByCode;

    /**
     * 登录策略获取，若有登录时间段策略，执行该策略
     *
     * @param loginCode 账号
     * @return 执行结果
     */
    @Override
    public JClientRspEntity<String> checkBeforeLogin(String loginCode) {

        // 校验用户是否存在
        AdminSmUserEntity userEntity = adminSmUserService.getOne(new QueryWrapper<AdminSmUserEntity>().eq("LOGIN_CODE", loginCode));

        if (Objects.isNull(userEntity)) {
            return JClientRspEntity.buildFail(ResponseAndMessageEnum.NON_USER.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.NON_USER.getCode()));
        }

        //获取开启的策略
        List<AdminSmCrelStraEntity> enableStrategies = getEnableStrategies();

        //获取登录策略
        List<AdminSmCrelStraEntity> loginEnableStrategies = enableStrategies.stream().filter(s -> s.getCrelKey().startsWith(Constants.SystemUserConstance.LOGIN_KEY_START)
                && s.getCrelKey().endsWith(Constants.SystemUserConstance.BEFORE_LOGIN_KEY_END)).toList();

        // 无有效参数不做拦截, 交给登录接口做账号验证
        if (StringUtils.isBlank(loginCode) || loginEnableStrategies.isEmpty()) {
            return JClientRspEntity.buildSuccess("成功");
        }

        //若是登录时间段策略，即执行该策略
        JClientRspEntity<String> checkResult = new JClientRspEntity<>();

        for (AdminSmCrelStraEntity crelStraEntity : loginEnableStrategies) {

            if (Constants.SystemUserConstance.LOGIN_TIME_PERIOD.equals(crelStraEntity.getCrelKey())) {
                checkResult = checkLoginTimePeriod(userEntity.getUserId(), crelStraEntity);
            }
            if (!checkResult.getHead().getRetCode().equals(Constant.SUCCESS_CODE)) {
                break;
            }

        }

        return checkResult;
    }

    /**
     * 登录时间段策略判断
     * @param userId 用户ID
     * @return
     */
    @Override
    public JClientRspEntity<String> checkLoginTime(String userId) {
        AdminSmCrelStraEntity crelStraEntity = getEnableStrategies().stream().filter(s -> s.getCrelKey().equals(Constants.SystemUserConstance.LOGIN_TIME_PERIOD)).findFirst().orElse(null);
        if (crelStraEntity == null) {
            return JClientRspEntity.buildSuccess("成功");
        }
        return checkLoginTimePeriod(userId, crelStraEntity);
    }

    /**
     * 密码错误次数策略
     *
     * @param loginCode 账号
     * @param ip        ip地址
     * @return 执行结果
     */
    @Override
    public JClientRspEntity<String> passwordErrorLimit(String loginCode, String ip) {

        if (StringUtils.isBlank(loginCode)) {
            return JClientRspEntity.buildFail(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode()));
        }

        //获取开启的策略
        List<AdminSmCrelStraEntity> validStraList = getEnableStrategies();

        //获取密码错误策略
        List<AdminSmCrelStraEntity> validLoginStraList = validStraList.stream().filter(s -> s.getCrelKey().equals(Constants.SystemUserConstance.PASSWORD_WRONG)).collect(Collectors.toList());

        if (validLoginStraList.isEmpty()) {
            return JClientRspEntity.buildFail(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode()));
        }

        //去缓存中设置错误次数
        String loginCodeCountCacheKey = "loginErrorCount:loginCode_%s".formatted(loginCode);
        String countStr = customCacheService.stringGet(loginCodeCountCacheKey, loginCodeCountCacheKey);
        int errorCount = countStr == null ? 0 : Integer.parseInt(countStr);
        ++errorCount;
        int totalLimitCount = StringUtils.isBlank(validLoginStraList.get(0).getCrelDetail()) ? Constants.SystemUserConstance.DEFAULT_MAX_PASSWORD_ERROR : Integer.parseInt(validLoginStraList.get(0).getCrelDetail());
        int leftCount = totalLimitCount - errorCount;
        // 设置缓存
        customCacheService.stringPut(loginCodeCountCacheKey, loginCodeCountCacheKey, String.valueOf(errorCount), Constants.SystemUserConstance.DEFAULT_CACHE_TIME_M);

        // 无剩余次数处理
        if (leftCount <= 0) {
            return JClientRspEntity.buildFail(ResponseAndMessageEnum.USER_FORBIDDEN_LOGIN.getCode(), ResponseAndMessageEnum.USER_FORBIDDEN_LOGIN.getMessage());
        }

        String message = i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.REDUNDANCY.getCode()).formatted(i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode()), leftCount);
        return JClientRspEntity.buildFail(ResponseAndMessageEnum.BAD_CREDENTIALS.getCode(), message);
    }

    /**
     * 登录成功后的策略
     *
     * @param userId 用户ID
     * @param ip     ip地址
     * @return 执行结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JClientRspEntity<?> checkSuccessLogin(String userId, String ip) {
        if (StringUtils.isBlank(userId)) {
            return JClientRspEntity.buildSuccess("成功");
        }

        //获取开启的策略
        List<AdminSmCrelStraEntity> validStraList = getEnableStrategies();

        //获取登录后策略
        List<AdminSmCrelStraEntity> validLoginStraList = validStraList.stream().filter(s -> (s.getCrelKey().startsWith(Constants.SystemUserConstance.LOGIN_KEY_START)
                && s.getCrelKey().endsWith(Constants.SystemUserConstance.AFTER_LOGIN_KEY_END))
                || s.getCrelKey().equals(Constants.SystemUserConstance.PASSWORD_COMPEL_CHANGE)).toList();

        // 校验业务
        JClientRspEntity<?> checkResult = JClientRspEntity.buildSuccess("成功");
        AdminSmCrelStraEntity filedStraEntity = null;
        for (AdminSmCrelStraEntity crelStraEntity : validLoginStraList) {
            switch (crelStraEntity.getCrelKey()) {
                case Constants.SystemUserConstance.LOGIN_FIRST_RULE ->
                        checkResult = checkFirstLogin(userId);
                case Constants.SystemUserConstance.LOGIN_IP_RULE ->
                        checkResult = checkLoginIpChange(userId, ip);
                case Constants.SystemUserConstance.PASSWORD_COMPEL_CHANGE ->
                        checkResult = checkPasswordNeedChange(userId, crelStraEntity);
                default -> {
                }
            }

            if (!checkResult.getHead().getRetCode().equals(Constant.SUCCESS_CODE)) {
                filedStraEntity = crelStraEntity;
                break;
            }
        }

        return failedAction(checkResult, filedStraEntity);
    }

    @Override
    public boolean getLoginSingleAgentEnabled() {
        List<AdminSmCrelStraEntity> validStraList = getEnableStrategies();
        List<AdminSmCrelStraEntity> validLoginStraList = validStraList.stream().filter(s -> s.getCrelKey().equals(Constants.SystemUserConstance.LOGIN_SINGLE_AGENT)).collect(Collectors.toList());

        return !validLoginStraList.isEmpty();
    }

    /**
     * 根据id更新认证策略
     *
     * @param adminSmCrelStraDto 被修改的策略信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateById(AdminSmCrelStraDto adminSmCrelStraDto) {
        // 当前用户
        String currentUser = SessionUtils.getUserId();
        // 当前时间
        Date date = new Date();
        // 非系统生成
        int sysDefault = 0;

        // 更新实体
        AdminSmCrelStraEntity adminSmCrelStraEntity = new AdminSmCrelStraEntity();
        BeanUtils.beanCopy(adminSmCrelStraDto, adminSmCrelStraEntity);
        adminSmCrelStraEntity.setLastChgUsr(currentUser);
        adminSmCrelStraEntity.setLastChgDt(date);
        adminSmCrelStraEntity.setSysDefault(sysDefault);

        // 更新
        this.updateById(adminSmCrelStraEntity);
        // 更新缓存
        if (Objects.nonNull(customCacheService.stringGet(Constants.SystemUserConstance.STRATEGY_CACHE_KEY, Constants.SystemUserConstance.STRATEGY_CACHE_KEY))) {
            customCacheService.clear(Constants.SystemUserConstance.STRATEGY_CACHE_KEY, Constants.SystemUserConstance.STRATEGY_CACHE_KEY);
        }
    }

    @Override
    public int getLoginPersonLimit() {
        List<AdminSmCrelStraEntity> validStraList = getEnableStrategies();
        List<AdminSmCrelStraEntity> validLoginStraList = validStraList.stream().filter(s -> s.getCrelKey().equals(Constants.SystemUserConstance.LOGIN_PERSON_LIMIT)).collect(Collectors.toList());
        if(!validLoginStraList.isEmpty()){
            String crelDetail = validLoginStraList.get(0).getCrelDetail();
            return  StringUtils.isNumeric(crelDetail)? Integer.parseInt(crelDetail):0;
        }
        return 0;
    }

    /**
     * 首次登录策略
     *
     * @param userId 用户id
     * @return 执行策略结果
     */
    @Transactional(rollbackFor = Exception.class)
    public JClientRspEntity<Object> checkFirstLogin(String userId) {
        //根据用户id获取用户信息
        AdminSmUserEntity adminSmUserEntity = adminSmUserService.getById(userId);

        if (Objects.isNull(adminSmUserEntity)) {
            return JClientRspEntity.buildSuccess("成功");
        }
        //获取最后登录时间，要是最后登录时间为空，即返回首次登录码
        if (Objects.isNull(adminSmUserEntity.getLastLoginTime())) {
            UserEntityVo userEntityVo = new UserEntityVo();
            BeanUtils.beanCopy(adminSmUserEntity, userEntityVo);
            return JClientRspEntity.buildSuccess(userEntityVo);
        }
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 登录ip策略
     *
     * @param userId 用户id
     * @param ip     用户ip
     * @return 执行结果
     */
    @Transactional(rollbackFor = Exception.class)
    public JClientRspEntity<String> checkLoginIpChange(String userId, String ip) {
        if (StringUtils.isBlank(ip)) {
            return JClientRspEntity.buildSuccess("成功");
        }

        AdminSmLoginLogEntity lastSuccessLoginLog = adminSmLoginLogService.getLastSuccessLogin(userId);
        AdminSmLoginLogEntity adminSmLoginLogEntity = new AdminSmLoginLogEntity();
        adminSmLoginLogEntity.setLogId(UUID.randomUUID().toString().replace("-", ""));
        adminSmLoginLogEntity.setTradeId("");
        adminSmLoginLogEntity.setLoginCode(userId);
        adminSmLoginLogEntity.setChnlNo("");
        adminSmLoginLogEntity.setIpAddress(ip);
        adminSmLoginLogEntity.setDeviceId("");
        adminSmLoginLogEntity.setTradeCode("");
        adminSmLoginLogEntity.setOperResult(Constants.LoginLogConstance.SUCCESS);
        adminSmLoginLogEntity.setOperDetail("");
        adminSmLoginLogEntity.setOperDate(new Date());
        if (lastSuccessLoginLog == null) {
            // 只记录ip
            adminSmLoginLogService.saveLog(adminSmLoginLogEntity);
            return JClientRspEntity.buildSuccess("成功");
        }

        if (!lastSuccessLoginLog.getIpAddress().equals(ip)) {
            adminSmLoginLogService.saveLog(adminSmLoginLogEntity);
            return JClientRspEntity.buildFail(ResponseAndMessageEnum.IPERROR.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.IPERROR.getCode()));
        }

        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 密码强制更改策略
     *
     * @param userId         用户id
     * @param crelStraEntity 认证策略
     * @return 执行结果
     */
    @Transactional(rollbackFor = Exception.class)
    public JClientRspEntity<String> checkPasswordNeedChange(String userId, AdminSmCrelStraEntity crelStraEntity) {

        AdminSmPasswordLogEntity lastUpdateEntity = adminSmPasswordLogService.getLastChangeLog(userId);

        if (lastUpdateEntity == null || lastUpdateEntity.getLastChgDt() == null) {
            return JClientRspEntity.buildSuccess("成功");
        }

        int changeDay = Integer.parseInt(crelStraEntity.getCrelDetail());
        // 当前日期
        LocalDate now = LocalDate.now();
        // 最后一次修改密码日期
        LocalDate lastUpdateDate = LocalDateTime.ofInstant(lastUpdateEntity.getLastChgDt().toInstant(), ZoneId.systemDefault()).toLocalDate();
        // 相差天数
        long diffDays = ChronoUnit.DAYS.between(lastUpdateDate, now);

        if (diffDays >= changeDay) {
            return JClientRspEntity.buildFail(ResponseAndMessageEnum.PASSWORD_NEED_CHANGE.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.PASSWORD_NEED_CHANGE.getCode()));
        }
        
        return JClientRspEntity.buildSuccess("成功");
    }


    /**
     * 登录时间段校验
     *
     * @param userId      用户ID
     * @param crelStraEntity 策略实体
     * @return 校验结果
     * @author zhanyq
     * @date 2021-06-24 18:05
     */
    private JClientRspEntity<String> checkLoginTimePeriod(String userId, AdminSmCrelStraEntity crelStraEntity) {

        // 若为管理员admin，即不会执行登录时间策略
        if (Constants.AdminSmLogicSysConstance.ADMIN_USER_ID.equals(userId)) {
            return JClientRspEntity.buildSuccess("成功");
        }

        String detail = crelStraEntity.getCrelDetail();
        String[] split = detail.split(",");
        LocalTime now = LocalTime.now();
        LocalTime startTime = LocalTime.parse(split[0]);
        LocalTime endTime = LocalTime.parse(split[1]);

        if (!now.isAfter(startTime) || !now.isBefore(endTime)) {
            return JClientRspEntity.buildFail(ResponseAndMessageEnum.LOGIN_TIMES_ERROR.getCode(), i18nMessageByCode.getMessageByCode(ResponseAndMessageEnum.LOGIN_TIMES_ERROR.getCode()));
        }

        return JClientRspEntity.buildSuccess("成功");
    }


    /**
     * 获取开启的策略
     *
     * @return 开启的策略集合
     * @author zhanyq
     * @date 2021-06-24 18:06
     */
    private List<AdminSmCrelStraEntity> getEnableStrategies() {
        List<AdminSmCrelStraEntity> validStraList;

        String jsonStr = customCacheService.stringGet(Constants.SystemUserConstance.STRATEGY_CACHE_KEY, Constants.SystemUserConstance.STRATEGY_CACHE_KEY);
        if (!StringUtils.isBlank(jsonStr)) {
            validStraList = JsonUtil.parseJsonArray(jsonStr, AdminSmCrelStraEntity.class);
        } else {
            QueryWrapper<AdminSmCrelStraEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ENABLE_FLAG", Constants.SystemUserConstance.ENABLE_FLAG_TRUE);
            validStraList = getBaseMapper().selectList(queryWrapper);
            customCacheService.stringPut(Constants.SystemUserConstance.STRATEGY_CACHE_KEY,
                    Constants.SystemUserConstance.STRATEGY_CACHE_KEY,
                    JSON.toJSONString(validStraList),
                    Constants.SystemUserConstance.DEFAULT_CACHE_TIME_H);
        }

        return validStraList;
    }

    /**
     * 失败后执行
     * @param jClientRspEntity 结果参数
     * @param adminSmCrelStraEntity 认证策略
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public JClientRspEntity<?> failedAction(JClientRspEntity<?> jClientRspEntity, AdminSmCrelStraEntity adminSmCrelStraEntity) {

        if (Constant.SUCCESS_CODE.equals(jClientRspEntity.getHead().getRetCode()) || Objects.isNull(adminSmCrelStraEntity)) {
            return jClientRspEntity;
        }

        String actionType = adminSmCrelStraEntity.getActionType();
        ((Map)jClientRspEntity.getBody()).put("actionType", actionType);

        return jClientRspEntity;
    }
}