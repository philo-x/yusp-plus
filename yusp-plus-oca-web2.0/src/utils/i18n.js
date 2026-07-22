
/**
 * @created by helin3 2019-04-09
 * @updated by
 * @description i18n工具类
 */
/*eslint prefer-template:0 */
import { sessionStore } from 'uadp-utils' // 调用先后顺序问题，直接从uadp-utils包中获取
import messages from '@/locale/messages'
import { LANGUAGE } from '@/config/constant/app.data.common'

/**
 * 获取当前语言
 * 1) 首先获取历史设置的，若不存在，获取config.js中配置的，配置也没有，直接获取浏览器语言；
 * 2) 若还是不存在，则获取默认：zh_CN
 */
export function getLanguage() {
  const chooseLanguage = sessionStore.get(LANGUAGE)
  if (chooseLanguage) {
    return chooseLanguage
  }
  // if has not choose language
  const language = (navigator.language || navigator.browserLanguage).toLowerCase()
  const locales = Object.keys(messages)
  for (const locale of locales) {
    if (language.indexOf(locale) > -1) {
      return locale
    }
  }
  return 'zh_CN'
}

/**
 * 转换路由route.meta.title，在面包屑、侧边栏、页签
 * @param {*} title
 */
export function generateTitle(title) {
  const hasKey = this.$te('route.' + title)

  if (hasKey) {
    // $t :this method from vue-i18n, inject in @/locale/index.js,
    const translatedTitle = this.$t('route.' + title)

    return translatedTitle
  }
  return title
}
