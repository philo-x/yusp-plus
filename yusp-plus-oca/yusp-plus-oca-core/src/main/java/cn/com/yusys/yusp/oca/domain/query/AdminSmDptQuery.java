package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.constants.AvailableStateEnum;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmDptVo;

/**
 * 部门信息 分页查询条件
 *
 * @author terry
 * @date 2020-11-12 10:47:26
 */
public class AdminSmDptQuery extends PageQuery<AdminSmDptVo> {
    /**
     * 所属机构编号
     */
    private String orgId;
    /**
     * 部门代码
     */
    private String dptCode;
    /**
     * 部门名称
     */
    private String dptName;
    /**
     * 状态
     */
    private AvailableStateEnum dptSts;
    /**
     * 关键字
     */
    private String keyWord;

    public AdminSmDptQuery() {
    }

    public String getOrgId() {
        return this.orgId;
    }

    public String getDptCode() {
        return this.dptCode;
    }

    public String getDptName() {
        return this.dptName;
    }

    public AvailableStateEnum getDptSts() {
        return this.dptSts;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setDptCode(String dptCode) {
        this.dptCode = dptCode;
    }

    public void setDptName(String dptName) {
        this.dptName = dptName;
    }

    public void setDptSts(AvailableStateEnum dptSts) {
        this.dptSts = dptSts;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
