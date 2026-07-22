package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 岗位关联VO
 * @author danyu
 */
@TableName("admin_sm_user_duty_rel")
public class AdminSmUserDutyRelVo  extends BaseEntity {
    /**
     * 岗位ID
     */
    @TableId()
    private String dutyId;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 登录账号
     */
    private String loginCode;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户编码
     */
    private String userCode;
    /**
     * 用户名称
     */
    private AvailableStateEnum userSts;

    public AdminSmUserDutyRelVo() {
    }

    public String getDutyId() {
        return this.dutyId;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public String getLoginCode() {
        return this.loginCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public AvailableStateEnum getUserSts() {
        return this.userSts;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setUserSts(AvailableStateEnum userSts) {
        this.userSts = userSts;
    }


}
