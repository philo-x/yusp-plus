package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.validation.Insert;
import cn.com.yusys.yusp.oca.validation.Update;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import jakarta.validation.constraints.NotBlank;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统岗位表
 * 
 * @author danyb1
 * @date 2020-12-01 21:55:19
 */
@TableName("admin_sm_duty")
public class AdminSmDutyEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @NotBlank(groups = Update.class)
    private String dutyId;
    /**
     * 岗位代码
     */
    @NotBlank(groups = Insert.class)
    private String dutyCode;
    /**
     * 岗位名称
     */
    @NotBlank(groups = Insert.class)
    private String dutyName;
    /**
     * 所属机构编号
     */
    @NotBlank(groups = Insert.class)
    private String orgId;
    /**
     * 备注
     */
    private String dutyRemark;
    /**
     * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
     */
    private AvailableStateEnum dutySts;

    public AdminSmDutyEntity() {
    }

    public @NotBlank(groups = Update.class) String getDutyId() {
        return this.dutyId;
    }

    public @NotBlank(groups = Insert.class) String getDutyCode() {
        return this.dutyCode;
    }

    public @NotBlank(groups = Insert.class) String getDutyName() {
        return this.dutyName;
    }

    public @NotBlank(groups = Insert.class) String getOrgId() {
        return this.orgId;
    }

    public String getDutyRemark() {
        return this.dutyRemark;
    }

    public AvailableStateEnum getDutySts() {
        return this.dutySts;
    }


    public void setDutyId(@NotBlank(groups = Update.class) String dutyId) {
        this.dutyId = dutyId;
    }

    public void setDutyCode(@NotBlank(groups = Insert.class) String dutyCode) {
        this.dutyCode = dutyCode;
    }

    public void setDutyName(@NotBlank(groups = Insert.class) String dutyName) {
        this.dutyName = dutyName;
    }

    public void setOrgId(@NotBlank(groups = Insert.class) String orgId) {
        this.orgId = orgId;
    }

    public void setDutyRemark(String dutyRemark) {
        this.dutyRemark = dutyRemark;
    }

    public void setDutySts(AvailableStateEnum dutySts) {
        this.dutySts = dutySts;
    }

}