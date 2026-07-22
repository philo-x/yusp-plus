/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : yusp

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 30/09/2021 11:18:39
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_file_upload_info
-- ----------------------------
DROP TABLE IF EXISTS `admin_file_upload_info`;
CREATE TABLE `admin_file_upload_info`
(
    `FILE_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '唯一主键',
    `FILE_NAME`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '文件名称',
    `FILE_PATH`      varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件存储路径 ',
    `FILE_SIZE`      decimal(20, 0) NULL DEFAULT NULL COMMENT '文件大小',
    `EXT_NAME`       varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件扩展名',
    `PARENT_FOLDER`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件虚拟文件夹',
    `BUS_NO`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '业务流水号',
    `UPLOAD_TIME`    varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传时间',
    `FILE_REMARK`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `USER_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传用户Id',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    PRIMARY KEY (`FILE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上传文件信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_auth_info
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_auth_info`;
CREATE TABLE `admin_sm_auth_info`
(
    `AUTH_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '记录编号',
    `AUTH_NAME`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '认证类型名称',
    `BEAN_NAME`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实现类名称',
    `AUTH_REMARK`    varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '备注',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    PRIMARY KEY (`AUTH_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '认证信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_auth_reco
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_auth_reco`;
CREATE TABLE `admin_sm_auth_reco`
(
    `AUTH_RECO_ID`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录编号',
    `SYS_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '逻辑系统记录编号',
    `AUTHOBJ_TYPE`   varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权对象类型（R-角色，U-用户，D-部门，G-机构，OU-对象组）',
    `AUTHOBJ_ID`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权对象记录编号',
    `AUTHRES_TYPE`   varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权资源类型（M-菜单，C-控制点，D-数据权限）',
    `AUTHRES_ID`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权资源记录编号',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `MENU_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单ID',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`AUTH_RECO_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源对象授权记录表(含菜单、控制点、数据权限)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_busi_func
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_busi_func`;
CREATE TABLE `admin_sm_busi_func`
(
    `FUNC_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '记录编号',
    `MOD_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '所属功能模块编号',
    `FUNC_NAME`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '功能点名称',
    `FUNC_DESC`      varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能点描述',
    `FUNC_URL`       varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'URL链接',
    `FUNC_URL_JS`    varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'JS链接',
    `FUNC_URL_CSS`   varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'CSS链接',
    `FUNC_ORDER`     int(5) NOT NULL COMMENT '顺序',
    `FUNC_ICON`      varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`FUNC_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统业务功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_crel_stra
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_crel_stra`;
CREATE TABLE `admin_sm_crel_stra`
(
    `CREL_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '记录编号',
    `SYS_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '逻辑系统编号',
    `CREL_KEY`       varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '策略标识',
    `CREL_NAME`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '策略名称',
    `ENABLE_FLAG`    varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '是否启用 1:是 2:否',
    `CREL_DETAIL`    varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '策略明细',
    `CREL_DESCRIBE`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '策略描述',
    `ACTION_TYPE`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '执行动作1: 冻结用户 2:禁止 3：警告',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `SYS_DEFAULT`    int(1) NOT NULL DEFAULT 1 COMMENT '是否为系统生成',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`CREL_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '策略参数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_crel_stra_en_us
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_crel_stra_en_us`;
CREATE TABLE `admin_sm_crel_stra_en_us`
(
    `CREL_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'Policy ID',
    `SYS_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'Logical system ID',
    `CREL_KEY`       varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'Policy identification',
    `CREL_NAME`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Policy name',
    `ENABLE_FLAG`    varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'Enable flag 1:Yes 2:No',
    `CREL_DETAIL`    varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Policy details',
    `CREL_DESCRIBE`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Policy description',
    `ACTION_TYPE`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Execution action 1:Freeze users 2:prohibit 3：warning',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'Latest change user',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT 'Latest change time',
    `SYS_DEFAULT`    int(1) NOT NULL DEFAULT 1 COMMENT 'System generated 1:Yes 0:No',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`CREL_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Policy parameter table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_data_auth
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_data_auth`;
CREATE TABLE `admin_sm_data_auth`
(
    `AUTH_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录编号',
    `CONTR_ID`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '控制点记录编号(为*时表示默认过滤器)',
    `AUTH_TMPL_ID`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限模板编号',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`AUTH_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_data_auth_tmpl
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_data_auth_tmpl`;
CREATE TABLE `admin_sm_data_auth_tmpl`
(
    `AUTH_TMPL_ID`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '记录编号',
    `AUTH_TMPL_NAME` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '数据权限模板名',
    `SQL_STRING`     varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据权限SQL条件',
    `SQL_NAME`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'SQL占位符名称',
    `STATUS`         tinyint(4) NOT NULL DEFAULT 0 COMMENT '用于表示该数据模板有没有被控制点关联，0未关联，1关联',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `PRIORITY`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优先级,值越小优先级越高',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`AUTH_TMPL_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据权限模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_dpt
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_dpt`;
CREATE TABLE `admin_sm_dpt`
(
    `DPT_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '记录编号',
    `DPT_CODE`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门代码',
    `DPT_NAME`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
    `ORG_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '所属机构编号',
    `UP_DPT_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级部门记录编号',
    `DPT_STS`        char(1) CHARACTER SET utf8 COLLATE utf8_general_ci      NOT NULL COMMENT '状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`DPT_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_duty
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_duty`;
CREATE TABLE `admin_sm_duty`
(
    `DUTY_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '记录编号',
    `DUTY_CODE`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位代码',
    `DUTY_NAME`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位名称',
    `ORG_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '所属机构编号',
    `DUTY_REMARK`    varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `DUTY_STS`       char(1) CHARACTER SET utf8 COLLATE utf8_general_ci      NOT NULL COMMENT '状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`DUTY_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统岗位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_func_mod
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_func_mod`;
CREATE TABLE `admin_sm_func_mod`
(
    `MOD_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录编号',
    `MOD_NAME`       varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模块名称',
    `MOD_DESC`       varchar(254) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块描述',
    `IS_OUTER`       varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否外部系统',
    `IS_APP`         varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否APP功能',
    `USER_NAME`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部系统登录名',
    `PASSWORD`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部系统登录密码',
    `USER_KEY`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部系统用户变量名称',
    `PWD_KEY`        varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部系统密码变量名称',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`MOD_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统功能模块表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_instu
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_instu`;
CREATE TABLE `admin_sm_instu`
(
    `INSTU_ID`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '记录编号',
    `SYS_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '逻辑系统记录编号',
    `INSTU_CDE`      varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '金融机构代码',
    `INSTU_NAME`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '金融机构名称',
    `JOIN_DT`        date NULL DEFAULT NULL COMMENT '进入日期',
    `INSTU_ADDR`     varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
    `ZIP_CDE`        varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮编',
    `CONT_TEL`       varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
    `CONT_USR`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
    `INSTU_STS`      varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`INSTU_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '金融机构表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_log
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_log`;
CREATE TABLE `admin_sm_log`
(
    `LOG_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录编号',
    `USER_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
    `OPER_TIME`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作时间',
    `OPER_OBJ_ID`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作对象ID',
    `BEFORE_VALUE`   varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作前值',
    `AFTER_VALUE`    varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作后值',
    `OPER_FLAG`      varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作标志',
    `LOG_TYPE_ID`    varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志类型',
    `CONTENT`        varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志内容',
    `ORG_ID`         varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作者机构',
    `LOGIN_IP`       varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录IP',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    PRIMARY KEY (`LOG_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_logic_sys
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_logic_sys`;
CREATE TABLE `admin_sm_logic_sys`
(
    `SYS_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '记录编号',
    `AUTH_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '认证类型',
    `SYS_VERSION`    varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版本号',
    `SYS_NAME`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '逻辑系统名称',
    `SYS_DESC`       varchar(254) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '逻辑系统描述',
    `SYS_STS`        varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '逻辑系统状态',
    `IS_SSO`         varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否单点登录',
    `SYS_CODE`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统简称',
    `I18N_KEY`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国际化key值',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    PRIMARY KEY (`SYS_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统逻辑系统表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_login_log
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_login_log`;
CREATE TABLE `admin_sm_login_log`
(
    `LOG_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '记录编号',
    `TRADE_ID`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易全局流水',
    `LOGIN_CODE`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
    `CHNL_NO`        varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '渠道编号',
    `IP_ADDRESS`     varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端IP',
    `DEVICE_ID`      varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'MAC地址/系统唯一标识',
    `TRADE_CODE`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易码/服务名，可以是restful的URL',
    `OPER_RESULT`    varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '操作结果。0：成功，1：失败',
    `OPER_DETAIL`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记录失败原因详情',
    `OPER_DATE`      datetime(0) NOT NULL COMMENT '操作日期',
    `OPER_TIME`      timestamp(0)                                            NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '操作时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    PRIMARY KEY (`LOG_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_lookup_dict
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_lookup_dict`;
CREATE TABLE `admin_sm_lookup_dict`
(
    `LOOKUP_ITEM_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '字典项编号，默认uuid',
    `LOOKUP_CODE`         varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典类别code码',
    `LOOKUP_NAME`         varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典类别名称',
    `LOOKUP_TYPE_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '字典类别分类标识id',
    `LOOKUP_TYPE_NAME`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典类别分类标识名称',
    `UP_LOOKUP_ITEM_ID`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级字典内容编号',
    `LOOKUP_ITEM_CODE`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典代码',
    `LOOKUP_ITEM_NAME`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称',
    `LOOKUP_ITEM_COMMENT` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典备注说明',
    `LOOKUP_ITEM_ORDER`   int(5) NULL DEFAULT NULL COMMENT '字典项排序',
    `INSTU_ID`            varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '金融机构编号',
    `LAST_CHG_USR`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`         datetime(0) NOT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`LOOKUP_ITEM_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典内容表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_lookup_dict_en_us
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_lookup_dict_en_us`;
CREATE TABLE `admin_sm_lookup_dict_en_us`
(
    `LOOKUP_ITEM_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '字典项编号，默认uuid',
    `LOOKUP_CODE`         varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典类别code码',
    `LOOKUP_NAME`         varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典类别名称',
    `LOOKUP_TYPE_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '字典类别分类标识id',
    `LOOKUP_TYPE_NAME`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典类别分类标识名称',
    `UP_LOOKUP_ITEM_ID`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级字典内容编号',
    `LOOKUP_ITEM_CODE`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典代码',
    `LOOKUP_ITEM_NAME`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称',
    `LOOKUP_ITEM_COMMENT` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典备注说明',
    `LOOKUP_ITEM_ORDER`   int(5) NULL DEFAULT NULL COMMENT '字典项排序',
    `INSTU_ID`            varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '金融机构编号',
    `LAST_CHG_USR`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`         datetime(0) NOT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`LOOKUP_ITEM_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典内容表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_menu
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_menu`;
CREATE TABLE `admin_sm_menu`
(
    `MENU_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录编号',
    `SYS_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '逻辑系统记录编号',
    `FUNC_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务功能编号',
    `UP_MENU_ID`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级菜单编号',
    `MENU_NAME`      varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
    `MENU_ORDER`     int(5) NOT NULL COMMENT '顺序',
    `MENU_ICON`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
    `MENU_TIP`       varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明(菜单描述)',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `I18N_KEY`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国际化key值',
    `MENU_CLASSIFY`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci     NOT NULL DEFAULT '0' COMMENT '菜单分类，0 菜单， 1是菜单目录',
    `DELETED`        int(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除，1：删除 0：未删除',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`MENU_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_menu_en_us
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_menu_en_us`;
CREATE TABLE `admin_sm_menu_en_us`
(
    `MENU_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录编号',
    `SYS_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '逻辑系统记录编号',
    `FUNC_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务功能编号',
    `UP_MENU_ID`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级菜单编号',
    `MENU_NAME`      varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
    `MENU_ORDER`     int(5) NOT NULL COMMENT '顺序',
    `MENU_ICON`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
    `MENU_TIP`       varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明(菜单描述)',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `I18N_KEY`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国际化key值',
    `MENU_CLASSIFY`  char(1) CHARACTER SET utf8 COLLATE utf8_general_ci     NOT NULL DEFAULT '0' COMMENT '菜单分类，0 菜单， 1是菜单目录',
    `DELETED`        int(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除，1：删除 0：未删除',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`MENU_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_message
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_message`;
CREATE TABLE `admin_sm_message`
(
    `MESSAGE_ID`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '消息编号',
    `CODE`           varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '信息码',
    `MESSAGE_LEVEL`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '信息级别:success成功 info信息 warning警告 error错误',
    `MESSAGE`        varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '提示内容',
    `MESSAGE_TYPE`   varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息类别：COMINFO系统级通用提示 DBERR数据库错误提示 MODULEINFO模块提示',
    `FUNC_NAME`      varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属模块名称',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '最后修改用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最后修改时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`MESSAGE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '提示信息管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_message_en_us
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_message_en_us`;
CREATE TABLE `admin_sm_message_en_us`
(
    `MESSAGE_ID`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '消息编号',
    `CODE`           varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '信息码',
    `MESSAGE_LEVEL`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '信息级别:success成功 info信息 warning警告 error错误',
    `MESSAGE`        varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '提示内容',
    `MESSAGE_TYPE`   varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息类别：COMINFO系统级通用提示 DBERR数据库错误提示 MODULEINFO模块提示',
    `FUNC_NAME`      varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属模块名称',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '最后修改用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最后修改时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`MESSAGE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '提示信息管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_notice
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_notice`;
CREATE TABLE `admin_sm_notice`
(
    `NOTICE_ID`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告编号',
    `NOTICE_TITLE`    varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告标题',
    `NOTICE_LEVEL`    varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告重要程度',
    `ACTIVE_DATE`     varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '有效期至',
    `IS_TOP`          varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否置顶',
    `TOP_ACTIVE_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '置顶有效期',
    `RICHEDIT_ID`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告内容(存富文本表记录编号)',
    `PUB_STS`         varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布状态（状态：对应字典项=NORM_STS C：未发布O：已发布）',
    `PUB_TIME`        datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
    `PUB_USER_ID`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告发布人编号',
    `PUB_USER_NAME`   varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告发布人姓名',
    `PUB_ORG_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布机构编号',
    `PUB_ORG_NAME`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布机构名称',
    `CREATOR_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人编号',
    `CREATOR_NAME`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人姓名',
    `CREATOR_TIME`    datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
    `DATA_TENANT_ID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    `LAST_CHG_USR`    varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`     datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    PRIMARY KEY (`NOTICE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_notice_read
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_notice_read`;
CREATE TABLE `admin_sm_notice_read`
(
    `READ_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录编号',
    `NOTICE_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告编号',
    `USER_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户编号',
    `READ_TIME`      datetime(0) NULL DEFAULT NULL COMMENT '阅读时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    PRIMARY KEY (`READ_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统公告用户查阅历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_notice_recive
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_notice_recive`;
CREATE TABLE `admin_sm_notice_recive`
(
    `RECIVE_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录编号',
    `NOTICE_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告编号',
    `RECIVE_TYPE`    varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对象类型',
    `RECIVE_OGJ_ID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对象记录编号',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    PRIMARY KEY (`RECIVE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统公告表接收对象表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_org
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_org`;
CREATE TABLE `admin_sm_org`
(
    `ORG_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '记录编号',
    `INSTU_ID`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '金融机构编号',
    `ORG_CODE`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构代码',
    `ORG_NAME`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构名称',
    `UP_ORG_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级机构记录编号',
    `ORG_LEVEL`      tinyint(10) NOT NULL COMMENT '机构层级',
    `ORG_ADDR`       varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
    `ZIP_CDE`        varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮编',
    `CONT_TEL`       varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
    `CONT_USR`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
    `ORG_SEQ`        varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构序列，记录从根机构到当前机构编号，方便业务中使用机构统计数据',
    `ORG_STS`        char(1) CHARACTER SET utf8 COLLATE utf8_general_ci      NOT NULL COMMENT '状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`ORG_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统机构表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_password_log
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_password_log`;
CREATE TABLE `admin_sm_password_log`
(
    `LOG_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '记录编号',
    `PWD_UP_TIME`    datetime(0) NOT NULL COMMENT '密码修改时间',
    `PWD_UPED`       varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '被修改的密码',
    `UPDATE_USER`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改者id',
    `USER_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '用户ID',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '最近一次修改人',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最近一次修改时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`LOG_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '密码修改记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_prop
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_prop`;
CREATE TABLE `admin_sm_prop`
(
    `PROP_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '记录编号',
    `PROP_NAME`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '属性名',
    `PROP_DESC`      varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性描述',
    `PROP_VALUE`     varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '属性值',
    `PROP_REMARK`    varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `INSTU_ID`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '金融机构编号',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`PROP_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统参数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_res_contr
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_res_contr`;
CREATE TABLE `admin_sm_res_contr`
(
    `CONTR_ID`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '记录编号',
    `FUNC_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '所属业务功能编号',
    `CONTR_CODE`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '控制操作代码',
    `CONTR_NAME`     varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '控制操作名称',
    `CONTR_URL`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '控制操作URL(用于后台校验时使用)',
    `CONTR_REMARK`   varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `METHOD_TYPE`    varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求类型',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`CONTR_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统功能控制点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_res_contr_en_us
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_res_contr_en_us`;
CREATE TABLE `admin_sm_res_contr_en_us`
(
    `CONTR_ID`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'Control point ID',
    `FUNC_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'Business function ID',
    `CONTR_CODE`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Control operation code',
    `CONTR_NAME`     varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Control operation name',
    `CONTR_URL`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Control operation URL(Used for background verification)',
    `CONTR_REMARK`   varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Control point remarks',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'Latest change user',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT 'Latest change time',
    `METHOD_TYPE`    varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Request type',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`CONTR_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'System function control point Table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_richedit_file_info
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_richedit_file_info`;
CREATE TABLE `admin_sm_richedit_file_info`
(
    `FILE_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '唯一主键',
    `FILE_NAME`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
    `FILE_PATH`      varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件存储路径 ',
    `FILE_SIZE`      decimal(20, 0) NULL DEFAULT NULL COMMENT '文件大小',
    `EXT_NAME`       varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '文件扩展名',
    `PARENT_FOLDER`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件虚拟文件夹',
    `BUS_NO`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '业务流水号',
    `UPLOAD_TIME`    datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
    `FILE_REMARK`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    PRIMARY KEY (`FILE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_richedit_info
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_richedit_info`;
CREATE TABLE `admin_sm_richedit_info`
(
    `RICHEDIT_ID`    varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '富文本编号',
    `REL_MOD`        varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联业务模块（NOTICE-公告；）',
    `REL_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关联业务主表编号',
    `CONTENT`        varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文本内容',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    PRIMARY KEY (`RICHEDIT_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '富文本信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_role
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_role`;
CREATE TABLE `admin_sm_role`
(
    `ROLE_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '记录编号',
    `ROLE_CODE`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色代码',
    `ROLE_NAME`      varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
    `ORG_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属机构编号',
    `ROLE_LEVEL`     varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色层级',
    `ROLE_STS`       char(1) CHARACTER SET utf8 COLLATE utf8_general_ci      NOT NULL COMMENT '状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`ROLE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_role_en_us
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_role_en_us`;
CREATE TABLE `admin_sm_role_en_us`
(
    `ROLE_ID`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'Role ID',
    `ROLE_CODE`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Role code',
    `ROLE_NAME`      varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Role name',
    `ORG_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Organization No',
    `ROLE_LEVEL`     varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Role Hierarchy',
    `ROLE_STS`       char(1) CHARACTER SET utf8 COLLATE utf8_general_ci      NOT NULL COMMENT 'Status：Corresponding dictionary item=NORM_STS A：enable I：disabled W：To be effective',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'Latest change user',
    `LAST_CHG_DT`    datetime(0) NOT NULL COMMENT 'Latest change date',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`ROLE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'System role table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_tenant
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_tenant`;
CREATE TABLE `admin_sm_tenant`
(
    `TENANT_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '租户ID',
    `TENANT_NAME`    varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '租户名称',
    `COMPANY_NAME`   varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '租户单位名称',
    `TENANT_STS`     char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '租户状态 0:启用，1停用',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`TENANT_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_user
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_user`;
CREATE TABLE `admin_sm_user`
(
    `USER_ID`             varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '记录编号',
    `LOGIN_CODE`          varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
    `USER_NAME`           varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
    `CERT_TYPE`           varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件类型',
    `CERT_NO`             varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件号码',
    `USER_CODE`           varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '员工号',
    `DEADLINE`            datetime(0) NULL DEFAULT NULL COMMENT '有效期到',
    `ORG_ID`              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '所属机构编号',
    `DPT_ID`              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属部门编号',
    `USER_PASSWORD`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
    `USER_SEX`            varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '性别',
    `USER_BIRTHDAY`       datetime(0) NULL DEFAULT NULL COMMENT '生日',
    `USER_EMAIL`          varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
    `USER_MOBILEPHONE`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '移动电话',
    `USER_OFFICETEL`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '办公电话',
    `USER_EDUCATION`      varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学历',
    `USER_CERTIFICATE`    varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资格证书',
    `ENTRANTS_DATE`       datetime(0) NULL DEFAULT NULL COMMENT '入职日期',
    `POSITION_TIME`       datetime(0) NULL DEFAULT NULL COMMENT '任职时间',
    `FINANCIAL_JOB_TIME`  datetime(0) NULL DEFAULT NULL COMMENT '从业时间',
    `POSITION_DEGREE`     varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职级',
    `USER_AVATAR`         varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
    `OFFEN_IP`            varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '常用IP，逗号分隔',
    `USER_STS`            char(1) CHARACTER SET utf8 COLLATE utf8_general_ci      NOT NULL COMMENT '状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效',
    `LAST_LOGIN_TIME`     datetime(0) NULL DEFAULT NULL COMMENT '最近登录时间',
    `LAST_EDIT_PASS_TIME` datetime(0) NULL DEFAULT NULL COMMENT '最近一次修改密码时间',
    `LAST_CHG_USR`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`         datetime(0) NOT NULL COMMENT '最新变更时间',
    `HEAD_PORT`           varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像地址',
    `FINGER_PRINT`        varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '指纹信息',
    `VOICE_PRINT`         varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '声纹信息',
    `FACE_PRINT`          varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '面部信息',
    `GESTURE_PASSWORD`    varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手势密码',
    `DATA_TENANT_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`USER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_user_duty_rel
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_user_duty_rel`;
CREATE TABLE `admin_sm_user_duty_rel`
(
    `USER_DUTY_REL_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录编号',
    `USER_ID`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编号',
    `DUTY_ID`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位编号',
    `LAST_CHG_USR`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`      datetime(0) NOT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`USER_DUTY_REL_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_user_en_us
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_user_en_us`;
CREATE TABLE `admin_sm_user_en_us`
(
    `USER_ID`             varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'User ID',
    `LOGIN_CODE`          varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Account number',
    `USER_NAME`           varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'User name',
    `CERT_TYPE`           varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Document type',
    `CERT_NO`             varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Identification number',
    `USER_CODE`           varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Employee number',
    `DEADLINE`            datetime(0) NULL DEFAULT NULL COMMENT 'Expiration date',
    `ORG_ID`              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'Organization No',
    `DPT_ID`              varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Department No',
    `USER_PASSWORD`       varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Password',
    `USER_SEX`            varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Sex',
    `USER_BIRTHDAY`       datetime(0) NULL DEFAULT NULL COMMENT 'Birthday',
    `USER_EMAIL`          varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Mailbox',
    `USER_MOBILEPHONE`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Mobile phone',
    `USER_OFFICETEL`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Office telephone',
    `USER_EDUCATION`      varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Education',
    `USER_CERTIFICATE`    varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Qualification',
    `ENTRANTS_DATE`       datetime(0) NULL DEFAULT NULL COMMENT 'Entry date',
    `POSITION_TIME`       datetime(0) NULL DEFAULT NULL COMMENT 'Length of service',
    `FINANCIAL_JOB_TIME`  datetime(0) NULL DEFAULT NULL COMMENT 'Working time',
    `POSITION_DEGREE`     varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Rank',
    `USER_AVATAR`         varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'User profile',
    `OFFEN_IP`            varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Common IP, separated by commas',
    `USER_STS`            char(1) CHARACTER SET utf8 COLLATE utf8_general_ci      NOT NULL COMMENT 'Status：Corresponding dictionary item=NORM_STS A：enable I：disabled W：To be effective',
    `LAST_LOGIN_TIME`     datetime(0) NULL DEFAULT NULL COMMENT 'Last login time',
    `LAST_EDIT_PASS_TIME` datetime(0) NULL DEFAULT NULL COMMENT 'Last password change time',
    `LAST_CHG_USR`        varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'Latest change user',
    `LAST_CHG_DT`         datetime(0) NOT NULL COMMENT 'Latest change time',
    `HEAD_PORT`           varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Picture address',
    `FINGER_PRINT`        varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Fingerprint information',
    `VOICE_PRINT`         varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Voiceprint information',
    `FACE_PRINT`          varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Facial information',
    `GESTURE_PASSWORD`    varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Gesture code',
    `DATA_TENANT_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`USER_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'System user table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_user_mgr_org
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_user_mgr_org`;
CREATE TABLE `admin_sm_user_mgr_org`
(
    `USER_MGR_ORG_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录编号',
    `USER_ID`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编号',
    `ORG_ID`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '被授权管理机构编号',
    `LAST_CHG_USR`    varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`     datetime(0) NOT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`USER_MGR_ORG_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户授权管理机构表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_sm_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_user_role_rel`;
CREATE TABLE `admin_sm_user_role_rel`
(
    `USER_ROLE_REL_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录编号',
    `USER_ID`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编号',
    `ROLE_ID`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编号',
    `LAST_CHG_USR`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`      datetime(0) NOT NULL COMMENT '最新变更时间',
    `DATA_TENANT_ID`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    PRIMARY KEY (`USER_ROLE_REL_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message_event
-- ----------------------------
DROP TABLE IF EXISTS `message_event`;
CREATE TABLE `message_event`
(
    `EVENT_NO`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '事件唯一编号',
    `TEMPLATE_PARAM` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数',
    `CREATE_TIME`    varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
    `MESSAGE_TYPE`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类型',
    PRIMARY KEY (`EVENT_NO`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消息事件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message_pool
-- ----------------------------
DROP TABLE IF EXISTS `message_pool`;
CREATE TABLE `message_pool`
(
    `PK_NO`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
    `EVENT_NO`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '事件唯一编号',
    `CHANNEL_TYPE`  varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '适用渠道类型',
    `USER_NO`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户码',
    `CREATE_TIME`   varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建时间',
    `SEND_TIME`     varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送完成时间',
    `MESSAGE_LEVEL` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '消息等级[小先发]',
    `STATE`         varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送状态',
    `TIME_START`    varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '固定发送时间',
    `PK_HASH`       decimal(11, 0)                                         NOT NULL,
    `MESSAGE_TYPE`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `TIME_END`      varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`PK_NO`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消息池表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message_pool_his
-- ----------------------------
DROP TABLE IF EXISTS `message_pool_his`;
CREATE TABLE `message_pool_his`
(
    `PK_NO`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
    `EVENT_NO`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '事件唯一编号',
    `CHANNEL_TYPE`  varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '适用渠道类型',
    `USER_NO`       varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户码',
    `CREATE_TIME`   varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建时间',
    `SEND_TIME`     varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送完成时间',
    `MESSAGE_LEVEL` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '消息等级[小先发]',
    `STATE`         varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送状态',
    `TIME_START`    varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '固定发送开始时间',
    `PK_HASH`       decimal(11, 0)                                         NOT NULL COMMENT '任务id',
    `MESSAGE_TYPE`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类型',
    `TIME_END`      varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '固定发送结束时间',
    PRIMARY KEY (`PK_NO`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消息池历史表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message_subscribe
-- ----------------------------
DROP TABLE IF EXISTS `message_subscribe`;
CREATE TABLE `message_subscribe`
(
    `CHANNEL_TYPE`    varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '渠道类型',
    `MESSAGE_TYPE`    varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息类型',
    `SUBSCRIBE_TYPE`  varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '订阅类型[U R O G]',
    `OP_USER_NO`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后编辑人',
    `SUBSCRIBE_VALUE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订阅类型对应值',
    PRIMARY KEY (`CHANNEL_TYPE`, `MESSAGE_TYPE`, `SUBSCRIBE_TYPE`, `SUBSCRIBE_VALUE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户订阅表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message_temp
-- ----------------------------
DROP TABLE IF EXISTS `message_temp`;
CREATE TABLE `message_temp`
(
    `MESSAGE_TYPE`     varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '消息类型',
    `CHANNEL_TYPE`     varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci    NOT NULL COMMENT '适用渠道类型',
    `SEND_NUM`         decimal(2, 0)                                            NOT NULL COMMENT '异常重发次数',
    `TEMPLATE_CONTENT` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板内容',
    `EMAIL_TITLE`      varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮件/系统消息标题',
    `TIME_START`       varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送开始时间',
    `TIME_END`         varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送结束时间',
    `IS_TIME`          varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否固定时间发送',
    PRIMARY KEY (`MESSAGE_TYPE`, `CHANNEL_TYPE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消息模板配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message_type
-- ----------------------------
DROP TABLE IF EXISTS `message_type`;
CREATE TABLE `message_type`
(
    `MESSAGE_TYPE`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '消息类型',
    `MESSAGE_DESC`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '描述',
    `MESSAGE_LEVEL` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '消息等级[小先发]',
    `TEMPLATE_TYPE` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL COMMENT '模板类型[实时模板、订阅模板]',
    PRIMARY KEY (`MESSAGE_TYPE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
    `client_id`               varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端ID',
    `resource_ids`            varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源id',
    `client_secret`           varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端密码',
    `scope`                   varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限范围',
    `authorized_grant_types`  varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权类型',
    `web_server_redirect_uri` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转地址',
    `authorities`             varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限',
    `access_token_validity`   int(11) NULL DEFAULT NULL COMMENT 'token过期时间',
    `refresh_token_validity`  int(11) NULL DEFAULT NULL COMMENT 'token刷新时间',
    `additional_information`  varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '控制信息',
    `autoapprove`             varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认开启权限',
    PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户端授权明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for s_modify_trace
-- ----------------------------
DROP TABLE IF EXISTS `s_modify_trace`;
CREATE TABLE `s_modify_trace`
(
    `seqid`          varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
    `usr_id`         varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户ID',
    `m_menu_id`      varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单ID',
    `m_pk_v`         varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据主键',
    `org_id`         varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构ID',
    `m_field_id`     varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单字段ID',
    `m_field_nm`     varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单字段名称',
    `m_old_v`        varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段原值',
    `m_old_disp_v`   varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段原值描述',
    `m_new_v`        varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段新值',
    `m_new_disp_v`   varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段新值描述',
    `m_datetime`     varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记录时间',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据所属租户ID',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新变更用户',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    PRIMARY KEY (`seqid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '小U留痕记录表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `sequence_config`;
CREATE TABLE `sequence_config`
(
    `ID`             varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
    `SEQ_NAME`       varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '序列名称',
    `SEQ_ID`         varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '序列ID',
    `STARTVALUE`     int(22) NULL DEFAULT NULL COMMENT '开始值',
    `MAXIMUMVALUE`   int(22) NULL DEFAULT NULL COMMENT '最大值',
    `INCREMENTVALUE` int(9) NULL DEFAULT NULL COMMENT '自增值',
    `IS_CYCLE`       varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否循环',
    `CACHEVALUE`     int(9) NULL DEFAULT NULL COMMENT '缓存值',
    `SEQ_TEMPLET`    varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '序列模版',
    `SEQ_PLACE`      int(9) NULL DEFAULT NULL COMMENT '序列用的位数',
    `ZERO_FILL`      varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '不足位数是否用0补全',
    `SEQ_CREATE`     varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '序列是否已生成',
    `CURRENT_VALUE`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前序列值',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT '最新变更时间',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新变更用户',
    PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '序列号模版配置' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for admin_sm_tenant_en_us
-- ----------------------------
DROP TABLE IF EXISTS `admin_sm_tenant_en_us`;
CREATE TABLE `admin_sm_tenant_en_us`
(
    `TENANT_ID`      varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'TENANT ID',
    `TENANT_NAME`    varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'TENANT NAME',
    `COMPANY_NAME`   varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'COMPANY NAME',
    `TENANT_STS`     char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'TENANT STS 0:enable，1:disenabled',
    `LAST_CHG_USR`   varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Latest change user',
    `LAST_CHG_DT`    datetime(0) NULL DEFAULT NULL COMMENT 'Latest change time',
    `DATA_TENANT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'TENANT ID',
    PRIMARY KEY (`TENANT_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'Tenant management table' ROW_FORMAT = Dynamic;



ALTER TABLE admin_sm_user_mgr_org ADD INDEX mgrUserId (USER_ID);

ALTER TABLE admin_sm_user_role_rel ADD INDEX relUserRoleUserId (USER_ID);

ALTER TABLE admin_sm_user_role_rel ADD INDEX relUserRoleRoleId (ROLE_ID);

ALTER TABLE admin_sm_user_duty_rel ADD INDEX relUserDutyUserId (USER_ID);

ALTER TABLE admin_sm_auth_reco ADD INDEX recoAuthobj (authobj_id);

ALTER TABLE admin_sm_res_contr ADD INDEX controFunc (func_id);

ALTER TABLE admin_sm_data_auth ADD INDEX dataAuth (contr_id);

ALTER TABLE admin_sm_menu ADD INDEX menuFuncId (func_id);



ALTER TABLE admin_sm_user_role_rel ADD CHECKED INT DEFAULT 0;