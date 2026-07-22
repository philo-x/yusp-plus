package cn.com.yusys.yusp.oca.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * session中dpt返回的Vo
 *
 * @author: wujp4
 * @create: 2020-11-19 09:36
 */
public class DptSessionVo {
    /**
     * 记录编号
     */
    @JsonProperty(value = "id")
    private String dptId;
    /**
     * 部门代码
     */
    @JsonProperty(value = "code")
    private String dptCode;
    /**
     * 部门名称
     */
    @JsonProperty(value = "name")
    private String dptName;

    public String getDptId() {
        return this.dptId;
    }

    public String getDptCode() {
        return this.dptCode;
    }

    public String getDptName() {
        return this.dptName;
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
}
