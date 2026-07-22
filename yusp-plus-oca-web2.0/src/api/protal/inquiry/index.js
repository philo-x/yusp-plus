import { request } from 'uadp-utils'
import backend from '@/config/constant/app.data.service'

export function postGetFlowParam(calcuData) {
  return request({
    url: backend.ehouseLoan + '/wfservice/q/flow/getFlowParam',
    method: 'POST',
    data: calcuData
  })
}
export function getLpCrdtAppFetById(data) {
  return request({
    url: backend.ehouseLoan + '/inquiry/q/lpCrdtAppFet/getLpCrdtAppFetById',
    param: data
  })
}
export function qrylpaplyinfo(data) {
  return request({
    url: backend.loanPrcdSprt + '/apltInfo/q/qrylpaplyinfo',
    param: data
  })
}

export function uptCrdtAppFetBaseLessInfo(model) {
  return request({
    url: backend.ehouseLoan + '/inquiry/n/lpCptlReglFet/uptCrdtAppFetBaseLessInfo',
    method: 'POST',
    data: model
  })
}

export function perminfo(data) {
  return request({
    url: backend.loanPrcdSprt + '/RiskDeteBusProcessing/prs/riskdetepermResources/q/perminfo',
    method: 'POST',
    data: data
  })
}

export function rulrslt(comitData) {
  return request({
    url: backend.loanPrcdSprt + '/RiskDeteBusProcessing/prs/riskdetersltResources/rulrslt',
    method: 'POST',
    data: comitData
  })
}

export function submitNode(data) {
  return request({
    url: backend.ehouseLoan + '/workflow/n/flow/submitNode', // 流程编排,
    method: 'POST',
    data: data
  })
}

export function create(data) {
  return request({
    url: backend.wfiRecord + '/wfiaprvrcrd/i/create',
    method: 'POST',
    data: data
  })
}

export function qryParamRcrd(data) {
  return request({
    url: backend.loanPrcdSprt + '/sealMgr/q/adminParamRcrd/qryParamRcrd',
    param: data
  })
}

export function dtl(data) {
  return request({
    url: backend.appDebugServices + '/provider/q/org/dtl',
    param: data
  })
}
