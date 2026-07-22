package cn.com.yusys.yusp.oca.domain.form;

/**
 * 日志表单
 * @author zhanyq
 * @date 2021-06-30 17:23
 */
public class AdminSmLogForm {
    /**
     * 日志类型
     */
    private String logTypeId;
    /**
     * 操作用户
     */
    private String userName;
    /**
     * 操作对象ID
     */
    private String operObjId;
    /**
     * 操作时间从
     */
    private String beginTime;
    /**
     * 操作时间至
     */
    private String endTime;
    /**
     * 分页页号
     */
    private int page;
    /**
     * 分页大小
     */
    private int size;

    public AdminSmLogForm() {
    }

    public String getLogTypeId() {
        return this.logTypeId;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getOperObjId() {
        return this.operObjId;
    }

    public String getBeginTime() {
        return this.beginTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public int getPage() {
        return this.page;
    }

    public int getSize() {
        return this.size;
    }

    public void setLogTypeId(String logTypeId) {
        this.logTypeId = logTypeId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setOperObjId(String operObjId) {
        this.operObjId = operObjId;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }


}
