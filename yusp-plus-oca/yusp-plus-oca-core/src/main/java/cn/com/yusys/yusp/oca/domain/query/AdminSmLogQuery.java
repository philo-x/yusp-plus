package cn.com.yusys.yusp.oca.domain.query;

import cn.com.yusys.yusp.common.query.PageQuery;
import cn.com.yusys.yusp.oca.domain.vo.AdminSmLogVo;

import java.util.List;

/**
 *日志的查询条件
 * @author lty
 * @date 2021/2/20　　
 */
public class AdminSmLogQuery extends PageQuery<AdminSmLogVo> {

    private String logTypeId;

    private String userName;

    private String operObjId;

    private String beginTime;

    private String endTime;

    private List<String> logIds;

    private int page;

    private int size;

    public AdminSmLogQuery() {
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

    public List<String> getLogIds() {
        return logIds;
    }

    public void setLogIds(List<String> logIds) {
        this.logIds = logIds;
    }

    @Override
    public Integer getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public Integer getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
