package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.oca.annotation.DictTranslator;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统功能模块表
 *
 * @author wujp4
 * @date 2020-11-26 10:50:57
 */
public class AdminSmFuncModVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    private String modId;
    /**
     * 模块名称
     */
    private String modName;
    /**
     * 是否外部系统
     */
    private String isOuter;
    /**
     * 是否APP功能
     */
    private String isApp;
    /**
     * 最新变更时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;

    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "lastChgName")
    private String lastChgUsr;
    /**
     * 添加模块描述
     */
    private String modDesc;

    public AdminSmFuncModVo() {
    }

    public String getModId() {
        return this.modId;
    }

    public String getModName() {
        return this.modName;
    }

    public String getIsOuter() {
        return this.isOuter;
    }

    public String getIsApp() {
        return this.isApp;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }



    public String getModDesc() {
        return this.modDesc;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public void setIsOuter(String isOuter) {
        this.isOuter = isOuter;
    }

    public void setIsApp(String isApp) {
        this.isApp = isApp;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }


    public void setModDesc(String modDesc) {
        this.modDesc = modDesc;
    }

}
