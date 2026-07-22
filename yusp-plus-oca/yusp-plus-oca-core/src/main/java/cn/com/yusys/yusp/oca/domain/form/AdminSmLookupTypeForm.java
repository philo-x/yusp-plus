package cn.com.yusys.yusp.oca.domain.form;

import cn.com.yusys.yusp.oca.domain.vo.AdminSmDataTmplListVo;


/**
 * 数据字典类型form表单
 * @author zhanyq
 * @date 2021-06-30 17:28
 */
public class AdminSmLookupTypeForm {

    /**
     * 记录编号
     */
    private String contrId;
    /**
     * 所属业务功能编号
     */
    private String funcId;
    /**
     * 控制操作代码
     */
    private String contrCode;
    /**
     * 控制操作名称
     */
    private String contrName;
    /**
     * 控制操作URL(用于后台校验时使用)
     */
    private String contrUrl;
    /**
     * 备注
     */
    private String contrRemark;
    /**
     * 请求类型
     */
    private String methodType;
    /**
     * 关联的模板
     */
    private AdminSmDataTmplListVo adminSmDataTmplListVo;

    public AdminSmLookupTypeForm() {
    }

    public String getContrId() {
        return this.contrId;
    }

    public String getFuncId() {
        return this.funcId;
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

    public String getContrRemark() {
        return this.contrRemark;
    }

    public String getMethodType() {
        return this.methodType;
    }

    public AdminSmDataTmplListVo getAdminSmDataTmplListVo() {
        return this.adminSmDataTmplListVo;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
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

    public void setContrRemark(String contrRemark) {
        this.contrRemark = contrRemark;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public void setAdminSmDataTmplListVo(AdminSmDataTmplListVo adminSmDataTmplListVo) {
        this.adminSmDataTmplListVo = adminSmDataTmplListVo;
    }


}
