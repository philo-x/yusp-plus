package cn.com.yusys.yusp.oca.domain.bo;

import cn.com.yusys.yusp.oca.annotation.PasswordStrategy;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;

/**
 * 密码修改参数
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
@Schema(description = "用户密码修改接收类", name = "用户密码修改接收类")
public class PasswordModifyBo {

    @Schema(name = "sysId", description = "客户端Id", required = false, example = "test")
    private String sysId;

    @Schema(name = "rawPassword", description = "原密码", required = true, example = "test")
    @NotEmpty
    private String rawPassword;

    @Schema(name = "seq", description = "随机数", required = false, example = "123456")
    private String seq;

    @Schema(name = "password", description = "修改后密码", required = true, example = "test")
    @NotEmpty(message = "密码不能为空")
    @PasswordStrategy
    private String password;

    @Schema(name = "loginCode", description = "用户名", required = false, example = "test")
    private String loginCode;

    @Schema(name = "lastChgUsr", description = "最后修改者", required = false, example = "test")
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private String lastChgUsr;


    public String getSysId() {
        return this.sysId;
    }

    public @NotEmpty String getRawPassword() {
        return this.rawPassword;
    }

    public @NotEmpty(message = "密码不能为空") String getPassword() {
        return this.password;
    }

    public String getLoginCode() {
        return this.loginCode;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setRawPassword(@NotEmpty String rawPassword) {
        this.rawPassword = rawPassword;
    }

    public void setPassword(@NotEmpty(message = "密码不能为空") String password) {
        this.password = password;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }
}
