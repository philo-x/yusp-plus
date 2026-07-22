package cn.com.yusys.yusp.oca.service.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 抽象日志处理类
 * @author lupan
 * @date 2018/1/31
 */
public abstract class AbstractLogProcessor implements ILogProcessor, Runnable {

    /**
     * 必备属性
     */
    private CommonLogInfo commonLogInfo;

    private static final Logger logger = LoggerFactory.getLogger(AbstractLogProcessor.class);

    public CommonLogInfo getCommonLogInfo() {
        return commonLogInfo;
    }

    public void setCommonLogInfo(CommonLogInfo commonLogInfo) {
        this.commonLogInfo = commonLogInfo;
    }

    @Override
    public void run() {
        logger.debug("线程:{}开始处理日志", Thread.currentThread());
        process(commonLogInfo);
        logger.debug("线程:{}结束日志处理", Thread.currentThread());
    }
}