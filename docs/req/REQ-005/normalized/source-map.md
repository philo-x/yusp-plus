# REQ-005 需求管理 - 风险、时效与归档 - 溯源映射表 (source-map.md)

> **任务包编号**：REQ-005  
> **任务包名称**：需求管理 - 风险、时效与归档  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-066** | PRD 4.9 风险 | `yusp-plus-workboard` (RiskController) | `IF-023`, `IF-024` | `requirement_risk`, `requirement` |
| **FR-067** | PRD 4.9 时效 | `RequirementServiceImpl` | `IF-025` | `stage_aging_record` |
| **FR-068** | PRD 4.9 归档 | `yusp-plus-job-core` (J13) | `IF-026` | `requirement` |
| **FR-069** | PRD 4.9 归档 | `RequirementController` | `IF-027` | `requirement` |
| **FR-070** | PRD 4.9 风险、时效 | `RequirementServiceImpl` | `IF-028` | `stage_aging_record` |
| **BR-071** | PRD 4.9 风险 | `RiskServiceImpl` | `IF-023` | `requirement_risk` |
| **BR-072** | PRD 4.9 时效 | `RequirementServiceImpl` | `IF-025` | `stage_aging_record` |
| **BR-073** | PRD 4.9 归档 | `RequirementServiceImpl` | `IF-027` | `requirement` |
| **BR-074** | PRD 4.9 风险、时效 | `RequirementServiceImpl` | `IF-028` | `stage_aging_record` |
