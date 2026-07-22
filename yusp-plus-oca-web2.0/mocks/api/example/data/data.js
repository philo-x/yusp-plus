import Mock from 'mockjs'
export const List = []
const count = 55

const baseContent = '<p>I am testing data, I am testing data.</p><p></p>'
const imageUri = 'http://www.yusys.com.cn/group/newsCenter/newslist/201812/W020181213492932714886.png'

for (let i = 0; i < count; i++) {
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
    imageUri,
    platforms: ['a-platform'],
    remark: '@ctitle(15, 100)',
    EVALUATION_PERIOD_ID: '@date',
    'OPEN_FLAG|1': ['O', 'N', 'C'],
    'MEASURE|1': ['已', '未'],
    'LOCK|1': ['已', '未'],
    'flag|1': [true, false],
    loginCode: '@zip',
    userName: '@cname',
    'gender|1': ['01', '02'],
    certNo: '@natural',
    deadline: '@date',
    userEmail: '@email',
    entrantsDate: '@date'
  }))
}

export const NameList = []

for (let i = 0; i < count; i++) {
  NameList.push(Mock.mock({
    name: '@first'
  }))
}
NameList.push({ name: 'mock-Pan' })
