package cn.com.yusys.yusp.oca.domain.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 系统策略更新类
 *
 * @author: wujiangpeng
 * @create: 2021-04-02 11:09
 */
public class AdminSmCrelStraDto {
    /**
     * 记录编号
     */
    @NotBlank(message = "记录编号不能为空！")
    private String crelId;
    /**
     * 是否启用 1:是 2:否
     */
    @NotBlank(message = "启用标识不能为空！")
    private String enableFlag;
    /**
     * 策略明细
     */
    private String crelDetail;


    public @NotBlank(message = "记录编号不能为空！") String getCrelId() {
        return this.crelId;
    }

    public @NotBlank(message = "启用标识不能为空！") String getEnableFlag() {
        return this.enableFlag;
    }

    public String getCrelDetail() {
        return this.crelDetail;
    }

    public void setCrelId(@NotBlank(message = "记录编号不能为空！") String crelId) {
        this.crelId = crelId;
    }

    public void setEnableFlag(@NotBlank(message = "启用标识不能为空！") String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public void setCrelDetail(String crelDetail) {
        this.crelDetail = crelDetail;
    }

}
