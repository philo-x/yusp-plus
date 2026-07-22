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
 * 系统业务功能表
 *
 * @author wujp4
 * @date 2020-11-20 13:43:51
 */
public class AdminSmBusiFuncBo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    private String funcId;
    /**
     * 所属功能模块编号
     */
    @NotBlank(message = "modId can not be empty!")
    private String modId;
    /**
     * 功能点名称
     */
    @NotBlank(message = "funcName can not be empty!")
    private String funcName;
    /**
     * 功能点描述
     */
    private String funcDesc;
    /**
     * URL链接
     */
    @NotBlank(message = "funcUrl can not be empty!")
    private String funcUrl;
    /**
     * JS链接
     */
    private String funcUrlJs;
    /**
     * CSS链接
     */
    private String funcUrlCss;
    /**
     * 顺序
     */
    private Integer funcOrder;
    /**
     * 图标
     */
    private String funcIcon;
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

    public AdminSmBusiFuncBo() {
    }

    public String getFuncId() {
        return this.funcId;
    }

    public @NotBlank(message = "modId can not be empty!") String getModId() {
        return this.modId;
    }

    public @NotBlank(message = "funcName can not be empty!") String getFuncName() {
        return this.funcName;
    }

    public String getFuncDesc() {
        return this.funcDesc;
    }

    public @NotBlank(message = "funcUrl can not be empty!") String getFuncUrl() {
        return this.funcUrl;
    }

    public String getFuncUrlJs() {
        return this.funcUrlJs;
    }

    public String getFuncUrlCss() {
        return this.funcUrlCss;
    }

    public Integer getFuncOrder() {
        return this.funcOrder;
    }

    public String getFuncIcon() {
        return this.funcIcon;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public void setModId(@NotBlank(message = "modId can not be empty!") String modId) {
        this.modId = modId;
    }

    public void setFuncName(@NotBlank(message = "funcName can not be empty!") String funcName) {
        this.funcName = funcName;
    }

    public void setFuncDesc(String funcDesc) {
        this.funcDesc = funcDesc;
    }

    public void setFuncUrl(@NotBlank(message = "funcUrl can not be empty!") String funcUrl) {
        this.funcUrl = funcUrl;
    }

    public void setFuncUrlJs(String funcUrlJs) {
        this.funcUrlJs = funcUrlJs;
    }

    public void setFuncUrlCss(String funcUrlCss) {
        this.funcUrlCss = funcUrlCss;
    }

    public void setFuncOrder(Integer funcOrder) {
        this.funcOrder = funcOrder;
    }

    public void setFuncIcon(String funcIcon) {
        this.funcIcon = funcIcon;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

}
