<!--
  @Created by zhuly8@yusys.com.cn 2020-12-22
  @updated by
  @description 机构管理
-->
<template>
  <div id="orgInfoManager">
    <yu-panel ref="panel" :title="$t('orgInfoManager.jggl')" class="adjust-height" show-search-input :placeholder="$t('orgInfoManager.gjz')" @search="fuzzyQuery">
      <template slot="right">
        <yu-button-drop>
          <yu-button v-if="checkCtrl('add')" @click="addOrg">{{ $t('orgInfoManager.xz') }}</yu-button>
          <yu-button v-if="checkCtrl('batchenable')" @click="useOrgFn">{{ $t('orgInfoManager.qy') }}</yu-button>
          <yu-button v-if="checkCtrl('batchdisable')" @click="stopOrgFn">{{ $t('orgInfoManager.ty') }}</yu-button>
          <yu-button v-if="checkCtrl('delete')" @click="deleteFn">{{ $t('orgInfoManager.sc') }}</yu-button>
          <!-- <yufp-excel-export :export-url="exportTemplateUrl" :title="$t('orgInfoManager.mbxz')"></yufp-excel-export> -->
          <!-- <yufp-excel-export :export-url="excelExportUrl" :title="$t('orgInfoManager.exceldc')" exportParam="exportParam"></yufp-excel-export> -->
          <!-- <yufp-excel-import :import-url="excelImportUrl" :title="$t('orgInfoManager.pldr')" max-file-size="10" @import-success="doAutoQuery"></yufp-excel-import> -->
        </yu-button-drop>
      </template>
      <!--快速查询-自定义查询区域-->
      <template slot="search">
        <yu-combo-tree
          ref="tree"
          v-model="orgName"
          :placeholder="$t('orgInfoManager.qbjg')"
          :local-data="orgTreeData"
          data-id="orgId"
          data-label="orgName"
          data-pid="upOrgId"
          :clearable="true"
          @node-click="changeUpOrgIdFn($event, true)"
          @clear="clearUpOrgIdFn(true)"
          :filter-node-method="filterNode"
          :all-node-value="true"
          :max-height="400"></yu-combo-tree>
      </template>
      <!--机构列表查询条件-->
      <template slot="filter">
        <yu-xform ref="searchForm" related-table-name="orgTable" v-model="searchFormdata" form-type="search" @form-search="clearFuzzyFn" @form-reset="resetFn">
          <yu-xform-group :column="4">
            <yu-xform-item name="orgCode" :label="$t('orgInfoManager.jgdm')" :placeholder="$t('orgInfoManager.qsr')"></yu-xform-item>
            <yu-xform-item name="orgName" :label="$t('orgInfoManager.jgmc')" :placeholder="$t('orgInfoManager.qsr')"></yu-xform-item>
            <yu-xform-item name="orgSts" :label="$t('orgInfoManager.zt')" :placeholder="$t('orgInfoManager.qxz')" ctype="select" data-code="DATA_STS"></yu-xform-item>
            <!--<yu-xform-item name="upOrgName" :label="$t('orgInfoManager.ssjg')" :placeholder="$t('orgInfoManager.qxz')" ctype="custom">
              <yu-combo-tree
                ref="searchTree"
                v-model="searchOrgName"
                :placeholder="$t('orgInfoManager.qxz')"
                :local-data="orgTreeData"
                data-id="orgId"
                data-label="orgName"
                data-pid="upOrgId"
                :clearable="true"
                @node-click="changeUpOrgIdFn($event, false)"
                @clear="clearUpOrgIdFn(false)"
                :filter-node-method="filterNodeSearch"
                :all-node-value="true"
                :max-height="400">
              </yu-combo-tree>
            </yu-xform-item>-->
          </yu-xform-group>
        </yu-xform>
      </template>
      <!--机构列表-->
      <yu-xtable request-type="POST" ref="orgTable" row-number :data-url="tableUrl" selection-type="checkbox" :base-params="tableParams" @select="selectFn">
        <yu-xtable-column :label="$t('orgInfoManager.jgmc')" width="260px">
          <template slot-scope="scope">
            <a class="underline" @click="orgDetailFn(scope.row)">{{ scope.row.orgName }}</a>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('orgInfoManager.jgdm')" prop="orgCode"></yu-xtable-column>
        <yu-xtable-column :label="$t('orgInfoManager.sjjg')" prop="upOrgName"> </yu-xtable-column>
        <!--<yu-xtable-column :label="$t('orgInfoManager.jrjgmc')" prop="instuName"></yu-xtable-column>-->
        <!-- <yu-xtable-column :label="$t('orgInfoManager.zt')" prop="orgSts" data-code="DATA_STS"></yu-xtable-column> -->
        <yu-xtable-column :label="$t('orgInfoManager.zt')">
          <template slot-scope="scope">
            <yu-tag v-if="scope.row.orgSts=== 'A'" type="success">{{ orgSts[scope.row.orgSts] }}</yu-tag>
            <yu-tag v-if="scope.row.orgSts=== 'I'" type="danger">{{ orgSts[scope.row.orgSts] }}</yu-tag>
            <yu-tag v-if="scope.row.orgSts=== 'W'" type="warning">{{ orgSts[scope.row.orgSts] }}</yu-tag>
          </template>
        </yu-xtable-column>
        <!-- <yu-xtable-column :label="$t('orgInfoManager.zxbgyh')" prop="lastChgName"></yu-xtable-column>
        <yu-xtable-column :label="$t('orgInfoManager.zxbgsj')" prop="lastChgDt"> </yu-xtable-column> -->
        <yu-xtable-column :label="$t('orgInfoManager.zxbg')" width="260">
          <template slot-scope="scope" v-if="scope.row.lastChgName">
            <span>{{ scope.row.lastChgName + '（' + scope.row.lastChgDt + '）' }}</span>
          </template>
        </yu-xtable-column>
        <yu-xtable-column fixed="right" :label="$t('component.operate')" width="160">
          <template slot-scope="scope">
            <yu-button-drop set-index="0" :show-length="2" type="text">
              <yu-button v-if="checkCtrl('add')" @click="addOrg(scope.row)" type="text">{{ $t('orgInfoManager.xzzjj') }}</yu-button>
              <yu-button v-if="checkCtrl('batchenable') && scope.row.orgSts != 'A'" v-norepeat.disabled @click="useOrgFn(scope.row)">{{ $t('orgInfoManager.qy') }}</yu-button>
              <yu-button v-if="checkCtrl('batchdisable') && scope.row.orgSts != 'I'" v-norepeat.disabled @click="stopOrgFn(scope.row)">{{ $t('orgInfoManager.ty') }}</yu-button>
              <yu-button v-if="checkCtrl('edit')" @click="editFn(scope.row)" type="text">{{ $t('orgInfoManager.xg') }}</yu-button>
              <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled @click="deleteFn(scope.row)" type="text">{{ $t('orgInfoManager.sc') }}</yu-button>
            </yu-button-drop>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
    <!--机构 新增 修改 详情-->
    <yu-xdialog :title="viewTitle[viewType]" :visible.sync="dialogVisible" size="small">
      <yu-xform ref="orgForm" v-model="formdata" :form-type="formType" label-width="150px" :rules="formRules">
        <yu-xform-group columns="4">
          <yu-xform-item :label="$t('orgInfoManager.jgmc')" name="orgName" :placeholder="$t('orgInfoManager.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('orgInfoManager.jgdm')" name="orgCode" :placeholder="$t('orgInfoManager.qsr')" :disabled="viewType != 'ADD'"></yu-xform-item>
          <yu-xform-item :label="$t('orgInfoManager.sjjg')" name="upOrgName" ctype="yufp-search-tree" :params="treeParams" :select-value="selectValue" :disabled="isAddchild" :details="viewType==='DETAIL'" :details-value="detailsValue" @node-click="changeUpOrgFn" @clear="clearUpOrgFn"></yu-xform-item>
          <yu-xform-item :label="$t('orgInfoManager.sjjgdm')" name="upOrgCode" :placeholder="$t('orgInfoManager.qxzsjjg')" :disabled="true"></yu-xform-item>
          <yu-xform-item :label="$t('orgInfoManager.zt')" name="orgSts" ctype="select" :placeholder="$t('orgInfoManager.qxz')" data-code="DATA_STS"></yu-xform-item>
          <yu-xform-item :label="$t('orgInfoManager.yb')" name="zipCde" :placeholder="$t('orgInfoManager.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('orgInfoManager.lxr')" name="contUsr" :placeholder="$t('orgInfoManager.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('orgInfoManager.lxdh')" name="contTel" :placeholder="$t('orgInfoManager.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('orgInfoManager.zxbgyh')" name="lastChgName" :placeholder="$t('orgInfoManager.qsr')" :hidden="viewType === 'ADD'" :disabled="true"></yu-xform-item>
          <yu-xform-item :label="$t('orgInfoManager.zxbgsj')" name="lastChgDt" :placeholder="$t('orgInfoManager.qsr')" :hidden="viewType === 'ADD'" :disabled="true"></yu-xform-item>
          <yu-xform-item :label="$t('orgInfoManager.dz')" name="orgAddr" :placeholder="$t('orgInfoManager.qsr')" :colspan="24"></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
      <div slot="footer" class="yu-grpButton">
        <yu-button v-if="formType==='edit'" key="edit" v-norepeat.disabled type="primary" @click="saveOrg">{{ $t('orgInfoManager.bc') }}</yu-button>
        <yu-button v-if="checkCtrl('add') && viewType === 'DETAIL'" type="primary" @click="switchStatus('EDIT', true)">{{ $t('orgInfoManager.xg') }}</yu-button>
        <yu-button v-if="formType === 'details'" @click="cancelFn">{{ $t('orgInfoManager.fh') }}</yu-button>
        <yu-button v-else @click="cancelFn">{{ $t('orgInfoManager.qx') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import {mapGetters} from 'vuex';
import {clone, lookup} from '@/utils';
import {checkBelongToChooseNode} from '@/utils/util';
import YufpSearchTree from '@/components/widgets/YufpSearchTree';
import {validator} from '@/utils/validate';
import YufpExcelExport from '@/components/widgets/YufpExcelExport'; // Excel导出组件
import YufpExcelImport from '@/components/widgets/YufpExcelImport'; // Excel导入组件
import YuSingleUpload from '@/components/widgets/YuSingleUpload'; // Excel导入组件
lookup.reg('DATA_STS');
export default {
  components: {YufpSearchTree, YufpExcelExport, YufpExcelImport, YuSingleUpload},
  data() {
    return {
      formdata: {}, // 表单数据
      tableUrl: backend.appOcaService + '/api/adminsmorg/querypage', // 机构列表查询地址
      tableParams: {
        upOrgId: ''
      }, // 机构列表查询参数
      dialogVisible: false, // 弹出框是否展示
      viewType: 'ADD', // 弹出框默认新增
      viewTitle: lookup.find('CRUD_TYPE', false), // 弹出框类型
      formDisabled: false, // 表单是否禁用
      props: {label: 'orgName', children: 'children'},
      orgTreeParams: {upOrgId: ''}, // todo 不带用户信息
      formType: 'details',
      isAddchild: false, // 新增子机构禁用上级机构输入框
      upOrgId: '', // 上级机构 id
      orgName: '', // 机构树搜索关键字
      optionsData: [], // 模糊查询数据选项
      formRules: {
        orgName: [
          {required: true, message: this.$t('orgInfoManager.btx')},
          {max: 100, message: this.$t('orgInfoManager.zdcdbcggzf')},
          {validator: validator.speChar, message: this.$t('orgInfoManager.srxxbhtszf')}
        ], // 机构名称校验
        orgCode: [
          {required: true, message: this.$t('orgInfoManager.btx')},
          {max: 100, message: this.$t('orgInfoManager.zdcdbcggzf')},
          {validator: validator.speChar, message: this.$t('orgInfoManager.srxxbhtszf')}
        ], // 机构代码校验
        orgSts: {required: true, message: this.$t('orgInfoManager.btx')}, // 必填项校验
        upOrgName: {
          required: true, message: this.$t('orgInfoManager.btx'), validator: (rule, value, callback) => {
            this.$refs.orgForm.validateField('upOrgCode');
            if (!value) {
              return callback(new Error(this.$t('orgInfoManager.btx')))
            } else {
              callback();
            }
          }
        }, // 必填项校验
        upOrgCode: {required: true, message: this.$t('orgInfoManager.btx'), trigger: 'blur'}, // 必填项校验
        zipCde: {validator: validator.postcode}
      }, // 表单验证规则
      treeParams: {
        dataId: 'orgId',
        dataLabel: 'orgName',
        dataPid: 'upOrgId',
        dataUrl: backend.appOcaService + '/api/adminsmorg/treequeryauth',
        placeholder: this.$t('orgInfoManager.qxz'),
        searchKey: 'orgName', // 树过滤关键字
        dataParams: {
          orgSts: 'A'
        } // 额外请求参数 默认查询启用的机构
      }, // 机构树选择
      selectValue: '', // 弹出框上级机构显示值
      detailsValue: '', // 搜索树详情字段
      searchFormdata: {}, // 查询表单数据
      searchOrgName: '', // 查询表单中上级机构
      isSelected: true, // 批量操作按钮是否禁用
      orgSts: {}, // 机构状态
      exportTemplateUrl: backend.appOcaService + '/api/adminsmorg/exporttemplate', // Excel模板下载地址
      excelExportUrl: backend.appOcaService + '/api/adminsmorg/excelexport', // Excel导出地址
      excelImportUrl: backend.appOcaService + '/api/adminsmorg/excelimport', // Excel导入地址
      exportParam: this.searchFormdata // 导出时的查询参数
    };
  },
  computed: {
    ...mapGetters([
      'org', 'orgTreeData'
    ])
  },
  watch: {
    orgName (newVal, oldVal) {
      this.$refs.tree.filter(newVal);
    },
    searchOrgName (newVal, oldVal) {
      this.$refs.searchTree.filter(newVal);
    }
  },
  created () {
    this.tableParams.upOrgId = this.org.id;
    this.orgTreeParams.upOrgId = this.org.id;
    this.$store.dispatch('funData/orgTreeFn').then(res => {
    });
  },
  mounted () {
    this.orgSts = lookup.find('DATA_STS', false);
  },
  methods: {
    remoteTableData(panelRef, tableRef, searFormVmodel) {
      // panel隐藏的时候
      if (this.$refs[panelRef].hide) {
        this.$refs[tableRef].remoteData({upOrgId: this.orgName, keyWord: this.$refs[panelRef].inputVal})
      } else {
        this.$refs[tableRef].remoteData(this[searFormVmodel])
      }
    },
    remoteData() {
      this.remoteTableData('panel', 'orgTable', 'searchFormdata')
    },
    /**
    * 勾选表格
    * @param selection 当前勾选数据
    */
    selectFn (selection) {
      if (selection.length > 0) {
        this.isSelected = false;
        return;
      }
      this.isSelected = true;
    },
    /**
    * 快速查询
    * @param e 模糊查询关键字
    */
    fuzzyQuery (e) {
      var param = {keyWord: e.value, upOrgId: this.orgName};
      // this.$refs.orgTable.remoteData(param);
      this.remoteData(param)
      this.resetFn(); // 清空精确查询条件
    },
    /**
    * 通过上级机构查询
    * @param node 点击的节点信息
    * @param immidately 是否立即执行查询，区分快速查询和精确查询
    */
    changeUpOrgIdFn (node, immidately) {
      if (immidately) {
        this.orgName = node.orgId;
        this.$refs.orgTable.remoteData({upOrgId: node.orgId, keyWord: this.$refs.panel.inputVal});
        this.resetFn(); // 清空精确查询条件
        return;
      }
      this.searchFormdata.upOrgId = node.orgId;
      this.searchOrgName = node.orgId;
    },
    /**
    * 清空上级机构查询条件
    * @param immidately 是否立即执行查询，区分快速查询和精确查询
    */
    clearUpOrgIdFn (immidately) {
      if (immidately) {
        this.$refs.orgTable.remoteData({upOrgId: '', keyWord: this.$refs.panel.inputVal});
        return;
      }
      this.searchFormdata.upOrgId = '';
      this.searchOrgName = '';
    },
    /**
    * 关闭弹出框
    */
    cancelFn () {
      this.dialogVisible = false;
    },
    /**
    * 清空精确查询表单查询参数
    */
    resetFn () {
      this.$refs.searchForm.resetFields();
      this.searchFormdata.upOrgId = '';
      this.searchOrgName = '';
    },
    /**
    * 清空快速查询区域查询条件
    */
    clearFuzzyFn () {
      this.orgName = '';
      this.$refs.panel.inputVal = '';
      this.fuzzyQueryParam = null
    },
    /**
    * 控制保存按钮、xdialog、表单的状态
    * @param viewType 表单类型
    * @param editable 可编辑,默认false
    */
    switchStatus (viewType, editable) {
      if (this.formdata && this.formdata.orgSts === 'A' && viewType === 'EDIT') {
        this.$message({
          message: this.$t('orgInfoManager.znxgtyhdqydjg'),
          type: 'warning'
        });
        return;
      }
      this.viewType = viewType;
      this.dialogVisible = true;
      this.isAddchild = viewType === 'DETAIL';
      this.formType = viewType === 'DETAIL' ? 'details' : 'edit';
      this.formDisabled = !editable;
    },
    editFn (row) {
      var _this = this;
      if (row.orgSts === 'A') {
        this.$message({
          message: this.$t('orgInfoManager.znxgtyhdqydjg'),
          type: 'warning'
        });
        return;
      }
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/adminsmorg/info/' + row.orgId
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.switchStatus('EDIT', true);
          _this.isAddchild = false;
          _this.$nextTick(function () {
            _this.$refs.orgForm.resetFields();
            clone(data, _this.formdata);
            _this.selectValue = _this.formdata.upOrgId;
          });
        } else {
          _this.$message({
            message: message
          });
          return {};
        }
      });
    },
    /**
    * 机构详情
    * @param row 当前机构信息/api/adminsmorg/info/{orgId}
    */
    orgDetailFn (row) {
      var _this = this;
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/adminsmorg/info/' + row.orgId
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.switchStatus('DETAIL', true);
          _this.isAddchild = true;
          _this.$nextTick(function () {
            _this.$refs.orgForm.resetFields();
            clone(data, _this.formdata);
            _this.selectValue = _this.formdata.upOrgId;
            _this.detailsValue = _this.formdata.upOrgName;
          });
        } else {
          _this.$message({
            message: message
          });
          return {};
        }
      });
    },
    /**
    * 新增子机构
    * @param row 当前机构信息
    */
    addOrg (row) {
      if (row.orgId && row.orgSts !== 'A') {
        this.$message({
          message: this.$t('orgInfoManager.znxzysxdsj'),
          type: 'warning'
        });
        return;
      }
      this.switchStatus('ADD', true);
      this.selectValue = '';
      this.isAddchild = false;
      this.$nextTick(function () {
        this.$refs.orgForm.resetFields();
        this.formdata.orgSts = 'W';
        this.formdata.orgId = '';
        // 新增子机构
        if (row.orgId) {
          this.selectValue = row.orgId;
          this.formdata.upOrgId = row.orgId;
          this.formdata.upOrgName = row.orgName;
          this.formdata.upOrgCode = row.orgCode;
          this.isAddchild = true;
        }
      });
    },
    /**
    * 模糊机构树搜索
    * @param value 当前输入信息
    * @param nodeData 当前节点属性信息
    * @param node 当前节点信息
    */
    filterNode (value, nodeData, node) {
      if (!this.$refs.tree.selectedLabel) {
        return true;
      }
      if (nodeData.orgName.indexOf(this.$refs.tree.selectedLabel) !== -1) {
        return true;
      }
      return checkBelongToChooseNode(this.$refs.tree.selectedLabel, node, 'orgName');
    },
    /**
    * 精确机构树搜索
    * @param value 当前输入信息
    * @param nodeData 当前节点属性信息
    * @param node 当前节点信息
    */
    filterNodeSearch (value, nodeData, node) {
      if (!this.$refs.searchTree.selectedLabel) {
        return true;
      }
      if (nodeData.orgName.indexOf(this.$refs.searchTree.selectedLabel) !== -1) {
        return true;
      }
      return checkBelongToChooseNode(this.$refs.searchTree.selectedLabel, node, 'orgName');
    },
    /**
    * 选择上级机构
    * @param node 当前选中机构信息
    */
    changeUpOrgFn (node) {
      this.formdata.upOrgId = node.orgId;
      this.formdata.upOrgCode = node.orgCode;
      this.formdata.upOrgName = node.orgName;
      this.selectValue = node.orgId;
    },
    /**
    * 清空上级机构
    * @param node 当前选中机构信息
    */
    clearUpOrgFn () {
      this.formdata.upOrgCode = '';
      this.formdata.upOrgName = '';
    },
    /**
    * 保存机构
    * @param row 当前机构信息
    */
    saveOrg () {
      var _this = this;
      var validate = false;
      _this.$refs.orgForm.validate(function (valid) {
        validate = valid;
      });
      if (!validate) {
        return;
      }
      var url = _this.formdata.orgId ? '/api/adminsmorg/update' : '/api/adminsmorg/save';
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + url,
        data: _this.formdata
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$message({
            message: _this.$t('orgInfoManager.sjbccg'),
            type: 'success'
          });
          _this.dialogVisible = false;
          _this.remoteData();
          _this.$store.dispatch('funData/orgTreeFn').then(res => {
          });
          // _this.$refs.searchTree.$refs.selectTreeX.remoteData();
          // _this.$refs.tree.$refs.selectTreeX.remoteData();
        } else {
          _this.$message({
            message: message || this.$t('orgInfoManager.czsb'),
            type: 'error'
          });
        }
      });
    },
    /**
    * 启用机构
    */
    useOrgFn (row) {
      var _this = this;
      var selections = row && row.orgId ? [row] : _this.$refs.orgTable.selections;
      if (selections.length < 1) {
        this.$message({ message: this.$t('orgInfoManager.qxzytsj'), type: 'warning' });
        return;
      }
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].orgSts === 'W' || selections[i].orgSts === 'I') {
          // 只能启动 待启用失效状态的机构
          ids.push(selections[i].orgId);
        } else {
          this.$message({
            message: this.$t('orgInfoManager.znxzsxhdsxdsj'),
            type: 'warning'
          });
          return;
        }
      }
      this.$confirm(this.$t('orgInfoManager.cczjqygsjsfjx'), this.$t('orgInfoManager.ts'), {
        confirmButtonText: this.$t('orgInfoManager.qr'),
        cancelButtonText: this.$t('orgInfoManager.qx'),
        type: 'warning',
        callback (action) {
          if (action === 'confirm') {
            // 发起启用用机构请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmorg/batchenable',
              data: ids
            }).then(({code, message, data}) => {
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('orgInfoManager.sjczcg'),
                  type: 'success'
                });
                _this.remoteData()
              } else {
                _this.$message({
                  message: message || _this.$t('orgInfoManager.sjczsb'),
                  type: 'warning'
                });
              }
            });
          }
        }
      });
    },
    /**
    * 停用机构
    */
    stopOrgFn (row) {
      // 校验是否已选择数据
      var _this = this;
      var selections = row && row.orgId ? [row] : _this.$refs.orgTable.selections;
      if (selections.length < 1) {
        this.$message({ message: this.$t('orgInfoManager.qxzytsj'), type: 'warning' });
        return;
      }
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].orgSts === 'A') {
          // 只能停用启用的机构
          ids.push(selections[i].orgId);
        } else {
          this.$message({
            message: this.$t('orgInfoManager.znxzysxdsj'),
            type: 'warning'
          });
          return;
        }
      }
      this.$confirm(this.$t('orgInfoManager.cczjtygsjsfjx'), this.$t('orgInfoManager.ts'), {
        confirmButtonText: this.$t('orgInfoManager.qr'),
        cancelButtonText: this.$t('orgInfoManager.qx'),
        type: 'warning',
        callback (action) {
          if (action === 'confirm') {
            // 发起停用机构请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmorg/batchdisable',
              data: ids
            }).then(({code, message, data}) => {
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('orgInfoManager.sjczcg'),
                  type: 'success'
                });
                _this.remoteData();
              } else {
                _this.$message({
                  message: message || _this.$t('orgInfoManager.sjczsb'),
                  type: 'error'
                });
              }
            });
          }
        }
      });
    },
    /**
    * 删除机构
    * @param row 当前行数据
    */
    deleteFn (row) {
      var _this = this;
      var selections = row && row.orgId ? [row] : _this.$refs.orgTable.selections;
      if (selections.length < 1) {
        this.$message({
          message: this.$t('orgInfoManager.qxxzyscdsj'),
          type: 'warning'
        });
        return;
      }
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].orgSts === 'W' || selections[i].orgSts === 'I') {
          // 只能删除待启用失效状态的机构
          ids.push(selections[i].orgId);
        } else {
          this.$message({
            message: this.$t('orgInfoManager.znscsxhdsxdsj'),
            type: 'warning'
          });
          return;
        }
      }
      this.$confirm(this.$t('orgInfoManager.cczjyjscgsjsfjx'), this.$t('orgInfoManager.ts'), {
        confirmButtonText: this.$t('orgInfoManager.qr'),
        cancelButtonText: this.$t('orgInfoManager.qx'),
        type: 'warning',
        callback (action) {
          if (action === 'confirm') {
            // 发起停用机构请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmorg/batchdelete',
              data: ids
            }).then(({code, message, data}) => {
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('orgInfoManager.sjczcg'),
                  type: 'success'
                });
                _this.remoteData();
                _this.$store.dispatch('funData/orgTreeFn').then(res => {
                });
              } else {
                _this.$message({
                  message: message || _this.$t('orgInfoManager.sjczsb'),
                  type: 'error'
                });
              }
            });
          }
        }
      });
    },
    /**
     * 上传成功
     */
    doAutoQuery () {
      var _this = this;
      _this.remoteData();
    }
  }
};
</script>
<style lang="scss" scoped>
.excel-import ~ .el-button{margin-left: 10px;}
</style>
