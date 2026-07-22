package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户角色关联表
 *
 * @author danyb1
 * @date 2020-12-01 21:55:19
 */
@TableName("admin_sm_user_duty_rel")
public class AdminSmUserDutyRelEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String userDutyRelId;
    /**
     * 用户编号
     */
    private String userId;
    /**
     * 岗位编号
     */
    private String dutyId;


    public AdminSmUserDutyRelEntity(String userId, String dutyId) {
        this.userId = userId;
        this.dutyId = dutyId;
    }

    public AdminSmUserDutyRelEntity() {
    }

    public String getUserDutyRelId() {
        return this.userDutyRelId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getDutyId() {
        return this.dutyId;
    }


    public void setUserDutyRelId(String userDutyRelId) {
        this.userDutyRelId = userDutyRelId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }



}
