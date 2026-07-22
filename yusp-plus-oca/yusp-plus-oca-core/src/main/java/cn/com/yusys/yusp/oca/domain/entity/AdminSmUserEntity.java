package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.validation.Insert;
import cn.com.yusys.yusp.oca.validation.Update;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户表
 *
 * @author wujp4
 * @date 2020-11-16 15:31:54
 */
@TableName("admin_sm_user")
public class AdminSmUserEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @NotEmpty(groups = Update.class, message = "userId can not be null or empty!!")
    private String userId;
    /**
     * 账号
     */
    @NotEmpty(groups = Insert.class, message = "loginCode can not be null or empty!!")
    private String loginCode;
    /**
     * 姓名
     */
    @NotEmpty(groups = Insert.class, message = "userName can not be null or empty!!")
    private String userName;
    /**
     * 员工号
     */
    @NotEmpty(groups = Insert.class, message = "userCode can not be null or empty!!")
    private String userCode;
    /**
     * 证件类型
     */
    private String certType;
    /**
     * 证件号码
     */
    private String certNo;

    /**
     * 有效期到
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Date deadline;
    /**
     * 所属机构编号
     */
    @NotEmpty(groups = Insert.class, message = "userCode can not be null or empty!!")
    private String orgId;
    /**
     * 所属部门编号
     */

    private String dptId;
    /**
     * 密码
     */
    @JsonIgnore
    private String userPassword;
    /**
     * 性别
     */
    private String userSex;
    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date userBirthday;
    /**
     * 邮箱
     */
    @Email
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
     * 学历
     */
    private String userEducation;
    /**
     * 资格证书
     */
    private String userCertificate;
    /**
     * 入职日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Date entrantsDate;
    /**
     * 任职时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Date positionTime;
    /**
     * 从业时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Date financialJobTime;
    /**
     * 职级
     */
    private String positionDegree;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 常用IP，逗号分隔
     */
    @JsonIgnore
    private String offenIp;
    /**
     * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效 F：冻结
     */
    private AvailableStateEnum userSts;
    /**
     * 最近登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;
    /**
     * 最近一次修改密码时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastEditPassTime;

    /**
     * 指纹信息
     */
    private String fingerPrint;
    /**
     * 声纹信息
     */
    private String voicePrint;
    /**
     * 面部信息
     */
    private String facePrint;
    /**
     * 手势密码
     */
    private String gesturePassword;


    public AdminSmUserEntity() {
    }

    public @NotEmpty(groups = Update.class, message = "userId can not be null or empty!!") String getUserId() {
        return this.userId;
    }

    public @NotEmpty(groups = Insert.class, message = "loginCode can not be null or empty!!") String getLoginCode() {
        return this.loginCode;
    }

    public @NotEmpty(groups = Insert.class, message = "userName can not be null or empty!!") String getUserName() {
        return this.userName;
    }

    public @NotEmpty(groups = Insert.class, message = "userCode can not be null or empty!!") String getUserCode() {
        return this.userCode;
    }

    public String getCertType() {
        return this.certType;
    }

    public String getCertNo() {
        return this.certNo;
    }

    public Date getDeadline() {
        return this.deadline;
    }

    public @NotEmpty(groups = Insert.class, message = "userCode can not be null or empty!!") String getOrgId() {
        return this.orgId;
    }

    public String getDptId() {
        return this.dptId;
    }

    public String getUserPassword() {
        return this.userPassword;
    }

    public String getUserSex() {
        return this.userSex;
    }

    public Date getUserBirthday() {
        return this.userBirthday;
    }

    public @Email String getUserEmail() {
        return this.userEmail;
    }

    public String getUserMobilephone() {
        return this.userMobilephone;
    }

    public String getUserOfficetel() {
        return this.userOfficetel;
    }

    public String getUserEducation() {
        return this.userEducation;
    }

    public String getUserCertificate() {
        return this.userCertificate;
    }

    public Date getEntrantsDate() {
        return this.entrantsDate;
    }

    public Date getPositionTime() {
        return this.positionTime;
    }

    public Date getFinancialJobTime() {
        return this.financialJobTime;
    }

    public String getPositionDegree() {
        return this.positionDegree;
    }

    public String getUserAvatar() {
        return this.userAvatar;
    }

    public String getOffenIp() {
        return this.offenIp;
    }

    public AvailableStateEnum getUserSts() {
        return this.userSts;
    }

    public Date getLastLoginTime() {
        return this.lastLoginTime;
    }

    public Date getLastEditPassTime() {
        return this.lastEditPassTime;
    }


    public String getFingerPrint() {
        return this.fingerPrint;
    }

    public String getVoicePrint() {
        return this.voicePrint;
    }

    public String getFacePrint() {
        return this.facePrint;
    }

    public String getGesturePassword() {
        return this.gesturePassword;
    }

    public void setUserId(@NotEmpty(groups = Update.class, message = "userId can not be null or empty!!") String userId) {
        this.userId = userId;
    }

    public void setLoginCode(@NotEmpty(groups = Insert.class, message = "loginCode can not be null or empty!!") String loginCode) {
        this.loginCode = loginCode;
    }

    public void setUserName(@NotEmpty(groups = Insert.class, message = "userName can not be null or empty!!") String userName) {
        this.userName = userName;
    }

    public void setUserCode(@NotEmpty(groups = Insert.class, message = "userCode can not be null or empty!!") String userCode) {
        this.userCode = userCode;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setOrgId(@NotEmpty(groups = Insert.class, message = "userCode can not be null or empty!!") String orgId) {
        this.orgId = orgId;
    }

    public void setDptId(String dptId) {
        this.dptId = dptId;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public void setUserBirthday(Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    public void setUserEmail(@Email String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserMobilephone(String userMobilephone) {
        this.userMobilephone = userMobilephone;
    }

    public void setUserOfficetel(String userOfficetel) {
        this.userOfficetel = userOfficetel;
    }

    public void setUserEducation(String userEducation) {
        this.userEducation = userEducation;
    }

    public void setUserCertificate(String userCertificate) {
        this.userCertificate = userCertificate;
    }

    public void setEntrantsDate(Date entrantsDate) {
        this.entrantsDate = entrantsDate;
    }

    public void setPositionTime(Date positionTime) {
        this.positionTime = positionTime;
    }

    public void setFinancialJobTime(Date financialJobTime) {
        this.financialJobTime = financialJobTime;
    }

    public void setPositionDegree(String positionDegree) {
        this.positionDegree = positionDegree;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public void setOffenIp(String offenIp) {
        this.offenIp = offenIp;
    }

    public void setUserSts(AvailableStateEnum userSts) {
        this.userSts = userSts;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setLastEditPassTime(Date lastEditPassTime) {
        this.lastEditPassTime = lastEditPassTime;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public void setVoicePrint(String voicePrint) {
        this.voicePrint = voicePrint;
    }

    public void setFacePrint(String facePrint) {
        this.facePrint = facePrint;
    }

    public void setGesturePassword(String gesturePassword) {
        this.gesturePassword = gesturePassword;
    }

}
