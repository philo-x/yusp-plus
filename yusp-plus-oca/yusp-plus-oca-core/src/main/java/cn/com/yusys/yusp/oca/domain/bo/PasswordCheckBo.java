package cn.com.yusys.yusp.oca.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;

/**
 * 密码校验参数
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Schema(description = "用户密码验密接收类", name = "用户密码验密接收类")
public class PasswordCheckBo {

    @Schema(name = "passwordType", description = "密码类型", required = false, example = "test")
    private String passwordType;

    @Schema(name = "pwd", description = "用户密码", required = true, example = "test")
    @NotEmpty(message = "密码不能为空")
    private String pwd;

    @Schema(name = "userId", description = "用户名", required = false, example = "test")
    private String userId;

    public PasswordCheckBo() {
    }

    public String getPasswordType() {
        return this.passwordType;
    }

    public @NotEmpty(message = "密码不能为空") String getPwd() {
        return this.pwd;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setPasswordType(String passwordType) {
        this.passwordType = passwordType;
    }

    public void setPwd(@NotEmpty(message = "密码不能为空") String pwd) {
        this.pwd = pwd;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
