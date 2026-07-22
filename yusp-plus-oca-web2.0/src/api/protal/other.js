import { request } from 'uadp-utils'
import backend from '@/config/constant/app.data.service'

export function getprduelemlist(data) {
  return request({
    url: backend.prduUseService + '/prdueleminfo/q/getprduelemlist',
    params: data
  })
}

/* 监控数据上传 */
let monitorList = [];
export function monitorLogUpload(data) {
  // return request({
  //   url: backend.prduUseService + '/api/monitor/upload',
  //   method: 'post',
  //   params: data
  // })
  monitorList = monitorList.concat(data.data);
  console.log('监控数据', monitorList);
  return new Promise((resolve, reject) => {
    resolve({code: 0, message: '上传成功!'});
  })
}