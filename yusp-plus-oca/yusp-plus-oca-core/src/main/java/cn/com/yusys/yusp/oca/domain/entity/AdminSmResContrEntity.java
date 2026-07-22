package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统功能控制点表
 *
 * @author zhangyt12
 * @date 2020-11-27 14:57:08
 */
@TableName("admin_sm_res_contr")
public class AdminSmResContrEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId
    private String contrId;
    /**
     * 所属业务功能编号
     */
    private String funcId;
    /**
     * 控制操作代码
     */
    private String contrCode;
    /**
     * 控制操作名称
     */
    private String contrName;
    /**
     * 控制操作URL(用于后台校验时使用)
     */
    private String contrUrl;
    /**
     * 备注
     */
    private String contrRemark;
    /**
     * 请求类型
     */
    private String methodType;
    /**
     * 强制加密
     */
    private String encodeCheck;
    /**
     * 强制防重
     */
    private String nonceCheck;

    public AdminSmResContrEntity() {
    }

    public String getContrId() {
        return this.contrId;
    }

    public String getFuncId() {
        return this.funcId;
    }

    public String getContrCode() {
        return this.contrCode;
    }

    public String getContrName() {
        return this.contrName;
    }

    public String getContrUrl() {
        return this.contrUrl;
    }

    public String getContrRemark() {
        return this.contrRemark;
    }


    public String getMethodType() {
        return this.methodType;
    }

    public void setContrId(String contrId) {
        this.contrId = contrId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public void setContrCode(String contrCode) {
        this.contrCode = contrCode;
    }

    public void setContrName(String contrName) {
        this.contrName = contrName;
    }

    public void setContrUrl(String contrUrl) {
        this.contrUrl = contrUrl;
    }

    public void setContrRemark(String contrRemark) {
        this.contrRemark = contrRemark;
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
