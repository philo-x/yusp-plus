<template>
  <div>
    <el-upload
      :action="action"
      :data="dataObj"
      :limit="limit"
      list-type="listType"
      :multiple="false"
      :show-file-list="showFileList"
      :file-list="fileList"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-preview="handlePreview"
    >
      <el-button size="small" type="primary">点击上传</el-button>
      <span slot="tip" class="el-upload__tip" v-if="uploadText" style="margin-left:10px;"> {{ uploadText }} </span>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible" v-if="fileList.length">
      <div v-for="item in fileList" :key="item.name">
        <img width="100%" :src="item.url" alt="" />
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { clone } from '@/utils'
export default {
  name: "singleUpload",
  props: { 
    value: String,
    uploadText: String, //上传规则说明
    limit: {
      type: Number,
      required: false,
      default: 10
    },
    // 上传文件的最大值 默认10M
    maxSize:{
      type: Number,
      required: false,
      default: 10
    },
    // 文件列表的类型 text/picture/picture-card
    listType:{
      type: String,
      default: 'text'
    },
    // 上传地址
    action: { 
      type: String,
      default: 'http://yusp-oss.oss-cn-chengdu.aliyuncs.com'
    },
    // 上传服务文件名
    dirName: {
      type: String,
      default: ''
    },
    file: {
      type: Array,
      default: function() {
        return []
      }
    }
  },
  data() {
    return {
      fileList: [],
      dataObj: {
        policy: "",
        signature: "",
        key: "",
        ossaccessKeyId: "",
        dir: "",
        host: "",
      },
      dialogVisible: false,
      fileObj: {},
    };
  },
  computed: {
    // imageUrl() {
    //   return this.value;
    // },
    // imageName() {
    //   if (this.value != null && this.value !== "") {
    //     return this.value.substr(this.value.lastIndexOf("/") + 1);
    //   } else {
    //     return null;
    //   }
    // },
    // fileList() {
    //   return [
    //     {
    //       name: this.imageName,
    //       url: this.imageUrl,
    //     },
    //   ];
    // },
    showFileList: {
      get: function() {
        // return this.value !== null && this.value !== "" && this.value !== undefined;
        return this.fileList.length > 0;
      },
      set: function(newValue) {},
    },
  },
  watch: {
    file: {
      deep: true,
      handler(newVal, oldVal) {
        clone(newVal, this.fileList);
        this.fileList.reverse();
      }
    }
  },
  methods: {
    getUUID() {
      return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, (c) => {
        return (c === "x" ? (Math.random() * 16) | 0 : "r&0x3" | "0x8").toString(16);
      });
    },
    policy() {
      return new Promise((resolve, reject) => {
        this.$request({
          method: "get",
          url: backend.appOcaService + "/api/oss/policy",
        }).then((response) => {
          // console.log(response);
          resolve(response);
        });
      });
    },
    emitInput(val) {
      this.$emit("input", val, this.fileObj);
    },
    handleRemove(file, fileList) {
      this.$emit('remove', file);
    },
    handlePreview(file) {
      this.dialogVisible = true;
    },
    beforeUpload(file) {
      const isMaxSize = file.size < 10;
      this.fileObj = file;
      if(isMaxSize) {
        this.$message.error(`上传头像图片大小不能超过${this.maxSize}MB!`);
      }
      const isGetParams = new Promise((resolve, reject) => {
        this.policy()
          .then((response) => {
            // console.log("响应的数据", response);
            this.dataObj.signature = response.data.signature;
            this.dataObj.ossaccessKeyId = response.data.ossaccessKeyId;
            this.dataObj.key = response.data.dir + this.getUUID() + "_${filename}";
            this.dataObj.dir = this.dirName + response.data.dir;
            this.dataObj.host = response.data.host;
            this.dataObj.policy = response.data.policy;
            this.dataObj.expire = response.data.expire;
            // console.log("响应的数据22222", this.dataObj);
            resolve(true);
          })
          .catch(() => {
            reject(false);
          });
      });
      return isMaxSize && isGetParams;
    },
    handleUploadSuccess(res, file) {
      this.showFileList = true;
      this.fileList.pop();
      this.fileList.push({
        name: file.name,
        url: this.dataObj.host + "/" + this.dataObj.key.replace("${filename}", file.name),
      });
      this.emitInput(this.fileList[0].url);
    },
  },
};
</script>
<style></style>
