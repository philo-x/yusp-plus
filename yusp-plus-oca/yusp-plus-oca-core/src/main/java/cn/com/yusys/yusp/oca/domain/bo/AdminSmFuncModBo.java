package cn.com.yusys.yusp.oca.domain.bo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统功能模块表
 *
 * @author wujp4
 * @date 2020-11-26 10:50:57
 */
public class AdminSmFuncModBo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    private String modId;
    /**
     * 模块名称
     */
    @NotBlank(message = "modName can not be empty!")
    private String modName;
    /**
     * 模块描述
     */
    private String modDesc;
    /**
     * 是否外部系统
     */
    private String isOuter;
    /**
     * 是否APP功能
     */
    private String isApp;
    /**
     * 外部系统登录名
     */
    private String userName;
    /**
     * 外部系统登录密码
     */
    private String password;
    /**
     * 外部系统用户变量名称
     */
    private String userKey;
    /**
     * 外部系统密码变量名称
     */
    private String pwdKey;
    /**
     * 最新变更用户
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private String lastChgUsr;
    /**
     * 最新变更时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;

    public AdminSmFuncModBo() {
    }

    public String getModId() {
        return this.modId;
    }

    public @NotBlank(message = "modName can not be empty!") String getModName() {
        return this.modName;
    }

    public String getModDesc() {
        return this.modDesc;
    }

    public String getIsOuter() {
        return this.isOuter;
    }

    public String getIsApp() {
        return this.isApp;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUserKey() {
        return this.userKey;
    }

    public String getPwdKey() {
        return this.pwdKey;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public void setModName(@NotBlank(message = "modName can not be empty!") String modName) {
        this.modName = modName;
    }

    public void setModDesc(String modDesc) {
        this.modDesc = modDesc;
    }

    public void setIsOuter(String isOuter) {
        this.isOuter = isOuter;
    }

    public void setIsApp(String isApp) {
        this.isApp = isApp;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public void setPwdKey(String pwdKey) {
        this.pwdKey = pwdKey;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

}
