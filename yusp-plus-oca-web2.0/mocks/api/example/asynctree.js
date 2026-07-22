/* eslint camelcase:0  */
/* eslint no-implicit-coercion:0  */
/* eslint arrow-body-style:0  */
/* eslint vars-on-top:0 */
/* eslint no-inner-declarations:0 */
import orgTreeData from './data/orgTreeData'

function paramUrl2Obj (url) {
  var search = url.split('?')[1];
  if (!search) {
    return {};
  }
  return JSON.parse('{"' + decodeURIComponent(search).replace(/"/g, '\\"').replace(/&/g, '","').replace(/\=/g, '":"').replace(/\n/g, '\\n') + '"}');
}

function getTreeAsync (config) {
  var reqData = paramUrl2Obj(config.url);
  var unitid = reqData.UNITID;
  var superUnitId = reqData.SUPERUNITID || reqData.UNITID;
  var treeList = orgTreeData.data.filter(function (item) {
    if (!superUnitId && item.UNITID == unitid) {
      // 加载根
      return true;
    } else if (item.SUPERUNITID == superUnitId) {
      return true;
    } else {
      return false;
    }
  });
  return {
    total: treeList.length,
    data: treeList
  };
};

export default [
  {
    url: '/trade/example/asynctree', // mock拦截路径
    type: 'get', // mock拦截方法
    response: getTreeAsync
  }
]
