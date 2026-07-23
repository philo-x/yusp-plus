# REQ-001 需求管理 - 视图、泳道与甘特图 - 溯源映射表 (source-map.md)

> **任务包编号**：REQ-001  
> **任务包名称**：需求管理 - 视图、泳道与甘特图  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-011** | PRD 4.2 需求价值流 | `yusp-plus-workboard` (RequirementController) | `IF-001` | `requirement`, `requirement_stage` |
| **FR-012** | PRD 4.1 功能概述 | `yusp-plus-oca-web2.0` (Vue Component) | `IF-001`, `IF-002` | `requirement` |
| **FR-013** | PRD 4.5 甘特图 | `RequirementController` | `IF-003` | `requirement`, `requirement_plan` |
| **FR-014** | PRD 4.5 甘特图 | `yusp-plus-oca-web2.0` | N/A (前端组件) | N/A |
| **FR-015** | PRD 4.5 甘特图 | `yusp-plus-oca-web2.0` | `IF-004` | `requirement_detail` |
| **FR-016** | PRD 4.5 甘特图 | `RequirementServiceImpl` | `IF-005` | `task`, `requirement` |
| **BR-016** | PRD 4.2 需求价值流 | `RequirementServiceImpl` | `IF-001` | `requirement_stage` |
| **BR-017** | PRD 4.5 甘特图 | `RequirementServiceImpl` | `IF-003` | `requirement_plan` |
| **BR-018** | PRD 4.5 甘特图 | `SyncIntegrationController` | `IF-100` | `requirement_stage` |
| **BR-019** | PRD 4.5 甘特图 | `RequirementServiceImpl` | `IF-005` | `task` |
