package cn.com.yusys.yusp.uaa.pojo;


import java.io.Serializable;

/**
 * @author lty
 * @description 与oca交互返回信息
 * @date 2020/12/29
 */
public class LoginOcaResultDto<T> implements Serializable {

    /**
     * 业务错误码 -认证通过不传或为空
     */
    private String code;

    /**
     * 认证失败时的提示信息
     */
    private String message;

    /**
     * 用户数据
     */
    private T data;

    public LoginOcaResultDto() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginJClientRspEntity{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}


