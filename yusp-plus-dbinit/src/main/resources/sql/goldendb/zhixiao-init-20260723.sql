/*
 * 知效平台数据库 DDL 建表脚本（除 8 张基础骨架核心复用表外的 64 张知效自建表）
 * 基于需求文档: docs/requirements/source/表设计.md (V1.2-DRAFT)
 * 方言兼容: MySQL 8.0.21 / GoldenDB 6.x
 * 生成时间: 2026-07-23
 */

SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tribe
-- ----------------------------
DROP TABLE IF EXISTS `tribe`;
CREATE TABLE `tribe`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `tribe_code`     varchar(50)          DEFAULT NULL COMMENT '部落编码',
    `tribe_name`     varchar(200)         DEFAULT NULL COMMENT '部落名称',
    `leader_id`      varchar(32)          DEFAULT NULL COMMENT '部落长（关联 admin_sm_user.USER_ID）',
    `pmo_id`         varchar(32)          DEFAULT NULL COMMENT 'PMO（关联 admin_sm_user.USER_ID）',
    `org_id`         varchar(32)          DEFAULT NULL COMMENT '对应机构编号（关联 admin_sm_org.ORG_ID）',
    `status`         tinyint              DEFAULT NULL COMMENT '状态',
    `source_system`  varchar(50)          DEFAULT NULL COMMENT '数据来源',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_tribe_code` (`data_tenant_id`, `tribe_code`),
    KEY              `idx_leader_id` (`leader_id`),
    KEY              `idx_org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='部落表';

-- ----------------------------
-- Table structure for tribe_team
-- ----------------------------
DROP TABLE IF EXISTS `tribe_team`;
CREATE TABLE `tribe_team`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `team_code`      varchar(50)          DEFAULT NULL COMMENT '小队编码',
    `team_name`      varchar(200)         DEFAULT NULL COMMENT '小队名称',
    `tribe_id`       varchar(32)          DEFAULT NULL COMMENT '所属部落ID (关联 tribe.id)',
    `leader_id`      varchar(32)          DEFAULT NULL COMMENT '小队长（关联 admin_sm_user.USER_ID）',
    `center_id`      varchar(32)          DEFAULT NULL COMMENT '所属中心ID（关联 admin_sm_org.ORG_ID）',
    `status`         tinyint              DEFAULT NULL COMMENT '状态',
    `source_system`  varchar(50)          DEFAULT NULL COMMENT '数据来源',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_team_code` (`data_tenant_id`, `team_code`),
    KEY              `idx_tribe_id` (`tribe_id`),
    KEY              `idx_leader_id` (`leader_id`),
    KEY              `idx_center_id` (`center_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='小队表';

-- ----------------------------
-- Table structure for user_tribe_team
-- ----------------------------
DROP TABLE IF EXISTS `user_tribe_team`;
CREATE TABLE `user_tribe_team`
(
    `id`                   varchar(32) NOT NULL COMMENT '主键',
    `user_id`              varchar(32)          DEFAULT NULL COMMENT '用户ID (关联 admin_sm_user.USER_ID)',
    `tribe_id`             varchar(32)          DEFAULT NULL COMMENT '部落ID (关联 tribe.id)',
    `team_id`              varchar(32)          DEFAULT NULL COMMENT '小队ID (关联 tribe_team.id)',
    `is_primary`           tinyint              DEFAULT NULL COMMENT '是否主归属',
    `relation_role`        varchar(30)          DEFAULT NULL COMMENT '成员角色（成员/小队长/兼职等）',
    `effective_start_time` datetime             DEFAULT NULL COMMENT '生效时间',
    `effective_end_time`   datetime             DEFAULT NULL COMMENT '失效时间',
    `status`               tinyint              DEFAULT 0 COMMENT '关系状态（1有效/0失效）',
    `create_time`          datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`       varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`           varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`           varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`          tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_user_team` (`data_tenant_id`, `user_id`, `tribe_id`, `team_id`),
    KEY                    `idx_user_id` (`user_id`),
    KEY                    `idx_team_id` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户部落小队关系表';

-- ----------------------------
-- Table structure for user_profile_ext
-- ----------------------------
DROP TABLE IF EXISTS `user_profile_ext`;
CREATE TABLE `user_profile_ext`
(
    `id`                   varchar(32) NOT NULL COMMENT '主键',
    `user_id`              varchar(32)          DEFAULT NULL COMMENT '用户ID（关联 admin_sm_user.USER_ID）',
    `external_employee_no` varchar(100)         DEFAULT NULL COMMENT '外部系统完整工号',
    `employee_type`        varchar(30)          DEFAULT NULL COMMENT '人员类型（行员/人力外包/混合开发等）',
    `vendor_company_code`  varchar(100)         DEFAULT NULL COMMENT '外包公司编码',
    `vendor_company_name`  varchar(200)         DEFAULT NULL COMMENT '外包公司名称快照',
    `outsource_type`       varchar(30)          DEFAULT NULL COMMENT '外包类型',
    `bank_owner_id`        varchar(32)          DEFAULT NULL COMMENT '行方负责人（关联 admin_sm_user.USER_ID）',
    `effective_start_time` datetime             DEFAULT NULL COMMENT '生效时间',
    `effective_end_time`   datetime             DEFAULT NULL COMMENT '失效时间',
    `status`               tinyint              DEFAULT 0 COMMENT '状态（1有效/0失效）',
    `create_time`          datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`       varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`           varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`           varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`          tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_user_id` (`data_tenant_id`, `user_id`),
    KEY                    `idx_ext_employee_no` (`external_employee_no`),
    KEY                    `idx_vendor_company` (`vendor_company_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='知效用户业务属性拓展表';

-- ----------------------------
-- Table structure for dev_system_module
-- ----------------------------
DROP TABLE IF EXISTS `dev_system_module`;
CREATE TABLE `dev_system_module`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `system_id`      varchar(32)          DEFAULT NULL COMMENT '逻辑系统ID（关联 admin_sm_logic_sys.SYS_ID）',
    `module_code`    varchar(50)          DEFAULT NULL COMMENT '模块编码',
    `module_name`    varchar(200)         DEFAULT NULL COMMENT '模块名称',
    `parent_id`      varchar(32)          DEFAULT NULL COMMENT '上级模块ID（自引用）',
    `vendor_company_code` varchar(32)     DEFAULT NULL COMMENT '关联外包公司编码（关联 vendor_company.company_id）',
    `status`         tinyint              DEFAULT 0 COMMENT '状态（1有效/0失效）',
    `source_system`  varchar(50)          DEFAULT NULL COMMENT '数据来源',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_sys_mod_code` (`data_tenant_id`, `system_id`, `module_code`),
    KEY              `idx_system_id` (`system_id`),
    KEY              `idx_parent_id` (`parent_id`),
    KEY              `idx_vendor_company_code` (`vendor_company_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='研发系统模块表';

-- ----------------------------
-- Table structure for user_system_rel
-- ----------------------------
DROP TABLE IF EXISTS `user_system_rel`;
CREATE TABLE `user_system_rel`
(
    `id`                   varchar(32) NOT NULL COMMENT '主键',
    `user_id`              varchar(32)          DEFAULT NULL COMMENT '用户ID（关联 admin_sm_user.USER_ID）',
    `system_id`            varchar(32)          DEFAULT NULL COMMENT '逻辑系统ID（关联 admin_sm_logic_sys.SYS_ID）',
    `module_id`            varchar(32)          DEFAULT NULL COMMENT '默认模块ID（关联 dev_system_module.id，可空）',
    `relation_type`        varchar(30)          DEFAULT NULL COMMENT '归属类型（主归属/兼职/临时授权）',
    `effective_start_time` datetime             DEFAULT NULL COMMENT '生效时间',
    `effective_end_time`   datetime             DEFAULT NULL COMMENT '失效时间',
    `status`               tinyint              DEFAULT 0 COMMENT '状态（1有效/0失效）',
    `create_time`          datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`       varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`           varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`           varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`          tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                    `idx_tenant_user_sys_rel` (`data_tenant_id`, `user_id`, `system_id`, `relation_type`),
    KEY                    `idx_user_id` (`user_id`),
    KEY                    `idx_system_id` (`system_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户研发系统归属关系表';

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project`
(
    `id`                   varchar(32) NOT NULL COMMENT '项目内部主键',
    `project_code`         varchar(100)         DEFAULT NULL COMMENT '项目编号',
    `project_name`         varchar(500)         DEFAULT NULL COMMENT '项目名称',
    `project_type`         varchar(30)          DEFAULT NULL COMMENT '项目类型',
    `project_manager_id`   varchar(32)          DEFAULT NULL COMMENT '项目经理（关联 admin_sm_user.USER_ID）',
    `owner_org_id`         varchar(32)          DEFAULT NULL COMMENT '归属机构（关联 admin_sm_org.ORG_ID）',
    `owner_dpt_id`         varchar(32)          DEFAULT NULL COMMENT '归属部门（关联 admin_sm_dpt.DPT_ID）',
    `host_tribe_id`        varchar(32)          DEFAULT NULL COMMENT '主办部落（关联 tribe.id，可空）',
    `host_team_id`         varchar(32)          DEFAULT NULL COMMENT '主办小队（关联 tribe_team.id，可空）',
    `project_status`       varchar(20)          DEFAULT NULL COMMENT '未开始/进行中/已暂停/已完成/已关闭',
    `source_system`        varchar(50)          DEFAULT NULL COMMENT '项目主数据来源；本地维护固定为 ZHIXIAO',
    `source_project_id`    varchar(100)         DEFAULT NULL COMMENT '来源项目ID，可空',
    `source_version`       varchar(100)         DEFAULT NULL COMMENT '来源对象版本/变更时间',
    `effective_start_time` datetime             DEFAULT NULL COMMENT '生效时间',
    `effective_end_time`   datetime             DEFAULT NULL COMMENT '失效时间，可空',
    `create_time`          datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`       varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`           varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`           varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `version`              int         NOT NULL DEFAULT 0 COMMENT '并发控制乐观锁版本',
    `delete_flag`          tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_project_code` (`data_tenant_id`, `project_code`),
    UNIQUE KEY `uk_tenant_src_project` (`data_tenant_id`, `source_system`, `source_project_id`),
    KEY                    `idx_pm_status` (`project_manager_id`, `project_status`),
    KEY                    `idx_owner_org_dpt` (`owner_org_id`, `owner_dpt_id`),
    KEY                    `idx_tribe_team` (`host_tribe_id`, `host_team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='项目主数据表';

-- ----------------------------
-- Table structure for project_member
-- ----------------------------
DROP TABLE IF EXISTS `project_member`;
CREATE TABLE `project_member`
(
    `id`                   varchar(32) NOT NULL COMMENT '主键',
    `project_id`           varchar(32)          DEFAULT NULL COMMENT '项目ID（关联 project.id）',
    `user_id`              varchar(32)          DEFAULT NULL COMMENT '成员ID（关联 admin_sm_user.USER_ID）',
    `member_role`          varchar(30)          DEFAULT NULL COMMENT '项目经理/开发/测试/外包/观察者等',
    `permission_level`     varchar(20)          DEFAULT NULL COMMENT 'VIEW/EDIT/MANAGE',
    `effective_start_time` datetime             DEFAULT NULL COMMENT '生效时间',
    `effective_end_time`   datetime             DEFAULT NULL COMMENT '失效时间，可空',
    `status`               tinyint              DEFAULT 0 COMMENT '1有效/0失效',
    `source_type`          varchar(20)          DEFAULT NULL COMMENT '手工/组织派生/外部同步',
    `create_time`          datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`       varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`           varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`           varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`          tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_proj_usr_role_time` (`data_tenant_id`, `project_id`, `user_id`, `member_role`, `effective_start_time`),
    KEY                    `idx_usr_sts_eff` (`user_id`, `status`, `effective_start_time`, `effective_end_time`),
    KEY                    `idx_proj_role_sts` (`project_id`, `member_role`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='项目成员与权限范围表';

-- ----------------------------
-- Table structure for vendor_company
-- ----------------------------
DROP TABLE IF EXISTS `vendor_company`;
CREATE TABLE `vendor_company`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `company_id`     varchar(32)          DEFAULT NULL COMMENT '公司编码',
    `name`           varchar(100)         DEFAULT NULL COMMENT '名字',
    `abbreviation`   varchar(30)          DEFAULT NULL COMMENT '简称',
    `status`         tinyint              DEFAULT NULL COMMENT '状态（1有效/0失效）',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_company_id` (`company_id`),
    KEY              `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='外包公司信息表';

-- ----------------------------
-- Table structure for requirement
-- ----------------------------
DROP TABLE IF EXISTS `requirement`;
CREATE TABLE `requirement`
(
    `id`                        varchar(32) NOT NULL COMMENT '主键',
    `req_code`                  varchar(50)          DEFAULT NULL COMMENT '需求编号',
    `req_name`                  varchar(500)         DEFAULT NULL COMMENT '需求名称',
    `req_type`                  varchar(50)          DEFAULT NULL COMMENT '需求类型（一般/人力/推广/测试/系统运维/项目关联）',
    `source_req_id`             varchar(100)         DEFAULT NULL COMMENT '知脉需求ID',
    `source_system`             varchar(50)          DEFAULT NULL COMMENT '来源系统；本地创建固定为 ZHIXIAO',
    `source_version`            varchar(100)         DEFAULT NULL COMMENT '来源对象版本/变更时间，用于幂等和乱序判断',
    `project_id`                varchar(32)          DEFAULT NULL COMMENT '关联项目ID（关联 project.id，可空）',
    `project_code_snapshot`     varchar(100)         DEFAULT NULL COMMENT '关联时项目编号快照，不作为关系键',
    `propose_dpt_id`            varchar(32)          DEFAULT NULL COMMENT '提出部门ID（关联 admin_sm_dpt.DPT_ID）',
    `propose_dept_name`         varchar(200)         DEFAULT NULL COMMENT '提出部门名称快照',
    `business_owner_id`         varchar(32)          DEFAULT NULL COMMENT '业务负责人ID（关联 admin_sm_user.USER_ID）',
    `business_owner_name`       varchar(100)         DEFAULT NULL COMMENT '业务负责人名称快照',
    `pm_id`                     varchar(32)          DEFAULT NULL COMMENT '项目经理（关联 admin_sm_user.USER_ID）',
    `host_tribe_id`             varchar(32)          DEFAULT NULL COMMENT '主办部落ID (关联 tribe.id)',
    `host_team_id`              varchar(32)          DEFAULT NULL COMMENT '主办小队ID (关联 tribe_team.id)',
    `current_stage_instance_id` varchar(32)          DEFAULT NULL COMMENT '当前阶段实例ID（关联 requirement_stage.id），当前阶段唯一权威指针',
    `current_stage`             varchar(50)          DEFAULT NULL COMMENT '当前阶段编码缓存，仅供列表查询，可由当前阶段实例重建',
    `plan_start_date`           date                 DEFAULT NULL COMMENT '计划研发开始时间',
    `plan_end_date`             date                 DEFAULT NULL COMMENT '计划完成时间',
    `actual_start_date`         date                 DEFAULT NULL COMMENT '实际开始时间',
    `actual_end_date`           date                 DEFAULT NULL COMMENT '实际完成时间',
    `first_plan_online_date`    date                 DEFAULT NULL COMMENT '首次计划上线日期，首次写入后不被后续计划覆盖',
    `first_online_date`         date                 DEFAULT NULL COMMENT '首次实际上线日期',
    `is_urgent`                 tinyint              DEFAULT NULL COMMENT '是否紧急需求',
    `is_blocked`                tinyint              DEFAULT NULL COMMENT '是否受阻；由未关闭风险和人工标记按规则汇总',
    `risk_count`                int                  DEFAULT NULL COMMENT '风险问题数',
    `archive_status`            tinyint              DEFAULT 0 COMMENT '归档状态（0未归档/1已归档）',
    `archive_time`              datetime             DEFAULT NULL COMMENT '归档时间',
    `create_time`               datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`               datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`            varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`                varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`                varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `version`                   int         NOT NULL DEFAULT 0 COMMENT '并发控制乐观锁版本',
    `delete_flag`               tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_req_code` (`data_tenant_id`, `req_code`),
    UNIQUE KEY `uk_tenant_src_req` (`data_tenant_id`, `source_system`, `source_req_id`),
    KEY                         `idx_tenant_stage_arch` (`data_tenant_id`, `current_stage`, `archive_status`),
    KEY                         `idx_host_tribe_team` (`host_tribe_id`, `host_team_id`),
    KEY                         `idx_pm_id` (`pm_id`),
    KEY                         `idx_plan_end_date` (`plan_end_date`),
    KEY                         `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='需求主表';

-- ----------------------------
-- Table structure for requirement_stage
-- ----------------------------
DROP TABLE IF EXISTS `requirement_stage`;
CREATE TABLE `requirement_stage`
(
    `id`                   varchar(32) NOT NULL COMMENT '主键',
    `req_id`               varchar(32)          DEFAULT NULL COMMENT '需求ID (关联 requirement.id)',
    `sequence_no`          int                  DEFAULT NULL COMMENT '同一需求阶段实例序号；回退重入时递增',
    `stage_code`           varchar(50)          DEFAULT NULL COMMENT '阶段编码',
    `stage_name`           varchar(100)         DEFAULT NULL COMMENT '阶段名称',
    `is_current`           tinyint              DEFAULT 0 COMMENT '当前实例缓存（1是/0否）；以 requirement.current_stage_instance_id 为准',
    `plan_start_date`      date                 DEFAULT NULL COMMENT '计划开始时间',
    `plan_end_date`        date                 DEFAULT NULL COMMENT '计划结束时间',
    `actual_start_date`    datetime             DEFAULT NULL COMMENT '实际开始时间',
    `actual_end_date`      datetime             DEFAULT NULL COMMENT '实际结束时间',
    `stay_days`            int                  DEFAULT NULL COMMENT '停留天数',
    `approved_deduct_days` int                  DEFAULT NULL COMMENT '已审批生效的扣减天数汇总',
    `effective_stay_days`  int                  DEFAULT NULL COMMENT '扣减后的有效停留天数',
    `stage_status`         varchar(20)          DEFAULT NULL COMMENT '阶段实例状态（进行中/已结束/已回退）',
    `create_time`          datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`       varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`           varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`           varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`          tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_req_seq` (`data_tenant_id`, `req_id`, `sequence_no`),
    KEY                    `idx_req_stage_code` (`req_id`, `stage_code`),
    KEY                    `idx_req_actual_date` (`req_id`, `actual_start_date`, `actual_end_date`),
    KEY                    `idx_stage_status` (`stage_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='需求阶段表';

-- ----------------------------
-- Table structure for requirement_stage_log
-- ----------------------------
DROP TABLE IF EXISTS `requirement_stage_log`;
CREATE TABLE `requirement_stage_log`
(
    `id`                varchar(32) NOT NULL COMMENT '主键',
    `req_id`            varchar(32)          DEFAULT NULL COMMENT '需求ID (关联 requirement.id)',
    `stage_instance_id` varchar(32)          DEFAULT NULL COMMENT '阶段实例ID（关联 requirement_stage.id）',
    `stage_code`        varchar(50)          DEFAULT NULL COMMENT '阶段编码',
    `transition_type`   varchar(30)          DEFAULT NULL COMMENT '进入/离开/回退/自动流转',
    `enter_time`        datetime             DEFAULT NULL COMMENT '进入时间',
    `leave_time`        datetime             DEFAULT NULL COMMENT '离开时间',
    `stay_days`         int                  DEFAULT NULL COMMENT '停留天数',
    `operator_id`       varchar(32)          DEFAULT NULL COMMENT '操作人；自动任务使用系统账号',
    `source_event_id`   varchar(100)         DEFAULT NULL COMMENT '触发流转的外部事件ID，可空',
    `transition_reason` varchar(500)         DEFAULT NULL COMMENT '回退或人工流转原因',
    `create_time`       datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`    varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                 `idx_req_id` (`req_id`),
    KEY                 `idx_stage_instance_id` (`stage_instance_id`),
    KEY                 `idx_enter_time` (`enter_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='阶段停留日志表';

-- ----------------------------
-- Table structure for requirement_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `requirement_operation_log`;
CREATE TABLE `requirement_operation_log`
(
    `id`               varchar(32) NOT NULL COMMENT '主键',
    `req_id`           varchar(32)          DEFAULT NULL COMMENT '需求ID (关联 requirement.id)',
    `operator_id`      varchar(32)          DEFAULT NULL COMMENT '操作人ID (关联 admin_sm_user.USER_ID)',
    `operation_type`   varchar(50)          DEFAULT NULL COMMENT '操作类型',
    `operation_detail` text                 DEFAULT NULL COMMENT '操作详情',
    `log_category`     varchar(30)          DEFAULT NULL COMMENT '日志分类（修改/关联/流转/归档）',
    `before_value`     json                 DEFAULT NULL COMMENT '操作前值',
    `after_value`      json                 DEFAULT NULL COMMENT '操作后值',
    `operation_reason` varchar(500)         DEFAULT NULL COMMENT '操作原因',
    `client_ip`        varchar(50)          DEFAULT NULL COMMENT '客户端IP',
    `operation_result` varchar(20)          DEFAULT NULL COMMENT '成功/失败/部分成功',
    `trace_id`         varchar(100)         DEFAULT NULL COMMENT '链路追踪号',
    `create_time`      datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`   varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `idx_req_id` (`req_id`),
    KEY                `idx_operator_id` (`operator_id`),
    KEY                `idx_create_time` (`create_time`),
    KEY                `idx_trace_id` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='需求操作日志表';

-- ----------------------------
-- Table structure for requirement_memo
-- ----------------------------
DROP TABLE IF EXISTS `requirement_memo`;
CREATE TABLE `requirement_memo`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `req_id`         varchar(32)          DEFAULT NULL COMMENT '需求ID (关联 requirement.id)',
    `content`        text                 DEFAULT NULL COMMENT '备忘录内容',
    `creator_id`     varchar(32)          DEFAULT NULL COMMENT '创建人ID (关联 admin_sm_user.USER_ID)',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    KEY              `idx_req_id` (`req_id`),
    KEY              `idx_creator_id` (`creator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='需求备忘录表';

-- ----------------------------
-- Table structure for risk_issue
-- ----------------------------
DROP TABLE IF EXISTS `risk_issue`;
CREATE TABLE `risk_issue`
(
    `id`                  varchar(32) NOT NULL COMMENT '主键',
    `req_id`              varchar(32)          DEFAULT NULL COMMENT '需求ID (关联 requirement.id)',
    `issue_type`          varchar(50)          DEFAULT NULL COMMENT '问题类型',
    `description`         text                 DEFAULT NULL COMMENT '问题描述',
    `severity`            varchar(20)          DEFAULT NULL COMMENT '严重程度（高/中/低）',
    `responsible_id`      varchar(32)          DEFAULT NULL COMMENT '责任人ID (关联 admin_sm_user.USER_ID)',
    `creator_id`          varchar(32)          DEFAULT NULL COMMENT '创建人ID（关联 admin_sm_user.USER_ID）',
    `status`              varchar(20)          DEFAULT NULL COMMENT '状态（未解决/已解决/已关闭）',
    `resolve_description` varchar(1000)        DEFAULT NULL COMMENT '解决说明',
    `resolve_time`        datetime             DEFAULT NULL COMMENT '解决时间',
    `create_time`         datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`      varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `updated_by`          varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`         tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                   `idx_req_id` (`req_id`),
    KEY                   `idx_responsible_id` (`responsible_id`),
    KEY                   `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='风险问题表';

-- ----------------------------
-- Table structure for requirement_comment
-- ----------------------------
DROP TABLE IF EXISTS `requirement_comment`;
CREATE TABLE `requirement_comment`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `req_id`         varchar(32)          DEFAULT NULL COMMENT '需求ID (关联 requirement.id)',
    `parent_id`      varchar(32)          DEFAULT NULL COMMENT '回复父评论ID (关联 requirement_comment.id，自引用)',
    `content`        text                 DEFAULT NULL COMMENT '评论内容',
    `creator_id`     varchar(32)          DEFAULT NULL COMMENT '评论人ID (关联 admin_sm_user.USER_ID)',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    KEY              `idx_req_id` (`req_id`),
    KEY              `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='需求评论表';

-- ----------------------------
-- Table structure for requirement_attachment
-- ----------------------------
DROP TABLE IF EXISTS `requirement_attachment`;
CREATE TABLE `requirement_attachment`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `req_id`         varchar(32)          DEFAULT NULL COMMENT '需求ID (关联 requirement.id)',
    `file_id`        varchar(32)          DEFAULT NULL COMMENT '文件ID（关联 admin_file_upload_info.FILE_ID）',
    `file_name`      varchar(500)         DEFAULT NULL COMMENT '文件名',
    `file_url`       varchar(1000)        DEFAULT NULL COMMENT '文件URL',
    `file_size`      bigint               DEFAULT NULL COMMENT '文件大小（字节）',
    `file_type`      varchar(50)          DEFAULT NULL COMMENT '文件类型',
    `scan_status`    varchar(20)          DEFAULT NULL COMMENT '安全扫描状态（待扫描/通过/拒绝）',
    `scan_result`    varchar(500)         DEFAULT NULL COMMENT '安全扫描结果摘要',
    `uploader_id`    varchar(32)          DEFAULT NULL COMMENT '上传人ID (关联 admin_sm_user.USER_ID)',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    KEY              `idx_req_id` (`req_id`),
    KEY              `idx_file_id` (`file_id`),
    KEY              `idx_uploader_id` (`uploader_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='需求附件表';

-- ----------------------------
-- Table structure for requirement_tag
-- ----------------------------
DROP TABLE IF EXISTS `requirement_tag`;
CREATE TABLE `requirement_tag`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `req_id`         varchar(32)          DEFAULT NULL COMMENT '需求ID (关联 requirement.id)',
    `tag_name`       varchar(100)         DEFAULT NULL COMMENT '标签名称',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_req_tag` (`data_tenant_id`, `req_id`, `tag_name`),
    KEY              `idx_req_id` (`req_id`),
    KEY              `idx_tag_name` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='需求标签表';

-- ----------------------------
-- Table structure for custom_field_definition
-- ----------------------------
DROP TABLE IF EXISTS `custom_field_definition`;
CREATE TABLE `custom_field_definition`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `field_name`     varchar(100)         DEFAULT NULL COMMENT '字段名称',
    `field_type`     varchar(30)          DEFAULT NULL COMMENT '字段类型（单行文本/多行文本/数字/日期时间/单选/多选）',
    `field_options`  json                 DEFAULT NULL COMMENT '选项值（单选/多选使用）',
    `scope_type`     varchar(20)          DEFAULT NULL COMMENT '作用域类型（全局/部落/小队/项目组）',
    `scope_id`       varchar(32)          DEFAULT NULL COMMENT '作用域对象ID；全局使用固定占位值，项目组关联 project.id',
    `scope_slot_no`  smallint             DEFAULT NULL COMMENT '作用域内配额槽位；全局1—20、项目1—10，其他作用域按批准规则',
    `visible_range`  varchar(50)          DEFAULT NULL COMMENT '可见范围（全员/组织内/指定角色）',
    `is_required`    tinyint              DEFAULT NULL COMMENT '是否必填',
    `sort_order`     int                  DEFAULT NULL COMMENT '排序号',
    `status`         tinyint              DEFAULT NULL COMMENT '状态（1启用/0归档）',
    `creator_id`     varchar(32)          DEFAULT NULL COMMENT '创建人ID (关联 admin_sm_user.USER_ID)',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `version`        int         NOT NULL DEFAULT 0 COMMENT '并发控制乐观锁版本',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_scope_slot` (`data_tenant_id`, `scope_type`, `scope_id`, `scope_slot_no`),
    KEY              `idx_scope_status` (`scope_type`, `scope_id`, `status`),
    KEY              `idx_creator_id` (`creator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='自定义字段定义表';

-- ----------------------------
-- Table structure for custom_field_value
-- ----------------------------
DROP TABLE IF EXISTS `custom_field_value`;
CREATE TABLE `custom_field_value`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `field_id`       varchar(32)          DEFAULT NULL COMMENT '自定义字段ID (关联 custom_field_definition.id)',
    `object_type`    varchar(30)          DEFAULT NULL COMMENT '对象类型（requirement/task）',
    `object_id`      varchar(32)          DEFAULT NULL COMMENT '对象ID',
    `value_text`     text                 DEFAULT NULL COMMENT '文本值；单行/多行使用',
    `value_number`   decimal(20, 6)       DEFAULT 0.00 COMMENT '数值型字段值',
    `value_datetime` datetime             DEFAULT NULL COMMENT '日期时间型字段值',
    `value_json`     json                 DEFAULT NULL COMMENT '多选等复合值',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_field_obj` (`data_tenant_id`, `field_id`, `object_type`, `object_id`),
    KEY              `idx_obj_type_id` (`object_type`, `object_id`),
    KEY              `idx_val_number` (`value_number`),
    KEY              `idx_val_datetime` (`value_datetime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='自定义字段值表';

-- ----------------------------
-- Table structure for requirement_display_setting
-- ----------------------------
DROP TABLE IF EXISTS `requirement_display_setting`;
CREATE TABLE `requirement_display_setting`
(
    `id`              varchar(32) NOT NULL COMMENT '主键',
    `user_id`         varchar(32)          DEFAULT NULL COMMENT '用户ID (关联 admin_sm_user.USER_ID)',
    `display_columns` json                 DEFAULT NULL COMMENT '显示列配置',
    `sort_config`     json                 DEFAULT NULL COMMENT '默认排序配置',
    `create_time`     datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`  varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`      varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`      varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`     tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_user_id` (`data_tenant_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='需求列表显示设置表';

-- ----------------------------
-- Table structure for requirement_statistics
-- ----------------------------
DROP TABLE IF EXISTS `requirement_statistics`;
CREATE TABLE `requirement_statistics`
(
    `id`                varchar(32) NOT NULL COMMENT '主键',
    `org_id`            varchar(32)          DEFAULT NULL COMMENT '组织/机构ID (关联 admin_sm_org.ORG_ID)',
    `stat_month`        varchar(7)           DEFAULT NULL COMMENT '统计月份（yyyy-MM）',
    `total_count`       int                  DEFAULT NULL COMMENT '需求总数',
    `new_count`         int                  DEFAULT NULL COMMENT '新增数',
    `online_count`      int                  DEFAULT NULL COMMENT '上线数',
    `in_progress_count` int                  DEFAULT NULL COMMENT '进行中数',
    `overdue_count`     int                  DEFAULT NULL COMMENT '延期数',
    `avg_complete_days` decimal(10, 2)       DEFAULT 0.00 COMMENT '平均完成天数',
    `create_time`       datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`    varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`        varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`        varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`       tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_org_month` (`data_tenant_id`, `org_id`, `stat_month`),
    KEY                 `idx_stat_month` (`stat_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='需求统计表';

-- ----------------------------
-- Table structure for requirement_participant
-- ----------------------------
DROP TABLE IF EXISTS `requirement_participant`;
CREATE TABLE `requirement_participant`
(
    `id`                   varchar(32) NOT NULL COMMENT '主键',
    `req_id`               varchar(32)          DEFAULT NULL COMMENT '需求ID（关联 requirement.id）',
    `user_id`              varchar(32)          DEFAULT NULL COMMENT '用户ID（关联 admin_sm_user.USER_ID）',
    `participant_role`     varchar(30)          DEFAULT NULL COMMENT '主办/辅办/开发/测试/外包/观察者',
    `source_type`          varchar(20)          DEFAULT NULL COMMENT '手工/任务派生/外部同步',
    `effective_start_time` datetime             DEFAULT NULL COMMENT '生效时间',
    `effective_end_time`   datetime             DEFAULT NULL COMMENT '失效时间',
    `status`               tinyint              DEFAULT 0 COMMENT '状态（1有效/0失效）',
    `create_time`          datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`       varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`           varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`           varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`          tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_req_usr_role_eff` (`data_tenant_id`, `req_id`, `user_id`, `participant_role`, `effective_start_time`),
    KEY                    `idx_usr_sts_eff` (`user_id`, `status`, `effective_start_time`, `effective_end_time`),
    KEY                    `idx_req_sts` (`req_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='需求参与人员关系表';

-- ----------------------------
-- Table structure for requirement_plan_change_log
-- ----------------------------
DROP TABLE IF EXISTS `requirement_plan_change_log`;
CREATE TABLE `requirement_plan_change_log`
(
    `id`                  varchar(32) NOT NULL COMMENT '主键',
    `req_id`              varchar(32)          DEFAULT NULL COMMENT '需求ID（关联 requirement.id）',
    `change_source`       varchar(30)          DEFAULT NULL COMMENT '知脉同步/人工修正/迁移',
    `old_plan_start_date` date                 DEFAULT NULL COMMENT '变更前计划开始日期',
    `new_plan_start_date` date                 DEFAULT NULL COMMENT '变更后计划开始日期',
    `old_plan_end_date`   date                 DEFAULT NULL COMMENT '变更前计划完成日期',
    `new_plan_end_date`   date                 DEFAULT NULL COMMENT '变更后计划完成日期',
    `source_event_id`     varchar(100)         DEFAULT NULL COMMENT '外部事件ID，可空',
    `change_reason`       varchar(500)         DEFAULT NULL COMMENT '变更原因',
    `operator_id`         varchar(32)          DEFAULT NULL COMMENT '操作人ID',
    `create_time`         datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`      varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `updated_by`          varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `update_time`         datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag`         tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                   `idx_req_id` (`req_id`),
    KEY                   `idx_operator_id` (`operator_id`),
    KEY                   `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='需求计划变更历史表';

-- ----------------------------
-- Table structure for custom_field_visibility
-- ----------------------------
DROP TABLE IF EXISTS `custom_field_visibility`;
CREATE TABLE `custom_field_visibility`
(
    `id`              varchar(32) NOT NULL COMMENT '主键',
    `field_id`        varchar(32)          DEFAULT NULL COMMENT '自定义字段ID（关联 custom_field_definition.id）',
    `subject_type`    varchar(20)          DEFAULT NULL COMMENT 'USER/ROLE/ORG/DPT/TRIBE/TEAM',
    `subject_id`      varchar(32)          DEFAULT NULL COMMENT '对应对象ID',
    `permission_type` varchar(20)          DEFAULT NULL COMMENT 'VIEW/EDIT',
    `create_time`     datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`  varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`      varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`      varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `update_time`     datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag`     tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    KEY               `idx_field_id` (`field_id`),
    KEY               `idx_subject` (`subject_type`, `subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='自定义字段可见对象关系表';

-- ----------------------------
-- Table structure for requirement_filter_setting
-- ----------------------------
DROP TABLE IF EXISTS `requirement_filter_setting`;
CREATE TABLE `requirement_filter_setting`
(
    `id`                varchar(32) NOT NULL COMMENT '主键',
    `user_id`           varchar(32)          DEFAULT NULL COMMENT '用户ID（关联 admin_sm_user.USER_ID）',
    `filter_name`       varchar(100)         DEFAULT NULL COMMENT '筛选方案名称',
    `filter_expression` json                 DEFAULT NULL COMMENT 'AND/OR 嵌套条件树',
    `is_default`        tinyint              DEFAULT 0 COMMENT '是否默认方案',
    `create_time`       datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`    varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`        varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`        varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`       tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_user_filter_name` (`data_tenant_id`, `user_id`, `filter_name`),
    KEY                 `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='个人需求筛选方案表';

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`
(
    `id`                      varchar(32) NOT NULL COMMENT '主键',
    `task_code`               varchar(50)          DEFAULT NULL COMMENT '任务编号',
    `task_name`               varchar(500)         DEFAULT NULL COMMENT '任务名称',
    `task_type`               varchar(30)          DEFAULT NULL COMMENT '任务类型（需求类/项目类/事务类/安全类）',
    `task_label`              varchar(50)          DEFAULT NULL COMMENT '任务标签/子类型（设计/开发/测试/OKR等）',
    `parent_id`               varchar(32)          DEFAULT NULL COMMENT '父任务ID (关联 task.id，自引用)',
    `req_id`                  varchar(32)          DEFAULT NULL COMMENT '关联需求ID',
    `project_id`              varchar(32)          DEFAULT NULL COMMENT '项目类任务关联项目ID（关联 project.id）',
    `task_group_key`          varchar(100)         DEFAULT NULL COMMENT '同需求同系统下开发/SIT任务对应组标识',
    `assignee_id`             varchar(32)          DEFAULT NULL COMMENT '责任人ID (关联 admin_sm_user.USER_ID)',
    `assigner_id`             varchar(32)          DEFAULT NULL COMMENT '分派人ID (关联 admin_sm_user.USER_ID)',
    `system_id`               varchar(32)          DEFAULT NULL COMMENT '关联系统ID（关联 admin_sm_logic_sys.SYS_ID）',
    `module_id`               varchar(32)          DEFAULT NULL COMMENT '关联模块ID（关联 dev_system_module.id）',
    `company_id`              varchar(32)          DEFAULT NULL COMMENT '关联公司ID（关联 admin_sm_company.COMPANY_ID）',
    `development_direction`   varchar(100)         DEFAULT NULL COMMENT '研发方向',
    `development_mode`        varchar(30)          DEFAULT NULL COMMENT '开发模式',
    `task_status`             varchar(20)          DEFAULT NULL COMMENT '业务状态（未开始/进行中/阻塞中/已暂停/已完成）',
    `priority`                tinyint              DEFAULT NULL COMMENT '优先级',
    `workload`                decimal(10, 2)       DEFAULT 0.00 COMMENT '工作量（人天）',
    `progress`                tinyint              DEFAULT NULL COMMENT '进度百分比',
    `is_lighted`              tinyint              DEFAULT 0 COMMENT '当前点亮缓存（0否/1是），历史以 task_light_daily 为准',
    `work_hour_eligible`      tinyint              DEFAULT NULL COMMENT '是否可报工，由任务类型与安全任务规则计算',
    `workload_confirm_status` tinyint              DEFAULT 0 COMMENT '知脉工作量确认/封板状态（0未确认/1已确认）',
    `workload_confirm_time`   datetime             DEFAULT NULL COMMENT '知脉工作量确认时间',
    `plan_start_date`         date                 DEFAULT NULL COMMENT '计划开始时间',
    `plan_end_date`           date                 DEFAULT NULL COMMENT '计划完成时间',
    `actual_start_date`       date                 DEFAULT NULL COMMENT '实际开始时间',
    `actual_end_date`         date                 DEFAULT NULL COMMENT '实际完成时间',
    `remark`                  varchar(2000)        DEFAULT NULL COMMENT '任务备注',
    `source_system`           varchar(50)          DEFAULT NULL COMMENT '数据来源',
    `source_task_id`          varchar(100)         DEFAULT NULL COMMENT '源系统任务ID',
    `source_version`          varchar(100)         DEFAULT NULL COMMENT '源对象版本/变更时间',
    `create_time`             datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`             datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`          varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`              varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`              varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `version`                 int         NOT NULL DEFAULT 0 COMMENT '并发控制乐观锁版本',
    `delete_flag`             tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_task_code` (`data_tenant_id`, `task_code`),
    UNIQUE KEY `uk_tenant_src_task` (`data_tenant_id`, `source_system`, `source_task_id`),
    KEY                       `idx_assignee_status` (`assignee_id`, `task_status`),
    KEY                       `idx_req_sys_type` (`req_id`, `system_id`, `task_type`),
    KEY                       `idx_parent_id` (`parent_id`),
    KEY                       `idx_plan_end_date` (`plan_end_date`),
    KEY                       `idx_project_id` (`project_id`),
    KEY                       `idx_group_key` (`task_group_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='任务主表';

-- ----------------------------
-- Table structure for task_collaborator
-- ----------------------------
DROP TABLE IF EXISTS `task_collaborator`;
CREATE TABLE `task_collaborator`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `task_id`        varchar(32)          DEFAULT NULL COMMENT '任务ID (关联 task.id)',
    `user_id`        varchar(32)          DEFAULT NULL COMMENT '用户ID (关联 admin_sm_user.USER_ID)',
    `role_type`      varchar(30)          DEFAULT NULL COMMENT '协作角色',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_task_user_role` (`data_tenant_id`, `task_id`, `user_id`, `role_type`),
    KEY              `idx_task_id` (`task_id`),
    KEY              `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='任务协作成员表';

-- ----------------------------
-- Table structure for task_change_log
-- ----------------------------
DROP TABLE IF EXISTS `task_change_log`;
CREATE TABLE `task_change_log`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `task_id`        varchar(32)          DEFAULT NULL COMMENT '任务ID (关联 task.id)',
    `operator_id`    varchar(32)          DEFAULT NULL COMMENT '操作人ID (关联 admin_sm_user.USER_ID)',
    `change_type`    varchar(30)          DEFAULT NULL COMMENT '变更类型',
    `before_value`   json                 DEFAULT NULL COMMENT '变更前值',
    `after_value`    json                 DEFAULT NULL COMMENT '变更后值',
    `change_comment` varchar(500)         DEFAULT NULL COMMENT '变更备注',
    `change_reason`  varchar(500)         DEFAULT NULL COMMENT '变更原因',
    `client_ip`      varchar(50)          DEFAULT NULL COMMENT '客户端IP',
    `change_result`  varchar(20)          DEFAULT NULL COMMENT '成功/失败/部分成功',
    `trace_id`       varchar(100)         DEFAULT NULL COMMENT '链路追踪号',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY              `idx_task_id` (`task_id`),
    KEY              `idx_operator_id` (`operator_id`),
    KEY              `idx_create_time` (`create_time`),
    KEY              `idx_trace_id` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='任务变更日志表';

-- ----------------------------
-- Table structure for task_template
-- ----------------------------
DROP TABLE IF EXISTS `task_template`;
CREATE TABLE `task_template`
(
    `id`               varchar(32) NOT NULL COMMENT '主键',
    `template_name`    varchar(200)         DEFAULT NULL COMMENT '模板名称',
    `task_type`        varchar(30)          DEFAULT NULL COMMENT '任务类型',
    `template_content` json                 DEFAULT NULL COMMENT '模板内容配置',
    `creator_id`       varchar(32)          DEFAULT NULL COMMENT '创建人ID (关联 admin_sm_user.USER_ID)',
    `create_time`      datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`   varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `updated_by`       varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`      tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `idx_creator_id` (`creator_id`),
    KEY                `idx_task_type` (`task_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='任务模板表';

-- ----------------------------
-- Table structure for security_check_task
-- ----------------------------
DROP TABLE IF EXISTS `security_check_task`;
CREATE TABLE `security_check_task`
(
    `id`                    varchar(32) NOT NULL COMMENT '主键',
    `task_id`               varchar(32)          DEFAULT NULL COMMENT '安全类任务ID（关联 task.id，且 task_type=安全类）',
    `related_dev_task_id`   varchar(32)          DEFAULT NULL COMMENT '被检查的开发任务ID（关联 task.id，可空）',
    `req_id`                varchar(32)          DEFAULT NULL COMMENT '关联需求ID',
    `scene`                 text                 DEFAULT NULL COMMENT '安全场景',
    `security_requirements` text                 DEFAULT NULL COMMENT '安全需求',
    `security_design`       text                 DEFAULT NULL COMMENT '安全设计要点',
    `confirm_status`        tinyint              DEFAULT NULL COMMENT '确认状态（0待确认/1已确认）',
    `confirmed_by`          varchar(32)          DEFAULT NULL COMMENT '确认人ID（关联 admin_sm_user.USER_ID）',
    `confirmed_time`        datetime             DEFAULT NULL COMMENT '确认时间',
    `source_task_id`        varchar(100)         DEFAULT NULL COMMENT '知安任务ID',
    `source_version`        varchar(100)         DEFAULT NULL COMMENT '知安对象版本',
    `sync_status`           varchar(20)          DEFAULT NULL COMMENT '同步状态',
    `create_time`           datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`           datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`        varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`            varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`            varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`           tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_task_id` (`data_tenant_id`, `task_id`),
    UNIQUE KEY `uk_tenant_src_task_id` (`data_tenant_id`, `source_task_id`),
    KEY                     `idx_dev_task_id` (`related_dev_task_id`),
    KEY                     `idx_req_id` (`req_id`),
    KEY                     `idx_confirm_status` (`confirm_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='安全检查任务表';

-- ----------------------------
-- Table structure for task_dependency
-- ----------------------------
DROP TABLE IF EXISTS `task_dependency`;
CREATE TABLE `task_dependency`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `task_id`        varchar(32)          DEFAULT NULL COMMENT '当前任务ID (关联 task.id)',
    `depend_task_id` varchar(32)          DEFAULT NULL COMMENT '依赖任务ID (关联 task.id)',
    `depend_type`    varchar(10)          DEFAULT NULL COMMENT '依赖类型（FS/SS/FF/SF）',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_task_depend` (`data_tenant_id`, `task_id`, `depend_task_id`),
    KEY              `idx_task_id` (`task_id`),
    KEY              `idx_depend_task_id` (`depend_task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='任务依赖关系表';

-- ----------------------------
-- Table structure for task_light_daily
-- ----------------------------
DROP TABLE IF EXISTS `task_light_daily`;
CREATE TABLE `task_light_daily`
(
    `id`                   varchar(32) NOT NULL COMMENT '主键',
    `user_id`              varchar(32)          DEFAULT NULL COMMENT '点亮人员ID（关联 admin_sm_user.USER_ID）',
    `task_id`              varchar(32)          DEFAULT NULL COMMENT '个人子任务ID（关联 task.id）',
    `work_date`            date                 DEFAULT NULL COMMENT '点亮归属自然日',
    `light_status`         tinyint              DEFAULT 0 COMMENT '日终是否点亮（0否/1是）',
    `active_slot_no`       tinyint              DEFAULT NULL COMMENT '当前点亮槽位1—5；取消时置空',
    `first_light_time`     datetime             DEFAULT NULL COMMENT '当日首次点亮时间',
    `last_change_time`     datetime             DEFAULT NULL COMMENT '当日最后状态变化时间',
    `cancel_time`          datetime             DEFAULT NULL COMMENT '取消点亮时间，可空',
    `source_change_log_id` varchar(32)          DEFAULT NULL COMMENT '触发点亮的任务变更日志ID',
    `create_time`          datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`       varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`           varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`           varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`          tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_usr_tsk_date` (`data_tenant_id`, `user_id`, `task_id`, `work_date`),
    UNIQUE KEY `uk_tenant_usr_date_slot` (`data_tenant_id`, `user_id`, `work_date`, `active_slot_no`),
    KEY                    `idx_usr_date_light` (`user_id`, `work_date`, `light_status`),
    KEY                    `idx_tsk_date` (`task_id`, `work_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='每日任务点亮记录表';

-- ----------------------------
-- Table structure for work_hour_record
-- ----------------------------
DROP TABLE IF EXISTS `work_hour_record`;
CREATE TABLE `work_hour_record`
(
    `id`                             varchar(32) NOT NULL COMMENT '主键',
    `user_id`                        varchar(32)          DEFAULT NULL COMMENT '外包人员ID (关联 admin_sm_user.USER_ID)',
    `user_name`                      varchar(100)         DEFAULT NULL COMMENT '报工时外包工号',
    `org_id_snapshot`                varchar(32)          DEFAULT NULL COMMENT '报工时所属机构ID快照',
    `dpt_id_snapshot`                varchar(32)          DEFAULT NULL COMMENT '报工时所属部门ID快照',
    `tribe_id_snapshot`              varchar(32)          DEFAULT NULL COMMENT '报工时所属部落ID快照',
    `team_id_snapshot`               varchar(32)          DEFAULT NULL COMMENT '报工时所属小队ID快照',
    `manager_id_snapshot`            varchar(32)          DEFAULT NULL COMMENT '报工时直接管理者/小队长ID快照',
    `company_id`                     varchar(100)         DEFAULT NULL COMMENT '逻辑厂商ID（关联 vendor_company.company_id）',
    `company`                        varchar(200)         DEFAULT NULL COMMENT '报工时外包公司名称',
    `outsource_type_snapshot`        varchar(30)          DEFAULT NULL COMMENT '报工时外包类型快照',
    `development_direction_snapshot` varchar(100)         DEFAULT NULL COMMENT '报工时研发方向快照',
    `development_mode_snapshot`      varchar(30)          DEFAULT NULL COMMENT '报工时开发模式快照',
    `work_date`                      date                 DEFAULT NULL COMMENT '报工日期',
    `zw_time`                        int                  DEFAULT NULL COMMENT '工作时长（分钟）',
    `task_num`                       varchar(32)          DEFAULT NULL COMMENT '关联开发任务编号；无点亮任务异常报工可空',
    `task_name`                      varchar(32)          DEFAULT NULL COMMENT '关联开发任务名称；无点亮任务异常报工可空',
    `req_num`                        varchar(32)          DEFAULT NULL COMMENT '关联需求ID，可空',
    `req_name`                       varchar(32)          DEFAULT NULL COMMENT '关联需求名称，可空',
    `system_id`                      varchar(32)          DEFAULT NULL COMMENT '研发系统快照（关联 admin_sm_logic_sys.code）',
    `system_name`                    varchar(32)          DEFAULT NULL COMMENT '研发系统（关联 admin_sm_logic_sys.SYS_NAME）',
    `system_module_id`               varchar(32)          DEFAULT NULL COMMENT '系统模块id（关联 dev_system_module.code）',
    `system_module_name`             varchar(32)          DEFAULT NULL COMMENT '系统模块名称（关联 dev_system_module.name）',
    `source_type`                    varchar(20)          DEFAULT NULL COMMENT '来源（自动生成/手动填报/知微迁移）',
    `source_system`                  varchar(50)          DEFAULT NULL COMMENT '明细实际来源系统',
    `create_time`                    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`                    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`                 varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`                     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`                     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`                    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                              `idx_user_date` (`user_id`,`user_name`, `work_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='报工记录表';

-- ----------------------------
-- Table structure for attendance_sync_record
-- ----------------------------
DROP TABLE IF EXISTS `attendance_sync_record`;
CREATE TABLE `attendance_sync_record`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `name`           varchar(50)          DEFAULT NULL COMMENT '外包姓名',
    `number`         varchar(100)         DEFAULT NULL COMMENT '外包工号',
    `date`           date                 DEFAULT NULL COMMENT '考勤日期',
    `minute`         int                  DEFAULT NULL COMMENT '考勤时长（分钟）',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='考勤同步记录表';

-- ----------------------------
-- Table structure for work_hour_modify_log
-- ----------------------------
DROP TABLE IF EXISTS `work_hour_modify_log`;
CREATE TABLE `work_hour_modify_log`
(
    `id`               varchar(32) NOT NULL COMMENT '主键',
    `record_id`        varchar(32)          DEFAULT NULL COMMENT '报工记录ID (关联 work_hour_record.id)',
    `operator_id`      varchar(32)          DEFAULT NULL COMMENT '操作人ID (关联 admin_sm_user.USER_ID)',
    `operation_type`   varchar(20)          DEFAULT NULL COMMENT '新增/修改/删除/自动生成/自动重算/锁定/解锁',
    `before_value`     json                 DEFAULT NULL COMMENT '修改前值',
    `after_value`      json                 DEFAULT NULL COMMENT '修改后值',
    `modify_reason`    varchar(500)         DEFAULT NULL COMMENT '修改原因',
    `client_ip`        varchar(50)          DEFAULT NULL COMMENT '客户端IP',
    `operation_result` varchar(20)          DEFAULT NULL COMMENT '成功/失败',
    `trace_id`         varchar(100)         DEFAULT NULL COMMENT '链路追踪号',
    `create_time`      datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`   varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `idx_record_id` (`record_id`),
    KEY                `idx_operator_id` (`operator_id`),
    KEY                `idx_create_time` (`create_time`),
    KEY                `idx_trace_id` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='报工修改日志表';

-- ----------------------------
-- Table structure for work_hour_summary
-- ----------------------------
DROP TABLE IF EXISTS `work_hour_summary`;
CREATE TABLE `work_hour_summary`
(
    `id`                       varchar(32) NOT NULL COMMENT '主键',
    `user_id`                  varchar(32)          DEFAULT NULL COMMENT '用户ID (关联 admin_sm_user.USER_ID)',
    `summary_month`            varchar(7)           DEFAULT NULL COMMENT '汇总月份（yyyy-MM）',
    `total_minutes`            int                  DEFAULT NULL COMMENT '报工总时长',
    `total_attendance_minutes` int                  DEFAULT NULL COMMENT '考勤总时长',
    `settleable_minutes`       int                  DEFAULT NULL COMMENT '可结算时长',
    `exception_minutes`        int                  DEFAULT NULL COMMENT '异常报工分钟数',
    `data_as_of_time`          datetime             DEFAULT NULL COMMENT '汇总数据截止时间',
    `create_time`              datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`           varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`               varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`               varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`              tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_usr_month` (`data_tenant_id`, `user_id`, `summary_month`),
    KEY                        `idx_summary_month` (`summary_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='报工月度汇总表';

-- ----------------------------
-- Table structure for work_hour_config
-- ----------------------------
DROP TABLE IF EXISTS `work_hour_config`;
CREATE TABLE `work_hour_config`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `config_key`     varchar(100)         DEFAULT NULL COMMENT '配置键',
    `config_value`   text                 DEFAULT NULL COMMENT '配置值',
    `value_type`     varchar(20)          DEFAULT NULL COMMENT 'INTEGER/DECIMAL/BOOLEAN/STRING/JSON',
    `is_deleted`     tinyint              DEFAULT NULL COMMENT '是否有效',
    `description`    varchar(500)         DEFAULT NULL COMMENT '配置说明',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `version`        int         NOT NULL DEFAULT 0 COMMENT '并发控制乐观锁版本',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_cfg_key` (`data_tenant_id`, `config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='报工配置表';

-- ----------------------------
-- Table structure for user_work_day
-- ----------------------------
DROP TABLE IF EXISTS `user_work_day`;
CREATE TABLE `user_work_day`
(
    `id`                           varchar(32) NOT NULL COMMENT '主键',
    `user_id`                      varchar(32)          DEFAULT NULL COMMENT '用户ID（关联 admin_sm_user.USER_ID）',
    `work_date`                    date                 DEFAULT NULL COMMENT '工作日期',
    `current_attendance_record_id` varchar(32)          DEFAULT NULL COMMENT '最新有效考勤版本ID（关联 attendance_sync_record.id，可空）',
    `attendance_minutes`           int                  DEFAULT NULL COMMENT '最新有效考勤分钟缓存，可由指针重建',
    `reported_minutes`             int                  DEFAULT NULL COMMENT '当前有效报工分钟合计缓存',
    `active_light_count`           tinyint              DEFAULT NULL COMMENT '当前有效点亮任务数缓存，范围0—5',
    `generation_status`            varchar(20)          DEFAULT NULL COMMENT '未生成/生成中/已生成/需重算/异常',
    `generation_rule_version`      varchar(100)         DEFAULT NULL COMMENT '最近一次默认工时生成规则版本',
    `last_reconcile_time`          datetime             DEFAULT NULL COMMENT '最近对账时间',
    `reconcile_status`             varchar(20)          DEFAULT NULL COMMENT '一致/待处理/不一致',
    `create_time`                  datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`                  datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`               varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`                   varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`                   varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `version`                      int         NOT NULL DEFAULT 0 COMMENT '并发控制乐观锁版本',
    `delete_flag`                  tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_usr_date` (`data_tenant_id`, `user_id`, `work_date`),
    KEY                            `idx_gen_reconcile_date` (`generation_status`, `reconcile_status`, `work_date`),
    KEY                            `idx_curr_att_rec` (`current_attendance_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='人员日考勤与报工并发控制表';

-- ----------------------------
-- Table structure for stage_aging_record
-- ----------------------------
DROP TABLE IF EXISTS `stage_aging_record`;
CREATE TABLE `stage_aging_record`
(
    `id`                varchar(32) NOT NULL COMMENT '主键',
    `req_id`            varchar(32)          DEFAULT NULL COMMENT '需求ID (关联 requirement.id)',
    `stage_instance_id` varchar(32)          DEFAULT NULL COMMENT '阶段实例ID（关联 requirement_stage.id；组合时效可空）',
    `aging_subject_key` varchar(32)          DEFAULT NULL COMMENT '非空统计主体键；阶段时效取 stage_instance_id，需求级组合时取固定需求主体键',
    `aging_type`        varchar(30)          DEFAULT NULL COMMENT '阶段/需求受理/业务/研发等待/研发实施',
    `stage_code`        varchar(50)          DEFAULT NULL COMMENT '阶段编码',
    `stage_name`        varchar(100)         DEFAULT NULL COMMENT '阶段名称',
    `actual_start_time` datetime             DEFAULT NULL COMMENT '本时效区间实际开始时间',
    `actual_end_time`   datetime             DEFAULT NULL COMMENT '本时效区间实际结束时间/计算截止时间',
    `plan_days`         int                  DEFAULT NULL COMMENT '计划天数',
    `actual_days`       int                  DEFAULT NULL COMMENT '实际天数',
    `deduct_days`       int                  DEFAULT NULL COMMENT '扣减天数',
    `effective_days`    int                  DEFAULT NULL COMMENT '扣减后有效天数，不得小于0',
    `is_overdue`        tinyint              DEFAULT NULL COMMENT '是否超期',
    `calc_date`         date                 DEFAULT NULL COMMENT '计算日期',
    `create_time`       datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`    varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`        varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_req_aging_subj_date` (`data_tenant_id`, `req_id`, `aging_type`, `aging_subject_key`, `calc_date`),
    KEY                 `idx_calc_date_overdue` (`calc_date`, `is_overdue`),
    KEY                 `idx_req_aging_type` (`req_id`, `aging_type`),
    KEY                 `idx_stage_instance` (`stage_instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='阶段时效记录表';

-- ----------------------------
-- Table structure for baseline_record
-- ----------------------------
DROP TABLE IF EXISTS `baseline_record`;
CREATE TABLE `baseline_record`
(
    `id`                        varchar(32) NOT NULL COMMENT '主键',
    `req_id`                    varchar(32)          DEFAULT NULL COMMENT '需求ID (关联 requirement.id)',
    `baseline_month`            varchar(7)           DEFAULT NULL COMMENT '基线月份（yyyy-MM）',
    `baseline_type`             varchar(20)          DEFAULT NULL COMMENT '类型（正常基线/加塞）',
    `status`                    varchar(20)          DEFAULT NULL COMMENT '状态（基线中/已调出/已取消）',
    `include_date`              date                 DEFAULT NULL COMMENT '纳入基线日期',
    `original_plan_date`        date                 DEFAULT NULL COMMENT '原计划完成日期（纳入时保存）',
    `original_plan_recorded_at` datetime             DEFAULT NULL COMMENT '原计划首次录入时间，用于判断取消时是否删除',
    `plan_date_snapshot`        date                 DEFAULT NULL COMMENT '本次纳入/加塞时的计划完成日期快照',
    `filter_rule_id`            varchar(32)          DEFAULT NULL COMMENT '纳入时使用的基线规则版本ID（关联 baseline_rule_version.id）',
    `filter_version`            varchar(100)         DEFAULT NULL COMMENT '规则版本号快照',
    `squeeze_reason`            varchar(500)         DEFAULT NULL COMMENT '加塞原因',
    `create_time`               datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`               datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`            varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`                varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`                varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `version`                   int         NOT NULL DEFAULT 0 COMMENT '并发控制乐观锁版本',
    `delete_flag`               tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_req_month` (`data_tenant_id`, `req_id`, `baseline_month`),
    KEY                         `idx_month_status` (`baseline_month`, `status`),
    KEY                         `idx_filter_rule` (`filter_rule_id`),
    KEY                         `idx_include_date` (`include_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='基线记录表';

-- ----------------------------
-- Table structure for baseline_change_log
-- ----------------------------
DROP TABLE IF EXISTS `baseline_change_log`;
CREATE TABLE `baseline_change_log`
(
    `id`                 varchar(32) NOT NULL COMMENT '主键',
    `baseline_id`        varchar(32)          DEFAULT NULL COMMENT '基线记录ID (关联 baseline_record.id)',
    `req_id`             varchar(32)          DEFAULT NULL COMMENT '需求ID (关联 requirement.id)',
    `change_type`        varchar(30)          DEFAULT NULL COMMENT '变更类型（纳入基线/基线取消/加塞/取消加塞/基线调出/恢复）',
    `operator_id`        varchar(32)          DEFAULT NULL COMMENT '操作人ID (关联 admin_sm_user.USER_ID)',
    `change_reason`      varchar(500)         DEFAULT NULL COMMENT '变更原因',
    `before_status`      varchar(20)          DEFAULT NULL COMMENT '变更前状态',
    `after_status`       varchar(20)          DEFAULT NULL COMMENT '变更后状态',
    `plan_date_snapshot` date                 DEFAULT NULL COMMENT '变更时计划完成日期快照',
    `filter_rule_id`     varchar(32)          DEFAULT NULL COMMENT '本次判定使用的基线规则版本ID',
    `decision_snapshot`  json                 DEFAULT NULL COMMENT '命中过滤条件、关键输入和判定结果快照',
    `source_job_id`      varchar(32)          DEFAULT NULL COMMENT '触发作业/执行批次ID',
    `trace_id`           varchar(100)         DEFAULT NULL COMMENT '链路追踪号',
    `create_time`        datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`     varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                  `idx_baseline_id` (`baseline_id`),
    KEY                  `idx_req_id` (`req_id`),
    KEY                  `idx_operator_id` (`operator_id`),
    KEY                  `idx_create_time` (`create_time`),
    KEY                  `idx_trace_id` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='基线变更日志表';

-- ----------------------------
-- Table structure for aging_statistics
-- ----------------------------
DROP TABLE IF EXISTS `aging_statistics`;
CREATE TABLE `aging_statistics`
(
    `id`                 varchar(32) NOT NULL COMMENT '主键',
    `org_id`             varchar(32)          DEFAULT NULL COMMENT '组织/机构ID (关联 admin_sm_org.ORG_ID)',
    `stat_month`         varchar(7)           DEFAULT NULL COMMENT '统计月份（yyyy-MM）',
    `total_requirements` int                  DEFAULT NULL COMMENT '总需求数',
    `on_time_count`      int                  DEFAULT NULL COMMENT '按时完成数',
    `overdue_count`      int                  DEFAULT NULL COMMENT '超期数',
    `compliance_rate`    decimal(5, 2)        DEFAULT 0.00 COMMENT '时效合规率',
    `baseline_count`     int                  DEFAULT NULL COMMENT '基线需求数',
    `squeeze_count`      int                  DEFAULT NULL COMMENT '加塞需求数',
    `create_time`        datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`     varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`         varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`         varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`        tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_org_month` (`data_tenant_id`, `org_id`, `stat_month`),
    KEY                  `idx_stat_month` (`stat_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='时效统计表';

-- ----------------------------
-- Table structure for stage_deduction_request
-- ----------------------------
DROP TABLE IF EXISTS `stage_deduction_request`;
CREATE TABLE `stage_deduction_request`
(
    `id`                varchar(32) NOT NULL COMMENT '主键',
    `req_id`            varchar(32)          DEFAULT NULL COMMENT '需求ID（关联 requirement.id）',
    `stage_instance_id` varchar(32)          DEFAULT NULL COMMENT '阶段实例ID（关联 requirement_stage.id）',
    `deduct_days`       int                  DEFAULT NULL COMMENT '申请扣减天数',
    `deduct_reason`     varchar(500)         DEFAULT NULL COMMENT '扣减原因',
    `applicant_id`      varchar(32)          DEFAULT NULL COMMENT '申请人ID（通常为项目经理）',
    `apply_time`        datetime             DEFAULT NULL COMMENT '申请时间',
    `approval_status`   varchar(20)          DEFAULT NULL COMMENT '待审批/通过/拒绝/撤回',
    `approver_id`       varchar(32)          DEFAULT NULL COMMENT '审批人ID（通常为部落长）',
    `approval_time`     datetime             DEFAULT NULL COMMENT '审批时间',
    `approval_comment`  varchar(500)         DEFAULT NULL COMMENT '审批意见',
    `client_ip`         varchar(50)          DEFAULT NULL COMMENT '客户端IP',
    `trace_id`          varchar(100)         DEFAULT NULL COMMENT '链路追踪号',
    `create_time`       datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`    varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `updated_by`        varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`       tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                 `idx_req_id` (`req_id`),
    KEY                 `idx_stage_instance` (`stage_instance_id`),
    KEY                 `idx_applicant_id` (`applicant_id`),
    KEY                 `idx_approver_id` (`approver_id`),
    KEY                 `idx_approval_status` (`approval_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='阶段时效扣减申请表';

-- ----------------------------
-- Table structure for work_calendar
-- ----------------------------
DROP TABLE IF EXISTS `work_calendar`;
CREATE TABLE `work_calendar`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `calendar_code`  varchar(50)          DEFAULT NULL COMMENT '日历编码',
    `calendar_date`  date                 DEFAULT NULL COMMENT '日期',
    `is_workday`     tinyint              DEFAULT NULL COMMENT '是否工作日',
    `day_type`       varchar(20)          DEFAULT NULL COMMENT '工作日/周末/法定节假日/调休工作日',
    `description`    varchar(200)         DEFAULT NULL COMMENT '说明',
    `source_system`  varchar(50)          DEFAULT NULL COMMENT '来源系统/维护来源',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_cal_code_date` (`data_tenant_id`, `calendar_code`, `calendar_date`),
    KEY              `idx_calendar_date` (`calendar_date`),
    KEY              `idx_is_workday` (`is_workday`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='业务日历表';

-- ----------------------------
-- Table structure for baseline_rule_version
-- ----------------------------
DROP TABLE IF EXISTS `baseline_rule_version`;
CREATE TABLE `baseline_rule_version`
(
    `id`                     varchar(32) NOT NULL COMMENT '主键',
    `rule_version`           varchar(100)         DEFAULT NULL COMMENT '不可变规则版本号',
    `rule_name`              varchar(200)         DEFAULT NULL COMMENT '规则名称',
    `applicable_month_start` varchar(7)           DEFAULT NULL COMMENT '首个适用月份（yyyy-MM）',
    `predicate_definition`   json                 DEFAULT NULL COMMENT '已审批的结构化过滤条件；不得直接保存可执行SQL',
    `rule_digest`            varchar(128)         DEFAULT NULL COMMENT '规范化规则摘要',
    `rule_status`            varchar(20)          DEFAULT NULL COMMENT '草稿/已批准/生效/停用',
    `approved_by`            varchar(32)          DEFAULT NULL COMMENT '审批人ID',
    `approval_record_no`     varchar(100)         DEFAULT NULL COMMENT '审批记录号',
    `effective_start_time`   datetime             DEFAULT NULL COMMENT '生效时间',
    `effective_end_time`     datetime             DEFAULT NULL COMMENT '失效时间，可空',
    `create_time`            datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`         varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`             varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`             varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `update_time`            datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag`            tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_rule_ver` (`data_tenant_id`, `rule_version`),
    KEY                      `idx_status_eff` (`rule_status`, `effective_start_time`),
    KEY                      `idx_app_month` (`applicable_month_start`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='基线过滤规则版本表';

-- ----------------------------
-- Table structure for sync_requirement
-- ----------------------------
DROP TABLE IF EXISTS `sync_requirement`;
CREATE TABLE `sync_requirement`
(
    `id`                 varchar(32) NOT NULL COMMENT '主键',
    `source_system`      varchar(50)          DEFAULT NULL COMMENT '源系统',
    `source_req_id`      varchar(100)         DEFAULT NULL COMMENT '源系统需求ID',
    `target_req_id`      varchar(32)          DEFAULT NULL COMMENT '知效需求ID',
    `req_name`           varchar(500)         DEFAULT NULL COMMENT '需求名称',
    `source_status`      varchar(50)          DEFAULT NULL COMMENT '源系统状态',
    `target_status`      varchar(50)          DEFAULT NULL COMMENT '知效状态',
    `sync_status`        varchar(20)          DEFAULT NULL COMMENT '同步状态（待同步/已同步/同步中/同步失败/冲突）',
    `sync_version`       varchar(100)         DEFAULT NULL COMMENT '源对象版本/变更时间',
    `last_event_id`      varchar(100)         DEFAULT NULL COMMENT '最后成功处理事件ID',
    `last_sync_time`     datetime             DEFAULT NULL COMMENT '最后同步时间',
    `last_source_update` datetime             DEFAULT NULL COMMENT '源系统最后更新时间',
    `source_data`        json                 DEFAULT NULL COMMENT '源系统完整数据快照',
    `target_data`        json                 DEFAULT NULL COMMENT '目标系统数据快照',
    `sync_direction`     varchar(20)          DEFAULT NULL COMMENT '同步方向',
    `create_time`        datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`     varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`         varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`         varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`        tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_src_req` (`data_tenant_id`, `source_system`, `source_req_id`),
    KEY                  `idx_target_req_id` (`target_req_id`),
    KEY                  `idx_sync_status_time` (`sync_status`, `last_sync_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='同步需求对象表';

-- ----------------------------
-- Table structure for sync_task
-- ----------------------------
DROP TABLE IF EXISTS `sync_task`;
CREATE TABLE `sync_task`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `source_system`  varchar(50)          DEFAULT NULL COMMENT '源系统',
    `source_task_id` varchar(100)         DEFAULT NULL COMMENT '源系统任务ID',
    `target_task_id` varchar(32)          DEFAULT NULL COMMENT '知效任务ID',
    `task_name`      varchar(500)         DEFAULT NULL COMMENT '任务名称',
    `task_type`      varchar(50)          DEFAULT NULL COMMENT '任务类型',
    `source_status`  varchar(50)          DEFAULT NULL COMMENT '源系统状态',
    `target_status`  varchar(50)          DEFAULT NULL COMMENT '知效状态',
    `sync_status`    varchar(20)          DEFAULT NULL COMMENT '同步状态',
    `sync_version`   varchar(100)         DEFAULT NULL COMMENT '源对象版本/变更时间',
    `last_event_id`  varchar(100)         DEFAULT NULL COMMENT '最后成功处理事件ID',
    `last_sync_time` datetime             DEFAULT NULL COMMENT '最后同步时间',
    `source_data`    json                 DEFAULT NULL COMMENT '源系统数据快照',
    `target_data`    json                 DEFAULT NULL COMMENT '目标系统数据快照',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_src_task` (`data_tenant_id`, `source_system`, `source_task_id`),
    KEY              `idx_target_task_id` (`target_task_id`),
    KEY              `idx_sync_status_time` (`sync_status`, `last_sync_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='同步任务对象表';

-- ----------------------------
-- Table structure for sync_user
-- ----------------------------
DROP TABLE IF EXISTS `sync_user`;
CREATE TABLE `sync_user`
(
    `id`                   varchar(32) NOT NULL COMMENT '主键',
    `source_user_code`     varchar(100)         DEFAULT NULL COMMENT '外部系统工号',
    `target_user_id`       varchar(32)          DEFAULT NULL COMMENT '知效用户ID (关联 admin_sm_user.USER_ID)',
    `user_name`            varchar(100)         DEFAULT NULL COMMENT '用户名',
    `source_system`        varchar(50)          DEFAULT NULL COMMENT '源系统',
    `sync_status`          varchar(20)          DEFAULT NULL COMMENT '同步状态',
    `sync_version`         varchar(100)         DEFAULT NULL COMMENT '源对象版本/变更时间',
    `last_event_id`        varchar(100)         DEFAULT NULL COMMENT '最后成功处理事件ID',
    `last_sync_time`       datetime             DEFAULT NULL COMMENT '最后同步时间',
    `source_data`          json                 DEFAULT NULL COMMENT '源系统数据',
    `target_data`          json                 DEFAULT NULL COMMENT '目标系统数据',
    `is_mapping_confirmed` tinyint              DEFAULT NULL COMMENT '是否确认映射关系',
    `create_time`          datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`       varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`           varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`           varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`          tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_src_usr` (`data_tenant_id`, `source_system`, `source_user_code`),
    KEY                    `idx_target_user_id` (`target_user_id`),
    KEY                    `idx_sync_status` (`sync_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='同步用户对象表';

-- ----------------------------
-- Table structure for sync_organization
-- ----------------------------
DROP TABLE IF EXISTS `sync_organization`;
CREATE TABLE `sync_organization`
(
    `id`                   varchar(32) NOT NULL COMMENT '主键',
    `source_org_code`      varchar(100)         DEFAULT NULL COMMENT '外部组织编码',
    `target_org_id`        varchar(32)          DEFAULT NULL COMMENT '知效组织ID (关联 admin_sm_org.ORG_ID)',
    `org_name`             varchar(200)         DEFAULT NULL COMMENT '组织名称',
    `source_system`        varchar(50)          DEFAULT NULL COMMENT '源系统',
    `sync_status`          varchar(20)          DEFAULT NULL COMMENT '同步状态',
    `sync_version`         varchar(100)         DEFAULT NULL COMMENT '源对象版本/变更时间',
    `last_event_id`        varchar(100)         DEFAULT NULL COMMENT '最后成功处理事件ID',
    `last_sync_time`       datetime             DEFAULT NULL COMMENT '最后同步时间',
    `source_data`          json                 DEFAULT NULL COMMENT '源系统数据',
    `target_data`          json                 DEFAULT NULL COMMENT '目标系统数据',
    `is_mapping_confirmed` tinyint              DEFAULT NULL COMMENT '是否确认映射',
    `create_time`          datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`       varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`           varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`           varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`          tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_src_org` (`data_tenant_id`, `source_system`, `source_org_code`),
    KEY                    `idx_target_org_id` (`target_org_id`),
    KEY                    `idx_sync_status` (`sync_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='同步组织对象表';

-- ----------------------------
-- Table structure for sync_work_hour
-- ----------------------------
DROP TABLE IF EXISTS `sync_work_hour`;
CREATE TABLE `sync_work_hour`
(
    `id`               varchar(32) NOT NULL COMMENT '主键',
    `source_system`    varchar(50)          DEFAULT NULL COMMENT '源系统',
    `source_record_id` varchar(100)         DEFAULT NULL COMMENT '源系统报工ID',
    `target_record_id` varchar(32)          DEFAULT NULL COMMENT '知效报工ID',
    `user_id`          varchar(32)          DEFAULT NULL COMMENT '用户ID (关联 admin_sm_user.USER_ID)',
    `work_date`        date                 DEFAULT NULL COMMENT '报工日期',
    `work_minutes`     int                  DEFAULT NULL COMMENT '工作时长',
    `source_task_id`   varchar(100)         DEFAULT NULL COMMENT '源系统任务ID',
    `target_task_id`   varchar(32)          DEFAULT NULL COMMENT '知效任务ID',
    `sync_status`      varchar(20)          DEFAULT NULL COMMENT '同步状态',
    `sync_version`     varchar(100)         DEFAULT NULL COMMENT '源对象版本/变更时间',
    `last_event_id`    varchar(100)         DEFAULT NULL COMMENT '最后成功处理事件ID',
    `last_sync_time`   datetime             DEFAULT NULL COMMENT '最后同步时间',
    `source_data`      json                 DEFAULT NULL COMMENT '源系统数据快照',
    `target_data`      json                 DEFAULT NULL COMMENT '目标系统数据快照',
    `create_time`      datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`   varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`       varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`       varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`      tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_src_rec` (`data_tenant_id`, `source_system`, `source_record_id`),
    KEY                `idx_target_rec_id` (`target_record_id`),
    KEY                `idx_user_date` (`user_id`, `work_date`),
    KEY                `idx_sync_status` (`sync_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='同步报工对象表';

-- ----------------------------
-- Table structure for sync_security_task
-- ----------------------------
DROP TABLE IF EXISTS `sync_security_task`;
CREATE TABLE `sync_security_task`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `source_system`  varchar(50)          DEFAULT NULL COMMENT '源系统，当前为 ZHIAN',
    `source_task_id` varchar(100)         DEFAULT NULL COMMENT '知安任务ID',
    `target_task_id` varchar(32)          DEFAULT NULL COMMENT '知效安全检查任务ID',
    `requirement_id` varchar(32)          DEFAULT NULL COMMENT '关联需求ID',
    `scene`          text                 DEFAULT NULL COMMENT '安全场景',
    `source_status`  varchar(50)          DEFAULT NULL COMMENT '知安状态',
    `target_status`  varchar(50)          DEFAULT NULL COMMENT '知效状态',
    `sync_status`    varchar(20)          DEFAULT NULL COMMENT '同步状态',
    `sync_version`   varchar(100)         DEFAULT NULL COMMENT '源对象版本/确认版本',
    `last_event_id`  varchar(100)         DEFAULT NULL COMMENT '最后成功处理事件ID',
    `last_sync_time` datetime             DEFAULT NULL COMMENT '最后同步时间',
    `source_data`    json                 DEFAULT NULL COMMENT '知安数据快照',
    `target_data`    json                 DEFAULT NULL COMMENT '知效数据快照',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_src_sec_task` (`data_tenant_id`, `source_system`, `source_task_id`),
    KEY              `idx_target_task_id` (`target_task_id`),
    KEY              `idx_req_id` (`requirement_id`),
    KEY              `idx_sync_status` (`sync_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='同步安全任务表';

-- ----------------------------
-- Table structure for sync_conflict
-- ----------------------------
DROP TABLE IF EXISTS `sync_conflict`;
CREATE TABLE `sync_conflict`
(
    `id`                varchar(32) NOT NULL COMMENT '主键',
    `source_system`     varchar(50)          DEFAULT NULL COMMENT '源系统',
    `sync_object_type`  varchar(50)          DEFAULT NULL COMMENT '同步对象类型',
    `sync_object_id`    varchar(100)         DEFAULT NULL COMMENT '同步对象ID',
    `sync_event_id`     varchar(100)         DEFAULT NULL COMMENT '关联同步事件ID，可空',
    `conflict_type`     varchar(50)          DEFAULT NULL COMMENT '冲突类型',
    `conflict_field`    varchar(100)         DEFAULT NULL COMMENT '冲突字段',
    `source_value`      text                 DEFAULT NULL COMMENT '源系统值',
    `target_value`      text                 DEFAULT NULL COMMENT '目标系统值',
    `resolved_value`    text                 DEFAULT NULL COMMENT '解决后的值',
    `resolution_method` varchar(20)          DEFAULT NULL COMMENT '解决方式（自动/手动/覆盖/忽略）',
    `resolution_status` varchar(20)          DEFAULT NULL COMMENT '解决状态（待处理/已解决/已忽略）',
    `handler_id`        varchar(32)          DEFAULT NULL COMMENT '处理人ID (关联 admin_sm_user.USER_ID)',
    `handle_time`       datetime             DEFAULT NULL COMMENT '处理时间',
    `trace_id`          varchar(100)         DEFAULT NULL COMMENT '链路追踪号',
    `create_time`       datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`    varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`        varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`        varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`       tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                 `idx_src_obj` (`source_system`, `sync_object_type`, `sync_object_id`),
    KEY                 `idx_res_status` (`resolution_status`),
    KEY                 `idx_trace_id` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='同步冲突记录表';

-- ----------------------------
-- Table structure for sync_incremental_change
-- ----------------------------
DROP TABLE IF EXISTS `sync_incremental_change`;
CREATE TABLE `sync_incremental_change`
(
    `id`                      varchar(32) NOT NULL COMMENT '主键',
    `object_type`             varchar(50)          DEFAULT NULL COMMENT '对象类型',
    `object_id`               varchar(100)         DEFAULT NULL COMMENT '对象ID',
    `event_id`                varchar(100)         DEFAULT NULL COMMENT '来源事件ID/幂等键',
    `change_type`             varchar(20)          DEFAULT NULL COMMENT '变更类型（create/update/delete/status_change）',
    `change_fields`           json                 DEFAULT NULL COMMENT '变更字段列表',
    `before_value`            json                 DEFAULT NULL COMMENT '变更前值',
    `after_value`             json                 DEFAULT NULL COMMENT '变更后值',
    `source_system`           varchar(50)          DEFAULT NULL COMMENT '源系统',
    `change_time`             datetime             DEFAULT NULL COMMENT '变更时间',
    `capture_status`          varchar(20)          DEFAULT NULL COMMENT '已捕获/已生成投递事件',
    `generated_sync_event_id` varchar(32)          DEFAULT NULL COMMENT '生成的 sync_event_log.id，可空',
    `captured_transaction_id` varchar(100)         DEFAULT NULL COMMENT '本地业务事务标识',
    `published_time`          datetime             DEFAULT NULL COMMENT '转换为投递事件的时间',
    `create_time`             datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`          varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`              varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_src_event` (`data_tenant_id`, `source_system`, `event_id`),
    KEY                       `idx_cap_status_time` (`capture_status`, `create_time`),
    KEY                       `idx_tx_id` (`captured_transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='本地业务变更捕获（Outbox）表';

-- ----------------------------
-- Table structure for sync_batch_record
-- ----------------------------
DROP TABLE IF EXISTS `sync_batch_record`;
CREATE TABLE `sync_batch_record`
(
    `id`                   varchar(32) NOT NULL COMMENT '主键',
    `batch_no`             varchar(100)         DEFAULT NULL COMMENT '批次号',
    `batch_category`       varchar(20)          DEFAULT NULL COMMENT '同步/迁移/灰度补偿',
    `sync_type`            varchar(20)          DEFAULT NULL COMMENT '全量/增量',
    `direction`            varchar(20)          DEFAULT NULL COMMENT '方向（导入/导出/双向）',
    `object_type`          varchar(50)          DEFAULT NULL COMMENT '同步对象类型',
    `source_system`        varchar(50)          DEFAULT NULL COMMENT '源系统',
    `target_system`        varchar(50)          DEFAULT NULL COMMENT '目标系统',
    `total_count`          int                  DEFAULT NULL COMMENT '总对象数',
    `success_count`        int                  DEFAULT NULL COMMENT '成功数',
    `fail_count`           int                  DEFAULT NULL COMMENT '失败数',
    `conflict_count`       int                  DEFAULT NULL COMMENT '冲突数',
    `batch_status`         varchar(20)          DEFAULT NULL COMMENT '批次状态（进行中/已完成/失败）',
    `current_step`         varchar(50)          DEFAULT NULL COMMENT '当前迁移/同步步骤',
    `checkpoint_value`     varchar(500)         DEFAULT NULL COMMENT '已确认检查点/水位',
    `input_digest`         varchar(128)         DEFAULT NULL COMMENT '输入摘要，用于重跑一致性确认',
    `operator_id`          varchar(32)          DEFAULT NULL COMMENT '启动操作人ID',
    `rollback_status`      varchar(20)          DEFAULT NULL COMMENT '未回滚/回滚中/已回滚/回滚失败',
    `rollback_operator_id` varchar(32)          DEFAULT NULL COMMENT '回滚操作人ID',
    `rollback_reason`      varchar(500)         DEFAULT NULL COMMENT '回滚原因',
    `start_time`           datetime             DEFAULT NULL COMMENT '开始时间',
    `end_time`             datetime             DEFAULT NULL COMMENT '结束时间',
    `error_msg`            text                 DEFAULT NULL COMMENT '错误信息',
    `create_time`          datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`       varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `updated_by`           varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `update_time`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag`          tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_batch_no` (`data_tenant_id`, `batch_no`),
    KEY                    `idx_batch_status` (`batch_status`),
    KEY                    `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='同步批次记录表';

-- ----------------------------
-- Table structure for sync_mapping_relation
-- ----------------------------
DROP TABLE IF EXISTS `sync_mapping_relation`;
CREATE TABLE `sync_mapping_relation`
(
    `id`             varchar(32) NOT NULL COMMENT '主键',
    `source_system`  varchar(50)          DEFAULT NULL COMMENT '源系统',
    `target_system`  varchar(50)          DEFAULT NULL COMMENT '目标系统',
    `object_type`    varchar(50)          DEFAULT NULL COMMENT '对象类型',
    `source_field`   varchar(100)         DEFAULT NULL COMMENT '源字段',
    `target_field`   varchar(100)         DEFAULT NULL COMMENT '目标字段',
    `mapping_type`   varchar(20)          DEFAULT NULL COMMENT '映射类型（direct/enum/expression/custom）',
    `mapping_rule`   json                 DEFAULT NULL COMMENT '映射规则',
    `is_active`      tinyint              DEFAULT NULL COMMENT '是否启用',
    `create_time`    datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id` varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`     varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`     varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`    tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_src_tgt_field` (`data_tenant_id`, `source_system`, `target_system`, `object_type`, `source_field`),
    KEY              `idx_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='同步映射关系表';

-- ----------------------------
-- Table structure for sync_config
-- ----------------------------
DROP TABLE IF EXISTS `sync_config`;
CREATE TABLE `sync_config`
(
    `id`                   varchar(32) NOT NULL COMMENT '主键',
    `source_system`        varchar(50)          DEFAULT NULL COMMENT '源系统',
    `target_system`        varchar(50)          DEFAULT NULL COMMENT '目标系统',
    `object_type`          varchar(50)          DEFAULT NULL COMMENT '同步对象类型',
    `sync_strategy`        varchar(20)          DEFAULT NULL COMMENT '同步策略（全量替换/增量追加/增量更新/双向同步）',
    `sync_frequency`       varchar(20)          DEFAULT NULL COMMENT '同步频率（real_time/hourly/daily/weekly/manual）',
    `conflict_strategy`    varchar(20)          DEFAULT NULL COMMENT '冲突策略（source_win/target_win/latest_win/manual）',
    `retry_count`          int                  DEFAULT NULL COMMENT '失败重试次数',
    `sync_window_start`    time                 DEFAULT NULL COMMENT '同步窗口开始时间',
    `sync_window_end`      time                 DEFAULT NULL COMMENT '同步窗口结束时间',
    `is_active`            tinyint              DEFAULT NULL COMMENT '是否启用',
    `filter_condition`     json                 DEFAULT NULL COMMENT '过滤条件',
    `config_version`       varchar(50)          DEFAULT NULL COMMENT '配置发布版本',
    `effective_start_time` datetime             DEFAULT NULL COMMENT '生效时间',
    `effective_end_time`   datetime             DEFAULT NULL COMMENT '失效时间',
    `approved_by`          varchar(32)          DEFAULT NULL COMMENT '配置审批人ID',
    `create_time`          datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`       varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`           varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`           varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `version`              int         NOT NULL DEFAULT 0 COMMENT '并发控制乐观锁版本',
    `delete_flag`          tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_cfg_ver` (`data_tenant_id`, `source_system`, `target_system`, `object_type`, `config_version`),
    KEY                    `idx_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='同步配置表';

-- ----------------------------
-- Table structure for sync_event_log
-- ----------------------------
DROP TABLE IF EXISTS `sync_event_log`;
CREATE TABLE `sync_event_log`
(
    `id`                    varchar(32) NOT NULL COMMENT '主键',
    `batch_id`              varchar(32)          DEFAULT NULL COMMENT '所属同步/迁移/灰度批次ID（关联 sync_batch_record.id，可空）',
    `incremental_change_id` varchar(32)          DEFAULT NULL COMMENT '来源 Outbox 记录ID（关联 sync_incremental_change.id，可空）',
    `event_id`              varchar(100)         DEFAULT NULL COMMENT '事件ID/幂等业务键',
    `source_system`         varchar(50)          DEFAULT NULL COMMENT '源系统',
    `target_system`         varchar(50)          DEFAULT NULL COMMENT '目标系统',
    `direction`             varchar(20)          DEFAULT NULL COMMENT 'INBOUND/OUTBOUND',
    `object_type`           varchar(50)          DEFAULT NULL COMMENT 'REQUIREMENT/TASK/WORK_HOUR/SECURITY/ATTENDANCE等',
    `source_object_id`      varchar(100)         DEFAULT NULL COMMENT '源对象ID',
    `target_object_id`      varchar(32)          DEFAULT NULL COMMENT '目标对象ID，可空',
    `source_version`        varchar(100)         DEFAULT NULL COMMENT '源版本/变更时间',
    `payload_digest`        varchar(128)         DEFAULT NULL COMMENT '原载荷摘要',
    `payload_snapshot`      json                 DEFAULT NULL COMMENT '脱敏后的原载荷；敏感内容按安全方案处理',
    `process_status`        varchar(20)          DEFAULT NULL COMMENT '待处理/处理中/成功/失败/冲突/人工处理',
    `retry_count`           int                  DEFAULT NULL COMMENT '已重试次数',
    `max_retry_count`       int                  DEFAULT NULL COMMENT '最大重试次数',
    `next_retry_time`       datetime             DEFAULT NULL COMMENT '下次重试时间',
    `error_code`            varchar(50)          DEFAULT NULL COMMENT '错误码',
    `error_message`         varchar(2000)        DEFAULT NULL COMMENT '错误原因',
    `handler_id`            varchar(32)          DEFAULT NULL COMMENT '人工处理人ID，可空',
    `handled_time`          datetime             DEFAULT NULL COMMENT '人工处理时间',
    `trace_id`              varchar(100)         DEFAULT NULL COMMENT '链路追踪号',
    `first_received_time`   datetime             DEFAULT NULL COMMENT '首次接收时间',
    `last_process_time`     datetime             DEFAULT NULL COMMENT '最后处理时间',
    `create_time`           datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`        varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`            varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_src_event_dir` (`data_tenant_id`, `source_system`, `event_id`, `direction`),
    KEY                     `idx_batch_proc_status` (`batch_id`, `process_status`),
    KEY                     `idx_proc_retry_time` (`process_status`, `next_retry_time`),
    KEY                     `idx_obj_type_id` (`object_type`, `source_object_id`),
    KEY                     `idx_trace_id` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='外部同步事件与投递日志表';

-- ----------------------------
-- Table structure for migration_batch_item
-- ----------------------------
DROP TABLE IF EXISTS `migration_batch_item`;
CREATE TABLE `migration_batch_item`
(
    `id`                    varchar(32) NOT NULL COMMENT '主键',
    `batch_id`              varchar(32)          DEFAULT NULL COMMENT '批次ID（关联 sync_batch_record.id）',
    `step_code`             varchar(50)          DEFAULT NULL COMMENT '组织人员/需求阶段/任务/报工/基线等步骤',
    `object_type`           varchar(50)          DEFAULT NULL COMMENT '迁移对象类型',
    `source_object_id`      varchar(100)         DEFAULT NULL COMMENT '源对象ID',
    `target_object_id`      varchar(32)          DEFAULT NULL COMMENT '目标对象ID，可空',
    `target_storage`        varchar(50)          DEFAULT NULL COMMENT '活动库/归档存储/数据仓库',
    `operation_type`        varchar(20)          DEFAULT NULL COMMENT 'INSERT/UPDATE/RELATE/ARCHIVE；决定补偿方式',
    `source_version`        varchar(100)         DEFAULT NULL COMMENT '本次迁移读取的源对象版本',
    `target_version_before` varchar(100)         DEFAULT NULL COMMENT '写入前目标对象版本，可空',
    `target_version_after`  varchar(100)         DEFAULT NULL COMMENT '写入后目标对象版本，可空',
    `before_image`          json                 DEFAULT NULL COMMENT '更新/关联前的目标关键字段快照；敏感字段脱敏或加密',
    `process_status`        varchar(20)          DEFAULT NULL COMMENT '待处理/成功/失败/排除/已回滚',
    `source_digest`         varchar(128)         DEFAULT NULL COMMENT '源关键字段摘要',
    `target_digest`         varchar(128)         DEFAULT NULL COMMENT '目标关键字段摘要',
    `validation_status`     varchar(20)          DEFAULT NULL COMMENT '未校验/一致/不一致',
    `error_message`         varchar(2000)        DEFAULT NULL COMMENT '错误或排除原因',
    `rollback_status`       varchar(20)          DEFAULT NULL COMMENT '未回滚/已回滚/回滚失败',
    `compensation_action`   varchar(30)          DEFAULT NULL COMMENT 'DELETE_CREATED/RESTORE_BEFORE/UNLINK/RESTORE_ARCHIVE_POINTER',
    `compensation_order`    bigint               DEFAULT NULL COMMENT '批次内逆序补偿序号，依赖对象后写先回滚',
    `compensation_digest`   varchar(128)         DEFAULT NULL COMMENT '补偿输入摘要，保证重复回滚幂等',
    `compensated_time`      datetime             DEFAULT NULL COMMENT '补偿完成时间',
    `processed_time`        datetime             DEFAULT NULL COMMENT '处理时间',
    `create_time`           datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`        varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`            varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_batch_step_obj` (`data_tenant_id`, `batch_id`, `step_code`, `object_type`, `source_object_id`),
    KEY                     `idx_batch_comp_seq` (`batch_id`, `compensation_order`),
    KEY                     `idx_proc_val_roll` (`process_status`, `validation_status`, `rollback_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='迁移批次对象明细表';

-- ----------------------------
-- Table structure for gray_release_rule
-- ----------------------------
DROP TABLE IF EXISTS `gray_release_rule`;
CREATE TABLE `gray_release_rule`
(
    `id`                   varchar(32) NOT NULL COMMENT '主键',
    `rule_version`         varchar(50)          DEFAULT NULL COMMENT '规则版本号',
    `dimension_type`       varchar(20)          DEFAULT NULL COMMENT 'VENDOR/SYSTEM/TRIBE',
    `dimension_id`         varchar(100)         DEFAULT NULL COMMENT '外包厂商编码、系统ID或部落ID',
    `route_target`         varchar(20)          DEFAULT NULL COMMENT 'ZHIXIAO/ZHIWEI',
    `priority`             int                  DEFAULT NULL COMMENT '命中优先级；数值越小优先级越高',
    `effective_start_time` datetime             DEFAULT NULL COMMENT '生效时间',
    `effective_end_time`   datetime             DEFAULT NULL COMMENT '失效时间',
    `rule_status`          varchar(20)          DEFAULT NULL COMMENT '草稿/已批准/生效/停用/回滚',
    `approval_record_no`   varchar(100)         DEFAULT NULL COMMENT '发布审批记录号',
    `operator_id`          varchar(32)          DEFAULT NULL COMMENT '操作人ID',
    `operation_reason`     varchar(500)         DEFAULT NULL COMMENT '变更或回滚原因',
    `trace_id`             varchar(100)         DEFAULT NULL COMMENT '链路追踪号',
    `create_time`          datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`       varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `updated_by`           varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `update_time`          datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag`          tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_rule_ver_dim` (`data_tenant_id`, `rule_version`, `dimension_type`, `dimension_id`),
    KEY                    `idx_status_time` (`rule_status`, `effective_start_time`, `effective_end_time`),
    KEY                    `idx_dim_prio` (`dimension_type`, `dimension_id`, `priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='灰度发布规则版本表';

-- ----------------------------
-- Table structure for gray_release_batch
-- ----------------------------
DROP TABLE IF EXISTS `gray_release_batch`;
CREATE TABLE `gray_release_batch`
(
    `id`                          varchar(32) NOT NULL COMMENT '主键',
    `batch_no`                    varchar(100)         DEFAULT NULL COMMENT '灰度/扩围批次号',
    `parent_batch_id`             varchar(32)          DEFAULT NULL COMMENT '上一扩围批次ID，可空',
    `release_wave_no`             int                  DEFAULT NULL COMMENT '扩围波次序号',
    `rule_version`                varchar(100)         DEFAULT NULL COMMENT '本批次冻结的灰度规则版本',
    `scope_snapshot`              json                 DEFAULT NULL COMMENT '厂商、系统、部落及排除范围的不可变快照',
    `route_priority_snapshot`     json                 DEFAULT NULL COMMENT '多维命中优先级和冲突规则快照',
    `rollback_threshold_snapshot` json                 DEFAULT NULL COMMENT 'P0、工时偏差、同步中断等量化门槛快照',
    `approval_record_no`          varchar(100)         DEFAULT NULL COMMENT '发布审批记录号',
    `approved_by`                 varchar(32)          DEFAULT NULL COMMENT '发布审批人ID',
    `approved_time`               datetime             DEFAULT NULL COMMENT '审批时间',
    `batch_status`                varchar(20)          DEFAULT NULL COMMENT '待审批/待执行/执行中/观察中/通过/停止扩围/回滚中/已回滚',
    `gate_result`                 varchar(20)          DEFAULT NULL COMMENT '未评估/通过/拒绝/人工豁免',
    `gate_evaluated_time`         datetime             DEFAULT NULL COMMENT '最近门禁评估时间',
    `rollback_reason`             varchar(500)         DEFAULT NULL COMMENT '回滚原因，可空',
    `rollback_approval_no`        varchar(100)         DEFAULT NULL COMMENT '回滚审批记录号，可空',
    `start_time`                  datetime             DEFAULT NULL COMMENT '批次开始时间',
    `observation_end_time`        datetime             DEFAULT NULL COMMENT '观察窗结束时间',
    `end_time`                    datetime             DEFAULT NULL COMMENT '批次结束时间',
    `create_time`                 datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`                 datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `data_tenant_id`              varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`                  varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`                  varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `delete_flag`                 tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_batch_no` (`data_tenant_id`, `batch_no`),
    KEY                           `idx_status_start` (`batch_status`, `start_time`),
    KEY                           `idx_parent_wave` (`parent_batch_id`, `release_wave_no`),
    KEY                           `idx_gate_result` (`gate_result`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='灰度发布与扩围批次表';

-- ----------------------------
-- Table structure for gray_release_metric_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `gray_release_metric_snapshot`;
CREATE TABLE `gray_release_metric_snapshot`
(
    `id`                 varchar(32) NOT NULL COMMENT '主键',
    `gray_batch_id`      varchar(32)          DEFAULT NULL COMMENT '灰度批次ID（关联 gray_release_batch.id）',
    `observation_time`   datetime             DEFAULT NULL COMMENT '观测时点',
    `window_start_time`  datetime             DEFAULT NULL COMMENT '指标窗口开始时间',
    `window_end_time`    datetime             DEFAULT NULL COMMENT '指标窗口结束时间',
    `metric_code`        varchar(50)          DEFAULT NULL COMMENT 'P0_COUNT/WORK_HOUR_DEVIATION/SYNC_INTERRUPTION等',
    `metric_value`       decimal(20, 6)       DEFAULT 0.00 COMMENT '实际指标值',
    `threshold_operator` varchar(10)          DEFAULT NULL COMMENT 'LT/LE/EQ/GE/GT',
    `threshold_value`    decimal(20, 6)       DEFAULT 0.00 COMMENT '本批次门槛值',
    `metric_unit`        varchar(20)          DEFAULT NULL COMMENT 'COUNT/PERCENT/MINUTE/SECOND等',
    `gate_result`        varchar(20)          DEFAULT NULL COMMENT '通过/拒绝/数据不足',
    `evidence_source`    varchar(100)         DEFAULT NULL COMMENT '指标来源系统/监控项',
    `evidence_digest`    varchar(128)         DEFAULT NULL COMMENT '原始证据摘要',
    `trace_id`           varchar(100)         DEFAULT NULL COMMENT '链路追踪号',
    `create_time`        datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`     varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`         varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_batch_obs_metric` (`data_tenant_id`, `gray_batch_id`, `observation_time`, `metric_code`),
    KEY                  `idx_batch_metric_win` (`gray_batch_id`, `metric_code`, `window_start_time`, `window_end_time`),
    KEY                  `idx_gate_result` (`gate_result`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='灰度观测指标证据表';

-- ----------------------------
-- Table structure for gray_route_decision
-- ----------------------------
DROP TABLE IF EXISTS `gray_route_decision`;
CREATE TABLE `gray_route_decision`
(
    `id`                     varchar(32) NOT NULL COMMENT '主键',
    `gray_batch_id`          varchar(32)          DEFAULT NULL COMMENT '灰度批次ID（关联 gray_release_batch.id）',
    `user_id`                varchar(32)          DEFAULT NULL COMMENT '用户ID（关联 admin_sm_user.USER_ID）',
    `employee_no_snapshot`   varchar(100)         DEFAULT NULL COMMENT '裁决时工号快照',
    `work_date`              date                 DEFAULT NULL COMMENT '报工日期',
    `matched_rule_id`        varchar(32)          DEFAULT NULL COMMENT '命中的 gray_release_rule.id，可空',
    `rule_version`           varchar(100)         DEFAULT NULL COMMENT '实际命中的规则版本',
    `matched_dimension`      varchar(20)          DEFAULT NULL COMMENT 'VENDOR/SYSTEM/TRIBE/DEFAULT',
    `route_target`           varchar(20)          DEFAULT NULL COMMENT 'ZHIXIAO/ZHIWEI',
    `zhixiao_dataset_digest` varchar(128)         DEFAULT NULL COMMENT '知效候选明细集摘要',
    `zhiwei_dataset_digest`  varchar(128)         DEFAULT NULL COMMENT '知微候选明细集摘要',
    `authoritative_source`   varchar(20)          DEFAULT NULL COMMENT '最终采用的整套来源 ZHIXIAO/ZHIWEI/NONE',
    `selected_record_count`  int                  DEFAULT NULL COMMENT '采用来源的明细条数',
    `selected_total_minutes` int                  DEFAULT NULL COMMENT '采用来源的分钟合计',
    `decision_status`        varchar(20)          DEFAULT NULL COMMENT '已裁决/冲突/待人工处理',
    `decision_reason`        varchar(500)         DEFAULT NULL COMMENT '优先级、缺失或冲突说明',
    `source_event_id`        varchar(100)         DEFAULT NULL COMMENT '触发裁决的事件ID',
    `decided_time`           datetime             DEFAULT NULL COMMENT '裁决时间',
    `create_time`            datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`         varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`             varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`             varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `update_time`            datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag`            tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_batch_usr_date` (`data_tenant_id`, `gray_batch_id`, `user_id`, `work_date`),
    KEY                      `idx_user_date` (`user_id`, `work_date`),
    KEY                      `idx_rule_ver` (`rule_version`),
    KEY                      `idx_decision_status` (`decision_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='灰度报工来源路由与去重裁决表';

-- ----------------------------
-- Table structure for archive_object_locator
-- ----------------------------
DROP TABLE IF EXISTS `archive_object_locator`;
CREATE TABLE `archive_object_locator`
(
    `id`                     varchar(32) NOT NULL COMMENT '主键',
    `object_type`            varchar(50)          DEFAULT NULL COMMENT 'WORK_HOUR_DETAIL/REQUIREMENT/TASK/BASELINE等',
    `source_system`          varchar(50)          DEFAULT NULL COMMENT '来源系统',
    `source_object_id`       varchar(100)         DEFAULT NULL COMMENT '来源对象或分区标识',
    `archive_batch_id`       varchar(32)          DEFAULT NULL COMMENT '迁移/归档批次ID（关联 sync_batch_record.id）',
    `storage_type`           varchar(30)          DEFAULT NULL COMMENT 'ARCHIVE_DB/OBJECT_STORAGE/DATA_WAREHOUSE',
    `storage_locator`        varchar(1000)        DEFAULT NULL COMMENT '库表分区、对象键或受控查询定位符',
    `archive_policy_version` varchar(100)         DEFAULT NULL COMMENT '近14天活动库/历史汇总等归档策略版本',
    `record_count`           bigint               DEFAULT NULL COMMENT '归档记录数',
    `content_digest`         varchar(128)         DEFAULT NULL COMMENT '归档内容摘要',
    `validation_status`      varchar(20)          DEFAULT NULL COMMENT '未校验/一致/不一致',
    `retrieval_status`       varchar(20)          DEFAULT NULL COMMENT '可检索/冻结/已销毁',
    `archived_time`          datetime             DEFAULT NULL COMMENT '归档完成时间',
    `retention_until`        datetime             DEFAULT NULL COMMENT '最短保留期限',
    `last_restore_test_time` datetime             DEFAULT NULL COMMENT '最近恢复/检索演练时间',
    `create_time`            datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`         varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    `created_by`             varchar(32)          DEFAULT NULL COMMENT '创建人ID',
    `updated_by`             varchar(32)          DEFAULT NULL COMMENT '修改人ID',
    `update_time`            datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_flag`            tinyint     NOT NULL DEFAULT 0 COMMENT '逻辑删除标识（0正常/1删除）',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_tenant_src_obj_pol` (`data_tenant_id`, `source_system`, `object_type`, `source_object_id`, `archive_policy_version`),
    KEY                      `idx_archive_batch` (`archive_batch_id`),
    KEY                      `idx_storage_type_status` (`storage_type`, `retrieval_status`),
    KEY                      `idx_retention_until` (`retention_until`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='迁移归档对象定位表';

-- ----------------------------
-- Table structure for business_audit_log
-- ----------------------------
DROP TABLE IF EXISTS `business_audit_log`;
CREATE TABLE `business_audit_log`
(
    `id`               varchar(32) NOT NULL COMMENT '主键',
    `audit_category`   varchar(30)          DEFAULT NULL COMMENT '工时/基线/扣减/权限/同步配置/迁移/灰度/回滚',
    `operation_type`   varchar(50)          DEFAULT NULL COMMENT '具体操作类型',
    `object_type`      varchar(50)          DEFAULT NULL COMMENT '操作对象类型',
    `object_id`        varchar(100)         DEFAULT NULL COMMENT '操作对象ID',
    `operator_id`      varchar(32)          DEFAULT NULL COMMENT '操作人ID（关联 admin_sm_user.USER_ID）',
    `operator_org_id`  varchar(32)          DEFAULT NULL COMMENT '操作时所属机构ID',
    `client_ip`        varchar(50)          DEFAULT NULL COMMENT '客户端IP',
    `before_value`     json                 DEFAULT NULL COMMENT '操作前值/摘要',
    `after_value`      json                 DEFAULT NULL COMMENT '操作后值/摘要',
    `operation_reason` varchar(500)         DEFAULT NULL COMMENT '操作原因',
    `operation_result` varchar(20)          DEFAULT NULL COMMENT '成功/失败/部分成功',
    `error_code`       varchar(50)          DEFAULT NULL COMMENT '失败错误码',
    `trace_id`         varchar(100)         DEFAULT NULL COMMENT '链路追踪号',
    `operation_time`   datetime             DEFAULT NULL COMMENT '操作时间',
    `create_time`      datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `data_tenant_id`   varchar(32) NOT NULL DEFAULT '1' COMMENT '数据所属租户id',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                `idx_cat_time` (`audit_category`, `operation_time`),
    KEY                `idx_obj_type_id` (`object_type`, `object_id`),
    KEY                `idx_oper_time` (`operator_id`, `operation_time`),
    KEY                `idx_trace_id` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='跨域业务审计日志表';
