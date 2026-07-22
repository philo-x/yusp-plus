package cn.com.yusys.yusp.oca.domain.form;

/**
 * 部门form表单
 * @author danyu
 */
public class AdminSmDptForm {

    private String dptId;
    /**
     * 部门代码
     */
    private String dptCode;
    /**
     * 部门名称
     */
    private String dptName;
    /**
     * 所属机构编号
     */
    private String belongOrgId;
    /**
     * 上级部门记录编号
     */
    private String upDptId;
    /**
     * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
     */
    private String dptSts;

    public AdminSmDptForm() {
    }


    public String getDptId() {
        return this.dptId;
    }

    public String getDptCode() {
        return this.dptCode;
    }

    public String getDptName() {
        return this.dptName;
    }

    public String getBelongOrgId() {
        return this.belongOrgId;
    }

    public String getUpDptId() {
        return this.upDptId;
    }

    public String getDptSts() {
        return this.dptSts;
    }

    public void setDptId(String dptId) {
        this.dptId = dptId;
    }

    public void setDptCode(String dptCode) {
        this.dptCode = dptCode;
    }

    public void setDptName(String dptName) {
        this.dptName = dptName;
    }

    public void setBelongOrgId(String belongOrgId) {
        this.belongOrgId = belongOrgId;
    }

    public void setUpDptId(String upDptId) {
        this.upDptId = upDptId;
    }

    public void setDptSts(String dptSts) {
        this.dptSts = dptSts;
    }
}
