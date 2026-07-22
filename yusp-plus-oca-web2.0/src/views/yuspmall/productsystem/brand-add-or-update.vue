<template>
  <yu-dialog :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <yu-form
      :model="dataForm"
      :rules="dataRule"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmit()"
      label-width="140px"
    >
      <yu-form-item label="品牌名" prop="name">
        <yu-input v-model="dataForm.name" placeholder="品牌名"></yu-input>
      </yu-form-item>
      <yu-form-item label="品牌logo地址" prop="logo">
        <!-- <yu-input v-model="dataForm.logo" placeholder="品牌logo地址"></yu-input> -->
        <single-upload v-model="dataForm.logo" @input="inputEvent($event)"></single-upload>
      </yu-form-item>
      <yu-form-item label="介绍" prop="descript">
        <yu-input v-model="dataForm.descript" placeholder="介绍"></yu-input>
      </yu-form-item>
      <yu-form-item label="显示状态" prop="showStatus">
        <yu-switch
          v-model="dataForm.showStatus"
          on-color="#13ce66"
          off-color="#ff4949"
          :on-value="1"
          :off-value="0"
        ></yu-switch>
      </yu-form-item>
      <yu-form-item label="检索首字母" prop="firstLetter">
        <yu-input v-model="dataForm.firstLetter" placeholder="检索首字母"></yu-input>
      </yu-form-item>
      <yu-form-item label="排序" prop="sort">
        <yu-input v-model.number="dataForm.sort" placeholder="排序"></yu-input>
      </yu-form-item>
    </yu-form>
    <span slot="footer" class="dialog-footer">
      <yu-button @click="visible = false">取消</yu-button>
      <yu-button type="primary" @click="dataFormSubmit()">确定</yu-button>
    </span>
  </yu-dialog>
</template>

<script>
import SingleUpload from "@/components/upload/singleUpload";
export default {
  components: { SingleUpload },
  data() {
    return {
      visible: false,
      dataForm: {
        brandId: 0,
        name: "",
        logo: "",
        descript: "",
        showStatus: 1,
        firstLetter: "",
        sort: 0,
      },
      dataRule: {
        name: [{ required: true, message: "品牌名不能为空", trigger: "blur" }],
        logo: [{ required: true, message: "品牌logo地址不能为空", trigger: "blur" }],
        descript: [{ required: true, message: "介绍不能为空", trigger: "blur" }],
        // showStatus: [
        //   {
        //     required: true,
        //     message: "显示状态[0-不显示；1-显示]不能为空",
        //     trigger: "blur",
        //   },
        // ],
        firstLetter: [
          {
            validator: (rule, value, callback) => {
              if (value == "") {
                callback(new Error("首字母必须填写"));
              } else if (!/^[a-zA-Z]$/.test(value)) {
                callback(new Error("首字母必须a-z或者A-Z之间"));
              } else {
                callback();
              }
            },
            trigger: "blur",
          },
        ],
        sort: [
          {
            validator: (rule, value, callback) => {
              console.log("sort validator,value:", value);
              if (value == "") {
                callback(new Error("排序字段必须填写"));
              } else if (!Number.isInteger(value) || value < 0) {
                callback(new Error("排序必须是一个大于等于0的整数"));
              } else {
                callback();
              }
            },
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    init(id) {
      this.dataForm.brandId = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.brandId) {
          this.$request({
            method: "get",
            url: `/api/product/brand/info/${this.dataForm.brandId}`,
          }).then(({ code, data }) => {
            if (code == "0") {
              this.dataForm.name = data.name;
              this.dataForm.logo = data.logo;
              this.dataForm.descript = data.descript;
              this.dataForm.showStatus = data.showStatus;
              this.dataForm.firstLetter = data.firstLetter;
              this.dataForm.sort = data.sort;
            }
          });
        }
      });
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          this.$request({
            method: "post",
            url: `/api/product/brand/${!this.dataForm.brandId ? "save" : "update"}`,
            data: {
              brandId: this.dataForm.brandId || undefined,
              name: this.dataForm.name,
              logo: this.dataForm.logo,
              descript: this.dataForm.descript,
              showStatus: this.dataForm.showStatus,
              firstLetter: this.dataForm.firstLetter,
              sort: this.dataForm.sort,
            },
          }).then(({ code, message }) => {
            if (code == "0") {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1500,
                onClose: () => {
                  this.visible = false;
                  this.$emit("refreshDataList");
                },
              });
            } else {
              this.$message.error(message);
            }
          });
        }
      });
    },
    inputEvent(msg) {
      console.log("文件上传陈公公~~~", msg);
    },
  },
};
</script>
