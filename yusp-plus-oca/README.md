
<p align="center" >
   <H1>yusp-plus-oca</H1>
</p>



## Introduction
### oca的功能简介
yusp-plus-oca是一套涉及用户，角色，岗位，机构，菜单，控制点，数据权限模板以及功能授权和数据授权等的后台管理系统；与yusp-plus-uaa和yusp-plus-gataway构成了统一开发平台认证-服务端；

### 文件位置
- 1、基础配置文件： 基础配置文件存放于yusp-plus/yusp-plus-oca/yusp-plus-oca-starter/src/main/resources/config/application.yml.apollo,application.yml.apollo , bootstrap.yml,bootstrap-nacos.yml,bootstrap-apollo.yml

### oca的用户，角色，岗位，机构关系：
- 机构同用户，角色，部门，岗位是一对多关系；
- 用户同角色是多对多关系；用户同部门是多对一关系；用户同岗位是多对一关系；
- 用户还可以关联授权机构，关联后用户具有授权机构的权限；

### oca模块，业务功能，菜单，控制点，数据权限模板以及功能授权和数据授权的关系：
- 模块：模块这个概念是对业务功能的划分，一个模块对应多个业务功能，一个业务功能只能属于一种模块；
- 业务功能：业务功能可以理解为前后端共同完成的一个完整需求，权限模型的配置起点便是业务功能，比如菜单、前端页面、页面按钮、后端接口、数据库查询等权限资源均和业务功能有关； 业务功能中的URL链接，对应存储的就是页面 vue 文件的目录地址
- 业务功能类型菜单在创建的时候需要先创建对应的 模块 和 业务功能，也就是新建的菜单必须归属于某一个业务功能，业务功能表中记录了该菜单对应 vue 文件的地址；
- 控制点对应的URL就是后端的api接口，用于前后端权限校验时使用；
- 数据模板是用于控制不同的人对同一批数据有不同的查询、修改和删除的权限;一条控制点数据对应多条数据权限数据，一条数据权限数据也可以从属于多个控制点数据;


### oca数据字典配置
- 样板工程的数据字典分为静态的数据字典和动态数据字典；静态数据字典存放在前端工程中，动态的数据字典通过页面进行配置；
- 数据字典翻译时会首先通过字典代码在静态数据字典中查找，如果查找不到再去配置的数据字典中查找；因此想要页面配置的字典生效，首先查看对应的字典代码在静态字典中是否存在，如果存在必须先把静态字典代码注释；
静态数据字典位置：yusp-plus\yusp-plus-oca-web2.0\src\config\constant\app.data.lookup.es.js和yusp-plus\yusp-plus-oca-web2.0\src\config\constant\app.data.lookup.js

### oca系统提示消息管理
- oca系统的中英文提示异常消息是通过抛出业务异常BizException实现；

  BizException的构造方法:
  >>public BizException(String i18nCode, String errorCode, Throwable cause, Object... data)
  
  参数描述：
  >>i18nCode:消息码，通过该消息吗和data对异常消息进行国际化处理
  >>errorCode: 错误码，此错误返回给前端使用
  >>data: 需要放入异常信息模板的数据
  
  使用方式：
  >>throw BizException.of(“ERROR00001”);
  >>throw BizException.error(“ERROR00001”, “503”);
  >>throw BizException.error(“ERROR00001”, “503”,data); 
  >

i18nCode异常码与异常信息的对应关系存储在本地工程的yusp-plus\yusp-plus-oca\yusp-plus-oca-core\src\main\resources文件夹中，message_msg.properties(中文描述)，message_msg_en_US.properties(英文描述)
errorCode错误码对应的是系统提示消息管理中新增的异常消息；
抛出BizException异常后，系统会对异常进行拦截；查看i18nCode是否有对应的异常消息，如果没有，就会查看errorCode对应的异常消息;最后返回的实体类的message就是对应的异常信息；
### oca引入工作流
- 直接在yusp-plus-oca-core的pom.xml中引入工作流的jar包：
```yaml
        <dependency>
            <groupId>cn.com.yusys.yusp</groupId>
            <artifactId>yusp-plus-flow</artifactId>
        </dependency>
```
- 在yusp-plus/yusp-plus-oca/yusp-plus-oca-starter/src/main/resources/config/application.yml中添加工作流的证书；

### oca引入小U留痕
- 1、直接在yusp-plus-oca-core的pom.xml中引入小U留痕的jar包
```yaml
        <dependency>
            <groupId>cn.com.yusys.yusp</groupId>
            <artifactId>yusp-plus-utrace-core</artifactId>
        </dependency>
```
 

### 文件配置：
- 1.认证：认证方式是oca     
  - 前端配置：修改yusp-plus/yusp-plus-oca-web2.0/.env.development配置文件，VUE_APP_BASE_API = 'http://网关ip:网关端口',UE_APP_SINGLE_SERVER = false;


- 2. 功能权限过滤器配置：yusp-plus/yusp-plus-oca/yusp-plus-oca-starter/src/main/resources/config/application.yml
    - 开启功能权限过滤器：yusp.filter.access.enabled: true 开启功能权限过来器AccessFilter；
    - 功能权限白名单url：AccessFilter拦截请求，从token中userId，如果没有获取到，就会报403错误；如果url配置到访问白名单中，则不会拦截，直接放行。因此获取token前、获取token和不需要token的相关接口都要设置到白名单中:
```yaml
  yusp:
    web:
      ignoreUrls: /api/login/queryuserandchecksecret,/v2/api-docs,/api/login/getuserinfowiththirdparty,/api/login/getuserinfoforpassword/*
```
 - 数据权限白名单api:如果yusp-plus-oca-core中引入了yusp-commons-starter-data-authority数据权限的jar包，DataAuthorityWebFilter就会拦截请求， 从token中userId；如果对应的api配置到访问白名单中，则不会拦截，直接放行。因此通权限拦截一样，获取token前、获取token和不需要token的相关接口都要设置到白名单中:

```yaml
  yusp:
    data-authority:
      ignoredApi: /api/login/queryuserandchecksecret,/v2/api-docs,/api/login/getuserinfowiththirdparty,/api/login/getuserinfoforpassword/*
```

### 其他：
- 1、敏感数据加密：在需要加密的字段上添加@JsonEncrypt注解,仅限于String类型，如手机号，身份证号码
