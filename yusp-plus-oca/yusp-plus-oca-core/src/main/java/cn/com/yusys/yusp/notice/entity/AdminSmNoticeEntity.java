package cn.com.yusys.yusp.notice.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统公告表对应实体类
 * @author zhangyt12
 * @date 2021/6/24 15:41
 */
@TableName("admin_sm_notice")
public class AdminSmNoticeEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

	/**
	 * 公告编号
	 */
	@TableId
	private String noticeId;
	/**
	 * 公告标题
	 */
	private String noticeTitle;
	/**
	 * 公告重要程度
	 */
	private String noticeLevel;
	/**
	 * 有效期至
	 */
	private String activeDate;
	/**
	 * 是否置顶
	 */
	private String isTop;
	/**
	 * 置顶有效期
	 */
	private String topActiveDate;
	/**
	 * 公告内容(存富文本表记录编号)
	 */
	private String richeditId;
	/**
	 * 发布状态（状态：对应字典项=NORM_STS C：未发布O：已发布）
	 */
	private String pubSts;
	/**
	 * 发布时间
	 */
	private String pubTime;
	/**
	 * 公告发布人编号
	 */
	private String pubUserId;
	/**
	 * 公告发布人姓名
	 */
	private String pubUserName;
	/**
	 * 发布机构编号
	 */
	private String pubOrgId;
	/**
	 * 发布机构名称
	 */
	private String pubOrgName;
	/**
	 * 创建人编号
	 */
	private String creatorId;
	/**
	 * 创建人姓名
	 */
	private String creatorName;
	/**
	 * 创建时间
	 */
	private String creatorTime;
	/**
	 * 是否阅读
	 */
	@TableField(exist = false)
	private String readSts;

    public AdminSmNoticeEntity() {
    }

    public String getNoticeId() {
        return this.noticeId;
    }

    public String getNoticeTitle() {
        return this.noticeTitle;
    }

    public String getNoticeLevel() {
        return this.noticeLevel;
    }

    public String getActiveDate() {
        return this.activeDate;
    }

    public String getIsTop() {
        return this.isTop;
    }

    public String getTopActiveDate() {
        return this.topActiveDate;
    }

    public String getRicheditId() {
        return this.richeditId;
    }

    public String getPubSts() {
        return this.pubSts;
    }

    public String getPubTime() {
        return this.pubTime;
    }

    public String getPubUserId() {
        return this.pubUserId;
    }

    public String getPubUserName() {
        return this.pubUserName;
    }

    public String getPubOrgId() {
        return this.pubOrgId;
    }

    public String getPubOrgName() {
        return this.pubOrgName;
    }

    public String getCreatorId() {
        return this.creatorId;
    }

    public String getCreatorName() {
        return this.creatorName;
    }

    public String getCreatorTime() {
        return this.creatorTime;
    }

    public String getReadSts() {
        return this.readSts;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public void setNoticeLevel(String noticeLevel) {
        this.noticeLevel = noticeLevel;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public void setTopActiveDate(String topActiveDate) {
        this.topActiveDate = topActiveDate;
    }

    public void setRicheditId(String richeditId) {
        this.richeditId = richeditId;
    }

    public void setPubSts(String pubSts) {
        this.pubSts = pubSts;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public void setPubUserId(String pubUserId) {
        this.pubUserId = pubUserId;
    }

    public void setPubUserName(String pubUserName) {
        this.pubUserName = pubUserName;
    }

    public void setPubOrgId(String pubOrgId) {
        this.pubOrgId = pubOrgId;
    }

    public void setPubOrgName(String pubOrgName) {
        this.pubOrgName = pubOrgName;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public void setCreatorTime(String creatorTime) {
        this.creatorTime = creatorTime;
    }

    public void setReadSts(String readSts) {
        this.readSts = readSts;
    }
}
