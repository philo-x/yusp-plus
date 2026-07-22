package cn.com.yusys.yusp.workboard.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

/**
 *
 *
 * @author wx20220514
 * @date 2022-07-29 15:07:33
 */
@TableName("oca_workboard")
public class OcaWorkboardEntity {
	
	/** user_id **/
	@TableId(type=IdType.AUTO)
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
	@TableField(fill=FieldFill.INSERT)
	private Date createTime;
	
	/** 更新时间 **/
    @TableField(fill=FieldFill.INSERT_UPDATE)
	private Date updateTime;


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
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return createTime
     */
    public Date getCreateTime() {
        return this.createTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return updateTime
     */
    public Date getUpdateTime() {
        return this.updateTime;
    }


}