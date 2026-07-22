package cn.com.yusys.yusp.notice.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 系统公告用户查阅历史表对应实体类
 * @author zhangyt12
 * @date 2021/6/24 15:42
 */
@TableName("admin_sm_notice_read")
public class AdminSmNoticeReadEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
	/**
	 * 记录编号
	 */
	@TableId
	private String readId;
	/**
	 * 公告编号
	 */
	private String noticeId;
	/**
	 * 用户编号
	 */
	private String userId;
	/**
	 * 阅读时间
	 */
	private String readTime;

    public AdminSmNoticeReadEntity() {
    }

    public String getReadId() {
        return this.readId;
    }

    public String getNoticeId() {
        return this.noticeId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getReadTime() {
        return this.readTime;
    }

    public void setReadId(String readId) {
        this.readId = readId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminSmNoticeReadEntity entity = (AdminSmNoticeReadEntity) o;
        return Objects.equals(readId, entity.readId) &&
                Objects.equals(noticeId, entity.noticeId) &&
                Objects.equals(userId, entity.userId) &&
                Objects.equals(readTime, entity.readTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readId, noticeId, userId, readTime);
    }
}
