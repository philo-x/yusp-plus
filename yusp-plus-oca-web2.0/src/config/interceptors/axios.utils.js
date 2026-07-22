/**
 * @description 用于axios重发处理(token失效)
 */
import axios from 'axios'
import store from '@/store'
import { logger } from '@/utils'
import { resHeader } from '../index'
import { showMessage, showMessageAlert } from '@/utils/util'
const SUCCESS_CODE = ['undefined', '20000', '200', '0000', '0', '20100003', '30100022', '30100026', '30100024', '30100034', '30100029', '30100035', '10100001', "00000000", "10000002", '10100001'];

const mockMode = process.env.VUE_APP_MOCK_MODE === 'true' // 模拟模式，true真实express服务，false XHR拦截方式
let __requests__ = []; // 等待请求列表
let __isRouterRefreshToken__ = false; // 是否在刷新路由token

/**
 * @description 判断状态码是否成功
 * @param {String|NUmber} code 状态码
 * @returns 返回状态码是否在列表中
 */
export function isSuccess(code) {
  // 将状态码转换成字符串
  for (let i = 0, c = code + ''; i < SUCCESS_CODE.length; i++) {
    if (c === SUCCESS_CODE[i]) {
      return true
    }
  }
  return false
}
/**
 * @description 添加请求
 * @param {Function} fn 回调函数
 */
export function pushRequest(fn) {
  __requests__.push(fn)
}
/**
 * @description 获取请求数量
 * @returns 返回没有发送的请求的数量
 */
export function getRequestLength() {
  return __requests__.length;
}
/**
 * @description 清空所有请求
 */
export function clearRequest() {
  __requests__ = []
}
/**
 * @description 触发回调函数
 * @param {String} param 回调函数参数
 */
export function fireRequest(param) {
  if (__requests__.length > 0) {
    __requests__.forEach(cb => cb(param));
    clearRequest(); // 执行完成后，清空队列
  }
}
/**
 * 
 * @param {Boolean} status 设置请求状态
 */
export function setTokenStatus(status) {
  __isRouterRefreshToken__ = status
}
/**
 * @description 获取token状态
 * @returns 返回当前请求状态
 */
export function getTokenStatus() {
  return __isRouterRefreshToken__
}
/**
 * @description 取消当前请求
 * @param {Object} config 请求参数配置
 * @returns 返回当前请求状态
 */
export function cancelRequest(config) {
  // 若token不存在，表示会话过期，终止请求
  const source = axios.CancelToken.source();
  config['concelToken'] = source.token;
  config['stop'] = source;
  source.cancel(`Session expiration request termination ${config.url}`)
  return false
}
/**
 * 解析接口返回报文头
 * @param {Object} header 报文头信息
 * @return {Boolean} true|false 解析结果状态
 */
export function analysisResponseHeader(header) {
  // 解析报文头
  let xyHead = header[resHeader]
  // 为空时不处理后续逻辑
  if(!resHeader) return true
  try {
    xyHead = xyHead ? JSON.parse(xyHead) : {}
  } catch (e) {
    logger.error(`解析HttpReponseHeader ${resHeader}错误`)
    xyHead = {}
    return false
  }
  // 流水序列号
  const seqNo = xyHead.seqNo
  let seqNoInfo = ''
  if (seqNo !== undefined && seqNo !== '') {
    seqNoInfo = '\n【' + seqNo + '】'
  }
  const retStatus = xyHead.retStatus
  // S-交易成功；F-交易失败；O-交易授权；C-交易确认；B-授权+确认；X-交易状态未知。
  switch (retStatus) {
    case 'S':
    case 'F':
    case 'X':
      const retArray = xyHead.retArray || [];
      const errMsg = []
      let errFlag = false
      let logoutFlag = false
      for (let i = 0, len = retArray.length; i < len; i++) {
        const ret = retArray[i]
        // 非000000，即交易出现异常
        if (ret.retCode !== '000000') {
          errFlag = true
          errMsg.push('【' + ret.retCode + '】' + decodeURIComponent(ret.retMsg))
        }
        if (ret.retCode === 'UA0001') {
          logoutFlag = true
        }
      }
      if (errFlag) {
        // event.code = 1
        // event.message = clone(retArray, {})
        if (logoutFlag) {
          // TODO 调用退出登录
          store.dispatch('oauth/logout').then(() => {
            window.location.reload() // 为了重新实例化vue-router对象 避免bug
          })
        } else {
          seqNoInfo ? errMsg.push(seqNoInfo) : ''
          showMessageAlert(errMsg.join('\n'))
        }
        return false
      }
      break
    case 'B':
    case 'O':
    case 'C':
      // B C O 不做任何处理，直接返回回调，由业务功能自行处理
      break
    default:
      // mock为true时，不直接返回，因为mock无法模拟HttpResponseHeader--xyHead
      if (!mockMode) {
        showMessage(`【X】响应报文头无${resHeader}参数!${seqNoInfo}`)
        return false
      }
      break
  }
  return true
}