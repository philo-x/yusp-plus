<template>
  <div class="excel-export">
    <yu-popover placement="top" v-model="isShowProgress" trigger="manual">
      <yu-progress :percentage="percentage" :stroke-width="16" :text-inside="true" :status="progressStatus"></yu-progress>
      <yu-button slot="reference" :icon="icon" :disabled="disabled" :size="size" :type="type" @click="exportFn">{{ title }}</yu-button>
    </yu-popover>
  </div>
</template>
<script>
import { download } from '@/utils/util';
export default {
  name: 'YufpExcelExport',
  props: {
    title: {
      type: String,
      default: '导出'
    },
    beforeClick: Function, // 导出前校验和参数传递
    // 按钮的类型
    type: {
      type: String
    },
    size: String, // 按钮的尺寸
    // 按钮的图片
    icon: {
      type: String,
      default: ''
    },
    // 按钮是否可以点击
    disabled: {
      type: Boolean,
      default: false
    },
    // 文件导出路径
    exportUrl: {
      type: String,
      default: backend.fileService + ''
    },
    exportParam: Object, // 文件导出查询参数
    // 文件导出请求方式
    exportRequestType: {
      type: String,
      default: 'POST'
    },
    // 导出进度查询路径
    progressUrl: {
      type: String,
      default: backend.fileService + '/api/progress'
    },
    progressBaseParam: Object, // 导出进度查询参数
    progressRequestType: {
      type: String,
      default: 'GET'
    },
    // 文件下载路径
    downloadUrl: {
      type: String,
      default: backend.fileService + '/api/file/provider/download'
    }
  },
  data () {
    return {
      finalTitle: this.title, //
      progressTimer: false, // 导出进度查询定时器
      exportId: '', // 导出任务ID
      isShowProgress: false, // 是否显示进度条
      fileId: '', // 文件id
      count: 0, // 轮询次数
      percentage: 0, // 进度条百分比
      progressStatus: '' // 进度条状态
    };
  },
  methods: {
    /**
      * 重置进度状态
      */
    resetDefaultData () {
      this.count = 0;
      this.percentage = 0;
      this.title = this.finalTitle;
      this.disabled = false;
      this.isShowProgress = false;
      if (this.progressTimer) {
        window.clearTimeout(this.progressTimer);
        this.progressTimer = null;
      }
    },
    // 导出
    exportFn () {
      var _this = this;
      _this.$confirm(_this.$t('component.qdsfdc'), _this.$t('component.msg_title'), {
        confirmButtonText: _this.$t('component.btn_confirm'),
        cancelButtonText: _this.$t('component.btn_cancel'),
        type: 'warning'
      }).then(function () {
        _this.title = _this.$t('component.exporting');
        _this.progressStatus = '';
        _this.disabled = true;
        _this.isShowProgress = true;
        _this.beforeClick && _this.beforeClick();
        _this.$nextTick(() => {
          _this.$request({
            url: yufp.util.addTokenInfo(_this.exportUrl),
            method: _this.exportRequestType,
            data: _this.exportParam
          }).then(({code, message, data}) => {
          // 处理请求成功的情况
            if (code === '0000') {
              _this.exportId = data && data.taskId;
              _this.count = 0;
              _this.progressTimer = setTimeout(_this.showProgressFn, 500);
            } else {
              _this.resetDefaultData();
              _this.$message({ type: 'error', message: message });
            }
          }).catch(e => {
            _this.resetDefaultData();
          });
        });
      });
    },
    showProgressFn () {
      const _this = this;
      // 第二次URL请求显示进度条
      _this.$request({
        url: yufp.util.addTokenInfo(_this.progressUrl + '/' + _this.exportId),
        method: _this.progressRequestType
      }).then(({code, message, data}) => {
        // 处理请求成功的情况
        if (code === '0000') {
          _this.count++;
          if (_this.count > 20) {
            _this.resetDefaultData();
            _this.$message.error(_this.$t('component.dcwjgd'));
            return;
          }
          if (!data || data.progressBar == undefined || data.progressBar == '-1') {
            _this.resetDefaultData();
            _this.$message.error(_this.$t('component.export_fail'));
            return;
          }
          _this.isShowProgress = true;
          _this.percentage = data.progressBar;
          if (_this.percentage < 100) {
            _this.progressTimer = setTimeout(_this.showProgressFn, 500);
          } else {
            _this.fileId = data.fileId;
            _this.successFn();
          };
        } else {
          _this.resetDefaultData();
          _this.$message({ type: 'error', message: message || _this.$t('component.export_fail') });
        }
      }).catch(e => {
        _this.resetDefaultData();
      });
    },
    /**
      * 导出成功下载文件
      */
    successFn () {
      const _this = this;
      // 触发导出成功事件
      _this.$emit('success-fn', _this.exportId);
      _this.progressStatus = 'success';
      _this.resetDefaultData();
      _this.$confirm(_this.$t('component.confirm_down'), _this.$t('component.msg_title'), {
        confirmButtonText: _this.$t('component.btn_confirm'),
        cancelButtonText: _this.$t('component.btn_cancel'),
        type: 'warning'
      }).then(() => {
        _this.btnDownloadFn();
      });
    },
    /**
      * 下载按钮点击事件，下载文件
      */
    btnDownloadFn () {
      const _this = this;
      if (_this.fileId && _this.downloadUrl) {
        download(yufp.util.addTokenInfo(_this.downloadUrl + '?fileId=' + _this.fileId));
      } else {
        _this.$message({ message: _this.$t('component.down_fail'), type: 'error' });
      }
    }
  }
};
</script>

<style>
  .excel-export{
    position: relative;
    float: left;
    margin-left: 10px;
  }
</style>