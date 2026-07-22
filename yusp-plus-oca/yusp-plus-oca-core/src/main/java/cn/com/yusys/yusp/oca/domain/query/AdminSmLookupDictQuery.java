package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmLookupDictEntity;

/**
 * 数据字典查询条件
 *
 * @author xufy1@yusys.com.cn
 * @date 2021-01-05 20:24
 */
public class AdminSmLookupDictQuery extends PageQuery<AdminSmLookupDictEntity> {

    /**
     * 关键字
     */
    private String keyWord;

    public AdminSmLookupDictQuery() {
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }


}
