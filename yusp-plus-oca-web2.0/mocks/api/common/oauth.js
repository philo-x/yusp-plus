/**
 * @created by helin3 2019-04-06
 * @updated by
 * @description 通用认证权限Mock模拟
 */
/* eslint camelcase:0 */
import Mock from 'mockjs'
import backend from '../../../src/config/constant/app.data.service'
import demoLookups from './data/data.lookup'
import menusAndContrls from './data/menus-contrls'

const { demoMenus, demoContrls } = menusAndContrls

/**
 * oauth认证获取/刷新token
 * @param {*} config 请求参数:
 * 1) post请求通过config.body
 * 2) get请求通过config.query
 */
const loginFn = config => {
  let returnObj = {}
  const suffix = new Date().getTime()
  // 获取Token、刷新Token请求成功返回相关token信息
  const tempReturnObj = {
    access_token: 'Basicd2ViX2FwcDo=' + suffix,
    expires_in: 3600, // 单位：秒
    refresh_token: 'eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9' + suffix
  }

  try {
    const data = config.body
    if (data.grant_type === 'refresh_token') {
      // 刷新token请求认证成功，返回相关token信息
      returnObj = tempReturnObj
      // 此处未模拟refresh_token失效情况，真实情况，请根据真实后端情况判断
    } else if (data.username !== 'admin' || data.password !== 'admin') {
      // 获取token请求认证失败，返回错误信息
      returnObj = {
        code: '9001',
        message: '用户名或密码错误，请重新输入!'
      }
    } else {
      // 获取token请求认证成功，返回相关token信息
      returnObj = tempReturnObj
    }
  } catch (e) {
    // 获取token认证请求、刷新token认证请求失败
    returnObj.code = '-1'
    returnObj.message = '系统错误，请联系系统管理员!'
  }
  return returnObj
}

/**
 * 模拟注销会话
 */
const logoutFn = config => {
  return {
    code: 0,
    message: 'logout success!'
  }
}

/**
 * 模拟获取用户会话信息
 */
const sessionInfoFn = config => {
  return {
    userId: '40',
    userName: '小宇',
    userAvatar: '',
    loginCode: 'admin',
    loginTime: null,
    roles: [
      { id: '100', code: 'R001', name: '系统管理员' },
      { id: 'modelAdmin', code: 'modelAdmin', name: '模型管理员' },
      { id: 'cstManager', code: 'cstManager', name: '客户经理' }
    ],
    dpt: { id: '010', code: '011', name: '运营管委会' },
    org: { id: '500', code: '500', name: '宇信集团' },
    logicSys: { id: '203', code: '', name: '' },
    instuOrg: null,
    upOrg: null,
    upDpt: null,
    dataContr: []
  }
}

/**
 * 菜单和权限获取
 */
const menuandcontrFn = config => {
  return {
    menu: demoMenus,
    contr: demoContrls
  }
}

/**
 * 字典获取
 * @param lookupCodes 字典类型参数，支持逗号分隔
 */
const lookupQueryFn = config => {
  let returnObj = {}
  const code = config.query.lookupCodes
  if (!code) {
    returnObj = {
      code: -1,
      message: '请求参数错误'
    }
  } else {
    const codeArr = code.split(',')
    for (let i = 0, len = codeArr.length; i < len; i++) {
      const codeType = codeArr[i]
      returnObj[codeType] = demoLookups[codeType] || []
    }
  }
  return {
    data: returnObj
  }
}

/**
 * 导出服务，{url,type,response}
 */
export default [
  {
    url: backend.uaaService + '/oauth2/token',
    type: 'post',
    response: loginFn
  },
  {
    url: backend.uaaService + '/api/logout',
    type: 'post',
    response: logoutFn
  },
  {
    url: backend.appOcaService + '/api/session/info',
    type: 'get',
    response: sessionInfoFn
  },
  {
    url: backend.appOcaService + '/api/account/menuandcontr',
    type: 'get',
    response: menuandcontrFn
  },
  {
    url: backend.appOcaService + '/api/codeImage/verifyCodeImage',
    type: 'get',
    response: () => Mock.Random.image('320x140', '#FFF', '#000', Mock.mock({ 'regexp': /\w{4}/ }).regexp)
  },
  {
    url: backend.appCommonService + '/api/adminsmlookupitem/weblist',
    type: 'get',
    response: lookupQueryFn
  }
]
