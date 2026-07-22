/**
 * @created by kongqf
 * @description 公共组件-入口
 */
import Vue from 'vue';
import lookup from '@/config/lookup';
import license from '@/config/license';
import { requestConfig, requestSuccessFunc, requestFailFunc, responseSuccessFunc, responseFailFunc } from '@/config/interceptors/axios';
import XyUtils from 'uadp-utils';
import { IS_ENCODE, IS_NONCE } from '@/config/constant/app.data.common'
import backend from '@/config/constant/app.data.service';
import * as filters from '@/utils/filters'; // global filters
import service from '@/utils/yufp.ajax'

window.backend = backend; // 为方便兼容，直接将对象挂载到全局 // 因app.data.service在mock服务中引用，window对象不存在
// console.log('字典管理配置', [lookup, XyUtils])

// XHR 方式注册拦截 mock api
const mockEnable = process.env.VUE_APP_MOCK_ENABLE === 'true';
const mockMode = process.env.VUE_APP_MOCK_MODE === 'true'; // 模拟模式，true真实express服务，false XHR拦截方式

if (mockEnable && !mockMode) {
  import('@/../mocks').then(({ mockXHR }) => {
    mockXHR();
  })
}
// 接口加密全局配置
const KEY_ALGORITHM = 'SM' // 非对称加密算法,默认国密SM2, [SM/RSA], 密钥加密方式,对清请求头body-encode-key
const BODY_ALGORITHM = 'SM' // 对称加密算法,默认国密SM4, [SM/AES], 报文体加密方式
// 获取api请求加密公钥字符串
//RSA和AES公钥
//const PUBLIC_KEY = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJ7Uf/t5Wvyg5y4lj6UQvP2SzHFRbT2DT1X2SAmN1j4Z34X4fMZnYIy6aSm+lFra2UmVeow/qd8VGJwScPXp7EJuKAissJt46jziYKqFUcX/O6HI1ZRxKOgC+bfCdR+CF/tJMfKkMOKCMrp5Fy5sr9uoikFb5Wc3usq40UaTFjLwIDAQAB'
//国密公钥
const PUBLIC_KEY = '04c4d878364a40a6e6cb984bc876b453bbe7bac127e0d0f1adddb73e9ecf981b0ed8b7af1ddfa5e583cc439c942a4b7e8205dfcb0996ecd001dd047fb70a46e81d'

Vue.use(XyUtils, {
  lookup: lookup,
  license: license,
  request: {
    config: requestConfig,
    requestSuccessFunc,
    requestFailFunc,
    responseSuccessFunc,
    responseFailFunc,
    __KEY_ALGORITHM__: KEY_ALGORITHM,
    __BODY_ALGORITHM__: BODY_ALGORITHM,
    __PUBLIC_KEY__: PUBLIC_KEY,
    __IS_NONCE__: IS_NONCE,
    __IS_ENCODE__: IS_ENCODE,
    __iv__: '0000000000000000' // 该值为固定值，请勿修改
  }
});
// 接口服务名注册至Vue全局
Vue.prototype.$backend = backend;
Vue.prototype.$request = (param) => {
  // 为兼容旧组件同步方法使用 #TODO 其他方式禁止使用该方式，如果需要实现同步，可使用async/await
  if (param.async === false) {
    return service.request(param)
  } else {
    return XyUtils.request(param);
  }
}

// 全局注入一个date json格式化方法 // TODO 临时存放
/**
  * 日期默认格式
  * @returns {*}
  */
/* eslint no-extend-native: 0 */
Date.prototype.toJSON = function () {
  return XyUtils.dateFormat(this, '{y}-{m}-{d}');
};

// 注册全局工具类过滤器.
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key]);
});
/**
 * 递归触发事件
 * @param {String} eventName 事件名称
 * @param {Object} data 事件发送数据对象
 */
Vue.prototype.$dispatch = function (eventName, data) {
  let parent = this.$parent;
  while (parent) {
    parent.$emit(eventName, data);
    parent = parent.$parent;
  }
};
/**
 * 广播事件
 * @param {String} eventName 事件名称
 * @param {Object} data 事件发送数据对象
 */
Vue.prototype.$broadcase = function (eventName, data) {
  const broadcase = function () {
    this.$children.forEach((child) => {
      child.$emit(eventName, data);
      if (child.$children) {
        broadcase.call(child, eventName, data);
      }
    });
  };
  broadcase.call(this, eventName, data);
};
/**
 * 生成唯一识别号
 * @param {String} len 长度
 * @param {Object} radix
 */
Vue.prototype.genUUID = function (len, radix) {
  var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
  var uuid = [],
    i;
  radix = radix || chars.length;
  if (len) {
    for (i = 0; i < len; i++) {
      uuid[i] = chars[0 | Math.random() * radix];
    }
  } else {
    var r;

    uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
    uuid[14] = '4';

    for (i = 0; i < 36; i++) {
      if (!uuid[i]) {
        r = 0 | Math.random() * 16;
        uuid[i] = chars[i == 19 ? (r & 0x3) | 0x8 : r];
      }
    }
  }

  return uuid.join('');
};

export * from 'uadp-utils'; // 内部已经注册 $lookup、$utils，在 vue 中可以通过 vm.$lookup/vm.$utils 直接使用
