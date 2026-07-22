package cn.com.yusys.yusp.oca.domain.vo;

/**
 * 消息中心-用户订阅vo
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class UserSubscribeVo {

    /**
     * 用户id
     */
    String userNo;
    /**
     * 用户名
     */
    String userName;

    public String getUserNo() {
        return this.userNo;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
