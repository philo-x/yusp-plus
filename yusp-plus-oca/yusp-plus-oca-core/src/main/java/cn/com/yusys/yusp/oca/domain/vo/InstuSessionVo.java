package cn.com.yusys.yusp.oca.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *金融机构在session中Vo
 *@author: wujp4
 *@create: 2020-11-19 14:49
 */
public class InstuSessionVo {
    /**
     * 记录编号
     */
    @JsonProperty(value = "id")
    private String instuId;
    /**
     * 部门代码
     */
    @JsonProperty(value = "code")
    private String instuCde;
    /**
     * 部门名称
     */
    @JsonProperty(value = "name")
    private String instuName;

    public String getInstuId() {
        return this.instuId;
    }

    public String getInstuCde() {
        return this.instuCde;
    }

    public String getInstuName() {
        return this.instuName;
    }

    public void setInstuId(String instuId) {
        this.instuId = instuId;
    }

    public void setInstuCde(String instuCde) {
        this.instuCde = instuCde;
    }

    public void setInstuName(String instuName) {
        this.instuName = instuName;
    }
}
