package cn.com.yusys.yusp.notice.form;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 接收前端发送请求时接收参数
 * @author zhangyt12
 * @date 2021/6/24 15:57
 */
public class AdminSmNoticeForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 公告编号
     */
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
     * 富文本表关联 id
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
     * 富文本内容
     */
    private String context;
    /**
     * 发布机构对象 id
     */
    private String reciveOrgId;
    /**
     * 发布角色对象 id
     */
    private String reciveRoleId;
    /**
     * 公告附件
     */
    private List<AdminSmRicheditFileInfoForm> fileInfoFormList;
    /**
     * 分页查询条件 page
     */
    private int page;
    /**
     * 分页查询条件 size
     */
    private int size;

    public AdminSmNoticeForm() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeLevel() {
        return noticeLevel;
    }

    public void setNoticeLevel(String noticeLevel) {
        this.noticeLevel = noticeLevel;
    }

    public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getTopActiveDate() {
        return topActiveDate;
    }

    public void setTopActiveDate(String topActiveDate) {
        this.topActiveDate = topActiveDate;
    }

    public String getRicheditId() {
        return richeditId;
    }

    public void setRicheditId(String richeditId) {
        this.richeditId = richeditId;
    }

    public String getPubSts() {
        return pubSts;
    }

    public void setPubSts(String pubSts) {
        this.pubSts = pubSts;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getPubUserId() {
        return pubUserId;
    }

    public void setPubUserId(String pubUserId) {
        this.pubUserId = pubUserId;
    }

    public String getPubUserName() {
        return pubUserName;
    }

    public void setPubUserName(String pubUserName) {
        this.pubUserName = pubUserName;
    }

    public String getPubOrgId() {
        return pubOrgId;
    }

    public void setPubOrgId(String pubOrgId) {
        this.pubOrgId = pubOrgId;
    }

    public String getPubOrgName() {
        return pubOrgName;
    }

    public void setPubOrgName(String pubOrgName) {
        this.pubOrgName = pubOrgName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorTime() {
        return creatorTime;
    }

    public void setCreatorTime(String creatorTime) {
        this.creatorTime = creatorTime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getReciveOrgId() {
        return reciveOrgId;
    }

    public void setReciveOrgId(String reciveOrgId) {
        this.reciveOrgId = reciveOrgId;
    }

    public String getReciveRoleId() {
        return reciveRoleId;
    }

    public void setReciveRoleId(String reciveRoleId) {
        this.reciveRoleId = reciveRoleId;
    }

    public List<AdminSmRicheditFileInfoForm> getFileInfoFormList() {
        return fileInfoFormList;
    }

    public void setFileInfoFormList(List<AdminSmRicheditFileInfoForm> fileInfoFormList) {
        this.fileInfoFormList = fileInfoFormList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
