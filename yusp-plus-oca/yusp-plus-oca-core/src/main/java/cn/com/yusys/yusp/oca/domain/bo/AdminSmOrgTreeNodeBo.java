package cn.com.yusys.yusp.oca.domain.bo;

import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * 机构树节点数据库实体
 *
 * @author terry
 * @date 2020-11-27 18:06:35
 */
@TableName("admin_sm_org" )
public class AdminSmOrgTreeNodeBo {

    /**
     * 机构id
     */
    @TableId
    private String orgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构码
     */
    private String orgCode;
    /**
     * 金融机构id
     */
    private String instuId;
    /**
     * 机构层级
     */
    private Integer orgLevel;
    /**
     * 状态
     */
    private AvailableStateEnum orgSts;


    /**
     * 上级机构id
     */
    private String upOrgId;
    /**
     * 最后修改时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date lastChgDt;
    /**
     * 子机构
     */
    private List<AdminSmOrgTreeNodeBo> children;

    public AdminSmOrgTreeNodeBo() {
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public String getInstuId() {
        return this.instuId;
    }

    public Integer getOrgLevel() {
        return this.orgLevel;
    }

    public AvailableStateEnum getOrgSts() {
        return this.orgSts;
    }

    public String getUpOrgId() {
        return this.upOrgId;
    }

    public Date getLastChgDt() {
        return this.lastChgDt;
    }

    public List<AdminSmOrgTreeNodeBo> getChildren() {
        return this.children;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setInstuId(String instuId) {
        this.instuId = instuId;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public void setOrgSts(AvailableStateEnum orgSts) {
        this.orgSts = orgSts;
    }

    public void setUpOrgId(String upOrgId) {
        this.upOrgId = upOrgId;
    }

    public void setLastChgDt(Date lastChgDt) {
        this.lastChgDt = lastChgDt;
    }

    public void setChildren(List<AdminSmOrgTreeNodeBo> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminSmOrgTreeNodeBo that = (AdminSmOrgTreeNodeBo) o;
        return Objects.equals(orgId, that.orgId) &&
                Objects.equals(orgName, that.orgName) &&
                Objects.equals(orgCode, that.orgCode) &&
                Objects.equals(instuId, that.instuId) &&
                Objects.equals(orgLevel, that.orgLevel) &&
                orgSts == that.orgSts &&
                Objects.equals(upOrgId, that.upOrgId) &&
                Objects.equals(lastChgDt, that.lastChgDt) &&
                Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, orgName, orgCode, instuId, orgLevel, orgSts, upOrgId, lastChgDt, children);
    }
}
