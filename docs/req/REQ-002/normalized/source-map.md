# REQ-002 需求管理 - 需求详情与列表筛选 - 溯源映射表 (source-map.md)

> **任务包编号**：REQ-002  
> **任务包名称**：需求管理 - 需求详情与列表筛选  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-021** | PRD 4.3 需求详情 | `yusp-plus-workboard` | `IF-007` | `requirement`, `requirement_detail`, `requirement_operation_log` |
| **FR-022** | PRD 4.4.1 默认范围 | `RequirementController` | `IF-008` | `requirement` |
| **FR-023** | PRD 4.4.1 默认字段 | `yusp-plus-oca-web2.0` | `IF-008` | `requirement`, `stage_aging_record` |
| **FR-024** | PRD 4.4.1 计时规则 | `yusp-plus-job-core` (J05) | `IF-066` | `stage_aging_record` |
| **FR-025** | PRD 4.4.2 筛选 | `RequirementServiceImpl` | `IF-009` | `requirement` |
| **FR-026** | PRD 4.4.2 排序 | `RequirementServiceImpl` | `IF-009` | `requirement` |
| **FR-027** | PRD 4.4.2 展示 | `SysController` | `IF-010`, `IF-087` | `user_column_config` |
| **FR-028** | PRD 4.4.2 批量/导出 | `RequirementController` | `IF-011`, `IF-012` | `requirement`, `custom_field_value` |
| **BR-026** | PRD 4.3 需求详情 | `RequirementServiceImpl` | `IF-007` | `requirement` |
| **BR-027** | PRD 4.4.1 计时规则 | `yusp-plus-job-core` | `IF-066` | `stage_aging_record` |
| **BR-028** | PRD 4.4.2 筛选 | `RequirementServiceImpl` | `IF-009` | `requirement` |
| **BR-029** | PRD 4.4.2 导出 | `RequirementServiceImpl` | `IF-012` | `requirement` |
| **BR-030** | PRD 4.4.2 展示 | `SysServiceImpl` | `IF-087` | `user_column_config` |
