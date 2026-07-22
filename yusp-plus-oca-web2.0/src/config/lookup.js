import { lookupQueryFn } from '@/api/common/lookup'
import { getLanguage } from '@/utils/i18n'
// 静态字典数据
import lookupMgrZh from '@/config/constant/app.data.lookup'
import lookupMgrEs from '@/config/constant/app.data.lookup.es'
const lookupMgr = getLanguage() === 'zh_CN' ? lookupMgrZh : lookupMgrEs;

export default {
  lookupMgr: {
    ...lookupMgr
    // 其他字典模块
  },
  lookupQueryFn
  // remoteParamName: 'lookupCodes', // 远程参数名
  // codeKey: 'key', // 对应后台字段key
  // codeValue: 'value', // 对应后台字段value
  // limit: false, // 是否开启字典长度超过limitlength长度转存
  // limitLength: 100, // 字典长度超过100，直接存储于localstorage
  // prefix: 'YUFP-LIMIT-TYPE-' // 超长字典缓存存储前缀
}