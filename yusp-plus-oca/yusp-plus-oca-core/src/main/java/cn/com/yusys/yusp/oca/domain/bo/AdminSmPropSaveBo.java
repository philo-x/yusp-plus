package cn.com.yusys.yusp.oca.domain.bo;

import jakarta.validation.constraints.NotEmpty;

/**
 * 系统参数保存参数
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class AdminSmPropSaveBo {
    /**
     * 属性名
     */
    @NotEmpty(message = "参数名不能为空")
    private String propName;
    /**
     * 属性描述
     */
    private String propDesc;
    /**
     * 属性值
     */
    @NotEmpty(message = "参数值不能为空")
    private String propValue;


    public @NotEmpty(message = "参数名不能为空") String getPropName() {
        return this.propName;
    }

    public String getPropDesc() {
        return this.propDesc;
    }

    public @NotEmpty(message = "参数值不能为空") String getPropValue() {
        return this.propValue;
    }

    public void setPropName(@NotEmpty(message = "参数名不能为空") String propName) {
        this.propName = propName;
    }

    public void setPropDesc(String propDesc) {
        this.propDesc = propDesc;
    }

    public void setPropValue(@NotEmpty(message = "参数值不能为空") String propValue) {
        this.propValue = propValue;
    }

}
