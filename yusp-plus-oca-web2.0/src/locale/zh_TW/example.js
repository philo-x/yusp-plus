/**
 * @created by helin3 2019-04-09
 * @updated by
 * @description 业务模块（可以分多个文件存储） zh-TW语言包
 */
export default {
  guide: {
    description: '引導頁對於壹些第壹次進入項目的人很有用，妳可以簡單介紹下項目的功能。本 Demo 是基於',
    button: '打開引導'
  },
  components: {
    documentation: '文檔',
    tinymceTips: '富文本是管理後臺壹個核心的功能，但同時又是壹個有很多坑的地方。在選擇富文本的過程中我也走了不少的彎路，市面上常見的富文本都基本用過了，最終權衡了壹下選擇了Tinymce。更詳細的富文本比較和介紹見',
    dropzoneTips: '由於我司業務有特殊需求，而且要傳七牛 所以沒用第三方，選擇了自己封裝。代碼非常的簡單，具體代碼妳可以在這裏看到 @/components/Dropzone',
    stickyTips: '當頁面滾動到預設的位置會吸附在頂部',
    backToTopTips1: '頁面滾動到指定位置會在右下角出現返回頂部按鈕',
    backToTopTips2: '可自定義按鈕的樣式、show/hide、出現的高度、返回的位置 如需文字提示，可在外部使用Element的el-tooltip元素',
    imageUploadTips: '由於我在使用時它只有vue@1版本，而且和mockjs不兼容，所以自己改造了壹下，如果大家要使用的話，優先還是使用官方版本。'
  },
  excel: {
    export: '導出',
    selectedExport: '導出已選擇項',
    placeholder: '請輸入文件名(默認excel-list)'
  },
  zip: {
    export: '導出',
    placeholder: '請輸入文件名(默認file)'
  },
  pdf: {
    tips: '這裏使用   window.print() 來實現下載pdf的功能'
  },
  theme: {
    change: '換膚',
    documentation: '換膚文檔',
    tips: 'Tips: 它區別於 navbar 上的 theme-pick, 是兩種不同的換膚方法，各自有不同的應用場景，具體請參考文檔。'
  },
  button: {
    xz: '新增',
    xg: '修改',
    xq: '詳情',
    sc: '刪除',
    dc: '導出'
  },
  rules: {
    message: {
      btx: '必填項',
      sz: '數字'
    }
  },
  xform: {
    query: {
      bt: '標題',
      sj: '時間',
      lx: '類型'
    },
    button: {
      ss: '搜索',
      cz: '重置'
    },
    update: {
      mc: '名稱',
      sj: '時間',
      lx: '類型',
      cyr: '參與人',
      zz: '作者',
      shr: '審覈人',
      yds: '閱讀數',
      zt: '狀態',
      zdy: '自定義',
      dp: '點評'
    }
  },
  xtable: {
    column: {
      bm: '編碼',
      mc: '名稱',
      lx: '類型',
      cyr: '參與人',
      zz: '作者',
      shr: '審覈人',
      yds: '閱讀數',
      zt: '狀態',
      sj: '時間'
    }
  },
  xdialog: {
    button: {
      bc: '保存',
      qx: '取消',
      qd: '確定'
    }
  },
  message: {
    czcg: '操作成功',
    qxxzytjl: '請先選擇一條記錄',
    cczjyjscgwjsfjx: '此操作將永久刪除該文件, 是否繼續?',
    ts: '提示',
    xzwj: '下載文件'
  },
  multiLanguage: {
    sc: '刪除', // 删除
    dc: '導出', // 导出
    xz: '新增', // 新增
    xq: '詳情', // 详情
    xg: '修改', // 修改
    sj: '時間', // 时间
    sz: '數字', // 数字
    zdy: '自定義', // 自定义
    mc: '名稱', // 名称
    ts: '提示', // 提示
    ss: '搜索', // 搜索
    cczjyjscgwjsfjx: '此操作將永久刪除該文件, 是否繼續?', // 此操作将永久删除该文件, 是否继续?
    bt: '標題', // 标题
    yds: '閱讀數', // 阅读数
    cyr: '參與人', // 参与人
    bm: '編碼', // 编码
    bc: '保存', // 保存
    xzwj: '下載文件', // 下载文件
    czcg: '操作成功', // 操作成功
    qd: '確定', // 确定
    shr: '審核人', // 审核人
    btx: '必填項', // 必填项
    qx: '取消', // 取消
    cz: '重置', // 重置
    zz: '作者', // 作者
    dp: '點評', // 点评
    zt: '狀態', // 状态
    lx: '類型', // 类型
    qxxzytjl: '請先選擇一條記錄', // 请先选择一条记录
  }
}
