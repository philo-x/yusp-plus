package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmOrgVo;

/**
 * 机构信息 分页查询条件
 *
 * @author terry
 * @date 2020-11-27 18:06:35
 */
public class AdminSmOrgExtQuery extends PageQuery<AdminSmOrgVo> {
    /**
     * 上级机构记录编号
     */
    private String upOrgId;
    /**
     * 机构代码
     */
    private String orgCode;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 状态：对应字典项=NORM_STS A：启用 I：停用 W：待启用
     */
    private AvailableStateEnum orgSts;
    /**
     * 关键字
     */
    private String keyWord;

    public AdminSmOrgExtQuery() {
    }

    public String getUpOrgId() {
        return this.upOrgId;
    }

    public String getOrgCode() {
        return this.orgCode;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public AvailableStateEnum getOrgSts() {
        return this.orgSts;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public void setUpOrgId(String upOrgId) {
        this.upOrgId = upOrgId;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setOrgSts(AvailableStateEnum orgSts) {
        this.orgSts = orgSts;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
