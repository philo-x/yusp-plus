package cn.com.yusys.yusp.oca.service.log;


/**
 * 日志处理接口
 *
 * @author lupan
 * @date 2018/1/31
 */
public interface ILogProcessor {
    /**
     * 处理传递的日志信息
     * @param logInfo
     */
    void process(CommonLogInfo logInfo);
}