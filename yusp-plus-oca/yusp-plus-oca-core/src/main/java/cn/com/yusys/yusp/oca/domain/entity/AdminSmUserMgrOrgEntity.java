package cn.com.yusys.yusp.oca.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户授权管理机构表
 *
 * @author danyb1
 * @email danyb1@yusys.com.cn
 * @date 2020-12-02 16:29:50
 */
@TableName("admin_sm_user_mgr_org")
public class AdminSmUserMgrOrgEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String userMgrOrgId;
    /**
     * 用户编号
     */
    private String userId;
    /**
     * 被授权管理机构编号
     */
    private String orgId;

    public AdminSmUserMgrOrgEntity(String userId, String orgId) {
        this.userId = userId;
        this.orgId = orgId;
    }

    public AdminSmUserMgrOrgEntity() {
    }

    public String getUserMgrOrgId() {
        return this.userMgrOrgId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getOrgId() {
        return this.orgId;
    }


    public void setUserMgrOrgId(String userMgrOrgId) {
        this.userMgrOrgId = userMgrOrgId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }


}
