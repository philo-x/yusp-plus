package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.vo.InstuExtVo;

/**
 * 金融机构查询条件
 * @author zhanyq
 * @date 2021-06-30 17:23
 */
public class AdminSmInstuQuery extends PageQuery<InstuExtVo> {


    /**
     * 金融机构代码
     */
    private String instuCde;
    /**
     * 金融机构名称
     */
    private String instuName;
    /**
     * 状态
     */
    private String instuSts;

    public AdminSmInstuQuery() {
    }

    public String getInstuCde() {
        return this.instuCde;
    }

    public String getInstuName() {
        return this.instuName;
    }

    public String getInstuSts() {
        return this.instuSts;
    }

    public void setInstuCde(String instuCde) {
        this.instuCde = instuCde;
    }

    public void setInstuName(String instuName) {
        this.instuName = instuName;
    }

    public void setInstuSts(String instuSts) {
        this.instuSts = instuSts;
    }


}
