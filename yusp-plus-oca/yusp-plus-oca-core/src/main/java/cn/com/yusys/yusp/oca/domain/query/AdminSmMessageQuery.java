package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.entity.AdminSmMessageEntity;

/**
 * 系统提示消息分页查询传参
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class AdminSmMessageQuery extends PageQuery<AdminSmMessageEntity> {

    /**
     * 信息码
     */
    private String code;
    /**
     * 提示内容
     */
    private String message;

    /**
     * 查询关键字
     */
    private String keyWord;

    public AdminSmMessageQuery() {
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

}
