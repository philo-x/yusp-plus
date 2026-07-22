package cn.com.yusys.yusp.oca.domain.vo;

import java.io.Serial;
import java.io.Serializable;

/**
 * 认证信息表
 *
 * @author danyb1
 * @email danyb1@yusys.com.cn
 * @date 2020-12-11 14:11:01
 */
public class AdminSmAuthInfoVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
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

    private String key;

    private String value;

    public AdminSmAuthInfoVo() {
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

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
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

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
