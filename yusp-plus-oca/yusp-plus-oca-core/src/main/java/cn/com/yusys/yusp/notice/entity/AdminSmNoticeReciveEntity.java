package cn.com.yusys.yusp.notice.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统公告表接收对象表对应实体类
 * @author zhangyt12
 * @date 2021/6/24 15:43
 */
@TableName("admin_sm_notice_recive")
public class AdminSmNoticeReciveEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
	/**
	 * 记录编号
	 */
	@TableId
	private String reciveId;
	/**
	 * 公告编号
	 */
	private String noticeId;
	/**
	 * 对象类型
	 */
	private String reciveType;
	/**
	 * 对象记录编号
	 */
	private String reciveOgjId;
	/**
	 * 能够访问的角色名称
	 */
	@TableField(exist = false)
	private String reciveRoleId;

	public AdminSmNoticeReciveEntity() {}

	public AdminSmNoticeReciveEntity(String noticeId, String reciveOgjId, String reciveRoleId) {
		this.noticeId = noticeId;
		this.reciveOgjId = reciveOgjId;
		this.reciveRoleId = reciveRoleId;
	}

	public AdminSmNoticeReciveEntity(String reciveId, String noticeId, String reciveType, String reciveOgjId) {
		this.reciveId = reciveId;
		this.noticeId = noticeId;
		this.reciveType = reciveType;
		this.reciveOgjId = reciveOgjId;
	}

	public String getReciveId() {
		return this.reciveId;
	}

	public String getNoticeId() {
		return this.noticeId;
	}

	public String getReciveType() {
		return this.reciveType;
	}

	public String getReciveOgjId() {
		return this.reciveOgjId;
	}

	public String getReciveRoleId() {
		return this.reciveRoleId;
	}

	public void setReciveId(String reciveId) {
		this.reciveId = reciveId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public void setReciveType(String reciveType) {
		this.reciveType = reciveType;
	}

	public void setReciveOgjId(String reciveOgjId) {
		this.reciveOgjId = reciveOgjId;
	}

	public void setReciveRoleId(String reciveRoleId) {
		this.reciveRoleId = reciveRoleId;
	}
}
