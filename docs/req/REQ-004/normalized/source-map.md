# REQ-004 需求管理 - 自动拖卡机制 - 溯源映射表 (source-map.md)

> **任务包编号**：REQ-004  
> **任务包名称**：需求管理 - 自动拖卡机制  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-046** | PRD 4.8 自动拖卡 | `SyncIntegrationController` | `IF-100` | `sync_event_log`, `requirement` |
| **FR-047** | PRD 4.8.1 一般需求 | `RequirementServiceImpl` | `IF-100`, `IF-101` | `requirement`, `requirement_stage` |
| **FR-048** | PRD 4.8.2 人力需求 | `RequirementServiceImpl` | `IF-100` | `requirement` |
| **FR-049** | PRD 4.8.1 防重 | `Redisson Lock` | `IF-100` | `sync_event_log` |
| **BR-051** | PRD 4.8 自动拖卡 | `yusp-plus-oca-web2.0` | N/A (前端界面) | N/A |
| **BR-052** | PRD 4.8.1 一般需求 | `RequirementServiceImpl` | `IF-100` | `requirement_stage` |
| **BR-053** | PRD 4.8.1 一般需求 | `RequirementServiceImpl` | `IF-100` | `requirement` |
| **BR-054** | PRD 4.8.2 分类型规则 | `RequirementServiceImpl` | `IF-100` | `requirement` |
| **BR-055** | PRD 4.8 自动拖卡 | `RequirementServiceImpl` | `IF-007`, `IF-100` | `requirement_stage`, `requirement_operation_log` |
