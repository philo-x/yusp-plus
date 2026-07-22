import { List, dataList } from './data/data.utrace'

function paramUrl2Obj (url) {
  var search = url.split('?')[1];
  if (!search) {
    return {};
  }
  return JSON.parse('{"' + decodeURIComponent(search).replace(/"/g, '\\"').replace(/&/g, '","').replace(/\=/g, '":"').replace(/\n/g, '\\n') + '"}');
}

export default [
  {
    url: '/api/trade/example/utrace/list',
    type: 'get',
    response: (config) => {
      console.log('查询原始数据', config)
      return {
        code: 20000,
        data: dataList,
        total: dataList.length,
      }
    }
  },
  {
    url: '/api/smodifydemo/addSModifyDemo',
    type: 'post',
    response: (config) => {
      var reqData = JSON.parse(config.body);
      for (var i = 0, l = reqData.length; i < l; i++) {
        List.unshift(reqData[i]);
      }
      return { data: { message: '操作成功', flag: 'success' } };
    }
  },
  {
    url: '/api/utrace/selectSModifyTraceWithPage',
    type: 'POST',
    response: (config) => {
      // 模拟真实数据需要针对参数进行数据过滤
      var reqData = paramUrl2Obj(config.url);
      var condition = JSON.parse(reqData.condition);
      var mPkV = condition.mPkV;
      var mFieldId = condition.mFieldId;
      console.log('查询小U列表数据', mPkV, mFieldId);
      return { data: List, total: List.length };
    }
  },
  {
    url: '/api/utrace/selectSModifyTrace',
    type: 'POST',
    response: (config) => {
      return {
        code: 20000,
        total: List.length,
        data: List
      }
    }
  }
]