package cn.com.yusys.yusp.oca.domain.form;
/**
 * 系统功能控制点查询封装条件
 *
 * @author zhangyt12
 * @date 2020-11-27 14:57:08
 */
public class AdminSmResContrForm {
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
     * 关键字
     */
    private String keyWord;

    /**
     * 租户ID
     */
    private String dataTenantId;

    /**
     * 被授权对象iD
     */
    private String authObjId;
    /**
     * http方法
     */
    private String methodType;
    /**
     * 当前页
     */
    private Long page;
    /**
     * 当前页条数
     */
    private Long size;

    /**
     * 强制加密
     */
    private String encodeCheck;
    /**
     * 强制防重
     */
    private String nonceCheck;

    public AdminSmResContrForm() {
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

    public String getKeyWord() {
        return this.keyWord;
    }

    public Long getPage() {
        return this.page;
    }

    public Long getSize() {
        return this.size;
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

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getDataTenantId() {
        return dataTenantId;
    }

    public void setDataTenantId(String dataTenantId) {
        this.dataTenantId = dataTenantId;
    }

    public String getAuthObjId() {
        return authObjId;
    }

    public void setAuthObjId(String authObjId) {
        this.authObjId = authObjId;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
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
