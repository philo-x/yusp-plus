package cn.com.yusys.yusp.oca.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 异步日志属性
 *
 * @author lupan
 * @date 2018/1/31
 */
@ConfigurationProperties(prefix = "application.log.async")
public class AsyncLogProperties {
    /**
     * 核心线程数
     */
    private int corePoolSize = 20;

    /**
     * 最大线程数
     */
    private int maxPoolSize = 50;

    /**
     * 阻塞队列大小
     */
    private int queueSize = 100;


    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

}