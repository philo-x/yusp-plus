/**
 * @created by helin3 2019-04-09
 * @updated by
 * @description 业务模块（可以分多个文件存储） zh_CN语言包
 */
export default {
  guide: {
    description: '引导页对于一些第一次进入项目的人很有用，你可以简单介绍下项目的功能。本 Demo 是基于',
    button: '打开引导'
  },
  components: {
    documentation: '文档',
    tinymceTips: '富文本是管理后台一个核心的功能，但同时又是一个有很多坑的地方。在选择富文本的过程中我也走了不少的弯路，市面上常见的富文本都基本用过了，最终权衡了一下选择了Tinymce。更详细的富文本比较和介绍见',
    dropzoneTips: '由于我司业务有特殊需求，而且要传七牛 所以没用第三方，选择了自己封装。代码非常的简单，具体代码你可以在这里看到 @/components/Dropzone',
    stickyTips: '当页面滚动到预设的位置会吸附在顶部',
    backToTopTips1: '页面滚动到指定位置会在右下角出现返回顶部按钮',
    backToTopTips2: '可自定义按钮的样式、show/hide、出现的高度、返回的位置 如需文字提示，可在外部使用Element的el-tooltip元素',
    imageUploadTips: '由于我在使用时它只有vue@1版本，而且和mockjs不兼容，所以自己改造了一下，如果大家要使用的话，优先还是使用官方版本。'
  },
  excel: {
    export: '导出',
    selectedExport: '导出已选择项',
    placeholder: '请输入文件名(默认excel-list)'
  },
  zip: {
    export: '导出',
    placeholder: '请输入文件名(默认file)'
  },
  pdf: {
    tips: '这里使用   window.print() 来实现下载pdf的功能'
  },
  theme: {
    change: '换肤',
    documentation: '换肤文档',
    tips: 'Tips: 它区别于 navbar 上的 theme-pick, 是两种不同的换肤方法，各自有不同的应用场景，具体请参考文档。'
  },
  button: {
    xz: '新增',
    xg: '修改',
    xq: '详情',
    sc: '删除',
    dc: '导出'
  },
  rules: {
    message: {
      btx: '必填项',
      sz: '数字'
    }
  },
  xform: {
    query: {
      bt: '标题',
      sj: '时间',
      lx: '类型'
    },
    button: {
      ss: '搜索',
      cz: '重置'
    },
    update: {
      mc: '名称',
      sj: '时间',
      lx: '类型',
      cyr: '参与人',
      zz: '作者',
      shr: '审核人',
      yds: '阅读数',
      zt: '状态',
      zdy: '自定义',
      dp: '点评'
    }
  },
  xtable: {
    column: {
      bm: '编码',
      mc: '名称',
      lx: '类型',
      cyr: '参与人',
      zz: '作者',
      shr: '审核人',
      yds: '阅读数',
      zt: '状态',
      sj: '时间'
    }
  },
  xdialog: {
    button: {
      bc: '保存',
      qx: '取消',
      qd: '确定'
    }
  },
  message: {
    czcg: '操作成功',
    qxxzytjl: '请先选择一条记录',
    cczjyjscgwjsfjx: '此操作将永久删除该文件, 是否继续?',
    ts: '提示',
    xzwj: '下载文件'
  },
  multiLanguage: {
    sc: '删除', // 删除
    dc: '导出', // 导出
    xz: '新增', // 新增
    xq: '详情', // 详情
    xg: '修改', // 修改
    sj: '时间', // 时间
    sz: '数字', // 数字
    zdy: '自定义', // 自定义
    mc: '名称', // 名称
    ts: '提示', // 提示
    ss: '搜索', // 搜索
    cczjyjscgwjsfjx: '此操作将永久删除该文件, 是否继续?', // 此操作将永久删除该文件, 是否继续?
    bt: '标题', // 标题
    yds: '阅读数', // 阅读数
    cyr: '参与人', // 参与人
    bm: '编码', // 编码
    bc: '保存', // 保存
    xzwj: '下载文件', // 下载文件
    czcg: '操作成功', // 操作成功
    qd: '确定', // 确定
    shr: '审核人', // 审核人
    btx: '必填项', // 必填项
    qx: '取消', // 取消
    cz: '重置', // 重置
    zz: '作者', // 作者
    dp: '点评', // 点评
    zt: '状态', // 状态
    lx: '类型', // 类型
    qxxzytjl: '请先选择一条记录', // 请先选择一条记录
  }
}
