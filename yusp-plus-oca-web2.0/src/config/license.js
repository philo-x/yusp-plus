import { showMessage, showMessageAlert } from '@/utils/util'
const lic = require('../../public/static/license.json')
export default {
  data: lic,
  remCallback: (msg) => {
    showMessage(msg.message, msg.type, msg.duration)
  }, // 许可证失效后回调函数
  remDay: 15 // 许可证过期提前提示，默认15天提示
}
