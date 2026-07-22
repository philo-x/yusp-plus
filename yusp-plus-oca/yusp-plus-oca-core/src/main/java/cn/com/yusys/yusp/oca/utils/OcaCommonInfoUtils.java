package cn.com.yusys.yusp.oca.utils;

import cn.com.yusys.yusp.commons.session.user.Client;
import cn.com.yusys.yusp.commons.session.user.User;
import cn.com.yusys.yusp.commons.session.util.SessionUtils;
import cn.com.yusys.yusp.commons.util.StringUtils;
import org.springframework.util.Assert;

/**
 * Oca系统获取共用信息
 *
 * @author yangzai
 * @since 2022/3/18
 **/
public class OcaCommonInfoUtils {

    /**
     * 获取用户所属系统ID
     * 根据Token从Session中获取
     *
     * @return 用户所属系统ID
     */
    public static String getUserSysId() {
        User userInformation = SessionUtils.getUserInformation();
        Assert.notNull(userInformation, "Cannot get userInformation from token.");

        Client logicSys = userInformation.getLogicSys();
        Assert.notNull(logicSys, "Cannot get logicSys info from token.");
        return StringUtils.isEmpty(logicSys.getId()) ? "YUSP" : logicSys.getId();
    }

}
