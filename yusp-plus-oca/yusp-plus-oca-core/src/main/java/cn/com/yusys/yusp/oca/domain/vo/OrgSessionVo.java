package cn.com.yusys.yusp.oca.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * session中机构信息
 *
 * @author: wujp4
 * @create: 2020-11-19 11:45
 */
public class OrgSessionVo {
    /**
     * 记录编号
     */
    @JsonProperty(value = "id")
    private String orgId;
    /**
     * 角色代码
     */
    @JsonProperty(value = "code")
    private String orgCode;
    /**
     * 角色名称
     */
    @JsonProperty(value = "name")
    private String orgName;

    /**
     * 机构层级
     */
    @JsonProperty(value = "orgLevel")
    private String orgLevel;

    public String getOrgId() {
        return this.orgId;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public String getOrgLevel() {
        return this.orgLevel;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }
}
