package cn.com.yusys.yusp.oca.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.List;
import java.util.Objects;

/**
 * 授权树实体类
 *
 * @author zhanyq
 * @date 2021-06-24 16:23
 */
public class AdminSmAuthTreeVo {

    /**
     * 授权记录ID
     */
    private String authRecoId;

    /**
     * 授权资源ID
     */
    private String authresId;

    /**
     * 节点父ID
     */
    private String upTreeId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 授权类型
     */
    private String authresType;

    /**
     * 排序
     */
    private int orderColumn;

    /**
     * 1、表示该对象已有该权限，0 表示该对象没有分配该权限
     */
    private int state;

    @TableField(exist = false)
    private List<AdminSmAuthTreeVo> children;

    public AdminSmAuthTreeVo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminSmAuthTreeVo that = (AdminSmAuthTreeVo) o;
        return Objects.equals(authresId, that.authresId);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getAuthRecoId() {
        return this.authRecoId;
    }

    public String getAuthresId() {
        return this.authresId;
    }

    public String getUpTreeId() {
        return this.upTreeId;
    }

    public String getNodeName() {
        return this.nodeName;
    }

    public String getAuthresType() {
        return this.authresType;
    }

    public int getOrderColumn() {
        return this.orderColumn;
    }

    public int getState() {
        return this.state;
    }

    public List<AdminSmAuthTreeVo> getChildren() {
        return this.children;
    }

    public void setAuthRecoId(String authRecoId) {
        this.authRecoId = authRecoId;
    }

    public void setAuthresId(String authresId) {
        this.authresId = authresId;
    }

    public void setUpTreeId(String upTreeId) {
        this.upTreeId = upTreeId;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setAuthresType(String authresType) {
        this.authresType = authresType;
    }

    public void setOrderColumn(int orderColumn) {
        this.orderColumn = orderColumn;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setChildren(List<AdminSmAuthTreeVo> children) {
        this.children = children;
    }

}
