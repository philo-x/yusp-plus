<template>
  <div class="excel-import">
    <yu-popover placement="top" trigger="manual" v-model="isShowProgress" popper-class="import-popover">
      <yu-progress :percentage="percentage" :stroke-width="16" :text-inside="true"></yu-progress>
    </yu-popover>
    <yu-upload class="import-upload-demo" ref="upload" action="" :on-change="changeFn" :show-file-list="false" :auto-upload="false">
      <yu-button :icon="icon" :size="size" :type="type">{{ title }}</yu-button>
    </yu-upload>
  </div>
</template>
<script>
export default {
  name: 'YufpExcelImport',
  props: {
    title: {
      type: String,
      default: '批量导入'
    },
    // 最大允许上传的文件大小，单位MB
    maxFileSize: {
      type: String,
      default: '10'
    },
    // 是否为异步导入，默认为异步，若为同步导入，服务端应该也使用同步导入方式
    async: {
      type: Boolean,
      default: true
    },
    // 导入按钮类型
    type: {
      type: String
    },
    // 导入按钮尺寸
    size: String,
    // 导入按钮icon
    icon: {
      type: String,
      default: ''
    },
    // 文件导入路径
    uploadfileUrl: {
      type: String,
      default: backend.fileService + '/api/file/provider/uploadfile'
    },
    // 文件解析路径
    importUrl: {
      type: String,
      default: ''
    },
    // 导入进度查询路径
    progressUrl: {
      type: String,
      default: backend.fileService + '/api/progress'
    },
    progressBaseParam: Object, // 导入进度查询参数
    // 导入查询类型
    progressRequestType: {
      type: String,
      default: 'GET'
    },
    bizDataParams: {
      type: Object,
      default: function () {
        return {};
      }
    }
  },
  data () {
    return {
      acceptType: ['xlsx', 'xlc', 'xlm', 'xls'],
      // 导入进度弹窗显示
      isShowProgress: false,
      // 进度条默认进度
      percentage: 0,
      // 导入进度查询定时器
      progressTimer: null,
      // 文件信息ID
      fileId: '',
      // 导出任务ID
      taskId: false,
      queryCont: 0,
      // 导出查询进度事件间隔
      delay: 500
    };
  },
  methods: {
    /**
    * 重置进度状态
    */
    resetDefaultData () {
      this.isShowProgress = false;
      this.percentage = 0;
      this.queryCont = 0;
      if (this.progressTimer) {
        // window.clearTimeout(this.progressTimer);
        this.progressTimer = null;
      }
    },
    changeFn (file, fileList) {
      const _this = this;
      const uploadFile = file.raw;
      // 判断导入文件的大小
      const isLt2M = file.raw.size / 1024 / 1024 < _this.maxFileSize;
      // 判断格式
      const fileName = file.raw.name;
      const fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
      const isType = _this.acceptType.some(
        item => item === fileType
      );
      var errorMsg = this.$t('component.fileLimit') + _this.maxFileSize + 'MB!';
      if (!isLt2M) {
        this.$message({message: errorMsg, type: 'error'});
        return;
      };
      if (!isType) {
        this.$message({message: this.$t('component.ExcelImport_msg_003'), type: 'error'});
        return;
      };
      const form = new window.FormData();
      form.append('file', uploadFile);
      _this.$request({
        url: _this.uploadfileUrl,
        method: 'post',
        data: form,
        headers: {
          transformRequest: [data => data]
        }
      }).then(({code, message, data}) => {
        // 处理请求成功的情况
        if (code === '0000') {
          _this.fileId = data;
          // 执行解析
          _this.importExcelFn();
        } else {
          _this.resetDefaultData();
          _this.$message({ type: 'error', message: message });
        }
      });
    },
    /**
    * Excel解析
    */
    importExcelFn () {
      const _this = this;
      var newUrl = yufp.util.addTokenInfo(_this.importUrl + '?fileId=' + _this.fileId);
      _this.$request({
        url: newUrl,
        method: 'post',
        data: _this.bizDataParams
      }).then(({code, message, data}) => {
        // 处理请求成功的情况
        if (code === '0000') {
          if (!_this.async) {
            _this.$message({ type: 'success', message: message });
            _this.$emit('import-success', data);// 导入成功后执行的方法
          } else {
            _this.taskId = data && data.taskId;
            _this.progressTimer = setTimeout(_this.showProgressFn, _this.delay);
          }
        } else {
          _this.resetDefaultData();
          _this.$message({ type: 'error', message: message });
        }
      });
    },
    /**
    * 轮询查询进度
    * @param {String} taskId
    */
    showProgressFn () {
      const _this = this;
      // 第二次URL请求显示进度条
      _this.$request({
        url: yufp.util.addTokenInfo(_this.progressUrl + '/' + _this.taskId),
        method: 'POST'
      }).then(({code, message, data}) => {
        // 处理请求成功的情况
        if (code === '0000') {
          if (!data || data.progressBar == undefined || data.progressBar == '-1') {
            _this.resetDefaultData();
            _this.$message.error(_this.$t('component.import_fail'));
            return;
          }
          _this.queryCont++;
          _this.percentage = data.progressBar;
          if (_this.percentage < 100) {
            _this.isShowProgress = true;
            _this.progressTimer = setTimeout(_this.showProgressFn, _this.delay);
          } else {
            _this.resetDefaultData();
            _this.$message({message: _this.$t('component.import_success'), type: 'success'});
            _this.$emit('import-success', data);// 导入成功后执行的方法
          };
        } else {
          _this.resetDefaultData();
          _this.$message({ type: 'error', message: message || _this.$t('component.import_fail') });
        }
      }).catch(e => {
        _this.resetDefaultData();
      });
    }
  }
};
</script>

<style>
.excel-import{
  position: relative;
  float: left;
  margin-left: 10px;
}
.import-upload-demo .upload-demo .el-upload{
  display: inline-block;
  float: left;
  margin-right: 5px;
}
.import-popover{
  top: -40px;
}
</style>