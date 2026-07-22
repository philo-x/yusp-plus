
/**
 * @created by helin3 2019-04-10
 * @updated by
 * @description constantRoutes 静态路由，代表不需要动态判断权限的路由，如登录页、404、等通用页面
 * {
 *   path                         // 路由路径
 *   component                    // 路由视图组件
 *   children                     // 嵌套路由
 *   hidden: true                 // 当设置true的时候该路由不会再侧边栏出现（默认:false），如：401,404,login等
 *   alwaysShow: true             // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                   只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                   若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                   你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 *
 *   redirect: noRedirect         // 当设置 noRedirect 的时候，该路由在面包屑导航中不可被点击
 *   name:'router-name'           // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 *   meta : {
 *     roles: ['admin','editor']    // 设置该路由进入的权限，支持多个权限叠加
 *     title: 'title'               // 设置该路由在侧边栏和面包屑中展示的名字，推荐设置
 *     icon: 'svg-name'             // 设置该路由的图标
 *     noCache: true                // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
 *     breadcrumb: false            // 如果设置为false，则不会在breadcrumb面包屑中显示(默认 true)
 *     affix: true                  // 如果设置为true，将会始终固定在页签上(默认 false)，如: dashboard
 *     activeMenu: '/example/list'  // 如果设置了值，则会高亮设定值的菜单
 *   }
 * }
 */
const Layout = () => import(/* webpackChunkName: "Layout" */ '@/components/layout')
import constantMoreRoutes from './constant-more-routes'
import { storage } from 'uadp-utils'
import { loadView } from '@/utils/loadView'
import { DYNAMIC_ROUTES } from '@/config/constant/app.data.common'

const SHOW_MORE_ROUTES = true // 是否加载本地路由（菜单）

let constantRoutes = [
  {
    path: '/redirect', // 通用跳转（重定向）路由
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/common/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/components/base/Login'),
    hidden: true
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/common/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    name: '401',
    component: () => import('@/views/common/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        component: () => import('@/views/common/dashboard/index'),
        name: 'Dashboard',
        meta: { title: 'dashboard', icon: 'yu-icon-home', noCache: true, affix: true }
      }
    ]
  },
  {
    path: '/scheduleLog', // 定时任务-日志列表
    component: Layout,
    redirect: '/systemManager/schedule/log/log',
    children: [
      {
        path: '/systemManager/schedule/log/log',
        component: () => import('@/views/content/systemManager/schedule/log/log'),
      }
    ]
  },
  {
    path: '/guide',
    component: Layout,
    hidden: true,
    redirect: '/guide/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/common/guide/index'),
        name: 'Guide',
        meta: { title: 'guide', icon: 'guide', noCache: true }
      }
    ]
  }
]

// 合并动态路由数据
const dynamicRouters = JSON.parse(storage.get(DYNAMIC_ROUTES));
if(dynamicRouters) {
  const dynamicRoutes = dynamicRouters.dRoute; 
  for(let i = 0;i < dynamicRoutes.length;i++) {
    const path = dynamicRoutes[i].children[0].commentsRouter;
    if (dynamicRoutes[i].children[0].commentsRouter[0] !== '/') {
      dynamicRoutes[i].children[0].commentsRouter = `/${path}`;
    }
    dynamicRoutes[i].children[0].component = loadView(dynamicRoutes[i].children[0].commentsRouter, path);
    dynamicRoutes[i].component = Layout;
  }
  constantRoutes = [...constantRoutes, ...dynamicRoutes]
}
if (SHOW_MORE_ROUTES) {
  constantRoutes = [...constantRoutes, ...constantMoreRoutes]
}
export default constantRoutes
