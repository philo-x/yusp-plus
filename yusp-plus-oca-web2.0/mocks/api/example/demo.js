import Mock from 'mockjs'
/* eslint camelcase:0  */
/* eslint no-implicit-coercion:0  */
/* eslint arrow-body-style:0  */
/* eslint vars-on-top:0 */
/* eslint no-inner-declarations:0 */

import { List, NameList } from './data/data'
import orgTreeData from './data/orgTreeData'
import orgOrgData from './data/orgOrgData'
import userData from './data/userData'
import roleData from './data/roleData'

function getList(config) {
  console.log(`数据-------：${JSON.stringify(config.query)}`)
  const { title, createAt, type, page, size, sort, author, auditor, status } = config.query.condition && config.query.condition !== '{}' ? JSON.parse(config.query.condition) : config.query
  var mockList = List.filter(function(item) {
    if (createAt && item.createAt !== createAt) {
      return false
    }
    if (type && item.type !== type) {
      return false
    }
    if (status && item.status !== status) {
      return false
    }
    if (title && item.title.indexOf(title) < 0) {
      return false
    }
    if (author && item.author.indexOf(author) < 0) {
      return false
    }
    if (auditor && item.auditor.indexOf(auditor) < 0) {
      return false
    }
    return true
  })
  if (sort === '-id') {
    mockList = mockList.reverse()
  }
  var pageList = []
  if (page && size) {
    pageList = mockList.filter(function(item, index) {
      return index < size * page && index >= size * (page - 1)
    })
  } else {
    pageList = mockList
  }
  return {
    code: 20000,
    total: mockList.length,
    data: pageList
  }
}
/**
 * 修改与新增
 * @param config
 * @returns {{code: number}}
 */
function save(config) {
  console.log(config.body)
  var { id } = config.body
  id = !id ? Math.floor((Math.random() * 1000) + 10000) : id
  var updateFlag = false
  for (var i = List.length - 1; i >= 0; i--) {
    var v = List[i]
    if (v.id === id) {
      var index = List.indexOf(v)
      List.splice(index, 1, config.body)
      updateFlag = true
      break
    }
  }
  if (!updateFlag) {
    config.body.id = id
    List.unshift(config.body)
  }
  return {
    code: 0
  }
}
/**
 * 批量删除
 * @param config
 * @returns {{code: string}}
 */
function deleteData(config) {
  console.log(config.body)
  const { ids } = config.body
  var idArray = typeof ids === 'string' ? ids.split(',') : ids;
  for (var i = List.length - 1; i >= 0; i--) {
    var v = List[i]
    if (Array.isArray(idArray) && idArray.length > 0) {
      for (var j = idArray.length - 1; j >= 0; j--) {
        var ele = idArray[j]
        if (v.id === Number(ele)) {
          var index = List.indexOf(v);
          List.splice(index, 1)
          idArray.splice(j, 1)
          break;
        }
      }
    } else {
      if (v.id === Number(idArray)) {
        List.splice(i, 1)
        break;
      }
    }
  }
  return {
    code: 0
  }
}

function exportExcel () {
  return {
    id: Mock.mock({
      'id|1-100': 100
    }).id
  };
};

Mock.Random.extend({
  percentage: function (date) {
    var percentageList = [20, 40, 50, 60, 80, 100];
    return this.pick(percentageList);
  }
});

function getProgress () {
  return {
    percentage: Mock.Random.percentage()
  };
};

export default [
  {
    url: '/trade/example/list', // mock拦截路径
    type: 'get', // mock拦截方法
    response: (config) => { // 接口对应的响应
      return getList(config)
    }
  },
  {
    url: '/trade/example/save', // mock拦截路径
    type: 'post', // mock拦截方法
    response: save// 接口对应的响应
  },
  {
    url: '/trade/example/delete',
    type: 'post',
    response: deleteData
  },
  {
    url: '/trade/example/tree',
    type: 'get',
    response: () => {
      return {
        data: orgTreeData.data
      }
    }
  },
  {
    url: '/trade/example/orgtree',
    type: 'get',
    response: () => {
      return {
        data: orgOrgData.data
      }
    }
  },
  {
    url: '/trade/example/user',
    type: 'get',
    response: () => {
      return {
        data: userData.data
      }
    }
  },
  {
    url: '/trade/example/role',
    type: 'get',
    response: () => {
      return {
        data: roleData.data
      }
    }
  },
  {
    url: '/trade/example/role',
    type: 'get',
    response: () => {
      return {
        data: roleData.data
      }
    }
  },
  {
    url: '/trade/example/export',
    type: 'POST',
    response: () => {
      return {
        code: 0,
        data: exportExcel
      }
    }
  },
  {
    url: '/trade/example/getProgress',
    type: 'POST',
    response: () => {
      return {
        code: 0,
        data: getProgress
      }
    }
  },
  // {
  //   url: '/trade/example/save',
  //   type: 'post',
  //   response: (res) => {
  //     const {body} = res;
  //     if (body) {
  //       if(body.id) {
  //         body.id = Mock.mock('@increment');
  //       }
  //       List.unshift(body)
  //     }
  //     return {
  //       code: 0
  //     };
  //   }
  // }
]
