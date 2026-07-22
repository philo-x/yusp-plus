/* eslint camelcase:0  */
/* eslint no-implicit-coercion:0  */
/* eslint arrow-body-style:0  */
/* eslint vars-on-top:0 */
/* eslint no-inner-declarations:0 */
import Mock from 'mockjs'

var selectList = [];
var count1 = 10;
for (var i = 0; i < count1; i++) {
  selectList.push(Mock.mock({
    key: '@id',
    value: '@ctitle(2, 4)'
  }));
}

var getSlectList = function () {
  return {
    count: selectList.length,
    data: selectList
  };
};

export default [
  {
    url: '/trade/example/select', // mock拦截路径
    type: 'get', // mock拦截方法
    response: getSlectList
  }
]
