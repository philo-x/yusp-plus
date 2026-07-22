package cn.com.yusys.yusp.oca.passwordstrategy;

import cn.com.yusys.yusp.commons.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 密码策略工厂
 *
 * @author wujp4
 * @date 2020-11-26 14:46:26
 */
public class PasswordFactory {

    /**
     * 策略缓存
     */
    private static Map<String, Handler> strategyMap = new HashMap();

    /**
     * 错误码缓存
     */
    private static Map<String, String> errorCodeMap = new HashMap();

    /**
     * 获取策略
     *
     * @param name 策略名称
     * @return 策略
     */
    public static Handler getInvokeStrategy(String name) {
        return strategyMap.get(name);
    }

    /**
     * 策略注册
     *
     * @param name    策略名称
     * @param handler 处理器
     */
    public static void register(String name, Handler handler) {
        if (StringUtils.isEmpty(name) || null == handler) {
            return;
        }
        strategyMap.put(name, handler);
    }

    /**
     * 错误码获取
     *
     * @param name 策略名称
     * @return 错误码
     */
    public static String getErrorCode(String name) {
        return errorCodeMap.get(name);
    }

    /**
     * 注册错误码到工厂
     *
     * @param name 策略名称
     * @param code 错误码
     */
    public static void registerErrorCode(String name, String code) {
        if (StringUtils.isEmpty(name) || null == code) {
            return;
        }
        errorCodeMap.put(name, code);
    }
}
