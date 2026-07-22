
<template>
  <!--
    @created by
    @updated by taoting1 2018-8-16 修改代码规范
    @updated by luoshun 2018-10-11 更新组件内容及标签
    @description 模板示例——普通查询
  -->
  <div>
    <yu-panel title="普通查询" panel-type="simple">
      <template slot="right">
        <yu-button-drop>
          <yu-button @click="addFn">新增</yu-button>
          <yu-button @click="modifyFn">修改</yu-button>
          <yu-button @click="infoFn">详情</yu-button>
          <yu-button @click="deleteFn">删除</yu-button>
        
        </yu-button-drop>
      </template>
      <!-- <template slot="filter">
        <yu-xform related-table-name="refTable" form-type="search" v-model="searchFormdata" label-width="60px">
          <yu-xform-group :column="4">
            <yu-xform-item label="标题" placeholder="标题" ctype="input" name="title"></yu-xform-item>
            <yu-xform-item label="时间" placeholder="时间" ctype="datepicker" name="createAt"></yu-xform-item>
            <yu-xform-item label="类型" placeholder="类型" ctype="select" name="type" data-code="NATIONALITY"></yu-xform-item>
          </yu-xform-group>
        </yu-xform>
      </template> -->
      <yu-xtable ref="refTable" row-number :data-url="dataUrl" request-type="POST" selection-type="checkbox">
        <yu-xtable-column label="编码" prop="userId"></yu-xtable-column>
        <yu-xtable-column label="客户名称" prop="userName"></yu-xtable-column>
        <yu-xtable-column label="性别" prop="userSex" data-code="SEX_TYPE"></yu-xtable-column>
        <yu-xtable-column label="联系方式" prop="userPhone"></yu-xtable-column>
        <yu-xtable-column label="客户地址" prop="userAddress"></yu-xtable-column>
        <yu-xtable-column label="客户经理" prop="userManager"></yu-xtable-column>
        <yu-xtable-column label="创建时间" prop="createTime"></yu-xtable-column>
        <yu-xtable-column label="维护时间" prop="updateTime"></yu-xtable-column>
      </yu-xtable>
    </yu-panel>
    <!-- 说明dialog可配置宽高属性，若不配置则宽度默认为屏幕50%，高度自适应 width="650px" height="380px" -->
    <yu-xdialog :title="viewTitle[viewType]" :visible.sync="dialogVisible" width="650px">
      <yu-xform ref="refForm" label-width="100px" v-model="formdata" :disabled="formDisabled">
        <yu-xform-group>
          <yu-xform-item label="编码" ctype="num" name="userId" :hidden="true"></yu-xform-item>
          <yu-xform-item label="客户名称" ctype="input" name="userName" :rules="rule[0]"></yu-xform-item>
          <yu-xform-item label="性别" ctype="select" name="userSex" data-code="SEX_TYPE"></yu-xform-item>
          <yu-xform-item label="联系方式" ctype="input" name="userPhone"></yu-xform-item>
          <yu-xform-item label="客户地址" ctype="input" name="userAddress"></yu-xform-item>
          <yu-xform-item label="客户经理" ctype="input" name="userManager"></yu-xform-item>
          <!-- <yu-xform-item label="创建时间" ctype="datepicker" name="createTime"></yu-xform-item>
          <yu-xform-item label="维护时间" ctype="datepicker" name="updateTime"></yu-xform-item> -->
          <!-- <yu-xform-item label="阅读数" ctype="input" name="pageviews" :rules="rule[1]"></yu-xform-item>
          <yu-xform-item label="自定义" ctype="yufp-demo-selector" name="yourField"></yu-xform-item> -->
          <yu-xform-item label="备注" ctype="textarea" name="userDesc" :rows="3" :colspan="24" placeholder="2000个字符以内"></yu-xform-item>
        </yu-xform-group>
        <div class="yu-grpButton">
          <yu-button v-show="saveBtnShow" type="primary" @click="saveFn">保存</yu-button>
          <yu-button @click="cancelFn">取消</yu-button>
        </div>
      </yu-xform>
    </yu-xdialog>
  </div>
</template>
<script>
/* eslint vue/no-unused-components:0 */
import YufpDemoSelector from "@/components/widgets/YufpDemoSelector";
import { lookup, clone } from '@/utils';
import { validator } from "@/utils/validate";
import { exportExcelByTable } from '@/utils/util'
lookup.reg("CRUD_TYPE,SEX_TYPE");

export default {
  components: { YufpDemoSelector },
  data () {
    return {
      searchFormdata: {},
      dataUrl: "/api/ocaworkboard/list",
      formdata: {},
      rule: [
        {
          required: true,
          message: "必填项",
          trigger: "blur"
        },
        {
          validator: validator.number,
          message: "数字",
          trigger: "blur"
        }
      ],
      dialogVisible: false,
      formDisabled: false,
      viewType: "DETAIL",
      viewTitle: lookup.find("CRUD_TYPE", false),
      saveBtnShow: true
    };
  },
  methods: {
    /**
     * 取消
     */
    cancelFn () {
      const _this = this;

      _this.dialogVisible = false;
    },

    /**
     * 保存
     */
    saveFn () {
      const _this = this;

      const model = {};
      clone(_this.formdata, model);
      let validate = false;

      _this.$refs.refForm.validate(function(valid) {
        validate = valid;
      });

      if (!validate) {
        return;
      } // 向后台发送保存请求
      console.log(_this.viewType);
      const _url = _this.viewType == 'ADD' ? "/api/ocaworkboard/save" : "/api/ocaworkboard/update"
      this.$request({
        method: "POST",
        url: _url,
        data: model
      }).then((code, message, response) => {
        //处理请求成功的情况
        _this.$refs.refTable.remoteData();
        _this.$message("操作成功");
        _this.dialogVisible = false;
      })
    },

    /**
     * 控制保存按钮、xdialog、表单的状态
     * @param viewType 表单类型
     * @param editable 可编辑,默认false
     */
    switchStatus (viewType, editable) {
      const _this = this;

      _this.viewType = viewType;
      _this.saveBtnShow = editable;
      _this.dialogVisible = true;
      _this.formDisabled = !editable; 
    },

    /**
     * 新增按钮
     */
    addFn() {
      const _this = this;

      _this.switchStatus("ADD", true);

      _this.$nextTick(function() {
        _this.$refs.refForm.resetFields();
      });
    },

    /**
     * 修改
     */
    modifyFn() {
      const _this = this;

      if (_this.$refs.refTable.selections.length !== 1) {
        _this.$message({
          message: "请先选择一条记录",
          type: "warning"
        });

        return;
      }

      _this.switchStatus("EDIT", true);

      _this.$nextTick(function() {
        _this.$refs.refForm.resetFields();

        const obj = _this.$refs.refTable.selections[0];
        clone(obj, _this.formdata);
      });
    },

    /**
     * 详情
     */
    infoFn() {
      const _this = this;

      const selectionsAry = _this.$refs.refTable.selections;

      if (selectionsAry.length !== 1) {
        _this.$message({
          message: "请先选择一条记录",
          type: "warning"
        });

        return;
      }

      _this.switchStatus("DETAIL", false);

      _this.$nextTick(function() {
        _this.$refs.refForm.resetFields();

        clone(selectionsAry[0], _this.formdata);
      });
    },

    /**
     * 删除
     */
    deleteFn() {
      const _this = this;

      const selections = _this.$refs.refTable.selections;

      if (selections.length < 1) {
        _this.$message({
          message: "请先选择一条记录",
          type: "warning"
        });

        return;
      }

      const len = selections.length,
        arr = [];

      for (let i = 0; i < len; i++) {
        arr.push(selections[i].userId);
      }
      

      _this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        callback: function(action) {
          if (action === "confirm") {
            _this.$request({
              method: "POST",
              url: "/api/ocaworkboard/delete",
              data: arr.join(',')
            }).then((code, message, response) => {
              //处理请求成功的情况
              _this.$refs.refTable.remoteData();
              _this.$message("操作成功");
            })
          }
        }
      });
    },

    /**
     * 导出操作
     */
    exportFn() {
      const _this = this;

      exportExcelByTable({
        fileName: "导出",
        importType: "service",
        ref: _this.$refs.refTable,
        url: "/trade/example/list"
      });
    }
  }
};
</script>
