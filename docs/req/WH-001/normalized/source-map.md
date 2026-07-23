# WH-001 报工管理 - 考勤同步与默认工时生成 - 溯源映射表 (source-map.md)

> **任务包编号**：WH-001  
> **任务包名称**：报工管理 - 考勤同步与默认工时生成  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-111** | PRD 6.2 考勤数据同步 | `yusp-plus-job-core` (J02) | `IF-103` | `attendance_sync_record` |
| **FR-112** | PRD 6.2, 6.3 默认工时 | `yusp-plus-job-core` (J02, J03) | `IF-051` | `user_work_day` |
| **FR-113** | PRD 6.3 默认工时生成 | `WorkHourServiceImpl` | `IF-052` | `work_hour_record` |
| **FR-114** | PRD 6.3 默认工时生成 | `WorkHourServiceImpl` | `IF-052` | `work_hour_record` |
| **FR-115** | PRD 6.3 默认工时生成 | `WorkHourServiceImpl` | `IF-052` | `work_hour_record` |
| **BR-116** | PRD 6.2 考勤数据同步 | `yusp-plus-job-core` | `IF-103` | `attendance_sync_record` |
| **BR-117** | PRD 6.2, 6.3 调度时点 | `yusp-plus-job-core` | `IF-051` | `work_hour_record` |
| **BR-118** | PRD 6.3 均摊算法 | `WorkHourServiceImpl` | `IF-052` | `work_hour_record` |
| **BR-119** | PRD 6.3 默认工时 | `WorkHourServiceImpl` | `IF-052` | `work_hour_record` |
