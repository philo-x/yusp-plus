<template>
  <div>
    <yu-input :icon="icon" :placeholder="placeholder" :disabled="disabled" :on-icon-click="onIconClickFn" v-model="selectedVal" @focus="focusFn" @blur="icon='search'"></yu-input>
    <yu-xdialog :title="dialogTitle" :visible.sync="dialogVisible">
      <yu-xform ref="searchForm" related-table-name="dptTable" v-model="searchFormdata" form-type="search" @form-reset="clearOrgIdFn">
        <yu-xform-group :column="4">
          <yu-xform-item name="orgName" :label="$t('dptManager.ssjg')" :placeholder="$t('dptManager.qxz')" ctype="custom">
            <yu-combo-tree ref="searchTree" v-model="searchOrgName" :placeholder="$t('dptManager.qxz')" :data-url="orgTreeUrl"
                           data-id="orgId" data-label="orgName" data-pid="upOrgId" :clearable="true" @node-click="changeOrgIdFn" @clear="clearOrgIdFn" :filter-node-method="filterNode">
            </yu-combo-tree>
          </yu-xform-item>
          <yu-xform-item name="dptCode" :label="$t('dptManager.bmdm')" :placeholder="$t('dptManager.qsr')"></yu-xform-item>
          <yu-xform-item name="dptName" :label="$t('dptManager.bmmc')" :placeholder="$t('dptManager.qsr')"></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
      <yu-xtable request-type="POST" ref="dptTable" row-number :data-url="dptTableUrl" :selection-type="selectionType" @row-click="selectFn">
        <yu-xtable-column :label="$t('dptManager.bmmc')" prop="dptName"></yu-xtable-column>
        <yu-xtable-column :label="$t('dptManager.bmdm')" prop="dptCode"></yu-xtable-column>
        <yu-xtable-column :label="$t('dptManager.ssjg')" prop="orgName"> </yu-xtable-column>
        <yu-xtable-column :label="$t('dptManager.zt')" prop="dptSts" data-code="DATA_STS"></yu-xtable-column>
      </yu-xtable>
      <div v-if="selectionType === 'checkbox'" slot="footer" class="dialog-footer" align="center">
        <yu-button type="primary" @click="confirmFn">{{ $t('app.component.btn_confirm') }}</yu-button>
        <yu-button @click="dialogVisible = false">{{ $t('app.component.btn_cancel') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import { extend } from '@/utils'
export default {
  name: 'YufpDptSelector',
  componentName: 'YufpDptSelector',
  props: {
    detailsValue: {
      type: String,
      default: ''
    },
    details: {
      type: Boolean,
      default: true
    },
    selectValue: {
      type: String,
      default: ''
    },
    disabled: Boolean,
    params: Object
  },
  data: function () {
    return this.createData();
  },
  watch: {
    details: {
      handler: function (newVal, oldVal) {
        this.value = this.selectValue;
      },
      immediate: true
    },
    selectValue: {
      handler: function (newVal) {
        this.value = newVal;
      },
      immediate: true
    },
    searchOrgName: function (val) {
      this.$refs.searchTree.filter(val);
    },
    params: function (val) {
      var _this = this;
      var temp = _this.createData();
      extend(true, _this, val);
      _this.dataParams = temp.dataParams;
    }
  },
  // 挂载后
  mounted: function () {
  },
  methods: {
    createData: function () {
      var _this = this;
      var temp = _this.getDefaultData();
      // 深度拷贝
      extend(true, temp, _this.params);
      return temp;
    },
    getDefaultData: function () {
      return {
        searchOrgName: '', //
        dptTableUrl: backend.appOcaService + '/api/adminsmdpt/page', // 部门列表查询地址
        icon: 'search', // 输入框图标
        selectedVal: '', // 选中的值
        dialogVisible: false, // 弹出框是否显示
        searchFormdata: {}, // 查询表单数据
        orgTreeUrl: backend.appOcaService + '/api/adminsmorg/treequeryauth', // 机构树请求 url
        dialogTitle: '', // 弹出框标题
        // 展示参数
        searchKey: '', // 树过滤参数
        value: '3',
        placeholder: '',
        // 数据参数
        dataParams: {
        },
        selectionType: 'checkbox' // 表格选择类型 默认多选
      };
    },
    focusFn: function () {
      if (this.selectedVal != '') {
        this.icon = 'circle-close';
        return;
      }
      this.dialogVisible = false;
    },
    /**
    * 通过所属机构查询
    * @param node 点击的节点信息
    */
    changeOrgIdFn: function (node) {
      this.searchFormdata.orgId = node.orgId;
      this.searchOrgName = node.orgId;
      this.$emit('node-click', node);
    },
    /**
    * 清空所属机构查询条件
    */
    clearOrgIdFn: function () {
      this.searchFormdata.orgId = '';
      this.searchOrgName = '';
    },
    /**
    * 机构树搜索
    * @param data 当前输入信息
    */
    filterNode: function (value, data, node) {
      if (!value) {
        return true;
      }
      return data.orgName.indexOf(value) !== -1;
    },
    clearFn: function () {
      this.$refs.tree.filter('');
    },
    /**
    *清除查询参数,当修改url的时候调用
    */
    clearSearchParams: function () {
      this.dataParams = {};
    },
    /**
    * 点击输入框图标
    */
    onIconClickFn: function (val) {
      if (this.selectedVal === '') {
        this.dialogVisible = true;
      } else {
        this.selectedVal = '';
        this.$emit('clear');
      }
    },
    /**
    * 确定按钮选择数据
    */
    confirmFn: function () {
      var selections = this.$refs.dptTable.selections;
      if (selections.length < 1 && this.selectionType === 'checkbox') {
        this.$message({
          message: this.$t('dptManager.qxxzytjl'),
          type: 'warning'
        });
        return;
      }
      this.$emit('select-fn', selections);
      this.dialogVisible = false;
    },
    /**
    * 选择数据
    */
    selectFn: function () {
      var selections = this.$refs.dptTable.selections;
      if (this.selectionType === 'checkbox') {
        return;
      }
      this.selectedVal = selections[0].dptName;
      this.$emit('select-fn', selections);
      this.dialogVisible = false;
    }
  }
};
</script>
