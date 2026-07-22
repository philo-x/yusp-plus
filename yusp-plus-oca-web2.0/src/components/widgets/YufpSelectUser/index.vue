<!--
 * @FileDescription: 使用查询表组件封装的获取填充组件
 * @Author: lvzl
 * @Date: 2020/09/25
 * @LastEditors: lvzl
 * 选项说明 见查询表组件的选项说明
 -->
<template>
  <div>
    <yu-input v-model="rawValue" readonly :on-icon-click="openSelector" :placeholder="inputPlaceholder" icon="search" :disabled="inputDisabled"></yu-input>
    <yu-xdialog :title="title" :visible.sync="dialogVisible" :width="width">
      <yufp-frame-list
        ref="refFameList"
        :item-attr="itemAttr"
        :x-table-attr="xTableAttr"
        :no-btn-arr="noBtnArr"
        :xform-attr="xformAttr"
      ></yufp-frame-list>
      <div slot="footer">
        <el-button @click="selectAndReturn" type="primary">选取返回</el-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import { clone } from '@/utils'
export default {
  name: 'YufpSelectUser',
  componentName: 'YufpSelectUser',
  props: {
    dialogWidth: String,
    tranData: Object,
    placeholder: String,
    disabled: Boolean,
    value: String,
  },
  data: function() {
    return {
      title: '选择法人信息',
      xformAttr: {
        labelWidth: '120px',
        column: 3
      },
      width: this.dialogWidth || '650px',
      inputDisabled: this.disabled,
      rawValue: '',
      itemAttr: [
        { label: '法人姓名', prop: 'id', sortable: 'custom', itemtype: 'input', isTFiled: true},
        { label: '法人性别', itemtype: 'select', isQFiled: true, isTFiled: true, prop: 'gender', dataCode: 'GENDER'},
        { label: '法人出生日期', prop: 'createAt', itemtype: 'datepicker', isTFiled: true, isQFiled: true}
      ],
      inputPlaceholder: this.placeholder,
      // 对话框是否可见变量
      dialogVisible: false,
      // 增删改查按钮默认提供, 不需要哪个可配置此选项隐藏['add', 'edit', 'info', 'delete'],不配置或改配置配置为空，显示全部
      noBtnArr: ['add', 'edit', 'info', 'delete'],
      // 弹出框的width, title属性配置。
      // 弹出框form配置，
      xTableAttr: { // 可在el-table-x上配置的属性都可以在这里配置, 详细可参考UI组件库对应xtablex的配置说明
        selectionType: 'radio',
        dataUrl: '/trade/example/list'
      },
    };
  },
  watch: {
    value: function (newVal, oldVal) {
      this.rawValue = newVal;
    },
    rawValue: function (newVal, oldVal) {
      this.$emit('input', newVal);
    },
    disabled: function (newVal, oldVal) {
      this.inputDisabled = newVal;
    },
    placeholder: function (newVal, oldVal) {
      this.inputPlaceholder = newVal;
    },
    tranData: {
      handler: function (val, oldVal) {
        clone(val, this.xTableAttr.baseParams);
      },
      deep: true
    }
  },
  created: function () {
    this.rawValue = this.rawValue ? this.rawValue : this.value;
  },
  methods: {
    selectAndReturn: function () {
      const obj = this.$refs.refFameList.getSelections();
      if (obj.length > 0) {
        this.$emit('select-fn', obj);
        this.dialogVisible = false;
      } else {
        this.$message({ message: '请选择一条记录', type: 'warning' });
      }
      
    },
    // 用于打开选择框
    openSelector: function () {
      var _this = this;
      if (_this.inputDisabled === false) {
        _this.dialogVisible = true;
        _this.$nextTick(function () {
        });
      }
    },
    saveFn: function(obj) {
      // console.log(this.$refs.refFameList);
    },
    btnClick: function(obj) {
      /**
      obj.btn 可以知道点击的哪个按钮, selections是选中的数据
      {selections: Array(1), btn: "bbb"}
        btn: "bbb"
        selections: Array(1)
        0: {id: 68, title: "与划走进准着政与增以", createAt: "1979-01-21", author: "周艳", auditor: "丁娜", …}
        length: 1
        __proto__: Array(0)
        __proto__: Object
       */
    }
  }
}
</script>
