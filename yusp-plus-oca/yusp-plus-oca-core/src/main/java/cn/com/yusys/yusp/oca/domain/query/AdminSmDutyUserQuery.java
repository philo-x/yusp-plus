package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserDutyRelVo;
/**
 * 系统角色表
 *
 * @author terry
 * @date 2020-11-18 18:06:35
 */
public class AdminSmDutyUserQuery extends PageQuery<AdminSmUserDutyRelVo> {
    /**
     * 记录编号
     */
    private String dutyId;

    /**
     * 账号
     */
    private String loginCode;
    /**
     * 姓名
     */
    private String userName;

    /**
     * 员工号
     */
    private String userCode;
    /**
     * 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效
     */
    private AvailableStateEnum userSts;

    public AdminSmDutyUserQuery() {
    }

    public String getDutyId() {
        return this.dutyId;
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
