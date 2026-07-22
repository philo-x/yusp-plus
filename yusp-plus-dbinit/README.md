
<p align="center" >
   <H1>yusp-plus-dbinit</H1>
</p>



## Introduction
yusp-plus-dbinit是初始化yusp-plus数据库脚本的服务；实现启动自动部署数据库;

### 文件位置
- 1、基础配置文件： 基础配置文件存放于yusp-plus/yusp-plus-dbinit/src/main/resources/config/dbinit.properties，这个配置文件配置要初始化数据库的类型，数据库地址，用户名和密码；
> 如果要把yusp-plus相关数据库初始化到mysql中，相关的配置如下：
```property  
  db.active=mysql
  mysql.jdbc.url=jdbc:mysql://localhost:3306/yusp1?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
  mysql.jdbc.username=root
  mysql.jdbc.password=root
```

### 数据库脚本：
- 1、yusp-plus-dbinit数据库脚本位于yusp-plus/yusp-plus-dbinit/src/main/resources/sql文件夹下；sql文件夹下的脚本按照数据库的类型非为db2,mysql和oracle三个文件夹，对应的是插入不同数据库的脚本；
- 2、只初始化以ddl或者dml结尾的脚本；其中以ddl结尾脚本先执行，是创建表的脚本，而以dml结尾脚本后执行，是插入表脚本；
- 3、脚本是按照不同功能进行划分，yusp-os-xxxx代表后台管理功能相关的表和数据；yusp-quartz-xxx代表定时任务相关表和数据；yusp-flow-xxx代表工作流相关的表和数据；