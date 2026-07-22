/**
 * @created by pan
 * @updated by helin3 2019-04-06
 * @description 入口文件：加载组件、初始化等
 */
import 'babel-polyfill'
import '@babel/polyfill' // 针对IE浏览器兼容
import Vue from 'vue'
import YuwpUI from 'yuwp-ui'
import App from './App'
import store from '@/store'
import router from '@/router'
import '@/utils' // 初始化uadp-utils
import '@/config/other/components' // 导入全局使用自定义组件
import '@/config/other/css' // 导入全局使用自定义组件
import '@/config/other/other' // 导入全局其他配置信息
import i18n from '@/locale' // Internationalization
import YuwpUIConfig from '@/config/components' //导入全局修改的组件参数
import '@/utils/yufp'
import { vueMixinButton } from '@/utils/mixin'
// import { formatFileSize} from '@/utils/filters'

const defaultSettings = require('@/config')
Vue.use(YuwpUI, YuwpUIConfig)

Vue.config.productionTip = false
Vue.mixin(vueMixinButton)

new Vue({
  el: defaultSettings.defaultRootId,
  router,
  store,
  i18n,
  render: h => h(App)
})
console.log(`${defaultSettings.title} ${defaultSettings.version} ${new Date()}`)
