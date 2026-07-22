## V4.0.3

### 升级指南

- 修改 AuthorizationServerConfig，将 rsaKey 的 keyId 从 `UUID.randomUUID().toString()` 改成 `UUID.nameUUIDFromBytes(publicKey.getEncoded()).toString()`

### 问题修复

- 修复应用重启后，登录过的用户 token 失效的问题


## V4.0.2

### 升级指南

- 修改父 pom xml 中的 yusp-commons-dependencies 版本号为 V4.0.1，此版本解耦了 tongweb，用户可根据需要自行选择引入 tongweb 还是 bes
```xml
<dependencies>
    <!-- 东方通 -->
    <dependency>
        <groupId>com.tongweb.springboot</groupId>
        <artifactId>tongweb-spring-boot-starter-3.x</artifactId>
    </dependency>

    <!-- 宝蓝德 -->
    <dependency>
        <groupId>com.bes.appserver</groupId>
        <artifactId>bes-lite-spring-boot-starter</artifactId>
    </dependency>    
</dependencies>
```
- 升级 yusp-plus-oca-web2.0 中引用的 uadp-utils 依赖包版本从 0.3.6 至 0.3.7，以修复前端产物安全审计问题

### 问题修复

- 移除 yusp.web.ignoreAccessUrls 配置，该配置无实际效果，可以安全移除
- 修复 excel 导入功能报错的 bug

## V4.0.1

- 修复密码强制更改策略中，计算相差天数错误的bug
- 新增 ESB、FTP、Http连接池公共组件调用示例，详情参考 yusp-plus-common 模块中的 controller 部分
- 修复功能授权使用 AuthTreeCacheServiceImpl 时，缓存key不正确的bug
- 修复修改控制点时，checkcode 调用报错的bug
- 默认禁用掉国际化功能
- 新增缓存依赖治理，引入缓存依赖治理公共组件，完成功能授权、报文加密和防重判断、错误码、控制点等业务的缓存依赖治理

## V4.0.0

- 完成 JDK17 + SpringBoot3.4.5 版本升级，具体内容见 jdk17_update_report/JDK17_UPDATE_REPORT.md

## V3.3.2.20250224-RELEASE
1. 移除 FileCodeController 中的 java.security.SecureRandom，具体原因见http://wiki.mis.bcs/confluence/pages/viewpage.action?pageId=160006468

## 3.3.2.20250122-RELEASE
1. 默认开启功能权限过滤器，用以配合前端解决垂直授权问题 `yusp.filter.access.enabled=true`
2. 开放 `/actuator/health` 接口，用于pass上健康检查
3. 优化 README.md 和 CHANGELOG.md，删除 oca-plus 中对于 oracle、mysql、db2 的支持，适应信创改造。

## 3.3.2.20250111-RELEASE
1. 升级 yusp-commons-dependencies 版本至 3.3.2.20250111-RELEASE
2. 修复 yusp-plus-generator 模块因 springboot 版本升级导致 `spring.resources.static-locations` 配置失效的 bug

## 3.3.2.20241121-RELEASE
### 👉后端
#### yusp-plus

1. 将mysql数据库替换成goldenDB 5.1.46.69
2. 将内嵌的tomcat替换成TongWeb 7.0.E.6_P5
3. 升级springboot到2.7.18，并升级一些存在安全漏洞的开源组件
4. 统一版本号

#### yusp-plus-dbinit

1. 更新oca项目初始化sql脚本：yusp-plus-dbinit/src/main/resources/sql/goldendb/oca-init-20241119.sql

#### yusp-plus-generator

1. yusp-plus-generator模块适配goldenDB

--- 

<span id="nav-8"></span>
## 前版本

## 🙇‍样板工程版本：V1.2.1.20220416.RELEASE

since 2022.4.15</br>

### 👉后端

#### yusp-plus

1. 平台引入组件版本由3.2.2升级到3.3.1；
2. 删除工程的废弃模块xxl-job,yu-bank-web,yusp-plus-monitor；删除样板工程的yusp-plus-file，此模块删除后，文件的上传下载直接使用公司微服务组件yusp-file；
3. 新增yusp-plus-utrace-core模块，把yusp-plus-oca所有关于小U留痕迁移到到这个模块，需要使用时oca直接引入jar包即可；
4. 把yusp-plus-oca中关于工作流的部分全部迁移到yusp-plus-flow中，要使用工作流直接引入这个jar包即可；
5. 把切换中英文的功能从系统内部转移到登录首页，在登录时选择语言环境；

#### yusp-plus-uaa(详细参考yusp-plus\yusp-plus-uaa\README.md)

#####  🐞 问题修复
1. 渠道互斥登录问题修复
2. 修复登录时国际化语言功能失效(国际化语言是通过LocaleContextHolder.getLocale()获取前端页面请求头中Accept-Language的值进行判断是什么语言信息，但各个服务之间的通过Feign相互调用时， LocaleContextHolder.getLocale()获取的时当前操作系统的语言信息，导致国际化失效；添加Feign拦截器实现的国际化头的传递）;

##### 🚀 功能优化
1. 去掉uaa中冗余代码；
2. 删除单体中UAA相关内容，单体和微服务统一引用yusp-plus-uaa-core(统一单体和微服务的oca认证方式)；
3. 客户端及系统id统一由test调整为YUSP；

##### 🌱 新增功能
1. 新增第三方验证扩展实现及案例，如果需要用自己的用户服务校验可以自行开发；
2. 新增无感刷新token（token要到期时前端直接刷新token接口），不会出现操作中token过期的问题;


#### yusp-plus-oca(详细参考yusp-plus\yusp-plus-oca\README.md)
#####  🐞 问题修复
1. 修复用户和岗位，机构增删改时没有修改对应缓存数据；
2. 修复数据字典通过id查找name失效问题；
3. 修复当租户关闭时导致插入所有数据的字段data_tenant_id为null的bug（修改数据库表的结构，data_tenant_id默认值是1）；
4. 修复拖拽菜单树修改菜单位置不起效的问题；
5. 修复英文环境查看多租户，策略模块报404错误（新增多租户，策略模块英文翻译，以及admin_sm_menu_en_us数据库表）；
6. 修复英文环境下提示信息是中文问题；
7. 修复数据字典配置不生效问题（数据字典翻译首先会去找前端的静态字典，如果静态字典中没有对应的字典码，再从页面配置的动态字典中查找）;
8. 修复切换语言环境后redis中某些缓存如数据字典，机构等没有更换成对应语言的缓存；
9. 修复功能/数据授权中显示停用状态的用户和角色；
10. 修复修改租户数据没有验证用户账号和工号是否存在bug;
11. 修复序列数据修改后不起效的bug;
12. 修复菜单详情中删除整个层级目录无法删除子节点问题；
13. 修复swagger任何密码都可登录和单体swagger无法登录问题；
14. 修复admin删除租户对应的超级用户的权限时，租户下其它的用户权限并未删除问题；
15. 修复文件，图片无法上传下载问题；

##### 🚀 功能优化
1. 优化登录用户获取有权限的机构Id，优化前先是获取所有的机构，然后通过指定的机构Id或者登录人的机构Id递归获取有权限的机构Id合集；优化后直接通过机构序列右匹配指定的机构Id或者登录人的机构Id获取有权限的机构id；
2. 优化oca中全表查询的代码并且给某些数据量大的表添加索引；
3. 统一机构，部门，岗位等停用和删除条件；
4. 之前初始化脚本时，把一些功能授权给超级管理员角色，一些授权给admin用户，还有一些二者都有；现在初始化脚本时把所有的功能权限授权给admin用户，并且新增菜单和控制点时直接把权限授权给admin;在功能授权，用户管理模块的用户列表中不显示admin，避免对admin的数据及权限进行修改，导致系统显示功能不全；
5. 租户分配权限时也是全部分配给租户对应的超级管理员用户而不是对应的角色；
6. 优化删除角色和用户时，其对应的授权关系没有删除问题；
7. pom.xml总剔除无用依赖，第三方包统一由yusp-parent管理；
8. 优化代码，剔除异常打印，无用导入，控制台输出等；


##### 🌱 新增功能
1. 用户切换角色功能；
2. 敏感数据加密，对用户手机号，身份证等敏感信息加密；
3. 登录人数进行限制策略；
4. 功能授权时通过修改数据库表admin_sm_exclude_menu实现对某些菜单的屏蔽；

## 🙇‍基础组件版本：V3.3.1.20220415.RELEASE

since 2022.4.15</br>

#### **yusp-common**

| 组件          | 功能描述                                                     | jar包名称                                                    |
| ------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 基础组件      | 中英文转换、模糊查询等基础功能                               | yusp-commons-base                                            |
| 服务上下文    | 使用上下文做链路追踪，对请求进出进行增强传递信息，feign调用回写、服务上下文等 | yusp-commons-context                                         |
| 日志规范      | JVM、DBpool、env、trace、api等日志格式和文件处理             | yusp-commons-starter--logs                                   |
| 数据校验      | 提供字段的校验，并提供自定义校验扩展机制                     | yusp-commons-starter-validation                              |
| 异常处理      | 统一异常处理将异常信息数据结构化，便于前端提示。             | yusp-commons-starter-exception                               |
| 操作日志      | 用于将用户的操作日志信息，记录下来留痕                       | yusp-commons-starter-op-log                                  |
| mybatisplus   | 实现增强日志，批量操作，多主键联合支持                       | yusp-commons-starter-mybatis-plus                            |
| 数据路由      | 实现支持通过sqlid路由数据库操作                              | yusp-commons-starter-data-route                              |
| 分库分表      | 实现分表分库，读写分离的数据库操作                           | yusp-commons-starter-shardingjdbc                            |
| 数据同步      | 实现不同服务之间的数据库数据同步                             | yusp-commons-starter-data-sync-client yusp-commons-starter-data-sync-server |
| 全局序列号    | 实现生成全局唯一id                                           | yusp-commons-starter-sequence                                |
| 数据清理      | 实现冷数据清理与备份                                         | yusp-commons-starter-data-clean                              |
| 分布式文件    | 提供文件的上传，下载，复制，归档等功能                       | yusp-commons-starter-file-localdisk yusp-commons-starter-file-fastdfs yusp-commons-starter-file-sftp |
| excel导入导出 | excel同步，异步导入导出                                      | yusp-commons-starter-excelcsv                                |
| pdf工具类     | 提供pdf/excel转换功能等                                      | yusp-commons-excel-pdf                                       |
| pdf模板导出   | pdf导出功能                                                  | yusp-commons-starter-pdf                                     |
| 进度查询      | 提供进度查询功能                                             | yusp-commons-progress                                        |
| 消息推送      | 消息推送服务提供统一消息推送和订阅发布功能，实现后台业务服务将消息推送到客户端。 | yusp-commons-starter-notification                            |
| 分布式锁      | 基于zk、db进行分布式锁的实现                                 | yusp-commons-starter-distributed-lock                        |
| 分布式缓存    | 对SpringCache 的注解进行增强，实现数据的一致性，可自定义过期时间等。 | yusp-commons-starter-redis                                   |
| 幂等性组件    | 支持方法级别的幂等性处理                                     | yusp-commons-starter-idempotent                              |
| 会话组件      | 传递登录用户会话所需的属性及配置信息                         | yusp-commons-starter-session                                 |
| 数据权限      | 控制不同的人对同一批数据有不同的查询、修改和删除的权限       | yusp-commons-starter-data-authority                          |
| Api调试组件   | 提供swagger进行api查询和调试                                 | yusp-commons-starter-swagger                                 |
| 属性加解密    | 对需要加密处理的属性进行加解密处理                           | yusp-commons-starter-crypt                                   |

#### **udp-cloud**

| 组件            | 功能描述                                                     | jar包名称                    |
| --------------- | ------------------------------------------------------------ | ---------------------------- |
| API访问控制组件 | 支持通过配置方式开放指定API                                  | udp-cloud-apilimit           |
| 负载均衡组件    | 支持全局级别负载均衡配置和动态切换                           | udp-cloud-loadbalance-ribbon |
| 熔断限流组件    | 提供对sentiel限流熔断的增强，之前全局默认级别规则配置        | udp-cloud-sentinel           |
| 消息组件        | 异步消息组件提供异步消息发送和接受的技术支撑。提供消息的可靠性。 | udp-cloud-message            |
| Feign挡板       | mock数据添加和使用                                           | udp-cloud-feign              |
| Nacos配置中心   | 提供Nacos配置中心功能支持和配置修改后能够实时推送到应用端的能力 | udp-cloud-config-nacos       |
| Nacos注册中心   | 提供Nacos注册中心功能支持和扩展                              | usp-cloud-discovery-nacos    |
| 微服务网关      | 扩展支持黑白名单配置、OpenAPI管理、 多维度动态负载均衡、多维度限流熔断降级、多维度容错重试等功能 | udp-cloud-gateway            |

#### udp-base

| 组件        | 功能描述                             | jar包名称           |
| ----------- | ------------------------------------ | ------------------- |
| 基础组件    | 基础功能                             | udp-base-common     |
| 通用工具类  | 提供集合、日期、Bean、反射等相关方法 | udp-base-util       |
| Api调试组件 | 提供swagger查询和调试页面            | udp-base-swagger-ui |


#### 模块和配置变动

##### 1、util组件

由原本的yusp-common项目迁移到udp-base项目

```
新的dependence坐标：
<dependency>
    <groupId>cn.com.yusys.yusp.common</groupId>
    <artifactId>udp-base-util</artifactId>
</dependency>
```

##### 2、swagger组件

### 3.3.2.20241121-RELEASE
1. 升级springboot到2.7.18，并升级一些存在安全漏洞的开源组件
2. 统一版本号