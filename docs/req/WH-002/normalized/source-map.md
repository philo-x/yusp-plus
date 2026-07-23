# WH-002 报工管理 - 我的报工与填报限制 - 溯源映射表 (source-map.md)

> **任务包编号**：WH-002  
> **任务包名称**：报工管理 - 我的报工与填报限制  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-121** | PRD 6.4.1 展示模式 | `yusp-plus-workboard` (WorkHourController) | `IF-053` | `work_hour_record` |
| **FR-122** | PRD 6.4.1 列表字段 | `yusp-plus-oca-web2.0` | `IF-053` | `work_hour_record` |
| **FR-123** | PRD 6.4.1 确认状态 | `WorkHourServiceImpl` | `IF-054` | `work_hour_record` |
| **FR-124** | PRD 6.4.2 字段限制 | `WorkHourServiceImpl` | `IF-055` | `work_hour_record` |
| **FR-125** | PRD 6.4.2 窗口限制 | `WorkHourServiceImpl` | `IF-055`, `IF-056` | `work_hour_record` |
| **FR-126** | PRD 6.4.2 时长限制 | `WorkHourServiceImpl` | `IF-055` | `attendance_sync_record`, `work_hour_record` |
| **FR-127** | PRD 6.4.2 任务关联 | `WorkHourServiceImpl` | `IF-055` | `task`, `oca_user` |
| **FR-128** | PRD 6.4.2 工作量确认 | `WorkHourServiceImpl` | `IF-055`, `IF-056` | `work_hour_record` |
| **FR-129** | PRD 6.4.2 修改历史 | `WorkHourController` | `IF-057`, `IF-058` | `work_hour_log` |
| **BR-126** | PRD 6.4.2 窗口限制 | `WorkHourServiceImpl` | `IF-055` | `work_hour_record` |
| **BR-127** | PRD 6.4.2 时长限制 | `WorkHourServiceImpl` | `IF-055` | `work_hour_record` |
| **BR-128** | PRD 6.4.2 工作量确认 | `WorkHourServiceImpl` | `IF-055` | `work_hour_record` |
| **BR-129** | PRD 6.4.2 系统一致 | `WorkHourServiceImpl` | `IF-055` | `task`, `oca_user` |
| **BR-130** | PRD 6.4.2 只读派生 | `WorkHourServiceImpl` | `IF-055` | `work_hour_record` |
