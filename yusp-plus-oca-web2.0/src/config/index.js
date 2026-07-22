/**
 * @created by pan
 * @updated by helin3 2019-04-15
 * @description APP全局参数配置
 */
// import backend from '@/config/constant/app.data.service';
module.exports = {
  /**
   * @type {string}
   * @description 多语言配置，默认'zh_CN'
   */
  lang: 'zh_CN',

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
  title: '应用管理框架-长沙银行',

  /**
   * @type {String}
   * @description 逻辑系统名称
   */
  sysLogicName: '应用管理框架',

  /**
   * @type {string}
   * @description axios response 解析返回头
   */
  resHeader: '', // 'XY-Head',

  /**
   * @type {boolean} true | false
   * @description 是否显示tab标签
   */
  tagsView: true,

  /**
   * @type {boolean} true | false
   * @description Whether fix the header
   */
  fixedHeader: false,

  /**
   * @type {boolean} true | false
   * @description 是否多页应用
   */
  mpa: false,

  /**
   * @type {boolean} true | false
   * @description 是否显示左侧菜单logo
   */
  sidebarLogo: true,

  /**
   * @type {boolean} true | false
   * @description 是否必须有角色
   */
  mustRole: false,

  /**
   * @type {string}
   * @description 开始页面
   */
  startPage: 'login',

  /**
   * @type {string}
   * @description 默认root id,不建议修改
   */
  defaultRootId: '#app',

  /**
   * @type {boolean} true|false
   * @description 开启水印, 可在组件工程中设置为指令执行
   */
  watermark: false,

  /**
   * @type {string}
   * @description 默认主题
   */
  theme: 'blue',

  /**
   * @type {string}
   * @description 默认布局
   */
  menumodel: 'left', // left

  /**
  * @type {boolean} true | false
  * @description 客户端监控
  */
  monitorAble: false,

  /**
   * 系统ID, 管控平台的前端监控功能需要用到
   */
  systemPid: 'Y4HGcWH7yKQMR07@yusys.com.cn',

  /**
   * 管控平台的前端监控功能, 每上传一次监控数据间隔时间 单位(毫秒)
   */
  delay: 1200000,

  /**
   * @type {array<string>}
   * @description 路由权限控制白名单
   */
  whiteList: [
    '/login',
    '/auth-redirect'
  ],

  /**
   * @type {string | array} 'production' | ['production', 'development']
   * @description 打印错误日志Need show err logs component.
   * The default is only used in the production env
   * If you want to also use it in dev, you can pass ['production', 'development']
   */
  errorLog: ['production', 'development'],

  /**
   * 发送请求时的loading配置
   */
  requestLoading: {
    // 是否展示loading
    show: false,
    // 局部loading 作用的/覆盖的 DOM 对象，
    // 如果不配置，则显示全局loading
    target: null
    // 缺少控制loading关闭时机的配置，
    // 目前的处理方式为，响应成功或响应失败后立即关闭
  },

  logoutUrl: '/yusp-uaa/api/logout',
  loginUrl: '/yusp-uaa/api/login',
  // 应用名
  basePath: ''
};
