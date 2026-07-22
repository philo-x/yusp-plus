package cn.com.yusys.yusp.oca.domain.constants;

/**
 * @author xufy1@yusys.com.cn
 * @desc
 * @date 2020-12-11 16:00
 */
public class Constants {


    /**
     * @author xufy1@yusys.com.cn
     * @desc
     * @date 2020-12-11 16:00
     */
    public static class AdminSmLogicSysConstance {

        /**
         * 默认账号后缀 _admin
         */
        public static final String DEFAULT_USER = "_admin";
        /**
         * 管理员用户ID
         */
        public static final String ADMIN_USER_ID = "40";
        /**
         * 默认管理员登录 2
         */
        public static final Integer DEFAULT_ROLE_LEVEL = 2;
//        /**
//         * 默认角色状态 A 正常
//         */
//        public static final String DEFAULT_ROLE_STATUS = "A";
        /**
         * 默认授权类 R 角色
         */

        public static final String DEFAULT_AUTH_OBJ_TYPE = "R";
        /**
         * 默认资源类型 M 菜单
         */
        public static final String DEFAULT_AUTH_RES_TYPE = "M";

        public static final String DEFAULT_CRELSTRA_RULE = "LOGIN_FIRST_RULE";

        /**
         * 默认登录用户名
         */
        public static final String DEFAULT_ROLE_NAME = "系统管理员";

        /**
         * 逻辑系统状态-正常
         */
        public static final String SYS_STATUS_ENABLE = "A";
        /**
         * 首页ID
         */
        public static final String HOME_PAGE_ID = "dashboard";
    }

    /**
     * @program: yusp-app-framework
     * @description: CRM系统级别静态变量
     * @author: wujiangpeng
     * @email: wujp4@yusys.com.cn
     * @create: 2020-11-02 14:17
     */
    public static class SystemUserConstance {
        /**
         * 有效策略缓存
         */
        public static final String STRATEGY_CACHE_KEY = "STRATEGY_CACHE_KEY";
//        /**
//         * 默认缓存字符串
//         */
//        public static final String DEFAULT_CACHE_STR = "0";
        /**
         * 默认缓存时间
         */
        public static final int DEFAULT_CACHE_TIME_H = 6 * 3600;
        public static final int DEFAULT_CACHE_TIME_M = 30 * 60;
        /**
         * 策略启用
         */
        public static final String ENABLE_FLAG_TRUE = "1";
//        /**
//         * 多角色登录
//         */
//        public static final String MULTI_ROLE_LOGIN = "0";
//        /**
//         * 单角色登录
//         */
//        public static final String SINGLE_ROLE_LOGIN = "1";
//        /**
//         * 逻辑系统管理员ID
//         */
//        public static final String LOGIC_SYSTEM_USER_ID = "logicSystemManager";
//        /**
//         * 超级系统管理ID
//         */
//        public static final String SUPER_SYSTEM_USER_ID = "BIPSuperRole";
//        /**
//         * 系统管理员角色
//         */
//        public static final String SYSTEM_MANAGER_ROLE = "usermanager";
//        /**
//         * 普通管理员角色
//         */
//        public static final String NORMAL_MANAGER_ROLE = "normaluser";
//        /**
//         * admin ID
//         */
//        public static final String SYSTEM_ADMIN_ID = "admin";
//        /**
//         * 用户状态 启用
//         */
//        public static final String USER_STATE_ENABLE = "true";
//        /**
//         * 用户状态冻结
//         */
//        public static final String USER_STATE_FREEZING = "1";
//        /**
//         * 用户状态禁止
//         */
//        public static final String USER_STATE_FORBIDDEN = "2";
        /**
         * 用户状态警告
         */
        public static final String USER_STATE_WARNING = "3";
        /**
         * 用户状态警告
         */
        public static final int DEFAULT_MAX_PASSWORD_ERROR = 5;
//        /**
//         * 策略名称
//         */
//        public static final String CRESTRATEGY_NAME = "crelName";
//        /**
//         * 策略消息
//         */
//        public static final String STRATEGY_MESSAGE = "strategyMessage";
        /**
         * 登录类策略key标识（前缀）
         */
        public static final String LOGIN_KEY_START = "LOGIN";
        /**
         * 登录成功前策略key标识（后缀）
         */
        public static final String BEFORE_LOGIN_KEY_END = "_B";
        /**
         * 登录成功前策略key标识（后缀）
         */
        public static final String AFTER_LOGIN_KEY_END = "_A";
//        /**
//         * 密码类策略key标识
//         */
//        public static final String PASSWORD_KEY_START = "PASSWORD";
        /**
         * 首次登陆策略
         */
        public static final String LOGIN_FIRST_RULE = "LOGIN_FIRST_TIME_A";
        /**
         * 常用IP策略
         */
        public static final String LOGIN_IP_RULE = "LOGIN_IP_CHECK_A";
//        /**
//         * 在线用户策略
//         */
//        public static final String LOGIN_USERNUM_RULE = "LOGIN_USERNUM_RULE";
        /**
         * 登陆时间段策略
         */
        public static final String LOGIN_TIME_PERIOD = "LOGIN_TIMES_B";
        /**
         * 强制密码修改策略
         */
        public static final String PASSWORD_COMPEL_CHANGE = "LOGIN_PASSWORD_COMPEL_CHANGE";
        /**
         * 密码复杂度策略
         */
        public static final String PASSWD_COMPLEX_RULE = "PASSWD_COMPLEX_RULE";
        /**
         * 密码错误次数策略
         */
        public static final String PASSWORD_WRONG = "LOGIN_PASSWORD_WRONG";
        /**
         * 密码长度策略
         */
        public static final String PASSWD_LENGTH_RULE = "PASSWD_LENGTH_RULE";
//        /**
//         * 密码重复策略
//         */
//        public static final String PASSWD_REPETCHG_RULE = "PASSWD_REPETCHG_RULE";
        /**
         * 密码重复字符策略
         */
        public static final String PASSWD_REPETNUMBER_RULE = "PASSWD_REPETNUMBER_RULE";
        /**
         * 密码连续字符策略
         */
        public static final String PASSWD_SEQUNNUMBER_RULE = "PASSWD_SEQUNNUMBER_RULE";
//        /**
//         * 多设备登录策略
//         */
//        public static final String LOGIN_TIME_MORE = "LOGIN_TIME_MORE";
        /**
         * 渠道互斥登录策略
         */
        public static final String LOGIN_SINGLE_AGENT = "LOGIN_SINGLE_AGENT_A";

        /**
         * 渠道互斥登录策略
         */
        public static final String LOGIN_PERSON_LIMIT = "LOGIN_PERSON_LIMIT";
//        /**
//         * 逻辑系统互斥登录策略
//         */
//        public static final String LOGIC_SYS_EXCLUSIVE_RULE = "LOGIC_SYS_EXCLUSIVE_RULE";
//        /**
//         * 冻结策略
//         */
//        public static final String FROZEN_RULE = "FROZEN_RULE";
//
//        public static final String FLAG_01 = "01";
//        public static final String FLAG_02 = "02";
//        public static final String FLAG_03 = "03";

        /*
         *控制点
         * */
        public static final String TRANSLATE_REDIS_KEY_CONTROL_NAME = "Control";
        public static final String TRANSLATE_REDIS_KEY_CONTROL_KEY = "allContrUrl";

        /**
         * 业务id翻译：用户名
         */
        public static final String TRANSLATE_REDIS_KEY_USER_NAME = "userName";
        /**
         * 业务id翻译：机构名
         */
        public static final String TRANSLATE_REDIS_KEY_ORG_NAME = "orgName";
        /**
         * 业务id翻译：部门名
         */
        public static final String TRANSLATE_REDIS_KEY_DPT_NAME = "dptName";
        /**
         * 业务id翻译：系统参数
         */
        public static final String TRANSLATE_REDIS_KEY_SYSTEM_PARAMS = "systemparams";
        /**
         * 业务id翻译：系统提示消息
         */
        public static final String TRANSLATE_REDIS_KEY_SYSTEM_TIP_MESSAGE = "systemtipsmessage";
        /**
         * 新增操作
         */
        public static final String ADD_OPERATION="add";
        /**
         * 修改操作
         */
        public static final String UPDATE_OPERATION="update";
        /**
         * 删除操作
         */
        public static final String DELETE_OPERATION="delete";


    }

    public static class LoginLogConstance {
        /**
         * 登录成功
         */
        public static final String SUCCESS = "0";
//        /**
//         * 登录失败
//         */
//        public static final String FAILED = "1";
    }

    public static class ExcelInitConstance {
        /**
         * 事务提交数据条数，每3000提交一次
         */
        public static final int BATCH_COUNT = 3000;
    }

    public static class AdminSmMenuConstance {

        /**
         * 菜单拖拽时，针对于关联对象的位置
         */
        public static final String MENU_ORDER_BEFORE = "before";

        /**
         * 菜单拖拽时，被拖拽菜单和关联菜单集合大小，只能为2
         */
        public static final int DRUG_MENU_SIZE = 2;
        /**
         * 公共参数管理id，超级管理员给租户授权时要屏蔽公共参数管理的模块（公共参数管理的权限只能赋给超级管理员）
         */
        public static final String PUBLIC_PARAMETER_MANAGEMENT_ID="dafdca98d69041a5aa190a6e489be358";
        /**
         * 租户管理id，超级管理员给租户授权时要屏蔽租户管理的模块（租户管理管理的权限只能赋给超级管理员）
         */
        public static final String TENANT_MANAGEMENT_ID="4b6a5712e0e244179b289908dcad50b5";
    }

    /**
     * 缓存常量
     */
    public static class CacheConstance {

        /**
         * 字典翻译字段
         */
        public static final String DICT_TRANSLATOR = "dict_translator";
//        /**
//         * yusp-app-oca:dict_translator:username
//         */
//        public static final String APPLICATION_DICT_USERNAME_KEY = ":dict_translator:userName";

//        /**
//         * yusp-app-oca:dict_translator:orgName
//         */
//        public static final String APPLICATION_DICT_ORGNAME_KEY = ":dict_translator:orgName";
    }

    /**
     * 租户管理
     */
    public static class TenantConst {
        /**
         * 活跃状态
         */
        public static final String TENANT_ENABLE = "0";

        /**
         * 注销状态
         */
        public static final String TENANT_DISENABLE = "1";

        public static final int ROOT_ROLE_LEVEL=-1;
    }
}
