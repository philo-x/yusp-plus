# BASE-001 时效与基线 - 阶段时效与扣减/回退 - 溯源映射表 (source-map.md)

> **任务包编号**：BASE-001  
> **任务包名称**：时效与基线 - 阶段时效与扣减/回退  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-141** | PRD 7.1 阶段时效 | `RequirementServiceImpl` | `IF-066` | `stage_aging_record` |
| **FR-142** | PRD 7.1 阶段时效 | `RequirementServiceImpl` | `IF-067` | `requirement_stage` |
| **FR-143** | PRD 7.2 时效扣减 | `BaselineController` | `IF-068`, `IF-069` | `stage_deduction_request` |
| **FR-144** | PRD 7.2 时效扣减 | `yusp-plus-oca-web2.0` | `IF-066` | `stage_aging_record` |
| **FR-145** | PRD 7.2 回退累计 | `RequirementServiceImpl` | `IF-070` | `stage_aging_record` |
| **BR-151** | PRD 7.2 时效扣减 | `BaselineServiceImpl` | `IF-068` | `stage_aging_record` |
| **BR-152** | PRD 7.2 回退累计 | `RequirementServiceImpl` | `IF-070` | `stage_aging_record` |
| **BR-153** | PRD 7.2 时效扣减 | `BaselineServiceImpl` | `IF-068` | `stage_deduction_request` |
| **BR-154** | PRD 7.1 阶段时效 | `RequirementServiceImpl` | `IF-066` | `stage_aging_record` |
