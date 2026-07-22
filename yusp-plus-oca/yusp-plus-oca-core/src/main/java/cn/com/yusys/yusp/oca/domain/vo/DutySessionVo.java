package cn.com.yusys.yusp.oca.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * session中duty信息
 *
 * @author: wujp4
 * @create: 2020-11-19 11:30
 */
public class DutySessionVo {
    /**
     * 岗位id
     */
    @JsonProperty("id")
    private String dutyId;
    /**
     * 岗位名称
     */
    @JsonProperty("name")
    private String dutyName;
    /**
     * 岗位code
     */
    @JsonProperty("code")
    private String dutyCode;

    public String getDutyId() {
        return dutyId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public String getDutyCode() {
        return dutyCode;
    }

    public void setDutyCode(String dutyCode) {
        this.dutyCode = dutyCode;
    }
}
