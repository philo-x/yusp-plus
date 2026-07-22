package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmRoleVo;

import jakarta.validation.constraints.NotEmpty;

/**
 * 角色分页查询条件
 * @author zhanyq
 * @date 2021-06-30 17:31
 */
public class AdminSmPasteRoleQuery extends PageQuery<AdminSmRoleVo> {

    private String keyWord;

    @NotEmpty(message = "Which role do you want to copy from?")
    private String expectedRoleId;

    public AdminSmPasteRoleQuery() {
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public @NotEmpty(message = "Which role do you want to copy from?") String getExpectedRoleId() {
        return this.expectedRoleId;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setExpectedRoleId(@NotEmpty(message = "Which role do you want to copy from?") String expectedRoleId) {
        this.expectedRoleId = expectedRoleId;
    }


}
