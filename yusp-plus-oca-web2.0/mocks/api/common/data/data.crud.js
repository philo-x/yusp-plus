import Mock from 'mockjs'

export const List = []
const count = 66

// 生成模拟数据
for (let i = 0; i < count; i++) {
  List.push(Mock.mock({
    id: '@increment',
    title: '@ctitle(5, 10)',
    createAt: '@date',
    author: '@cname',
    auditor: '@cname',
    importance: '@integer(1, 3)',
    'type|1': ['CN', 'US', 'JP', 'EU'],
    'status|1': ['published', 'draft', 'deleted'],
    pageviews: '@integer(300, 5000)',
    remark: '@ctitle(15, 100)',
    userId: '@zip',
    userName: '@cname',
    loginCode: '@zip',
    'gender|1': ['01', '02'],
    'certTepe:1': ['01', '02', '03', '04'],
    certNo: '@natural',
    deadline: '@date',
    userEmail: '@email'
    // TODO 您可以添加更多属性
  }))
}