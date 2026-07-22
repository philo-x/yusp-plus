package cn.com.yusys.yusp.oca.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author dlf
 * @version 1.0
 * @since 2025/9/22 14:47
 */
@Configuration
public class ScheduleTaskThreadPoolConfig {


    /**
     * 在升级为 SpringBoot3.4.5 + JDK17 之后，通过 CompletableFuture.runAsync() 调用 quartz 定时任务，会出现 ClassNotFoundException
     * <p>
     * 显示指定线程池，而不是使用 CompletableFuture 默认的 ForkJoinPool.commonPool，来解决这个问题
     *
     * @return 用于异步执行定时任务的线程池
     */
    @Bean
    public Executor scheduleTaskExecutor() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("schedule-task-thread-pool-%d").build();
        return new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), namedThreadFactory);
    }


}
