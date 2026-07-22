/**
 * @created by pan
 * @updated by helin3 2019-04-08
 * @description 根级别的 getters
 */
const getters = {
  language: state => state.app.language, // 默认语言包
  sidebar: state => state.app.sidebar, // 侧边栏状态（打开/关闭）
  device: state => state.app.device, // 设备
  size: state => state.app.size,
  theme: state => state.app.theme, // 选中的皮肤
  menuModel: state => state.app.menuModel, // 菜单模式
  menuShowStat: state => state.app.menuShowStat, // 垂直菜单展示状态（1:展开状态；2:收起状态；3:收起后mouseenter；4:点击锁定后的状态）
  defaultActive: state => state.app.defaultActive, // 激活某一菜单项（设置某一菜单项高亮） 可供外部调用
  currentSizeModeId: state => state.app.currentSizeModeId, // 当前布局模式，紧凑 | 正常

  token: state => state.oauth.token, // 会话token信息
  accessToken: state => state.oauth.accessToken, // 访问token
  refreshToken: state => state.oauth.refreshToken, // 刷新toekn
  expiresIn: state => state.oauth.expiresIn, // 过期时间
  // lastRefreshTime: state => state.oauth.lastRefreshTime, // 上次刷新时间

  userId: state => state.oauth.userId, // 用户ID
  userName: state => state.oauth.userName, // 用户姓名
  userCode: state => state.oauth.userCode, // 用户登录码
  userAvatar: state => state.oauth.userAvatar, // 用户头像
  logicSys: state => state.oauth.logicSys, // 逻辑系统Object
  roles: state => state.oauth.roles, // 角色数组Object
  selectedRoles: state => state.oauth.selectedRoles, // 选中角色
  org: state => state.oauth.org, // 机构Object
  dpt: state => state.oauth.dpt, // 部门Object
  orgCode: state => state.oauth.orgCode, // 当前机构ID
  instu: state => state.oauth.instu, // 金融机构Object
  upOrg: state => state.oauth.upOrg, // 上级机构Object
  upDpt: state => state.oauth.upDpt, // 上级部门Object
  loginCode: state => state.oauth.loginCode, // 登录代码

  otherInfo: state => state.oauth.otherInfo, // 其它用户信息
  originalMenus: state => state.oauth.originalMenus,
  originalCtrls: state => state.oauth.originalCtrls,
  menus: state => state.oauth.menus, // 菜单数据
  currentTopMenu: state => state.oauth.currentTopMenu, // 当前选中的一级菜单
  showLeftMenu: state => state.oauth.showLeftMenu, // 当前选中的一级菜单
  ctrls: state => state.oauth.ctrls, // 控制点权限

  routes: state => state.oauth.routes, // 路由

  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,

  errorLogs: state => state.errorLog.logs,
  title: state => state.app.title,
  orgTreeData: state => state.funData.orgTreeData // 机构树
}
export default getters
