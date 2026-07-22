package cn.com.yusys.yusp.oca.service.log;

import cn.com.yusys.yusp.oca.domain.dto.AdminSmLogDto;
import cn.com.yusys.yusp.oca.service.AdminSmLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 提供构造器接受参数
 *
 * @author lupan
 * @date 2018/1/31
 */
public class BusinessLogProcessor extends AbstractLogProcessor {

    private static final Logger logger = LoggerFactory.getLogger(BusinessLogProcessor.class);

    @Autowired
    AdminSmLogService adminSmLogService;

    @Override
    public void process(CommonLogInfo logInfo) {
        logger.debug("线程:{}开始处理日志", Thread.currentThread());
        AdminSmLogDto logDto = (AdminSmLogDto) logInfo.getLogInfo();
        adminSmLogService.addLog(logDto);
        logger.debug("线程:{}结束日志处理", Thread.currentThread());
    }
}