/* eslint-disable */
import { List } from './data/data.subtable'

export default [
  {
    url: '/example/template/subtable',
    type: 'get',
    response: (config) => {
      const { id, page, size } = config.query
      console.log(config.query)
      var filterData = List.filter(function(item, index) {
        if (id) {
          return +item.id === +id
        } else {
          return true
        }
      })
      var pageList = []
      if (page && size) {
        pageList = filterData.filter(function(item, index) {
          return index < size * page && index >= size * (page - 1)
        })
      } else {
        pageList = filterData
      }
      return {
        code: 20000,
        total: filterData.length,
        data: pageList
      }
    }
  }
]
