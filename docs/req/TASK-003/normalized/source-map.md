# TASK-003 任务管理 - 个人子任务与打卡点亮 - 溯源映射表 (source-map.md)

> **任务包编号**：TASK-003  
> **任务包名称**：任务管理 - 个人子任务与打卡点亮  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-101** | PRD 5.7 点亮 | `TaskController` | `IF-043` | `task_sub`, `user_task_lighting_log` |
| **FR-102** | PRD 5.7 点亮 | `TaskServiceImpl` | `IF-043` | `user_task_lighting_log` |
| **FR-103** | PRD 5.7 点亮 | `TaskServiceImpl` | `IF-043` | `task_sub` |
| **FR-104** | PRD 5.7 报工联动 | `yusp-plus-oca-web2.0` | `IF-044` | `user_task_lighting_log` |
| **FR-105** | PRD 5.7 隔离规则 | `WorkHourServiceImpl` | `IF-051` | `work_hour_record` |
| **BR-106** | PRD 5.7 点亮上限 | `TaskServiceImpl` | `IF-043` | `user_task_lighting_log` |
| **BR-107** | PRD 5.7 范围限定 | `TaskServiceImpl` | `IF-043` | `task_sub` |
| **BR-108** | PRD 5.7 报工提示 | `yusp-plus-oca-web2.0` | N/A (前端界面) | N/A |
| **BR-109** | PRD 5.7 隔离规则 | `WorkHourServiceImpl` | `IF-051` | `work_hour_record` |
