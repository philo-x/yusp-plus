package cn.com.yusys.yusp.oca.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;

/**
 * 数据字典内容表
 *
 * @author danyb1
 * @date 2021-01-15 16:44:33
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminSmLookupDictVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字典项编号，默认uuid
     */
    private String lookupItemId;
    /**
     * 字典类别code码
     */
    private String lookupCode;
    /**
     * 字典类别名称
     */
    private String lookupName;
    /**
     * 字典类别分类标识id
     */
    private String lookupTypeId;
    /**
     * 字典类别分类标识名称
     */
    private String lookupTypeName;
    /**
     * 上级字典内容编号
     */
    private String upLookupItemId;
    /**
     * 字典代码
     */
    private String lookupItemCode;
    /**
     * 字典名称
     */
    private String lookupItemName;
    /**
     * 字典备注说明
     */
    private String lookupItemComment;
    /**
     * 字典项排序
     */
    private Integer lookupItemOrder;
    /**
     * 金融机构编号
     */
    private String instuId;

    /**
     * 详情的标识字段
     */
    private String lookupItemsString;

    public AdminSmLookupDictVo() {
    }

    public String getLookupItemId() {
        return this.lookupItemId;
    }

    public String getLookupCode() {
        return this.lookupCode;
    }

    public String getLookupName() {
        return this.lookupName;
    }

    public String getLookupTypeId() {
        return this.lookupTypeId;
    }

    public String getLookupTypeName() {
        return this.lookupTypeName;
    }

    public String getUpLookupItemId() {
        return this.upLookupItemId;
    }

    public String getLookupItemCode() {
        return this.lookupItemCode;
    }

    public String getLookupItemName() {
        return this.lookupItemName;
    }

    public String getLookupItemComment() {
        return this.lookupItemComment;
    }

    public Integer getLookupItemOrder() {
        return this.lookupItemOrder;
    }

    public String getInstuId() {
        return this.instuId;
    }

    public String getLookupItemsString() {
        return this.lookupItemsString;
    }

    public void setLookupItemId(String lookupItemId) {
        this.lookupItemId = lookupItemId;
    }

    public void setLookupCode(String lookupCode) {
        this.lookupCode = lookupCode;
    }

    public void setLookupName(String lookupName) {
        this.lookupName = lookupName;
    }

    public void setLookupTypeId(String lookupTypeId) {
        this.lookupTypeId = lookupTypeId;
    }

    public void setLookupTypeName(String lookupTypeName) {
        this.lookupTypeName = lookupTypeName;
    }

    public void setUpLookupItemId(String upLookupItemId) {
        this.upLookupItemId = upLookupItemId;
    }

    public void setLookupItemCode(String lookupItemCode) {
        this.lookupItemCode = lookupItemCode;
    }

    public void setLookupItemName(String lookupItemName) {
        this.lookupItemName = lookupItemName;
    }

    public void setLookupItemComment(String lookupItemComment) {
        this.lookupItemComment = lookupItemComment;
    }

    public void setLookupItemOrder(Integer lookupItemOrder) {
        this.lookupItemOrder = lookupItemOrder;
    }

    public void setInstuId(String instuId) {
        this.instuId = instuId;
    }

    public void setLookupItemsString(String lookupItemsString) {
        this.lookupItemsString = lookupItemsString;
    }

}
