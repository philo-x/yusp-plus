<template>
  <el-dialog :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <el-form
      :model="dataForm"
      :rules="dataRule"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmit()"
      label-width="120px"
    >
      <el-form-item label="sku_id" prop="skuId">
        <el-input v-model="dataForm.skuId" placeholder="sku_id"></el-input>
      </el-form-item>
      <el-form-item label="仓库" prop="wareId">
        <el-select v-model="dataForm.wareId" placeholder="请选择仓库" clearable>
          <el-option :label="w.name" :value="w.id" v-for="w in wareList" :key="w.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="库存数" prop="stock">
        <el-input v-model="dataForm.stock" placeholder="库存数"></el-input>
      </el-form-item>
      <el-form-item label="sku_name" prop="skuName">
        <el-input v-model="dataForm.skuName" placeholder="sku_name"></el-input>
      </el-form-item>
      <el-form-item label="锁定库存" prop="stockLocked">
        <el-input v-model="dataForm.stockLocked" placeholder="锁定库存"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      wareList: [],
      dataForm: {
        id: 0,
        skuId: "",
        wareId: "",
        stock: 0,
        skuName: "",
        stockLocked: 0,
      },
      dataRule: {
        skuId: [{ required: true, message: "sku_id不能为空", trigger: "blur" }],
        wareId: [{ required: true, message: "仓库id不能为空", trigger: "blur" }],
        stock: [{ required: true, message: "库存数不能为空", trigger: "blur" }],
        skuName: [{ required: true, message: "sku_name不能为空", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getWares();
  },
  methods: {
    getWares() {
      this.$request({
        method: "get",
        url: "/api/ware/wareinfo/list",
        params: { page: 1, size: 500 },
      }).then(({ code, data }) => {
        if (code == "0") {
          this.wareList = data;
        }
      });
    },
    init(id) {
      this.dataForm.id = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.id) {
          this.$request({
            url: `/api/ware/waresku/info/${this.dataForm.id}`,
            method: "get",
          }).then(({ code, data }) => {
            if (code == "0") {
              this.dataForm.skuId = data.skuId;
              this.dataForm.wareId = data.wareId;
              this.dataForm.stock = data.stock;
              this.dataForm.skuName = data.skuName;
              this.dataForm.stockLocked = data.stockLocked;
            }
          });
        }
      });
    },
    // 表单提交
    dataFormSubmit() {
      // 表单校验暂时绕过

      this.$request({
        url: `/api/ware/waresku/${!this.dataForm.id ? "save" : "update"}`,
        method: "post",
        data: {
          id: this.dataForm.id || undefined,
          skuId: this.dataForm.skuId,
          wareId: this.dataForm.wareId,
          stock: this.dataForm.stock,
          skuName: this.dataForm.skuName,
          stockLocked: this.dataForm.stockLocked,
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

      // this.$refs["dataForm"].validate((valid) => {
      //   if (valid) {

      //   }
      // });
    },
  },
};
</script>
