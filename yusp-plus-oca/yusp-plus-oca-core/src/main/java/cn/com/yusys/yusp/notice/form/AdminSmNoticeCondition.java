package cn.com.yusys.yusp.notice.form;

import java.io.Serial;
import java.io.Serializable;

/**
 * 条件查询时接收参数的自定义实体类
 * @author zhangyt12
 * @date 2021/6/24 15:55
 */
public class AdminSmNoticeCondition implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 公告标题
     */
    private String noticeTitle;
    /**
     * 公告重要程度
     */
    private String noticeLevel;
    /**
     * 发布状态（状态：对应字典项=NORM_STS C：未发布O：已发布）
     */
    private String pubSts;
    /**
     * 是否阅读
     */
    private String readSts;
    /**
     * 模糊查询关键字
     */
    private String keyWord;
    /**
     * 分页查询条件 page
     */
    private int page;
    /**
     * 分页查询条件 size
     */
    private int size;

    public AdminSmNoticeCondition() {
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

    public String getPubSts() {
        return pubSts;
    }

    public void setPubSts(String pubSts) {
        this.pubSts = pubSts;
    }

    public String getReadSts() {
        return readSts;
    }

    public void setReadSts(String readSts) {
        this.readSts = readSts;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
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
