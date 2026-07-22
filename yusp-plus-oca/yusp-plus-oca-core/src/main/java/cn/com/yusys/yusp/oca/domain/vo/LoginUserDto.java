package cn.com.yusys.yusp.oca.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;

/**
 * 登录时用户传输Vo
 *
 * @author: wujp4
 * @create: 2020-11-23 15:06
 */
@Schema(description = "用户登录实体类", name = "用户登录实体类")
public class LoginUserDto {

    @Schema(name = "loginCode", description = "用户登录名", required = true, example = "test")
    @NotEmpty(message = "{loginCode}")
    private String loginCode;

    @Schema(name = "password", description = "用户密码", required = true, example = "123456")
    @NotEmpty(message = "{password}")
    private String password;

    @Schema(name = "num", description = "随机数", required = true, example = "123456")
    private String num;
    public @NotEmpty(message = "{loginCode}") String getLoginCode() {
        return this.loginCode;
    }

    public @NotEmpty(message = "{password}") String getPassword() {
        return this.password;
    }

    public void setLoginCode(@NotEmpty(message = "{loginCode}") String loginCode) {
        this.loginCode = loginCode;
    }

    public void setPassword(@NotEmpty(message = "{password}") String password) {
        this.password = password;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
