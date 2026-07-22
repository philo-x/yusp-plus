package cn.com.yusys.yusp.oca.job;

import cn.com.yusys.yusp.commons.exception.PlatformException;
import cn.com.yusys.yusp.job.core.task.ITask;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmPropEntity;
import cn.com.yusys.yusp.oca.service.AdminSmPropService;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yusys
 */
@Component("propLoadTask")
public class PropLoadTask implements ITask {

    @Autowired
    private AdminSmPropService adminSmPropService;
    @Autowired
    private CustomCacheService customCacheService;
    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public void run(String params) {
        String name=Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_SYSTEM_PARAMS;
        String redisKey = Constants.CacheConstance.DICT_TRANSLATOR;
        //删除系统参数信息
        customCacheService.clear(name, redisKey);
        //初始化系统参数信息
        List<AdminSmPropEntity> propList = adminSmPropService.list();
        // 使用hash类型存储字典 例如: redisKey -> message  HashKey -> 40 HashValue -> 您好，该时间段不允许登录系统，请您谅解！
        Map<String, String> collect = propList.stream().collect(Collectors.toMap(AdminSmPropEntity::getPropName, item -> {
            try {
                return objectMapper.writeValueAsString(item);
            } catch (JsonProcessingException e) {
                throw new PlatformException("系统参数初始化redis的Json序列化失败,失败原因：" + e.getMessage());
            }
        }));
        customCacheService.hashPut(name, redisKey, collect, 24 * 3600);
    }
}
