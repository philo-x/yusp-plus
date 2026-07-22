package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import cn.com.yusys.yusp.oca.validation.Insert;
import cn.com.yusys.yusp.oca.validation.Update;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import com.baomidou.mybatisplus.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 系统机构表
 *
 * @author wujp4
 * @date 2020-11-18 18:06:35
 */
@TableName("admin_sm_org")
public class AdminSmOrgEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @NotBlank(groups = Update.class, message = "orgId can not be empty!")
    private String orgId;
    /**
     * 金融机构编号
     */
    private String instuId;
    /**
     * 机构代码
     */
    @NotBlank(groups = Insert.class, message = "orgCode can not be empty!")
    private String orgCode;
    /**
     * 机构名称
     */
    @NotBlank(groups = Insert.class, message = "orgName can not be empty!")
    private String orgName;
    /**
     * 上级机构记录编号
     */
    @NotBlank(groups = Insert.class, message = "upOrgId can not be empty!")
    private String upOrgId;
    /**
     * 机构层级
     */
    private Integer orgLevel;
    /**
     * 地址
     */
    private String orgAddr;
    /**
     * 邮编
     */
    @Size(max = 6, message = "邮编接受最长6位")
    private String zipCde;
    /**
     * 联系电话
     */
    private String contTel;
    /**
     * 联系人
     */
    private String contUsr;
    /**
     * 状态：对应字典项=NORM_STS A：启用 I：停用 W：待启用
     */
    private AvailableStateEnum orgSts;

    /**
     * 机构序列，记录从根机构到当前机构编号，方便业务中使用机构统计数据
     */
    private String orgSeq;



    public AdminSmOrgEntity() {
    }

    public @NotBlank(groups = Update.class, message = "orgId can not be empty!") String getOrgId() {
        return this.orgId;
    }

    public String getInstuId() {
        return this.instuId;
    }

    public @NotBlank(groups = Insert.class, message = "orgCode can not be empty!") String getOrgCode() {
        return this.orgCode;
    }

    public @NotBlank(groups = Insert.class, message = "orgName can not be empty!") String getOrgName() {
        return this.orgName;
    }

    public @NotBlank(groups = Insert.class, message = "upOrgId can not be empty!") String getUpOrgId() {
        return this.upOrgId;
    }

    public Integer getOrgLevel() {
        return this.orgLevel;
    }

    public String getOrgAddr() {
        return this.orgAddr;
    }

    public @Size(max = 6, message = "邮编接受最长6位") String getZipCde() {
        return this.zipCde;
    }

    public String getContTel() {
        return this.contTel;
    }

    public String getContUsr() {
        return this.contUsr;
    }

    public AvailableStateEnum getOrgSts() {
        return this.orgSts;
    }


    public void setOrgId(@NotBlank(groups = Update.class, message = "orgId can not be empty!") String orgId) {
        this.orgId = orgId;
    }

    public void setInstuId(String instuId) {
        this.instuId = instuId;
    }

    public void setOrgCode(@NotBlank(groups = Insert.class, message = "orgCode can not be empty!") String orgCode) {
        this.orgCode = orgCode;
    }

    public void setOrgName(@NotBlank(groups = Insert.class, message = "orgName can not be empty!") String orgName) {
        this.orgName = orgName;
    }

    public void setUpOrgId(@NotBlank(groups = Insert.class, message = "upOrgId can not be empty!") String upOrgId) {
        this.upOrgId = upOrgId;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public void setOrgAddr(String orgAddr) {
        this.orgAddr = orgAddr;
    }

    public void setZipCde(@Size(max = 6, message = "邮编接受最长6位") String zipCde) {
        this.zipCde = zipCde;
    }

    public void setContTel(String contTel) {
        this.contTel = contTel;
    }

    public void setContUsr(String contUsr) {
        this.contUsr = contUsr;
    }

    public void setOrgSts(AvailableStateEnum orgSts) {
        this.orgSts = orgSts;
    }


    public String getOrgSeq() {
        return orgSeq;
    }

    public void setOrgSeq(String orgSeq) {
        this.orgSeq = orgSeq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminSmOrgEntity that = (AdminSmOrgEntity) o;
        return Objects.equals(orgId, that.orgId) &&
                Objects.equals(instuId, that.instuId) &&
                Objects.equals(orgCode, that.orgCode) &&
                Objects.equals(orgName, that.orgName) &&
                Objects.equals(upOrgId, that.upOrgId) &&
                Objects.equals(orgLevel, that.orgLevel) &&
                Objects.equals(orgAddr, that.orgAddr) &&
                Objects.equals(zipCde, that.zipCde) &&
                Objects.equals(contTel, that.contTel) &&
                Objects.equals(contUsr, that.contUsr) &&
                orgSts == that.orgSts;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orgId, instuId, orgCode, orgName, upOrgId, orgLevel, orgAddr, zipCde, contTel, contUsr, orgSts);
    }

}
