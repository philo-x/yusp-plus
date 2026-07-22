package cn.com.yusys.yusp.oca.domain.vo;

import java.util.List;
/**
 * 用户关联机构
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class UserMgrOrgVo {
    private String orgId;
    private String orgName;
    private String upOrgId;
    private Boolean checked=false;
    private List<UserMgrOrgVo> children;

    public String getOrgId() {
        return this.orgId;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public String getUpOrgId() {
        return this.upOrgId;
    }

    public Boolean getChecked() {
        return this.checked;
    }

    public List<UserMgrOrgVo> getChildren() {
        return this.children;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setUpOrgId(String upOrgId) {
        this.upOrgId = upOrgId;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public void setChildren(List<UserMgrOrgVo> children) {
        this.children = children;
    }
}
