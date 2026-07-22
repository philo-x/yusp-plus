package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmUserVo;

import jakarta.validation.constraints.NotEmpty;
/**
 *用户粘贴列表查询
 *
 * @author terry
 * @date 2020-12-11 18:06:35
 */
public class AdminSmPasteUserQuery extends PageQuery<AdminSmUserVo> {
    /**
     * 关键字
     */
    private String keyWord;
    /**
     * 排除指定用户的userId
     */
    @NotEmpty(message = "Which user do you want to copy from?")
    private String expectedUserId;

    public AdminSmPasteUserQuery() {
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public @NotEmpty(message = "Which user do you want to copy from?") String getExpectedUserId() {
        return this.expectedUserId;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setExpectedUserId(@NotEmpty(message = "Which user do you want to copy from?") String expectedUserId) {
        this.expectedUserId = expectedUserId;
    }

}
