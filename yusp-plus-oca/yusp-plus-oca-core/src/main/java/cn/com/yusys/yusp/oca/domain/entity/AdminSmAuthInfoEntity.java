package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * 认证信息表
 *
 * @author danyb1
 * @date 2020-12-11 14:11:01
 */
@TableName("admin_sm_auth_info")
public class AdminSmAuthInfoEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId
    private String authId;
    /**
     * 认证类型名称
     */
    private String authName;
    /**
     * 实现类名称
     */
    private String beanName;
    /**
     * 备注
     */
    private String authRemark;

    public AdminSmAuthInfoEntity() {
    }

    public String getAuthId() {
        return this.authId;
    }

    public String getAuthName() {
        return this.authName;
    }

    public String getBeanName() {
        return this.beanName;
    }

    public String getAuthRemark() {
        return this.authRemark;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setAuthRemark(String authRemark) {
        this.authRemark = authRemark;
    }
}
