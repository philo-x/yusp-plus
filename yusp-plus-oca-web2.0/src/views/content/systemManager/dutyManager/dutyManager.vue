<!--
  @Created by zhuly8@yusys.com.cn 2020-12-28
  @updated by
  @description 岗位管理
-->
<template>
  <div id="dutyManager">
    <yu-panel ref="panel" :title="$t('dutyManager.gwgl')" class="adjust-height" show-search-input :placeholder="$t('dutyManager.gjz')" @search="fuzzyQuery">
      <!--岗位列表操作按钮-->
      <template slot="right">
        <yu-toolBar>
          <yu-button v-if="checkCtrl('add')" @click="addDutyFn">{{ $t('dutyManager.xz') }}</yu-button>
          <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled @click="deleteFn">{{ $t('dutyManager.sc') }}</yu-button>
          <yu-button v-if="checkCtrl('batchenable')" v-norepeat.disabled @click="useDutyFn">{{ $t('dutyManager.qy') }}</yu-button>
          <yu-button v-if="checkCtrl('batchdisable')" v-norepeat.disabled @click="stopDutyFn">{{ $t('dutyManager.ty') }}</yu-button>
        </yu-toolBar>
      </template>
      <!--快速查询-所属机构-->
      <template slot="search">
        <yu-combo-tree ref="tree" v-model="orgName" :placeholder="$t('dutyManager.qbjg')" :local-data="orgTreeData" data-id="orgId" data-label="orgName" data-pid="upOrgId" clearable @node-click="changeOrgIdFn($event, true)" @clear="clearUpOrgIdFn(true)" :filter-node-method="filterNode" :data-params="dataParams" :all-node-value="true" max-height="400"></yu-combo-tree>
      </template>
      <!--岗位列表查询条件-->
      <template slot="filter">
        <yu-xform ref="searchForm" related-table-name="dutyTable" v-model="searchFormdata" form-type="search" @form-search="clearFuzzyFn" @form-reset="resetFn">
          <yu-xform-group :column="4">
            <yu-xform-item :label="$t('dutyManager.gwdm')" name="dutyCode" :placeholder="$t('dutyManager.qsr')"></yu-xform-item>
            <yu-xform-item :label="$t('dutyManager.gwmc')" name="dutyName" :placeholder="$t('dutyManager.qsr')"></yu-xform-item>
            <yu-xform-item :label="$t('dutyManager.zt')" name="dutySts" :placeholder="$t('dutyManager.qxz')" ctype="select" data-code="DATA_STS"></yu-xform-item>
            <yu-xform-item name="orgName" :label="$t('dutyManager.ssjg')" :placeholder="$t('dutyManager.qxz')" ctype="custom">
              <yu-combo-tree ref="searchTree" v-model="searchOrgName" :placeholder="$t('dutyManager.qxz')" :local-data="orgTreeData"
                             data-id="orgId" data-label="orgName" data-pid="upOrgId" :clearable="true" :data-params="dataParams" @node-click="changeOrgIdFn($event, false)" @clear="clearUpOrgIdFn(false)" :filter-node-method="filterNodeSearch" :all-node-value="true" max-height="400">
              </yu-combo-tree>
            </yu-xform-item>
          </yu-xform-group>
        </yu-xform>
      </template>
      <!--岗位列表-->
      <yu-xtable request-type="POST" ref="dutyTable" row-number :data-url="tableUrl" selection-type="checkbox" @select="selectFn">
        <yu-xtable-column :label="$t('dutyManager.gwmc')">
          <template slot-scope="scope">
            <a class="underline" @click="orgDetailFn(scope.row)">{{ scope.row.dutyName }}</a>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('dutyManager.gwdm')" prop="dutyCode"> </yu-xtable-column>
        <!-- <yu-xtable-column :label="$t('dutyManager.zt')" prop="dutySts" data-code="DATA_STS"></yu-xtable-column> -->
        <yu-xtable-column :label="$t('dutyManager.zt')">
          <template slot-scope="scope">
            <yu-tag v-if="scope.row.dutySts=== 'A'" type="success">{{ dutySts[scope.row.dutySts] }}</yu-tag>
            <yu-tag v-if="scope.row.dutySts=== 'I'" type="danger">{{ dutySts[scope.row.dutySts] }}</yu-tag>
            <yu-tag v-if="scope.row.dutySts=== 'W'" type="warning">{{ dutySts[scope.row.dutySts] }}</yu-tag>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('dutyManager.ssjg')" prop="orgName"></yu-xtable-column>
        <!-- <yu-xtable-column :label="$t('dutyManager.zxgxr')" prop="lastChgName"></yu-xtable-column>
        <yu-xtable-column :label="$t('dutyManager.zxbgsj')" prop="lastChgDt"></yu-xtable-column> -->
        <yu-xtable-column :label="$t('dutyManager.zxbg')" width="260">
          <template slot-scope="scope" v-if="scope.row.lastChgName">
            <span>{{ scope.row.lastChgName + '（' + scope.row.lastChgDt + '）' }}</span>
          </template>
        </yu-xtable-column>
        <yu-xtable-column fixed="right" :label="$t('component.operate')" width="160">
          <template slot-scope="scope">
            <yu-button-drop set-index="0" :show-length="2" type="text">
              <yu-button v-if="checkCtrl('userlist')" @click="userDutyFn(scope.row)" type="text">{{ $t('dutyManager.gwyh') }}</yu-button>
              <yu-button v-if="checkCtrl('edit')" @click="editFn(scope.row)" type="text">{{ $t('dutyManager.xg') }}</yu-button>
              <yu-button v-if="checkCtrl('batchenable')" v-norepeat.disabled @click="useDutyFn(scope.row)" type="text">{{ $t('dutyManager.qy') }}</yu-button>
              <yu-button v-if="checkCtrl('batchdisable')" v-norepeat.disabled @click="stopDutyFn(scope.row)" type="text">{{ $t('dutyManager.ty') }}</yu-button>
              <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled @click="deleteFn(scope.row)" type="text">{{ $t('dutyManager.sc') }}</yu-button>
            </yu-button-drop>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
    <!--岗位 新增 修改 详情-->
    <yu-xdialog :title="viewTitle[viewType]" :visible.sync="dialogVisible" width="520px">
      <yu-xform ref="dutyForm" v-model="formdata" :form-type="formType" label-width="100px" :rules="formRules">
        <yu-xform-item :label="$t('dutyManager.gwmc')" name="dutyName" :placeholder="$t('dutyManager.qsr')"></yu-xform-item>
        <yu-xform-item :label="$t('dutyManager.gwdm')" name="dutyCode" :placeholder="$t('dutyManager.qsr')" :disabled="viewType != 'ADD'"></yu-xform-item>
        <yu-xform-item :label="$t('dutyManager.ssjg')" name="orgName" ctype="yufp-search-tree" :params="treeParams" :select-value="selectValue" :details="viewType === 'DETAIL'" :details-value="detailsValue" @node-click="changeOrgFn" @clear="formdata.orgName=null"></yu-xform-item>
        <yu-xform-item :label="$t('dutyManager.zt')" name="dutySts" ctype="select" :placeholder="$t('dutyManager.qsr')" data-code="DATA_STS"></yu-xform-item>
        <yu-xform-item :label="$t('dutyManager.bz')" name="dutyRemark" ctype="textarea" :placeholder="$t('dutyManager.qsr')" :row="3" :colspan="24"></yu-xform-item>
      </yu-xform>
      <div slot="footer" class="yu-grpButton">
        <yu-button v-if="formType==='edit'" key="edit" v-norepeat.disabled type="primary" @click="saveOrg">{{ $t('dutyManager.bc') }}</yu-button>
        <yu-button v-if="checkCtrl('edit') && viewType === 'DETAIL'" type="primary" @click="switchStatus('EDIT', true)">{{ $t('dutyManager.xg') }}</yu-button>
        <yu-button v-if="formType === 'details'" @click="cancelFn">{{ $t('dutyManager.fh') }}</yu-button>
        <yu-button v-else @click="cancelFn">{{ $t('dutyManager.qx') }}</yu-button>
      </div>
    </yu-xdialog>
    <!--岗位用户-->
    <yu-xdialog class="post-user" :title="$t('dutyManager.gwyh')" :visible.sync="userDialogVisible" size="small">
      <yu-xform v-model="userFormdata" related-table-name="userTable" form-type="search" :label-width="language==='en'?'142px':'80px'">
        <yu-xform-group :column="2">
          <yu-xform-item :label="$t('dutyManager.ygxm')" name="userName" :placeholder="$t('dutyManager.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('dutyManager.zh')" name="loginCode" :placeholder="$t('dutyManager.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('dutyManager.gh')" name="userCode" :placeholder="$t('dutyManager.qsr')"></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
      <yu-xtable request-type="POST" ref="userTable" row-number :data-url="userTableUrl" :base-params="userTableParams"
                 :default-load="false">
        <yu-xtable-column :label="$t('dutyManager.ygxm')" prop="userName"> </yu-xtable-column>
        <yu-xtable-column :label="$t('dutyManager.zh')" prop="loginCode"></yu-xtable-column>
        <yu-xtable-column :label="$t('dutyManager.gh')" prop="userCode"> </yu-xtable-column>
        <yu-xtable-column :label="$t('dutyManager.ssjg')" prop="orgName"></yu-xtable-column>
      </yu-xtable>
    </yu-xdialog>
  </div>
</template>
<script>
import { clone, lookup } from '@/utils'
import { checkBelongToChooseNode } from '@/utils/util'
import YufpSearchTree from '@/components/widgets/YufpSearchTree';
import { mapGetters } from 'vuex'
lookup.reg('DATA_STS,SYS_TYPE,FINCAL_ORG');
export default {
  components: { YufpSearchTree },
  data: function() {
    return {
      formdata: {}, // 表单数据
      tableUrl: backend.appOcaService + '/api/adminsmduty/page', // 岗位列表查询地址
      dialogVisible: false, // 弹出框是否展示
      viewType: 'ADD', // 弹出框默认新增
      viewTitle: lookup.find('CRUD_TYPE', false), // 弹出框类型
      formDisabled: false, // 表单是否禁用
      treeUrl: backend.appOcaService + '/api/adminsmorg/treequeryauth', // 岗位树请求 url
      props: { label: 'orgName', children: 'children' },
      formType: 'details',
      orgName: '', // 机构树搜索关键字
      formRules: {
        dutyName: [
          { required: true, message: this.$t('dutyManager.btx') },
          { max: 33, message: this.$t('dutyManager.srzgc') }
        ], // 岗位名称校验
        dutyCode: [
          { required: true, message: this.$t('dutyManager.btx') },
          { max: 33, message: this.$t('dutyManager.srzgc'), trigger: 'blur' }
        ], // 岗位代码校验
        orgName: { required: true, message: this.$t('dutyManager.btx') } // 必填项校验
      }, // 表单验证规则
      userDialogVisible: false, // 岗位用户弹框是否显示
      userFormdata: {}, // 岗位用户查询表单数据
      userTableUrl: backend.appOcaService + '/api/adminsmduty/userlist', // 岗位用户列表查询地址
      userTableParams: { dutyId: '' }, // 岗位用户表查询参数
      dataParams: {
        orgSts: 'A'
      }, // 额外请求参数 默认查询启用的机构
      treeParams: {
        dataId: 'orgId',
        dataLabel: 'orgName',
        dataPid: 'upOrgId',
        dataUrl: backend.appOcaService + '/api/adminsmorg/treequeryauth',
        placeholder: this.$t('dutyManager.qxz'),
        searchKey: 'orgName', // 树过滤关键字
        dataParams: {
          orgSts: 'A'
        } // 额外请求参数 默认查询启用的机构
      }, // 机构树选择
      selectValue: '', // 弹出框上级机构显示值
      searchOrgName: '', // 查询机构名称
      detailsValue: '', // 搜索树详情字段
      searchFormdata: {}, // 查询表单数据
      isSelected: true, // 批量操作按钮是否禁用
      dutySts: {}
    };
  },
  computed: {
    ...mapGetters(['orgTreeData', 'language'])
  },
  watch: {
    orgName: function(newVal, oldVal) {
      this.$refs.tree.filter(newVal);
    },
    searchOrgName: function(newVal, oldVal) {
      this.$refs.searchTree.filter(newVal);
    },
    userTableParams: {
      handler: function(newVal, oldVal) {
        this.$refs.userTable.remoteData(this.userTableParams);
      },
      deep: true
    }
  },
  mounted: function() {
    this.dutySts = lookup.find('DATA_STS', false);
    this.$store.dispatch('funData/orgTreeFn').then(res => {
    })
  },
  methods: {
    remoteTableData(panelRef, tableRef, searFormVmodel) {
      // panel隐藏的时候
      if (this.$refs[panelRef].hide) {
        this.$refs[tableRef].remoteData({orgId: this.orgName, keyWord: this.$refs.panel.inputVal})
      } else {
        this.$refs[tableRef].remoteData(this[searFormVmodel])
      }
    },
    remoteData() {
      this.remoteTableData('panel', 'dutyTable', 'searchFormdata')
    },
    /**
     * 表格数据勾选
     * @param selection 当前选中数据
     */
    selectFn: function(selection, row) {
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
    fuzzyQuery: function(e) {
      var param = { keyWord: e.value, orgId: this.orgName };
      this.$refs.dutyTable.remoteData(param);
      this.resetFn(); // 清空精确查询查询参数
    },
    /**
     * 通过所属机构查询
     * @param node 点击的节点信息
     * @param immidately 是否立即执行查询，区分快速查询和精确查询
     */
    changeOrgIdFn: function(node, immidately) {
      if (immidately) {
        this.orgName = node.orgId;
        this.$refs.dutyTable.remoteData({
          orgId: node.orgId,
          keyWord: this.$refs.panel.inputVal
        });
        this.resetFn(); // 清空精确查询查询参数
        return;
      }
      this.searchFormdata.orgId = node.orgId;
      this.searchOrgName = node.orgId;
    },
    /**
     * 清空所属机构查询条件
     * @param immidately 是否立即执行查询，区分快速查询和精确查询
     */
    clearUpOrgIdFn: function(immidately) {
      if (immidately) {
        this.$refs.dutyTable.remoteData({
          orgId: '',
          keyWord: this.$refs.panel.inputVal
        });
        return;
      }
      this.searchFormdata.orgId = '';
      this.searchOrgName = '';
    },
    /**
     * 清空所属机构查询条件
     * @param immidately 是否立即执行查询，区分快速查询和精确查询
     */
    resetFn: function() {
      this.$refs.searchForm.resetFields();
      this.searchFormdata.orgId = '';
      this.searchOrgName = '';
    },
    /**
     * 清空快速查询查询参数
     */
    resetFuzzyFn: function() {
      this.orgName = '';
      this.$refs.panel.inputVal = '';
    },
    /**
     * 清空快速查询区域查询条件
     */
    clearFuzzyFn: function() {
      this.orgName = '';
      this.$refs.panel.inputVal = '';
    },
    /**
     * 关闭弹出框
     */
    cancelFn: function() {
      this.dialogVisible = false;
    },
    /**
     * 控制保存按钮、xdialog、表单的状态
     * @param viewType 表单类型
     * @param editable 可编辑,默认false
     */
    switchStatus: function(viewType, editable, row) {
      if (
        this.formdata &&
        this.formdata.dutySts === 'A' &&
        viewType === 'EDIT'
      ) {
        this.$message({
          message: this.$t('dutyManager.znxzsxhdsxdsj'),
          type: 'warning'
        });
        return;
      }
      this.viewType = viewType;
      this.dialogVisible = true;
      this.formType = viewType === 'DETAIL' ? 'details' : 'edit';
      this.formDisabled = !editable;
    },
    editFn: function(row) {
      if (row.dutySts === 'A') {
        this.$message({
          message: this.$t('dutyManager.znxzsxhdsxdsj')
        });
        return;
      }
      this.switchStatus('EDIT', true);
      this.$nextTick(function() {
        this.$refs.dutyForm.resetFields();
        clone(row, this.formdata);
        this.selectValue = this.formdata.orgId;
      });
    },
    /**
     * 岗位详情
     * @param row 当前岗位信息
     */
    orgDetailFn: function(row) {
      this.switchStatus('DETAIL', true);
      this.$nextTick(function() {
        this.$refs.dutyForm.resetFields();
        clone(row, this.formdata);
        this.selectValue = this.formdata.orgId;
        this.detailsValue = this.formdata.orgName;
      });
    },
    /**
     * 新增岗位
     */
    addDutyFn: function() {
      this.switchStatus('ADD', true);
      this.$nextTick(function() {
        this.$refs.dutyForm.resetFields();
        this.selectValue = '';
        this.formdata.dutySts = 'W';
        this.formdata.dutyId = '';
      });
    },
    /**
     * 模糊机构树搜索
     * @param value 当前输入信息
     * @param nodeData 当前节点属性信息
     * @param node 当前节点信息
     */
    filterNode: function(value, nodeData, node) {
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
    filterNodeSearch: function(value, nodeData, node) {
      if (!this.$refs.searchTree.selectedLabel) {
        return true;
      }
      if (nodeData.orgName.indexOf(this.$refs.searchTree.selectedLabel) !== -1) {
        return true;
      }
      return checkBelongToChooseNode(this.$refs.searchTree.selectedLabel, node, 'orgName');
    },
    /**
     * 选择所属机构
     * @param node 当前选中机构信息
     */
    changeOrgFn: function(node) {
      this.formdata.orgId = node.orgId;
      this.formdata.orgName = node.orgName;
      this.selectValue = node.orgId;
    },
    /**
     * 保存岗位
     */
    saveOrg: function() {
      var _this = this;
      var validate = false;
      _this.$refs.dutyForm.validate(function(valid) {
        validate = valid;
      });
      if (!validate) {
        return;
      }
      var url = _this.formdata.dutyId
        ? '/api/adminsmduty/update'
        : '/api/adminsmduty/add';
      this.$request({
        method: 'POST',
        url: backend.appOcaService + url,
        data: _this.formdata
      }).then(({code, message, data}) => {
        //处理请求成功的情况
        if (code === '0000') {
          _this.$message({
            message: _this.$t('dutyManager.sjbccg'),
            type: 'success'
          });
          _this.dialogVisible = false;
          _this.remoteData();
        } else {
          _this.$message({
            message: message || _this.$('dutyManager.sjczsb'),
            type: 'error'
          });
        }
      })
    },
    /**
     * 启用岗位
     * @param row 当前岗位信息
     */
    useDutyFn: function(row) {
      // 校验是否已选择数据
      var _this = this;
      var selections = row.dutyId ? [row] : _this.$refs.dutyTable.selections;
      if (selections.length < 1) {
        this.$message({
          message: this.$t('dutyManager.qxzytjl'),
          type: 'warning'
        });
        return;
      }
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        // 只能启动 待启用失效状态的岗位
        if (selections[i].dutySts === 'W' || selections[i].dutySts === 'I') {
          ids.push(selections[i].dutyId);
        } else {
          this.$message({
            message: this.$t('dutyManager.znxzsxhdsxdsj'),
            type: 'warning'
          });
          return;
        }
      }
      this.$confirm(
        this.$t('dutyManager.cczjsxgwsxsfjx'),
        this.$t('dutyManager.ts'),
        {
          confirmButtonText: this.$t('dutyManager.qd'),
          cancelButtonText: this.$t('dutyManager.qx'),
          type: 'warning',
          callback: function(action) {
            if (action === 'confirm') {
              // 发起启用用岗位请求
              _this.$request({
                method: 'POST',
                url: backend.appOcaService + '/api/adminsmduty/batchenable',
                data: ids,
              }).then(({code, message, data}) => {
                //处理请求成功的情况
                if (code === '0000') {
                  _this.$message({
                    message: _this.$t('dutyManager.sjczcg'),
                    type: 'success'
                  });
                  _this.remoteData();
                } else {
                  _this.$message({
                    message: message || _this.$t('dutyManager.sjczsb'),
                    type: 'error'
                  });
                }
              })
            }
          }
        }
      );
    },
    /**
     * 停用岗位
     * @param row 当前岗位信息
     */
    stopDutyFn: function(row) {
      // 校验是否已选择数据
      var _this = this;
      var selections = row.dutyId ? [row] : _this.$refs.dutyTable.selections;
      if (selections.length < 1) {
        this.$message({
          message: this.$t('dutyManager.qxzytjl'),
          type: 'warning'
        });
        return;
      }
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].dutySts === 'A') {
          // 只能停用启用的岗位
          ids.push(selections[i].dutyId);
        } else {
          this.$message({
            message: this.$t('dutyManager.znxzsxdsj'),
            type: 'warning'
          });
          return;
        }
      }
      this.$confirm(
        this.$t('dutyManager.cczjsxgwsxsfjxx'),
        this.$t('dutyManager.ts'),
        {
          confirmButtonText: this.$t('dutyManager.qd'),
          cancelButtonText: this.$t('dutyManager.qx'),
          type: 'warning',
          callback: function(action) {
            if (action === 'confirm') {
              // 发起停用岗位请求
              _this.$request({
                method: 'POST',
                url: backend.appOcaService + '/api/adminsmduty/batchdisable',
                data: ids,
              }).then(({code, message, data}) => {
                //处理请求成功的情况
                if (code === '0000') {
                  _this.$message({
                    message: _this.$t('dutyManager.sjczcg'),
                    type: 'success'
                  });
                  _this.remoteData();
                } else {
                  _this.$message({
                    message:
                        message || _this.$t('dutyManager.sjczsb'),
                    type: 'error'
                  });
                }
              })
            }
          }
        }
      );
    },
    /**
     * 删除岗位
     * @param row 当前行数据
     */
    deleteFn: function(row) {
      var _this = this;
      var selections = row.dutyId ? [row] : this.$refs.dutyTable.selections;
      if (selections.length < 1) {
        this.$message({
          message: this.$t('dutyManager.qxxzyscdsj'),
          type: 'warning'
        });
        return;
      }
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].dutySts === 'W' || selections[i].dutySts === 'I') {
          // 只能删除待启用失效状态的岗位
          ids.push(selections[i].dutyId);
        } else {
          this.$message({
            message: this.$t('dutyManager.znxzsxhdsxdsj'),
            type: 'warning'
          });
          return;
        }
      }
      this.$confirm(
        this.$t('dutyManager.cczjyjscgwjsfjx'),
        this.$t('dutyManager.ts'),
        {
          confirmButtonText: this.$t('dutyManager.qd'),
          cancelButtonText: this.$t('dutyManager.qx'),
          type: 'warning',
          callback: function(action) {
            if (action === 'confirm') {
              // 发起停用岗位请求
              _this.$request({
                method: 'POST',
                url: backend.appOcaService + '/api/adminsmduty/batchdelete',
                data: ids,
              }).then(({code, message, data}) => {
                //处理请求成功的情况
                if (code === '0000') {
                  _this.$message({
                    message: _this.$t('dutyManager.sjsccg'),
                    type: 'success'
                  });
                  _this.remoteData();
                } else {
                  _this.$message({
                    message:
                        message || _this.$t('dutyManager.sjczsb'),
                    type: 'error'
                  });
                }
              })
            }
          }
        }
      );
    },
    /**
     * 岗位用户
     * @param row 当前岗位信息
     */
    userDutyFn: function(row) {
      var selections = row.dutyId ? [row] : this.$refs.dutyTable.selections;
      if (selections.length != 1) {
        this.$message({
          message: this.$t('dutyManager.qxzytjl'),
          type: 'warning'
        });
        return;
      }
      this.userDialogVisible = true;
      this.$nextTick(function() {
        this.userTableParams.dutyId = selections[0].dutyId;
      });
    }
  }
};
</script>
