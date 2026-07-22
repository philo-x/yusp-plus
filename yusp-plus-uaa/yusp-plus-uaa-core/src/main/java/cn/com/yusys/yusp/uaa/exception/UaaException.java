package cn.com.yusys.yusp.uaa.exception;


/**
 * @author lty
 * @description UAA异常
 * @date 2021/6/24
 */

public class UaaException extends Exception {

    private String code;

    public UaaException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
