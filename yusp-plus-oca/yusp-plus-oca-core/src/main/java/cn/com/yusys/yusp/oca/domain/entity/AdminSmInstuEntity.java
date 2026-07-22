package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 金融机构表
 * 
 * @author wujp4
 * @email wujp4@yusys.com.cn
 * @date 2020-11-19 14:30:22
 */
@TableName("admin_sm_instu")
public class AdminSmInstuEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String instuId;
    /**
     * 逻辑系统记录编号
     */
    private String sysId;
    /**
     * 金融机构代码
     */
    private String instuCde;
    /**
     * 金融机构名称
     */
    private String instuName;
    /**
     * 进入日期
     */
    private Date joinDt;
    /**
     * 地址
     */
    private String instuAddr;
    /**
     * 邮编
     */
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
     * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
     */
    private String instuSts;

    public AdminSmInstuEntity() {
    }


    public String getInstuId() {
        return this.instuId;
    }

    public String getSysId() {
        return this.sysId;
    }

    public String getInstuCde() {
        return this.instuCde;
    }

    public String getInstuName() {
        return this.instuName;
    }

    public Date getJoinDt() {
        return this.joinDt;
    }

    public String getInstuAddr() {
        return this.instuAddr;
    }

    public String getZipCde() {
        return this.zipCde;
    }

    public String getContTel() {
        return this.contTel;
    }

    public String getContUsr() {
        return this.contUsr;
    }

    public String getInstuSts() {
        return this.instuSts;
    }


    public void setInstuId(String instuId) {
        this.instuId = instuId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public void setInstuCde(String instuCde) {
        this.instuCde = instuCde;
    }

    public void setInstuName(String instuName) {
        this.instuName = instuName;
    }

    public void setJoinDt(Date joinDt) {
        this.joinDt = joinDt;
    }

    public void setInstuAddr(String instuAddr) {
        this.instuAddr = instuAddr;
    }

    public void setZipCde(String zipCde) {
        this.zipCde = zipCde;
    }

    public void setContTel(String contTel) {
        this.contTel = contTel;
    }

    public void setContUsr(String contUsr) {
        this.contUsr = contUsr;
    }

    public void setInstuSts(String instuSts) {
        this.instuSts = instuSts;
    }

}
