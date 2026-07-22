简体中文

<span id="nav-2"></span>

## 版本历史改动详见 [ChangeLog](CHANGELOG.md)

## ℹ️ 项目介绍
oca-plus是一套轻量级的企业级微服务开发框架，提供了后台管理系统，网关，统一认证中心等基础功能，集成公司文
件服务、定时任务，工作流，数据权限，小U留痕等组件。

uaa构造的统一认证中心，提供多种认证方式对用户进行认证；

优秀的菜单功能权限，前端可灵活控制页面及按钮的展示，后端可对未授权的请求进行权限拦截；

灵活的用户、角色权限管理，对不同的用户和角色提供不同的功能数据权限，可有效防止权限越权；

灵活的国际化配置，支持不同的语言，项目目前已支持简体中文、English，如需新增其它语言，只需增加新语言[i18n]文
件即可；

支持Nacos Eureka多种注册中心、Apollo，Nacos配置中心；

**目前oca-plus已完成信创改造，数据库使用 GoldenDB，中间件使用 TongWeb。**

<span id="nav-3-1"></span>

## 🍊 项目规划
| 项目名称                       | 简介                                |
|----------------------------|-----------------------------------|
| **yusp-plus**              | 样板工程                              |
| - **yusp-plus-common**     | 公共组件包                             |
| - **yusp-plus-crm**        | 样例工程                              |
| - **yusp-plus-dbinit**     | 初始化数据库脚本                          |
| - **yusp-plus-extend**     | 扩展包，引入组件需要添加的额外逻辑                 |
| - **yusp-plus-file**       | 文件上传组件                            |
| - **yusp-plus-generator**  | 后端代码快速生成                          |
| - **yusp-plus-oca**        | 后台管理系统                            |
| - **yusp-plus-oca-web2.0** | 前端工程2.0                           |
| - **yusp-plus-single**     | 单体，引入yusp-plus-single-starter为启动类 |
| - **yusp-plus-uaa**        | 认证中心                              |
| - **...**                  |

<span id="nav-7"></span>

## 💎 快速开始
1. 微服务：（**目前oca-plus不支持微服务方式，请使用单体模式**）
   
    - 前端： 
      - 修改yusp-plus\yusp-plus-oca-web2.0\.env.development
        - VUE_APP_BASE_API = '网关或者Nginx的ip:端口'
        - VUE_APP_SINGLE_SERVER = false  关闭单体模式
      - 安装依赖：npm install  
      - 启动前端：yarn dev  或 npm run dev
    - 后端：
      - 初始化脚本：
        - 修改yusp-plus\yusp-plus-dbinit\src\main\resources\dbinit.properties文件，修改对应的数据库类型，地址，用户名和密码
      - 修改配置文件；
      - 启动yusp-plus-gataway,yusp-plus-uaa,yusp-plus-oca
    
2. 单体模式： 
    - 前端：
        - 修改yusp-plus\yusp-plus-oca-web2.0\.env.development
            - VUE_APP_BASE_API = '单体ip:端口'
            - VUE_APP_SINGLE_SERVER = true  开启单体模式
        - 安装依赖：npm install
        - 启动前端：yarn dev 或 npm run dev
    - 后端：
       - 初始化脚本：
           - ~~修改yusp-plus\yusp-plus-dbinit\src\main\resources\dbinit.properties文件，修改对应的数据库类型，地址，用户名和密码~~
           - 在数据库中执行 yusp-plus-dbinit/src/main/resources/sql/goldendb/oca-init-20241119.sql 中的sql文件（JDK8版本）
           - 在数据库中执行 yusp-plus-dbinit/src/main/resources/sql/goldendb/oca-init-20250915.sql 中的sql文件（JDK17版本）
       - 修改yusp-plus-single-starter中的配置文件；
       - 启动yusp-plus-single-starter YuspPlusSingleStarterMicroserviceApp，需要添加 --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED


3. 其他注意事项
   - 使用方可根据需要将 `yusp-plus-oca-web2.0` 迁移到新的工程目录下，样板工程为了统一更改，暂时将前端和后端工程放在同一目录下。
   - JDK17 版本从 master 拉取最新的版本
   - JDK8 版本从 test/jdk8 分支拉取，且当前分支进进行漏洞修复的维护，不进行功能的新增