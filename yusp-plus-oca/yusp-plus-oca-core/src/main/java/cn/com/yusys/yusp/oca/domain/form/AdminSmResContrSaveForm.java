package cn.com.yusys.yusp.oca.domain.form;

import java.util.List;
/**
 * 系统功能控制点表新建/修改表单数据
 *
 * @author zhangyt12
 * @date 2020-11-27 14:57:08
 */
public class AdminSmResContrSaveForm {
    /**
     * 控制点记录编号
     */
    private String contrId;
    /**
     * 控制点名称
     */
    private String contrName;
    /**
     * 菜单 id
     */
    private String menuId;

    /**
     * 菜单对应的业务功能 funcId
     */
    private String funcId;
    /**
     * 控制点代码
     */
    private String contrCode;
    /**
     * 控制点 URL
     */
    private String contrUrl;
    /**
     * 控制点备注
     */
    private String contrRemark;
    /**
     * 方法类型
     */
    private String methodType;
    /**
     * 以上均是控制点相关数据
     * authDataTmplIdList：关联的数据模板 id
     */
    private List<String> authDataTmplIdList;

    /**
     * 租户ID
     */
    private String dataTenantId;
    /**
     * 强制加密
     */
    private String encodeCheck;
    /**
     * 强制防重
     */
    private String nonceCheck;

    public AdminSmResContrSaveForm() {
    }

    public String getContrId() {
        return this.contrId;
    }

    public String getContrName() {
        return this.contrName;
    }

    public String getMenuId() {
        return this.menuId;
    }

    public String getFuncId() {
        return this.funcId;
    }

    public String getContrCode() {
        return this.contrCode;
    }

    public String getContrUrl() {
        return this.contrUrl;
    }

    public String getContrRemark() {
        return this.contrRemark;
    }


    public List<String> getAuthDataTmplIdList() {
        return this.authDataTmplIdList;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public void setContrName(String contrName) {
        this.contrName = contrName;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public void setContrCode(String contrCode) {
        this.contrCode = contrCode;
    }

    public void setContrUrl(String contrUrl) {
        this.contrUrl = contrUrl;
    }

    public void setContrRemark(String contrRemark) {
        this.contrRemark = contrRemark;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public void setAuthDataTmplIdList(List<String> authDataTmplIdList) {
        this.authDataTmplIdList = authDataTmplIdList;
    }

    public String getDataTenantId() {
        return dataTenantId;
    }

    public void setDataTenantId(String dataTenantId) {
        this.dataTenantId = dataTenantId;
    }

    public String getEncodeCheck() {
        return encodeCheck;
    }

    public void setEncodeCheck(String encodeCheck) {
        this.encodeCheck = encodeCheck;
    }

    public String getNonceCheck() {
        return nonceCheck;
    }

    public void setNonceCheck(String nonceCheck) {
        this.nonceCheck = nonceCheck;
    }
}
