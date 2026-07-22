/**
 * @created by helin3 2019-04-08
 * @updated by
 * @description 数据字典静态数据（模拟后台全量字典）
 */
const demoLookups = {
  USER_STATUS: [
    { key: '01', value: '正常' },
    { key: '02', value: '冻结' },
    { key: '03', value: '销户' }
  ],
  CUST_TYPE: [
    { key: '1', value: '零售' },
    { key: '2', value: '公司' }
  ],
  IDENT_TYPE: [
    { key: '1', value: '居民身份证' },
    { key: '2', value: '居民户口薄' },
    { key: '3', value: '组织机构代码' },
    { key: '4', value: '营业执照代码' }
  ],
  NATIONALITY: [
    { key: 'CN', value: '中国' },
    { key: 'US', value: '美国' },
    { key: 'JP', value: '日本' },
    { key: 'EU', value: '欧元区' }
  ],
  PUBLISH_STATUS: [
    { key: 'published', value: '草稿' },
    { key: 'draft', value: '已发布' },
    { key: 'deleted', value: '已删除' }
  ],
  BRANCH: [
    { key: 'c1001', value: '成都支行' }
  ],
  SUBRANCH: [
    { key: 'c100101', value: '高新支行' },
    { key: 'c100102', value: '天府三街支行' },
    { key: 'c100103', value: '天府五街支行' }
  ],
  EDUCATION_TYPE: [
    { key: '0', value: '博士' },
    { key: '1', value: '硕士' },
    { key: '2', value: '本科' },
    { key: '3', value: '大专' },
    { key: '4', value: '高中及以下' }
  ]
}

// 导出所有静态字典数据
export default demoLookups
