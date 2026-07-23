# TASK-001 任务管理 - 任务类型与业务对象 - 溯源映射表 (source-map.md)

> **任务包编号**：TASK-001  
> **任务包名称**：任务管理 - 任务类型与业务对象  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-076** | PRD 5.1, 5.2 需求类任务 | `yusp-plus-workboard` (TaskController) | `IF-029`, `IF-030` | `task` |
| **FR-077** | PRD 5.2 需求类任务 | `TaskController` | `IF-031` | `task_sub` |
| **FR-078** | PRD 5.2 需求类任务 | `TaskServiceImpl` | `IF-032` | `task`, `oca_user` |
| **FR-079** | PRD 5.3 项目类任务 | `TaskController` | `IF-033`, `IF-034` | `task`, `task_sub` |
| **FR-080** | PRD 5.4 事务类任务 | `TaskController` | `IF-035` | `task` |
| **FR-081** | PRD 5.5 安全内容 | `SyncIntegrationController` | `IF-036`, `IF-102` | `security_check_task`, `task` |
| **BR-081** | PRD 5.1 业务对象 | `TaskServiceImpl` | `IF-029` | `task` |
| **BR-082** | PRD 5.2 需求类任务 | `TaskServiceImpl` | `IF-031` | `task_sub` |
| **BR-083** | PRD 5.5 安全内容 | `TaskServiceImpl` | `IF-036` | `security_check_task` |
| **BR-084** | PRD 5.4 事务类任务 | `TaskServiceImpl` | `IF-035` | `task` |
| **BR-085** | PRD 5.2 需求类任务 | `TaskServiceImpl` | `IF-031` | `task` |
