# WH-003 报工管理 - 报工查看与知脉同步/系统切换 - 溯源映射表 (source-map.md)

> **任务包编号**：WH-003  
> **任务包名称**：报工管理 - 报工查看与知脉同步/系统切换  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-131** | PRD 6.5 报工查看 | `WorkHourController` | `IF-059`, `IF-060` | `work_hour_record`, `oca_user` |
| **FR-132** | PRD 6.6 报工同步知脉 | `yusp-plus-job-core` (J03) | `IF-061`, `IF-104` | `work_hour_record`, `sync_work_hour_log` |
| **FR-133** | PRD 6.7 系统切换 | `WorkHourServiceImpl` | `IF-062` | `sync_work_hour_log` |
| **FR-134** | PRD 6.7 系统切换 | `WorkHourServiceImpl` | `IF-063` | `sync_work_hour_log` |
| **BR-141** | PRD 6.5 报工查看 | `WorkHourController` | `IF-059` | `oca_user` |
| **BR-142** | PRD 6.6 同步规则 | `yusp-plus-job-core` | `IF-104` | `work_hour_record` |
| **BR-143** | PRD 6.7 系统切换 | `WorkHourServiceImpl` | `IF-063` | `sync_work_hour_log` |
| **BR-144** | PRD 6.7 系统切换 | `WorkHourServiceImpl` | `IF-063` | `sync_work_hour_log` |
