package cn.com.yusys.yusp.oca.domain.vo;

/**
 * @author xufy1@yusys.com.cn
 * @desc 控制点左侧列表树
 * @date 2020-12-09 14:20
 */
public class AdminSmResContrTreeVo {
    /**
     * 节点名称
     */
    String nodeName;

    /**
     * 父ID
     */
    String upTreeId;

    /**
     * 节点类型
     */
    String nodeType;

    /**
     * 节点名称
     */
    String nodeId;

    public AdminSmResContrTreeVo(String nodeName, String upTreeId, String nodeType, String nodeId) {
        this.nodeName = nodeName;
        this.upTreeId = upTreeId;
        this.nodeType = nodeType;
        this.nodeId = nodeId;
    }

    public AdminSmResContrTreeVo() {
    }

    public String getNodeName() {
        return this.nodeName;
    }

    public String getUpTreeId() {
        return this.upTreeId;
    }

    public String getNodeType() {
        return this.nodeType;
    }

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setUpTreeId(String upTreeId) {
        this.upTreeId = upTreeId;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }


}
