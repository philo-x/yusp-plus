import Mock from 'mockjs'
var List = [];
var count = 15;
Mock.Random.increment(1000);
for (var i = 0; i < count; i++) {
  List.push(Mock.mock({
    id: '@increment(2)',
    num: '@integer(5000, 600000)',
    title: '@ctitle(8, 15)',
    createAt: '@date',
    loginTime: '@date',
    born: '@date',
    author: '@name',
    auditor: '@cname',
    telephone: /^1[3456789]\d{9}$/,
    browser: '@integer(3, 5)',
    'passwordType|1': ['define', 'default'],
    acco: '@integer(64534534534534345345,6543455555555555222264)',
    'type|1': ['gg', 'kx'],
    'sex|1': ['01', '02'],
    'idenType|1': ['1', '2', '3', '4'],
    'idenCard': /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
    'status|1': ['01', '02'],
    'org|1': ['c1001', 'c1002'],
    pageviews: '@integer(300, 5000)',
    addr: '@ctitle',
    jodAddr: '@ctitle',
    'OPEN_FLAG|1': ['O', 'N', 'C'],
    'MEASURE|1': ['已', '未'],
    'LOCK|1': ['已', '未'],
    'content': '@paragraph',
    'department|1': ['t01', 't02', 'board_director', 't03', 't04']
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
  console.log(body)
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

/**
 * 修改与新增
 * @param config
 * @returns {{code: number}}
 */
export function save (config) {
  // var temp = paramBody2Obj(config.body);
  var temp = config.body;
  temp.id = !temp.id ? Math.floor((Math.random() * 1000) + 10000) : temp.id;
  var updateFlag = false;
  for (var i = List.length - 1; i >= 0; i--) {
    var v = List[i];
    if (v.id === temp.id) {
      var index = List.indexOf(v);
      List.splice(index, 1, temp);
      updateFlag = true;
      break;
    }
  }
  if (!updateFlag) {
    List.unshift(temp);
  }
  return {
    data: {
      code: 0
    }
  };
};

export function changeStatus (config) {
  var temp = config.body;
  var ids = temp.ids.split(',');
  for (var i = List.length - 1; i >= 0; i--) {
    var v = List[i];
    for (var j = ids.length - 1; j >= 0; j--) {
      var id = ids[j];
      if (v.id === Number(id)) {
        List[i].status = v.status === '01' ? '02' : '01';
        ids.splice(j, 1);
        break;
      }
    }
  }
  return {
    data: {
      code: '0'
    }
  };
};
/**
 * 批量删除
 * @param config
 * @returns {{code: string}}
 */
export function deleteData (config) {
  var temp = config.body;
  var ids = temp.ids.split(',');
  for (var i = List.length - 1; i >= 0; i--) {
    var v = List[i];
    for (var j = ids.length - 1; j >= 0; j--) {
      var id = ids[j];
      if (v.id === Number(id)) {
        var index = List.indexOf(v);
        List.splice(index, 1);
        ids.splice(j, 1);
        break;
      }
    }
  }
  return {
    data: {
      code: '0'
    }
  };
};

