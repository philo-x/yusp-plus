/**
 * @created by helin3 2019-04-06
 * @updated by
 * @description 认证相关api模块：登录、注销、获取权限（用户、菜单）
 */
import {request} from 'uadp-utils'
import backend from '@/config/constant/app.data.service'

/**
 * 登录API调用
 * @param {*} data
 */
export function loginFn(data) {
  return request({
    url: backend.uaaService + '/oauth2/token',
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
      'Authorization': 'Basic WVVTUDoxMjM0NTY='
    },
    needToken: false,
    data
  })
}

export function replaySeqFn(data = 'seq') {
  return request({
    url: backend.uaaService + '/api/util/getReplaySeq',
    method: 'post',
    needToken: false,
    data
  })
}
/**
 * 刷新Token API调用
 * @param {*} data
 */
export function refreshTokenFn(data) {
  /* eslint camelcase:0 */
  return loginFn({
    grant_type: 'refresh_token',
    refresh_token: data
  })
}

/**
 * 退出登录/注销API调用
 * @param {*} data
 */
export function logoutFn(data) {
  return request({
    url: backend.uaaService + '/api/logout',
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
    },
    data
  })
}

/**
 * 用户会话信息获取API
 */
export function getSessionInfoFn() {
  return request({
    url: backend.appOcaService + '/api/session/info',
    method: 'post'
  })
}

/**
 * 菜单&控制点获取API
 * @param {*} params
 */
export function getMenuandcontrFn(params) {
  return request({
    url: backend.appOcaService + '/api/account/menuandcontr',
    method: 'post',
    params
  })
}

/**
 * 数据权限信息获取API
 * @param {*} params
 */
export function datacontrFn(params) {
  return request({
    url: backend.appOcaService + '/api/account/datacontr',
    method: 'POST',
    params
  })
}

/**
 * 密码信息获取API
 * @param {*} params
 */
export function passwordFn(params) {
  return request({
    url: backend.appOcaService + '/api/account/password',
    method: 'POST',
    params
  })
}

/**
 * 控制点URL获取API
 * @param {*} params
 */
export function contrUrlFn(params) {
  return request({
    url: backend.appOcaService + '/api/contr/url',
    method: 'POST',
    params
  })
}

/**
 * 获取验证码
 * @param {*} params
 */
export function getVerifyCodeImage(params) {
  return request({
    url: backend.appOcaService + '/api/codeImage/verifyCodeImage',
    method: 'POST',
    params
  })
}

/**
 * 重置密码
 */
export function resetPwd(data) {
  return request({
    url: backend.appOcaService + '/api/password/passwordmodification',
    method: 'POST',
    data
  })
}

/**
 * 获取机构树数据
 */
export function getOrgTree() {
  return request({
    url: backend.appOcaService + '/api/adminsmorg/treequeryauth', // 机构树请求 url
    method: 'POST',
    // data
  })
}
