/**
 * @created by helin3 2019-04-10
 * @updated by
 * @description moreRoutes 更多静态路由，根据./constant-routes SHOW_MORE_ROUTES动态添加
 * 自动注册./modules目录下所有动态路由，不递归处理子目录
 * 此路由配置仅用于系统无权限控制路由
 */

// https://webpack.js.org/guides/dependency-management/#requirecontext
const modulesFiles = require.context('./more-routes', false, /\.js$/)

// you do not need `import app from './modules/app'`
// it will auto require all vuex module from modules file
const modules = modulesFiles.keys().reduce((modules, modulePath) => {
  // set './app.js' => 'app'
  // const moduleName = modulePath.replace(/^\.\/(.*)\.\w+$/, '$1')
  const value = modulesFiles(modulePath)
  modules.routes = modules.routes || []
  modules.routes = [...modules.routes, ...value.default]
  return modules
}, {})

export default [
  ...modules.routes
]
