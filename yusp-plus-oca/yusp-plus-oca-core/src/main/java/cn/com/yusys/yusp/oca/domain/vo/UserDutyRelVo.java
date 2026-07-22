package cn.com.yusys.yusp.oca.domain.vo;

/**
 * 用户所在机构可关联的角色列表以及实际关联状态
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class UserDutyRelVo {
    private String dutyId;
    private String dutyCode;
    private String dutyName;
    private Boolean checked=false;


    public String getDutyId() {
        return this.dutyId;
    }

    public String getDutyCode() {
        return this.dutyCode;
    }

    public String getDutyName() {
        return this.dutyName;
    }

    public Boolean getChecked() {
        return this.checked;
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

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
