package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;

import java.io.Serial;
import java.io.Serializable;

/**
 * 认证策略参数表
 *
 * @author wujp4
 * @date 2021-03-30 11:27:32
 */
@TableName("admin_sm_crel_stra")
public class AdminSmCrelStraEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;



    /**
     * 记录编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String crelId;
    /**
     * 逻辑系统编号
     */
    private String sysId;
    /**
     * 策略标识
     */
    private String crelKey;
    /**
     * 策略名称
     */
    private String crelName;
    /**
     * 是否启用 1:是 2:否
     */
    private String enableFlag;
    /**
     * 策略明细
     */
    private String crelDetail;
    /**
     * 策略描述
     */
    private String crelDescribe;
    /**
     * 执行动作1: 冻结用户 2:禁止 3：警告
     */
    private String actionType;

    /**
     * 是否为系统生成
     */
    private Integer sysDefault;

    public AdminSmCrelStraEntity() {
    }

    public String getCrelId() {
        return this.crelId;
    }

    public String getSysId() {
        return this.sysId;
    }

    public String getCrelKey() {
        return this.crelKey;
    }

    public String getCrelName() {
        return this.crelName;
    }

    public String getEnableFlag() {
        return this.enableFlag;
    }

    public String getCrelDetail() {
        return this.crelDetail;
    }

    public String getCrelDescribe() {
        return this.crelDescribe;
    }

    public String getActionType() {
        return this.actionType;
    }


    public Integer getSysDefault() {
        return this.sysDefault;
    }

    public void setCrelId(String crelId) {
        this.crelId = crelId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setCrelKey(String crelKey) {
        this.crelKey = crelKey;
    }

    public void setCrelName(String crelName) {
        this.crelName = crelName;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public void setCrelDetail(String crelDetail) {
        this.crelDetail = crelDetail;
    }

    public void setCrelDescribe(String crelDescribe) {
        this.crelDescribe = crelDescribe;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }


    public void setSysDefault(Integer sysDefault) {
        this.sysDefault = sysDefault;
    }


}
