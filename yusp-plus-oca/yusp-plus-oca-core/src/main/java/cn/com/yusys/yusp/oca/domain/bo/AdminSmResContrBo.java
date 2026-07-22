package cn.com.yusys.yusp.oca.domain.bo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统功能控制点表
 *
 * @author wujp4
 * @email wujp4@yusys.com.cn
 * @date 2020-11-19 16:38:48
 */
public class AdminSmResContrBo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    private String contrId;
    /**
     * 所属业务功能编号
     */
    private String funcId;
    /**
     * 控制操作代码
     */
    private String contrCode;
    /**
     * 控制操作名称
     */
    private String contrName;
    /**
     * 控制操作URL(用于后台校验时使用)
     */
    private String contrUrl;
    /**
     * 备注
     */
    private String contrRemark;
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
    /**
     * 请求类型
     */
    private String methodType;

    public AdminSmResContrBo() {
    }

    public String getContrId() {
        return this.contrId;
    }

    public String getFuncId() {
        return this.funcId;
    }

    public String getContrCode() {
        return this.contrCode;
    }

    public String getContrName() {
        return this.contrName;
    }

    public String getContrUrl() {
        return this.contrUrl;
    }

    public String getContrRemark() {
        return this.contrRemark;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public String getMethodType() {
        return this.methodType;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public void setContrCode(String contrCode) {
        this.contrCode = contrCode;
    }

    public void setContrName(String contrName) {
        this.contrName = contrName;
    }

    public void setContrUrl(String contrUrl) {
        this.contrUrl = contrUrl;
    }

    public void setContrRemark(String contrRemark) {
        this.contrRemark = contrRemark;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }
}
