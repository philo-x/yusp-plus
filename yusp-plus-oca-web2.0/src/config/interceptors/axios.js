/* eslint camelcase: 0 */
import store from '@/store'
// import { sessionStore } from '@/utils'
import { getToken, isRefreshToken, refreshToken } from '@/utils/oauth'
import { X_AUTHORIZATION } from '@/config/constant/app.data.common'
import { getBaseUrl, showMessage } from '@/utils/util'
import { uuid } from '@/utils'
// import { refreshTokenFn } from '@/api/common/oauth'
import { requestLoading, resHeader } from '../index'
import { MessageBox } from 'yuwp-ui'
import { getLanguage } from '@/utils/i18n'
import { analysisResponseHeader, cancelRequest, isSuccess, pushRequest } from '@/config/interceptors/axios.utils.js'
const mockEnable = process.env.VUE_APP_MOCK_ENABLE === 'true'; // 模拟模式，true真实express服务，false XHR拦截方式
const mockMode = process.env.VUE_APP_MOCK_MODE === 'true'; // 模拟模式，true真实express服务，false XHR拦截方式
const qs = require('qs');
//不需转换报文的请求路径
const doesNotNeedTransFormURLArr = ['/oauth2/token', '/api/logout']
/* const doesNotNeedTransFormURLArr = ['/oauth/token', '/api/session/info', '/api/account/menuandcontr'] */
/**
 * 配置信息
 * config: {
 *  url: '/user', // 是将用于请求的服务器URL
 *  method: 'post', //是发出请求时使用的请求方法,默认post
 *  baseURL: 'http://127.0.0.1:8012/api/', // 将被添加到`url`前面，除非`url`是绝对的。 // 可以方便地为 axios 的实例设置`baseURL`，以便将相对 URL 传递给该实例的方法。
 *  headers: {'X-Requested-With': 'XMLHttpRequest'}, // 是要发送的自定义 headers
 *  params: {
 *    ID: 12345
 *  }, // 是要与请求一起发送的URL参数 // 必须是纯对象或URLSearchParams对象 // 必须是纯对象或URLSearchParams对象
 *  data: {firstName: 'Fred'}, // 是要作为请求主体发送的数据
 *  timeout: 1000, // 指定请求超时之前的毫秒数。 // 如果请求的时间超过'timeout'，请求将被中止
 *  withCredentials: false, // 指示是否跨站点访问控制请求,默认值:true
 *  responseType: 'json', // 表示服务器将响应的数据类型,默认值：json,包括 'arraybuffer', 'blob', 'document', 'json', 'text', 'stream'
 *  maxContentLength: 2000, // 定义允许的http响应内容的最大大小
 *  // 这将设置一个`Proxy-Authorization` header，覆盖任何使用`headers`设置的现有的`Proxy-Authorization` 自定义 headers。
 *  proxy: {
 *    host: '127.0.0.1',
 *    port: 9000,
 *    auth: {
 *      username: 'mikeymike',
 *      password: 'rapunz3l'
 *    }
 *  }
 */
export const requestConfig = {
  // retry: false, // 失败后是否重试，数字时表示重试次数
  // retryDelay: 0, // 失败后重试时间
  // isHeader: false, // 是否解析返回头
  needToken: true, // 是否传认证Token值去后台
  // isCrypto: false, // 是否加密，默认均加密
  isRepeat: false, // 是否允许接口重复请求，默认不允许
  // method: 'POST', // 默认POST，支持4种访问类型 GET/POST/PUT/DELETE
  // async: true, // 异步请求
  data: {}, // 请求数据
  headers: {
    'Accept-Language': getLanguage() === 'zh_CN' ? 'zh-CN' : 'en-US'
  }, // 后端实现多语言 前端配合传递标识参数
  // headers: { // http请求头
  //   'Content-Type': 'application/json; charset=UTF-8'
  //   // 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
  // },
  // dataType: 'json', // 默认返回数据类型
  baseURL: getBaseUrl() // api 的 base_url
  // baseURL: '' // api 的 base_url
};
/**
 * 请求前置处理函数
 * @param {Object} config 配置参数
 * @return {Object} 将配置参数处理后,返回调用参数
 */
export function requestSuccessFunc(config) {
  const isLogin = doesNotNeedTransFormURLArr.indexOf(config.url) !== -1;
  // isdebug && console.warn('【request-interceptor】', `url: ${config.url}`, config)
  // 自定义请求拦截逻辑，可以处理权限，请求发送监控等
  // Do something before request is sent
  // 展示loading
  let option;
  // config.withCredentials = false; // 请求头中不携带cookie
  const target = (config.loadingUi && config.loadingUi.target) || (config.loadingUi && config.loadingUi.option.target) || requestLoading.target;
  if ((config.loadingUi && !config.loadingUi.show) || requestLoading.show) {
    if (target) {
      option = {
        target: target, // Loading 需要覆盖的 DOM 节点
        body: true // 遮罩是否插入至 DOM 中的 body 中，true: 插入，false: 不插入，
      };
    } else {
      option = {
        fullscreen: true, // 遮罩是否全屏
        lock: true
      };
    }
    // 显示loading
    // 每个请求拥有一个loading实例
    // config.instance = Loading.service(option);
  }
  if (requestConfig.needToken === true && config.needToken === undefined) {
    config.needToken = requestConfig.needToken
  }
  // 如有有全局配置timeout参数，且请求本身没有传timeout就将全局参数timeout设置给config
  if (requestConfig.timeout && !config.timeout) {
    config.timeout = requestConfig.timeout;
  }
  // 传递参数转换
  if (config.method.toLowerCase() === 'post') {

    if (!config.data && config.params) {
      config.data = config.params
    }
    if(config.url.indexOf('/api/file/provider/richedituploadfile') > -1 || config.url.indexOf('/api/file/provider/uploadfile') > -1) {

    } else if (!mockEnable && !isLogin) {
      config.data = {
        body: JSON.parse(JSON.stringify(config.data || {})),
        head: {
          transName: "String",
          rocket: "String",
          pageId: "String",
          reqJnls: "String",
        }
      }

    }
    //小U留痕特殊处理
    if (config.url.includes('utrace')) {
      config.data.body = JSON.parse(config.data.body);
    }
  } else if (config.method.toLowerCase() === 'get') {
    if (!config.params && config.data) {
      config.params = config.data
    }

    // 解决get请求中有中文问题
    if (config.params) {
      if (typeof config.params === 'object') {
        for (var item in config.params) {
          config.params[item] = encodeURI(config.params[item]);
        }
      } else {
        config.params = encodeURI(config.params);
      }
    }
    // 解决get请求中数组参数显示默认为ids[]=1&ids[]=2&ids[]=3，转换为ids=1&ids=2&id=3格式
    config.paramsSerializer = (params) => qs.stringify(params, { indices: false });
  }
  const contentType = config.headers['Content-Type'];
  // 修改axios post请求默认headers值
  config.headers.post['Content-Type'] = 'application/json; charset=UTF-8';
  if (contentType && contentType.indexOf('application/x-www-form-urlencoded') > -1) {
    config.data = qs.stringify(config.data || {})
  }
  if (requestConfig.baseURL) {
    config.baseURL = requestConfig.baseURL
  }
  config.headers['Accept-Language'] = requestConfig.headers['Accept-Language'];

  // 对于默认没有传递，登录相关请求都是有传递该值
  if (config.needToken && !config.headers[X_AUTHORIZATION]) {
    // 让每个请求携带token-- ['Authorization']为自定义key 请根据实际情况自行修改
    const _refreshToken = isRefreshToken();
    if (_refreshToken === true) {
      refreshToken();
      return new Promise((resolve) => {
        pushRequest((access_token) => {
          config.headers[X_AUTHORIZATION] = 'Bearer ' + access_token;
          resolve(config);
        })
      })
    }
    const token = getToken();
    if (token) {
      config.headers[X_AUTHORIZATION] = 'Bearer ' + token.access_token;
    } else {
      // 若token不存在，表示会话过期，终止请求
      return cancelRequest(config);
    }
  }
  // TODO 将数据根据请求类型，转换params 和 data参数
  return config
}

/**
 * 请求前置失败处理函数
 * @param {Object} config 配置参数
 * @return {Promise} 将配置参数处理后,返回调用参数
 */
export function requestFailFunc(error) {
  // 自定义发送请求失败逻辑，断网，请求发送监控等
  return Promise.reject(error);
}

/**
 * 请求成功处理函数
 * @param {Object} response 返回对象
 * response: {
 *  config: {url: "adminsmlookupitem/weblist", method: "get", params: {…}, headers: {…}, baseURL: "", …}
 *  data: {data: {…}}
 *  headers: {date: "Tue, 03 Sep 2019 06:57:43 GMT", etag: "W/"95-n5AxojPuAQKqPigfmnzv5Sd505A"", x-mock-api: "xyMock", connection: "keep-alive", x-powered-by: "Express", …}
 *  request: XMLHttpRequest {onreadystatechange: ƒ, readyState: 4, timeout: 60000, withCredentials: true, upload: XMLHttpRequestUpload, …}
 *  status: 200
 *  statusText: "OK"
 * }
 */
export function responseSuccessFunc(res) {
  // 自定义响应成功逻辑，全局拦截接口，根据不同业务做不同处理，响应成功监控等
  // const resData = res.data
  let resData = null;
  const isLogin = doesNotNeedTransFormURLArr.indexOf(res.config.url) !== -1;
  if (!mockEnable && !isLogin) {
    resData = {
      code: res.data.head.retCode,
      data: res.data.body.records ? res.data.body.records : res.data.body,
      message: res.data.head.retMsg,
      total: res.data.body.total,
      head: res.data.head
    }
  } else {
    resData = res.data
  }
  // 加延迟才可观察到loading的效果
  setTimeout(function () {
    // 关闭loading
    res && res.config.instance && res.config.instance.close();
  }, 100);
  // 解析报文头信息 // 不需要解析通用报文头
  // if (resHeader && res.headers && !analysisResponseHeader(res.headers)) {
  //   return Promise.reject(`解析HttpReponseHeader ${JSON.stringify(resHeader)}错误`)
  // }
  // 配置拦截信息,解析报文头
  // api/xxljobindex/triggerchartdate 接口返回code='200'
  const code = String(resData.code);
  if (resData && !isSuccess(code)) {
    // 50008:非法的token; 50012:其他客户端登录了;  50014:Token 过期了;
    if (code === '50008' || code === '50012' || code === '50014' || code === '10000001') {
      // 请自行在引入 MessageBox
      // import { Message, MessageBox } from 'element-ui'
      MessageBox.confirm('你已被登出，可以取消继续留在该页面，或者重新登录', '确定登出', {
        confirmButtonText: '重新登录',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        store.dispatch('oauth/resetToken').then(() => {
          window.location.reload() // 为了重新实例化vue-router对象 避免bug
        })
      })
    } else {
      showMessage(resData.message, 'error')
    }
    return Promise.reject(res).catch(() => {
      // 关闭loading
      res && res.config && res.config.loadingUi && res.config.loadingUi.option.loadingInstance.close();
    })
  } else {
    return resData
  }
}

/**
 * 请求失败处理函数
 * @param {Object} error 错误信息
 */
// todo 暂时配合后端调整码值，还需要标准化
// var strategyCode = ['10000001', '10000003', '10400000', '10000005', '10000006', '10000007', '10300005', '10300004', '10200005', '10300006', '10100020', '10300002', '10100021', '10100022', '10100023', '10100024', '10300007', '10000004']; //登录认证策略错误码，理论上只有登录页会拿到这些错误码，so交给登录页处理
export function responseFailFunc(error) {
  const getWayEorrCode = ['80000007', '90000001', '90000002', '90000003']; // 网关401错误接口，80000007错误代码是由uaa抛出
  // 响应失败，可根据 responseError.message 和 responseError.response.status 来做监控处理
  const res = error && error.response;
  // 错误信息中无其他数据信息，直接打印错误
  if (!res) {
    return Promise.reject(error)
  }
  const status = res.status;
  // 对返回状态进行处理
  switch (status) {
    case 400:
      if (getWayEorrCode.indexOf(res.data.code) > -1) {
        const errorMsg = getLanguage() === 'zh_CN' ? '会话已过期，请重新登录' : 'Session expired, please login again';
        showMessage(errorMsg, 'error');
        setTimeout(function () {
          store.dispatch('oauth/logout', true).then(() => {
            window.location.reload() // 为了重新实例化vue-router对象 避免bug
          });
        }, 200);
        break;
      }
      showMessage('请求参数错误[400]');
      break;
    case 401:
      // if (strategyCode.indexOf(res.data.code) > -1) {
      //   break;
      // }
      // 不是网关的，就是登录页的401，在登录页面做处理
      if (res.data.code && getWayEorrCode.indexOf(res.data.code) < 0) {
        break;
      }

      // const errorMsg = getLanguage() === 'zh_CN' ? '会话已过期，请重新登录' : 'Session expired, please login again';
      if (!res.data.code || res.data.code === getWayEorrCode[0]) {
        showMessage(getLanguage() === 'zh_CN' ? '会话已过期，请重新登录' : 'Session expired, please login again', 'error', 30);
      }

      if (res.data.code === getWayEorrCode[1]) {
        showMessage(getLanguage() === 'zh_CN' ? '无权访问，请提供 Access Token' : 'No access, please provide access token', 'error', 30);
      }

      if (res.data.code === getWayEorrCode[2]) {
        showMessage(getLanguage() === 'zh_CN' ? '无权访问，没有从redis中获取到token' : 'No access, No token was obtained from redis', 'error', 30);
      }

      setTimeout(function () {
        store.dispatch('oauth/logout', true).then(() => {
          window.location.reload() // 为了重新实例化vue-router对象 避免bug
        });
      }, 200);
      break;
    case 403:
      showMessage('您无权限访问，请联系系统管理员!');
      break;
    case 404:
      showMessage('请求错误[404]');
      break;
    case 402:
      if (process.env.VUE_APP_SINGLE_LOGING === 'true') {
        // 单点登录方式，重定向页面替换当前页面
        window.location.href = res.data.message;
      } else {
        // 做为认证服务，重定向页面打开新的标签页
        window.open(res.data.message);
      }
      break;
    default:
      showMessage('系统错误，请联系系统管理员!');
      break;
  }
  if (status === 401 && getWayEorrCode.indexOf(res.data.code) < 0) {
    return Promise.reject(error, res);
  } else {
    return Promise.reject(error, res).catch(err => {
      console.log(err);
      // 关闭loading
      res && res.config && res.config.loadingUi && res.config.loadingUi.option.loadingInstance.close();
    });
  }


  // TODO 下面代码为接口报错后重试
  // const config = res.config
  // // 判断是否配置了重试
  // if (!config || !config.retry) return Promise.reject(err)
  // if (!config.shouldRetry || typeof config.shouldRetry !== 'function') return Promise.reject(err)

  // // 判断是否满足重试条件
  // if (!config.shouldRetry(err)) return Promise.reject(err)

  // // 设置重试次数
  // config.__retryCount = config.__retryCount || 0
  // if (config.__retryCount >= config.retry) return Promise.reject(err)

  // // 重试次数自增
  // config.__retryCount += 1

  // // 延时处理
  // var backoff = new Promise(function (resolve) {
  //   setTimeout(function () {
  //       resolve()
  //   }, config.retryDelay || 1)
  // })
  // config.data = qs.parse(config.data)
  // // 重新发起axios请求
  // return backoff.then(() => {
  //     return AxiosInst(config)
  // })
}
