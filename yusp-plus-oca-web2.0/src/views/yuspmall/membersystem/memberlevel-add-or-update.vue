<template>
  <yu-dialog :title="!dataForm.id ? '新增' : '修改'" :close-on-click-modal="false" :visible.sync="visible">
    <yu-form
      :model="dataForm"
      :rules="dataRule"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmit()"
      label-width="120px"
    >
      <yu-form-item label="等级名称" prop="name">
        <yu-input v-model="dataForm.name" placeholder="等级名称"></yu-input>
      </yu-form-item>
      <yu-form-item label="所需成长值" prop="growthPoint">
        <yu-input-number v-model="dataForm.growthPoint" :min="0"></yu-input-number>
      </yu-form-item>
      <yu-form-item label="默认等级" prop="defaultStatus">
        <yu-checkbox v-model="dataForm.defaultStatus" :true-label="1" :false-label="0"></yu-checkbox>
      </yu-form-item>
      <yu-form-item label="免运费标准" prop="freeFreightPoint">
        <yu-input-number :min="0" v-model="dataForm.freeFreightPoint"></yu-input-number>
      </yu-form-item>
      <yu-form-item label="每次评价获取的成长值" prop="commentGrowthPoint">
        <yu-input-number :min="0" v-model="dataForm.commentGrowthPoint"></yu-input-number>
      </yu-form-item>
      <yu-form-item label="是否有免邮特权" prop="priviledgeFreeFreight">
        <yu-checkbox v-model="dataForm.priviledgeFreeFreight" :true-label="1" :false-label="0"></yu-checkbox>
      </yu-form-item>
      <yu-form-item label="是否有会员价格特权" prop="priviledgeMemberPrice">
        <yu-checkbox v-model="dataForm.priviledgeMemberPrice" :true-label="1" :false-label="0"></yu-checkbox>
      </yu-form-item>
      <yu-form-item label="是否有生日特权" prop="priviledgeBirthday">
        <yu-checkbox v-model="dataForm.priviledgeBirthday" :true-label="1" :false-label="0"></yu-checkbox>
      </yu-form-item>
      <yu-form-item label="备注" prop="note">
        <yu-input v-model="dataForm.note" placeholder="备注"></yu-input>
      </yu-form-item>
    </yu-form>
    <span slot="footer" class="dialog-footer">
      <yu-button @click="visible = false">取消</yu-button>
      <yu-button type="primary" @click="dataFormSubmit()">确定</yu-button>
    </span>
  </yu-dialog>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      dataForm: {
        id: 0,
        name: "",
        growthPoint: 0,
        defaultStatus: 0,
        freeFreightPoint: 0,
        commentGrowthPoint: 0,
        priviledgeFreeFreight: 0,
        priviledgeMemberPrice: 0,
        priviledgeBirthday: 0,
        note: "",
      },
      dataRule: {
        name: [{ required: true, message: "等级名称不能为空", trigger: "blur" }],
        growthPoint: [{ required: true, message: "等级需要的成长值不能为空", trigger: "blur" }],
        defaultStatus: [{ required: true, message: "是否为默认等级[0->不是；1->是]不能为空", trigger: "blur" }],
        freeFreightPoint: [{ required: true, message: "免运费标准不能为空", trigger: "blur" }],
        commentGrowthPoint: [{ required: true, message: "每次评价获取的成长值不能为空", trigger: "blur" }],
        priviledgeFreeFreight: [{ required: true, message: "是否有免邮特权不能为空", trigger: "blur" }],
        priviledgeMemberPrice: [{ required: true, message: "是否有会员价格特权不能为空", trigger: "blur" }],
        priviledgeBirthday: [{ required: true, message: "是否有生日特权不能为空", trigger: "blur" }],
        note: [{ required: true, message: "备注不能为空", trigger: "blur" }],
      },
    };
  },
  methods: {
    init(id) {
      this.dataForm.id = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.id) {
          this.$request({
            method: "get",
            url: `/api/member/memberlevel/info/${this.dataForm.id}`,
          }).then(({ code, data }) => {
            if (code == "0") {
              this.dataForm.name = data.name;
              this.dataForm.growthPoint = data.growthPoint || 0;
              this.dataForm.defaultStatus = data.defaultStatus || 0;
              this.dataForm.freeFreightPoint = data.freeFreightPoint || 0;
              this.dataForm.commentGrowthPoint = data.commentGrowthPoint || 0;
              this.dataForm.priviledgeFreeFreight = data.priviledgeFreeFreight || 0;
              this.dataForm.priviledgeMemberPrice = data.priviledgeMemberPrice || 0;
              this.dataForm.priviledgeBirthday = data.priviledgeBirthday || 0;
              this.dataForm.note = data.note;
            }
          });
        }
      });
    },
    // 表单提交
    dataFormSubmit() {
      // 暂时绕过表单校验
      this.$request({
        method: "post",
        url: `/api/member/memberlevel/${!this.dataForm.id ? "save" : "update"}`,
        data: {
          id: this.dataForm.id || undefined,
          name: this.dataForm.name,
          growthPoint: this.dataForm.growthPoint,
          defaultStatus: this.dataForm.defaultStatus,
          freeFreightPoint: this.dataForm.freeFreightPoint,
          commentGrowthPoint: this.dataForm.commentGrowthPoint,
          priviledgeFreeFreight: this.dataForm.priviledgeFreeFreight,
          priviledgeMemberPrice: this.dataForm.priviledgeMemberPrice,
          priviledgeBirthday: this.dataForm.priviledgeBirthday,
          note: this.dataForm.note,
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
      //     // 请求代码移到了外部，暂时绕过表单校验过不去的问题，等框架组解决
      //   }
      // });
    },
  },
};
</script>
