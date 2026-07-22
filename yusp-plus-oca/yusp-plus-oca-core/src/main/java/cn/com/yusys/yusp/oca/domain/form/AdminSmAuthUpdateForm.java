package cn.com.yusys.yusp.oca.domain.form;

import cn.com.yusys.yusp.oca.domain.vo.AdminSmAuthTreeVo;

import java.util.List;
/**
 * 修改被授权人权限所需参数
 *
 * @author zhangyt12
 * @date 2020-12-08 18:53:56
 */
public class AdminSmAuthUpdateForm {
    /**
     * 修改对象Id
     */
    private String authObjId;
    /**
     * 修改对象类型
     */
    private String authObjType;
    /**
     * 复制对象Id
     */
    private String copyForAuthObjIds;

    /**
     * 租户ID
     */
    private String dataTenantId;

    /**
     * 修改过的权限列表
     */
    private List<AdminSmAuthTreeVo> authFormList;

    public AdminSmAuthUpdateForm() {
    }

    public String getAuthObjId() {
        return this.authObjId;
    }

    public String getAuthObjType() {
        return this.authObjType;
    }

    public String getCopyForAuthObjIds() {
        return this.copyForAuthObjIds;
    }

    public List<AdminSmAuthTreeVo> getAuthFormList() {
        return this.authFormList;
    }

    public void setAuthObjId(String authObjId) {
        this.authObjId = authObjId;
    }

    public void setAuthObjType(String authObjType) {
        this.authObjType = authObjType;
    }

    public void setCopyForAuthObjIds(String copyForAuthObjIds) {
        this.copyForAuthObjIds = copyForAuthObjIds;
    }

    public void setAuthFormList(List<AdminSmAuthTreeVo> authFormList) {
        this.authFormList = authFormList;
    }

    public String getDataTenantId() {
        return dataTenantId;
    }

    public void setDataTenantId(String dataTenantId) {
        this.dataTenantId = dataTenantId;
    }
}
