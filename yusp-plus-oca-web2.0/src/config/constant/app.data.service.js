/**
 * @created by helin3 2019-04-06
 * @updated by zhuly 2021-02-03
 * @description 全局后台API服务映射表
 * 使用方式：
 * 1) /src/api/* 可使用别名@
 * import backend from '@/config/constant/app.data.service'
 * 2) /mocks/* 请使用相对路径，否则可能出错
 * import backend from '../../../src/config/constant/app.data.service'
 * 3) 组件内部：$backend.xxx
 */
const isSingleServer = process.env.VUE_APP_SINGLE_SERVER === "true";
const serviceNameList = {
  uaaService: '/yusp-uaa', // 用户认证微服务
  gatewayService: '/zuul', // 网关
  appCommonService: '/yusp-app-common', // 基础服务
  appOcaService: '/yusp-app-oca', // 组织机构、菜单权限微服务-kqf-lyq
  noticeService: '/yusp-app-notice', // 公告微服务
  messageService: '/yusp-app-oca', // 消息中心微服务
  jobService: '/yusp-job-admin', // 分布式调度管理端
  fileService: '/yusp-file', // 文件服务地址
  seqService: '/yusp-sequence', // 全局序列号微服务
  echainService: '/yusp-echain', // 工作流微服务
  remote: 'xxx.xxx.xxx.xxx', // TODO 待修改
  example: '/ms-example-common', // 示例微服务
  actService: '/ms-tcc-service1', // TCC-示例原子微服务
  scoreService: '/ms-tcc-service2', // TCC-示例原子微服务
  compositeService: '/ms-tcc-composite', // TCC-示例聚合微服务
  mycatService: '/ms-example-mycat', // 示例分布式数据库 缓存
  msScriptsPSService: '/ms-scripts-public-security', // 剧本-公安信息
  msScriptsLimitService: '/ms-scripts-limit', // 剧本-公安信息
  workflowService: '/yusp-app-oca', // 新工作流微服务
  plusBusinessService: '/yusp-plus-business', // oca业务场景微服务
  msScriptsApplyService: '/ms-scripts-apply', // 剧本申请服务
  msmFrontWebService: '/yusp-msm-front-web-server' // 前端监控与审计日志服务
};
// 单体模式时 服务地址置空
const singleServiceList = {
  uaaService: '', // 用户认证微服务
  gatewayService: '', // 网关
  appCommonService: '', // 基础服务
  appOcaService: '', // 组织机构、菜单权限微服务-kqf-lyq
  noticeService: '', // 公告微服务
  messageService: '', // 消息中心微服务
  jobService: '', // 分布式调度管理端
  // fileService: 'http://172.16.20.93:8400', // TR文件服务地址
  // fileService: 'http://139.155.246.245:8400', // 文件服务地址
  fileService: '', // 文件服务地址
  seqService: '', // 全局序列号微服务
  echainService: '', // 工作流微服务
  remote: '', // TODO 待修改
  example: '', // 示例微服务
  actService: '', // TCC-示例原子微服务
  scoreService: '', // TCC-示例原子微服务
  compositeService: '', // TCC-示例聚合微服务
  mycatService: '', // 示例分布式数据库 缓存
  msScriptsPSService: '', // 剧本-公安信息
  msScriptsLimitService: '', // 剧本-公安信息
  msScriptsApplyService: '', // 剧本申请服务
  msmFrontWebService: '', // 前端监控与审计日志服务
  workflowService: '' // 新工作流微服务
};
export default isSingleServer ? singleServiceList : serviceNameList;
