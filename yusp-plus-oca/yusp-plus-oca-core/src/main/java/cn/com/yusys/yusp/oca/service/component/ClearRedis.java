package cn.com.yusys.yusp.oca.service.component;

import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @program: yusp-plus
 * @description: 清除redis
 * @author: wujiangpeng
 * @email: wujp4@yusys.com.cn
 * @create: 2021-02-20 16:43
 */
@Component
public class ClearRedis implements ApplicationRunner {
    @Autowired
    private CustomCacheService customCacheService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String lock = customCacheService.stringGet("lock", "load");
        if (!StringUtils.isEmpty(lock)) {
            return;
        }

        //删除控制点权限
        customCacheService.clear(Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_CONTROL_NAME, Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_CONTROL_KEY);
    }
}
