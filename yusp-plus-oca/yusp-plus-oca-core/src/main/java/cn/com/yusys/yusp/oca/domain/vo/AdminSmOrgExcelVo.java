package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.commons.excelcsv.annotation.ExcelCsv;
import cn.com.yusys.yusp.commons.excelcsv.annotation.ExcelField;

/**
 * 系统机构Excel导入导出
 *
 * @author zhanyq
 * @date 2021-06-30 17:31
 */
@ExcelCsv(namePrefix = "系统机构信息", fileType = ExcelCsv.ExportFileType.XLS)
public class AdminSmOrgExcelVo {

    @ExcelField(title = "机构代码", viewLength = 20)
    private String orgCode;

    @ExcelField(title = "机构名称", viewLength = 40)
    private String orgName;

    @ExcelField(title = "上级机构代码", viewLength = 20)
    private String upOrgId;

    @ExcelField(title = "机构级别", viewLength = 15)
    private Integer orgLevel;

    @ExcelField(title = "地址", viewLength = 50)
    private String orgAddr;

    @ExcelField(title = "邮编", viewLength = 10)
    private String zipCde;

    @ExcelField(title = "联系人", viewLength = 20)
    private String contUsr;

    @ExcelField(title = "联系电话", viewLength = 20)
    private String contTel;

    @ExcelField(title = "机构序列", viewLength = 20)
    private String orgSeq;

    @ExcelField(title = "机构状态", dictCode = "DATA_STS", viewLength = 15)
    private String orgSts;

    public AdminSmOrgExcelVo() {
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getUpOrgId() {
        return upOrgId;
    }

    public void setUpOrgId(String upOrgId) {
        this.upOrgId = upOrgId;
    }

    public Integer getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getOrgAddr() {
        return orgAddr;
    }

    public void setOrgAddr(String orgAddr) {
        this.orgAddr = orgAddr;
    }

    public String getZipCde() {
        return zipCde;
    }

    public void setZipCde(String zipCde) {
        this.zipCde = zipCde;
    }

    public String getContUsr() {
        return contUsr;
    }

    public void setContUsr(String contUsr) {
        this.contUsr = contUsr;
    }

    public String getContTel() {
        return contTel;
    }

    public void setContTel(String contTel) {
        this.contTel = contTel;
    }

    public String getOrgSeq() {
        return orgSeq;
    }

    public void setOrgSeq(String orgSeq) {
        this.orgSeq = orgSeq;
    }

    public String getOrgSts() {
        return orgSts;
    }

    public void setOrgSts(String orgSts) {
        this.orgSts = orgSts;
    }
}