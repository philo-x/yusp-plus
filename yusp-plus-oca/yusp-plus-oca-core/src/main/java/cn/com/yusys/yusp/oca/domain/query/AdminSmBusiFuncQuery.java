package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmBusiFuncVo;

/**
 * 业务功能查询参数Vo
 * @author xufy1@yusys.com.cn
 * @date 2020-12-15 06:57
 */
public class AdminSmBusiFuncQuery extends PageQuery<AdminSmBusiFuncVo> {

    /**
     * 所属功能模块编号
     */
    private String modId;
    /**
     * 模糊查询的关键字
     */
    private String keyWord;
    /**
     * 旧oca中 工作流查询用到的模糊 关键字查询字段
     */
    private String queryKey;

    public AdminSmBusiFuncQuery() {
    }

    public String getModId() {
        return this.modId;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public String getQueryKey() {
        return this.queryKey;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setQueryKey(String queryKey) {
        this.queryKey = queryKey;
    }


}
