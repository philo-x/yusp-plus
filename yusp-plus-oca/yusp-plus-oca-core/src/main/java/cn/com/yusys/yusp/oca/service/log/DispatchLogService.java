package cn.com.yusys.yusp.oca.service.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 日志分发服务
 *
 * @author lupan
 * @date 2018/1/31 15:04
 */
@Service
public class DispatchLogService {
    
    @Autowired
    @Qualifier("asyncLogThreadPoolExecutor")
    private ThreadPoolExecutor executor;
    
    @Autowired
    private AbstractLogProcessor abstractLogProcessor;

    /**
     * 核心日志处理方法
     * @param logInfo 日志信息公共基础类
     */
    public void handleLog(CommonLogInfo logInfo) {
        abstractLogProcessor.setCommonLogInfo(logInfo);
        executor.execute(abstractLogProcessor);
    }
}