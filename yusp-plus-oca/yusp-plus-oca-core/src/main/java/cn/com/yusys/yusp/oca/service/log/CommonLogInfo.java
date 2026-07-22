package cn.com.yusys.yusp.oca.service.log;

/**
 * 日志信息公共基础类
 *
 * @author lcrack
 * @date 2017/12/7 15:04
 */
public class CommonLogInfo {
    /**
     * 日志信息,可设置为日志实体
     */
    private Object logInfo;

    public Object getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(Object logInfo) {
        this.logInfo = logInfo;
    }

    @Override
    public String toString() {
        return "CommonLogInfo{" +
            "logInfo=" + logInfo +
            '}';
    }
}