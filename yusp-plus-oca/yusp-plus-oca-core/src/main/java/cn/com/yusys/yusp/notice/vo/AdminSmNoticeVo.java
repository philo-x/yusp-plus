package cn.com.yusys.yusp.notice.vo;

import cn.com.yusys.yusp.notice.entity.AdminSmRicheditFileInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 公告表连表查询数据对应 vo
 * @author zhangyt12
 * @date 2021/6/28 10:07
 */
public class AdminSmNoticeVo {
    /**
     * 公告编号.
     */
    private String noticeId;
    /**
     * 公告标题.
     */
    private String noticeTitle;
    /**
     * 公告重要程度.
     */
    private String noticeLevel;
    /**
     * 有效期至.
     */
    private String activeDate;
    /**
     * 是否置顶.
     */
    private String isTop;
    /**
     * 置顶有效期.
     */
    private String topActiveDate;
    /**
     * 富文本表关联 id.
     */
    private String richeditId;
    /**
     * 富文本内容.
     */
    private String context;
    /**
     * 发布状态（状态：对应字典项=NORM_STS C：未发布O：已发布）.
     */
    private String pubSts;
    /**
     * 发布时间.
     */
    private String pubTime;
    /**
     * 公告发布人姓名
     */
    private String pubUserName;
    /**
     * 发布机构名称
     */
    private String pubOrgName;
    /**
     * 创建人姓名
     */
    private String creatorName;
    /**
     * 创建时间
     */
    private String creatorTime;
    /**
     * 发布对象机构数据
     */
    private Map<String, String> reciveOrgMap;
    /**
     * 发布对象角色数据
     */
    private Map<String, String> reciveRoleMap;
    /**
     * 公告附件信息
     */
    private List<AdminSmRicheditFileInfoEntity> fileInfoFormList;

    public AdminSmNoticeVo() {
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

    public String getContext() {
        return this.context;
    }

    public String getPubSts() {
        return this.pubSts;
    }

    public String getPubTime() {
        return this.pubTime;
    }

    public String getPubUserName() {
        return this.pubUserName;
    }

    public String getPubOrgName() {
        return this.pubOrgName;
    }

    public String getCreatorName() {
        return this.creatorName;
    }

    public String getCreatorTime() {
        return this.creatorTime;
    }

    public Map<String, String> getReciveOrgMap() {
        return this.reciveOrgMap;
    }

    public Map<String, String> getReciveRoleMap() {
        return this.reciveRoleMap;
    }

    public List<AdminSmRicheditFileInfoEntity> getFileInfoFormList() {
        return this.fileInfoFormList;
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

    public void setContext(String context) {
        this.context = context;
    }

    public void setPubSts(String pubSts) {
        this.pubSts = pubSts;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public void setPubUserName(String pubUserName) {
        this.pubUserName = pubUserName;
    }

    public void setPubOrgName(String pubOrgName) {
        this.pubOrgName = pubOrgName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public void setCreatorTime(String creatorTime) {
        this.creatorTime = creatorTime;
    }

    public void setReciveOrgMap(Map<String, String> reciveOrgMap) {
        this.reciveOrgMap = reciveOrgMap;
    }

    public void setReciveRoleMap(Map<String, String> reciveRoleMap) {
        this.reciveRoleMap = reciveRoleMap;
    }

    public void setFileInfoFormList(List<AdminSmRicheditFileInfoEntity> fileInfoFormList) {
        this.fileInfoFormList = fileInfoFormList;
    }
}
