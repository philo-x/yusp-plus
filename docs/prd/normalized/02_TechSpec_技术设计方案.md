# 知效平台技术设计方案 (Tech Spec)

> **文档编号**：ZX-TEC-2026  
> **文档版本**：V2.0-FINAL（系统化重构版）  
> **文档状态**：团队协作标准基线（正式发布）  
> **更新日期**：2026-07-23  
> **面向视角**：系统架构师 / 后端研发 / 前端研发 / DBA / 运维工程师

---

## 1. 架构图与系统骨架对齐 (Architecture & Skeleton)

### 1.1 Maven Reactor 多模块架构映射

本系统基于 repository 中实际的 `yusp-plus` Reactor 多模块工程构建：

```
yusp-plus (Maven Reactor 父工程 - Java 17 / Spring Boot 3.4.5)
 ├── yusp-plus-single
 │    └── yusp-plus-single-starter      # [运行主入口] YuspPlusSingleStarterMicroserviceApp (单体部署模式)
 ├── yusp-plus-workboard                 # [知效业务核心] 敏捷协作、需求管理、任务分配、报工与看板控制层
 │    ├── controller                     # OcaWorkboardController, EsbController, FtpController
 │    ├── service/impl                   # OcaWorkboardServiceImpl
 │    └── dao / domain                   # OcaWorkboardDao, OcaWorkboardEntity, Msgm00001Req/Resp
 ├── yusp-plus-oca                       # [平台骨架核心] 基础组织用户与通用组件
 │    └── yusp-plus-oca-core             # AdminSmUserDao, AdminSmOrgDao, AdminSmDptDao, AdminSmDutyDao,
 │                                       # AdminSmRoleDao, AdminSmLookupDictDao, AdminSmLogDao
 │                                       # 统一响应封装: ResultWarpReturnValueHandler, RestApiResponseAdvice
 ├── yusp-plus-uaa                       # [统一认证服务] OAuth2 认证与 Token 存储
 │    └── yusp-plus-uaa-core             # OcaUserDetailsServiceImpl, CustomRedisTokenStore, CheckTokenController
 ├── yusp-plus-extend                    # [扩展能力模块] 调度与审计链路
 │    ├── yusp-plus-job-core             # UADP 批处理调度: ScheduleJobDao, ScheduleJobLogDao, ScheduleConfig
 │    └── yusp-plus-utrace-core          # 变更链路追踪: ModifyTraceDao, ModifyTraceController
 ├── yusp-plus-common                    # [公共基础库] 通用 Redis 工具、分布式锁、基类与异常处理
 ├── yusp-plus-dbinit                    # [数据库初始化] GoldenDB DDL (oca-init-20250915.sql, zhixiao-init-20260723.sql)
 └── yusp-plus-oca-web2.0                # [前端平台] Vue 2.6 / Element UI 前端工程
```

### 1.2 系统架构与组件图

```mermaid
graph TD
    subgraph 接入层 [Client & Gateway]
        WEB[Vue2 前端 yusp-plus-oca-web2.0]
        GATEWAY[Nginx / 反向代理网关]
    end

    subgraph 知效平台 Reactor 服务核 [yusp-plus Reactor]
        STARTER[yusp-plus-single-starter 主程序]
        WORKBOARD[yusp-plus-workboard 敏捷工作台/需求/任务/报工]
        OCA_CORE[yusp-plus-oca-core 组织用户/角色/字典/统一响应]
        UAA_CORE[yusp-plus-uaa-core OAuth2 统一Token认证]
        JOB_CORE[yusp-plus-job-core J01-J13 定时调度中心]
        COMMON[yusp-plus-common 工具/分布式锁/基类]
    end

    subgraph 数据与缓存层 [Persistence & Cache]
        REDIS[(Redis 6.x 缓存/分布式锁)]
        GOLDEN_DB[(GoldenDB 6.x 73张表)]
    end

    subgraph 外部系统集成 [External Services]
        SSO[知悉统一身份认证]
        ZM[知脉 - 需求/任务/工作量封板]
        ZC[知测 - SIT准入/UAT准出事件]
        ZA[知安 - 安全任务与确认]
        DW[数据仓库 - 日终清洗与归档]
    end

    WEB --> GATEWAY
    GATEWAY --> STARTER
    STARTER --> WORKBOARD
    STARTER --> OCA_CORE
    STARTER --> UAA_CORE
    WORKBOARD --> COMMON
    WORKBOARD --> REDIS
    WORKBOARD --> GOLDEN_DB
    JOB_CORE --> GOLDEN_DB

    STARTER <--> SSO
    WORKBOARD <--> ZM
    WORKBOARD <--> ZC
    WORKBOARD <--> ZA
    JOB_CORE --> DW
```

---

## 2. 核心时序图 (Sequence Diagrams)

### 2.1 知测 SIT/UAT 及知脉上线自动拖卡时序图

```mermaid
sequenceDiagram
    autonumber
    participant Ext as 知测/知脉外部系统
    participant API as SyncIntegrationController (IF-100)
    participant Lock as Redis Redisson Lock
    participant Service as RequirementServiceImpl
    participant DB as GoldenDB (requirement_stage)

    Ext->>API: POST /api/sync/requirement (事件: SIT准入/UAT准出/上线)
    API->>Lock: 尝试获取分布式锁 (zx:lock:sync:{eventId})
    alt 获取锁成功
        API->>Service: handleSyncEvent(eventBo)
        Service->>DB: 校验 sync_event_log 幂等键
        alt 未处理且当前阶段有效
            Service->>DB: 开启事务: 关闭当前阶段(exit_time) -> 插入新阶段(enter_time)
            Service->>DB: 更新 requirement.current_stage 指针
            Service->>DB: 记录 requirement_operation_log
            Service-->>API: 返回成功
        else 重复事件
            Service-->>API: 幂等跳过
        end
        Lock-->>API: 释放锁
        API-->>Ext: 返回 200 SUCCESS
    else 锁占用
        API-->>Ext: 返回 200 (提示: 重复请求处理中)
    end
```

### 2.2 外包人员每日点亮与并发报工 Redisson 锁时序图

```mermaid
sequenceDiagram
    autonumber
    actor User as 外包员工
    participant API as WorkHourController (IF-052)
    participant Redis as Redis (zx:worklog:lock)
    participant Service as WorkHourServiceImpl
    participant DB as GoldenDB (work_hour_record)

    User->>API: 提交报工 (user_id, work_date, task_id, minutes)
    API->>Redis: 获取分布式锁 (zx:worklog:lock:{user_id}:{work_date})
    Redis-->>API: 锁获取成功
    API->>Service: submitWorkHour(bo)
    Service->>DB: 校验 user_work_day (打卡/请假考勤工时)
    alt 填报工时 > 考勤上限 或 日期 > 14天窗口
        Service-->>API: 抛出 BusinessException (错误码 40015)
        API-->>User: 返回“超出考勤工时上限或14天报工窗口已关闭”
    else 校验通过
        Service->>DB: 校验知脉封板状态 (workload_confirm_status)
        alt 已封板锁定
            Service-->>API: 抛出 BusinessException (错误码 40018)
            API-->>User: 返回“知脉工作量已封板，禁止修改”
        else 未封板
            Service->>DB: 保存 work_hour_record 并记录审计日志
            Service-->>API: 成功
        end
    end
    API->>Redis: 释放分布式锁
    API-->>User: 返回提交成功
```

### 2.3 J04 每月初基线快照与加塞分析时序图

```mermaid
sequenceDiagram
    autonumber
    participant Job as UADP Job (J04/J07)
    participant Service as BaselineServiceImpl
    participant DB as GoldenDB (baseline_record)
    participant DW as 数据仓库

    Job->>Service: 执行月度基线计算 (每月1日 01:00)
    Service->>DB: 扫描全行在建需求与排期
    Service->>DB: 批量生成基线快照记录 (baseline_record)
    Job->>Service: 执行加塞与偏离度分析 (J07)
    Service->>DB: 比对当前需求集合与基线快照
    Service->>DB: 写入 baseline_change_log (记录加塞/调出/取消)
    Service->>DW: 导出基线分析报表至数据仓库
```

---

## 3. 数据库与缓存设计 (Data Schema & Cache)

### 3.1 Redis 键规范与防击穿/穿透方案

| 键命名格式 (Redis Key Pattern) | 数据结构 | TTL 过期策略 | 防击穿/雪崩/穿透设计 |
|---|---|---|---|
| `zx:req:detail:{tenant_id}:{req_id}` | String (JSON) | 2小时 + Jitter(0-600s) | 防雪崩：随机偏移 TTL；防穿透：空对象缓存 (`{}`) 5 分钟 |
| `zx:worklog:lock:{user_id}:{date}` | Redisson Lock | 10 秒自动释放 | 防并发争用：分布式锁保障报工写操作单线程串行化 |
| `zx:user:perm:{user_id}` | Hash (角色/权限) | 30 分钟 (登出主动清除)| 高频读缓存：缓存用户组织角色矩阵，降低 DB 压力 |
| `zx:bloom:req_code` | Bloom Filter | 永久 (增量追加) | 防穿透：拦截非法需求编号查询 |

---

## 4. 异常处理与兜底策略 (Resilience)

### 4.1 外部集成降级与重试
* **异常队列**：外部系统 (知脉/知测/知安) 接口超时或 5xx 异常时，异步落库至 `sync_conflict`。
* **作业补偿**：由 `yusp-plus-job-core` 中的 J01、J10、J11 作业每 15 分钟扫表重试 3 次退避。

### 4.2 幂等性控制
* **MD5 幂等键**：基于请求 Header `reqJnls` + `transName` + `data_tenant_id` 生成 MD5 防重 Key 存入 Redis，有效期 30 秒。

---

## 5. 非功能性需求 (NFR)


