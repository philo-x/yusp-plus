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
  // { menuId: 'gm-10000', menuName: '首页', upMenuId: '0', menuIcon: 'yu-icon-home', funcId: 'dashboard', funcUrl: 'pages/common/dashboard/dashboard', isIndex: true },
  { menuId: 'gm-20000', menuName: '模板示例', upMenuId: '0', menuIcon: 'yu-icon-template' },
  { menuId: 'gm-30000', menuName: '组件示例', upMenuId: '0', menuIcon: 'yu-icon-box1' },
  { menuId: 'gm-40000', menuName: '特殊示例', upMenuId: '0', menuIcon: 'yu-icon-group' },
  { menuId: 'gm-50000', menuName: '其他示例', upMenuId: '0', menuIcon: 'yu-icon-setting1' },
  { menuId: 'gm-60000', menuName: '原生（废弃）', upMenuId: '0', menuIcon: 'yu-icon-sever' },

  // 模板二级菜单
  { menuId: 'gm-21000', menuName: '空白模板', upMenuId: 'gm-20000', funcId: 'blank', funcUrl: 'example/blank/blank' },
  { menuId: 'gm-23000', menuName: '查询列表', upMenuId: 'gm-20000' },
  { menuId: 'gm-24000', menuName: '表单', upMenuId: 'gm-20000' },
  { menuId: 'gm-26000', menuName: '详情', upMenuId: 'gm-20000' },
  { menuId: 'gm-25000', menuName: '提示', upMenuId: 'gm-20000' },
  // 查询三级级菜单
  { menuId: 'gm-23010', menuName: '简洁列表', upMenuId: 'gm-23000', menuIcon: '', funcId: 'conciseSearch', funcUrl: 'example/template/conciseSearch/conciseSearch' },
  { menuId: 'gm-23012', menuName: '标准列表', upMenuId: 'gm-23000', menuIcon: 'yu-icon-home1', funcId: 'standardSearch', funcUrl: 'example/template/standardSearch/standardSearch' },
  { menuId: 'gm-23013', menuName: '卡片列表', upMenuId: 'gm-23000', menuIcon: '', funcId: 'cardtable', funcUrl: 'example/template/cardtable/cardtable' },
  // { menuId: 'gm-23001', menuName: '普通查询（新）', upMenuId: 'gm-23000', menuIcon: '', funcId: 'searchForm', funcUrl: 'example/template/searchForm/searchForm' },
  // { menuId: 'gm-23002', menuName: '普通查询1111', upMenuId: 'gm-23000', menuIcon: '', funcId: 'exampleQuery', funcUrl: 'example/template/exampleQuery/exampleQuery' },
  { menuId: 'gm-23003', menuName: '树+查询', upMenuId: 'gm-23000', menuIcon: '', funcId: 'exampleTree', funcUrl: 'example/template/exampleTree/exampleTree' },
  { menuId: 'gm-23004', menuName: '查询+表单（编辑）', upMenuId: 'gm-23000', menuIcon: '', funcId: 'exampleEdit', funcUrl: 'example/template/exampleEdit/exampleEdit' },
  { menuId: 'gm-23005', menuName: '查询+表格（主从表）', upMenuId: 'gm-23000', menuIcon: '', funcId: 'searchTable', funcUrl: 'example/template/searchTable/searchTable' },
  { menuId: 'gm-23006', menuName: 'Tab页签+查询', upMenuId: 'gm-23000', menuIcon: '', funcId: 'tabsearch', funcUrl: 'example/template/tabSearch/tabSearch' },
  { menuId: 'gm-23007', menuName: '查询嵌套表格', upMenuId: 'gm-23000', menuIcon: '', funcId: 'queryNestedTable', funcUrl: 'example/template/queryNestedTable/queryNestedTable' },
  { menuId: 'gm-23008', menuName: '查询嵌套表单', upMenuId: 'gm-23000', menuIcon: '', funcId: 'queryNestedForm', funcUrl: 'example/template/queryNestedForm/queryNestedForm' },
  { menuId: 'gm-23009', menuName: '高级查询（旧）', upMenuId: 'gm-23000', menuIcon: '', funcId: 'moreFieldsSearch', funcUrl: 'example/template/moreFieldsSearch/moreFieldsSearch' },
  // { menuId: 'gm-23014', menuName: '性能表格列表', upMenuId: 'gm-23000', menuIcon: '', funcId: 'performanceTable', funcUrl: 'example/template/performanceTable/performanceTable' },

  // 表单三级菜单
  { menuId: 'gm-24012', menuName: '简洁表单', upMenuId: 'gm-24000', menuIcon: '', funcId: 'conciseForm', funcUrl: 'example/template/conciseForm/conciseForm' },
  { menuId: 'gm-24011', menuName: '标准表单', upMenuId: 'gm-24000', menuIcon: '', funcId: 'standardForm', funcUrl: 'example/template/standardForm/standardForm' },
  { menuId: 'gm-24010', menuName: '分组表单（导航）', upMenuId: 'gm-24000', menuIcon: '', funcId: 'groupForm', funcUrl: 'example/template/groupForm/groupForm' },
  // { menuId: 'gm-24008', menuName: '普通表单（新）', upMenuId: 'gm-24000', menuIcon: '', funcId: 'exampleForm1', funcUrl: 'example/template/exampleForm1/exampleForm1' },
  { menuId: 'gm-24001', menuName: '普通表单', upMenuId: 'gm-24000', menuIcon: '', funcId: 'exampleForm', funcUrl: 'example/template/exampleForm/exampleForm' },
  { menuId: 'gm-24003', menuName: '编辑（分组）', upMenuId: 'gm-24000', menuIcon: '', funcId: 'exampleGroup', funcUrl: 'example/template/exampleGroup/exampleGroup' },
  { menuId: 'gm-24009', menuName: '树+表单', upMenuId: 'gm-23000', menuIcon: '', funcId: 'treeForm', funcUrl: 'example/template/treeForm/treeForm' },
  { menuId: 'gm-24004', menuName: '编辑+表格', upMenuId: 'gm-24000', menuIcon: '', funcId: 'tableList', funcUrl: 'example/template/tableList/tableList' },
  { menuId: 'gm-24005', menuName: '页签嵌套编辑', upMenuId: 'gm-24000', menuIcon: '', funcId: 'tabform', funcUrl: 'example/template/tabForm/tabForm' },
  { menuId: 'gm-24006', menuName: '编辑嵌套页签', upMenuId: 'gm-24000', menuIcon: '', funcId: 'formNestTabs', funcUrl: 'example/template/formNestTab/formNestTab' },
  { menuId: 'gm-24007', menuName: 'Steps步骤表单', upMenuId: 'gm-24000', menuIcon: '', funcId: 'exampleSteps', funcUrl: 'example/template/exampleSteps/exampleSteps' },
  
  // 提示三级菜单
  { menuId: 'gm-25001', menuName: '404页面', upMenuId: 'gm-25000', menuIcon: '', funcId: 'error', funcUrl: 'common/error-page/404.vue' },
  { menuId: 'gm-25002', menuName: '500页面', upMenuId: 'gm-25000', menuIcon: '', funcId: 'serverError', funcUrl: 'common/error-page/401.vue' },
  
  // 详情三级菜单
  { menuId: 'gm-26001', menuName: '简洁详情', upMenuId: 'gm-26000', menuIcon: '', funcId: 'conciseDetail', funcUrl: 'example/template/conciseDetail/conciseDetail' },
  { menuId: 'gm-26002', menuName: '标准详情', upMenuId: 'gm-26000', menuIcon: '', funcId: 'standardDetail', funcUrl: 'example/template/standardDetail/standardDetail' },
  { menuId: 'gm-26003', menuName: '分组详情（导航）', upMenuId: 'gm-26000', menuIcon: '', funcId: 'groupInfoForm', funcUrl: 'example/template/groupInfoForm/groupInfoForm' },
  { menuId: 'gm-26004', menuName: '详情（旧）', upMenuId: 'gm-26000', menuIcon: '', funcId: 'exampleFormInfo', funcUrl: 'example/template/exampleFormInfo/exampleFormInfo' },

  //  组件实例二级菜单
  { menuId: 'gm-31000', menuName: '封装字典管理器', upMenuId: 'gm-30000', menuIcon: '', funcId: 'lookup', funcUrl: 'example/package/lookup/lookup' },
  { menuId: 'gm-32000', menuName: '封装树', upMenuId: 'gm-30000', menuIcon: '', funcId: 'elTreeX', funcUrl: 'example/package/elTreeX/elTreeX' },
  { menuId: 'gm-33000', menuName: '封装表格', upMenuId: 'gm-30000', menuIcon: '', funcId: 'elTableX', funcUrl: 'example/package/elTableX/elTableX' },
  { menuId: 'gm-34000', menuName: '封装表格-文档', upMenuId: 'gm-30000', menuIcon: '', funcId: 'elTableXDoc', funcUrl: 'example/package/elTableXDoc/elTableXDoc' },
  { menuId: 'gm-35000', menuName: '自定义选择器', upMenuId: 'gm-30000', menuIcon: '', funcId: 'demoSelector', funcUrl: 'example/package/demoSelector/demoSelector' },
  { menuId: 'gm-36000', menuName: '封装下拉框', upMenuId: 'gm-30000', menuIcon: '', funcId: 'elSelect', funcUrl: 'example/package/elSelect/elSelect' },
  { menuId: 'gm-37000', menuName: '封装级联下拉框', upMenuId: 'gm-30000', menuIcon: '', funcId: 'elCascader', funcUrl: 'example/package/elCascader/elCascader' },
  { menuId: 'gm-38000', menuName: '封装下拉树框', upMenuId: 'gm-30000', menuIcon: '', funcId: 'elComboTree', funcUrl: 'example/package/elComboTree/elComboTree' },
  { menuId: 'gm-39000', menuName: '封装下拉表格框', upMenuId: 'gm-30000', menuIcon: '', funcId: 'elComboTable', funcUrl: 'example/package/elComboTable/elComboTable' },
  { menuId: 'gm-31100', menuName: '富文本组件', upMenuId: 'gm-30000', menuIcon: '', funcId: 'tinymce', funcUrl: 'example/components-demo/tinymce' },
  { menuId: 'gm-31300', menuName: 'Form组件', upMenuId: 'gm-30000', menuIcon: '', funcId: 'elformx', funcUrl: 'example/package/elformx/elformx' },
  { menuId: 'gm-31400', menuName: '拖拽', upMenuId: 'gm-30000', menuIcon: '', funcId: 'exampleDraggable', funcUrl: 'example/package/exampleDraggable/exampleDraggable' },
  { menuId: 'gm-31500', menuName: '拖拽填表单', upMenuId: 'gm-30000', menuIcon: '', funcId: 'dragForm', funcUrl: 'example/package/dragForm/dragForm' },
  { menuId: 'gm-31600', menuName: '普通文件上传', upMenuId: 'gm-30000', menuIcon: '', funcId: 'normalUpload', funcUrl: 'example/native/normalUpload/normalUpload' },
  { menuId: 'gm-31700', menuName: '异步树', upMenuId: 'gm-30000', menuIcon: '', funcId: 'asynctree', funcUrl: 'example/template/exampleTree/exampleTree' },
  { menuId: 'gm-31800', menuName: '打开（菜单/自定义）页签', upMenuId: 'gm-30000', menuIcon: '', funcId: 'demoAddMenuTab', funcUrl: 'example/package/demoAddMenuTab/demoAddMenuTab' },
  { menuId: 'gm-32200', menuName: '文件上传附带MD5', upMenuId: 'gm-30000', menuIcon: '', funcId: 'uploadWidthMD5', funcUrl: 'example/native/uploadWidthMD5/uploadWidthMD5' },
  { menuId: 'gm-32300', menuName: '常见图表', upMenuId: 'gm-30000', menuIcon: '', funcId: 'lineBarPie', funcUrl: 'example/native/lineBarPie/lineBarPie' },
  { menuId: 'gm-32400', menuName: 'Echarts图表组件示例', upMenuId: 'gm-30000', menuIcon: '', funcId: 'yuEcharts', funcUrl: 'example/package/demoEcharts/demoEcharts' },
  { menuId: 'gm-32500', menuName: '性能验证', upMenuId: 'gm-30000', menuIcon: '', funcId: 'performance', funcUrl: 'example/package/performance/performance' },
  //  特殊示例二级菜单
  { menuId: 'gm-41000', menuName: '云图', upMenuId: 'gm-40000', menuIcon: '', funcId: 'wordcloud', funcUrl: 'example/package/wordcloud/wordcloud' },
  { menuId: 'gm-42000', menuName: 'excel操作', upMenuId: 'gm-40000' },
  { menuId: 'gm-43000', menuName: '多语言验证', upMenuId: 'gm-40000', menuIcon: '', funcId: 'exampleLanguage', funcUrl: 'example/package/multiLanguage/multiLanguage' },
  //{ menuId: 'gm-44000', menuName: '外部网页', upMenuId: 'gm-40000', menuIcon: '', funcId: 'externalPage', funcUrl: 'http://www.baidu.com' },
  { menuId: 'gm-44000', menuName: '外部网页', upMenuId: 'gm-40000', menuIcon: '', funcId: 'externalPage', funcUrl: 'iframelink=http%3A%2F%2Fwww.baidu.com' },
  { menuId: 'gm-45000', menuName: '思维导图', upMenuId: 'gm-40000', menuIcon: '', funcId: 'mindMap', funcUrl: 'example/package/mindMap/mindMap' },
  { menuId: 'gm-46000', menuName: '图片裁剪', upMenuId: 'gm-40000', menuIcon: '', funcId: 'xCropper', funcUrl: 'example/package/yufpCropper/yufpCropper' },
  { menuId: 'gm-47000', menuName: '视窗布局', upMenuId: 'gm-40000', menuIcon: '', funcId: 'xView', funcUrl: 'example/package/yufpView/yufpView' },
  { menuId: 'gm-48000', menuName: '日历', upMenuId: 'gm-40000', menuIcon: '', funcId: 'calendar', funcUrl: 'example/package/calendar/calendar' },
  { menuId: 'gm-49000', menuName: '路由演示', upMenuId: 'gm-40000', menuIcon: '', funcId: 'routeExample', funcUrl: 'example/package/routeExample/routePage/routePage' },
  { menuId: 'gm-41100', menuName: '公式计算器', upMenuId: 'gm-40000', menuIcon: '', funcId: 'calcExample', funcUrl: 'example/package/calculator/calculator' },
  { menuId: 'gm-41200', menuName: '附属表单', upMenuId: 'gm-40000', menuIcon: '', funcId: 'attachedForm', funcUrl: 'example/package/attachedForm/attachedForm' },
  { menuId: 'gm-41300', menuName: '打印', upMenuId: 'gm-40000', menuIcon: '', funcId: 'printExample', funcUrl: 'example/package/printExample/printExample' },
  { menuId: 'gm-41400', menuName: 'pdf预览及下载', upMenuId: 'gm-40000', menuIcon: '', funcId: 'pdf', funcUrl: 'example/package/pdf/pdf' },

  // 特殊示例二级菜单
  { menuId: 'gm-42001', menuName: 'excel导出', upMenuId: 'gm-42000', menuIcon: '', funcId: 'excelExport', funcUrl: 'example/package/excelExport/excelExport' },

  //  其他示例二级菜单
  { menuId: 'gm-51000', menuName: '图标Icons', upMenuId: 'gm-50000', menuIcon: '', funcId: 'icons', funcUrl: 'example/package/icons/icons' },
  { menuId: 'gm-52000', menuName: '小U留痕', upMenuId: 'gm-50000', menuIcon: '', funcId: 'umodifytrace', funcUrl: 'example/package/umodifytrace/uTrace' },

  // 原生二级菜单
  { menuId: 'gm-61000', menuName: '增删改查', upMenuId: 'gm-60000', menuIcon: '', funcId: 'gridCrud', funcUrl: 'example/native/gridCrud/gridCrud' },
  { menuId: 'gm-62000', menuName: '普通多表头', upMenuId: 'gm-60000', menuIcon: '', funcId: 'multiplegrid', funcUrl: 'example/native/multiplegrid/multiplegrid' },
  { menuId: 'gm-63000', menuName: '动态多表头', upMenuId: 'gm-60000', menuIcon: '', funcId: 'dynamicMultipleGrid', funcUrl: 'example/native/dynamicMultipleGrid/dynamicMultipleGrid' },
  { menuId: 'gm-64000', menuName: '可编辑表格', upMenuId: 'gm-60000', menuIcon: '', funcId: 'editorGrid', funcUrl: 'example/native/editorGrid/editorGrid' },
  { menuId: 'gm-65000', menuName: '左树右表格', upMenuId: 'gm-60000', menuIcon: '', funcId: 'treedemo', funcUrl: 'example/native/treedemo/treedemo' },
  { menuId: 'gm-66000', menuName: 'TAB页签', upMenuId: 'gm-60000', menuIcon: '', funcId: 'tab', funcUrl: 'example/native/tab/tab' },
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
