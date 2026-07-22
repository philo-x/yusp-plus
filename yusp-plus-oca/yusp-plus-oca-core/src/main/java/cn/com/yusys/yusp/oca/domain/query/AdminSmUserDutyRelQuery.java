package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.vo.UserRoleRelVo;

import jakarta.validation.constraints.NotEmpty;

/**
 * 用用户去查角色列表
 *
 * @author terry
 * @date 2020-01-17 15:31:54
 */
public class AdminSmUserDutyRelQuery extends PageQuery<UserRoleRelVo> {
    /**
     * 用户id
     */
    @NotEmpty
    private String userId;
    /**
     * 关键字
     */
    private String keyWord;
    /**
     * 是否勾选
     */
    private Boolean checked;

    public AdminSmUserDutyRelQuery() {
    }


    public @NotEmpty String getUserId() {
        return this.userId;
    }

    public String getKeyWord() {
        return this.keyWord;
    }

    public Boolean getChecked() {
        return this.checked;
    }

    public void setUserId(@NotEmpty String userId) {
        this.userId = userId;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }


}
