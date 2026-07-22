package cn.com.yusys.yusp.uaa.pojo;

/**
 * @author lty
 * @description 与oca交互所需请求参数信息
 * @date 2020/12/29
 */
public class LoginOcaRequestDto {

    /**
     * 登录码
     */
    private String loginCode;

    /**
     * 密码
     */
    private String password;

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
