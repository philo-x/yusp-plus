/* eslint-disable */
import Mock from 'mockjs'
export const List = []

var index = 2
var parrentTmpId = 1000
for (var i = 0; i < index; i++) {
  List.push(Mock.mock({
    parentId: parrentTmpId,
    id: '@increment(2)',
    title: '@ctitle(8, 15)',
    createAt: '@date',
    'status|1': ['published', 'draft', 'deleted'],
    pageviews: '@integer(300, 5000)',
    remark: '@ctitle(15, 100)',
    EVALUATION_PERIOD_ID: '@date',
    'OPEN_FLAG|1': ['O', 'N', 'C'],
    'MEASURE|1': ['已', '未'],
    'LOCK|1': ['已', '未']
  }))
  parrentTmpId += 2
}