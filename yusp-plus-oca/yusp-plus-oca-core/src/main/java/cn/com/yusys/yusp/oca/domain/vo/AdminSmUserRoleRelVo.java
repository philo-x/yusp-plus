package cn.com.yusys.yusp.oca.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author danyu
 */
public class AdminSmUserRoleRelVo {

    private String userRoleRelId;
    /**
     * 用户编号
     */
    private String userId;
    /**
     * 角色编号
     */
    private String roleId;
    /**
     * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
     */
    private String relSts;
    /**
     * 最新变更用户
     */
    private String lastChgName;
    /**
     * 最新变更时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;
    /**
     * 角色代码
     */
    private String roleCode;

    public AdminSmUserRoleRelVo() {
    }

    public String getUserRoleRelId() {
        return this.userRoleRelId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public String getRelSts() {
        return this.relSts;
    }

    public String getLastChgName() {
        return this.lastChgName;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public void setUserRoleRelId(String userRoleRelId) {
        this.userRoleRelId = userRoleRelId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRelSts(String relSts) {
        this.relSts = relSts;
    }

    public void setLastChgName(String lastChgName) {
        this.lastChgName = lastChgName;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }


}
