package cn.com.yusys.yusp.single;

import cn.com.yusys.yusp.common.annotation.EnableMetrics;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @author lty
 * @date 2021/6/24
 */
@EnableMetrics
@SpringBootApplication(exclude = {RabbitAutoConfiguration.class}, scanBasePackages = {"cn.com.yusys.yusp.single", "cn.com.yusys.yusp.uaa", "cn.com.yusys.yusp.oca", "cn.com.yusys.yusp.notice", "cn.com.yusys.yusp.common.exception", "cn.com.yusys.yusp.commons", "cn.com.yusys.yusp.file", "cn.com.yusys.yusp.commons.req.crypto.replay", "cn.com.yusys.yusp.common.config",  "cn.com.yusys.yusp.job.core", "cn.com.yusys.yusp.utrace", "cn.com.yusys.yusp.workboard"})
@MapperScan(basePackages = {"cn.com.yusys.yusp.workboard.dao", "cn.com.yusys.yusp.job.core.dao", "cn.com.yusys.yusp.oca.dao", "cn.com.yusys.yusp.notice.dao", "cn.com.yusys.yusp.flow.repository.mapper", "cn.com.yusys.yusp.file.repository.mapper"})
@EnableAsync(proxyTargetClass = true)
public class YuspPlusSingleStarterMicroserviceApp {
    public static void main(String[] args) {
        SpringApplication.run(YuspPlusSingleStarterMicroserviceApp.class, args);
    }

}
