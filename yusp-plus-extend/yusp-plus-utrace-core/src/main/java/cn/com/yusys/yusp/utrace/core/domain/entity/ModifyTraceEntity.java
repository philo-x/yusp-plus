package cn.com.yusys.yusp.utrace.core.domain.entity;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

/**
 * 小U留痕记录表
 *
 * @author zhanyq
 * @date 2021-05-17 15:13:18
 */
@TableName("s_modify_trace")
public class ModifyTraceEntity extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId

    private String seqid;
    /**
     * 操作用户ID
     */
    private String usrId;
    /**
     * 菜单ID
     */
    @JsonProperty("mMenuId")
    private String mMenuId;
    /**
     * 数据主键
     */
    @JsonProperty("mPkV")
    private String mPkV;
    /**
     * 机构ID
     */
    private String orgId;
    /**
     * 表单字段ID
     */
    @JsonProperty("mFieldId")
    private String mFieldId;
    /**
     * 表单字段名称
     */
    @JsonProperty("mFieldNm")
    private String mFieldNm;
    /**
     * 字段原值
     */
    @JsonProperty("mOldV")
    private String mOldV;
    /**
     * 字段原值描述
     */
    @JsonProperty("mOldDispV")
    private String mOldDispV;
    /**
     * 字段新值
     */
    @JsonProperty("mNewV")
    private String mNewV;
    /**
     * 字段新值描述
     */
    @JsonProperty("mNewDispV")
    private String mNewDispV;
    /**
     * 记录时间
     */
    @JsonProperty("mDatetime")
    private String mDatetime;


    private String userName;


    public ModifyTraceEntity() {
    }

    public String getSeqid() {
        return seqid;
    }

    public void setSeqid(String seqid) {
        this.seqid = seqid;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getmMenuId() {
        return mMenuId;
    }

    public void setmMenuId(String mMenuId) {
        this.mMenuId = mMenuId;
    }

    public String getmPkV() {
        return mPkV;
    }

    public void setmPkV(String mPkV) {
        this.mPkV = mPkV;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getmFieldId() {
        return mFieldId;
    }

    public void setmFieldId(String mFieldId) {
        this.mFieldId = mFieldId;
    }

    public String getmFieldNm() {
        return mFieldNm;
    }

    public void setmFieldNm(String mFieldNm) {
        this.mFieldNm = mFieldNm;
    }

    public String getmOldV() {
        return mOldV;
    }

    public void setmOldV(String mOldV) {
        this.mOldV = mOldV;
    }

    public String getmOldDispV() {
        return mOldDispV;
    }

    public void setmOldDispV(String mOldDispV) {
        this.mOldDispV = mOldDispV;
    }

    public String getmNewV() {
        return mNewV;
    }

    public void setmNewV(String mNewV) {
        this.mNewV = mNewV;
    }

    public String getmNewDispV() {
        return mNewDispV;
    }

    public void setmNewDispV(String mNewDispV) {
        this.mNewDispV = mNewDispV;
    }

    public String getmDatetime() {
        return mDatetime;
    }

    public void setmDatetime(String mDatetime) {
        this.mDatetime = mDatetime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
