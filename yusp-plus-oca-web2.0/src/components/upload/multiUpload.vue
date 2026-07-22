<template>
  <div>
    <yu-upload
      action="http://yusp-mall.oss-cn-chengdu.aliyuncs.com"
      :data="dataObj"
      list-type="picture-card"
      :file-list="fileList"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-preview="handlePreview"
      :limit-number="maxCount"
      :on-exceed="handleExceed"
      :multiple="true"
    >
      <i class="el-icon-plus"></i>
    </yu-upload>
    <yu-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt />
    </yu-dialog>
  </div>
</template>
<script>
// import { policy } from "./policy";
// import { getUUID } from "@/utils";
export default {
  name: "multiUpload",
  props: {
    //图片属性数组
    value: Array,
    //最大上传图片数量
    maxCount: {
      type: Number,
      default: 30,
    },
  },
  data() {
    return {
      dataObj: {
        policy: "",
        signature: "",
        key: "",
        ossaccessKeyId: "",
        dir: "",
        host: "",
        uuid: "",
      },
      dialogVisible: false,
      dialogImageUrl: null,
    };
  },
  computed: {
    fileList() {
      const fileList = [];
      for (let i = 0; i < this.value.length; i++) {
        fileList.push({ url: this.value[i] });
      }

      return fileList;
    },
  },
  mounted() {},
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
          url: "/api/thirdparty/oss/policy",
        }).then((response) => {
          // console.log(response);
          resolve(response);
        });
      });
    },
    emitInput(fileList) {
      const value = [];
      for (let i = 0; i < fileList.length; i++) {
        value.push(fileList[i].url);
      }
      this.$emit("input", value);
    },
    handleRemove(file, fileList) {
      this.emitInput(fileList);
    },
    handlePreview(file) {
      this.dialogVisible = true;
      this.dialogImageUrl = file.url;
    },
    beforeUpload(file) {
      // let _self = this;
      return new Promise((resolve, reject) => {
        this.policy()
          .then((response) => {
            console.log("这是什么${filename}");
            this.dataObj.policy = response.data.policy;
            this.dataObj.signature = response.data.signature;
            this.dataObj.ossaccessKeyId = response.data.accessid;
            this.dataObj.key = response.data.dir + "/" + this.getUUID() + "_${filename}";
            this.dataObj.dir = response.data.dir;
            this.dataObj.host = response.data.host;
            resolve(true);
          })
          .catch((err) => {
            console.log("出错了...", err);
            reject(false);
          });
      });
    },
    handleUploadSuccess(res, file) {
      this.fileList.push({
        name: file.name,
        // url: this.dataObj.host + "/" + this.dataObj.dir + "/" + file.name； 替换${filename}为真正的文件名
        url: this.dataObj.host + "/" + this.dataObj.key.replace("${filename}", file.name),
      });
      this.emitInput(this.fileList);
    },
    handleExceed(files, fileList) {
      this.$message({
        message: "最多只能上传" + this.maxCount + "张图片",
        type: "warning",
        duration: 1000,
      });
    },
  },
};
</script>
<style></style>
