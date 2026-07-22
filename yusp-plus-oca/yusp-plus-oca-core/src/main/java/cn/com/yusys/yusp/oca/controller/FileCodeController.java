package cn.com.yusys.yusp.oca.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @version: 3.1.1-SNAPSHOT
 * @description: 为外部服务提供票据及验证
 * @author lty
 * @date 2021/1/6　　
 */
@RestController
@RequestMapping("api/filecode")
public class FileCodeController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final String FILE_CODE = "FILE_CODE_REDIS_KEY";

    private static final Random RAND = new Random();


    @PostMapping("/codegenerate")
    public JClientRspEntity<Object> codeGenerate(){
        String random = UUID.randomUUID().toString().replaceAll("-", "") + RAND.nextLong();
        String userId = SessionUtils.getUserId();
        // 将生成的图形验证码存储到 redis 缓存中，默认超时时间是1分钟
        int fileCodeTime = 300;
        // 向redis里存入数据和设置缓存时间
        stringRedisTemplate.opsForValue().set(FILE_CODE + "-" + random,
                userId, fileCodeTime, TimeUnit.SECONDS);
        return JClientRspEntity.buildSuccess(random);
    }

    @PostMapping("/codeverify/{random}")
    public String codeverify(@PathVariable String random){

        String userId = stringRedisTemplate.opsForValue().get(FILE_CODE + "-" + random);
        if(StringUtils.isEmpty(userId)){
            return null;
        }else {
            return userId;
        }
    }
}
