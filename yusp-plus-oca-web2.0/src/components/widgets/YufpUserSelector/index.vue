<template>
  <div class="yufp-user-selector">
    <yu-input v-if="!buttonModel" :icon="icon" :placeholder="placeholder" :disabled="disabled" @focus="openFn" :on-icon-click="onIconClickFn" v-model="selectedVal"></yu-input>
    <yu-button v-if="buttonModel" @click="openFn">{{ $t('sysUserManager.xzyh') }}</yu-button>
    <yu-xdialog :title="dialogTitle" :visible.sync="dialogVisible" size="small" class="yufp-user-selector">
      <yu-xform ref="searchForm" related-table-name="userTableCustom" v-model="searchFormdata" form-type="search" @form-reset="clearOrgIdFn" @close="clearOrgIdFn">
        <yu-xform-group :column="2">
          <yu-xform-item name="orgName" :label="$t('sysUserManager.ssjg')" :placeholder="$t('sysUserManager.qxz')" ctype="custom">
            <yu-combo-tree ref="searchTree" v-model="searchOrgName" :placeholder="$t('sysUserManager.qxz')" :data-url="orgTreeUrl" :data-params="treeParams" request-type="POST" data-id="orgId" data-label="orgName" data-pid="upOrgId" :clearable="true" @node-click="changeOrgIdFn" @clear="clearOrgIdFn" :filter-node-method="filterNode" :all-node-value="true"></yu-combo-tree></yu-xform-item>
          <yu-xform-item name="loginCode" :label="$t('sysUserManager.zh')" :placeholder="$t('sysUserManager.qsr')"></yu-xform-item>
          <yu-xform-item name="userName" :label="$t('sysUserManager.yhmc')" :placeholder="$t('sysUserManager.qsr')"></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
      <yu-xtable request-type="POST" ref="userTableCustom" row-number :data-url="userTableUrl" selection-type="checkbox" :base-params="dataParams" :default-load="false">
        <yu-xtable-column :label="$t('sysUserManager.yhmc')" prop="userName"></yu-xtable-column>
        <yu-xtable-column :label="$t('sysUserManager.zh')" prop="loginCode"></yu-xtable-column>
        <yu-xtable-column :label="$t('sysUserManager.ssjg')" prop="orgName"> </yu-xtable-column>
        <yu-xtable-column :label="$t('sysUserManager.glzt')" prop="checked">
          <template slot-scope="scope">
            <yu-tag :type="scope.row.checked ? 'success':'danger'">{{ relSts[scope.row.checked] }}</yu-tag>
          </template>
        </yu-xtable-column>
      </yu-xtable>
      <div slot="footer" class="dialog-footer" align="center">
        <yu-button type="primary" @click="selecFn">{{ $t('component.btn_confirm') }}</yu-button>
        <yu-button @click="cancelFn">{{ $t('component.btn_cancel') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import { lookup, extend } from '@/utils'
import { checkBelongToChooseNode } from '@/utils/util'

export default {
  name: "YufpUserSelector",
  componentName: "YufpUserSelector",
  props: {
    buttonModel: {
      type: Boolean,
      default: false
    },
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
    params: {
      handler (val) {
        // var temp = this.createData();
        this.dataParams = val.dataParams;
        this.treeParams = val.treeParams;
      },
      deep: true
    }
  },
  // 挂载后
  mounted: function () {
    this.relSts = lookup.find('REL_STS', false);
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
        userTableUrl: backend.appOcaService + '/api/adminsmrole/userlist', // 用户列表查询地址
        icon: 'search', // 输入框图标
        selectedVal: '', // 选中的值
        dialogVisible: false, // 弹出框是否显示
        searchFormdata: {}, // 查询表单数据
        orgTreeUrl: backend.appOcaService + '/api/adminsmorg/treequeryauth', // 机构树请求 url
        treeParams: {},
        dialogTitle: '', // 弹出框标题
        // 展示参数
        searchKey: '', // 树过滤参数
        value: '3',
        placeholder: '',
        width: '',
        height: '',
        multiple: false,
        // 节点参数属性
        dataId: '',
        dataLabel: '',
        dataPid: '',
        // 数据参数
        dataParams: {
        },
        searchType: 'CUR_ORG', // 所辖或者当前""
        tempNodekeys: [],
        dataUrl: ''
      };
    },
    /**
    * 打开弹出框
    */
    openFn: function () {
      this.dialogVisible = true;
      this.$nextTick(function () {
        this.$refs.userTableCustom.remoteData(this.dataParams);
        this.searchFormdata.orgId = '';
        this.searchOrgName = '';
      });
    },
    /**
    * 取消
    */
    cancelFn: function () {
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
      if (data.orgName.indexOf(this.$refs.searchTree.selectedLabel) !== -1) {
        return true;
      }
      return checkBelongToChooseNode(this.$refs.searchTree.selectedLabel, node, 'orgName');
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
      this.dialogVisible = true;
    },
    /**
    * 选择数据
    */
    selecFn: function () {
      var _this = this;
      var selections = _this.$refs.userTableCustom.selections;
      if (selections < 1) {
        _this.$message({
          message: _this.$t('sysUserManager.qxxzytjl'),
          type: 'warning'
        });
        return;
      }
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].checked) {
          _this.$message({
            message: _this.$t('sysUserManager.znxzwgldsj'),
            type: 'warning'
          });
          return;
        }
      }
      _this.$confirm(_this.$t('sysUserManager.qdtjsxyh'), _this.$t('sysUserManager.ts'), {
        confirmButtonText: _this.$t('sysUserManager.qd'),
        cancelButtonText: _this.$t('sysUserManager.qx'),
        type: 'warning',
        callback: function (action) {
          if (action === 'confirm') {
            _this.$emit('select-fn', _this.$refs.userTableCustom.selections);
            _this.dialogVisible = false;
          }
        }
      });
    }
  }
};
</script>
