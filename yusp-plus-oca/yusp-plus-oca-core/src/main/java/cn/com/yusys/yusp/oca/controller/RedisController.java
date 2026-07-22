package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.exception.PlatformException;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.*;
import cn.com.yusys.yusp.oca.service.*;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 初始化到controller
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Deprecated
@RestController
@RequestMapping("/api")
public class RedisController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RedisController.class);
    @Autowired
    AdminSmUserService adminSmUserService;
    @Autowired
    AdminSmOrgService adminSmOrgService;
    @Autowired
    AdminSmDptService adminSmDptService;
    @Autowired
    AdminSmMessageService adminSmMessageService;
    @Autowired
    AdminSmPropService adminSmPropService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private CustomCacheService customCacheService;


    /**
     * 将用户信息初始化到redis
     *
     * @return 初始化结果
     */
    @PostMapping("/userredis")
    public JClientRspEntity<String> insertUserToRedis() {
        List<AdminSmUserEntity> list = adminSmUserService.list();
        // 使用hash类型存储字典 例如: redisKey -> userName  HashKey -> 40 HashValue -> 系统管理员
        Map<String, String> collect = list.stream().collect(Collectors.toMap(AdminSmUserEntity::getUserId, AdminSmUserEntity::getUserName));
        customCacheService.hashPut(Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME, Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME, collect, 24 * 3600);
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 将机构信息初始化到redis
     *
     * @return 初始化结果
     */
    @PostMapping("/orgredis")
    public JClientRspEntity<String> insertOrgToRedis() {
        List<AdminSmOrgEntity> list = adminSmOrgService.list();
        // 使用hash类型存储字典 例如: redisKey -> orgName  HashKey -> 40 HashValue -> 银行
        Map<String, String> collect = list.stream().collect(Collectors.toMap(AdminSmOrgEntity::getOrgId, AdminSmOrgEntity::getOrgName));
        customCacheService.hashPut(Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_ORG_NAME, Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_ORG_NAME, collect, 24 * 3600);
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 将部门信息初始化到redis
     *
     * @return 初始化结果
     */
    @PostMapping("/dptredis")
    public JClientRspEntity<String> insertDptToRedis() {
        List<AdminSmDptEntity> list = adminSmDptService.list();
        Map<String, String> collect = list.stream().collect(Collectors.toMap(AdminSmDptEntity::getDptId, AdminSmDptEntity::getDptName));
        customCacheService.hashPut(Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_DPT_NAME, Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_DPT_NAME, collect, 24 * 3600);
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 将系统参数初始化到redis
     *
     * @return 初始化结果
     */
    @PostMapping("/propredis")
    public JClientRspEntity<String> insertPropToRedis() {
        List<AdminSmPropEntity> list = adminSmPropService.list();
        // 使用hash类型存储字典 例如: redisKey -> message  HashKey -> 40 HashValue -> 您好，该时间段不允许登录系统，请您谅解！
        Map<String, String> collect = list.stream().collect(Collectors.toMap(AdminSmPropEntity::getPropName, item -> {
            try {
                return objectMapper.writeValueAsString(item);
            } catch (JsonProcessingException e) {
                log.error("系统参数初始化redis的Json序列化失败！");
                throw new PlatformException("系统参数初始化redis的Json序列化失败,失败原因：" + e.getMessage());
            }
        }));
        customCacheService.hashPut(Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_SYSTEM_PARAMS, Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_SYSTEM_PARAMS, collect, 24 * 3600);
        return JClientRspEntity.buildSuccess("成功");
    }

    /**
     * 将系统提示消息初始化到redis
     *
     * @return 初始化结果
     */
    @PostMapping("/messageredis")
    public JClientRspEntity<String> insertMessageToRedis() {
        List<AdminSmMessageEntity> list = adminSmMessageService.list();
        // 使用hash类型存储字典 例如: redisKey -> message  HashKey -> 40 HashValue -> 您好，该时间段不允许登录系统，请您谅解！
        Map<String, String> collect = list.stream().collect(Collectors.toMap(AdminSmMessageEntity::getCode, AdminSmMessageEntity::getMessage));
        customCacheService.hashPut(Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_SYSTEM_TIP_MESSAGE, Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_SYSTEM_TIP_MESSAGE, collect, 24 * 3600);
        return JClientRspEntity.buildSuccess("成功");
    }

}
