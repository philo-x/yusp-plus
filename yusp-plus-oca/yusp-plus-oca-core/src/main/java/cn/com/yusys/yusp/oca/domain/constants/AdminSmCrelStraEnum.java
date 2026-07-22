package cn.com.yusys.yusp.oca.domain.constants;

/**
 * @类名称: AdminSmCrelStraEnum
 * @类描述: 认证策略参数对应枚举
 * @author xufy1@yusys.com.cn
 * @创建时间: 2020-11-24 17:55
 * @修改备注:
 * @修改日期 修改人员    修改原因
 * ----------  ---------  -----------------------------
 * @Version 1.0.0
 * @Copyright (c) 2018宇信科技-版权所有
 */
public enum AdminSmCrelStraEnum {

    /**
     * 认证策略批量入库失败
     */
    LOGIC_CREL_STRAL_INS_BATCH_FAIL(30100001, "认证策略批量入库失败");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    AdminSmCrelStraEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
