<!--
  @created by tanrui1@yusys.com.cn 2020-12-28
  @updated by
  @description 部门管理
-->
<template>
  <div id="dptManager">
    <yu-panel ref="panel" :title="$t('dptManager.bmgl')" :placeholder="$t('dptManager.gjz')" show-search-input @search="fuzzyQuery" class="adjust-height">
      <!--部门列表操作按钮-->
      <template slot="right">
        <yu-toolBar>
          <yu-button v-if="checkCtrl('add')" @click="addFn">{{ $t('dptManager.xz') }}</yu-button>
          <yu-button v-if="checkCtrl('batchenable')" v-norepeat.disabled @click="useFn">{{ $t('dptManager.qy') }}</yu-button>
          <yu-button v-if="checkCtrl('batchdisable')" v-norepeat.disabled @click="stopFn">{{ $t('dptManager.ty') }}</yu-button>
          <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled @click="deleteFn">{{ $t('dptManager.sc') }}</yu-button>
        </yu-toolBar>
      </template>
      <!--快速查询-自定义查询区域-->
      <template slot="search">
        <yu-combo-tree
          ref="tree"
          v-model="orgId"
          :placeholder="$t('dptManager.qbjg')"
          :local-data="orgTreeData"
          data-id="orgId"
          data-label="orgName"
          data-pid="upOrgId"
          :clearable="true"
          :multiple="false"
          :all-node-value="true"
          @node-click="changeOrgFn($event, true)"
          @clear="clearOrgIdFn(true)"
          :filter-node-method="filterNode"
          :max-height="400"></yu-combo-tree>
      </template>
      <!--部门列表高级查询条件-->
      <template slot="filter">
        <yu-xform ref="searchForm" related-table-name="dptTable" v-model="searchFormdata" form-type="search" @form-reset="resetFn">
          <yu-xform-group :column="4">
            <yu-xform-item name="orgId" :label="$t('dptManager.ssjg')" :placeholder="$t('dptManager.qxz')" ctype="custom">
              <yu-combo-tree
                ref="searchTree"
                v-model="searchOrgId"
                :placeholder="$t('dptManager.qxz')"
                :local-data="orgTreeData"
                data-id="orgId"
                data-label="orgName"
                data-pid="upOrgId"
                :clearable="true"
                @node-click="changeOrgFn($event, false)"
                @clear="clearOrgIdFn(false)"
                :filter-node-method="filterNodeSearch"
                :max-height="400">
              </yu-combo-tree>
            </yu-xform-item>
            <yu-xform-item name="dptCode" :label="$t('dptManager.bmdm')" :placeholder="$t('dptManager.qsr')"></yu-xform-item>
            <yu-xform-item name="dptName" :label="$t('dptManager.bmmc')" :placeholder="$t('dptManager.qsr')"></yu-xform-item>
            <yu-xform-item name="dptSts" :label="$t('dptManager.zt')" :placeholder="$t('dptManager.qsr')" ctype="select" data-code="DATA_STS"></yu-xform-item>
            <!-- <div slot="custom">
              <yu-button type="primary" icon="search" @click="searchFn" style="margin-left: 10px">{{ $t('dptManager.cx') }}</yu-button>
              <yu-button type="primary" icon="edit" @click="resetFn">{{ $t('dptManager.cz') }}</yu-button>
            </div> -->
          </yu-xform-group>
        </yu-xform>
      </template>
      <!--部门列表-->
      <yu-xtable request-type="POST" ref="dptTable" row-number :data-url="tableUrl" selection-type="checkbox" :base-params="tableParams" condition-key="" @select="tableSelectedChangeFn">
        <yu-xtable-column :label="$t('dptManager.bmmc')">
          <template slot-scope="scope">
            <a class="underline" @click="dptDetailFn(scope.row)">{{ scope.row.dptName }}</a>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('dptManager.bmdm')" prop="dptCode"></yu-xtable-column>
        <yu-xtable-column :label="$t('dptManager.ssjg')" prop="orgName"> </yu-xtable-column>
        <yu-xtable-column :label="$t('dptManager.zt')" prop="dptSts" data-code="DATA_STS">
          <template slot-scope="scope">
            <yu-tag v-if="scope.row.dptSts=== 'A'" type="success">{{ dptSts[scope.row.dptSts] }}</yu-tag>
            <yu-tag v-if="scope.row.dptSts=== 'I'" type="danger">{{ dptSts[scope.row.dptSts] }}</yu-tag>
            <yu-tag v-if="scope.row.dptSts=== 'W'" type="warning">{{ dptSts[scope.row.dptSts] }}</yu-tag>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('dptManager.sjbm')" prop="upDptName"></yu-xtable-column>
        <yu-xtable-column :label="$t('dptManager.zxgx')" width="260">
          <template slot-scope="scope" v-if="scope.row.lastChgName">
            <span>{{ scope.row.lastChgName + '（' + scope.row.lastChgDt + '）' }}</span>
          </template>
        </yu-xtable-column>
        <yu-xtable-column fixed="right" :label="$t('dptManager.op')" width="160">
          <template slot-scope="scope">
            <yu-button-drop set-index="0" :show-length="2" type="text">
              <yu-button v-if="checkCtrl('add')" @click="addSubDpt(scope.row)" type="text">{{ $t('dptManager.xzzbm') }}</yu-button>
              <yu-button v-if="checkCtrl('update')" @click="editDptFn(scope.row)" type="text">{{ $t('dptManager.xg') }}</yu-button>
              <yu-button @click="userListFn(scope.row)" type="text">{{ $t('dptManager.bmyh') }}</yu-button>
              <yu-button v-if="checkCtrl('delete')" @click="deleteFn(scope.row)" type="text">{{ $t('dptManager.sc') }}</yu-button>
            </yu-button-drop>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
    <!--部门 新增 修改 详情-->
    <yu-xdialog :title="viewTitle[viewType]" :visible.sync="dialogVisible" size="small" @close="dialogCloseFn">
      <yu-xform ref="dptForm" v-model="formdata" :form-type="formType" label-width="120px" :rules="formRules">
        <yu-xform-group columns="4">
          <yu-xform-item :label="$t('dptManager.bmmc')" name="dptName" :placeholder="$t('dptManager.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('dptManager.bmdm')" name="dptCode" :placeholder="$t('dptManager.qsr')" :disabled="viewType != 'ADD'"></yu-xform-item>
          <!--所在机构-->
          <yu-xform-item
            :label="$t('dptManager.ssjg')"
            name="orgName"
            ctype="yufp-search-tree"
            :params="orgTreeParams"
            :select-value="formdata.orgId"
            :details="viewType === 'DETAIL'"
            :details-value="formdata.orgName"
            :disabled="isAddchild"
            @node-click="changeFormOrgFn"
            @clear="clearOrgFn"></yu-xform-item>
          <!--上级部门-->
          <yu-xform-item
            :label="$t('dptManager.sjbm')"
            name="upDptName"
            :placeholder="$t('dptManager.qxxzjg')"
            ctype="yufp-search-tree"
            :params="dptTreeParams"
            :select-value="formdata.upDptId"
            :details="viewType === 'DETAIL'"
            :details-value="formdata.upDptName"
            :disabled="isAddchild"
            @node-click="changeFormDptFn"
            @clear="clearUpdptFn"
            :empty-text="$t('dptManager.qxxzjg')"></yu-xform-item>
          <yu-xform-item :label="$t('dptManager.zt')" name="dptSts" :placeholder="$t('dptManager.qsr')" ctype="select" data-code="DATA_STS"></yu-xform-item>
          <yu-xform-item :label="$t('dptManager.zxbgyh')" name="lastChgName" :hidden="true" :disabled="true"></yu-xform-item>
          <yu-xform-item :label="$t('dptManager.zxbgsj')" name="lastChgDt" :hidden="true" :disabled="true"></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
      <div slot="footer" class="yu-grpButton">
        <yu-button type="primary" v-if="formType==='edit'" key="edit" v-norepeat.disabled @click="saveDptFn">{{ $t('dptManager.bc') }}</yu-button>
        <yu-button v-if="checkCtrl('update') && viewType === 'DETAIL'" type="primary" @click="switchStatus('EDIT', true)">{{ $t('dptManager.xg') }}</yu-button>
        <yu-button v-if="formType === 'details'" @click="cancelFn">{{ $t('dptManager.fh') }}</yu-button>
        <yu-button v-else @click="cancelFn">{{ $t('dptManager.qx') }}</yu-button>
      </div>
    </yu-xdialog>
    <!-- 部门成员查看 -->
    <yu-xdialog :title="$t('dptManager.bmyh')" :visible.sync="memberDialogVisible" size="small">
      <yu-xform ref="memberForm" related-table-name="memberTable" form-type="search" label-width="90px">
        <yu-xform-group :column="2">
          <yu-xform-item name="loginCode" :label="$t('dptManager.zh')" :placeholder="$t('dptManager.qsr')"></yu-xform-item>
          <yu-xform-item name="userName" :label="$t('dptManager.ygxm')" :placeholder="$t('dptManager.qsr')"></yu-xform-item>
          <yu-xform-item name="userCode" :label="$t('dptManager.ygh')" :placeholder="$t('dptManager.qsr')"></yu-xform-item>
          <yu-xform-item name="userSts" :label="$t('dptManager.zt')" :placeholder="$t('dptManager.qsr')" ctype="select" data-code="DATA_STS"></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
      <!--部门成员列表-->
      <yu-xtable request-type="POST" ref="memberTable" row-number :data-url="memberTableUrl" v-if="memberTableParams.dptId" :base-params="memberTableParams" condition-key="">
        <yu-xtable-column :label="$t('dptManager.zh')" prop="loginCode"></yu-xtable-column>
        <yu-xtable-column :label="$t('dptManager.ygxm')" prop="userName"></yu-xtable-column>
        <yu-xtable-column :label="$t('dptManager.ygh')" prop="userCode"></yu-xtable-column>
        <yu-xtable-column :label="$t('dptManager.zt')" prop="userSts" data-code="DATA_STS"></yu-xtable-column>
      </yu-xtable>
    </yu-xdialog>
  </div>
</template>
<script>
import { clone, extend, lookup } from '@/utils'
import { validator } from "@/utils/validate"
import { checkBelongToChooseNode } from '@/utils/util'
import { mapGetters } from 'vuex';
lookup.reg('DATA_STS');
export default {
  data() {
    return {
      viewType: 'ADD', // 弹框模式
      viewTitle: lookup.find('CRUD_TYPE', false), // 弹出框类型
      formType: 'details', // 表单模式

      orgId: '', // 快速搜索 机构树
      orgTreeUrl: backend.appOcaService + '/api/adminsmorg/treequeryauth', // 机构树请求 url
      searchOrgId: null, // 高级搜索 机构树
      searchFormdata: {}, // 高级搜索 表单数据
      selected: false,
      tableUrl: backend.appOcaService + '/api/adminsmdpt/page', // 部门列表查询地址
      tableParams: {
      }, // 部门列表查询参数
      dptSts: {}, // 用户状态

      dialogVisible: false,
      isAddchild: false, // 新增子部门禁用所在机构下拉树和上级部门下拉树
      formdata: {

      },
      formDisabled: false,
      orgTreeParams: {
        dataId: 'orgId',
        dataLabel: 'orgName',
        dataPid: 'upOrgId',
        // dataParams: {orgSts: 'A'},
        dataUrl: backend.appOcaService + '/api/adminsmorg/treequeryauth',
        placeholder: this.$t('dptManager.qxz'),
        searchKey: 'orgName' // 树过滤关键字
      }, // 机构树选择
      // selectedOrgId: null, // 弹出框上级机构选中值

      dptTreeParams: {
        dataId: 'dptId',
        dataLabel: 'dptName',
        dataPid: 'upDptId',
        // localData: [],
        dataUrl: backend.appOcaService + '/api/adminsmdpt/tree',
        dataParams: {},
        placeholder: this.$t('dptManager.qxz'),
        searchKey: 'dptName' // 树过滤关键字
      },
      // selectedUpDptId: null,

      formRules: {
        dptName: [
          { required: true, message: this.$t('dptManager.qsrbmmc') },
          { max: 50, message: this.$t('dptManager.zdcdbcggzf') },
          { validator: validator.speChar, message: this.$t('dptManager.srxxbhtszf') }
        ],
        dptCode: [
          { required: true, message: this.$t('dptManager.qsrbmdm') },
          { max: 50, message: this.$t('dptManager.zdcdbcggzf') },
          { validator: validator.speChar, message: this.$t('dptManager.srxxbhtszf') }
        ],
        orgName: [{ required: true, message: this.$t('dptManager.qxxzjg') }],
        dptSts: [
          { required: true, message: this.$t('dptManager.qxzbmzt') },
          { validator: validator.speChar, message: this.$t('dptManager.qxzbmzt'), trigger: 'change' }
        ]
      },
      memberDialogVisible: false,
      memberTableUrl: backend.appOcaService + '/api/adminsmdpt/memberlist',
      memberTableParams: {
        dptId:""
      }
    };
  },
  computed: {
    ...mapGetters(['orgTreeData'])
  },
  watch: {
    orgId(newVal, oldVal) {
      this.$refs.tree.filter(newVal);
    },
    searchOrgId(newVal, oldVal) {
      this.$refs.searchTree.filter(newVal);
    }
  },
  mounted() {
    this.dptSts = lookup.find('DATA_STS', false);
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
      this.remoteTableData('panel', 'dptTable', 'searchFormdata')
    },
    switchStatus(viewType, editable) { // 初始面板标题、按钮等信息
      if (this.formdata && this.formdata.dptSts === 'A' && viewType === 'EDIT') {
        this.$message({
          message: this.$t('dptManager.znxztyhdqydsj'),
          type: 'warning'
        });
        return;
      }
      this.viewType = viewType;
      this.dialogVisible = true;
      this.formType = viewType === 'DETAIL' ? 'details' : 'edit';
      this.formDisabled = !editable;
    },
    /**
        * 快速查询
        * @param data 模糊查询关键字
        */
    fuzzyQuery(data) {
      var param = {
        orgId: this.orgId,
        keyWord: data.value
      };
      this.$refs.dptTable.remoteData(param);
    },
    tableSelectedChangeFn() {
      this.selected = this.$refs.dptTable.selections.length > 0;
    },
    /**
      * 通过所在机构查询部门列表
      * @param node 点击的节点信息
      * @param immidately 是否立即执行查询，区分快速查询和精确查询
        */
    changeOrgFn(node, immidately) {
      if (immidately) {
        this.orgName = node.orgId;
        this.$refs.dptTable.remoteData({orgId: node.orgId, keyWord: this.$refs.panel.inputVal});
        return;
      }
      this.searchFormdata.orgId = node.orgId;
      this.searchOrgId = node.orgId;
    },
    clearOrgIdFn(immidately) {
      if (immidately) {
        this.$refs.dptTable.remoteData({ keyWord: this.$refs.panel.inputVal});
        return;
      }
      this.searchFormdata.orgId = null;
      this.searchOrgId = null;
    },
    /**
      * 机构树搜索
      * @param nodeData 当前输入信息
      */
    filterNode(value, nodeData, node) {
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
      * @param nodeData 当前输入信息
      */
    filterNodeSearch(value, nodeData, node) {
      if (!this.$refs.searchTree.selectedLabel) {
        return true;
      }
      if (nodeData.orgName.indexOf(this.$refs.searchTree.selectedLabel) !== -1) {
        return true;
      }
      return checkBelongToChooseNode(this.$refs.searchTree.selectedLabel, node, 'orgName');

    },
    /**
      * 查询表单触发查询
      */
    searchFn() {
      this.$refs.dptTable.remoteData(this.searchFormdata);
    },
    /**
      * 清空表单查询参数
      */
    resetFn() {
      this.$refs.searchForm.resetFields();
      this.searchFormdata.upOrgId = '';
      this.searchOrgId = '';
    },
    addFn() { // 新增弹出方法
      this.switchStatus('ADD', true);
      this.$nextTick(function () {
        this.$refs.dptForm.resetFields(); // 重置form
        this.formdata.dptSts = 'W';
      });
    },
    deleteFn(row) { // 批量删除
      var _this = this;
      var ids = [];
      if (row.dptId) { // 行内操作
        if (row.dptSts === 'A') {
          this.$message({
            message: this.$t('dptManager.znscdqyhqydsj'),
            type: 'warning'
          });
          return;
        } else {
          ids.push(row.dptId);
        }

      } else if (this.$refs.dptTable.selections.length > 0) { // 按钮组
        for (var i = 0; i < this.$refs.dptTable.selections.length; i++) {
          var selection = this.$refs.dptTable.selections[i];
          if (selection.dptSts != 'A') {
            ids.push(selection.dptId);
          } else {
            this.$message({
              message: this.$t('dptManager.znscdqyhqydsj'),
              type: 'warning'
            });
            return;
          }
        }
      } else {
        _this.$message({
          message: _this.$t('dptManager.qxxzyscdsj'),
          type: 'warning'
        });
        return;
      }
      _this.$confirm(_this.$t('dptManager.cczjscgbmxxsfjx'), _this.$t('dptManager.ts'), {
        confirmButtonText: _this.$t('dptManager.qd'),
        cancelButtonText: _this.$t('dptManager.qx'),
        type: 'warning'
      }).then(function () {
        _this.$request({
          method: 'POST',
          url: backend.appOcaService + '/api/adminsmdpt/batchdelete',
          data: ids
        }).then(({code, message, data}) => {
        //处理请求成功的情况
          if (code === '0000') {
            _this.$message({
              message: _this.$t('dptManager.sjczcg')
            });
            _this.remoteData();
          } else {
            _this.$message({
              message: _this.$t('dptManager.sjczsb')
            });
          }
        })
      }).catch(function () {
        return;
      });
    },
    useFn() { // 批量启用
      var _this = this;
      if (this.$refs.dptTable.selections.length > 0) {
        var ids = [];
        for (var i = 0; i < this.$refs.dptTable.selections.length; i++) {
          var row = this.$refs.dptTable.selections[i];
          if (row.dptSts === 'W' || row.dptSts === 'I') {
            ids.push(row.dptId);
          } else {
            this.$message({
              message: this.$t('dptManager.znxztyhdqydsj'),
              type: 'warning'
            });
            return;
          }
        }
        this.$confirm(this.$t('dptManager.czzjqysxbm'), this.$t('dptManager.ts'), {
          confirmButtonText: this.$t('dptManager.qr'),
          cancelButtonText: this.$t('dptManager.qx'),
          type: 'warning',
          callback(action) {
            if (action === 'confirm') {
              // 发起启用用部门请求
              _this.$request({
                method: 'POST',
                url: backend.appOcaService + '/api/adminsmdpt/batchenable',
                data: ids
              }).then(({code, message, data}) => {
                //处理请求成功的情况
                if (code === '0000') {
                  _this.$message({
                    message: _this.$t('dptManager.sjczcg')
                  });
                  _this.remoteData();
                } else {
                  _this.$message({
                    message: _this.$t('dptManager.sjczsb')
                  });
                }
              })
            }
          }
        });
      } else {
        _this.$message({
          message: _this.$t('dptManager.qxxzyqydsj'),
          type: 'warning'
        });
        return;
      }
    },
    /**
        * 停用部门
        */
    stopFn() {
      // 校验是否已选择数据
      var _this = this;
      if (this.$refs.dptTable.selections.length < 1) {
        this.$message({
          message: this.$t('dptManager.qxzytjl'),
          type: 'warning'
        });
        return;
      }
      var selections = this.$refs.dptTable.selections;
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].dptSts === 'A') {
          // 只能停用已启用的机构
          ids.push(selections[i].dptId);
        } else {
          this.$message({
            message: this.$t('dptManager.znxzyqydsj'),
            type: 'warning'
          });
          return;
        }
      }
      this.$confirm(this.$t('dptManager.cczjtysyxzsj'), this.$t('dptManager.ts'), {
        confirmButtonText: this.$t('dptManager.qr'),
        cancelButtonText: this.$t('dptManager.qx'),
        type: 'warning',
        callback(action) {
          if (action === 'confirm') {
            // 发起停用机构请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmdpt/batchdisable',
              data: ids,
            }).then(({code, message, data}) => {
              //处理请求成功的情况
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('dptManager.sjczcg')
                });
                _this.remoteData();
              } else {
                _this.$message({
                  message: _this.$t('dptManager.sjczsb')
                });
              }
            })
          }
        }
      });
    },

    /**
        * 部门详情
        * @param row 当前机构信息
        */
    dptDetailFn(row) {
      this.switchStatus('DETAIL', true);
      this.$nextTick(function () {
        this.$refs.dptForm.resetFields(); // TODO 发/info接口
        clone(row, this.formdata);
      });
    },
    // 新增子部门
    addSubDpt(row) {
      if (row.dptSts !== 'A') {
        this.$message({
          message: this.$t('dptManager.dqytydbmbkxzzjg'),
          type: 'warning'
        });
        return;
      }
      this.isAddchild = true;
      this.switchStatus('ADD', true);
      this.$nextTick(function () {
        this.$refs.dptForm.resetFields(); // 重置form
        this.formdata.dptSts = 'W'; // 设置默认值
        this.formdata.orgId = row.orgId;
        this.formdata.upDptId = row.dptId;
        this.changeFormOrgFn(row);
      });
    },
    editDptFn(row) {
      if (row.dptSts === 'A') {
        this.$message({
          message: this.$t('dptManager.znxztyhdqydsj'),
          type: 'warning'
        });
        return;
      }
      this.switchStatus('EDIT', false);
      this.$nextTick(function () {
        this.$refs.dptForm.resetFields(); // 重置form
        extend(this.formdata, row);
        this.formdata.upDptId = row.upDptId === '0' ? null : row.upDptId;
        this.changeFormOrgFn(row);
      });
    },
    clearOrgFn() {
      this.formdata.orgName = null;
    },
    clearUpdptFn() {
      this.formdata.upDptId = null;
    },
    userListFn(row) {
      var _this = this;
      _this.memberDialogVisible = true;
      _this.$nextTick(function() {
        _this.$refs.memberForm.resetFields();
        _this.memberTableParams = {
          dptId: row.dptId
        };
      })
    },
    dialogCloseFn() {
      this.isAddchild = false;
    },
    /**
        * 表单机构树
        * @param node 当前选中机构信息
        */
    changeFormOrgFn(node) {
      var _this = this;
      this.formdata.orgId = node.orgId;
      this.formdata.orgName = node.orgName;
      // this.selectedOrgId = node.orgId;
      // 部门树联动
      var temp = extend(true, {}, _this.dptTreeParams);
      temp.dataParams = {orgId: node.orgId, dptSts: 'A'};
      _this.dptTreeParams = temp;
    },
    /**
        * 表单部门树
        * @param e 当前选中部门信息
        */
    changeFormDptFn(option) {
      this.formdata.upDptId = option.dptId;
    },
    /**
        * 保存机构
        * @param row 当前机构信息
        */
    saveDptFn() { // 新增保存方法
      var _this = this;
      var url = this.viewType == 'ADD' ? backend.appOcaService + '/api/adminsmdpt/add' : backend.appOcaService + '/api/adminsmdpt/update';
      this.$refs.dptForm.validate(function (valid) {
        if (valid) {
          var params = clone(_this.formdata);
          if (_this.viewType == 'ADD') {
            delete params.dptId;
          }
          if (!params.upDptId) {
            delete params.upDptId;
          }
          _this.$request({
            method: 'POST',
            url: url,
            data: params
          }).then(({code, message, data}) => {
            //处理请求成功的情况
            if (code === '0000') {
              _this.$message({
                message: _this.$t('dptManager.sjbccg')
              });
              _this.dialogVisible = false;
              _this.remoteData();
            } else {
              _this.$message({
                message: _this.$t('dptManager.sjbcsb'),
                type: 'warning'
              });
            }
          })
        }
      });
    },
    /**
        * 关闭弹出框
        */
    cancelFn() {
      this.dialogVisible = false;
    }
  }
}
</script>
