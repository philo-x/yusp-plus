/**
 * @created by pan
 * @updated by helin3 2019-04-09
 * @description 多语言配置国际化引用入口
 * TODO: 懒加载及热重载
 */
import Vue from 'vue'
import VueI18n from 'vue-i18n'

import { getLanguage } from '@/utils/i18n'
import messages from './messages'

Vue.use(VueI18n)
// 实例化vue i18n
const i18n = new VueI18n({
  // options: zh_CN | zh_TW | en
  locale: getLanguage(),
  // set locale messages
  messages
})
/**
 * @description 单信息翻译
 * @param {String} key 关键字
 * @param {Object} options 参数
 */
export function getI18nMessage(key, options){
  return i18n.t(key, options)
}
export default i18n
