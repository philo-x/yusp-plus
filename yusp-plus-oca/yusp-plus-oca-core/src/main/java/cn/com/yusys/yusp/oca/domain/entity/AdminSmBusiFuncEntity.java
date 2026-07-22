package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.oca.domain.vo.AdminSmContrTreeVo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统业务功能表
 *
 * @author wujp4
 * @date 2020-11-20 13:43:51
 */
@TableName("admin_sm_busi_func")
public class AdminSmBusiFuncEntity extends AdminSmContrTreeVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId
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
     * 功能点描述
     */
    private String funcDesc;
    /**
     * URL链接
     */
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

    public AdminSmBusiFuncEntity() {
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

    public String getFuncDesc() {
        return this.funcDesc;
    }

    public String getFuncUrl() {
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


    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public void setFuncDesc(String funcDesc) {
        this.funcDesc = funcDesc;
    }

    public void setFuncUrl(String funcUrl) {
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



}
