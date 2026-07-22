# 配置使用私服地址

- **前端获取依赖私库地址 `http://artifactory.mis.bcs:8081/artifactory/api/npm/uadp-npm-group`, publish私库地址 `http://artifactory.mis.bcs:8082/artifactory/api/npm/uadp-npm-releases-local/ `**

# 常见问题

## 缓存清除

  当本地调试出现图标丢失问题时，请执行 `yarn clean` 将缓存进行清除

# 安全【加密】

- 通过引入`fe-crypto`加密包，支持`RSA、SM`两种加密方式，前端通过调用`randomID`获取密码，然后通过 `非对称加密`对密码进行加密，并放在请求头`body-encode-key`中，再使用密码对`post`提交的报文体进行`对称加密`,并对内容进行传输。
- 在返回时，通过请求头`body-encoded`标记是否需要解密，通过`body-encode-key`获取对应的密码，然后对报文体进行解密。

```sh
# 处理过程
# 对称密钥：A（前端randomID生成）
# 非对称密钥的公钥：B （前端从服务器端获取）
# 非对称密钥的私钥：C
# 传输内容(报文)：D

# 请求发送前端处理
# 1、用B非对称加密A   --> encode-key
# 1、用A对称加密D  --> encode-data
# 将结果一起加密后的结果一起传输给后端

# 后端接受数据处理：
# 1、获取 encode-key ，通过 C 非对称解密得到 A
# 2、通过 A 对称解密 encode-data  得到明文 D

# 前端接收数据处理
# 1、获取 body-encoded，是否需要界面，通过encode-key C 从缓存中获取 A
# 2、通过 A 对称解密 encode-data  得到明文 D

# 当 KEY_ALGORITHM=SM 时，非对称加密 SM2, 对称加密SM4 -> BODY_ALGORITHM=SM4
# 当 KEY_ALGORITHM=RSA 时，非对称加密 RSA, 对称加密AES -> BODY_ALGORITHM=AES

```

使用方式

###### 配置
1、`src\utils\index.js` 文件中参数 `KEY_ALGORITHM` 、`BODY_ALGORITHM`配置加密方式; `PUBLIC_KEY`标注加密公钥，如果不配置属性，则使用封装在util类中的属性，对应的默认否，不防重，不加密。
2、`src\config\constant\app.data.common.js` 中参数 `IS_ENCODE` 标注是否启用加密功能
3、业务功能使用
```js
// 1、`src\config\constant\app.data.common.js` 中参数 `IS_ENCODE` 标注是否启用加密功能
// 2、跟后端确认好加密方式，并修改 `src\utils\index.js` 文件中参数`KEY_ALGORITHM` 、`BODY_ALGORITHM`配置加密方式值
// 3、公钥对应属性 `PUBLIC_KEY`，跟后端确认好密钥对
// 4、业务页面中发送请求
this.$request({
  url: '{API地址}',
  method: 'post', // 加密只对post请求生效
  // 只有 IS_ENCODE=true && method=post && isCrypto=true 才会进行加解密处理
  isCrypto: true, // 标注需要采用加密，
  ... // 其他参数配置
}).then(res => {
  // do something // 解密后数据结果
})

```

# 安全【防重】
- 通过请求头增加`nonce-request`标注。
- 在数据内容`head`中增加`timestamp`、`nonce`两个值来实现。

使用方式

###### 配置
1、`src\config\constant\app.data.common.js` 中参数 `IS_NONCE` 标注是否启用防重功能
2、业务功能使用
```js
// 1、`src\config\constant\app.data.common.js` 中参数 `IS_NONCE` 标注是否启用防重功能
// 2、业务页面中发送请求
this.$request({
  url: '{API地址}',
  isRepeat: true, // 标注不需要采用防重， true-不防重，false-防重
  ... // 其他参数配置
}).then(res => {
  // do something
})
```
3、如table查询，直接配置属性即可
| request-config | 发送请求时请求全局配置，:request-config="{isCrypto:true,isRepeat:true}"。
isRepeat：是否需要防重-true：不防重，fasle：防重 isRepeat=true/false
isCrypto: 是否需要加密，true-加密，false-不加密
```jsp
<yu-xtable ref="refUTraceListTable" :request-config="{isCrypto:true,isRepeat:true}"></yu-xtable>
```
# uadp-web

> BCS Front-end Platform.

> 1、 yarn install/ npm install

> 2、 yarn dev

> 3、更新插件  yarn upgrade 插件名称@版本号

> 4、其他命令说明：`inspect`[输出 webpack 配置]、`lint`[eslint校验]

# 目录结构

```sh
uadp-web
  ├── .setting                      # 工具配置 老版本配置已废弃
  │     └── .remarks.json           # 目录备注
  ├── build 
  │     └── index.js                # 打包编译配置
  ├── mocks
  │     ├── api                     # 公共常量类
  │     │     ├── common        
  │     │     │     ├── data      
  │     │     │     ├── oauth.js    #通用认证权限MOCk模拟     
  │     │     │     └── crud.js     #通用增、删、改服务
  │     │     └── example           #页面模拟数据
  │     │           └── ...    
  │     ├── index.js                # Mock数据模拟入口
  │     ├── mocks-server.js         # Mock热更新配置
  │     └── register.js             # Mock业务模块等级注册表
  ├── src                         
  │     ├── api                     # 公共常量类
  │     │     ├── common
  │     │     │     ├── crud.js     # 字典请求API 保存和删除数据
  │     │     │     ├── lookup.js   # 字典请求API 后端请求    
  │     │     │     └── oauth.js    # 认证相关API模块
  │     │     ├── example           # 请求示例
  │     │     │     └── ...
  │     │     └── protal            # 映射后台API服务请求示例 
  │     │           └── ...     
  │     ├── assets                  # 静态资源 （样式、图片、字体图标）
  │     │    ├── common             # 字体图标，通用样式
  │     │    ├── default            # 默认主题
  │     │    ├── styles             # 样式变量 
  │     │    └── ...        
  │     ├── components              # 组件
  │     │    ├── base               # 通用组件
  │     │    │     └── ...                  
  │     │    ├── features           # 扩展组件
  │     │    │     └── ...                
  │     │    ├── layout             # 框架
  │     │    │     └── ...                
  │     │    └──  website           # 网站  
  │     │          └── ...     
  │     ├── config                  # 配置文件
  │     │    ├── constant           # 接口请求相关配置
  │     │    │     ├── app.data.common.js     # 全局公共全局变量
  │     │    │     ├── app.data.service.js     # 全局后台API服务映射
  │     │    │     ├── app.data.icons.js        # 图标
  │     │    │     └── app.datalookup.js        # 静态字典
  │     │    ├── interceptors       # 拦截器
  │     │    │     └── axios.js     # axios拦截器配置  
  │     │    ├── other              # 其他配置
  │     │    │     ├── components.js     # 自定义组件全局导入
  │     │    │     ├── css.js       # 导入CSS
  │     │    │     └── other.js     # 其他辅助
  │     │    ├── index.js           # 配置入口
  │     │    └── ...                      
  │     ├── locale                  # 多语言配置
  │     ├── router                  # 产品/项目静态路由配置
  │     ├── store                   # 存储器配置
  │     ├── utils                   # 系统全局方法设定配置
  │     ├── views                   # 页面内容
  │     ├── app.vue                 # 入口页面
  │     └── main.js                 # 入口文件
  ├── tests                         # 单元测试模块
  ├── .env.development              # 开发环境配置文件
  ├── .env.production               # 生产环境配置文件
  ├── .env.staging                  # 环境标识：自定义配置文件
  ├── jest.config.js                # 业务功能
  ├── vue.config.js                 # vue-cli配置文件
  ├── gulpfile.js                   # 皮肤压缩配置
  ├── package.json           
  ... 
```

# [配置全局参数](http://100.89.35.27:8803/uadp-guide/)

- 参见文件：src/config/index.js

- 首页框架布局配置：src/config/frame.js

- 服务访问配置：src/config/interceptors/axios.js
  
  主要是指开发过程中的一些全局个性化参数配置，详见API[全局配置](http://100.89.35.27:8803/uadp-guide/api/core-api.html)
  
  ```js
  // 配置文件路径：src/config/index.js
  module.exports = {
  /**
   * @type {boolean} true|false
   * @description 是否调试模式,用于开启默认日志打印
   */
  isdebug: true,
  
  /**
   * @type {string}
   * @description 系统版本,包含当前产品信息
   */
  version: require('../../package.json').version,
  
  /**
   * @type {string}
   * @description 文件编码
   */
  charset: 'UTF-8',
  /**
   * @type {boolean}
   * @description 是否开启路由缓存
   */
  iscache: true,
  
  /**
   * @type {string}
   * @description 系统标题
   */
  title: 'UADP应用管理框架-长沙银行',
  
  /**
   * @type {String}
   * @description 逻辑系统名称
   */
  sysLogicName: 'PC内管应用',
  
  /**
   * @type {string}
   * @description axios response 解析返回头
   */
  resHeader: '',
  
  //...
  
  }
  ```
  
  引用配置文件：
  
  ```js
  import config from "@/config/index";
  const IS_DEBUG = config.isdebug;
  ```

## 配置组件全局参数

- src/config/components.js

## 配置mock

| 文件                                                                 | 说明            |
| ------------------------------------------------------------------ | ------------- |
| index.js                                                           | 挡板 UI 配置      |
| common/data/\*, example/data/\*                                    | 挡板返回数据配置      |
| crud.js                                                            | 挡板通用增、删、改、查服务 |
| article.js,concise.js,demo.js,maintable.js,subtable.js,uTrace.js 等 | 挡板路径数据映射配置    |

- [Mcok.js官方文档](http://mockjs.com/0.1/)

- 学会如何在工程中使用Mock模拟数据，你需要：
  
  1. 看一下mock文件夹下的`index.js`文件的逻辑
  2. 了解`Mock.mock()`方法
  3. 了解如何生成数据
  
  以下是生成数据的示例解析:

- 在了解了如何使用Mock.js后，在工程中实现Mock模拟数据的步骤如下:
  
  1. 参考`demo.js`（文件路径——mocks/example/demo.js） 文件的写法，实现一个生成模拟数据的js
  
  2. 编辑`vue.config.js`文件，引入mock
     
     ```js
     const mocksServer = require('./mocks/mocks-server.js')
     ```
  
  3. 将devServer下的after属性为mocksServer。具体设置详情参考环境搭建[全局配置介绍](/guide/basic-course/#全局配置介绍)
  
  ```js
    devServer: {
      after: mocksServer //添加这一行
    },
  ```

完成以上步骤，你就可以使用框架中发送请求的方法`request()`发送请求了。比如：你设置的模拟接口信息为 `{ url: '/api/demoModel/test ', method: 'POST', fn: responseFn }`, 返回的数据是什么格式的，取决于你写的模拟数据的js文件，参照第一步，则发送请求的方式如下：

```js
  //推荐使用如下方式
  this.$request({
    url: "/api/demoModel/test",
    method: 'post',
    params: {}
  }).then((code, message, response) => {
    //处理请求成功的情况
  }).catch(error =>{
    //处理请求失败的情况
  })

  //或者
  import { request } from '@/utils';

  request({
    url: "/api/demoModel/test",
    method: 'post',
    params: {}
  }).then((code, message, response) =>{
    //处理请求成功的情况
  }).catch(error =>{
    //处理请求失败的情况
  })  
```

## 配置反向代理

使用vue搭建的项目在本地与后端联调（开发环境）时，因为是使用node运行服务器，IP与后端不一致，所以会产生跨域问题，需要使用如JSONP、跨域代理等手段进行跨域请求。在vue项目中，只需要设置一下proxyTable就可以了。

::: tip
  原理：
  本地服务器 ==》代理 ==> 目标服务器 ==》 拿到数据后通过代理伪装成本地服务器请求的返回值
  ==》 浏览器接收数据
:::

配置相关文件结构

```sh
  xy-web
  ├── .env.development      #开发环境变量设置
  ├── vue.config.js         #跨域设置入口位置
```

#### vue.config.js内devServer配置

```js
  module.exports = {
    ...,
    devServer: {
      disabledHostCheck: true,//屏蔽因安全考虑，默认检查hostname是否在配置内
      port: devPort, //默认端口，配置来源于.env.development中VUE_APP_DEV_PORT（本地服务端口）
                    //devPort = progress.env.VUE_APP_DEV_PORT;
      open: false,//开发服务器是否打开浏览器
      overlay: {
        warning: false,//浏览器是否全屏显示编译的warning
        errors: true//浏览器是否全屏显示编译的error
      },
      proxy: getProxyConfig(),//跨域代理配置 配置修改主要地方
      after: mocksServer//开发环境配置mock服务
    },
    ...
  }
```

- getProxyConfig方法内容

```js
  /**
   * 开发模式，获取代理配置
   * 注: 若配置项VUE_APP_BASE_API包含协议、域名[IP]、端口[可选]，开发模式则默认配置代理
   */
  const getProxyConfig = () => {
    const devEnv = process.env.NODE_ENV === 'development' // 开发环境
    const baseApi = process.env.VUE_APP_BASE_API // 应用服务前缀URL
    let proxyPrefix = process.env.VUE_APP_PROXY_PREFIX; // 代理API前缀
    const match = /^(https?:\/\/[0-9a-z.]+)(:[0-9]+)?([/0-9a-z.]+)?/i // 匹配URL(协议+域名+端口)
    const matchResult = match.exec(baseApi)

    let devServerProxy = {}; // 配置的代理对象，默认false，为空
    if (devEnv && matchResult) {
      // VUE_APP_BASE_API代理配置演示，/dev-proxy-api/xxx-api/* => https://172.16.20.92:8102/xxx-api/*
      // 详见: https://cli.vuejs.org/config/#devserver-proxy
      // 可数组配置多个
      if (Object.prototype.toString.call(proxyPrefix) === '[object String]') {
        proxyPrefix = proxyPrefix.split(',')
      }
      if (Array.isArray(proxyPrefix)) {
        proxyPrefix.forEach(item => {
          devServerProxy[item] = {
            target: `${matchResult[1] + matchResult[2]}`, // 协议+域名
            changeOrigin: true  //允许跨域
          }
        })
      }
    }
    devServerProxy = JSON.stringify(devServerProxy) == '{}' ? false : devServerProxy;
    console.log('配置代理', devServerProxy)
    return devServerProxy
  }
```

- .env.development内容

```
  # 环境标识（开发环境）
  ENV = 'development'

  # APP应用若计划部署在应用服务器子目录，则需要设置publicPath, 
  # 例如：你计划部署在GitHub Pages上，访问路径为：https://your.github.io/app/,则你此处应配置为：'/app/'
  # 默认配置为根：'/'
  VUE_APP_PUBLIC_PATH = ''

  # 应用服务前缀URL
  # 1) 指定应用模式，Protocol(http/https)+IP+PORT。如：'https://172.16.20.92:8102/xyapp'
  #    开发环境：默认开启反向代理，反向代理到：http://localhost:8102/dev-proxy-api/
  # 2) 本地应用模式，前后端部署在一起。如：'' 或者 '/xyapp' 
  VUE_APP_BASE_API = ''
  VUE_APP_BASE_URL = ''

  # Mock模拟数据，true启用，false禁用
  VUE_APP_MOCK_ENABLE = true
  # Mock模拟方式，是否真实服务模拟: true 使用express模拟, false 使用XHR拦截模拟
  VUE_APP_MOCK_MODE = true

  # 本地服务端口
  VUE_APP_DEV_PORT = 8102

  # 代理API前缀
  VUE_APP_PROXY_PREFIX = '^/api'

  # 控制是否启用Babel插件动态导入，当系统有大量页面时，启用配置可以显著提高热更新速度
  # 它只是将所有import()转换为require()，配置之后不支持懒加载（不要用于生产）
  VUE_CLI_BABEL_TRANSPILE_MODULES = true

  # 更多 vue-cli 内置环境变量
  # 详见:  https://github.com/vuejs/vue-cli/blob/dev/packages/@vue/babel-preset-app/index.js
```

::: warning

  实际应用中我们如果需要修改代理则仅需修改 .env.development中

    VUE_APP_PROXY_PREFIX(代理API前缀)
    
    VUE_APP_BASE_API(应用服务前缀URL)

  参数即可

:::

**注： 更多devServer配置内容请查阅[webpack-dev-server文档](https://www.webpackjs.com/configuration/dev-server/)**

**更多反向代理配置内容请查阅[跨域方案](https://mp.weixin.qq.com/s/WL0gtu_hr1em9hsOCl52wA)**

## 配置axios

- 配置相关文件结构

```sh
  uadp-web
  ├── src   
  │     ├── api                             # API请求配置
  │     │     ├── common                    # 全局通用请求方法
  │     │     │     ├── lookup.js           # 字典相关API封装
  │     │     │     ├── ...                      
  │     │     ├── example                   # 请求封装示例
  │     │     │     ├── article.js          # 封装示例
  │     │     │     └── ...                      
  │     │     └── portal                    # 其他请求方法
  │     │           ├── other.js            # 其他方法
  │     │           └── ...                      
  │     ├── config                          # 配置文件
  │     │     ├── interceptors                
  │     │     │     └── axios.js            # 路由拦截设定
  │     │     └── ...
  │     ├── utils                           # 工具集合
  │     │     └── index.js                  # 工具类入口
  │     └── main.js                         # 初始化utils
```

- 挂载请求方法

通过全局挂载至Vue,便于页面内部调用

```js
  //src/utils/index.js
  ...
  import backend from '@/utils/constant/backend'
  import { requestConfig, requestSuccessFunc, requestFailFunc, responseSuccessFunc, responseFailFunc } from '@/config/interceptors/axios'
...

Vue.prototype.$backend = backend;//全局挂载接口服务名

// XHR 方式注册拦截 mock api
const mockEnable = process.env.VUE_APP_MOCK_ENABLE === 'true'
const mockMode = process.env.VUE_APP_MOCK_MODE === 'true' // 模拟模式，true真实express服务，false XHR拦截方式

if (mockEnable && !mockMode) {
  const { mockXHR } = require('@/../mocks')
  mockXHR()
}
Vue.use(XyUtils, {
  lookup: lookup,
  watermark: config.watermark,
  license: license,
  request: {//请求方法挂载
    config: requestConfig,
    requestSuccessFunc,
    requestFailFunc,
    responseSuccessFunc,
    responseFailFunc
  }
})
```

- 请求拦截设定

```js
  //解析返回报文头
  function analysisResponseHeader(header){
    let xyHead = header[resHeader]
    ...
    const retStatus = xyHead.retStatus;//统一处理返回状态
    ...
  }
  export function requestSuccessFunc (config) {
    //请求前置处理函数,对参数进行处理
  }
  export function requestFailFunc (error) {
    //请求前置失败处理函数，请求失败处理
  }
  export function responseSuccessFunc (res) {
    //请求成功处理函数,做权限控制 及登出路由重定向操作
  }
  export function responseFailFunc (error) {
    //请求失败处理，及对应不同状态码做对应提示 
  }
```

- 请求封装

接口方法统一放置于api文件夹下，全局方法放置于common文件夹下，其他方法放置于protal下

```js
  如：src/api/example/article.js

  import { request } from 'uadp-utils'//引入封装拦截器
  /**
   * 查询文章列表
   * @param {Object} query 查询参数对象
   */
  export function fetchList(query) { // 定义导出函数
    return request({ // 调用查询方法
      url: '/article/list', // 接口路径
      method: 'get', // 获取数据方式
      params: query // 查询参数
    })
  }
```

- 请求方法调用

```js
  //任意.vue页面
  <template>...</template>
  <script>
  import { fetchList } from "@/api/example/article"
  export default {
    ...,
    methods: {
      fetchArticle(){
        fetchList({offset: 1, limit: 10}).then(res => {
          //请求完成后调用接下来的代码
          console.log(res);
        });
      }
    }
  }
  </script>
```

#### 同步方式使用axios

:::warning
此方法不兼容IE
:::

```js
  //任意.vue页面
  <template>...</template>
  <script>
  import { fetchList, fetchArticle } from "@/api/example/article"
  export default {
    ...,
    methods: {
      fetchArticle: async function (){
        const res = await fetchList({offset: 1, limit: 10});
        //fetchList请求完成后调用以下代码
        const article = await fetchArticle({id: res[0].id});
        //fetchArticle请求完成后调用以下代码
        console.log(article);
        //...
        }
      }
  }
  </script>
```

#### IE下同步方式使用axios

```js
  //任意.vue页面
  <template>...</template>
  <script>
  import { fetchList, fetchDetail } from "@/api/example/article"
  export default {
    ...,
    methods: {
      fetchArticle: function (){
        fetchList({offset: 1, limit: 10}).then(data => {
          //fetchList请求完成后调用以下代码
          fetchDetail({id: data[0].id}).then(detail => {
            //fetchDetail请求完成后调用以下代码
            console.log(detail);
            //...
          })
        })
      }
    }
  }
  </script>
```

注：更多Axios设置请参考[Axios文档](http://www.axios-js.com/)

## 配置路由拦截器

vue-router路由插件，这里的路由并不是指我们平时所说的硬件路由器，这里的路由就是SPA（单页应用）的路径管理器。再通俗的说，vue-router就是我们WebApp的链接路径管理系统。

- 配置相关文件结构

```sh
 uadp-web
  ├── src                         
  │     ├── router                           # 路由模块
  │     │     ├── more-routes                
  │     │     │     └── example-routes.js    # 其他静态路由
  │     │     ├── constant-more-routes.js    # 挂载moreRoutes 
  │     │     ├── constant-routes.js         # 静态路由
  │     │     ├── index.js                   # 路由配置入口文件
  │     │     └── router-filter.js           # 路由导航守卫
  │     └── main.js                          # 初始化组件，加载路由 
```

- 加载路由设置 main.js

```js
  ...
  import router from './router'
  import App from './App'
  import i18n from '@/locale'
  const defaultSettings = require('./config')
  ...
  new Vue({
    el: defaultSettings.defaultRootId,
    router,//挂载到根节点
    store,
    i18n,
    render: h => h(App)
  })
```

- 基础路由模块配置模式

```js
  //constant-more-routes.js
  import NestedMenu from '@/components/layout/NestedMenu';
  const exampleRoutes = {
    path: 'features',
    component: NestedMenu,
    redirect: 'noRedirect',
    name: 'FeaturesDemo',
    meta: {
      title: 'featuresDemo',
      icon: 'component'
    },
    children: [
      {
        path: 'error',
        component: NestedMenu,
        redirect: 'noRedirect',
        name: 'ErrorPages',
        meta: {
          title: 'errorPages',
          icon: '404'
        },
        children: [
          {
            path: '401',
            component: () => import('@/views/common/error-page/401'),//动态引入页面位置
            name: 'Page401',//页面名称
            meta: { 
              title: 'page401', 
              noCache: true //是否缓存
            }
          },
        ...
        ],
        ...
      },
      ...
    ],
    ...
  }  
```

上述路由嵌套结构

```sh
/features/error/401     
+------------------+       
| error            |             
| +--------------+ |          
| | 401          | |  
| |              | |           
| +--------------+ |            
+------------------+            
```

路由嵌套的实现需要一个被渲染组件包含自己的嵌套 `<router-view>`，如上述代码中的`NestedMenu`组件

NestedMenu.vue

```html
  <NestedMnenu>
    <!--其他NestedMnenu代码-->
    <router-view></router-view>
  </NestedMenu>
```

此时，基于上面的配置，当访问 `/features/error/401` 时，401页面会渲染在`router-view`处，子路由401的切换只会引起`router-view`区域的变化，其他NestedMnenu代码区域不会变动

- 路由守卫设置（src/router/router-filter.js）

```js
  ...
  import NProgress from 'nprogress' // progress bar
  import 'nprogress/nprogress.css' // progress bar style
  ...
  router.beforeEach(async(to, from, next) => {
    //to 进入到哪个路由去
    //from 从哪个路由离开
    //next 路由的控制参数，常用的有next(true)和next(false)
    NProgress.start();//进度加载条开始
    ...
    if (to.path === '/login') {//通过参数判断做权限控制
      next({ path: '/' })
      NProgress.done()
      return
    }
    ...
  })

  // 路由后置拦截器
  router.afterEach(() => {
    NProgress.done()//进度条加载结束
  })
```

注：更多配置内容请查阅[Vue Router文档](https://router.vuejs.org/zh/)

## 配置全局存储

- 配置相关文件结构

```sh
   xy-web
   ├── src   
   │     ├── store                          # 存储器管理
   │     │     └── modules                  # 存储模块
   │     │          ├── app.js              # 全局框架设定，主要是（对入口配置及框架配置内容全局修改）
   │     │          ├── errorLog.js         # 错误日志记录文件  
   │     │          ├── oauth.js            # 用户信息存储、菜单、接口获取动态生成菜单和路由
   │     │          ├── setting.js          # src/config/index.js入口文件信息全局设定
   │     │          └── tagsView.js         # 全局操作tab选项卡内容（新增、删除、清空、获取）                                
   │     ├── getter.js                      # Getters 可以用于监听、state中的值的变化，返回计算后的结果
   │     ├── index.js                       # 动态将存储器模块注入到vuex
   │     └── main.js                        # 挂载存储器
```

- 挂载存储器

```js
  import store from './store'
  const defaultSettings = require('./config')
  ...
  new Vue({
    el: defaultSettings.defaultRootId,
    router,
    store,//全局挂载存储器
    i18n,
    render: h => h(App)
  })
```

#### 实际应用：

:::warning
新建分模块时, 文件需在src/store/modules文件下，建立后会动态注入到vuex
:::

* 使用已有模块

项目里已经有预设的vuex模块，可以直接在项目中使用，预设模块注册在`src\store\getters.js`

```js
  //getter.js
  const getters = {
    language: state => state.app.language, // 默认语言包
    sidebar: state => state.app.sidebar, // 侧边栏状态（打开/关闭）
    device: state => state.app.device, // 设备
    size: state => state.app.size,
    theme: state => state.app.theme, // 选中的皮肤
    menuModel: state => state.app.menuModel, // 菜单模式
    menuShowStat: state => state.app.menuShowStat, // 垂直菜单展示状态（1:展开状态；2:收起状态；3:收起后mouseenter；4:点击锁定后的状态）
    //...
  }

  export default getters
```

- 页面使用

```js
  import {mapState} from 'vuex';
  export default {
    computed: {
      ...mapState({
        sidebar: state => state.app.sidebar //引入sidebar
      }),
      ...mapGetters([
        'menuModel',  //引入menuModel
      ]),
      classObj() {
        return {
          hideSidebar: !this.sidebar.opened,  //使用sidebar
        }
      }
    },
    watch:{
      //使用menuModel
      menuModel: function (val, oldVal) {
        if (val.id === "topTree" || val.id === "topTile") {
          //...
        }
      },
    },
  }
```

* 新建文件 user.js

```js
  //vuex中的数据源，我们需要保存的数据就保存在这里 页面调用this.$store.state
  const state = {
    userInfo: {}
  }

  //修改Vuex中的数据
  const mutations = {
    SET_USERINFO: (state, data) => {
      state.userInfo = data;
    }
  }

  //外部调用修改方法入口
  const actions = {
    getUserInfo({commit , state}) {
      const data= ...;//处理数据
      commit('SET_USERINFO', data);
    }
  }

  export default {
    namespced: true,//解决不同模块命名冲突问题
    state,
    mutations,
    actions
  }
```

- 页面调用

```js
  import {mapState} from 'vuex';
  export default {
    computed:{
      ...mapState({
        userInfo: state => state.user.userInfo
      })
    },
    methods: {
    getUserInfo(){
      this.$store.dispatch('user/getUserInfo');
    }
    }
  }
```

组件通过`Dispatch`调用`Action`，由`Action`调用`Mutation`对仓库`State`进行更新

```js
//组件vue文件
this.$store.dispatch('user/getUserInfo');

//Actions.js
getUserInfo({commit , state}) {
  const data= ...;//处理数据
  commit('SET_USERINFO', data);
}

//Mutations.js
SET_USERINFO: (state, data) => {
  state.userInfo = data;
}
```

当然组件也可以通过`commit`直接调用`Mutation`更新仓库

```js
this.$store.commit('user/SET_USERINFO', data)
```

仓库被更改后，会自动触发组件视图的更新

注： 更多vuex内容请参考[vuex文档](https://vuex.vuejs.org/zh/)

## [登录模式](http://100.89.35.27:8803/uadp-guide/guide/solution/#登录方案)

# git提交备注：

- feat：新功能（feature） 
- fix：修补bug 
- docs：文档（documentation） 
- style：格式（不影响代码运行的改动） 
- refactor：重构（即不是新增功能，也不是修改bug的代码变动） 
- test：增加测试 
- chore：构建过程或辅助工具的变动
- revert:开头，后面跟着被撤销的Commit的Header

# 知识库文档地址

  http://100.89.35.27:8803/uadp-guide/

# 单页面，多页面

首先要思考我们的项目最终的`构建主体`是`单页面`，还是`多页面`，还是`单页 + 多页`，通过他们的优缺点来分析：

- **单页面（SPA）**
  - 优点：体验好，路由之间跳转流程，可定制转场动画，使用了`懒加载`可有效减少首页白屏时间，相较于`多页面`减少了用户访问静态资源服务器的次数等。
  - 缺点：初始会加载较大的静态资源，并且随着业务增长会越来越大，`懒加载`也有他的弊端，不做特殊处理不利于 SEO 等。
- **多页面（MPA）**：
  - 优点：对搜索引擎友好，开发难度较低。
  - 缺点：资源请求较多，整页刷新体验较差，页面间传递数据只能依赖 `URL`，`cookie`，`storage` 等方式，较为局限。
- **SPA + MPA**
  - 这种方式常见于较`老 MPA 项目迁移至 SPA 的情况`，缺点结合两者，两种主体通信方式也只能以兼容`MPA 为准`
  - 不过这种方式也有他的好处，假如你的 SPA 中，有类似文章分享这样（没有后端直出，后端返 `HTML 串`的情况下），想保证用户体验在 SPA 中开发一个页面，在 MPA 中也开发一个页面，去掉没用的依赖，或者直接用原生 JS 来开发，分享出去是 MPA 的文章页面，这样可以**加快分享出去的打开速度，同时也能减少静态资源服务器的压力**，因为如果分享出去的是 SPA 的文章页面，那 SPA 所需的静态资源`至少都需要去进行协商请求`,当然如果服务配置了强缓存就忽略以上所说。

我们首先根据业务所需，来最终确定`构建主体`，而我们选择了`体验至上的 SPA`，并选用 `Vue` 技术栈。

# 其他优化

## 部署配置

以nginx部署为例

1、开启gzip配置

```sh
  gzip  on;
  gzip_min_length  1k;
  gzip_buffers     4 16k;
  gzip_http_version 1.1;
  gzip_comp_level 9;
  gzip_types       text/plain application/x-javascript text/css application/xml text/javascript application/x-httpd-php application/javascript application/json;
  gzip_disable "MSIE [1-6]\.";
  gzip_vary on;
```

2、开启缓存

```sh
  server {
    location ~* \.(html)$ {
      access_log off;
      add_header  Cache-Control  max-age=no-cache;
    }

    location ~* \.(css|js|png|jpg|jpeg|gif|gz|svg|mp4|ogg|ogv|webm|htc|xml|woff)$ {
      # 同上，通配所有以.css/.js/...结尾的请求
      access_log off;
      add_header    Cache-Control  max-age=360000;
    }
  }
```

3、配置反向代理,根据.env.development中代理配置

```sh
  server {
    # 接口代理 (api|oauth|business) 对应 VUE_APP_PROXY_PREFIX; 
    # http://172.16.20.152:9001 对应 VUE_APP_BASE_API
        location ~ /(api|oauth|business)/ {
            proxy_pass http://172.16.20.152:9001;
        }
  }
```

## 打包，构建

这里网上已经有很多优化方法：`dll`，`happypack`，`多线程打包`等，但随着项目的代码量级，每次 dev 保存的时候编译的速度也是会愈来愈慢的，而一过慢的时候我们就不得不进行拆分，这是肯定的，而在拆分之前尽可能容纳更多的可维护的代码，有几个可以尝试和规避的点：

1. 优化项目流程：这个点看起来好像没什么用，但改变却是最直观的，页面/业务上的化简为繁会直接提现到代码上，同时也会增大项目的可维护，可拓展行等。
2. 减少项目文件层级纵向深度。
3. 减少无用业务代码，避免使用无用或者过大依赖（类似 `moment.js` 这样的库）等。

## 代码规范

请强制使用 `eslint`。

## 最后

项目底层构建往往会成为前端忽略的地方，我们不应该只从一个大局观来看待一个大型项目，或者整条业务线，应该对每一行代码精益求精，对开发体验不断优化，慢慢累积才能应对未知的变化。
