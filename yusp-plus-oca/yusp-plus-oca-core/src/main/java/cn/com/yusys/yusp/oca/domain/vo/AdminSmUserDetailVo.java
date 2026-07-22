package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.oca.annotation.JsonEncrypt;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;


/**
 * 用户详情实体
 * @author zhanyq
 * @date 2021-06-30 17:37
 */
public class AdminSmUserDetailVo {

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
     * 证件类型
     */
    private String certType;
    /**
     * 证件号码
     */

    @JsonEncrypt(beginIdx = 4, endIdx = 13)
    private String certNo;
    /**
     * 员工号
     */
    private String userCode;
    /**
     * 有效期到
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deadline;
    /**
     * 所属机构编号
     */
    private String orgId;

    private String orgName;
    /**
     * 所属部门编号
     */
    private String dptId;

    private String dptName;

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
    @JsonEncrypt(splitChar = "@")
    private String userEmail;
    /**
     * 移动电话
     */
    @JsonEncrypt(beginIdx = 3, endIdx = 99)
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
    private String entrantsDate;
    /**
     * 任职时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String positionTime;
    /**
     * 从业时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String financialJobTime;
    /**
     * 职级
     */
    private String positionDegree;
    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
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
     * 最新变更用户
     */
    private String lastChgUsr;
    /**
     * 最新变更时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;

    public AdminSmUserDetailVo() {
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

    public String getCertType() {
        return this.certType;
    }

    public String getCertNo() {
        return this.certNo;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public Date getDeadline() {
        return this.deadline;
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public String getDptId() {
        return this.dptId;
    }

    public String getDptName() {
        return this.dptName;
    }

    public String getUserSex() {
        return this.userSex;
    }

    public Date getUserBirthday() {
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

    public String getUserEducation() {
        return this.userEducation;
    }

    public String getUserCertificate() {
        return this.userCertificate;
    }

    public String getEntrantsDate() {
        return this.entrantsDate;
    }

    public String getPositionTime() {
        return this.positionTime;
    }

    public String getFinancialJobTime() {
        return this.financialJobTime;
    }

    public String getPositionDegree() {
        return this.positionDegree;
    }

    public String getUserAvatar() {
        return this.userAvatar;
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

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
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

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setDptId(String dptId) {
        this.dptId = dptId;
    }

    public void setDptName(String dptName) {
        this.dptName = dptName;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public void setUserBirthday(Date userBirthday) {
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

    public void setUserEducation(String userEducation) {
        this.userEducation = userEducation;
    }

    public void setUserCertificate(String userCertificate) {
        this.userCertificate = userCertificate;
    }

    public void setEntrantsDate(String entrantsDate) {
        this.entrantsDate = entrantsDate;
    }

    public void setPositionTime(String positionTime) {
        this.positionTime = positionTime;
    }

    public void setFinancialJobTime(String financialJobTime) {
        this.financialJobTime = financialJobTime;
    }

    public void setPositionDegree(String positionDegree) {
        this.positionDegree = positionDegree;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
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

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

}
