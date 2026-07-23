# PERM-001 角色与数据权限管理 - 溯源映射表 (source-map.md)

> **任务包编号**：PERM-001  
> **任务包名称**：角色与数据权限管理  

---

## 溯源映射表 (Source Traceability Matrix)

| 需求/规则 ID | PRD 章节/条款 | TechSpec 模块/组件 | API 接口编号 | GoldenDB 数据库表 |
|---|---|---|---|---|
| **FR-001** | PRD 3.1 角色范围 | `yusp-plus-oca-core` | `IF-082` | `oca_role`, `oca_user_role` |
| **FR-002** | PRD 3.2 需求管理权限 | `yusp-plus-workboard` (OcaWorkboardController) | `IF-083`, `IF-084` | `oca_org`, `oca_user` |
| **FR-003** | PRD 3.2 需求管理权限 | `yusp-plus-workboard` | `IF-084` | `requirement` |
| **FR-004** | PRD 3.3 任务管理权限 | `TaskController` | `IF-085` | `task`, `oca_org` |
| **FR-005** | PRD 3.4 报工查看权限 | `WorkHourController` | `IF-086` | `work_hour_record`, `oca_user` |
| **FR-006** | PRD 3.5 自定义字段权限 | `CustomFieldController` | `IF-087`, `IF-088` | `custom_field_config` |
| **BR-001** | PRD 3.1 角色范围 | `yusp-plus-uaa-core` | `IF-082` | `oca_user` |
| **BR-002** | PRD 3.2 需求管理权限 | `RequirementServiceImpl` | `IF-001`, `IF-007` | `requirement` |
| **BR-003** | PRD 3.3 任务管理权限 | `TaskServiceImpl` | `IF-029`, `IF-037` | `task` |
| **BR-004** | PRD 3.4 报工查看权限 | `WorkHourServiceImpl` | `IF-059` | `work_hour_record` |
| **BR-005** | PRD 3.5 自定义字段权限 | `CustomFieldServiceImpl` | `IF-019` | `custom_field_config` |
