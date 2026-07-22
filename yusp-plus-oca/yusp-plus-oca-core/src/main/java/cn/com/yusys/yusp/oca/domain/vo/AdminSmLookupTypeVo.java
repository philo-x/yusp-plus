package cn.com.yusys.yusp.oca.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;


/**
 * 数据自带你类型VO
 * @author zhanyq
 * @date 2021-06-30 17:29
 */
public class AdminSmLookupTypeVo {

    /**
     * 记录编号
     */
    private String lookupTypeId;
    /**
     * 目录名称
     */
    private String lookupTypeName;
    /**
     * 上级目录编号
     */
    private String upLookupTypeId;
    /**
     * 上级目录编号名称
     */
    private String upLookupTypeName;
    /**
     * 金融机构编号
     */
    private String instuId;
    /**
     * 最新变更用户
     */
    private String lastChgUsr;
    /**
     * 最新变更时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;

    public AdminSmLookupTypeVo() {
    }

    public String getLookupTypeId() {
        return this.lookupTypeId;
    }

    public String getLookupTypeName() {
        return this.lookupTypeName;
    }

    public String getUpLookupTypeId() {
        return this.upLookupTypeId;
    }

    public String getUpLookupTypeName() {
        return this.upLookupTypeName;
    }

    public String getInstuId() {
        return this.instuId;
    }

    public String getLastChgUsr() {
        return this.lastChgUsr;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public void setLookupTypeId(String lookupTypeId) {
        this.lookupTypeId = lookupTypeId;
    }

    public void setLookupTypeName(String lookupTypeName) {
        this.lookupTypeName = lookupTypeName;
    }

    public void setUpLookupTypeId(String upLookupTypeId) {
        this.upLookupTypeId = upLookupTypeId;
    }

    public void setUpLookupTypeName(String upLookupTypeName) {
        this.upLookupTypeName = upLookupTypeName;
    }

    public void setInstuId(String instuId) {
        this.instuId = instuId;
    }

    public void setLastChgUsr(String lastChgUsr) {
        this.lastChgUsr = lastChgUsr;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }


}
