package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.oca.annotation.DictTranslator;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统业务功能表
 *
 * @author wujp4
 * @date 2020-11-20 13:43:51
 */
public class AdminSmBusiFuncVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    private String funcId;
    /**
     * 所属功能模块编号
     */
    private String modId;
    /**
     * 功能点名称
     */
    private String funcName;

    /**
     * URL链接
     */
    private String funcUrl;

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
    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "lastChgName")
    private String lastChgUsr;
    /**
     * 最新变更时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;

    private String funcDesc;

    private String modName;

    public AdminSmBusiFuncVo() {
    }

    public String getFuncId() {
        return this.funcId;
    }

    public String getModId() {
        return this.modId;
    }

    public String getFuncName() {
        return this.funcName;
    }

    public String getFuncUrl() {
        return this.funcUrl;
    }

    public Integer getFuncOrder() {
        return this.funcOrder;
    }

    public String getFuncIcon() {
        return this.funcIcon;
    }


    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public String getFuncDesc() {
        return this.funcDesc;
    }

    public String getModName() {
        return this.modName;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public void setFuncUrl(String funcUrl) {
        this.funcUrl = funcUrl;
    }

    public void setFuncOrder(Integer funcOrder) {
        this.funcOrder = funcOrder;
    }

    public void setFuncIcon(String funcIcon) {
        this.funcIcon = funcIcon;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public void setFuncDesc(String funcDesc) {
        this.funcDesc = funcDesc;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public String getLastChgUsr() {
        return lastChgUsr;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }
}
