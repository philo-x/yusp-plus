package cn.com.yusys.yusp.oca.domain.form;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 数据字典form表单
 * @author zhanyq
 * @date 2021-06-30 17:27
 */
public class AdminSmLookupItemForm {

    /**
     * 记录编号
     */
    private String lookupItemId;
    /**
     * 字典类别英文别名
     */
    private String lookupCode;
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
     * 最新变更用户
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String lastChgUsr;

    public AdminSmLookupItemForm() {
    }


    public String getLookupItemId() {
        return this.lookupItemId;
    }

    public String getLookupCode() {
        return this.lookupCode;
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

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public void setLookupItemId(String lookupItemId) {
        this.lookupItemId = lookupItemId;
    }

    public void setLookupCode(String lookupCode) {
        this.lookupCode = lookupCode;
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

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }


}
