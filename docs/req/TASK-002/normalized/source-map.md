# TASK-002 任务管理 - 任务分发、认领与流转 - 溯源映射表 (source-map.md)

> **任务包编号**：TASK-002  
> **任务包名称**：任务管理 - 任务分发、认领与流转  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-091** | PRD 5.6 任务分发 | `TaskController` | `IF-037` | `task`, `task_assignment_log` |
| **FR-092** | PRD 5.6, 5.8 未分配池 | `TaskController` | `IF-038` | `task` |
| **FR-093** | PRD 5.8 嵌入式表格 | `yusp-plus-oca-web2.0` | `IF-039` | `task` |
| **FR-094** | PRD 5.6, 5.8 流程触发 | `TaskServiceImpl` | `IF-040`, `IF-100` | `task`, `sync_event_log` |
| **FR-095** | PRD 5.8 任务列表 | `TaskController` | `IF-041` | `task` |
| **FR-096** | PRD 5.8 连击下钻 | `yusp-plus-oca-web2.0` | `IF-042` | `task`, `oca_org` |
| **BR-096** | PRD 5.6 任务分发 | `TaskServiceImpl` | `IF-037`, `IF-039` | `task_sub` |
| **BR-097** | PRD 5.6, 5.8 未分配池 | `TaskServiceImpl` | `IF-038` | `task` |
| **BR-098** | PRD 5.8 嵌入式表格 | `yusp-plus-oca-web2.0` | `IF-039` | `task` |
| **BR-099** | PRD 5.8 任务列表 | `TaskServiceImpl` | `IF-041` | `task` |
