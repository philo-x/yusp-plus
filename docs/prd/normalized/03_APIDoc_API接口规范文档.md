# 知效平台 API 接口规范文档

> **文档编号**：ZX-API-2026  
> **文档版本**：V2.0-FINAL（系统化重构版）  
> **文档状态**：团队协作标准基线（正式发布）  
> **更新日期**：2026-07-23  
> **面向视角**：后端研发 / 前端研发 / 接口测试 / 外部系统对接团队

---

## 1. 接口协议与通用报文规范

### 1.1 传输与协议说明
* **通信协议**：HTTPS / TLS 1.3
* **编码格式**：UTF-8
* **时间与日期格式**：ISO 8601 标准带时区（如 `2026-07-23T18:30:00+08:00`），日期字段统一使用 `YYYY-MM-DD`。
* **HTTP 请求方法**：全量业务接口遵循项目 Controller 统一规范，使用 **`POST /api/<controller>/<action>`** 动作语义。

### 1.2 统一请求报文格式 (`JClientReqEntity<T>`)
```json
{
  "head": {
    "reqJnls": "REQ-JNL-20260723-00019283",
    "pageId": "PAGE_REQUIREMENT_LIST",
    "transName": "requirementPageQuery",
    "timestamp": 1784812200000,
    "nonce": "a9b8c7d6e5"
  },
  "body": {
    "page": 1,
    "size": 10,
    "stageCode": "IN_DEVELOPMENT",
    "keyword": "核心支付"
  }
}
```

### 1.3 统一响应报文格式 (`JClientRspEntity<T>`)
受框架 `ResultWarpReturnValueHandler` 与 `RestApiResponseAdvice` 拦截自动封装：

```json
{
  "head": {
    "retCode": "00000",
    "retMsg": "操作成功",
    "reqJnls": "REQ-JNL-20260723-00019283",
    "timestamp": 1784812200250
  },
  "body": {
    "total": 1,
    "records": [
      {
        "reqId": "REQ_88392019",
        "reqCode": "REQ-2026-05",
        "title": "核心支付服务信创替代",
        "currentStage": "IN_DEVELOPMENT",
        "pmName": "张三",
        "stayDays": 3
      }
    ]
  }
}
```

---

## 2. 108 个逻辑接口全量目录 (IF-001 ~ IF-108)

| 分类编码 | 逻辑接口编号 | 对应业务 Controller | 数量 | 说明 |
|---|---|---|---:|---|
| **REQ** | `IF-001` ~ `IF-028` | `RequirementController` | 28 个 | 需求视图、泳道、甘特图、拖卡、自定义字段、风险管理 |
| **TASK** | `IF-029` ~ `IF-050` | `TaskController` | 22 个 | 任务创建、认领、点亮、行内批量编辑、知安安全确认 |
| **WH** | `IF-051` ~ `IF-065` | `WorkHourController` | 15 个 | 考勤同步、每日点亮、14天窗口报工、封板写回 |
| **BASE** | `IF-066` ~ `IF-081` | `BaselineController` | 16 个 | 阶段停留计算、扣减审批、月度基线、加塞与调出分析 |
| **AUTH** | `IF-082` ~ `IF-099` | `AuthController` / `SysController` | 18 个 | SSO 登录、数据权限作用域切换、自定义列保存、审计 |
| **SYNC** | `IF-100` ~ `IF-108` | `SyncIntegrationController` | 9 个 | 外部系统 (知脉/知测/知安/考勤/数据仓库) 同步端点 |

---

## 3. J01 ~ J13 批处理调度作业规约

| 作业 ID | 作业名称 | Cron 表达式 | 核心输入/输出表 | 业务处理逻辑与重试机制 |
|---|---|---|---|---|
| **J01** | 知脉需求定时增量同步 | `0 0/10 * * * ?` | `sync_requirement` -> `requirement` | 每 10 分钟拉取知脉增量需求；失败重试 3 次 |
| **J02** | 每日考勤数据定时同步 | `0 0 2 * * ?` | 外部考勤 -> `attendance_sync_record` | 每天凌晨 2 点拉取前一日打卡/请假记录 |
| **J03** | 工时写回知脉作业 | `0 0 4 * * ?` | `work_hour_record` -> 知脉 API | 汇总核查无误工时同步写回知脉系统 |
| **J04** | 月度基线快照自动生成 | `0 0 1 1 * ?` | `requirement` -> `baseline_record` | 每月初 1 点生成上月交付基线快照 |
| **J05** | 阶段停留时长定时计算 | `0 0 3 * * ?` | `requirement_stage` -> `stage_aging_record` | 每天凌晨 3 点扣除工作日计算最新停留天数 |
| **J06** | 扣减审批生效定时计算 | `0 0/30 * * * ?` | `stage_deduction_request` -> `stage_aging_record` | 每 30 分钟刷新已审批通过的停留扣减天数 |
| **J07** | 基线加塞与调出分析 | `0 0 5 * * ?` | `baseline_record` -> `baseline_change_log` | 自动计算基线偏离度与加塞率 |
| **J08** | 数据双向清洗与对账 | `0 0 4 * * ?` | `sync_incremental_change` | 清洗知微与知效双向一致性数据 |
| **J09** | 灰度路由策略定时刷新 | `0 0/5 * * * ?` | `gray_release_rule` -> Redis | 每 5 分钟向 Redis 刷新最新灰度路由配置 |
| **J10** | 知脉任务状态异步补偿 | `0 0/15 * * * ?` | `sync_task` -> `task` | 补偿知脉回调失败的任务同步事件 |
| **J11** | 知测 SIT/UAT 事件补偿 | `0 0/15 * * * ?` | `sync_event_log` | 对接知测遗漏的自动拖卡事件 |
| **J12** | 知安安全任务自动推送 | `0 0 1 * * ?` | `security_check_task` | 同步知安每日新建立的扫描任务 |
| **J13** | 满 90 天上线需求自动归档| `0 0 2 * * ?` | `requirement` | 扫描上线满 90 天的需求，更新为 `ARCHIVED` |

---

## 4. 错误代码字典 (Error Code Registry)

| 错误代码 (retCode) | HTTP 状态 | 错误含义描述 | 排错与解决建议 |
|---|---|---|---|
| `00000` | 200 | 操作成功 | 无 |
| `40001` | 200 | 统一认证失败 / Token 过期 | 提示用户重新进行 SSO 登录认证 |
| `40003` | 200 | 越权访问：无当前数据作用域权限 | 检查用户组织角色矩阵与上下文数据范围 |
| `40010` | 200 | 请求入参校验失败 | 检查 `body` 中必填字段与格式规范 |
| `40015` | 200 | 超出 14 天报工窗口或超考勤上限 | 检查填报日期是否超出 14 天或超打卡工时 |
| `40018` | 200 | 知脉工作量已封板锁定 | 该月份工作量已被知脉锁定，不可修改 |
| `40025` | 200 | 突破单日最多点亮 5 个任务限制 | 提示用户先取消或完成已有点亮任务 |
| `40030` | 200 | 阶段回退违规：非相邻前序阶段 | 校验回退目标阶段必须为紧邻的前一个阶段 |
| `50000` | 200 | 系统内部未知错误 | 查看后台 log4j2 日志，根据 traceId 定位堆栈 |
| `50002` | 200 | 外部服务 (知脉/知测) 调用超时 | 触发展示降级提示，触发 J01/J11 自动补偿 |
