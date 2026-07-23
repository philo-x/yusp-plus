# BASE-002 时效与基线 - 基线、调出、加塞与原计划 - 业务规则说明书 (business-rules.md)

> **任务包编号**：BASE-002  
> **任务包名称**：时效与基线 - 基线、调出、加塞与原计划  

---

## 业务规则列表 (Business Rules)

- **`BR-161`**：**基线统计包容原则**：在计算月度基线完成率时，公式为 `完成率 = 已完成基线需求数 / (初始基线数 + 调出数 + 加塞数)`。调出与加塞需求均明确保留在基线统计分母中。
- **`BR-162`**：**原计划防篡改不可逆规则**：`original_plan_date` 字段只允许写入一次，后续即便知脉重新变更排期，原计划只读锁定，仅允许更新 `current_plan_date`。
- **`BR-163`**：**首次上线日期锁定**：`first_online_date` 在需求第一次达到上线状态时固化，二次上线或补丁上线绝不覆盖。
- **`BR-164`**：**计划偏差与变更公式**：
  - 判定计划偏差：`current_date > stage_plan_date AND stage_status != COMPLETED` 或 `actual_finish_date > plan_finish_date`。
  - 判定计划变更：`current_plan_date != original_plan_date`。
