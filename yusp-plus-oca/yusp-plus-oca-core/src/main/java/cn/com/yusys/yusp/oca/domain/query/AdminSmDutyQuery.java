package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDutyVo;
/**
 * 岗位分页查询条件类
 *
 * @author terry
 * @email tanrui1@yusys.com.cn
 */
public class AdminSmDutyQuery extends PageQuery<AdminSmDutyVo> {
    /**
     * 所属机构编号
     */
    private String orgId;
    /**
     * 关键字
     */
    private String keyWord;
    /**
     * 岗位代码
     */
    private String dutyCode;
    /**
     * 岗位名称
     */
    private String dutyName;
    /**
     * 状态
     */
    private AvailableStateEnum dutySts;

    public AdminSmDutyQuery() {
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public String getDutyCode() {
        return this.dutyCode;
    }

    public String getDutyName() {
        return this.dutyName;
    }

    public AvailableStateEnum getDutySts() {
        return this.dutySts;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setDutyCode(String dutyCode) {
        this.dutyCode = dutyCode;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    public void setDutySts(AvailableStateEnum dutySts) {
        this.dutySts = dutySts;
    }

}
