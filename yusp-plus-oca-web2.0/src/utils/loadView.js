/**
 * @description 懒加载路由
 * @param {String} view 待加载路由路径
 * @returns Promise 返回加载对应的vue页面
 */
export const loadView = (view, name) => {
  // 去掉第一个/，优化上一版写法
  view = /^\/.*/.test(view) ? view.slice(1) : view
  if (process.env.NODE_ENV === 'development') {
    return () => {
      const component = require(`@/views/${view}`).default
      if(!component.name && name) {
        component.name = name
      }
      return Promise.resolve(component)
    }
    // return resolve => { require.ensure([], () => resolve(require(`@/views/${view}`)), '[request]')}
  } else {
    return () => {
      // /* webpackChunkName: "[request]" */ 单独指定chunk名称，也可通过output.chunkFilename 配置对应的名称
      // 由于存在相同的文件名+hash，暂时用wen文件路径做hash
      const component = import(`@/views/${view}`)
      if(!component.name && name) {
        component.name = name
      }
      return component
    }
  }
}