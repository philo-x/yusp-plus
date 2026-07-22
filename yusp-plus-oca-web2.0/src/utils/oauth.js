/**
 * @created by helin3 2019-04-10
 * @updated by
 * @description 认证相关工具
 */
/* eslint camelcase: 0 */
import store from '@/store'
import {getRouteData, router} from '@/router'
import Layout from '@/components/layout'
import {refreshTokenFn} from '@/api/common/oauth'
import {looseEqual, sessionStore, toMappingFn} from '@/utils'
import {
  CTRL_JSON_ROOT,
  CTRL_STORE_KEY,
  CTRL_STOREOG_KEY,
  CURRENT_TOP_MENU_STORE_KEY,
  DYNAMIC_ROUTES,
  DYNAMIC_ROUTES_PARAMS,
  MENU_JSON_ROOT,
  MENU_MAPPING,
  MENU_ROOT_PID,
  MENU_STORE_KEY,
  MENU_STOREOG_KEY,
  ROUTER_STORE_KEY,
  USER_STORE_KEY,
  XIAO_YU_TOKEN
} from '@/config/constant/app.data.common'
import { fireRequest, setTokenStatus, getTokenStatus } from '@/config/interceptors/axios.utils.js'

export const refreshToken = async function () {
  const _refreshToken = isRefreshToken();
  if (_refreshToken === true && !getTokenStatus()) {
    setTokenStatus(true)
    // access_token 已失效，重新获取；
    const tokenObj = sessionStore.get(XIAO_YU_TOKEN) || {};
    const refreshtokenObj = await refreshTokenFn(tokenObj.refresh_token);
    setTokenStatus(false)
    if (refreshtokenObj && refreshtokenObj.access_token) {
      // sessionStore.set(XIAO_YU_TOKEN, refreshtokenObj);
      setToken(refreshtokenObj);
      fireRequest(refreshtokenObj.access_token)
      return refreshtokenObj.access_token;
    } else {
      logoutCleanFn();
      return '';
    }
  } else if (_refreshToken === false) {
    return ''
  }
  return _refreshToken.access_token
};
/**
 * 退出登录清除缓存
 */
export const logoutCleanFn = function () {
  // 清除用户信息
  // 先判断token是否存在
  const token = getToken();
  if (token) {
    store.dispatch('oauth/logout', true);
  }
  /**
   * 在 src/utils/permission.js 中，有以下逻辑
   *  1.获取Token信息，判定是否已登录
   *  2.不存在token信息，当前状态是未登录
   *   2-1.当前访问路由，在免登录白名单中，直接访问当前
   *   2-2.当前访问路由，不在免登录白名单中，重定向跳转登录路由（页面），并附带参数存储当前路由信息
   */
  sessionStore.remove(XIAO_YU_TOKEN);
};
/**
 * @description 标注token是否要刷新
 */
export const isRefreshToken = function () {
  const tokenObj = sessionStore.get(XIAO_YU_TOKEN);
  const currentTime = new Date().getTime(); // 当前时间
  if (!tokenObj) {
    return false
  }
  if (tokenObj.expires_in && (currentTime - tokenObj.buildTime) / 1000 > tokenObj.expires_in) {
    return true
  }
  return tokenObj
};
export function getToken() {
  const accessToken = refreshToken();
  return accessToken ? sessionStore.get(XIAO_YU_TOKEN) : '';
}
export function getAccessToken() {
  const token = getToken();
  if(token) {
    return token.access_token;
  }
  return ''
}
export function setToken(token) {
  // 记录 access_token 时间（相对于浏览器客户端来说），默认减去1min中
  if (typeof token == 'object') {
    var currentTime = new Date().getTime(); // 当前时间
    // 记录 access_token 时间（相对于浏览器客户端来说），默认减去1min中
    token.buildTime = currentTime - Math.ceil(token.expires_in / 3) * 1000; // 单位：毫秒
    // token.buildTime = currentTime // 配合后端认证策略提前过期获取到的token和之前一样 故不能提前失效token
  }
  return sessionStore.set(XIAO_YU_TOKEN, token)
}
export function putToken(token) {
  return setToken(token)
}
/**
 * 退出登录
 * @param already undefined|false 主动触发退出，如：logout(); true 会话已过期，如：logout(true);
 */
export async function logout(already) {
  if (already) {
    await logoutCleanFn();
    router.push(`/login?redirect=${router.currentRoute.fullPath}`)
  } else {
    await store.dispatch('oauth/logout')
  }
  // 清除水印 #TODO
  window.watermark && window.watermark.clearWatermark()
}
/**
 * @description 移除token缓存
 */
export function removeToken() {
  return sessionStore.remove(XIAO_YU_TOKEN)
}
/**
 * @description 移除所有session缓存
 */
export function cleanAllSession() {
  return sessionStore.clear()
}
/**
 * @description 移除部分session缓存
 */
export function cleanSession() {
  const keys = [XIAO_YU_TOKEN, DYNAMIC_ROUTES_PARAMS, DYNAMIC_ROUTES, CTRL_JSON_ROOT, MENU_JSON_ROOT, ROUTER_STORE_KEY, CURRENT_TOP_MENU_STORE_KEY, USER_STORE_KEY, MENU_ROOT_PID, MENU_STOREOG_KEY, MENU_STORE_KEY, CTRL_STOREOG_KEY, CTRL_STORE_KEY];
  keys.forEach(k => {
    sessionStore.remove(k)
  })
}
export function getContrs() {
  return sessionStore.get(CTRL_STOREOG_KEY);
}

/**
 * 处理一级菜单直接为功能点的情况，如首页
 * @param {*} rootMenus 根菜单
 */
const dealRootFuns = (rootMenus) => {
  const rootMenusLenth = rootMenus.length;
  for (let i = 0; i < rootMenusLenth; i++) {
    const menu = rootMenus[i];
    if (menu.children.length > 0) {
      // 一级菜单，若不是对应功能点，则component组件，须配置为Layout
      menu.component = menu.meta.routeUrl ? menu.component : Layout;
      continue
    }
    // 一级菜单直接是功能点的情况
    if (menu.component || menu.component !== Layout) {
      delete menu.children;
      const title = menu.meta.title;
      if (title === 'dashboard' || title === '首页') {
        menu.meta.affix = true
      }
      rootMenus[i] = {
        path: menu.name,
        component: Layout,
        redirect: menu.path,
        children: [
          {...menu}
        ]
      }
    }
  }
};

/**
 * 动态生成路由
 * @param {Array} nodes 菜单数据
 */
export function generateRoutes(nodes) {
  const mapping = MENU_MAPPING;
  const len = nodes.length;
  const rootMenus = [];
  const tmpNodesObj = {};
  if (mapping) {
    // 去除无用转换配置（如：userId: 'userId'），提升转换效率
    for (const key in mapping) {
      if (key === mapping[key]) {
        delete mapping[key]
      }
    }
  }
  for (let i = 0; i < len; i++) {
    let node = nodes[i];
    if (mapping) {
      node = toMappingFn(node, mapping) // 调用映射转换方法
    }
    // 多语言翻译菜单
    // node.mText = (node['i18nKey'] && getI18nMessage('menu.' + node['i18nKey'])) || node.mText;
    nodes[i] = node;
    tmpNodesObj[node.mId] = node
  }
  for (let i = 0; i < len; i++) {
    const node = nodes[i]; // 当前节点对象
    const idValue = node.mId; // 节点ID(唯一键值)
    const pidValue = node.mPid; // 指向父节点ID

    const route = getRouteData(node);
    const pNode = tmpNodesObj[pidValue]; // 父节点对象
    if (pNode && !looseEqual(idValue, pidValue)) {
      pNode.children = pNode.children || [];
      pNode.children.push(route)
    } else {
      rootMenus.push(route)
    }
    // 更新节点引用关系
    tmpNodesObj[idValue] = route
  }
  dealRootFuns(rootMenus);

  return rootMenus
}
