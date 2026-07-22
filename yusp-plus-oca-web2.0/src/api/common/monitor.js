
/**
 * 前端数据采集上报接口
 */
import { request } from 'uadp-utils'
import backend from '@/config/constant/app.data.service'
import { extend } from '@/utils'
/* 监控数据上传 */
const monitorList = [];

export function getprduelemlist(data) {
  return request({
    url: backend.prduUseService + '/prdueleminfo/q/getprduelemlist',
    params: data
  })
}
/**
 * 监控数据上传
 */
export function monitorLogUpload(data) {
  return request({
    url: backend.msmFrontWebService + '/api/monitor/webmonitordata/',
    method: 'post',
    data
  })
  // monitorList = monitorList.concat(data.data);
  // console.log('监控数据', monitorList);
  // return new Promise((resolve, reject) => {
  //   resolve({code: 0, message: '上传成功!'});
  // })
}

/**
 * 按钮日志上传
 */
export function auditlogdata(data) {
  const baseInfo = {
    userId: yufp.session.userId || '',
    userName: yufp.session.userName || '',
    orgId: yufp.session.org ? yufp.session.org.code : '',
    orgName: yufp.session.org ? yufp.session.org.name : '',
  }
  
  return request({
    url: backend.appOcaService + '/api/monitor/auditlogdata',
    method: 'post',
    data: extend(data, baseInfo)
  })
}