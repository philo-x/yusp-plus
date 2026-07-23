# INTEG-001 外部系统集成与数据迁移 - 溯源映射表 (source-map.md)

> **任务包编号**：INTEG-001  
> **任务包名称**：外部系统集成与数据迁移  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-166** | PRD 8.1 外部系统 | `SyncIntegrationController` | `IF-100` ~ `IF-108` | `sync_event_log`, `sync_conflict` |
| **FR-167** | PRD 8.2 数据迁移 | `yusp-plus-dbinit` (SQL scripts) | N/A | `historical_migration_record` |
| **FR-168** | PRD 8.2 数据迁移 | `yusp-plus-dbinit` | N/A | `historical_migration_record` |
| **FR-169** | PRD 8.3 原型设计 | `yusp-plus-oca-web2.0` | N/A (Vue UI) | N/A |
| **BR-176** | PRD 8.1 职责 | `SyncIntegrationController` | `IF-100` ~ `IF-108` | `requirement`, `task` |
| **BR-177** | PRD 8.2 数据迁移 | `yusp-plus-dbinit` | N/A | `historical_migration_record` |
| **BR-178** | PRD 8.1 防重 | `Redisson Lock` | `IF-100` ~ `IF-108` | `sync_event_log` |
