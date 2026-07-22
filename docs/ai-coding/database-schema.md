# GoldenDB 数据模型（JDK 17 / master）

> 自动生成自 `yusp-plus-dbinit/src/main/resources/sql/goldendb/oca-init-20250915.sql`。共 67 张表。字段名、类型、空值、默认值、键与外键均以原始 DDL 为准；不要手工修改本文件。
> 重新生成：`ruby yusp-plus-dbinit/scripts/generate_schema_doc.rb`。

## 表目录

- [admin_file_upload_info — 上传文件信息](#admin_file_upload_info)
- [admin_sm_auth_info — 认证信息表](#admin_sm_auth_info)
- [admin_sm_auth_reco — 资源对象授权记录表(含菜单、控制点、数据权限)](#admin_sm_auth_reco)
- [admin_sm_busi_func — 系统业务功能表](#admin_sm_busi_func)
- [admin_sm_crel_stra — 策略参数表](#admin_sm_crel_stra)
- [admin_sm_crel_stra_en_us — Policy parameter table](#admin_sm_crel_stra_en_us)
- [admin_sm_data_auth — 数据权限表](#admin_sm_data_auth)
- [admin_sm_data_auth_tmpl — 数据权限模板表](#admin_sm_data_auth_tmpl)
- [admin_sm_dpt — 系统部门表](#admin_sm_dpt)
- [admin_sm_duty — 系统岗位表](#admin_sm_duty)
- [admin_sm_exclude_menu — 不能授权的菜单](#admin_sm_exclude_menu)
- [admin_sm_func_mod — 系统功能模块表](#admin_sm_func_mod)
- [admin_sm_instu — 金融机构表](#admin_sm_instu)
- [admin_sm_log — 系统操作日志表](#admin_sm_log)
- [admin_sm_logic_sys — 系统逻辑系统表](#admin_sm_logic_sys)
- [admin_sm_login_log](#admin_sm_login_log)
- [admin_sm_lookup_dict — 数据字典内容表](#admin_sm_lookup_dict)
- [admin_sm_lookup_dict_en_us — 数据字典内容表](#admin_sm_lookup_dict_en_us)
- [admin_sm_menu — 系统菜单表](#admin_sm_menu)
- [admin_sm_menu_en_us — 系统菜单表](#admin_sm_menu_en_us)
- [admin_sm_message — 提示信息管理表](#admin_sm_message)
- [admin_sm_message_en_us — 提示信息管理表](#admin_sm_message_en_us)
- [admin_sm_notice — 系统公告表](#admin_sm_notice)
- [admin_sm_notice_read — 系统公告用户查阅历史表](#admin_sm_notice_read)
- [admin_sm_notice_recive — 系统公告表接收对象表](#admin_sm_notice_recive)
- [admin_sm_org — 系统机构表](#admin_sm_org)
- [admin_sm_password_log — 密码修改记录表](#admin_sm_password_log)
- [admin_sm_prop — 系统参数表](#admin_sm_prop)
- [admin_sm_res_contr — 系统功能控制点表](#admin_sm_res_contr)
- [admin_sm_res_contr_en_us — System function control point Table](#admin_sm_res_contr_en_us)
- [admin_sm_richedit_file_info](#admin_sm_richedit_file_info)
- [admin_sm_richedit_info — 富文本信息表](#admin_sm_richedit_info)
- [admin_sm_role — 系统角色表](#admin_sm_role)
- [admin_sm_role_en_us — System role table](#admin_sm_role_en_us)
- [admin_sm_tenant — 租户管理表](#admin_sm_tenant)
- [admin_sm_tenant_en_us — Tenant management table](#admin_sm_tenant_en_us)
- [admin_sm_tenant_user_rel — 租户用户关联表](#admin_sm_tenant_user_rel)
- [admin_sm_user — 系统用户表](#admin_sm_user)
- [admin_sm_user_duty_rel — 用户角色关联表](#admin_sm_user_duty_rel)
- [admin_sm_user_en_us — System user table](#admin_sm_user_en_us)
- [admin_sm_user_mgr_org — 用户授权管理机构表](#admin_sm_user_mgr_org)
- [admin_sm_user_role_rel — 用户角色关联表](#admin_sm_user_role_rel)
- [message_event — 消息事件表](#message_event)
- [message_pool — 消息池表](#message_pool)
- [message_pool_his — 消息池历史表](#message_pool_his)
- [message_subscribe — 用户订阅表](#message_subscribe)
- [message_temp — 消息模板配置表](#message_temp)
- [message_type](#message_type)
- [oauth2_registered_client](#oauth2_registered_client)
- [oca_crm — 客户关系表](#oca_crm)
- [oca_test — 认证信息表](#oca_test)
- [qrtz_blob_triggers — 自定义触发器](#qrtz_blob_triggers)
- [qrtz_calendars — 日历信息触发器](#qrtz_calendars)
- [qrtz_cron_triggers — cron触发器](#qrtz_cron_triggers)
- [qrtz_fired_triggers — 空闲的触发器](#qrtz_fired_triggers)
- [qrtz_job_details — 定时任务明细](#qrtz_job_details)
- [qrtz_locks — 通过悲观锁获取触发器](#qrtz_locks)
- [qrtz_paused_trigger_grps — 被暂停的触发器](#qrtz_paused_trigger_grps)
- [qrtz_scheduler_state — 调度器状态](#qrtz_scheduler_state)
- [qrtz_simple_triggers — 任务计划](#qrtz_simple_triggers)
- [qrtz_simprop_triggers — 用来存储存储CalendarIntervalTrigger和DailyTimeIntervalTrigger](#qrtz_simprop_triggers)
- [qrtz_triggers — 触发器和作业绑定](#qrtz_triggers)
- [schedule_job — 定时任务](#schedule_job)
- [schedule_job_log — 定时任务日志](#schedule_job_log)
- [sequence](#sequence)
- [sequence_config — 序列号模版配置](#sequence_config)
- [s_modify_trace — 小U留痕记录表](#s_modify_trace)

## admin_file_upload_info

上传文件信息

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `FILE_ID` | `varchar(32)` | NO | `—` | — | 唯一主键 |
| `FILE_NAME` | `varchar(100)` | NO | `—` | — | 文件名称 |
| `FILE_PATH` | `varchar(1000)` | NO | `—` | — | 文件存储路径  |
| `FILE_SIZE` | `decimal(20,0)` | YES | `NULL` | — | 文件大小 |
| `EXT_NAME` | `varchar(10)` | YES | `NULL` | — | 文件扩展名 |
| `PARENT_FOLDER` | `varchar(100)` | YES | `NULL` | — | 文件虚拟文件夹 |
| `BUS_NO` | `varchar(50)` | NO | `—` | — | 业务流水号 |
| `UPLOAD_TIME` | `varchar(20)` | YES | `NULL` | — | 上传时间 |
| `FILE_REMARK` | `varchar(100)` | YES | `NULL` | — | 备注 |
| `USER_ID` | `varchar(32)` | YES | `NULL` | — | 上传用户Id |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |

表级约束/索引：
- `PRIMARY KEY (FILE_ID) USING BTREE`

## admin_sm_auth_info

认证信息表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `AUTH_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `AUTH_NAME` | `varchar(100)` | YES | `NULL` | — | 认证类型名称 |
| `BEAN_NAME` | `varchar(100)` | YES | `NULL` | — | 实现类名称 |
| `AUTH_REMARK` | `varchar(1024)` | NO | `—` | — | 备注 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |

表级约束/索引：
- `PRIMARY KEY (AUTH_ID) USING BTREE`

## admin_sm_auth_reco

资源对象授权记录表(含菜单、控制点、数据权限)

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `AUTH_RECO_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `SYS_ID` | `varchar(32)` | NO | `—` | — | 逻辑系统记录编号 |
| `AUTHOBJ_TYPE` | `varchar(10)` | NO | `—` | — | 授权对象类型（R-角色，U-用户，D-部门，G-机构，OU-对象组） |
| `AUTHOBJ_ID` | `varchar(32)` | NO | `—` | — | 授权对象记录编号 |
| `AUTHRES_TYPE` | `varchar(10)` | NO | `—` | — | 授权资源类型（M-菜单，C-控制点，D-数据权限） |
| `AUTHRES_ID` | `varchar(32)` | NO | `—` | — | 授权资源记录编号 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `MENU_ID` | `varchar(32)` | YES | `NULL` | — | 菜单ID |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (AUTH_RECO_ID) USING BTREE`
- `KEY recoAuthobj (AUTHOBJ_ID)`

## admin_sm_busi_func

系统业务功能表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `FUNC_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `MOD_ID` | `varchar(32)` | NO | `—` | — | 所属功能模块编号 |
| `FUNC_NAME` | `varchar(100)` | NO | `—` | — | 功能点名称 |
| `FUNC_DESC` | `varchar(250)` | YES | `NULL` | — | 功能点描述 |
| `FUNC_URL` | `varchar(1024)` | NO | `—` | — | URL链接 |
| `FUNC_URL_JS` | `varchar(1024)` | YES | `NULL` | — | JS链接 |
| `FUNC_URL_CSS` | `varchar(1024)` | YES | `NULL` | — | CSS链接 |
| `FUNC_ORDER` | `int` | NO | `—` | — | 顺序 |
| `FUNC_ICON` | `varchar(200)` | YES | `NULL` | — | 图标 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (FUNC_ID) USING BTREE`

## admin_sm_crel_stra

策略参数表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `CREL_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `SYS_ID` | `varchar(32)` | NO | `—` | — | 逻辑系统编号 |
| `CREL_KEY` | `varchar(50)` | NO | `—` | — | 策略标识 |
| `CREL_NAME` | `varchar(100)` | NO | `—` | — | 策略名称 |
| `ENABLE_FLAG` | `varchar(1)` | NO | `—` | — | 是否启用 1:是 0:否 |
| `CREL_DETAIL` | `varchar(1024)` | YES | `NULL` | — | 策略明细 |
| `CREL_DESCRIBE` | `varchar(255)` | NO | `—` | — | 策略描述 |
| `ACTION_TYPE` | `varchar(100)` | NO | `—` | — | 执行动作1: 冻结用户 2:禁止 3：警告 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `SYS_DEFAULT` | `int` | NO | `'1'` | — | 是否为系统生成 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (CREL_ID) USING BTREE`

## admin_sm_crel_stra_en_us

Policy parameter table

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `CREL_ID` | `varchar(32)` | NO | `—` | — | Policy ID |
| `SYS_ID` | `varchar(32)` | NO | `—` | — | Logical system ID |
| `CREL_KEY` | `varchar(50)` | NO | `—` | — | Policy identification |
| `CREL_NAME` | `varchar(100)` | NO | `—` | — | Policy name |
| `ENABLE_FLAG` | `varchar(1)` | NO | `—` | — | Enable flag 1:Yes 0:No |
| `CREL_DETAIL` | `varchar(1024)` | YES | `NULL` | — | Policy details |
| `CREL_DESCRIBE` | `varchar(255)` | NO | `—` | — | Policy description |
| `ACTION_TYPE` | `varchar(100)` | NO | `—` | — | Execution action 1:Freeze users 2:prohibit 3：warning |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | Latest change user |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | Latest change time |
| `SYS_DEFAULT` | `int` | NO | `'1'` | — | System generated 1:Yes 0:No |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (CREL_ID) USING BTREE`

## admin_sm_data_auth

数据权限表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `AUTH_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `CONTR_ID` | `varchar(32)` | NO | `—` | — | 控制点记录编号(为*时表示默认过滤器) |
| `AUTH_TMPL_ID` | `varchar(32)` | NO | `—` | — | 权限模板编号 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (AUTH_ID) USING BTREE`
- `KEY dataAuth (CONTR_ID)`

## admin_sm_data_auth_tmpl

数据权限模板表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `AUTH_TMPL_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `AUTH_TMPL_NAME` | `varchar(32)` | NO | `—` | — | 数据权限模板名 |
| `SQL_STRING` | `varchar(1024)` | NO | `—` | — | 数据权限SQL条件 |
| `SQL_NAME` | `varchar(100)` | NO | `—` | — | SQL占位符名称 |
| `STATUS` | `tinyint` | NO | `'0'` | — | 用于表示该数据模板有没有被控制点关联，0未关联，1关联 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `PRIORITY` | `varchar(100)` | YES | `NULL` | — | 优先级,值越小优先级越高 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (AUTH_TMPL_ID) USING BTREE`

## admin_sm_dpt

系统部门表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `DPT_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `DPT_CODE` | `varchar(100)` | NO | `—` | — | 部门代码 |
| `DPT_NAME` | `varchar(100)` | NO | `—` | — | 部门名称 |
| `ORG_ID` | `varchar(32)` | NO | `—` | — | 所属机构编号 |
| `UP_DPT_ID` | `varchar(32)` | YES | `NULL` | — | 上级部门记录编号 |
| `DPT_STS` | `char(1)` | NO | `—` | — | 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (DPT_ID) USING BTREE`

## admin_sm_duty

系统岗位表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `DUTY_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `DUTY_CODE` | `varchar(100)` | NO | `—` | — | 岗位代码 |
| `DUTY_NAME` | `varchar(50)` | NO | `—` | — | 岗位名称 |
| `ORG_ID` | `varchar(32)` | NO | `—` | — | 所属机构编号 |
| `DUTY_REMARK` | `varchar(1024)` | YES | `NULL` | — | 备注 |
| `DUTY_STS` | `char(1)` | NO | `—` | — | 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (DUTY_ID) USING BTREE`

## admin_sm_exclude_menu

不能授权的菜单

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `MENU_ID` | `varchar(32)` | NO | `—` | — | 菜单编号 |
| `DATA_TENANT_ID` | `varchar(32)` | YES | `NULL` | — | 数据所属租户ID |

表级约束/索引：
- `PRIMARY KEY (MENU_ID) USING BTREE`

## admin_sm_func_mod

系统功能模块表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `MOD_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `MOD_NAME` | `varchar(50)` | NO | `—` | — | 模块名称 |
| `MOD_DESC` | `varchar(254)` | YES | `NULL` | — | 模块描述 |
| `IS_OUTER` | `varchar(10)` | YES | `NULL` | — | 是否外部系统 |
| `IS_APP` | `varchar(1024)` | YES | `NULL` | — | 是否APP功能 |
| `USER_NAME` | `varchar(100)` | YES | `NULL` | — | 外部系统登录名 |
| `PASSWORD` | `varchar(100)` | YES | `NULL` | — | 外部系统登录密码 |
| `USER_KEY` | `varchar(100)` | YES | `NULL` | — | 外部系统用户变量名称 |
| `PWD_KEY` | `varchar(100)` | YES | `NULL` | — | 外部系统密码变量名称 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (MOD_ID) USING BTREE`

## admin_sm_instu

金融机构表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `INSTU_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `SYS_ID` | `varchar(32)` | NO | `—` | — | 逻辑系统记录编号 |
| `INSTU_CDE` | `varchar(10)` | NO | `—` | — | 金融机构代码 |
| `INSTU_NAME` | `varchar(100)` | NO | `—` | — | 金融机构名称 |
| `JOIN_DT` | `date` | YES | `NULL` | — | 进入日期 |
| `INSTU_ADDR` | `varchar(200)` | YES | `NULL` | — | 地址 |
| `ZIP_CDE` | `varchar(6)` | YES | `NULL` | — | 邮编 |
| `CONT_TEL` | `varchar(11)` | YES | `NULL` | — | 联系电话 |
| `CONT_USR` | `varchar(50)` | YES | `NULL` | — | 联系人 |
| `INSTU_STS` | `varchar(10)` | YES | `NULL` | — | 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (INSTU_ID) USING BTREE`

## admin_sm_log

系统操作日志表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `LOG_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `USER_ID` | `varchar(32)` | NO | `—` | — | 用户ID |
| `OPER_TIME` | `varchar(20)` | YES | `NULL` | — | 操作时间 |
| `OPER_OBJ_ID` | `varchar(100)` | YES | `NULL` | — | 操作对象ID |
| `BEFORE_VALUE` | `varchar(1024)` | YES | `NULL` | — | 操作前值 |
| `AFTER_VALUE` | `varchar(1024)` | YES | `NULL` | — | 操作后值 |
| `OPER_FLAG` | `varchar(10)` | YES | `NULL` | — | 操作标志 |
| `LOG_TYPE_ID` | `varchar(10)` | YES | `NULL` | — | 日志类型 |
| `CONTENT` | `varchar(1024)` | YES | `NULL` | — | 日志内容 |
| `ORG_ID` | `varchar(100)` | YES | `NULL` | — | 操作者机构 |
| `LOGIN_IP` | `varchar(50)` | YES | `NULL` | — | 登录IP |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |

表级约束/索引：
- `PRIMARY KEY (LOG_ID) USING BTREE`

## admin_sm_logic_sys

系统逻辑系统表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `SYS_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `AUTH_ID` | `varchar(32)` | NO | `—` | — | 认证类型 |
| `SYS_VERSION` | `varchar(10)` | YES | `NULL` | — | 版本号 |
| `SYS_NAME` | `varchar(100)` | NO | `—` | — | 逻辑系统名称 |
| `SYS_DESC` | `varchar(254)` | YES | `NULL` | — | 逻辑系统描述 |
| `SYS_STS` | `varchar(10)` | NO | `—` | — | 逻辑系统状态 |
| `IS_SSO` | `varchar(10)` | YES | `NULL` | — | 是否单点登录 |
| `SYS_CODE` | `varchar(32)` | YES | `NULL` | — | 系统简称 |
| `I18N_KEY` | `varchar(32)` | YES | `NULL` | — | 国际化key值 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |

表级约束/索引：
- `PRIMARY KEY (SYS_ID) USING BTREE`

## admin_sm_login_log

（DDL 未提供表注释）

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `LOG_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `TRADE_ID` | `varchar(100)` | NO | `—` | — | 交易全局流水 |
| `LOGIN_CODE` | `varchar(100)` | NO | `—` | — | 用户ID |
| `CHNL_NO` | `varchar(10)` | NO | `—` | — | 渠道编号 |
| `IP_ADDRESS` | `varchar(20)` | YES | `NULL` | — | 客户端IP |
| `DEVICE_ID` | `varchar(20)` | YES | `NULL` | — | MAC地址/系统唯一标识 |
| `TRADE_CODE` | `varchar(100)` | NO | `—` | — | 交易码/服务名，可以是restful的URL |
| `OPER_RESULT` | `varchar(2)` | NO | `—` | — | 操作结果。0：成功，1：失败 |
| `OPER_DETAIL` | `varchar(255)` | YES | `NULL` | — | 记录失败原因详情 |
| `OPER_DATE` | `datetime` | NO | `—` | — | 操作日期 |
| `OPER_TIME` | `timestamp` | NO | `CURRENT_TIMESTAMP` | — | 操作时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |

表级约束/索引：
- `PRIMARY KEY (LOG_ID) USING BTREE`

## admin_sm_lookup_dict

数据字典内容表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `LOOKUP_ITEM_ID` | `varchar(32)` | NO | `—` | — | 字典项编号，默认uuid |
| `LOOKUP_CODE` | `varchar(100)` | NO | `—` | — | 字典类别code码 |
| `LOOKUP_NAME` | `varchar(200)` | NO | `—` | — | 字典类别名称 |
| `LOOKUP_TYPE_ID` | `varchar(32)` | NO | `—` | — | 字典类别分类标识id |
| `LOOKUP_TYPE_NAME` | `varchar(100)` | YES | `NULL` | — | 字典类别分类标识名称 |
| `UP_LOOKUP_ITEM_ID` | `varchar(32)` | YES | `NULL` | — | 上级字典内容编号 |
| `LOOKUP_ITEM_CODE` | `varchar(100)` | NO | `—` | — | 字典代码 |
| `LOOKUP_ITEM_NAME` | `varchar(100)` | NO | `—` | — | 字典名称 |
| `LOOKUP_ITEM_COMMENT` | `varchar(150)` | YES | `NULL` | — | 字典备注说明 |
| `LOOKUP_ITEM_ORDER` | `int` | YES | `NULL` | — | 字典项排序 |
| `INSTU_ID` | `varchar(32)` | YES | `NULL` | — | 金融机构编号 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (LOOKUP_ITEM_ID) USING BTREE`

## admin_sm_lookup_dict_en_us

数据字典内容表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `LOOKUP_ITEM_ID` | `varchar(32)` | NO | `—` | — | 字典项编号，默认uuid |
| `LOOKUP_CODE` | `varchar(100)` | NO | `—` | — | 字典类别code码 |
| `LOOKUP_NAME` | `varchar(200)` | NO | `—` | — | 字典类别名称 |
| `LOOKUP_TYPE_ID` | `varchar(32)` | NO | `—` | — | 字典类别分类标识id |
| `LOOKUP_TYPE_NAME` | `varchar(100)` | YES | `NULL` | — | 字典类别分类标识名称 |
| `UP_LOOKUP_ITEM_ID` | `varchar(32)` | YES | `NULL` | — | 上级字典内容编号 |
| `LOOKUP_ITEM_CODE` | `varchar(100)` | NO | `—` | — | 字典代码 |
| `LOOKUP_ITEM_NAME` | `varchar(100)` | NO | `—` | — | 字典名称 |
| `LOOKUP_ITEM_COMMENT` | `varchar(150)` | YES | `NULL` | — | 字典备注说明 |
| `LOOKUP_ITEM_ORDER` | `int` | YES | `NULL` | — | 字典项排序 |
| `INSTU_ID` | `varchar(32)` | YES | `NULL` | — | 金融机构编号 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (LOOKUP_ITEM_ID) USING BTREE`

## admin_sm_menu

系统菜单表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `MENU_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `SYS_ID` | `varchar(32)` | NO | `—` | — | 逻辑系统记录编号 |
| `FUNC_ID` | `varchar(32)` | YES | `NULL` | — | 业务功能编号 |
| `UP_MENU_ID` | `varchar(32)` | YES | `NULL` | — | 上级菜单编号 |
| `MENU_NAME` | `varchar(50)` | NO | `—` | — | 菜单名称 |
| `MENU_ORDER` | `int` | NO | `—` | — | 顺序 |
| `MENU_ICON` | `varchar(100)` | YES | `NULL` | — | 图标 |
| `MENU_TIP` | `varchar(50)` | YES | `NULL` | — | 说明(菜单描述) |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `I18N_KEY` | `varchar(32)` | YES | `NULL` | — | 国际化key值 |
| `MENU_CLASSIFY` | `char(1)` | NO | `'0'` | — | 菜单分类，0 菜单， 1是菜单目录 |
| `DELETED` | `int` | NO | `'0'` | — | 逻辑删除，1：删除 0：未删除 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (MENU_ID) USING BTREE`
- `KEY menuFuncId (FUNC_ID)`

## admin_sm_menu_en_us

系统菜单表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `MENU_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `SYS_ID` | `varchar(32)` | NO | `—` | — | 逻辑系统记录编号 |
| `FUNC_ID` | `varchar(32)` | YES | `NULL` | — | 业务功能编号 |
| `UP_MENU_ID` | `varchar(32)` | YES | `NULL` | — | 上级菜单编号 |
| `MENU_NAME` | `varchar(50)` | NO | `—` | — | 菜单名称 |
| `MENU_ORDER` | `int` | NO | `—` | — | 顺序 |
| `MENU_ICON` | `varchar(100)` | YES | `NULL` | — | 图标 |
| `MENU_TIP` | `varchar(50)` | YES | `NULL` | — | 说明(菜单描述) |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `I18N_KEY` | `varchar(32)` | YES | `NULL` | — | 国际化key值 |
| `MENU_CLASSIFY` | `char(1)` | NO | `'0'` | — | 菜单分类，0 菜单， 1是菜单目录 |
| `DELETED` | `int` | NO | `'0'` | — | 逻辑删除，1：删除 0：未删除 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (MENU_ID) USING BTREE`

## admin_sm_message

提示信息管理表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `MESSAGE_ID` | `varchar(32)` | NO | `—` | — | 消息编号 |
| `CODE` | `varchar(20)` | NO | `—` | — | 信息码 |
| `MESSAGE_LEVEL` | `varchar(20)` | NO | `—` | — | 信息级别:success成功 info信息 warning警告 error错误 |
| `MESSAGE` | `varchar(1024)` | NO | `—` | — | 提示内容 |
| `MESSAGE_TYPE` | `varchar(20)` | YES | `NULL` | — | 消息类别：COMINFO系统级通用提示 DBERR数据库错误提示 MODULEINFO模块提示 |
| `FUNC_NAME` | `varchar(40)` | YES | `NULL` | — | 所属模块名称 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最后修改用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最后修改时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (MESSAGE_ID) USING BTREE`

## admin_sm_message_en_us

提示信息管理表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `MESSAGE_ID` | `varchar(32)` | NO | `—` | — | 消息编号 |
| `CODE` | `varchar(20)` | NO | `—` | — | 信息码 |
| `MESSAGE_LEVEL` | `varchar(20)` | NO | `—` | — | 信息级别:success成功 info信息 warning警告 error错误 |
| `MESSAGE` | `varchar(1024)` | NO | `—` | — | 提示内容 |
| `MESSAGE_TYPE` | `varchar(20)` | YES | `NULL` | — | 消息类别：COMINFO系统级通用提示 DBERR数据库错误提示 MODULEINFO模块提示 |
| `FUNC_NAME` | `varchar(40)` | YES | `NULL` | — | 所属模块名称 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最后修改用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最后修改时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (MESSAGE_ID) USING BTREE`

## admin_sm_notice

系统公告表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `NOTICE_ID` | `varchar(32)` | NO | `—` | — | 公告编号 |
| `NOTICE_TITLE` | `varchar(1024)` | YES | `NULL` | — | 公告标题 |
| `NOTICE_LEVEL` | `varchar(10)` | YES | `NULL` | — | 公告重要程度 |
| `ACTIVE_DATE` | `varchar(20)` | YES | `NULL` | — | 有效期至 |
| `IS_TOP` | `varchar(10)` | YES | `NULL` | — | 是否置顶 |
| `TOP_ACTIVE_DATE` | `varchar(20)` | YES | `NULL` | — | 置顶有效期 |
| `RICHEDIT_ID` | `varchar(32)` | YES | `NULL` | — | 公告内容(存富文本表记录编号) |
| `PUB_STS` | `varchar(10)` | YES | `NULL` | — | 发布状态（状态：对应字典项=NORM_STS C：未发布O：已发布） |
| `PUB_TIME` | `datetime` | YES | `NULL` | — | 发布时间 |
| `PUB_USER_ID` | `varchar(32)` | YES | `NULL` | — | 公告发布人编号 |
| `PUB_USER_NAME` | `varchar(50)` | YES | `NULL` | — | 公告发布人姓名 |
| `PUB_ORG_ID` | `varchar(32)` | YES | `NULL` | — | 发布机构编号 |
| `PUB_ORG_NAME` | `varchar(100)` | YES | `NULL` | — | 发布机构名称 |
| `CREATOR_ID` | `varchar(32)` | YES | `NULL` | — | 创建人编号 |
| `CREATOR_NAME` | `varchar(50)` | YES | `NULL` | — | 创建人姓名 |
| `CREATOR_TIME` | `datetime` | YES | `NULL` | — | 创建时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |

表级约束/索引：
- `PRIMARY KEY (NOTICE_ID) USING BTREE`

## admin_sm_notice_read

系统公告用户查阅历史表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `READ_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `NOTICE_ID` | `varchar(32)` | YES | `NULL` | — | 公告编号 |
| `USER_ID` | `varchar(32)` | YES | `NULL` | — | 用户编号 |
| `READ_TIME` | `datetime` | YES | `NULL` | — | 阅读时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |

表级约束/索引：
- `PRIMARY KEY (READ_ID) USING BTREE`

## admin_sm_notice_recive

系统公告表接收对象表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `RECIVE_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `NOTICE_ID` | `varchar(32)` | YES | `NULL` | — | 公告编号 |
| `RECIVE_TYPE` | `varchar(10)` | YES | `NULL` | — | 对象类型 |
| `RECIVE_OGJ_ID` | `varchar(32)` | YES | `NULL` | — | 对象记录编号 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |

表级约束/索引：
- `PRIMARY KEY (RECIVE_ID) USING BTREE`

## admin_sm_org

系统机构表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `ORG_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `INSTU_ID` | `varchar(32)` | NO | `—` | — | 金融机构编号 |
| `ORG_CODE` | `varchar(100)` | NO | `—` | — | 机构代码 |
| `ORG_NAME` | `varchar(100)` | NO | `—` | — | 机构名称 |
| `UP_ORG_ID` | `varchar(32)` | YES | `NULL` | — | 上级机构记录编号 |
| `ORG_LEVEL` | `tinyint` | NO | `—` | — | 机构层级 |
| `ORG_ADDR` | `varchar(200)` | YES | `NULL` | — | 地址 |
| `ZIP_CDE` | `varchar(6)` | YES | `NULL` | — | 邮编 |
| `CONT_TEL` | `varchar(11)` | YES | `NULL` | — | 联系电话 |
| `CONT_USR` | `varchar(50)` | YES | `NULL` | — | 联系人 |
| `ORG_SEQ` | `varchar(500)` | YES | `NULL` | — | 机构序列，记录从根机构到当前机构编号，方便业务中使用机构统计数据 |
| `ORG_STS` | `char(1)` | NO | `—` | — | 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (ORG_ID) USING BTREE`

## admin_sm_password_log

密码修改记录表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `LOG_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `PWD_UP_TIME` | `datetime` | NO | `—` | — | 密码修改时间 |
| `PWD_UPED` | `varchar(150)` | NO | `—` | — | 被修改的密码 |
| `UPDATE_USER` | `varchar(100)` | NO | `—` | — | 修改者id |
| `USER_ID` | `varchar(32)` | NO | `—` | — | 用户ID |
| `LAST_CHG_USR` | `varchar(50)` | NO | `—` | — | 最近一次修改人 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最近一次修改时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (LOG_ID) USING BTREE`

## admin_sm_prop

系统参数表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `PROP_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `PROP_NAME` | `varchar(100)` | NO | `—` | — | 属性名 |
| `PROP_DESC` | `varchar(1024)` | YES | `NULL` | — | 属性描述 |
| `PROP_VALUE` | `varchar(1024)` | NO | `—` | — | 属性值 |
| `PROP_REMARK` | `varchar(1024)` | YES | `NULL` | — | 备注 |
| `INSTU_ID` | `varchar(32)` | NO | `—` | — | 金融机构编号 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (PROP_ID) USING BTREE`

## admin_sm_res_contr

系统功能控制点表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `CONTR_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `FUNC_ID` | `varchar(32)` | NO | `—` | — | 所属业务功能编号 |
| `CONTR_CODE` | `varchar(100)` | NO | `—` | — | 控制操作代码 |
| `CONTR_NAME` | `varchar(200)` | NO | `—` | — | 控制操作名称 |
| `CONTR_URL` | `varchar(100)` | YES | `NULL` | — | 控制操作URL(用于后台校验时使用) |
| `CONTR_REMARK` | `varchar(1024)` | YES | `NULL` | — | 备注 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `METHOD_TYPE` | `varchar(20)` | YES | `NULL` | — | 请求类型 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |
| `ENCODE_CHECK` | `char(2)` | NO | `'02'` | — | 强制加密 |
| `NONCE_CHECK` | `char(2)` | NO | `'02'` | — | 强制防重 |

表级约束/索引：
- `PRIMARY KEY (CONTR_ID) USING BTREE`
- `KEY controFunc (FUNC_ID)`

## admin_sm_res_contr_en_us

System function control point Table

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `CONTR_ID` | `varchar(32)` | NO | `—` | — | Control point ID |
| `FUNC_ID` | `varchar(32)` | NO | `—` | — | Business function ID |
| `CONTR_CODE` | `varchar(100)` | NO | `—` | — | Control operation code |
| `CONTR_NAME` | `varchar(200)` | NO | `—` | — | Control operation name |
| `CONTR_URL` | `varchar(100)` | YES | `NULL` | — | Control operation URL(Used for background verification) |
| `CONTR_REMARK` | `varchar(1024)` | YES | `NULL` | — | Control point remarks |
| `ENCODE_CHECK` | `varchar(32)` | NO | `—` | — | Control encode_check |
| `NONCE_CHECK` | `varchar(32)` | NO | `—` | — | Control nonce_check |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | Latest change user |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | Latest change time |
| `METHOD_TYPE` | `varchar(20)` | YES | `NULL` | — | Request type |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (CONTR_ID) USING BTREE`

## admin_sm_richedit_file_info

（DDL 未提供表注释）

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `FILE_ID` | `varchar(32)` | NO | `—` | — | 唯一主键 |
| `FILE_NAME` | `varchar(100)` | NO | `—` | — | 文件名称 |
| `FILE_PATH` | `varchar(400)` | NO | `—` | — | 文件存储路径  |
| `FILE_SIZE` | `decimal(20,0)` | YES | `NULL` | — | 文件大小 |
| `EXT_NAME` | `varchar(32)` | NO | `—` | — | 文件扩展名 |
| `PARENT_FOLDER` | `varchar(100)` | YES | `NULL` | — | 文件虚拟文件夹 |
| `BUS_NO` | `varchar(50)` | NO | `—` | — | 业务流水号 |
| `UPLOAD_TIME` | `datetime` | YES | `NULL` | — | 上传时间 |
| `FILE_REMARK` | `varchar(100)` | YES | `NULL` | — | 备注 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |

表级约束/索引：
- `PRIMARY KEY (FILE_ID) USING BTREE`

## admin_sm_richedit_info

富文本信息表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `RICHEDIT_ID` | `varchar(32)` | NO | `—` | — | 富文本编号 |
| `REL_MOD` | `varchar(10)` | YES | `NULL` | — | 关联业务模块（NOTICE-公告；） |
| `REL_ID` | `varchar(32)` | NO | `—` | — | 关联业务主表编号 |
| `CONTENT` | `varchar(5000)` | YES | `NULL` | — | 文本内容 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |

表级约束/索引：
- `PRIMARY KEY (RICHEDIT_ID) USING BTREE`

## admin_sm_role

系统角色表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `ROLE_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `ROLE_CODE` | `varchar(100)` | NO | `—` | — | 角色代码 |
| `ROLE_NAME` | `varchar(200)` | NO | `—` | — | 角色名称 |
| `ORG_ID` | `varchar(32)` | YES | `NULL` | — | 所属机构编号 |
| `ROLE_LEVEL` | `varchar(10)` | YES | `NULL` | — | 角色层级 |
| `ROLE_STS` | `char(1)` | NO | `—` | — | 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (ROLE_ID) USING BTREE`

## admin_sm_role_en_us

System role table

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `ROLE_ID` | `varchar(32)` | NO | `—` | — | Role ID |
| `ROLE_CODE` | `varchar(100)` | NO | `—` | — | Role code |
| `ROLE_NAME` | `varchar(200)` | NO | `—` | — | Role name |
| `ORG_ID` | `varchar(32)` | YES | `NULL` | — | Organization No |
| `ROLE_LEVEL` | `varchar(10)` | YES | `NULL` | — | Role Hierarchy |
| `ROLE_STS` | `char(1)` | NO | `—` | — | Status：Corresponding dictionary item=NORM_STS A：enable I：disabled W：To be effective |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | Latest change user |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | Latest change date |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (ROLE_ID) USING BTREE`

## admin_sm_tenant

租户管理表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `TENANT_ID` | `varchar(32)` | NO | `—` | — | 租户ID |
| `TENANT_NAME` | `varchar(40)` | YES | `NULL` | — | 租户名称 |
| `COMPANY_NAME` | `varchar(100)` | YES | `NULL` | — | 租户单位名称 |
| `TENANT_STS` | `char(1)` | YES | `NULL` | — | 租户状态 0:启用，1停用 |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (TENANT_ID) USING BTREE`

## admin_sm_tenant_en_us

Tenant management table

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `TENANT_ID` | `varchar(32)` | NO | `—` | — | TENANT ID |
| `TENANT_NAME` | `varchar(40)` | YES | `NULL` | — | TENANT NAME |
| `COMPANY_NAME` | `varchar(100)` | YES | `NULL` | — | COMPANY NAME |
| `TENANT_STS` | `char(1)` | YES | `NULL` | — | TENANT STS 0:enable，1:disenabled |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | Latest change user |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | Latest change time |
| `DATA_TENANT_ID` | `varchar(32)` | YES | `NULL` | — | TENANT ID |

表级约束/索引：
- `PRIMARY KEY (TENANT_ID) USING BTREE`

## admin_sm_tenant_user_rel

租户用户关联表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `TENANT_USER_REL_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `USER_ID` | `varchar(32)` | NO | `—` | — | 用户编号 |
| `data_tenant_id` | `varchar(32)` | NO | `—` | — | 租户编号 |

表级约束/索引：
- `PRIMARY KEY (TENANT_USER_REL_ID) USING BTREE`

## admin_sm_user

系统用户表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `USER_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `LOGIN_CODE` | `varchar(18)` | NO | `—` | — | 账号 |
| `USER_NAME` | `varchar(50)` | NO | `—` | — | 姓名 |
| `CERT_TYPE` | `varchar(6)` | YES | `NULL` | — | 证件类型 |
| `CERT_NO` | `varchar(20)` | YES | `NULL` | — | 证件号码 |
| `USER_CODE` | `varchar(8)` | YES | `NULL` | — | 员工号 |
| `DEADLINE` | `datetime` | YES | `NULL` | — | 有效期到 |
| `ORG_ID` | `varchar(32)` | NO | `—` | — | 所属机构编号 |
| `DPT_ID` | `varchar(32)` | YES | `NULL` | — | 所属部门编号 |
| `USER_PASSWORD` | `varchar(100)` | NO | `—` | — | 密码 |
| `USER_SEX` | `varchar(1)` | YES | `NULL` | — | 性别 |
| `USER_BIRTHDAY` | `date` | YES | `NULL` | — | 生日 |
| `USER_EMAIL` | `varchar(50)` | YES | `NULL` | — | 邮箱 |
| `USER_MOBILEPHONE` | `varchar(11)` | YES | `NULL` | — | 移动电话 |
| `USER_OFFICETEL` | `varchar(12)` | YES | `NULL` | — | 办公电话 |
| `USER_EDUCATION` | `varchar(2)` | YES | `NULL` | — | 学历 |
| `USER_CERTIFICATE` | `varchar(200)` | YES | `NULL` | — | 资格证书 |
| `ENTRANTS_DATE` | `datetime` | YES | `NULL` | — | 入职日期 |
| `POSITION_TIME` | `datetime` | YES | `NULL` | — | 任职时间 |
| `FINANCIAL_JOB_TIME` | `datetime` | YES | `NULL` | — | 从业时间 |
| `POSITION_DEGREE` | `varchar(3)` | YES | `NULL` | — | 职级 |
| `USER_AVATAR` | `varchar(200)` | YES | `NULL` | — | 用户头像 |
| `OFFEN_IP` | `varchar(200)` | YES | `NULL` | — | 常用IP，逗号分隔 |
| `USER_STS` | `char(1)` | NO | `—` | — | 状态：对应字典项=NORM_STS A：生效 I：失效 W：待生效 |
| `LAST_LOGIN_TIME` | `datetime` | YES | `NULL` | — | 最近登录时间 |
| `LAST_EDIT_PASS_TIME` | `datetime` | YES | `NULL` | — | 最近一次修改密码时间 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `HEAD_PORT` | `varchar(200)` | YES | `NULL` | — | 头像地址 |
| `FINGER_PRINT` | `varchar(500)` | YES | `NULL` | — | 指纹信息 |
| `VOICE_PRINT` | `varchar(500)` | YES | `NULL` | — | 声纹信息 |
| `FACE_PRINT` | `varchar(500)` | YES | `NULL` | — | 面部信息 |
| `GESTURE_PASSWORD` | `varchar(500)` | YES | `NULL` | — | 手势密码 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (USER_ID) USING BTREE`

## admin_sm_user_duty_rel

用户角色关联表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `USER_DUTY_REL_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `USER_ID` | `varchar(32)` | NO | `—` | — | 用户编号 |
| `DUTY_ID` | `varchar(32)` | NO | `—` | — | 岗位编号 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (USER_DUTY_REL_ID) USING BTREE`
- `KEY relUserDutyUserId (USER_ID)`

## admin_sm_user_en_us

System user table

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `USER_ID` | `varchar(32)` | NO | `—` | — | User ID |
| `LOGIN_CODE` | `varchar(18)` | NO | `—` | — | Account number |
| `USER_NAME` | `varchar(50)` | NO | `—` | — | User name |
| `CERT_TYPE` | `varchar(6)` | YES | `NULL` | — | Document type |
| `CERT_NO` | `varchar(20)` | YES | `NULL` | — | Identification number |
| `USER_CODE` | `varchar(8)` | YES | `NULL` | — | Employee number |
| `DEADLINE` | `datetime` | YES | `NULL` | — | Expiration date |
| `ORG_ID` | `varchar(32)` | NO | `—` | — | Organization No |
| `DPT_ID` | `varchar(32)` | YES | `NULL` | — | Department No |
| `USER_PASSWORD` | `varchar(100)` | NO | `—` | — | Password |
| `USER_SEX` | `varchar(1)` | YES | `NULL` | — | Sex |
| `USER_BIRTHDAY` | `date` | YES | `NULL` | — | Birthday |
| `USER_EMAIL` | `varchar(50)` | YES | `NULL` | — | Mailbox |
| `USER_MOBILEPHONE` | `varchar(11)` | YES | `NULL` | — | Mobile phone |
| `USER_OFFICETEL` | `varchar(12)` | YES | `NULL` | — | Office telephone |
| `USER_EDUCATION` | `varchar(2)` | YES | `NULL` | — | Education |
| `USER_CERTIFICATE` | `varchar(200)` | YES | `NULL` | — | Qualification |
| `ENTRANTS_DATE` | `datetime` | YES | `NULL` | — | Entry date |
| `POSITION_TIME` | `datetime` | YES | `NULL` | — | Length of service |
| `FINANCIAL_JOB_TIME` | `datetime` | YES | `NULL` | — | Working time |
| `POSITION_DEGREE` | `varchar(3)` | YES | `NULL` | — | Rank |
| `USER_AVATAR` | `varchar(200)` | YES | `NULL` | — | User profile |
| `OFFEN_IP` | `varchar(200)` | YES | `NULL` | — | Common IP, separated by commas |
| `USER_STS` | `char(1)` | NO | `—` | — | Status：Corresponding dictionary item=NORM_STS A：enable I：disabled W：To be effective |
| `LAST_LOGIN_TIME` | `datetime` | YES | `NULL` | — | Last login time |
| `LAST_EDIT_PASS_TIME` | `datetime` | YES | `NULL` | — | Last password change time |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | Latest change user |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | Latest change time |
| `HEAD_PORT` | `varchar(200)` | YES | `NULL` | — | Picture address |
| `FINGER_PRINT` | `varchar(500)` | YES | `NULL` | — | Fingerprint information |
| `VOICE_PRINT` | `varchar(500)` | YES | `NULL` | — | Voiceprint information |
| `FACE_PRINT` | `varchar(500)` | YES | `NULL` | — | Facial information |
| `GESTURE_PASSWORD` | `varchar(500)` | YES | `NULL` | — | Gesture code |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (USER_ID) USING BTREE`

## admin_sm_user_mgr_org

用户授权管理机构表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `USER_MGR_ORG_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `USER_ID` | `varchar(32)` | NO | `—` | — | 用户编号 |
| `ORG_ID` | `varchar(32)` | NO | `—` | — | 被授权管理机构编号 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |

表级约束/索引：
- `PRIMARY KEY (USER_MGR_ORG_ID) USING BTREE`
- `KEY mgrUserId (USER_ID)`

## admin_sm_user_role_rel

用户角色关联表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `USER_ROLE_REL_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `USER_ID` | `varchar(32)` | NO | `—` | — | 用户编号 |
| `ROLE_ID` | `varchar(32)` | NO | `—` | — | 角色编号 |
| `LAST_CHG_USR` | `varchar(32)` | NO | `—` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | NO | `—` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(32)` | NO | `'1'` | — | 数据所属租户id |
| `CHECKED` | `int` | YES | `'0'` | — | — |

表级约束/索引：
- `PRIMARY KEY (USER_ROLE_REL_ID) USING BTREE`
- `KEY relUserRoleUserId (USER_ID)`
- `KEY relUserRoleRoleId (ROLE_ID)`

## message_event

消息事件表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `EVENT_NO` | `varchar(32)` | NO | `—` | — | 事件唯一编号 |
| `TEMPLATE_PARAM` | `varchar(1024)` | YES | `NULL` | — | 参数 |
| `CREATE_TIME` | `varchar(20)` | YES | `NULL` | — | 创建时间 |
| `MESSAGE_TYPE` | `varchar(32)` | NO | `—` | — | 消息类型 |

表级约束/索引：
- `PRIMARY KEY (EVENT_NO) USING BTREE`

## message_pool

消息池表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `PK_NO` | `varchar(32)` | NO | `—` | — | 主键 |
| `EVENT_NO` | `varchar(32)` | NO | `—` | — | 事件唯一编号 |
| `CHANNEL_TYPE` | `varchar(6)` | NO | `—` | — | 适用渠道类型 |
| `USER_NO` | `varchar(32)` | NO | `—` | — | 用户码 |
| `CREATE_TIME` | `varchar(20)` | NO | `—` | — | 创建时间 |
| `SEND_TIME` | `varchar(20)` | YES | `NULL` | — | 发送完成时间 |
| `MESSAGE_LEVEL` | `varchar(1)` | NO | `—` | — | 消息等级[小先发] |
| `STATE` | `varchar(1)` | YES | `NULL` | — | 发送状态 |
| `TIME_START` | `varchar(20)` | YES | `NULL` | — | 固定发送时间 |
| `PK_HASH` | `decimal(11,0)` | NO | `—` | — | — |
| `MESSAGE_TYPE` | `varchar(32)` | NO | `—` | — | — |
| `TIME_END` | `varchar(20)` | YES | `NULL` | — | 固定发送结束时间 |

表级约束/索引：
- `PRIMARY KEY (PK_NO) USING BTREE`

## message_pool_his

消息池历史表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `PK_NO` | `varchar(32)` | NO | `—` | — | 主键 |
| `EVENT_NO` | `varchar(50)` | NO | `—` | — | 事件唯一编号 |
| `CHANNEL_TYPE` | `varchar(6)` | NO | `—` | — | 适用渠道类型 |
| `USER_NO` | `varchar(32)` | NO | `—` | — | 用户码 |
| `CREATE_TIME` | `varchar(20)` | NO | `—` | — | 创建时间 |
| `SEND_TIME` | `varchar(20)` | YES | `NULL` | — | 发送完成时间 |
| `MESSAGE_LEVEL` | `varchar(1)` | NO | `—` | — | 消息等级[小先发] |
| `STATE` | `varchar(1)` | YES | `NULL` | — | 发送状态 |
| `TIME_START` | `varchar(20)` | YES | `NULL` | — | 固定发送开始时间 |
| `PK_HASH` | `decimal(11,0)` | NO | `—` | — | 任务id |
| `MESSAGE_TYPE` | `varchar(32)` | NO | `—` | — | 消息类型 |
| `TIME_END` | `varchar(20)` | YES | `NULL` | — | 固定发送结束时间 |

表级约束/索引：
- `PRIMARY KEY (PK_NO) USING BTREE`

## message_subscribe

用户订阅表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `CHANNEL_TYPE` | `varchar(6)` | NO | `—` | — | 渠道类型 |
| `MESSAGE_TYPE` | `varchar(32)` | NO | `—` | — | 消息类型 |
| `SUBSCRIBE_TYPE` | `varchar(1)` | NO | `—` | — | 订阅类型[U R O G] |
| `OP_USER_NO` | `varchar(50)` | YES | `NULL` | — | 最后编辑人 |
| `SUBSCRIBE_VALUE` | `varchar(32)` | NO | `—` | — | 订阅类型对应值 |

表级约束/索引：
- `PRIMARY KEY (CHANNEL_TYPE,MESSAGE_TYPE,SUBSCRIBE_TYPE,SUBSCRIBE_VALUE) USING BTREE`

## message_temp

消息模板配置表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `MESSAGE_TYPE` | `varchar(32)` | NO | `—` | — | 消息类型 |
| `CHANNEL_TYPE` | `varchar(6)` | NO | `—` | — | 适用渠道类型 |
| `SEND_NUM` | `decimal(2,0)` | NO | `—` | — | 异常重发次数 |
| `TEMPLATE_CONTENT` | `varchar(1024)` | NO | `—` | — | 模板内容 |
| `EMAIL_TITLE` | `varchar(200)` | YES | `NULL` | — | 邮件/系统消息标题 |
| `TIME_START` | `varchar(20)` | YES | `NULL` | — | 发送开始时间 |
| `TIME_END` | `varchar(20)` | YES | `NULL` | — | 发送结束时间 |
| `IS_TIME` | `varchar(1)` | YES | `NULL` | — | 是否固定时间发送 |

表级约束/索引：
- `PRIMARY KEY (MESSAGE_TYPE,CHANNEL_TYPE) USING BTREE`

## message_type

（DDL 未提供表注释）

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `MESSAGE_TYPE` | `varchar(32)` | NO | `—` | — | 消息类型 |
| `MESSAGE_DESC` | `varchar(100)` | NO | `—` | — | 描述 |
| `MESSAGE_LEVEL` | `varchar(1)` | NO | `—` | — | 消息等级[小先发] |
| `TEMPLATE_TYPE` | `varchar(1)` | NO | `—` | — | 模板类型[实时模板、订阅模板] |

表级约束/索引：
- `PRIMARY KEY (MESSAGE_TYPE) USING BTREE`

## oauth2_registered_client

（DDL 未提供表注释）

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `id` | `varchar(100)` | NO | `—` | — | — |
| `client_id` | `varchar(100)` | NO | `—` | — | — |
| `client_id_issued_at` | `timestamp` | NO | `CURRENT_TIMESTAMP` | — | — |
| `client_secret` | `varchar(200)` | YES | `NULL` | — | — |
| `client_secret_expires_at` | `timestamp` | YES | `NULL` | — | — |
| `client_name` | `varchar(200)` | NO | `—` | — | — |
| `client_authentication_methods` | `varchar(1000)` | NO | `—` | — | — |
| `authorization_grant_types` | `varchar(1000)` | NO | `—` | — | — |
| `redirect_uris` | `varchar(1000)` | YES | `NULL` | — | — |
| `post_logout_redirect_uris` | `varchar(1000)` | YES | `NULL` | — | — |
| `scopes` | `varchar(1000)` | NO | `—` | — | — |
| `client_settings` | `varchar(2000)` | NO | `—` | — | — |
| `token_settings` | `varchar(2000)` | NO | `—` | — | — |

表级约束/索引：
- `PRIMARY KEY (id) USING BTREE`

## oca_crm

客户关系表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `user_id` | `int` | NO | `—` | AUTO_INCREMENT | — |
| `user_name` | `varchar(200)` | YES | `NULL` | — | 客户名称 |
| `user_phone` | `varchar(20)` | YES | `NULL` | — | 联系方式 |
| `user_address` | `varchar(300)` | YES | `NULL` | — | 客户地址 |
| `user_manager` | `varchar(100)` | YES | `NULL` | — | 客户经理 |
| `user_sex` | `varchar(1)` | YES | `NULL` | — | 性别0男 1女 |
| `user_desc` | `varchar(500)` | YES | `NULL` | — | 客户备注 |
| `create_time` | `datetime` | YES | `NULL` | — | 创建时间 |
| `update_time` | `datetime` | YES | `NULL` | — | 更新时间 |
| `DATA_TENANT_ID` | `varchar(100)` | NO | `—` | — | 租户id |

表级约束/索引：
- `PRIMARY KEY (user_id)`

## oca_test

认证信息表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `AUTH_ID` | `varchar(32)` | NO | `—` | — | 记录编号 |
| `AUTH_NAME` | `varchar(100)` | YES | `NULL` | — | 认证类型名称 |
| `BEAN_NAME` | `varchar(100)` | YES | `NULL` | — | 实现类名称 |
| `AUTH_REMARK` | `varchar(1024)` | NO | `—` | — | 备注 |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |
| `DATA_TENANT_ID` | `varchar(1)` | NO | `—` | — | 租金id |

表级约束/索引：
- `PRIMARY KEY (AUTH_ID) USING BTREE`

## qrtz_blob_triggers

自定义触发器

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `SCHED_NAME` | `varchar(120)` | NO | `—` | — | 计划名称 |
| `TRIGGER_NAME` | `varchar(200)` | NO | `—` | — | 触发器名称 |
| `TRIGGER_GROUP` | `varchar(200)` | NO | `—` | — | 触发器群组 |
| `BLOB_DATA` | `blob` | YES | `—` | — | 基本信息 |

表级约束/索引：
- `PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)`
- `KEY SCHED_NAME (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)`
- `CONSTRAINT qrtz_blob_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)`

## qrtz_calendars

日历信息触发器

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `SCHED_NAME` | `varchar(120)` | NO | `—` | — | 计划名称 |
| `CALENDAR_NAME` | `varchar(200)` | NO | `—` | — | 触发器名称 |
| `CALENDAR` | `blob` | NO | `—` | — | 日历信息 |

表级约束/索引：
- `PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)`

## qrtz_cron_triggers

cron触发器

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `SCHED_NAME` | `varchar(120)` | NO | `—` | — | 计划名称 |
| `TRIGGER_NAME` | `varchar(200)` | NO | `—` | — | 触发器名称 |
| `TRIGGER_GROUP` | `varchar(200)` | NO | `—` | — | 触发器群组 |
| `CRON_EXPRESSION` | `varchar(120)` | NO | `—` | — | cron表达式 |
| `TIME_ZONE_ID` | `varchar(80)` | YES | `NULL` | — | 时区 |

表级约束/索引：
- `PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)`
- `CONSTRAINT qrtz_cron_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)`

## qrtz_fired_triggers

空闲的触发器

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `SCHED_NAME` | `varchar(120)` | NO | `—` | — | 计划名称 |
| `ENTRY_ID` | `varchar(95)` | NO | `—` | — | 组织id |
| `TRIGGER_NAME` | `varchar(200)` | NO | `—` | — | 触发器名称 |
| `TRIGGER_GROUP` | `varchar(200)` | NO | `—` | — | 触发器组 |
| `INSTANCE_NAME` | `varchar(200)` | NO | `—` | — | 实例名称 |
| `FIRED_TIME` | `bigint` | NO | `—` | — | 触发时间 |
| `SCHED_TIME` | `bigint` | NO | `—` | — | 计划时间 |
| `PRIORITY` | `int` | NO | `—` | — | 权重 |
| `STATE` | `varchar(16)` | NO | `—` | — | 状态 |
| `JOB_NAME` | `varchar(200)` | YES | `NULL` | — | 作业名称 |
| `JOB_GROUP` | `varchar(200)` | YES | `NULL` | — | 作业群组 |
| `IS_NONCONCURRENT` | `varchar(1)` | YES | `NULL` | — | 是否并行 |
| `REQUESTS_RECOVERY` | `varchar(1)` | YES | `NULL` | — | 是否要求唤醒 |

表级约束/索引：
- `PRIMARY KEY (SCHED_NAME,ENTRY_ID)`
- `KEY IDX_QRTZ_FT_TRIG_INST_NAME (SCHED_NAME,INSTANCE_NAME)`
- `KEY IDX_QRTZ_FT_INST_JOB_REQ_RCVRY (SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY)`
- `KEY IDX_QRTZ_FT_J_G (SCHED_NAME,JOB_NAME,JOB_GROUP)`
- `KEY IDX_QRTZ_FT_JG (SCHED_NAME,JOB_GROUP)`
- `KEY IDX_QRTZ_FT_T_G (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)`
- `KEY IDX_QRTZ_FT_TG (SCHED_NAME,TRIGGER_GROUP)`

## qrtz_job_details

定时任务明细

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `SCHED_NAME` | `varchar(120)` | NO | `—` | — | 调度器名称 |
| `JOB_NAME` | `varchar(200)` | NO | `—` | — | 任务名称 |
| `JOB_GROUP` | `varchar(200)` | NO | `—` | — | 任务群组 |
| `DESCRIPTION` | `varchar(250)` | YES | `NULL` | — | 调度器说明 |
| `JOB_CLASS_NAME` | `varchar(250)` | NO | `—` | — | 任务class全路径 |
| `IS_DURABLE` | `varchar(1)` | NO | `—` | — | 是否持久性任务 |
| `IS_NONCONCURRENT` | `varchar(1)` | NO | `—` | — | 是否并行 |
| `IS_UPDATE_DATA` | `varchar(1)` | NO | `—` | — | 是否更新日期 |
| `REQUESTS_RECOVERY` | `varchar(1)` | NO | `—` | — | 是否唤醒 |
| `JOB_DATA` | `blob` | YES | `—` | — | 任务数据 |

表级约束/索引：
- `PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)`
- `KEY IDX_QRTZ_J_REQ_RECOVERY (SCHED_NAME,REQUESTS_RECOVERY)`
- `KEY IDX_QRTZ_J_GRP (SCHED_NAME,JOB_GROUP)`

## qrtz_locks

通过悲观锁获取触发器

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `SCHED_NAME` | `varchar(120)` | NO | `—` | — | 计划名称 |
| `LOCK_NAME` | `varchar(40)` | NO | `—` | — | 锁名称 |

表级约束/索引：
- `PRIMARY KEY (SCHED_NAME,LOCK_NAME)`

## qrtz_paused_trigger_grps

被暂停的触发器

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `SCHED_NAME` | `varchar(120)` | NO | `—` | — | 计划名称 |
| `TRIGGER_GROUP` | `varchar(200)` | NO | `—` | — | 触发器组 |

表级约束/索引：
- `PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)`

## qrtz_scheduler_state

调度器状态

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `SCHED_NAME` | `varchar(120)` | NO | `—` | — | 调度器名称 |
| `INSTANCE_NAME` | `varchar(200)` | NO | `—` | — | 实例化时间 |
| `LAST_CHECKIN_TIME` | `bigint` | NO | `—` | — | 最后验证时间 |
| `CHECKIN_INTERVAL` | `bigint` | NO | `—` | — | 时间间隔 |

表级约束/索引：
- `PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)`

## qrtz_simple_triggers

任务计划

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `SCHED_NAME` | `varchar(120)` | NO | `—` | — | 计划名称 |
| `TRIGGER_NAME` | `varchar(200)` | NO | `—` | — | 触发器名称 |
| `TRIGGER_GROUP` | `varchar(200)` | NO | `—` | — | 触发器组 |
| `REPEAT_COUNT` | `bigint` | NO | `—` | — | 重复次数 |
| `REPEAT_INTERVAL` | `bigint` | NO | `—` | — | 重复间隔 |
| `TIMES_TRIGGERED` | `bigint` | NO | `—` | — | 触发次数 |

表级约束/索引：
- `PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)`
- `CONSTRAINT qrtz_simple_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)`

## qrtz_simprop_triggers

用来存储存储CalendarIntervalTrigger和DailyTimeIntervalTrigger

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `SCHED_NAME` | `varchar(120)` | NO | `—` | — | 计划名称 |
| `TRIGGER_NAME` | `varchar(200)` | NO | `—` | — | 触发器名称 |
| `TRIGGER_GROUP` | `varchar(200)` | NO | `—` | — | 触发器组 |
| `STR_PROP_1` | `varchar(512)` | YES | `NULL` | — | String类型的trigger的第一个参数 |
| `STR_PROP_2` | `varchar(512)` | YES | `NULL` | — | String类型的trigger的第二个参数 |
| `STR_PROP_3` | `varchar(512)` | YES | `NULL` | — | String类型的trigger的第三个参数 |
| `INT_PROP_1` | `int` | YES | `NULL` | — | int类型的trigger的第一个参数 |
| `INT_PROP_2` | `int` | YES | `NULL` | — | int类型的trigger的第二个参数 |
| `LONG_PROP_1` | `bigint` | YES | `NULL` | — | long类型的trigger的第一个参数 |
| `LONG_PROP_2` | `bigint` | YES | `NULL` | — | long类型的trigger的第二个参数 |
| `DEC_PROP_1` | `decimal(13,4)` | YES | `NULL` | — | decimal类型的trigger的第一个参数 |
| `DEC_PROP_2` | `decimal(13,4)` | YES | `NULL` | — | decimal类型的trigger的第二个参数 |
| `BOOL_PROP_1` | `varchar(1)` | YES | `NULL` | — | Boolean类型的trigger的第一个参数 |
| `BOOL_PROP_2` | `varchar(1)` | YES | `NULL` | — | Boolean类型的trigger的第二个参数 |

表级约束/索引：
- `PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)`
- `CONSTRAINT qrtz_simprop_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP) REFERENCES qrtz_triggers (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)`

## qrtz_triggers

触发器和作业绑定

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `SCHED_NAME` | `varchar(120)` | NO | `—` | — | 计划名称 |
| `TRIGGER_NAME` | `varchar(200)` | NO | `—` | — | 触发器名称 |
| `TRIGGER_GROUP` | `varchar(200)` | NO | `—` | — | 触发器群组 |
| `JOB_NAME` | `varchar(200)` | NO | `—` | — | 作业名称 |
| `JOB_GROUP` | `varchar(200)` | NO | `—` | — | 作业群组 |
| `DESCRIPTION` | `varchar(250)` | YES | `NULL` | — | 说明信息 |
| `NEXT_FIRE_TIME` | `bigint` | YES | `NULL` | — | 下次执行时间 |
| `PREV_FIRE_TIME` | `bigint` | YES | `NULL` | — | 上次执行时间 |
| `PRIORITY` | `int` | YES | `NULL` | — | 线程优先级 |
| `TRIGGER_STATE` | `varchar(16)` | NO | `—` | — | 触发状态 |
| `TRIGGER_TYPE` | `varchar(8)` | NO | `—` | — | 触发器类型 |
| `START_TIME` | `bigint` | NO | `—` | — | 开始时间 |
| `END_TIME` | `bigint` | YES | `NULL` | — | 结束时间 |
| `CALENDAR_NAME` | `varchar(200)` | YES | `NULL` | — | 日历名称 |
| `MISFIRE_INSTR` | `smallint` | YES | `NULL` | — | 失败警告 |
| `JOB_DATA` | `blob` | YES | `—` | — | 任务数据 |

表级约束/索引：
- `PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)`
- `KEY IDX_QRTZ_T_J (SCHED_NAME,JOB_NAME,JOB_GROUP)`
- `KEY IDX_QRTZ_T_JG (SCHED_NAME,JOB_GROUP)`
- `KEY IDX_QRTZ_T_C (SCHED_NAME,CALENDAR_NAME)`
- `KEY IDX_QRTZ_T_G (SCHED_NAME,TRIGGER_GROUP)`
- `KEY IDX_QRTZ_T_STATE (SCHED_NAME,TRIGGER_STATE)`
- `KEY IDX_QRTZ_T_N_STATE (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE)`
- `KEY IDX_QRTZ_T_N_G_STATE (SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE)`
- `KEY IDX_QRTZ_T_NEXT_FIRE_TIME (SCHED_NAME,NEXT_FIRE_TIME)`
- `KEY IDX_QRTZ_T_NFT_ST (SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME)`
- `KEY IDX_QRTZ_T_NFT_MISFIRE (SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME)`
- `KEY IDX_QRTZ_T_NFT_ST_MISFIRE (SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE)`
- `KEY IDX_QRTZ_T_NFT_ST_MISFIRE_GRP (SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE)`
- `CONSTRAINT qrtz_triggers_ibfk_1 FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP) REFERENCES qrtz_job_details (SCHED_NAME, JOB_NAME, JOB_GROUP)`

## schedule_job

定时任务

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `job_id` | `bigint` | NO | `—` | — | 任务id |
| `bean_name` | `varchar(200)` | YES | `NULL` | — | spring bean名称 |
| `params` | `varchar(2000)` | YES | `NULL` | — | 参数 |
| `cron_expression` | `varchar(100)` | YES | `NULL` | — | cron表达式 |
| `status` | `tinyint` | YES | `NULL` | — | 任务状态  0：正常  1：暂停 |
| `remark` | `varchar(255)` | YES | `NULL` | — | 备注 |
| `create_time` | `datetime` | YES | `NULL` | — | 创建时间 |

表级约束/索引：
- `PRIMARY KEY (job_id)`

## schedule_job_log

定时任务日志

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `log_id` | `bigint` | NO | `—` | — | 任务日志id |
| `job_id` | `bigint` | NO | `—` | — | 任务id |
| `bean_name` | `varchar(200)` | YES | `NULL` | — | spring bean名称 |
| `params` | `varchar(2000)` | YES | `NULL` | — | 参数 |
| `status` | `tinyint` | NO | `—` | — | 任务状态    0：成功    1：失败 |
| `error` | `varchar(2000)` | YES | `NULL` | — | 失败信息 |
| `times` | `int` | NO | `—` | — | 耗时(单位：毫秒) |
| `create_time` | `datetime` | YES | `NULL` | — | 创建时间 |

表级约束/索引：
- `PRIMARY KEY (log_id)`
- `KEY job_id (job_id)`

## sequence

（DDL 未提供表注释）

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `name` | `varchar(50)` | NO | `—` | — | — |
| `current_value` | `bigint` | NO | `'0'` | — | — |
| `increment` | `int` | NO | `'1'` | — | — |
| `already_next` | `char(1)` | NO | `'0'` | — | — |

表级约束/索引：
- `PRIMARY KEY (name)`

## sequence_config

序列号模版配置

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `ID` | `varchar(32)` | NO | `—` | — | 主键ID |
| `SEQ_NAME` | `varchar(50)` | YES | `NULL` | — | 序列名称 |
| `SEQ_ID` | `varchar(50)` | YES | `NULL` | — | 序列ID |
| `STARTVALUE` | `int` | YES | `NULL` | — | 开始值 |
| `MAXIMUMVALUE` | `int` | YES | `NULL` | — | 最大值 |
| `INCREMENTVALUE` | `int` | YES | `NULL` | — | 自增值 |
| `IS_CYCLE` | `varchar(2)` | YES | `NULL` | — | 是否循环 |
| `CACHEVALUE` | `int` | YES | `NULL` | — | 缓存值 |
| `SEQ_TEMPLET` | `varchar(200)` | YES | `NULL` | — | 序列模版 |
| `SEQ_PLACE` | `int` | YES | `NULL` | — | 序列用的位数 |
| `ZERO_FILL` | `varchar(2)` | YES | `NULL` | — | 不足位数是否用0补全 |
| `SEQ_CREATE` | `varchar(2)` | YES | `NULL` | — | 序列是否已生成 |
| `CURRENT_VALUE` | `varchar(20)` | YES | `NULL` | — | 当前序列值 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |

表级约束/索引：
- `PRIMARY KEY (ID) USING BTREE`

## s_modify_trace

小U留痕记录表

| 字段 | 类型 | 可空 | 默认值 | 列约束 | 说明 |
|---|---|:---:|---|---|---|
| `seqid` | `varchar(32)` | NO | `—` | — | 主键 |
| `usr_id` | `varchar(20)` | YES | `NULL` | — | 操作用户ID |
| `m_menu_id` | `varchar(32)` | YES | `NULL` | — | 菜单ID |
| `m_pk_v` | `varchar(250)` | YES | `NULL` | — | 数据主键 |
| `org_id` | `varchar(20)` | YES | `NULL` | — | 机构ID |
| `m_field_id` | `varchar(50)` | YES | `NULL` | — | 表单字段ID |
| `m_field_nm` | `varchar(50)` | YES | `NULL` | — | 表单字段名称 |
| `m_old_v` | `varchar(1000)` | YES | `NULL` | — | 字段原值 |
| `m_old_disp_v` | `varchar(1000)` | YES | `NULL` | — | 字段原值描述 |
| `m_new_v` | `varchar(1000)` | YES | `NULL` | — | 字段新值 |
| `m_new_disp_v` | `varchar(1000)` | YES | `NULL` | — | 字段新值描述 |
| `m_datetime` | `varchar(20)` | YES | `NULL` | — | 记录时间 |
| `DATA_TENANT_ID` | `varchar(32)` | YES | `NULL` | — | 数据所属租户ID |
| `LAST_CHG_USR` | `varchar(32)` | YES | `NULL` | — | 最新变更用户 |
| `LAST_CHG_DT` | `datetime` | YES | `NULL` | — | 最新变更时间 |

表级约束/索引：
- `PRIMARY KEY (seqid) USING BTREE`
