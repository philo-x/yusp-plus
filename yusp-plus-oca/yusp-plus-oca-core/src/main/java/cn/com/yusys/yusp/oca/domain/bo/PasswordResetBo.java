package cn.com.yusys.yusp.oca.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;

/**
 * 密码重置参数
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Schema(description = "用户密码修改接收类", name = "用户密码修改接收类")
public class PasswordResetBo {

    @Schema(name = "loginCode", description = "用户名", required = true, example = "test")
    @NotEmpty(message = "登录名不能为空")
    private String loginCode;

    public @NotEmpty(message = "登录名不能为空") String getLoginCode() {
        return this.loginCode;
    }

    public void setLoginCode(@NotEmpty(message = "登录名不能为空") String loginCode) {
        this.loginCode = loginCode;
    }

}
