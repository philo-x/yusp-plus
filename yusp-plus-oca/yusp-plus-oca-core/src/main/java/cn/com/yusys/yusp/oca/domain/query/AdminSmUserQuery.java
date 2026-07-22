package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserVo;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 分页查询实体类
 *
 * @author terry
 * @date 2020-11-16 15:31:54
 */
public class AdminSmUserQuery extends PageQuery<AdminSmUserVo> {

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
     * 关键字
     */
    private String keyWord;
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
    private Date deadline;
    /**
     * 所属机构编号
     */
    private String orgId;
    /**
     * 所属部门编号
     */
    private String dptId;
    /**
     * 性别
     */
    private String userSex;
    /**
     * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
     */
    private AvailableStateEnum userSts;
    /**
     * 查询条件中需要排除的用户id
     */
    private String exceptedUserId;

    public AdminSmUserQuery() {
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

    public String getKeyWord() {
        return this.keyWord;
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

    public String getOrgId() {
        return this.orgId;
    }

    public String getDptId() {
        return this.dptId;
    }

    public String getUserSex() {
        return this.userSex;
    }

    public AvailableStateEnum getUserSts() {
        return this.userSts;
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

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
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

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setDptId(String dptId) {
        this.dptId = dptId;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public void setUserSts(AvailableStateEnum userSts) {
        this.userSts = userSts;
    }

    public String getExceptedUserId() {
        return exceptedUserId;
    }

    public void setExceptedUserId(String exceptedUserId) {
        this.exceptedUserId = exceptedUserId;
    }
}
