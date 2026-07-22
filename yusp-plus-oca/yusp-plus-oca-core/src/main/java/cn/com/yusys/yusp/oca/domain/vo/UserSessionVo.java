package cn.com.yusys.yusp.oca.domain.vo;

import java.util.Date;
import java.util.List;

/**
 * session信息
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class UserSessionVo {

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
     * 邮箱
     */
    private String userEmail;
    /**
     * 移动电话
     */
    private String userMobilephone;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 最近登录时间
     */
    private Date lastLoginTime;
    /**
     * 机构层级
     */
    private String orgLevel;


    private DptSessionVo dpt;

    private DptSessionVo upDpt;

    private List<RoleSessionVo> roles;

    private OrgSessionVo org;

    private OrgSessionVo upOrg;

    private InstuSessionVo instuOrg;

    private LogicSysSessionVo logicSys;

    private List<DutySessionVo> dutys;

    public List<DutySessionVo> getDutys() {
        return dutys;
    }

    public void setDutys(List<DutySessionVo> dutys) {
        this.dutys = dutys;
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

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getUserMobilephone() {
        return this.userMobilephone;
    }

    public String getUserAvatar() {
        return this.userAvatar;
    }

    public Date getLastLoginTime() {
        return this.lastLoginTime;
    }

    public String getOrgLevel() {
        return this.orgLevel;
    }

    public DptSessionVo getDpt() {
        return this.dpt;
    }

    public DptSessionVo getUpDpt() {
        return this.upDpt;
    }

    public List<RoleSessionVo> getRoles() {
        return this.roles;
    }

    public OrgSessionVo getOrg() {
        return this.org;
    }

    public OrgSessionVo getUpOrg() {
        return this.upOrg;
    }

    public InstuSessionVo getInstuOrg() {
        return this.instuOrg;
    }

    public LogicSysSessionVo getLogicSys() {
        return this.logicSys;
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

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserMobilephone(String userMobilephone) {
        this.userMobilephone = userMobilephone;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    public void setDpt(DptSessionVo dpt) {
        this.dpt = dpt;
    }

    public void setUpDpt(DptSessionVo upDpt) {
        this.upDpt = upDpt;
    }

    public void setRoles(List<RoleSessionVo> roles) {
        this.roles = roles;
    }

    public void setOrg(OrgSessionVo org) {
        this.org = org;
    }

    public void setUpOrg(OrgSessionVo upOrg) {
        this.upOrg = upOrg;
    }

    public void setInstuOrg(InstuSessionVo instuOrg) {
        this.instuOrg = instuOrg;
    }

    public void setLogicSys(LogicSysSessionVo logicSys) {
        this.logicSys = logicSys;
    }
}
