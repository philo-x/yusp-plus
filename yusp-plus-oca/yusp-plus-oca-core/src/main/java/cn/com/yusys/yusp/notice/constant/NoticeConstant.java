package cn.com.yusys.yusp.notice.constant;

/**
 * 公告业务中使用的常量
 * @author zhangyt12
 * @date 2021/6/24 14:06
 */
public class NoticeConstant {
    /**
     * 富文本存储数据时的业务类型
     */
    public static final String RICHEDIT_REL_MOD = "NOTICE";
    /**
     * 公告发布对象，角色
     */
    public static final String RECIVE_TYPE_ROLE = "ROLE";
    /**
     * 表示所有角色权限或所有部门权限都可以查看该公告
     */
    public static final String RECIVE_OGJ_ID_NA = "NA";
    /**
     * 公告发布对象，机构
     */
    public static final String RECIVE_TYPE_ORG = "ORG";
    /**
     * 权限表新增判断
     */
    public static final String RECIVE_CREATE = "CREATE";
    /**
     * 权限表修改判断
     */
    public static final String RECIVE_UPDATE = "UPDATE";
    /**
     * 公告对象字符串分隔符，逗号
     */
    public static final String SEGMENTATION_COMMA = ",";
    /**
     * 公告发布状态，已发布
     */
    public static final String PUB_STS_O = "O";
    /**
     * 公告发布状态，未发布
     */
    public static final String PUB_STS_C = "C";
}

