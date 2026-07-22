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
 * 系统用户表拓展实体
 *
 * @author danyu
 * @date 2020-12-11 15:31:54
 */
public class AdminSmUserVo {

    /**
     * 记录编号
     */
    private String userId;
    /**
     * 账号
     */
    private String loginCode;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 员工号
     */
    private String userCode;
    /**
     * 性别
     */
    private String userSex;
    /**
     * 证件类型
     */
    private String certType;
    /**
     * 证件号码
     */
    private String certNo;

    /**
     * 所属机构
     */
    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_ORG_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "orgName" )
    private String orgId;
    /**
     * 所属部门
     */
    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_DPT_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "dptName" )
    private String dptId;

    /**
     * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
     */
    private AvailableStateEnum userSts;
    /**
     * 用户头像地址
     */
    private String userAvatar;
    /**
     * 生日
     */
    private String userBirthday;
    /**
     * 邮箱
     */
    private String userEmail;
    /**
     * 移动电话
     */
    private String userMobilephone;
    /**
     * 办公电话
     */
    private String userOfficetel;

    /**
     * 最近登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    private Date lastLoginTime;

    /**
     * 最新变更用户
     */
    @DictTranslator(cacheName = Constants.SystemUserConstance.TRANSLATE_REDIS_KEY_USER_NAME, key = Constants.CacheConstance.DICT_TRANSLATOR, fieldName = "lastChgName" )
    private String lastChgUsr;

    /**
     * 最新变更时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;
    /**
     * 有效期到
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    private Date deadline;

    public AdminSmUserVo() {
    }

    public String getUserId() {
        return this.userId;
    }

    public String getLoginCode() {
        return this.loginCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public String getUserSex() {
        return this.userSex;
    }

    public String getCertType() {
        return this.certType;
    }

    public String getCertNo() {
        return this.certNo;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getDptId() {
        return this.dptId;
    }

    public AvailableStateEnum getUserSts() {
        return this.userSts;
    }

    public String getUserAvatar() {
        return this.userAvatar;
    }

    public String getUserBirthday() {
        return this.userBirthday;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getUserMobilephone() {
        return this.userMobilephone;
    }

    public String getUserOfficetel() {
        return this.userOfficetel;
    }

    public Date getLastLoginTime() {
        return this.lastLoginTime;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public Date getDeadline() {
        return this.deadline;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setDptId(String dptId) {
        this.dptId = dptId;
    }

    public void setUserSts(AvailableStateEnum userSts) {
        this.userSts = userSts;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserMobilephone(String userMobilephone) {
        this.userMobilephone = userMobilephone;
    }

    public void setUserOfficetel(String userOfficetel) {
        this.userOfficetel = userOfficetel;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

}
