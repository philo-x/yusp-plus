/**
 * @created by kongqf
 * @description 样式导入
 */
import store from '@/store'
import '@/assets/common/normalize.css' // A modern alternative to CSS resets
// import '@/assets/styles/element-variables.scss'
// import '@/assets/styles/index.scss' // global css
import 'yuwp-ui/lib/theme-default/index.css' // global css
import '@/assets/common/iconfont/iconfont.css'
import '@/assets/common/icoFonts/icoFonts.css'
import '@/assets/common/base.css' // 基础样式
import '@/assets/common/common.css' // 基础样式
import '@/assets/common/compact.scss' // 紧凑模式样式
import '@/assets/common/common-ext.css' // 基础拓展样式
import '@/assets/common/common-add.css' // 基础样式追加样式
import '@/assets/common/oca.css' // oca 样板工程独有样式
import '@/assets/common/top-left.css' // 2.0 前端上左菜单模式独有主题样式
import '@/assets/common/common-en.css' // 针对英文样式单独调整少数区域字体大小
import { changeTheme } from '@/utils/util'
changeTheme(store.getters.theme, '')