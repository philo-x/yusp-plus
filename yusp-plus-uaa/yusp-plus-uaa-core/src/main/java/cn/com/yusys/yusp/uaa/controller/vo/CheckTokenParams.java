package cn.com.yusys.yusp.uaa.controller.vo;

/**
 * @author yinjun
 * @version 1.2
 * @description: token验证参数
 * @date 2022/3/11
 **/
public class CheckTokenParams {

    /**
     * 渠道来源
     */
    private String clientId;
    /**
     * token
     */
    private String token;
    /**
     * 请求路径
     */
    private String requestPath;
    /**
     * 请求方法
     */
    private String httpMethod;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Override
    public String toString() {
        return "CheckTokenParams{" +
                "clientId='" + clientId + '\'' +
                ", token='" + token + '\'' +
                ", requestPath='" + requestPath + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                '}';
    }
}
