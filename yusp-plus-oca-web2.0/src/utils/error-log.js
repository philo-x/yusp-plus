import Vue from 'vue'
import store from '@/store'
import { isString, isArray } from '@/utils/validate'
import settings from '@/config'

// you can set in settings.js
// errorLog:'production' | ['production','development']
const { errorLog: needErrorLog } = settings

function checkNeed(arg) {
  const env = process.env.NODE_ENV
  if (isString(needErrorLog)) {
    return env === needErrorLog
  }
  if (isArray(needErrorLog)) {
    return needErrorLog.includes(env)
  }
  return false
}

if (checkNeed()) {
  Vue.config.errorHandler = function(err, vm, info, a) {
    // Don't ask me why I use _this.nextTick, it just a hack.
    // detail see https://forum.vuejs.org/t/dispatch-in-vue-config-errorhandler-has-some-problem/23500
    console.error('Vue.config.errorHandler', err, vm, info, a)
    Vue.nextTick(() => {
      store.dispatch('errorLog/addErrorLog', {
        err,
        vm,
        info,
        url: window.location.href
      })
    });
    if (yufp && yufp.monitor) {
      yufp.monitor.vueErrorHandler(err, vm, info);
    }
  }
  // Vue.config.warnHandler = function(err, vm, info, a) {
  //   console.log('警告监听', err, vm, info, a)
  // }
}
