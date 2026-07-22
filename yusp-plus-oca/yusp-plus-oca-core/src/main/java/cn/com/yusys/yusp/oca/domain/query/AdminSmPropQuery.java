package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmPropEntity;

/**
 * 系统信息分页查询传参
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class AdminSmPropQuery extends PageQuery<AdminSmPropEntity> {
    private String propName;
    private String propDesc;
    private String keyWord;

    public AdminSmPropQuery() {
    }

    public String getPropName() {
        return this.propName;
    }

    public String getPropDesc() {
        return this.propDesc;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public void setPropDesc(String propDesc) {
        this.propDesc = propDesc;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
