import Mock from 'mockjs'
export const UList = [];
export const List = [];
export const dataList = [];
const count = 15;
Mock.Random.increment(1000);
let str = '';
let mFiled = '';
for (var i = 0; i < count; i++) {
  str = Mock.mock('@id');
  mFiled = ['title', 'type', 'status', 'author', 'tel', 'time', 'label', 'top'][Mock.mock('@integer(0, 7)')];
  dataList.push(Mock.mock({
    id: str,
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
    'LOCK|1': ['已', '未'],
    'flag|1': [true, false],
    loginCode: '@zip',
    userName: '@cname',
    'gender|1': ['01', '02'],
    certNo: '@natural',
    deadline: '@date',
    userEmail: '@email',
    entrantsDate: '@date'
  }));
  List.push(Mock.mock({
    'mPkV': 'WfiWorkflowOrgGroupWfiWorkflowOrgForm' + str,
    'pkvalue': str,
    'usrId': '@increment(2)',
    'mNewDispV': '@ctitle(8, 15)',
    'orgName': '@ctitle(8, 15)',
    'wfsign': '@ctitle(8, 15)',
    'wfname': '@ctitle(8, 15)',
    'applType': '@ctitle(8, 15)',
    'orgCode': '@ctitle(8, 15)',
    'remark': '@ctitle(8, 15)',
    'mFieldNm': mFiled,
    'mFieldId': mFiled,
    'mDatetime': '@ctitle(8, 15)',
    'mOldDispV': '@ctitle(8, 15)'
  }));
}