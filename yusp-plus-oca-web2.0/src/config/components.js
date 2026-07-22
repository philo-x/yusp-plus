/**
 * @description YUWPUI 组件全局配置
 * @created by weimei1
 * @updated by
 * @description 组件参数全局修改
 */
import locale from 'yuwp-ui/lib/locale/lang/zh-CN.js'
import { getBaseUrl } from '@/utils/util'
import { getI18nMessage } from "@/locale";
export default {
  i18n: function(key, options) {
    return getI18nMessage(key, options)
  },
  locale,
  xtable: {
    conditionKey: '' //适配后端新框架条件查询无conditon对象
  },
  xform: {
    labelSuffix: '', //适配UI样式,表单label和输入框之间无冒号
    removeEmpty: true // 移除表单查询为空的项
  },
  upload: {
    baseUrl: getBaseUrl()
  }
}

