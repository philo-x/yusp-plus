<template>
  <div class="yu-single-upload">
    <yu-upload ref="upload" :action="action" :limit-number="limit" :disabled="disabled" list-type="listType" :multiple="false" :file-list="fileList" :on-remove="handleRemove" :on-success="handleUploadSuccess" :before-upload="beforeUpload" :on-error="handleError">
      <yu-button size="small" type="primary" :disabled="disabled">{{ $t('notice.djsc') }}</yu-button>
      <span slot="tip" class="el-upload__tip" v-if="uploadText" style="margin-left:10px;"> {{ uploadText }} </span>
    </yu-upload>
    <transition name="el-zoom-in-top">
      <ul class="loaded-file-list" v-if="fileInfo.length">
        <li v-for="(item, key) in fileInfo" :key="item.fileId">
          <span :class="[item.extName,'file-icon']" @click="download(item, key)"></span>
          <div class="file-info" @click="download(item, key)">
            <p class="elli">{{ item.fileName }}</p>
            <span class="elli">{{ item.fileSize | formatFileSize }}</span>
          </div>
          <p v-if="!disabled" class="delete" @click="deleteFile(item, key)"><span class="yu-icon-delete"></span></p>
        </li>
      </ul>
    </transition>
  </div>
</template>
<script>
import { download } from '@/utils/util';
import { clone, extend } from '@/utils';
export default {
  name: 'YuSingleUpload',
  props: {
    uploadText: String, // 上传规则说明
    // 文件限制个数
    limit: {
      type: Number,
      required: false,
      default: 10
    },
    // 是否禁用上传
    disabled: {
      type: Boolean,
      default: false
    },
    // 上传文件的最大值 默认10M
    maxSize: {
      type: Number,
      required: false,
      default: 10
    },
    // 文件列表的类型 text/picture/picture-card
    listType: {
      type: String,
      default: 'text'
    },
    // 上传地址
    action: {
      type: String,
      default: yufp.util.addTokenInfo(
        backend.fileService + '/api/file/provider/uploadfile'
      )
    },
    // 上传服务文件名
    dirName: {
      type: String,
      default: ''
    },
    file: {
      type: Array,
      default: function () {
        return [];
      }
    }
  },
  data () {
    return {
      fileInfo: [], // 展示的附件列表
      fileList: [], // 组件上传列
      loadFileNum: 0, // 正在现在的文件数量
      dataObj: {
        policy: '',
        signature: '',
        key: '',
        ossaccessKeyId: '',
        dir: '',
        host: ''
      }
    };
  },
  watch: {
    // 正在上传的文件数量，用于提交时判断文件是否提交完成
    loadFileNum (val) {
      this.$emit('load-number', val);
    },
    // 页面初始化列表展示
    file: {
      deep: true,
      handler (val) {
        this.fileInfo = extend([], val);
        this.fileInfo.forEach((item) => {
          if (item.extName.startsWith('.')) {
            const type = item.extName.substring(
              item.extName.lastIndexOf('.') + 1
            );
            item.extName = this.getICon(type);
          }
        });
        this.loadFileNum = val.length;
      }
    }
  },
  methods: {
    /**
     * 文件上传失败时的钩子
     */
    handleError (err, file, fileList) {
      console.log(err);
      this.loadFileNum = this.loadFileNum - 1;
    },

    /**
     * 文件列表移除文件时的钩子
     */
    handleRemove (file, fileList) {
      this.$refs.upload.abort(file);
      this.loadFileNum = this.loadFileNum - 1;
    },

    /**
     * 上传文件之前的钩子，参数为上传的文件，若返回 false 或者返回 Promise 且被 reject，则停止上传。
     */
    beforeUpload (file) {
      const isMaxSize = file.size / (1024 * 1024) > 10;
      const limitNum = this.fileInfo.length < this.limit;
      this.loadFileNum = this.loadFileNum + 1;
      if (isMaxSize) {
        this.$message.error(`${this.$t('notice.maxsize')}${this.maxSize}MB!`);
        return false;
      }
      if (!limitNum) {
        this.$message.error(`${this.$t('notice.limitnum')}${this.limit}!`);
        return false;
      }
    },

    /**
     * 文件上传成功时的钩子
     */
    handleUploadSuccess (res, file, fileList) {
      var index = fileList.indexOf(file);
      // var fileSize = (file.size / 1024).toFixed(2);
      if (index > -1) {
        fileList.splice(index, 1);
      }
      if (this.fileInfo.length >= this.maxSize) {
        this.$message.error('`${this.$t(\'notice.limitnum\')}${this.limit}!`');
        this.loadFileNum = this.loadFileNum - 1;
        return;
      }
      if (file) {
        const fileObj = {};
        const nameArr = file.name.split('.');
        fileObj.fileName = file.name;
        fileObj.fileSize = file.size;
        fileObj.fileId = res.body;
        fileObj.filePath =
          this.dataObj.host +
          '/' +
          this.dataObj.key.replace('${filename}', file.name);
        fileObj.extName = this.getICon(nameArr[1]);
        this.fileInfo.push(fileObj);
        this.$emit('uploaded', fileObj);
      }
    },

    /**
     * 根据文件类型显示icon
     * @param e.value 搜索框的值
     */
    getICon (type) {
      var icon = '';
      if (type === 'xls' || type === 'xlsx') {
        icon = 'yu-icon-read';
      } else if (type === 'doc' || type === 'docx') {
        icon = 'yu-icon-word';
      } else if (type === 'ppt' || type === 'pptx') {
        icon = 'yu-icon-data';
      } else if (type === 'pdf') {
        icon = 'yu-icon-pdf';
      } else if (type === 'txt') {
        icon = 'yu-icon-details';
      } else if (type === 'zip') {
        icon = 'yu-icon-zip';
      } else if (type === 'rar') {
        icon = 'yu-icon-zip';
      } else if (
        type === 'png' ||
        type === 'jpg' ||
        type === 'jpeg' ||
        type === 'svg' ||
        type === 'gif'
      ) {
        icon = 'yu-icon-img';
      } else {
        icon = 'yu-icon-infofile';
      }
      return icon;
    },

    // 删除已上传的附件
    deleteFile (item, index) {
      var _this = this;
      _this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        callback: function (action) {
          if (action === 'confirm') {
            // 删除文件
            if (item.fileId) {
              _this.$request({
                method: 'post',
                url: yufp.util.addTokenInfo(
                  backend.fileService +
                    '/api/file/provider/delete/' +
                    item.fileId
                )
              }).then((response) => {
                _this.fileInfo.splice(index, 1);
                _this.$emit('delete', item);
                _this.loadFileNum = _this.loadFileNum - 1;
              });
            } else {
              _this.fileInfo.splice(index, 1);
              _this.$emit('delete', item);
              _this.loadFileNum = _this.loadFileNum - 1;
            }
          } else {
            _this.$message({
              message: '已取消删除',
              type: 'info'
            });
          }
        }
      });
    },
    download (item, index) {
      if (item.fileId) {
        download(
          yufp.util.addTokenInfo(
            backend.fileService +
              '/api/file/provider/download?fileId=' +
              item.fileId
          )
        );
      }
    }
  }
};
</script>
<style>
.loaded-file-list li {
  list-style: none;
  width: 150px;
  display: inline-block;
  margin-top: 16px;
  margin-right: 12px;
  position: relative;
  padding: 10px 12px 0;
  border: 1px solid #f5f5f5;
}
.loaded-file-list li .file-icon {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  background: #f5f5f5;
  display: inline-block;
  color: #333;
  font-size: 24px;
  text-align: center;
  line-height: 40px;
  margin-right: 8px;
  vertical-align: top;
}
.loaded-file-list li .file-info {
  display: inline-block;
  width: 90px;
}
.file-info {
  cursor: pointer;
}
.file-info > p {
  font-size: 12px;
  color: #666666;
  line-height: 16px;
}
.file-info > span {
  color: #999999;
  font-size: 12px;
}
.loaded-file-list .delete {
  position: absolute;
  bottom: 8px;
  right: 12px;
  width: 20px;
  height: 20px;
  text-align: center;
  line-height: 20px;
  border-radius: 4px 0 4px 0;
  color: #2877ff;
  font-size: 18px;
  display: none;
}
.loaded-file-list li:hover {
  border: 1px solid #2877ff;
  border-radius: 4px;
}
.loaded-file-list li:hover .delete {
  display: inline-block;
  cursor: pointer;
}
.yu-single-upload .elli {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.yu-single-upload .el-upload-list__item .el-icon-close {
  top: 0px;
}
.yu-single-upload .el-progress-bar__inner {
  background-color: #2877ff;
}
</style>
