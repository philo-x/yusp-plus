<template>
  <yu-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    @closed="dialogClose"
  >
    <yu-form :model="dataForm" :rules="dataRule" ref="dataForm" label-width="120px">
      <!--       @keyup.enter.native="dataFormSubmit()" -->
      <yu-form-item label="属性名" prop="attrName">
        <yu-input v-model="dataForm.attrName" placeholder="属性名"></yu-input>
      </yu-form-item>
      <yu-form-item label="属性类型" prop="attrType">
        <yu-select v-model="dataForm.attrType" placeholder="请选择">
          <yu-option label="规格参数" :value="1"></yu-option>
          <yu-option label="销售属性" :value="0"></yu-option>
        </yu-select>
      </yu-form-item>

      <yu-form-item label="值类型" prop="valueType">
        <yu-switch
          v-model="dataForm.valueType"
          on-text="允许多个值"
          off-text="只能单个值"
          on-color="#13ce66"
          off-color="#ff4949"
          :off-value="0"
          :on-value="1"
        ></yu-switch>
      </yu-form-item>
      <yu-form-item label="可选值" prop="valueSelect">
        <!-- <el-input v-model="dataForm.valueSelect"></el-input> -->
        <yu-select v-model="dataForm.valueSelect" multiple filterable allow-create placeholder="请输入内容"></yu-select>
      </yu-form-item>
      <yu-form-item label="属性图标" prop="icon">
        <yu-input v-model="dataForm.icon" placeholder="属性图标"></yu-input>
      </yu-form-item>
      <yu-form-item label="所属分类" prop="catelogId">
        <category-cascader :catelogPath.sync="catelogPath"></category-cascader>
      </yu-form-item>
      <yu-form-item label="所属分组" prop="attrGroupId" v-if="type == 1">
        <yu-select ref="groupSelect" v-model="dataForm.attrGroupId" placeholder="请选择">
          <yu-option
            v-for="item in attrGroups"
            :key="item.attrGroupId"
            :label="item.attrGroupName"
            :value="item.attrGroupId"
          ></yu-option>
        </yu-select>
      </yu-form-item>
      <yu-form-item label="可检索" prop="searchType" v-if="type == 1">
        <yu-switch
          v-model="dataForm.searchType"
          on-color="#13ce66"
          off-color="#ff4949"
          :on-value="1"
          :off-value="0"
        ></yu-switch>
      </yu-form-item>
      <yu-form-item label="快速展示" prop="showDesc" v-if="type == 1">
        <yu-switch
          v-model="dataForm.showDesc"
          on-color="#13ce66"
          off-color="#ff4949"
          :on-value="1"
          :off-value="0"
        ></yu-switch>
      </yu-form-item>
      <yu-form-item label="启用状态" prop="enable">
        <yu-switch
          v-model="dataForm.enable"
          on-color="#13ce66"
          off-color="#ff4949"
          :on-value="1"
          :off-value="0"
        ></yu-switch>
      </yu-form-item>
    </yu-form>
    <span slot="footer" class="dialog-footer">
      <yu-button @click="visible = false">取消</yu-button>
      <yu-button type="primary" @click="dataFormSubmit()">确定</yu-button>
    </span>
  </yu-dialog>
</template>

<script>
import CategoryCascader from "../common/category-cascader";
export default {
  components: { CategoryCascader },
  props: {
    type: {
      type: Number,
      default: 1,
    },
  },
  data() {
    return {
      visible: false,
      dataForm: {
        attrId: 0,
        attrName: "",
        searchType: 0,
        valueType: 1,
        icon: "",
        valueSelect: "",
        attrType: 1,
        enable: 1,
        catelogId: "",
        attrGroupId: "",
        showDesc: 0,
      },
      catelogPath: [],
      attrGroups: [],
      dataRule: {
        attrName: [{ required: true, message: "属性名不能为空", trigger: "blur" }],
        searchType: [
          {
            required: true,
            message: "是否需要检索不能为空",
            trigger: "blur",
          },
        ],
        valueType: [
          {
            required: true,
            message: "值类型不能为空",
            trigger: "blur",
          },
        ],
        icon: [{ required: true, message: "属性图标不能为空", trigger: "blur" }],
        attrType: [
          {
            required: true,
            message: "属性类型不能为空",
            trigger: "blur",
          },
        ],
        enable: [
          {
            required: true,
            message: "启用状态不能为空",
            trigger: "blur",
          },
        ],
        catelogId: [
          {
            required: true,
            message: "需要选择正确的三级分类数据",
            trigger: "blur",
          },
        ],
        showDesc: [
          {
            required: true,
            message: "快速展示不能为空",
            trigger: "blur",
          },
        ],
      },
    };
  },
  watch: {
    catelogPath(path) {
      //监听到路径变化需要查出这个三级分类的分组信息
      console.log("路径变了", path);
      this.attrGroups = [];
      this.dataForm.attrGroupId = "";
      this.dataForm.catelogId = path[path.length - 1];
      if (path && path.length == 3) {
        this.$request({
          method: "get",
          url: `/api/product/attrgroup/list/${path[path.length - 1]}`,
          params: { page: 1, size: 100000 },
        }).then(({ code, data, message }) => {
          if (code == "0") {
            this.attrGroups = data;
          } else {
            this.$message.error(message);
          }
        });

      } else if (path.length == 0) {
        this.dataForm.catelogId = "";
      } else {
        this.$message.error("请选择正确的分类");
        this.dataForm.catelogId = "";
      }
    },
  },
  methods: {
    init(id) {
      this.dataForm.attrId = id || 0;
      this.dataForm.attrType = this.type;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.attrId) {
          this.$request({
            method: "get",
            url: `/api/product/attr/info/${this.dataForm.attrId}`,
          }).then(({ code, data }) => {
            if (code == "0") {
              this.dataForm.attrName = data.attrName;
              this.dataForm.searchType = data.searchType;
              this.dataForm.valueType = data.valueType;
              this.dataForm.icon = data.icon;
              this.dataForm.valueSelect = data.valueSelect.split(";");
              this.dataForm.attrType = data.attrType;
              this.dataForm.enable = data.enable;
              this.dataForm.catelogId = data.catelogId;
              this.dataForm.showDesc = data.showDesc;
              this.catelogPath = data.catelogPath;
              this.$nextTick(() => {
                this.dataForm.attrGroupId = data.attrGroupId;
              });
            }
          });
        }
      });
    },
    // 表单提交
    dataFormSubmit() {
      // 暂时不做表单校验
      this.$request({
        method: "post",
        url: `/api/product/attr/${!this.dataForm.attrId ? "save" : "update"}`,
        data: {
          attrId: this.dataForm.attrId || undefined,
          attrName: this.dataForm.attrName,
          searchType: this.dataForm.searchType,
          valueType: this.dataForm.valueType,
          icon: this.dataForm.icon,
          valueSelect: this.dataForm.valueSelect.join(";"),
          attrType: this.dataForm.attrType,
          enable: this.dataForm.enable,
          catelogId: this.dataForm.catelogId,
          attrGroupId: this.dataForm.attrGroupId,
          showDesc: this.dataForm.showDesc,
        },
      }).then(({ code, data, message }) => {
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

      // this.$refs["dataForm"].validate((valid) => {
      //   if (valid) {
      //     // 校验通过再发交易
      //   }
      // });
    },
    //dialogClose
    dialogClose() {
      this.catelogPath = [];
    },
  },
};
</script>
