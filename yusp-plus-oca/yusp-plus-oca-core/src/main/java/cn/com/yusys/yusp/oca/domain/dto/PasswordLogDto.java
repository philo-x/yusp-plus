package cn.com.yusys.yusp.oca.domain.dto;

/**
 * 密码日志传输参数
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class PasswordLogDto {
    /**
     * 被修改的密码
     */
    private String pwdUped;
    /**
     * 修改者id
     */
    private String updateUser;
    /**
     * 用户ID
     */
    private String userId;

    public String getPwdUped() {
        return this.pwdUped;
    }

    public String getUpdateUser() {
        return this.updateUser;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setPwdUped(String pwdUped) {
        this.pwdUped = pwdUped;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
