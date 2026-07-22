/* eslint camelcase:0  */
/* eslint no-implicit-coercion:0  */
/* eslint arrow-body-style:0  */
/* eslint vars-on-top:0 */
/* eslint no-inner-declarations:0 */
import { getList } from './data/data.maintable'
export default [
  {
    url: '/example/template/msform', // mock拦截路径
    type: 'get', // mock拦截方法
    response: getList
  }
]
