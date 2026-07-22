package cn.com.yusys.yusp.utrace.core.domain.vo;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.utrace.core.domain.entity.ModifyTraceEntity;

/**
 * 小U留痕查询参数实体类
 * @author Zhan YongQiang
 * @date 2021/6/24 13:45
 */
public class TraceQueryVo extends PageQuery<ModifyTraceEntity> {

    /**
     * 表单字段ID
     */
    private String mFieldId;


    /**
     * 数据主键
     */
    private  String mPkV;

    /**
     * 月份
     */
    private String month;

    public TraceQueryVo() {
    }

    public String getMFieldId() {
        return this.mFieldId;
    }

    public String getMPkV() {
        return this.mPkV;
    }

    public String getMonth() {
        return this.month;
    }

    public void setMFieldId(String mFieldId) {
        this.mFieldId = mFieldId;
    }

    public void setMPkV(String mPkV) {
        this.mPkV = mPkV;
    }

    public void setMonth(String month) {
        this.month = month;
    }


}
