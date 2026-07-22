package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.oca.annotation.DictTranslator;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.constants.Constants;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author danyu
 */
public class AdminSmRoleVo {

    private String roleId;
    /**
     * 角色代码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 所属机构编号
     */
    private String orgId;

    private String orgName;

    /**
     * 角色层级
     */
    private Integer roleLevel;
    /**
     * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
     */
    private AvailableStateEnum roleSts;
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

    private String orgSeq;


    public AdminSmRoleVo() {
    }

    public String getRoleId() {
        return this.roleId;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public Integer getRoleLevel() {
        return this.roleLevel;
    }

    public AvailableStateEnum getRoleSts() {
        return this.roleSts;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public void setRoleSts(AvailableStateEnum roleSts) {
        this.roleSts = roleSts;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public String getOrgSeq() {
        return orgSeq;
    }

    public void setOrgSeq(String orgSeq) {
        this.orgSeq = orgSeq;
    }
}
