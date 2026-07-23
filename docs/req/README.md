# 知效平台开发任务拆解规范与总览目录 (Requirements & Tasks Index)

> **基线需求文档**：`docs/prd/normalized/01_PRD_产品需求文档.md` (V1.0)  
> **技术设计基线**：`docs/prd/normalized/02_TechSpec_技术设计方案.md` (V2.0)  
> **接口规范基线**：`docs/prd/normalized/03_APIDoc_API接口规范文档.md` (V2.0)  
> **数据库设计基线**：`docs/prd/normalized/04_DBDesign_数据库设计说明书.md` (V2.0)  
> **任务拆解版本**：V2.0-FINAL（语义化编码版）  
> **发布日期**：2026-07-24  

---

## 1. 拆解说明与规范

按照业务与系统架构对齐原则，本文档集将 PRD 全量需求规范化拆解为 **17 个语义化开发任务包**（如 `PERM-001`, `REQ-001`, `TASK-001`, `WH-001`, `BASE-001`, `INTEG-001`, `SYS-001` 等）。

每个任务包包含 5 个标准化结构文件：
1. `normalized/requirements.md` — 包含功能需求 (`FR-xxx`) 与非功能需求 (`NFR-xxx`)
2. `normalized/business-rules.md` — 包含业务规则、校验逻辑与数据约束 (`BR-xxx`)
3. `normalized/acceptance-criteria.md` — 包含功能验收标准与边界测试场景 (`AC-xxx`)
4. `normalized/open-questions.md` — 包含原始需求遗留待确认事项及设计/验收门禁 (`Q-xxx`)
5. `normalized/source-map.md` — 包含溯源映射（PRD 章节、TechSpec 组件、API 编号及 GoldenDB 表）

---

## 2. 17 个语义化子功能任务包目录索引

| 任务包编号 | 任务包名称 | 对应 PRD 章节 | 对应 API 范围 | GoldenDB 核心表 |
|---|---|---|---|---|
| [PERM-001](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/PERM-001/normalized/requirements.md) | 角色与数据权限管理 | 3.1 ~ 3.5 | `IF-082` ~ `IF-088` | `oca_user`, `oca_org`, `oca_role`, `oca_user_role` |
| [REQ-001](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/REQ-001/normalized/requirements.md) | 需求管理 - 视图、泳道与甘特图 | 4.1, 4.2, 4.5 | `IF-001` ~ `IF-006` | `requirement`, `requirement_stage` |
| [REQ-002](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/REQ-002/normalized/requirements.md) | 需求管理 - 需求详情与列表筛选 | 4.3, 4.4 | `IF-007` ~ `IF-012` | `requirement`, `requirement_detail`, `requirement_operation_log` |
| [REQ-003](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/REQ-003/normalized/requirements.md) | 需求管理 - 固定横幅与指标统计 | 4.6 | `IF-013` ~ `IF-015` | `requirement`, `baseline_record` |
| [REQ-004](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/REQ-004/normalized/requirements.md) | 需求管理 - 自动拖卡机制 | 4.8 | `IF-016` ~ `IF-018`, `IF-100`, `IF-101` | `requirement`, `requirement_stage`, `sync_event_log` |
| [FIELD-001](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/FIELD-001/normalized/requirements.md) | 需求与任务 - 自定义字段管理 | 4.7, 5.9 | `IF-019` ~ `IF-022`, `IF-045` ~ `IF-048` | `custom_field_config`, `custom_field_value` |
| [REQ-005](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/REQ-005/normalized/requirements.md) | 需求管理 - 风险、时效与归档 | 4.9 | `IF-023` ~ `IF-028` | `requirement_risk`, `stage_aging_record`, `requirement` |
| [TASK-001](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/TASK-001/normalized/requirements.md) | 任务管理 - 任务类型与业务对象 | 5.1 ~ 5.5 | `IF-029` ~ `IF-036`, `IF-102` | `task`, `task_sub`, `security_check_task` |
| [TASK-002](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/TASK-002/normalized/requirements.md) | 任务管理 - 任务分发、认领与流转 | 5.6, 5.8 | `IF-037` ~ `IF-042` | `task`, `task_assignment_log` |
| [TASK-003](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/TASK-003/normalized/requirements.md) | 任务管理 - 个人子任务与打卡点亮 | 5.7 | `IF-043` ~ `IF-044` | `task_sub`, `user_task_lighting_log` |
| [WH-001](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/WH-001/normalized/requirements.md) | 报工管理 - 考勤同步与默认工时生成 | 6.2, 6.3 | `IF-051`, `IF-052`, `IF-103` | `attendance_sync_record`, `work_hour_record`, `user_work_day` |
| [WH-002](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/WH-002/normalized/requirements.md) | 报工管理 - 我的报工与填报限制 | 6.4 | `IF-053` ~ `IF-058` | `work_hour_record`, `work_hour_log` |
| [WH-003](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/WH-003/normalized/requirements.md) | 报工管理 - 报工查看与知脉同步/系统切换 | 6.5, 6.6, 6.7 | `IF-059` ~ `IF-065`, `IF-104` | `work_hour_record`, `sync_work_hour_log` |
| [BASE-001](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/BASE-001/normalized/requirements.md) | 时效与基线 - 阶段时效与扣减/回退 | 7.1, 7.2 | `IF-066` ~ `IF-071` | `requirement_stage`, `stage_aging_record`, `stage_deduction_request` |
| [BASE-002](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/BASE-002/normalized/requirements.md) | 时效与基线 - 基线、调出、加塞与原计划 | 7.3 ~ 7.6 | `IF-072` ~ `IF-081` | `baseline_record`, `baseline_change_log`, `requirement_plan` |
| [INTEG-001](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/INTEG-001/normalized/requirements.md) | 外部系统集成与数据迁移 | 8.1, 8.2, 8.3 | `IF-100` ~ `IF-108` | `sync_event_log`, `sync_conflict`, `historical_migration_record` |
| [SYS-001](file:///Users/xuuyin/Dev/IdeaProjects/yusp-plus/docs/requirements/SYS-001/normalized/requirements.md) | 非功能、安全与合规要求 | 9.1 ~ 9.3 | 全局切面 & NFR | `modify_trace_log`, System Architecture |

---

## 3. 稳定标识符（Stable Identifiers）命名规约

- **`FR-xxx`**：功能需求 (Functional Requirement)
- **`BR-xxx`**：业务规则 (Business Rule)
- **`NFR-xxx`**：非功能需求 (Non-Functional Requirement)
- **`AC-xxx`**：验收条件 (Acceptance Criteria)
- **`Q-xxx`**：待确认事项 (Open Question & Gate)
