package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmFuncModVo;

/**
 * 系统模块查询参数实体
 *
 * @author xufy1@yusys.com.cn
 * @date 2021-01-05 20:24
 */
public class AdminSmFuncModQuery extends PageQuery<AdminSmFuncModVo> {

    /**
     * 模块名称
     */
    private String modelName;

    public AdminSmFuncModQuery() {
    }

    public String getModelName() {
        return this.modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

}
