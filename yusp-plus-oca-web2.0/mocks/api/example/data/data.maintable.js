import Mock from 'mockjs'
var List = [];
var count = 15;
Mock.Random.increment(1000);
for (var i = 0; i < count; i++) {
  List.push(Mock.mock({
    id: '@increment(2)',
    title: '@ctitle(8, 15)',
    createAt: '@date',
    author: '@cname',
    auditor: '@cname',
    importance: '@integer(1, 3)',
    'type|1': ['CN', 'US', 'JP', 'EU'],
    'status|1': ['published', 'draft', 'deleted'],
    pageviews: '@integer(300, 5000)',
    remark: '@ctitle(15, 100)',
    EVALUATION_PERIOD_ID: '@date',
    'OPEN_FLAG|1': ['O', 'N', 'C'],
    'MEASURE|1': ['已', '未'],
    'LOCK|1': ['已', '未']
  }));
}

function paramUrl2Obj (url) {
  var search = url.split('?')[1];
  if (!search) {
    return {};
  }
  return JSON.parse('{"' + decodeURIComponent(search).replace(/"/g, '\\"').replace(/&/g, '","').replace(/\=/g, '":"').replace(/\n/g, '\\n') + '"}');
}

function paramBody2Obj (body) {
  if (!body) {
    return {};
  }
  return JSON.parse(body);
}

export function getList(config) {
  // var reqData = paramBody2Obj(config.body)
  var reqData = paramUrl2Obj(config.url);
  var page = reqData.page;
  var size = reqData.size;
  var precise = reqData.precise;
  var condition = reqData.condition ? JSON.parse(reqData.condition) : {};
  var content = condition.content || '';
  var sort = condition.sort;

  // 精确查询参数
  var author = content.author;
  var auditor = content.auditor;
  var num = content.num;
  var status = content.status;
  var department = content.department;
  var telephone = content.telephone;
  var loginTime = content.loginTime;

  var mockList = List.filter(function (item) {
    if (!precise) {
      return JSON.stringify(item).indexOf(content) !== -1;
    } else {
      if (author && item.createAt !== author) {
        return false;
      }
      if (auditor && item.auditor !== auditor) {
        return false;
      }
      if (num && item.num !== num) {
        return false;
      }
      if (status && item.status !== status) {
        return false;
      }
      if (department && item.department !== department) {
        return false;
      }
      if (telephone && item.telephone !== telephone) {
        return false;
      }
      if (loginTime && item.loginTime !== loginTime) {
        return false;
      }
      return true;
    }
  });
  if (sort === '-id') {
    mockList = mockList.reverse();
  }
  var pageList = [];
  if (page && size) {
    pageList = mockList.filter(function (item, index) {
      return index < size * page && index >= size * (page - 1);
    });
  } else {
    pageList = mockList;
  }
  return {
    total: mockList.length,
    data: pageList
  };
};

