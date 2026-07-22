/**
 * @created by helin3 2019-04-09
 * @updated by
 * @description 多语言配置自动导入
 */

// https://webpack.js.org/guides/dependency-management/#requirecontext
// 注：此处默认只检索语言文件夹下的一级文件，不包含子目录文件
const zhCnFiles = require.context('./zh_CN', false, /\.js$/)
const zhTwFiles = require.context('./zh_TW', false, /\.js$/)
const enFiles = require.context('./en', false, /\.js$/)
/**
 * 解析语言包，自动将对应语言下的所有多语言对象整合至一起
 * @param {*} localeFiles
 */
const mergeMessage = (localeFiles) => {
  // you do not need `import app from './modules/app'`
  // it will auto require all vuex module from modules file
  const modules = localeFiles.keys().reduce((modules, modulePath) => {
    // set './app.js' => 'app'
    // const moduleName = modulePath.replace(/^\.\/(.*)\.\w+$/, '$1')
    // modules[moduleName] = value.default
    const value = localeFiles(modulePath)
    modules = { ...modules, ...value.default }
    return modules
  }, {})
  return modules
}

const zhCnLocale = mergeMessage(zhCnFiles)
const zhTwLocale = mergeMessage(zhTwFiles)
const enLocale = mergeMessage(enFiles)

// 整体合并后的语言包，暂且未考虑懒加载情况
const messages = {
  'zh_CN': {
    ...zhCnLocale
  },
  'zh_TW': {
    ...zhTwLocale
  },
  en: {
    ...enLocale
  }
}
export default messages
