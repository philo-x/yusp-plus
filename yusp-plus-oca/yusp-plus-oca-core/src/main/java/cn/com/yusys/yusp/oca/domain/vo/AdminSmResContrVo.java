package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.oca.annotation.DictTranslator;
import cn.com.yusys.yusp.oca.domain.constants.Constants;

import java.io.Serializable;
/**
 * 系统功能控制点表
 *
 * @author zhangyt12
 * @date 2020-11-27 14:57:08
 */
public class AdminSmResContrVo implements Serializable {
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
     * 请求类型
     */
    private String methodType;
    /**
     * 菜单id
     */
    private String menuId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 最新变更用户
     */
    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "lastChgName")
    private String lastChgUsr;
    /**
     * 备注
     */
    private String contrRemark;

    /**
     * 最后修改人名称
     */
    private String lastChgDt;

    /**
     * 强制加密
     */
    private String encodeCheck;
    /**
     * 强制防重
     */
    private String nonceCheck;

    public AdminSmResContrVo() {
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

    public String getMethodType() {
        return this.methodType;
    }

    public String getMenuId() {
        return this.menuId;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public String getContrRemark() {
        return this.contrRemark;
    }

    public String getLastChgDt() {
        return this.lastChgDt;
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

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setContrRemark(String contrRemark) {
        this.contrRemark = contrRemark;
    }

    public void setLastChgDt(String lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public String getEncodeCheck() {
        return encodeCheck;
    }

    public void setEncodeCheck(String encodeCheck) {
        this.encodeCheck = encodeCheck;
    }

    public String getNonceCheck() {
        return nonceCheck;
    }

    public void setNonceCheck(String nonceCheck) {
        this.nonceCheck = nonceCheck;
    }
}
