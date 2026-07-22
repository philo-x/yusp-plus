package cn.com.yusys.yusp.oca.domain.vo;

import cn.com.yusys.yusp.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.List;
/**
 * 业务功能树
 *
 * @author zhangyt12
 * @date 2020-11-27 14:57:08
 */
public class AdminSmContrTreeVo extends BaseEntity {

    @TableField(exist = false)
    private String upTreeId;

    @TableField(exist = false)
    private String nodeId;

    @TableField(exist = false)
    private String nodeName;

    @TableField(exist = false)
    private List<AdminSmContrTreeVo> children;

    public AdminSmContrTreeVo() {
    }

    public String getUpTreeId() {
        return this.upTreeId;
    }

    public String getNodeId() {
        return this.nodeId;
    }

    public String getNodeName() {
        return this.nodeName;
    }

    public List<AdminSmContrTreeVo> getChildren() {
        return this.children;
    }

    public void setUpTreeId(String upTreeId) {
        this.upTreeId = upTreeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setChildren(List<AdminSmContrTreeVo> children) {
        this.children = children;
    }
}
