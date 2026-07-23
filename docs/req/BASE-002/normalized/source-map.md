# BASE-002 时效与基线 - 基线、调出、加塞与原计划 - 溯源映射表 (source-map.md)

> **任务包编号**：BASE-002  
> **任务包名称**：时效与基线 - 基线、调出、加塞与原计划  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-151** | PRD 7.3 基线生成 | `yusp-plus-job-core` (J04, J07) | `IF-072`, `IF-073` | `baseline_record`, `baseline_change_log` |
| **FR-152** | PRD 7.4 原计划 | `BaselineServiceImpl` | `IF-074` | `requirement_plan` |
| **FR-153** | PRD 7.5 首次上线 | `RequirementServiceImpl` | `IF-075` | `requirement` |
| **FR-154** | PRD 7.6 计划偏差 | `BaselineServiceImpl` | `IF-076`, `IF-077` | `baseline_change_log` |
| **BR-161** | PRD 7.3 基线判定 | `BaselineServiceImpl` | `IF-072` | `baseline_record` |
| **BR-162** | PRD 7.4 原计划 | `BaselineServiceImpl` | `IF-074` | `requirement_plan` |
| **BR-163** | PRD 7.5 首次上线 | `RequirementServiceImpl` | `IF-075` | `requirement` |
| **BR-164** | PRD 7.6 偏差变更 | `BaselineServiceImpl` | `IF-076` | `baseline_change_log` |
