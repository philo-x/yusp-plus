import { request } from 'uadp-utils'

export function saveData(data) {
  return request({
    url: '/trade/example/save',
    method: 'post',
    data
  })
}

export function deleteData(data) {
  return request({
    url: '/trade/example/delete',
    method: 'post',
    data
  })
}

export function deleteDataDelete(data) {
  return request({
    url: '/trade/example/delete',
    method: 'delete',
    data
  })
}

export function testInterface(data) {
  return request({
    url: '/trade/example/test',
    method: 'post',
    data
  })
}