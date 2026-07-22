package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 密码修改记录表
 * 
 * @author danyb1
 * @email danyb1@yusys.com.cn
 * @date 2020-12-02 15:07:44
 */
@TableName("admin_sm_password_log")
public class AdminSmPasswordLogEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 记录编号
	 */
	@TableId(type = IdType.ASSIGN_UUID)
	private String logId;
	/**
	 * 密码修改时间
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date pwdUpTime;
	/**
	 * 被修改的密码
	 */
	private String pwdUped;
	/**
	 * 修改者id
	 */
	private String updateUser;
	/**
	 * 用户ID
	 */
	private String userId;

    public AdminSmPasswordLogEntity() {
    }

    public String getLogId() {
        return this.logId;
    }

    public Date getPwdUpTime() {
        return this.pwdUpTime;
    }

    public String getPwdUped() {
        return this.pwdUped;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public String getUserId() {
        return this.userId;
    }


    public void setLogId(String logId) {
        this.logId = logId;
    }

    public void setPwdUpTime(Date pwdUpTime) {
        this.pwdUpTime = pwdUpTime;
    }

    public void setPwdUped(String pwdUped) {
        this.pwdUped = pwdUped;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
