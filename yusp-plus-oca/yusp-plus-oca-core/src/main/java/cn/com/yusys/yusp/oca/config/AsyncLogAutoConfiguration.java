package cn.com.yusys.yusp.oca.config;

import cn.com.yusys.yusp.oca.service.log.AbstractLogProcessor;
import cn.com.yusys.yusp.oca.service.log.BusinessLogProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 异步日志配置.
 * <p>
 * 开启线程池,用于日志记录;
 *
 * @author danyu
 * @date 2021/6/28 11:27:00
 */
@Configuration
@EnableConfigurationProperties(AsyncLogProperties.class)
public class AsyncLogAutoConfiguration implements DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(AsyncLogAutoConfiguration.class);

    private final AsyncLogProperties asyncLogProperties;

    private ThreadPoolExecutor threadPoolExecutor;

    public AsyncLogAutoConfiguration(AsyncLogProperties asyncLogProperties) {
        this.asyncLogProperties = asyncLogProperties;
    }

    @Bean
    @ConditionalOnMissingBean(name = "asyncLogThreadPoolExecutor")
    public ThreadPoolExecutor asyncLogThreadPoolExecutor() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(asyncLogProperties.getQueueSize());

        threadPoolExecutor = new ThreadPoolExecutor(asyncLogProperties.getCorePoolSize(), asyncLogProperties.getMaxPoolSize(), 5,
                TimeUnit.SECONDS, workQueue, Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        return threadPoolExecutor;
    }

    public AbstractLogProcessor defaultAsyncLogProcessor() {
        return new BusinessLogProcessor();
    }

    @Bean
    public AbstractLogProcessor abstractLogProcessor() {
        return defaultAsyncLogProcessor();
    }

    /**
     * @see DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
        logger.info("开始销毁异步线程池");
        if (threadPoolExecutor != null) {
            try {
                threadPoolExecutor.shutdown();
                // 观察30秒是否成功关闭线程池，否则调用shutdownNow再次关闭;
                if (!threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                    threadPoolExecutor.shutdownNow();
                }
            } catch (Exception ex) {
                logger.error("异步线程池关闭异常", ex);
                throw new Exception(ex);
            }
        }
        logger.info("成功销毁异步线程池");
    }

}