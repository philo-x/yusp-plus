/* eslint camelcase:0 */
/* eslint arrow-body-style:0 */
import Mock from 'mockjs'
import {List, NameList} from './data/data'

export default [
  // {
  //   url: '/article/list',
  //   type: 'get',
  //   response: config => {
  //     const { importance, type, title, page = 1, limit = 20, sort } = config.query

  //     let mockList = List.filter(item => {
  //       if (importance && item.importance !== +importance) return false
  //       if (type && item.type !== type) return false
  //       if (title && item.title.indexOf(title) < 0) return false
  //       return true
  //     })

  //     if (sort === '-id') {
  //       mockList = mockList.reverse()
  //     }

  //     const pageList = mockList.filter((item, index) => index < limit * page && index >= limit * (page - 1))

  //     return {
  //       code: 20000,
  //       data: {
  //         total: mockList.length,
  //         items: pageList
  //       }
  //     }
  //   }
  // },

  {
    url: '/article/detail',
    type: 'get',
    response: config => {
      const { id } = config.query
      for (const article of List) {
        if (article.id === +id) {
          return {
            code: 20000,
            data: article
          }
        }
      }
    }
  },

  {
    url: '/article/pv',
    type: 'get',
    response: _ => {
      return {
        code: 20000,
        data: {
          pvData: [
            { key: 'PC', pv: 1024 },
            { key: 'mobile', pv: 1024 },
            { key: 'ios', pv: 1024 },
            { key: 'android', pv: 1024 }
          ]
        }
      }
    }
  },

  {
    url: '/article/create',
    type: 'post',
    response: _ => {
      return {
        code: 20000,
        data: 'success'
      }
    }
  },

  {
    url: '/article/update',
    type: 'post',
    response: _ => {
      return {
        code: 20000,
        data: 'success'
      }
    }
  },

  // username search
  {
    url: '/search/user',
    type: 'get',
    response: config => {
      const { name } = config.query
      const mockNameList = NameList.filter(item => {
        const lowerCaseName = item.name.toLowerCase()
        return !(name && lowerCaseName.indexOf(name.toLowerCase()) < 0)
      })
      return {
        code: 20000,
        data: { items: mockNameList }
      }
    }
  },

  // transaction list
  {
    url: '/transaction/list',
    type: 'get',
    response: config => {
      return {
        code: 20000,
        data: {
          total: 20,
          'items|20': [{
            order_no: '@guid()',
            timestamp: +Mock.Random.date('T'),
            username: '@name()',
            price: '@float(1000, 15000, 0, 2)',
            'status|1': ['success', 'pending']
          }]
        }
      }
    }
  }
]

