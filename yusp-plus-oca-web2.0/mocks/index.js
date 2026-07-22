/**
 * @created by pan
 * @updated by helin3 2019-04-05
 * @description Mock数据模拟入口
 * 1) 开发模式，调用Mock入口见: /vue.config.js devServer.after
 * 2) 生产模式，调用Mock入口见: /src/main.js
 */
import Mock from 'mockjs'
import registers from './registers'
/*eslint camelcase:0 */
/*eslint no-div-regex:0 */
/*eslint prefer-template:0 */
/*eslint prefer-rest-params:0*/
/*eslint consistent-return:0*/

/**
 * URL 参数转对象
 * @param {String} url 带问号URL
 * eg. ?username=aleynhe&password=123456
 * eg.  username=aleynhe&password=123456
 * @returns {Object}
 */
const param2Object = url => {
  const search = url.split('?')[1]
  if (!search) {
    return {}
  }
  return JSON.parse('{"' +
    decodeURIComponent(search).replace(/"/g, '\\"').replace(/&/g, '","').replace(/=/g, '":"').replace(/\+/g, ' ') +
    '"}')
}
/**
 * 模拟响应数据，用于导出开发环境mock api响应模拟
 * 基于 express 服务模拟
 * @param {*} url
 * @param {*} type
 * @param {*} respond
 */
const responseFake = (url, type, respond) => {
  return {
    url: new RegExp(`${url}`),
    type: type || 'get',
    response(req, res) {
      res.json(Mock.mock(respond instanceof Function ? respond(req, res) : respond))
    }
  }
}

/**
 * 前端Mock模拟，请谨慎使用，它将重新定义xmlhttprequest，这将导致许多第三方库失效（如进度事件）。
 * 生产（测试/演示）环境，自动注册所有mock api
 * 调用入口见：src/main.js
 */
export function mockXHR() {
  // 修复在使用 MockJS 情况下，设置 withCredentials = true，且未被拦截的跨域请求丢失 Cookies 的问题
  // https://github.com/nuysoft/Mock/issues/300
  Mock.XHR.prototype.proxy_send = Mock.XHR.prototype.send
  Mock.XHR.prototype.send = function() {
    if (this.custom.xhr) {
      this.custom.xhr.withCredentials = this.withCredentials || false
      if (this.responseType) {
        this.custom.xhr.responseType = this.responseType
      }
    }
    this.proxy_send(...arguments)
  }

  /**
   * Mock XHR模式转Express响应，保证开发模式与生产模式，Mock api接口一致
   * @param {*} respond
   */
  function XHR2ExpressReqWrap(respond) {
    return function(options) {
      let result = null
      if (respond instanceof Function) {
        const { type, url } = options
        let { body } = options
        // 先按JSON字符串来转对象
        // 若出错，则按URL来转对象;若还是出错，则传递原参数值
        try {
          body = JSON.parse(body)
        } catch (e) {
          try {
            body = param2Object('?' + body)
          } catch (err) {
            window.console.error('Mocks XHR2ExpressReqWrap request data format error.')
            return
          }
        }

        // https://expressjs.com/en/4x/api.html#req
        result = respond({
          method: type,
          body: body,
          query: param2Object(url)
        })
      } else {
        result = respond
      }
      console.log('返回数据', result)
      return Mock.mock(result)
    }
  }
  // 循环注册mock api
  for (const i of registers) {
    Mock.mock(new RegExp(i.url), i.type || 'get', XHR2ExpressReqWrap(i.response))
  }
}

/**
 * 开发环境Mock api自动注册（通过express注册）
 * 详见：/mocks-server.js export
 */
export default registers.map(route => {
  let type = route.type || 'get'
  type = (`${type}`).toLowerCase()
  return responseFake(route.url, type, route.response)
})
