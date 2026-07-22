package cn.com.yusys.yusp.oca.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *逻辑系统在session中Vo
 *@author: wujp4
 *@create: 2020-11-19 14:51
 */
public class LogicSysSessionVo {
    /**
     * 记录编号
     */
    @JsonProperty(value = "id")
    private String sysId;
    /**
     * 部门代码
     */
    @JsonProperty(value = "code")
    private String sysCode;
    /**
     * 部门名称
     */
    @JsonProperty(value = "name")
    private String sysName;

    public String getSysId() {
        return this.sysId;
    }

    public String getSysCode() {
        return this.sysCode;
    }

    public String getSysName() {
        return this.sysName;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }
}
