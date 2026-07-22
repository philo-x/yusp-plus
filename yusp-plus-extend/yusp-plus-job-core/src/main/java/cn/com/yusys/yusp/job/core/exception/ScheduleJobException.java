package cn.com.yusys.yusp.job.core.exception;

import java.io.Serial;

/**
 * @author lty
 * @description 自定义异常
 * @date 2021/2/25
 */
public class ScheduleJobException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public ScheduleJobException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public ScheduleJobException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
