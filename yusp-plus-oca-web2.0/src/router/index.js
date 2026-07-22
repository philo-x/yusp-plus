
/**
 * @created by pan
 * @updated by helin3 2019-04-06
 * @description 路由配置
 * 注：使用router.addRoutes的代码，请注意通过resetRouter恢复初始状态（去除通配符路由）
 */
import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store'
import { isdebug } from '@/config/index'
import { isString, isObject, extend, logger, clone, storage} from '@/utils'
import { resolveQueryParameters } from '@/utils/util'
import { MENU_STOREOG_KEY, ROUTER_STORE_KEY } from '@/config/constant/app.data.common'
import { loadView } from '@/utils/loadView'
const Layout = () => import(/* webpackChunkName: "Layout" */ '@/components/layout')
const NestedMenu = () => import(/* webpackChunkName: "NestedMenu" */ '@/components/layout/NestedMenu')
// 静态（不做权限控制）路由
import constantRoutes from './constant-routes'

Vue.use(Router)

// const originalPush = Router.prototype.push
// Router.prototype.push = function push (location) {
//   return originalPush.call(this, location).catch(err => err)
// }

/**
 * 创建路由，并赋值静态（不做权限控制）路由
 */
const createRouter = () => new Router({
  // mode: 'history', // 需要后台服务支持
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
//处理路由名字
function handleRouteName(path) {
  const REG = /([^/]+)$/;
  const RANDOM = parseInt((Math.random() * 9 + 1) * 100000);
  return path.substring(path.lastIndexOf("/") + 1, path.length) + '-' + RANDOM;
}

//处理默认meta
function setMeta(rname) {
  return {
    title: rname,
    hidden: true,
    showTab: false,
    noCache: false,
    data: null
  }
}
// 初始化路由
const router = createRouter()
// 添加通配符路由（跳转404页面）
router.addRoutes([{ path: '*', redirect: '/404', hidden: true }])
/**
 * 自定义方法添加动态路由
 * @param {Object|String} route 路由对象或路由路径
 * @param {String} title 路由参数或路由标题
 * @param {Object} meta 路由参数
 * @param {String} path 路由名称，当route与当前页路径不同时存在
 * @description 注，动态添加得路由刷新时会跳转到404
 */
Router.prototype.addRoute = function(route, title, meta, path) {
  if (isString(route)) {
    const paramRouter = { path: '/', hidden: true, meta: meta, component: Layout, children: [{
      path: path || route,
      name: path || route,
      meta: extend(isString(title) ? {
        title: title, activeMenu: meta && meta.activeMenu, params: meta && meta.params, noCache: meta.noCache, manAddTagFlag: meta.manAddTagFlag
      } : meta, {}),
      commentsRouter : route,
      // #TODO fixed: Critical dependency: the request of a dependency is an expression
      // #TODO fixed: Cannot read property 'range' of null
      // component: resolve => require([`@/views/${route}`], resolve) // () => import(`@/views/${route}`)
      // component: () => import('@/views/' + route)
      // component: () => Promise.resolve(require('@/views/' + route).default)
      // component: () => {
      //   // 组件name 赋值
      //   const curComponent = loadView(route) // require('@/views/' + route).default;
      //   const obj = {};
      //   clone(curComponent, obj);
      //   obj.name = path || route;
      //   return Promise.resolve(obj);
      // }
      component: loadView(route, path || route)
    }]}
    // 将动态路由添加到session中
    let dynamicRoutesParms = JSON.parse(storage.get("dynamicRoutes"));
    const sessionParm = clone(paramRouter, {});
    sessionParm.component = {}
    sessionParm.children[0].component = {}
    //判断是否是首次加载
    if(dynamicRoutesParms && dynamicRoutesParms.dRoute.length > 0) {
      if(JSON.stringify(dynamicRoutesParms.dRoute).indexOf(JSON.stringify(sessionParm)) === -1) {
        dynamicRoutesParms.dRoute.push(sessionParm)
        storage.set("dynamicRoutes", JSON.stringify(dynamicRoutesParms))
      }
    }else{
      dynamicRoutesParms = {};
      storage.set("dynamicRoutes", JSON.stringify({dRoute:[sessionParm]}))
    }  
    router.addRoutes([paramRouter])
  } else if (isObject(route)) {
    route['name'] && (route['name'] = route.path)
    router.addRoutes([{ path: '/', component: Layout, children: [route]}])
  }
  isdebug && logger.warn('【router/index addRoute】', route)
}

const _routeReplace = Router.prototype.replace
/**
 * 注入路由replace信息
 * @param {Object} location 路由对象
 * @param {Function} onComplete 替换完成时回调
 * @param {Function} onAbort 终止时回调
*/
Router.prototype.replace = function(location, onComplete, onAbort) {
  // vue-router如果提供了 path，params 会被忽略，故如果传path，改query参数
  location.params = location.params || {}
  location.query = location.query || {}
  if (location.name && !location.rollback) {
    location.params['_routeType'] = 'replace' // 不添加页签
    location.params['_form'] = { name: this.history.current.name, params: this.history.current.params }
    // window.sessionStorage.setItem('routeType', 'replace');
  } else if (location.path && !location.rollback) {
    location.query['_routeType'] = 'replace' // 不添加页签
    // location.query['_form'] = this.history.current
  }
  _routeReplace.call(this, location, onComplete, onAbort)
}
/**
 * 回退
 * @param {Object} location 路由对象
 * @param {Function} onComplete 替换完成时回调
 * @param {Function} onAbort 终止时回调
*/
Router.prototype.rollback = function(location, onComplete, onAbort) {
  // #TODO 注意此处多参数处理及query参数处理
  this.replace({ name: location.name, params: location.params, rollback: true })
}

/****
 * 打开无tab页签页面,支持路由回退
 * @param {Object} location 路由对象
 * 
 */
const _routePush = Router.prototype.push
Router.prototype.routePush = function(location) {
  // vue-router如果提供了 path，params 会被忽略，故如果传path，改query参数
  location.params = location.params || {}
  location.query = location.query || {}
  if (location.name) {
    location.params['_routeType'] = 'push' // 不添加页签
    // window.sessionStorage.setItem('routeType', 'replace');
  } else if (location.path) {
    location.query['_routeType'] = 'push' // 不添加页签
    // location.query['_form'] = this.history.current
  }
  _routePush.call(this, location)
}


/**
 * 添加tab标签
 * @param {Object} route 路由信息对象
 */
Router.prototype.addTab = function(route) {  
  const menuList = storage.get(MENU_STOREOG_KEY)
  const menuFunId = [];
  const { data, params } = route;
  const meta = {
    params: {} // 解决params不能赋值问题
  };
  let newName = '';
  let { name } = route; 
  if(data) {
    clone(data, meta.params)
  }
  if(route.title) {
    meta.title = route.title
  }

  // 新增页签是否缓存,默认为false缓存
  if (route.noCache) {
    meta.noCache = route.noCache
  } else {
    meta.noCache = false
  }
  
  /*菜单高亮 -- 添加菜单高亮activeMenu*/
  for(var i = 0;i < menuList.length;i++) {
    menuFunId.push(menuList[i].funcId);
  } 
  // 激活当前非菜单路由父级菜单高亮
  if(params && params.activeMenu && params.activeMenu !== router.currentRoute.fullPath) {
    meta.activeMenu = router.currentRoute.fullPath
  }
  if (menuFunId.indexOf(name) === -1) {
    meta.activeMenu = router.currentRoute.fullPath
  } else {
    meta.activeMenu = "/" + menuList[menuFunId.indexOf(name)].funcUrl 
  }
  if (route.data && route.data.activeMenu) {
    meta.activeMenu = route.data.activeMenu
  } else {
    meta.activeMenu = router.currentRoute.fullPath
  }  
  /* 同名tab多开 & 添加动态路由 */
  if (name) {
    if (route.key) {
      newName = getRouterUrl(route.name) || name
      name = newName + '/' + route.key;
      if (!getRouterUrl(name)) {
        const sysCompName = newName.replace(/\//g, '');
        // 统一设置组件名称
        meta.sysCompName = sysCompName;
        this.addRoute(newName, route.title || 'no-name', meta, name, route.key, sysCompName);
      } 
    } else {
      const sysCompName = name.replace(/\//g, '');
      // 统一设置组件名称
      meta.sysCompName = sysCompName;
      // 动态添加路由
      this.addRoute(name, route.title || 'no-name', meta, name, '', sysCompName);
    }
    // 处理tagViews和cachedViews状态数据
    store.dispatch('tagsView/addView', {
      fullPath: '/' + name,
      path: '/' + name,
      name,
      meta,
      title: route.title || 'no-name',
      params: data
    });
    const param = {name: name, meta, params: data}
    router.push(param, (route) => {
      route.meta['params'] = data;
    });
  } else {
    isdebug && logger.error('【addTab】', route)
  }
}

/**
 * 删除tab标签
 * @param {String} path 路由路径
 */
Router.prototype.removeTab = function (path) {
  let name = getRouterName(path);
  if (name.indexOf('/') === 0) {
    name = name.substr(1); // 删除第一个'/'字符,确保与路由name一致
  }
  store.dispatch('tagsView/delVisitedView', { path });
  store.dispatch('tagsView/delCachedView', { name });
  const visitedViews = store.state.tagsView.visitedViews;
  const lastView = visitedViews[visitedViews.length - 1];
  router.push({
    path: lastView.fullPath,
    params: lastView.params
  });
};

// 通过访问路由路径获取路由名称
export const getRouterName = path => {
  const visitedViews = store.state.tagsView.visitedViews;
  const viewsObj = visitedViews.find(item => {
    return path === item.path;
  });
  return viewsObj.name;
};
// 通过路由名称获取路由路径
export const getRouterUrl = name => {
  const routerList = storage.get(ROUTER_STORE_KEY);
  const routerArr = [];

  function transfer(treeData) {
    if (!(!(treeData.hasOwnProperty('name') || treeData.hasOwnProperty('children')) || !treeData)) {
      let arr = []
      const obj = {}
      obj.name = treeData.name || "";
      obj.path = treeData.path;
      if(treeData.meta && treeData.meta.filePath) {
        obj.path = treeData.meta.filePath;
      }
      if(treeData.children && treeData.children.length > 0) {
        obj.children = treeData.children.map(value => {
          return value.name
        })
        treeData.children.forEach(value => {
          arr = arr.concat(transfer(value))
        })
      }
      arr.push(obj)  
      
      return arr
    } else { 
      return []
    }
  }
  for(var i = 0 ;i < routerList.length ;i++) {
    routerArr.push(...transfer(routerList[i]))
  }
  for(var j = 0 ;j < routerArr.length ; j++) {
    if(name === routerArr[j].name) {
      return routerArr[j].path
    }
  }
  return ""
}
// 外链（浏览器标签页打开）匹配正则
const externalLink = /^https?:\/\//
// 外链（iframe方式打开）匹配正则
const iframeLink = /^iframelink=(http(s)?)?/
/**
 * 菜单数据转换为路由
 * 1.若是顶级菜单(非叶子菜单)，则路由component是布局组件: Layout,此方法无法判断是否为一级菜单路由，则暂且设置为NestedMenu
 * 2.若非叶子菜单(且不是顶级菜单)，则路由component是嵌套组件: NestedMenu
 * 3.若是叶子菜单，则是routeUrl对应页面(懒加载): () => import(`@/views/${routeUrl}`)
 * @param {*} node 菜单节点
 */
export const getRouteData = node => {
  const { mId, mText, mPid, mIcon, routeId, routeUrl, children } = node
  let path = '/' + (routeUrl || mId)
  const queryParams = resolveQueryParameters(path)
  path = path && path.indexOf('?') > -1 ? path.substr(0, path.indexOf('?')) : path
  const route = {
    path: path,
    name: mId,
    meta: {
      id: mId,
      title: mText,
      pid: mPid,
      icon: mIcon,
      routeId: routeId,
      routeUrl: routeUrl,
      sysCompName: path.replace(/\//g, '')
    },
    children: children || []
  }
  if (routeUrl) { // 存在即表示叶子节点
    if (externalLink.test(routeUrl)) {
      // 外链（浏览器标签页打开），不添加component属性
      route.meta.noCache = true
      route.component = undefined
      route.children.push({ path: routeUrl, meta: route.meta })
    } else if (iframeLink.test(routeUrl)) {
      // 外链（iframe方式打开），跳转公共iframe组件
      route.meta.noCache = true
      route.path = `/iframelink/${mId}`
      route.component = () => import('@/views/common/iframe-link/index')
    } else {
      // 如果path第一个字符是/就去掉
      if(path.indexOf('/') === 0) {
        path = path.substr(1)
      }
      // #TODO fixed: Critical dependency: the request of a dependency is an expression
      // #TODO fixed: Cannot read property 'range' of null
      // route.component = resolve => require([`@/views/${route}`], resolve) // () => import(`@/views/${path}`)
      // route.component = () => import('@/views/' + path)
      // route.component = () => Promise.resolve(require('@/views/' + path).default)
      // route.component = () => {
      //   // 组件name 赋值
      //   const curComponent = () => import('@/views/' + path);
      //   const obj = {};
      //   clone(curComponent, obj);
      //   obj.name = mId;// path.replace(/\//g, '');
      //   return Promise.resolve(obj);
      // }
      route.component = loadView(path, mId)
    }
  } else {
    route.redirect = 'noRedirect'
    route.component = NestedMenu
  }
  if (queryParams) {
    route.query = queryParams
  }
  return route
}

/**
 * 重置路由为初始状态
 */
export function resetRouter() {
  // Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // 重置路由为初始状态
}

export default router
