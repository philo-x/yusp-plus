package cn.com.yusys.yusp.oca.service.component;

import cn.com.yusys.yusp.commons.session.SessionService;
import cn.com.yusys.yusp.commons.util.StringUtils;
import cn.com.yusys.yusp.oca.service.cache.CustomCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化数据
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Component
public class LoadRedis implements CommandLineRunner {

    @Autowired
    private SessionService ocaSessionService;
    @Autowired
    private CustomCacheService customCacheService;

    @Override
    public void run(String... args) throws Exception {

        //该锁10分钟后释放
        boolean lock = this.lock("load", 600);
        if (!lock) {
            return;
        }
        //初始化控制点信息
        ocaSessionService.getAllControls();
    }


    /**
     * 加锁，该锁只用于初始化oca初始化redis使用
     *
     * @param key        缓存key
     * @param expireTime 过期时间
     * @return 锁结果
     */
    private boolean lock(String key, int expireTime) {
        String lock = customCacheService.stringGet("lock", key);
        if (StringUtils.isEmpty(lock)) {
            customCacheService.stringPut("lock", key, "locked", expireTime);
            return true;
        }
        return false;
    }
}
