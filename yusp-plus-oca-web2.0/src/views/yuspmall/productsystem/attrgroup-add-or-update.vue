<template>
  <yu-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    @closed="dialogClose"
  >
    <yu-form
      :model="dataForm"
      :rules="dataRule"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmit()"
      label-width="120px"
    >
      <yu-form-item label="组名" prop="attrGroupName">
        <yu-input v-model="dataForm.attrGroupName" placeholder="组名"></yu-input>
      </yu-form-item>
      <yu-form-item label="排序" prop="sort">
        <yu-input v-model="dataForm.sort" placeholder="排序"></yu-input>
      </yu-form-item>
      <yu-form-item label="描述" prop="descript">
        <yu-input v-model="dataForm.descript" placeholder="描述"></yu-input>
      </yu-form-item>
      <yu-form-item label="组图标" prop="icon">
        <yu-input v-model="dataForm.icon" placeholder="组图标"></yu-input>
      </yu-form-item>
      <yu-form-item label="所属分类" prop="catelogId">
        <!-- <yu-input v-model="dataForm.catelogId" placeholder="所属分类id"></yu-input> -->
        <!-- <yu-cascader filterable placeholder="试试搜索：手机" v-model="catelogPath" :options="categorys" :props="props"></yu-cascader> -->
        <!-- :catelogPath="catelogPath"自定义绑定的属性，可以给子组件传值 -->
        <category-cascader :catelogPath.sync="catelogPath"></category-cascader>
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
  data() {
    return {
      props: {
        value: "catId",
        label: "name",
        children: "children",
      },
      visible: false,
      categorys: [],
      catelogPath: [],
      dataForm: {
        attrGroupId: 0,
        attrGroupName: "",
        sort: "",
        descript: "",
        icon: "",
        catelogId: 0,
      },
      dataRule: {
        attrGroupName: [{ required: true, message: "组名不能为空", trigger: "blur" }],
        sort: [{ required: true, message: "排序不能为空", trigger: "blur" }],
        descript: [{ required: true, message: "描述不能为空", trigger: "blur" }],
        icon: [{ required: true, message: "组图标不能为空", trigger: "blur" }],
        // catelogId: [{ required: true, message: "所属分类id不能为空", trigger: "blur" }],
      },
    };
  },

  created() {
    this.getCategorys();
  },

  methods: {
    dialogClose() {
      console.log("当关闭时，catelogPath=", this.catelogPath);
      this.catelogPath = [];
    },
    getCategorys() {
      this.$request({
        url: "/api/product/category/list/tree",
        method: "get",
      }).then(({ code, data }) => {
        if (code == "0") {
          // console.log("获取菜单数据：", data);
          this.categorys = data;
        }
      });
    },
    init(id) {
      this.dataForm.attrGroupId = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.attrGroupId) {
          this.$request({
            url: `/api/product/attrgroup/info/${this.dataForm.attrGroupId}`,
            method: "get",
          }).then(({ code, data }) => {
            if (code == "0") {
              this.dataForm.attrGroupName = data.attrGroupName;
              this.dataForm.sort = data.sort + ""; // 加空串临时解决yu-form表单校验的bug
              this.dataForm.descript = data.descript;
              this.dataForm.icon = data.icon;
              this.dataForm.catelogId = data.catelogId;
              //查出catelogId的完整路径
              this.catelogPath = data.catelogPath;
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
            url: `/api/product/attrgroup/${!this.dataForm.attrGroupId ? "save" : "update"}`,
            method: "post",
            data: {
              attrGroupId: this.dataForm.attrGroupId || undefined,
              attrGroupName: this.dataForm.attrGroupName,
              sort: this.dataForm.sort,
              descript: this.dataForm.descript,
              icon: this.dataForm.icon,
              catelogId: this.catelogPath[this.catelogPath.length - 1],
            },
          }).then(({ code, data }) => {
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
              this.$message.error(data.msg);
            }
          });
        }
      });
    },
  },
};
</script>
