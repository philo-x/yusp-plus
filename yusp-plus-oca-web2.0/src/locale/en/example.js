/**
 * @created by helin3 2019-04-09
 * @updated by
 * @description 业务模块（可以分多个文件存储） en语言包
 */

export default {
  guide: {
    description: 'The guide page is useful for some people who entered the project for the first time. You can briefly introduce the features of the project. Demo is based on ',
    button: 'Show Guide'
  },
  components: {
    documentation: 'Documentation',
    tinymceTips: 'Rich text is a core feature of the management backend, but at the same time it is a place with lots of pits. In the process of selecting rich texts, I also took a lot of detours. The common rich texts on the market have been basically used, and I finally chose Tinymce. See the more detailed rich text comparison and introduction.',
    dropzoneTips: 'Because my business has special needs, and has to upload images to qiniu, so instead of a third party, I chose encapsulate it by myself. It is very simple, you can see the detail code in @/components/Dropzone.',
    stickyTips: 'when the page is scrolled to the preset position will be sticky on the top.',
    backToTopTips1: 'When the page is scrolled to the specified position, the Back to Top button appears in the lower right corner',
    backToTopTips2: 'You can customize the style of the button, show / hide, height of appearance, height of the return. If you need a text prompt, you can use element-ui el-tooltip elements externally',
    imageUploadTips: 'Since I was using only the vue@1 version, and it is not compatible with mockjs at the moment, I modified it myself, and if you are going to use it, it is better to use official version.'
  },
  excel: {
    export: 'Export',
    selectedExport: 'Export Selected Items',
    placeholder: 'Please enter the file name (default excel-list)'
  },
  zip: {
    export: 'Export',
    placeholder: 'Please enter the file name (default file)'
  },
  pdf: {
    tips: 'Here we use window.print() to implement the feature of downloading PDF.'
  },
  theme: {
    change: 'Change Theme',
    documentation: 'Theme documentation',
    tips: 'Tips: It is different from the theme-pick on the navbar is two different skinning methods, each with different application scenarios. Refer to the documentation for details.'
  },
  button: {
    xz: 'Add',
    xg: 'Update',
    xq: 'Detail',
    sc: 'Delete',
    dc: 'Export'
  },
  rules: {
    message: {
      btx: 'compulsory',
      sz: 'digits'
    }
  },
  xform: {
    query: {
      bt: 'Title',
      sj: 'Time',
      lx: 'Type'
    },
    button: {
      ss: 'Search',
      cz: 'Reset'
    },
    update: {
      mc: 'Name',
      sj: 'Time',
      lx: 'Type',
      cyr: 'Participant',
      zz: 'Author',
      shr: 'Auditor',
      yds: 'Readcount',
      zt: 'State',
      zdy: 'Custom',
      dp: 'Comment'
    }
  },
  xtable: {
    column: {
      bm: 'Code',
      mc: 'Name',
      lx: 'Type',
      cyr: 'Participant',
      zz: 'Author',
      shr: 'Auditor',
      yds: 'Readcount',
      zt: 'State',
      sj: 'Time'
    }
  },
  xdialog: {
    button: {
      bc: 'Save',
      qx: 'Cancel',
      qd: 'Confirm'
    }
  },
  message: {
    czcg: 'Operation success',
    qxxzytjl: 'Please select a record first',
    cczjyjscgwjsfjx: 'This operation will permanently delete the file. Do you want to continue? ',
    ts: 'Tips',
    xzwj: 'Download files'
  },
  multiLanguage: {
    sc: 'Delete', // 删除
    dc: 'export', // 导出
    xz: 'new', // 新增
    xq: 'details', // 详情
    xg: 'modification', // 修改
    sj: 'time', // 时间
    sz: 'digits', // 数字
    zdy: 'custom', // 自定义
    mc: 'name', // 名称
    ts: 'hint', // 提示
    ss: 'search', // 搜索
    cczjyjscgwjsfjx: 'This operation will permanently delete the file. Do you want to continue?', // 此操作将永久删除该文件, 是否继续?
    bt: 'title', // 标题
    yds: 'reading number', // 阅读数
    cyr: 'participant', // 参与人
    bm: 'encoding', // 编码
    bc: 'Save', // 保存
    xzwj: 'download files', // 下载文件
    czcg: 'successful operation', // 操作成功
    qd: 'sure', // 确定
    shr: 'auditor', // 审核人
    btx: 'compulsory', // 必填项
    qx: 'Cancel', // 取消
    cz: 'reset', // 重置
    zz: 'author', // 作者
    dp: 'comment', // 点评
    zt: 'state', // 状态
    lx: 'type', // 类型
    qxxzytjl: 'Please select a record first.' // 请先选择一条记录
  }
}
