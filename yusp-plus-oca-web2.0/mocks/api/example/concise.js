/* eslint camelcase:0  */
/* eslint no-implicit-coercion:0  */
/* eslint arrow-body-style:0  */
/* eslint vars-on-top:0 */
/* eslint no-inner-declarations:0 */
import { getList, save, deleteData, changeStatus } from './data/conciseTable'
export default [
  {
    url: '/example/concise/list', // mock拦截路径
    type: 'get', // mock拦截方法
    response: getList
  },
  {
    url: '/example/concise/save', // mock拦截路径
    type: 'post', // mock拦截方法
    response: save// 接口对应的响应
  },
  {
    url: '/example/concise/delete',
    type: 'post',
    response: deleteData
  },
  {
    url: '/example/concise/changeStatus',
    type: 'post',
    response: changeStatus
  }
]
