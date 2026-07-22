package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.oca.domain.vo.AdminSmContrTreeVo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统功能模块表
 *
 * @author wujp4
 * @date 2020-11-26 10:50:57
 */
@TableName("admin_sm_func_mod")
public class AdminSmFuncModEntity extends AdminSmContrTreeVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId
    private String modId;
    /**
     * 模块名称
     */
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

    public AdminSmFuncModEntity() {
    }

    public String getModId() {
        return this.modId;
    }

    public String getModName() {
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


    public void setModId(String modId) {
        this.modId = modId;
    }

    public void setModName(String modName) {
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


}
