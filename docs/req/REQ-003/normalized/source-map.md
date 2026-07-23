# REQ-003 需求管理 - 固定横幅与指标统计 - 溯源映射表 (source-map.md)

> **任务包编号**：REQ-003  
> **任务包名称**：需求管理 - 固定横幅与指标统计  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-036** | PRD 4.6.1 展示范围 | `yusp-plus-oca-web2.0` | N/A (前端布局) | N/A |
| **FR-037** | PRD 4.6.1 展示指标 | `RequirementController` | `IF-013` | `requirement`, `requirement_risk` |
| **FR-038** | PRD 4.6.2 联动 | `yusp-plus-oca-web2.0` | `IF-014` | `requirement` |
| **FR-039** | PRD 4.6.2 联动 | `yusp-plus-oca-web2.0` | N/A (前端UI高亮) | N/A |
| **FR-040** | PRD 4.6.2 权限与联动 | `RequirementController` | `IF-015` | `requirement`, `oca_org` |
| **BR-041** | PRD 4.6.1 展示指标 | `RequirementServiceImpl` | `IF-013` | `requirement` |
| **BR-042** | PRD 4.6.1 展示指标 | `RequirementServiceImpl` | `IF-013` | `requirement_risk`, `stage_aging_record` |
| **BR-043** | PRD 4.6.1 展示指标 | `RequirementServiceImpl` | `IF-013` | `requirement` |
| **BR-044** | PRD 4.6.2 权限 | `RequirementServiceImpl` | `IF-013`, `IF-015` | `requirement`, `oca_user` |
