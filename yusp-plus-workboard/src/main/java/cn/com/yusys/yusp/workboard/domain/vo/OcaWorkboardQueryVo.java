package cn.com.yusys.yusp.workboard.domain.vo;
import cn.com.yusys.yusp.commons.module.adapter.query.PageQuery;

/**
 *
 *
 * @author wx20220514
 * @date 2022-07-29 15:07:33
 */
public class OcaWorkboardQueryVo extends PageQuery{
	
	/** user_id **/
	private Integer userId;
	
	/** 客户名称 **/
	private String userName;
	
	/** 联系方式 **/
	private String userPhone;
	
	/** 客户地址 **/
	private String userAddress;
	
	/** 客户经理 **/
	private String userManager;
	
	/** 性别0男 1女 **/
	private String userSex;
	
	/** 客户备注 **/
	private String userDesc;
	
	/** 创建时间 **/
	private java.time.LocalDateTime createTime;
	
	/** 更新时间 **/
	private java.time.LocalDateTime updateTime;
	
	/** data_tenant_id **/
	private String dataTenantId;
	

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return userId
     */
    public Integer getUserId() {
        return this.userId;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param userPhone
     */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    /**
     * @return userPhone
     */
    public String getUserPhone() {
        return this.userPhone;
    }

    /**
     * @param userAddress
     */
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    /**
     * @return userAddress
     */
    public String getUserAddress() {
        return this.userAddress;
    }

    /**
     * @param userManager
     */
    public void setUserManager(String userManager) {
        this.userManager = userManager;
    }

    /**
     * @return userManager
     */
    public String getUserManager() {
        return this.userManager;
    }

    /**
     * @param userSex
     */
    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    /**
     * @return userSex
     */
    public String getUserSex() {
        return this.userSex;
    }

    /**
     * @param userDesc
     */
    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    /**
     * @return userDesc
     */
    public String getUserDesc() {
        return this.userDesc;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(java.time.LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * @return createTime
     */
    public java.time.LocalDateTime getCreateTime() {
        return this.createTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(java.time.LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return updateTime
     */
    public java.time.LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    /**
     * @param dataTenantId
     */
    public void setDataTenantId(String dataTenantId) {
        this.dataTenantId = dataTenantId;
    }

    /**
     * @return dataTenantId
     */
    public String getDataTenantId() {
        return this.dataTenantId;
    }

}