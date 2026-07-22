/**
 * @description 定义公共全局变量使用
 */
export const X_AUTHORIZATION = 'Authorization'
export const XIAO_YU_TOKEN = 'UFP-' + X_AUTHORIZATION // token存储key值
export const USER_STORE_KEY = 'YUFP-SESSION-USER' // 会话存储前缀
export const MENU_ROOT_PID = '0' // 菜单根节点父级Id
export const MENU_STOREOG_KEY = 'YUFP-SESSION-MENUS-OG'
export const MENU_STORE_KEY = 'YUFP-SESSION-MENUS' // 菜单存储前缀
export const CTRL_STOREOG_KEY = 'YUFP-SESSION-STRLS-OG'
export const CTRL_STORE_KEY = 'YUFP-SESSION-STRLS'
export const CURRENT_TOP_MENU_STORE_KEY = 'YUFP-SESSION-CURRENT_TOP_MENU' // 当前选中的一级菜单存储前缀
export const ROUTER_STORE_KEY = 'YUFP-SESSION-ROUTER'
export const USER_JSON_ROOT = '' // 用户返回数据节点,如：'data.user'
// #TODO 有部分代码此处key值被写的不一样
export const MENU_JSON_ROOT = 'menu' // 菜单返回数据节点,如：'data.menus'
export const CTRL_JSON_ROOT = 'contr' // 控制点返回数据节点,如：'data.ctrls'，控制点数据，查询需按菜单ID、功能ID排序返回
export const DYNAMIC_ROUTES = 'dynamicRoutes' // 动态路由，页面动态增加路由时存储动态路由
export const DYNAMIC_ROUTES_PARAMS = 'DYNAMIC_ROUTES_PARAMS' // 动态路由对应的参数信息
export const VIEW_SIZE = 'VIEW-SIZE' // 视窗大小
export const LANGUAGE = 'language' // 语言
export const SCREENFULL = 'screenfull' // 是否全屏
export const IS_REPLAY = true // 是否使用密码防重：在登录界面中使用
export const IS_ENCODE = true // 是否启用加密，true时，各接口的配置isCrypto才生效，false时，优先级最高
export const IS_NONCE = true // 是否接口防重

export const USER_MAPPING = { // 用户后端数据模型映射
  userId: 'userId', // 用户ID
  userName: 'userName', // 用户姓名
  userCode: 'loginCode', // 用户登录码
  userAvatar: 'userAvatar', // 用户头像
  logicSys: 'logicSys', // 逻辑系统Object
  roles: 'roles', // 角色数组Object
  org: 'org', // 机构Object
  dpt: 'dpt', // 部门Object
  instu: 'instuOrg', // 金融机构Object
  upOrg: 'upOrg', // 上级机构Object
  upDpt: 'upDpt', // 上级部门Object
  loginCode: 'loginCode' // 登录代码
}

export const MENU_MAPPING = { // 菜单后端数据模型映射
  mId: 'menuId', // 菜单ID
  mText: 'menuName', // 菜单名称
  mPid: 'upMenuId', // 上级菜单ID
  mIcon: 'menuIcon', // 菜单图标
  routeId: 'funcId', // 菜单功能ID
  routeUrl: 'funcUrl', // 菜单功能URL
  mType: 'menuType' // 菜单类型
}

export const CONTRL_MAPPING = { // 控制点数据模型映射
  mId: 'menuId', // 菜单ID
  rId: 'funcId', // 菜单功能ID
  cId: 'contrCode', // 控制点CODE
  cText: 'contrName' // 控制点名称
}