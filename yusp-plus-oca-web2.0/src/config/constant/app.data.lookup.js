/**
 * @created by helin3 2019-04-11
 * @description 静态数据字典
 */

export default {
  CRUD_TYPE: [
    { key: 'ADD', value: '新增' },
    { key: 'EDIT', value: '修改' },
    { key: 'DETAIL', value: '详情' }
  ],
  GENDER: [
    { key: '01', value: '男' },
    { key: '02', value: '女' }
  ],
  YESNO: [
    { key: '01', value: '是' },
    { key: '02', value: '否' }
  ],
  HASNO: [
    { key: '01', value: '有' },
    { key: '02', value: '无' }
  ],
  OBJECT_TYPE: [
    { 'key': 'R', 'value': '角色' },
    { 'key': 'U', 'value': '用户' },
    { 'key': 'D', 'value': '部门' },
    { 'key': 'G', 'value': '机构' }
  ],
  NOTICE_LEVEL: [
    { 'key': 'I', 'value': '重要' },
    { 'key': 'N', 'value': '一般' }
  ],
  TOP_FLAG: [
    { 'key': 'I', 'value': '是' },
    { 'key': 'N', 'value': '否' }
  ],
  USER_CLASSIFY: [
    { 'key': 'gg', 'value': '通知公告' },
    { 'key': 'kx', 'value': '行业快讯' }
  ],
  USER_STATUS: [
    { 'key': '01', 'value': '生效' },
    { 'key': '03', 'value': '待生效' },
    { 'key': '02', 'value': '失效' }
  ],
  DEPARTMENT: [
    { key: 't01', value: '技术一部' },
    { key: 't02', value: '技术二部' },
    { key: 'board_director', value: '董事会' },
    { key: 't03', value: '技术三部' },
    { key: 't04', value: '技术四部' }
  ],
  BRANCH: [
    { key: 'c1001', value: '成都支行' },
    { key: 'c1002', value: '北京总部' }
  ],
  EDUCATION_TYPE: [
    { key: '0', value: '博士' },
    { key: '1', value: '硕士' },
    { key: '2', value: '本科' },
    { key: '3', value: '大专' },
    { key: '4', value: '高中及以下' }
  ],
  NATION: [
    { key: '01', value: '汉族' },
    { key: '02', value: '傣族' },
    { key: '03', value: '维吾尔族' },
    { key: '04', value: '白族' }
  ],
  COMPANY_TYPE: [
    { key: '01', value: '国有企业' },
    { key: '02', value: '集体企业' },
    { key: '03', value: '研究生' },
    { key: '04', value: '私营企业' },
    { key: '05', value: '个体工商户' }
  ],
  JOB_TYPE: [
    { key: '01', value: '管理岗位' },
    { key: '02', value: '专技岗位' },
    { key: '03', value: '工勤岗位' }
  ],
  RESOURCE_TYPE: [
    { 'key': 'M', 'value': '菜单' },
    { 'key': 'C', 'value': '控制点' },
    { 'key': 'D', 'value': '数据权限' }
  ],
  RECIVE_TYPE: [
    { 'key': 'R', 'value': '角色' },
    { 'key': 'G', 'value': '机构' }
  ],
  PUB_STS: [
    { 'key': 'O', 'value': '已发布' },
    { 'key': 'C', 'value': '未发布' }
  ],
  OP_TYPE: [
    { 'key': 'O-0', 'value': '拿回'},
    { 'key': 'O-1', 'value': '打回'},
    { 'key': 'O-2', 'value': '退回'},
    { 'key': 'O-3', 'value': '挂起'},
    { 'key': 'O-4', 'value': '唤醒'},
    { 'key': 'O-5', 'value': '催办'},
    { 'key': 'O-6', 'value': '转办'},
    { 'key': 'O-7', 'value': '协办'},
    { 'key': 'O-8', 'value': '否决'},
    { 'key': 'O-9', 'value': '跳转'},
    { 'key': 'O-10', 'value': '委托'},
    { 'key': 'O-11', 'value': '抄送'},
    { 'key': 'O-12', 'value': '同意'},
    { 'key': 'O-13', 'value': '自动提交'},
    { 'key': 'O-14', 'value': '正常结束'},
    { 'key': 'O-15', 'value': '撤回'},
    { 'key': 'O-16', 'value': '发起'},
    { 'key': 'O-17', 'value': '作废'},
    { 'key': 'O-18', 'value': '签收'},
    { 'key': 'O-19', 'value': '撤销签收'},
    { 'key': 'O-26', 'value': '补签'},
    { 'key': 'O-27', 'value': '加签'}
  ],
  DATA_STS: [
    {'key': 'A', 'value': '启用'},
    {'key': 'I', 'value': '停用'},
    {'key': 'W', 'value': '待启用'}
  ],
  REL_STS: [
    {'key': true, 'value': '已关联'},
    {'key': false, 'value': '未关联'}
  ],
  HTTP_METHOD_TYPE: [
    {'key': 'POST', 'value': 'POST'},
    {'key': 'GET', 'value': 'GET'}
  ],
  SEX_TYPE: [
    {'key': '0', 'value': '女'},
    {'key': '1', 'value': '男'},
    {'key': '2', 'value': '未知'}
  ],
  IDENT_TYPE: [
    {'key': 'A', 'value': '境内企业代码'},
    {'key': 'D', 'value': '台湾居民身份证'},
    {'key': 'K', 'value': '工会机构代码'},
    {'key': 'M', 'value': '境内居民护照'},
    {'key': 'F', 'value': '境外居民护照'},
    {'key': 'H', 'value': '境内企业名称核准号'},
    {'key': 'B', 'value': '境外机构代码'},
    {'key': 'C', 'value': '境内居民身份证'},
    {'key': 'E', 'value': '港澳居民身份证'},
  ],
  HIGHEST_EDU: [
    {'key': '1', 'value': '博士后'},
    {'key': '2', 'value': '博士'},
    {'key': '3', 'value': '硕士'},
    {'key': '4', 'value': '大学本科'},
    {'key': '5', 'value': '大学专科/电大'},
    {'key': '6', 'value': '中专'},
    {'key': '7', 'value': '技术学校'},
    {'key': '8', 'value': '高中'},
    {'key': '9', 'value': '高中'},
    {'key': '10', 'value': '小学'},
    {'key': '11', 'value': '文盲或半文盲'},
    {'key': '99', 'value': '其他'},
  ],
  MESSAGE_LEVEL: [
    {'key': 'info', 'value': '信息'},
    {'key': 'M001', 'value': '未知错误'},
    {'key': 'success', 'value': '成功'},
    {'key': 'error', 'value': '错误'},
  ],
  MESSAGE_TYPE: [
    {'key': 'MODULEINFO', 'value': '模块提示'},
    {'key': 'COMINFO', 'value': '系统级通用提示'},
    {'key': 'DBERR', 'value': '数据库错误提示'},
  ],
  LOG_TYPE: [
    {'key': '2', 'value': '查询数据日志'},
    {'key': '5', 'value': '登出系统'},
    {'key': '9', 'value': '授权日志'},
    {'key': '7', 'value': '访问菜单'},
    {'key': '1', 'value': '操作用户'},
    {'key': '6', 'value': '记录操作日志'},
    {'key': '8', 'value': '访问报表'},
    {'key': '10', 'value': '报表导出'},
    {'key': '3', 'value': '登录系统'},
    {'key': '4', 'value': '按钮操作日志'}
  ],
  READ_STS: [
    {'key': '1', 'value': '已读'},
    {'key': '0', 'value': '未读'},
  ],
  PASSWD_COMPLEX_RULE: [
    {'key': 'number', 'value': '数字'},
    {'key': 'uppercase', 'value': '大写字母'},
    {'key': 'lowercase', 'value': '小写字母'},
    {'key': 'specialCharacters', 'value': '特殊字符'},
  ]
}
