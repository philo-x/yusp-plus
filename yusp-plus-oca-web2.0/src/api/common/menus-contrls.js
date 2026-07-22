/**
 * @created by helin3 2019-04-08
 * @updated by
 * @description 会话相关静态数据（菜单、控制点），模拟后台接口返回
 */

// 外链配置示例，浏览器打开与iframe打开
// { menuId: 'dm-22009', menuName: 'externalLink', upMenuId: 'dm-22000', menuIcon: '', funcId: 'externalLink', funcUrl: 'http://www.yusys.com.cn' },
// { menuId: 'dm-22010', menuName: 'iframeLink', upMenuId: 'dm-22000', menuIcon: '', funcId: 'iframeLink', funcUrl: 'iframelink=http%3A%2F%2Fwww.yusys.com.cn' },

// 菜单数据
const demoMenus = [
  // 一级菜单
  // { menuId: 'dm-10000', menuName: '首页', upMenuId: '0', menuIcon: 'component', funcId: 'dashboard', funcUrl: 'common/dashboard/index'},
  // { menuId: 'dm-20000', menuName: '子工程1', upMenuId: '0', menuIcon: 'yu-icon-task' },
  // 二级菜单
  // { menuId: 'dm-21000', menuName: '空白模板', upMenuId: 'dm-20000', menuIcon: '', funcId: 'blank', funcUrl: 'example/templates/blank/index' },
  // { menuId: 'gm-22000', menuName: '子工程2', upMenuId: '0', menuIcon: 'yu-icon-box1' },
  // { menuId: 'dm-23000', menuName: '模板示例', upMenuId: 'dm-20000', menuIcon: 'yu-icon-task' },
  
  {'menuId': '39c0cf27c05a4eff8aba3be93ccb4bf6', 'menuName': '系统管理', 'upMenuId': '0', 'menuOrder': 1, 'menuIcon': 'yu-icon-task', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'a0d472ff0b0c4de5b7d4411ff4e32bca', 'i18nKey': 'xtgl', 'details': null},

  {'menuId': '74d47cbb74d64263b5b2da4f18bfdd2d', 'menuName': '权限管理', 'upMenuId': '39c0cf27c05a4eff8aba3be93ccb4bf6', 'menuOrder': 1, 'menuIcon': null, 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'a0d472ff0b0c4de5b7d4411ff4e32bca', 'i18nKey': 'qxgl', 'details': null},
  // {'menuId': '152f5e47a9f140ff8539a5b0a5d97938', 'menuName': '流程管理', 'upMenuId': '39c0cf27c05a4eff8aba3be93ccb4bf6', 'menuOrder': 1, 'menuIcon': null, 'funcUrl': null, 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': null, 'i18nKey': null, 'details': null},
  {'menuId': 'dafdca98d69041a5aa190a6e489be358', 'menuName': '公共参数管理', 'upMenuId': '39c0cf27c05a4eff8aba3be93ccb4bf6', 'menuOrder': 2, 'menuIcon': null, 'funcUrl': null, 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': null, 'i18nKey': 'ggcsgl', 'details': null},
  // {'menuId': '445b6c1ef7da48fdb18416309f108774', 'menuName': '消息中心', 'upMenuId': '39c0cf27c05a4eff8aba3be93ccb4bf6', 'menuOrder': 4, 'menuIcon': null, 'funcUrl': null, 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': null, 'i18nKey': 'xxzx', 'details': null},
  {'menuId': '814e52ecb0df4421ba774a30746b87be', 'menuName': '调度管理', 'upMenuId': '39c0cf27c05a4eff8aba3be93ccb4bf6', 'menuOrder': 5, 'menuIcon': null, 'funcUrl': null, 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': null, 'i18nKey': 'ddgl', 'details': null},
  {'menuId': '733c7872049a429c8381b10c5969fca0', 'menuName': '系统监控', 'upMenuId': '39c0cf27c05a4eff8aba3be93ccb4bf6', 'menuOrder': 6, 'menuIcon': null, 'funcUrl': null, 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': null, 'i18nKey': 'xtjk', 'details': null},

  {'menuId': '4173d1d5031842c4b844d1728105f1c8', 'menuName': '金融机构管理', 'upMenuId': '74d47cbb74d64263b5b2da4f18bfdd2d', 'menuOrder': 1, 'menuIcon': null, 'funcUrl': 'content/systemManager/fincalOrgManager/fincalOrgManager', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '00b992e3b05c4751a4afe7de66182e00', 'i18nKey': 'jrjggl', 'details': null},
  {'menuId': '63c2e6d0c5dc496490f4ea44dd3abf6e', 'menuName': '机构管理', 'upMenuId': '74d47cbb74d64263b5b2da4f18bfdd2d', 'menuOrder': 2, 'menuIcon': null, 'funcUrl': 'content/systemManager/orgInfoManager/orgInfoManager', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'ec06c05f4add442894817d79057607d4', 'i18nKey': 'jggl', 'details': null},
  {'menuId': '0bebfbf96424497c8c99bb4693185c84', 'menuName': '用户管理', 'upMenuId': '74d47cbb74d64263b5b2da4f18bfdd2d', 'menuOrder': 3, 'menuIcon': null, 'funcUrl': 'content/systemManager/userInfoManager/sysUserManager', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'f80ae5132e2142a0a6eb9133543bdf49', 'i18nKey': 'yhgl', 'details': null},
  {'menuId': '8878aee19d8a4943ab8fcdd06028144b', 'menuName': '岗位管理', 'upMenuId': '74d47cbb74d64263b5b2da4f18bfdd2d', 'menuOrder': 3, 'menuIcon': null, 'funcUrl': 'content/systemManager/dutyManager/dutyManager', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '540588f82fb44ba58923a679a373de0f', 'i18nKey': 'gwgl', 'details': null},
  {'menuId': '907c8c7ad35646b285d37bac9f74fb0b', 'menuName': '角色管理', 'upMenuId': '74d47cbb74d64263b5b2da4f18bfdd2d', 'menuOrder': 4, 'menuIcon': null, 'funcUrl': 'content/systemManager/roleManage/roleManage', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '6cbf17506f9244e6b80d3762030adbcf', 'i18nKey': 'jsgl', 'details': null},
  {'menuId': '8367fb9ae69c4e9aaf6c31316c01a38f', 'menuName': '部门管理', 'upMenuId': '74d47cbb74d64263b5b2da4f18bfdd2d', 'menuOrder': 5, 'menuIcon': null, 'funcUrl': 'content/systemManager/dptManager/dptManager', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'a9530ea1a3fe4f44b0514a3e62bb6cf7', 'i18nKey': 'bmgl', 'details': null},
  {'menuId': '1211', 'menuName': '数据授权', 'upMenuId': '74d47cbb74d64263b5b2da4f18bfdd2d', 'menuOrder': 7, 'menuIcon': null, 'funcUrl': 'content/systemManager/roleDataPowerManager/roleDataPowerManager', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '8452109860c947d89663d65a3f424056', 'i18nKey': 'sjsq', 'details': null},
  {'menuId': 'e22e72e7cadd49a0bb42c6544f5dd12f', 'menuName': '功能授权', 'upMenuId': '74d47cbb74d64263b5b2da4f18bfdd2d', 'menuOrder': 8, 'menuIcon': null, 'funcUrl': 'content/systemManager/ResourceAllocationManager/resourceSetManager', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '607189ea9b7144c3842b02f4e661950d', 'i18nKey': 'gnsq', 'details': null},

  // {'menuId': 'b487a1df41244be3a1a65ef0b256ddaa', 'menuName': '流程定义', 'upMenuId': '152f5e47a9f140ff8539a5b0a5d97938', 'menuOrder': 1, 'menuIcon': null, 'funcUrl': 'workflow/studio/nwflist/nwflist', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'ae41f68f2fbb4de5b1a5af4e28ca545e', 'i18nKey': null, 'details': null},
  // {'menuId': '144617b227a04e2496449541a2ab9ec9', 'menuName': '流程仿真', 'upMenuId': '152f5e47a9f140ff8539a5b0a5d97938', 'menuOrder': 2, 'menuIcon': null, 'funcUrl': 'workflow/studio/wfsimulation/wfsimulation', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '82b063a838824d73b66c89ec53116a50', 'i18nKey': null, 'details': null},
  // {'menuId': '4ef5f3e095b242bab24b7b4ab3417c05', 'menuName': '业务配置', 'upMenuId': '152f5e47a9f140ff8539a5b0a5d97938', 'menuOrder': 3, 'menuIcon': null, 'funcUrl': 'workflow/studio/config/nwfbiz', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'fee14fa1eecc436baaa35782a72626de', 'i18nKey': null, 'details': null},
  // {'menuId': '59ba13ae7fa1458f948b58ad70a39b86', 'menuName': '高级配置', 'upMenuId': '152f5e47a9f140ff8539a5b0a5d97938', 'menuOrder': 4, 'menuIcon': null, 'funcUrl': null, 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': null, 'i18nKey': null, 'details': null},
  // {'menuId': '60fecf854a3a4f89a22c511c3e31f019', 'menuName': '流程监控', 'upMenuId': '152f5e47a9f140ff8539a5b0a5d97938', 'menuOrder': 5, 'menuIcon': null, 'funcUrl': null, 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': null, 'i18nKey': null, 'details': null},

  {'menuId': 'a9dd3aa991cb423e94212dd18a42200c', 'menuName': '逻辑系统管理', 'upMenuId': 'dafdca98d69041a5aa190a6e489be358', 'menuOrder': 1, 'menuIcon': null, 'funcUrl': 'content/systemManager/logicSysManager/logicSysManager', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'beb23fb37bc548b39b52d938f75470fe', 'i18nKey': 'ljxtgl', 'details': null},
  {'menuId': '4eccf88dbdce4151a2a31ea0f236021c', 'menuName': '业务功能管理', 'upMenuId': 'dafdca98d69041a5aa190a6e489be358', 'menuOrder': 2, 'menuIcon': null, 'funcUrl': 'content/systemManager/functionManage/functionManage', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'dbd0c7878e47494d9b93c15391f0b690', 'i18nKey': 'ywgngl', 'details': null},
  {'menuId': '5ed68cd744914c83ba300d85eda1b3c8', 'menuName': '控制点管理', 'upMenuId': 'dafdca98d69041a5aa190a6e489be358', 'menuOrder': 3, 'menuIcon': null, 'funcUrl': 'content/systemManager/resContrManage/resContrManage', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'a4750c7d71f84cdf862551c836cfdcb2', 'i18nKey': 'kzdgl', 'details': null},
  {'menuId': '5cb209f943974c038e5d93292a271e11', 'menuName': '数据权限模板配置', 'upMenuId': 'dafdca98d69041a5aa190a6e489be358', 'menuOrder': 4, 'menuIcon': null, 'funcUrl': 'content/systemManager/datafilter/dataFilter', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'ffa5aa9129e1441bad43bc4034abba3e', 'i18nKey': 'sjqxmbpz', 'details': null},
  {'menuId': 'ed03c7a1318f40fa9dadcba37209d286', 'menuName': '菜单配置', 'upMenuId': 'dafdca98d69041a5aa190a6e489be358', 'menuOrder': 4, 'menuIcon': null, 'funcUrl': 'content/systemManager/menuConfig/menuConfig', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'a9ab28c38ea24518b21a00f3a7078742', 'i18nKey': 'cdpz', 'details': null},
  {'menuId': 'd9d4e444f1c4448dbed674d11e138a5a', 'menuName': '数据权限管理', 'upMenuId': 'dafdca98d69041a5aa190a6e489be358', 'menuOrder': 5, 'menuIcon': null, 'funcUrl': 'content/systemManager/dataAuthManage/dataAuthManage', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '36ea07fea59844c6b0e43973c7dd237d', 'i18nKey': 'sjqxgl', 'details': null},
  {'menuId': 'a854b72e7bec46d1ab27d182b2f88b5d', 'menuName': '数据字典配置', 'upMenuId': 'dafdca98d69041a5aa190a6e489be358', 'menuOrder': 7, 'menuIcon': null, 'funcUrl': 'content/systemManager/lookupdict/lookupdict', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'd5d97cfa25554f06b071831f9f8dcb68', 'i18nKey': 'sjzdpz', 'details': null},
  {'menuId': '8b0c26d45792416fb31d314586f6ebf7', 'menuName': '系统参数', 'upMenuId': 'dafdca98d69041a5aa190a6e489be358', 'menuOrder': 8, 'menuIcon': null, 'funcUrl': 'content/systemManager/sysprop/sysprop', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '7da4972d736747879a12f7b6b6c65a32', 'i18nKey': 'xtcs', 'details': null},
  {'menuId': '4be52cb3923f4b60aaf41add36736d87', 'menuName': '序列号模板', 'upMenuId': 'dafdca98d69041a5aa190a6e489be358', 'menuOrder': 9, 'menuIcon': null, 'funcUrl': 'content/systemManager/sequenceConfig/sequenceConfig', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '2eb361b40a04400190436cabc534b570', 'i18nKey': 'xlhmb', 'details': null},
  {'menuId': '288a43e9d2c6432986b15e9faf274960', 'menuName': '系统提示消息管理', 'upMenuId': 'dafdca98d69041a5aa190a6e489be358', 'menuOrder': 9, 'menuIcon': null, 'funcUrl': 'content/systemManager/messageManager/messageManager', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '282fc7cc0657451b9bb2cfe343f66dfb', 'i18nKey': 'xttsxxgl', 'details': null},

  // {'menuId': '2033bfeecf7241ad836f9fc2d5606c13', 'menuName': '消息模板', 'upMenuId': '445b6c1ef7da48fdb18416309f108774', 'menuOrder': 1, 'menuIcon': null, 'funcUrl': 'message/template/template', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '58d145a028034c9098ac219198181017', 'i18nKey': 'xxmb', 'details': null},
  // {'menuId': 'ca60acf448174b1b8213e003fdcda341', 'menuName': '消息订阅', 'upMenuId': '445b6c1ef7da48fdb18416309f108774', 'menuOrder': 2, 'menuIcon': null, 'funcUrl': 'message/subscribe/subscribe', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '38243a44a9684cad817d352c3a9f681e', 'i18nKey': 'xxdy', 'details': null},
  // {'menuId': '2224c4948c0446c89aa9cec9e2b5cebd', 'menuName': '消息历史', 'upMenuId': '445b6c1ef7da48fdb18416309f108774', 'menuOrder': 3, 'menuIcon': null, 'funcUrl': 'message/history/history', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'b5cb12e37f3c4070a984d7a72cd5324c', 'i18nKey': 'xxls', 'details': null},
  // {'menuId': '549e65de5c65438e8183796a26b11bb6', 'menuName': '消息队列', 'upMenuId': '445b6c1ef7da48fdb18416309f108774', 'menuOrder': 4, 'menuIcon': null, 'funcUrl': 'message/pool/pool', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '95f8140f8ddd499fab84e8615c53205d', 'i18nKey': 'xxdl', 'details': null},

  {'menuId': 'f62b80ba050740b4ae8a4f9442853e32', 'menuName': '任务管理', 'upMenuId': '814e52ecb0df4421ba774a30746b87be', 'menuOrder': 1, 'menuIcon': null, 'funcUrl': 'content/systemManager/xxlJob/jobInfo', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'dbaec1b99b5b42a09014b97c21f1da49', 'i18nKey': 'rwgl', 'details': null},
  {'menuId': '52df11d289464a448fdd06b804c71581', 'menuName': '调度中心', 'upMenuId': '814e52ecb0df4421ba774a30746b87be', 'menuOrder': 2, 'menuIcon': null, 'funcUrl': 'content/systemManager/xxlJob/jobIndex', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'b0f09c8b571d4705a3b411e51ae3de85', 'i18nKey': 'ddzx', 'details': null},
  {'menuId': '960347d965cb4d25b332d889fdd1ffc0', 'menuName': '执行器管理', 'upMenuId': '814e52ecb0df4421ba774a30746b87be', 'menuOrder': 3, 'menuIcon': null, 'funcUrl': 'content/systemManager/taskExecutorManager/taskExecutorManager', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '964b218e5bde4ed6867b9f4144b30b21', 'i18nKey': 'zxqgl', 'details': null},
  {'menuId': 'ae7a1a203bfb494fa328df2cb870393f', 'menuName': '调度日志', 'upMenuId': '814e52ecb0df4421ba774a30746b87be', 'menuOrder': 4, 'menuIcon': null, 'funcUrl': 'content/systemManager/xxlJobLog/jobLog', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '0e81b99414cb4041ae753b4d19bae7e4', 'i18nKey': 'ddrz', 'details': null},

  {'menuId': '3936f63cdc994a038a4935e9bcbcdc3d', 'menuName': '日志管理', 'upMenuId': '733c7872049a429c8381b10c5969fca0', 'menuOrder': 1, 'menuIcon': null, 'funcUrl': 'content/systemManager/logManager/logManager', 'funcUrlJs': '1', 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '7aa1a401541d43ae87dce9dd74231328', 'i18nKey': 'rzgl', 'details': null},

  // {'menuId': '6390472c304f46ecaea9dc2d4ae9b487', 'menuName': '委托配置', 'upMenuId': '59ba13ae7fa1458f948b58ad70a39b86', 'menuOrder': 1, 'menuIcon': null, 'funcUrl': 'workflow/studio/config/nwfagentset', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '1c9822c1a5e645fdae5f8a2cea4e78b7', 'i18nKey': null, 'details': null},
  // {'menuId': '6c7f2553f76f4c9ea5cc42ea501e6137', 'menuName': '项目池配置', 'upMenuId': '59ba13ae7fa1458f948b58ad70a39b86', 'menuOrder': 2, 'menuIcon': null, 'funcUrl': 'workflow/studio/config/nwftaskpoolset', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '09539358ad9548dc872839fea479795d', 'i18nKey': null, 'details': null},

  // {'menuId': '900d722a515f495c9bc5760d76669b8e', 'menuName': '全局监控', 'upMenuId': '60fecf854a3a4f89a22c511c3e31f019', 'menuOrder': 1, 'menuIcon': null, 'funcUrl': 'workflow/studio/wfmonitor/wfmonitor', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '9decbe65ea80408fabf5ee8d0b3408da', 'i18nKey': null, 'details': null},
  // {'menuId': '91333885ef374e7b93983b999f492ffb', 'menuName': '运行实例监控', 'upMenuId': '60fecf854a3a4f89a22c511c3e31f019', 'menuOrder': 2, 'menuIcon': null, 'funcUrl': 'workflow/studio/wfmonitor/wfruninstance', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '989ce261518e490fa64e49e995484104', 'i18nKey': null, 'details': null},
  // {'menuId': 'd161e761da8846b4995b4963c5732750', 'menuName': '办结实例监控', 'upMenuId': '60fecf854a3a4f89a22c511c3e31f019', 'menuOrder': 3, 'menuIcon': null, 'funcUrl': 'workflow/studio/wfmonitor/wfendinstance', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': 'e7ca887eaa5740bf94784ca5bb8cc5d6', 'i18nKey': null, 'details': null},
  // {'menuId': 'beaafb13857d4d9a81874c806aef75ff', 'menuName': '人员待办监控', 'upMenuId': '60fecf854a3a4f89a22c511c3e31f019', 'menuOrder': 4, 'menuIcon': null, 'funcUrl': 'workflow/studio/wfmonitor/usertodo', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '9b4af0b7c14e4e028811a0bd6bd79dd1', 'i18nKey': null, 'details': null},
  // {'menuId': '74d131a8286e43248a38f0cdd2a3144a', 'menuName': '异常队列', 'upMenuId': '60fecf854a3a4f89a22c511c3e31f019', 'menuOrder': 5, 'menuIcon': null, 'funcUrl': 'workflow/studio/exception/nwfexception', 'funcUrlJs': null, 'funcUrlCss': null, 'sysId': '1cab27def8fb4c0f9486dcf844b783c0', 'funcId': '4c4a299f4ecb487e9222d1522a35db40', 'i18nKey': null, 'details': null}
  { menuId: 'gm-24000', menuName: '关于项目', upMenuId: '0', menuIcon: 'yu-icon-star-empty', funcId: 'about', funcUrl: 'common/about/index' },
]

// 控制点数据
const demoContrls = [
  { menuId: 'dm-23101', funcId: 'exampleQuery', contrCode: 'create', contrName: '新增' },
  { menuId: 'dm-23101', funcId: 'exampleQuery', contrCode: 'edit', contrName: '修改' },
  { menuId: 'dm-23101', funcId: 'exampleQuery', contrCode: 'detail', contrName: '详情' },
  { menuId: 'dm-23101', funcId: 'exampleQuery', contrCode: 'delete', contrName: '删除' },
  { menuId: 'dm-23101', funcId: 'exampleQuery', contrCode: 'export', contrName: '导出' },

  { menuId: 'dm-23102', funcId: 'exampleTree', contrCode: 'create', contrName: '新增' },
  { menuId: 'dm-23102', funcId: 'exampleTree', contrCode: 'edit', contrName: '修改' },
  { menuId: 'dm-23102', funcId: 'exampleTree', contrCode: 'detail', contrName: '详情' }
]

// 导出菜单&控制点数据
export default {
  demoMenus,
  demoContrls
}
