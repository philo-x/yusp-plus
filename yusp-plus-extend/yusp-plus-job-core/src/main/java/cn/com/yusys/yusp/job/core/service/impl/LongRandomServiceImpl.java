package cn.com.yusys.yusp.job.core.service.impl;

import cn.com.yusys.yusp.job.core.service.LongRandomService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @description 随机数生成
 * @Author: lty
 * @Date: 2021/6/28
 */
@Service
public class LongRandomServiceImpl implements LongRandomService {

    @Override
    public Long getLongRandom() {
        long now = System.currentTimeMillis();
        long l = (long) ((ThreadLocalRandom.current().nextDouble() * 9 + 1) * 100);
        String s = now + String.valueOf(l);
        return Long.valueOf(s);
    }
}
