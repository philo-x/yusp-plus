package cn.com.yusys.yusp.oca.domain.vo;

/**
 * @author yusys
 */
public class ContrVo {

    private String contrCode;

    private String contrName;

    private String contrUrl;

    private String sysId;

    private String funcId;

    private String methodType;
    private String details;

    public ContrVo() {
    }


    public String getContrCode() {
        return this.contrCode;
    }

    public String getContrName() {
        return this.contrName;
    }

    public String getContrUrl() {
        return this.contrUrl;
    }

    public String getSysId() {
        return this.sysId;
    }

    public String getFuncId() {
        return this.funcId;
    }

    public String getMethodType() {
        return this.methodType;
    }

    public String getDetails() {
        return this.details;
    }

    public void setContrCode(String contrCode) {
        this.contrCode = contrCode;
    }

    public void setContrName(String contrName) {
        this.contrName = contrName;
    }

    public void setContrUrl(String contrUrl) {
        this.contrUrl = contrUrl;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}