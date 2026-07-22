package cn.com.yusys.yusp.oca.domain.form;

/**
 * 岗位form表单
 * @author danyu
 */
public class AdminSmDutyForm {

    /**
     * 记录编号
     */
    private String dutyId;
    /**
     * 岗位代码
     */
    private String dutyCode;
    /**
     * 岗位名称
     */
    private String dutyName;
    /**
     * 所属机构编号
     */
    private String belongOrgId;
    /**
     * 备注
     */
    private String dutyRemark;
    /**
     * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
     */
    private String dutySts;

    public AdminSmDutyForm() {
    }


    public String getDutyId() {
        return this.dutyId;
    }

    public String getDutyCode() {
        return this.dutyCode;
    }

    public String getDutyName() {
        return this.dutyName;
    }

    public String getBelongOrgId() {
        return this.belongOrgId;
    }

    public String getDutyRemark() {
        return this.dutyRemark;
    }

    public String getDutySts() {
        return this.dutySts;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public void setDutyCode(String dutyCode) {
        this.dutyCode = dutyCode;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public void setBelongOrgId(String belongOrgId) {
        this.belongOrgId = belongOrgId;
    }

    public void setDutyRemark(String dutyRemark) {
        this.dutyRemark = dutyRemark;
    }

    public void setDutySts(String dutySts) {
        this.dutySts = dutySts;
    }

}
