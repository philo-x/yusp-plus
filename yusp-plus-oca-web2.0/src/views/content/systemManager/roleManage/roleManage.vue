<!--
  @Created by zhuly8@yusys.com.cn 2021-01-06
  @updated by
  @description 角色管理
-->
<template>
  <div id="roleManage">
    <yu-panel
      ref="panel"
      :title="$t('roleManage.jsgl')"
      class="adjust-height"
      show-search-input
      :placeholder="$t('roleManage.gjz')"
      @search="fuzzyQuery">
      <!--角色列表操作按钮-->
      <template slot="right">
        <yu-toolBar>
          <yu-button v-if="checkCtrl('add')" @click="addRoleFn">{{ $t('roleManage.xz') }}</yu-button>
          <yu-button v-if="checkCtrl('batchenable')" v-norepeat.disabled @click="useRoleFn">{{ $t('roleManage.qy') }}</yu-button>
          <yu-button v-if="checkCtrl('batchdisable')" v-norepeat.disabled @click="stopRoleFn">{{ $t('roleManage.ty') }}</yu-button>
          <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled @click="deleteFn">{{ $t('roleManage.sc') }}</yu-button>
        </yu-toolBar>
      </template>
      <!-- 快速查询机构查询 -->
      <template slot="search">
        <yu-combo-tree
          ref="tree"
          v-model="orgName"
          :placeholder="$t('roleManage.qbjg')"
          :local-data="orgTreeData"
          data-id="orgId"
          data-label="orgName"
          data-pid="upOrgId"
          :clearable="true"
          @node-click="changeOrgIdFn($event, true)"
          @clear="clearUpOrgIdFn(true)"
          :filter-node-method="filterNode"
          :all-node-value="true"
          :max-height="400">
        </yu-combo-tree>
      </template>
      <!--角色列表查询条件-->
      <template slot="filter">
        <yu-xform ref="searchForm" related-table-name="rolesTable" v-model="searchFormdata" form-type="search" @form-search="clearFuzzyFn" @form-reset="formResetFn('role')">
          <yu-xform-group :column="4">
            <yu-xform-item :label="$t('roleManage.jsdm')" name="roleCode" :placeholder="$t('roleManage.qsr')"></yu-xform-item>
            <yu-xform-item :label="$t('roleManage.jsmc')" name="roleName" :placeholder="$t('roleManage.qsr')"></yu-xform-item>
            <yu-xform-item :label="$t('roleManage.zt')" name="roleSts" :placeholder="$t('roleManage.qxz')" ctype="select" data-code="DATA_STS"></yu-xform-item>
            <yu-xform-item name="orgName" :label="$t('roleManage.ssjg')" ctype="custom">
              <yu-combo-tree
                ref="searchTree"
                v-model="searchOrgName"
                :placeholder="$t('roleManage.qxz')"
                :local-data="orgTreeData"
                data-id="orgId"
                data-label="orgName"
                data-pid="upOrgId"
                :clearable="true"
                @node-click="changeOrgIdFn($event, false)"
                @clear="clearUpOrgIdFn(false)"
                :filter-node-method="filterNodeSearch"
                :all-node-value="true"
                :max-height="400">
              </yu-combo-tree>
            </yu-xform-item>
          </yu-xform-group>
        </yu-xform>
      </template>
      <!--角色列表-->
      <yu-xtable request-type="POST" ref="rolesTable" row-number :data-url="tableUrl" selection-type="checkbox" @select="selectFn($event, 'roleSelected')">
        <yu-xtable-column :label="$t('roleManage.jsmc')">
          <template slot-scope="scope">
            <a class="underline" @click="orgDetailFn(scope.row)">{{ scope.row.roleName }}</a>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('roleManage.jsdm')" prop="roleCode"> </yu-xtable-column>
        <!-- <yu-xtable-column :label="$t('roleManage.zt')" prop="roleSts" data-code="DATA_STS"></yu-xtable-column> -->
        <yu-xtable-column :label="$t('roleManage.zt')">
          <template slot-scope="scope">
            <yu-tag v-if="scope.row.roleSts=== 'A'" type="success">{{ roleSts[scope.row.roleSts] }}</yu-tag>
            <yu-tag v-if="scope.row.roleSts=== 'I'" type="danger">{{ roleSts[scope.row.roleSts] }}</yu-tag>
            <yu-tag v-if="scope.row.roleSts=== 'W'" type="warning">{{ roleSts[scope.row.roleSts] }}</yu-tag>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('roleManage.ssjg')" prop="orgName"></yu-xtable-column>
        <yu-xtable-column :label="$t('roleManage.zxbg')">
          <template slot-scope="scope" v-if="scope.row.lastChgName" width="260">
            <span>{{ scope.row.lastChgName + '（' + scope.row.lastChgDt + '）' }}</span>
          </template>
        </yu-xtable-column>
        <yu-xtable-column fixed="right" :label="$t('component.operate')" width="160">
          <template slot-scope="scope">
            <yu-button-drop set-index="0" :show-length="2" type="text">
              <yu-button v-if="checkCtrl('userlist')" @click="openRoleUserFn(scope.row)" type="text">{{ $t('roleManage.jsyh') }}</yu-button>
              <yu-button v-if="checkCtrl('edit')" @click="editFn(scope.row)" type="text">{{ $t('roleManage.xg') }}</yu-button>
              <yu-button v-if="checkCtrl('batchenable') && scope.row.roleSts != 'A'" v-norepeat.disabled @click="useRoleFn(scope.row)" type="text">{{ $t('roleManage.qy') }}</yu-button>
              <yu-button v-if="checkCtrl('batchdisable') && scope.row.roleSts != 'I'" v-norepeat.disabled @click="stopRoleFn(scope.row)" type="text">{{ $t('roleManage.ty') }}</yu-button>
              <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled @click="deleteFn(scope.row)" type="text">{{ $t('roleManage.sc') }}</yu-button>
            </yu-button-drop>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
    <!--角色 新增 修改 详情-->
    <yu-xdialog :title="viewTitle[viewType]" :visible.sync="dialogVisible" size="tiny">
      <yu-xform ref="roleForm" v-model="formdata" :form-type="formType" label-width="100px" :rules="formRules">
        <yu-xform-group :column="1">
          <yu-xform-item :label="$t('roleManage.jsmc')" name="roleName" :placeholder="$t('roleManage.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('roleManage.jsdm')" name="roleCode" :placeholder="$t('roleManage.qsr')" :disabled="viewType != 'ADD'"></yu-xform-item>
          <yu-xform-item :label="$t('roleManage.ssjg')" name="orgName" @clear="formdata.orgName = ''" ctype="yufp-search-tree" :params="treeProps" :select-value="selectValue" :details="viewType==='DETAIL'" :details-value="detailsValue" @node-click="changeOrgFn"></yu-xform-item>
          <yu-xform-item :label="$t('roleManage.zt')" name="roleSts" ctype="select" :placeholder="$t('roleManage.qsr')" data-code="DATA_STS"></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
      <div slot="footer" class="yu-grpButton">
        <yu-button type="primary" key="edit" v-if="formType==='edit'" v-norepeat.disabled @click="saveRoleFn">{{ $t('roleManage.bc') }}</yu-button>
        <yu-button type="primary" v-if="checkCtrl('edit')&& viewType === 'DETAIL'" @click="switchStatus('EDIT', true)">{{ $t('roleManage.xg') }}</yu-button>
        <yu-button v-if="formType === 'details'" @click="cancelFn">{{ $t('roleManage.fh') }}</yu-button>
        <yu-button v-else @click="cancelFn">{{ $t('roleManage.qx') }}</yu-button>
      </div>
    </yu-xdialog>
    <!--角色用户-->
    <yu-xdialog :title="$t('roleManage.jsyh')" :visible.sync="userDialogVisible" size="small" class="role-user-xdialog">
      <!-- 已关联用户列表查询区域 -->
      <yu-xform ref="userSearchForm" related-table-name="userTable" v-model="userSearchFormdata">
        <yu-xform-group :column="2">
          <yu-xform-item name="orgId" :label="$t('roleManage.ssjg')" ctype="custom">
            <yu-combo-tree
              ref="userSearchTree"
              v-model="userSearchOrgName"
              :placeholder="$t('roleManage.qxz')"
              :data-url="orgTreeInUserListUrl"
              request-type="POST"
              :data-params="treeParams"
              data-id="orgId"
              data-label="orgName"
              data-pid="upOrgId"
              :clearable="true"
              @node-click="changeUserOrgIdFn"
              @clear="clearUpOrgIdFn(false)"
              :filter-node-method="filterNodeUserSearch"
              :max-height="400"
              :all-node-value="true">
            </yu-combo-tree>
          </yu-xform-item>
          <yu-xform-item :label="$t('roleManage.zh')" name="loginCode" :placeholder="$t('roleManage.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('roleManage.yhmc')" name="userName" :placeholder="$t('roleManage.qsr')"></yu-xform-item>
          <div slot="custom">
            <yu-button type="primary" @click="searchFn" style="margin-left:100px" v-norepeat.disabled>{{ $t('roleManage.search') }}</yu-button>
            <yu-button @click="formResetFn('user')">{{ $t('roleManage.reset') }}</yu-button>
          </div>
        </yu-xform-group>
      </yu-xform>
      <div class="dialog-btn">
        <div class="btn">
          <yufp-user-selector :button-model="true" :params="userParams" @select-fn="saveUserRelFn"></yufp-user-selector>
        </div>
        <yu-button @click="delUserRelFn">{{ $t('roleManage.tcyh') }}</yu-button>
      </div>
      <!-- 已关联用户列表 -->
      <yu-xtable request-type="POST" ref="userTable" row-number :data-url="userTableUrl" selection-type="checkbox" :base-params="userTableParams" :default-load="false" @select="selectFn($event, 'userSelected')">
        <yu-xtable-column :label="$t('roleManage.yhmc')" prop="userName"> </yu-xtable-column>
        <yu-xtable-column :label="$t('roleManage.zh')" prop="loginCode"></yu-xtable-column>
        <yu-xtable-column :label="$t('roleManage.ssjg')" prop="orgName"></yu-xtable-column>
        <yu-xtable-column fixed="right" :label="$t('component.operate')">
          <template slot-scope="scope">
            <yu-button v-ctrl:removeuserrolerel type="text" @click="delUserRelFn(scope.row)">{{ $t('roleManage.tcyh') }}</yu-button>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-xdialog>
  </div>
</template>
<script>
import { clone, lookup } from '@/utils'
import YufpSearchTree from "@/components/widgets/YufpSearchTree";
import YufpUserSelector from "@/components/widgets/YufpUserSelector";
import { checkBelongToChooseNode } from '@/utils/util'
import { mapGetters } from "vuex"
lookup.reg('DATA_STS,SEX_TYPE,REL_STS');
export default {
  components: { YufpSearchTree, YufpUserSelector },
  data() {
    return {
      formdata: {
        roleId: ''
      }, // 表单数据
      tableUrl: backend.appOcaService + '/api/adminsmrole/page', // 角色列表查询地址
      dialogVisible: false, // 弹出框是否展示
      viewType: 'ADD', // 弹出框默认新增
      viewTitle: lookup.find('CRUD_TYPE', false), // 弹出框类型
      formDisabled: false, // 表单是否禁用
      treeUrl: backend.appOcaService + '/api/adminsmorg/treequeryauth', // 角色列表机构树请求 url
      props: {label: 'orgName', children: 'children'},
      formType: 'details',
      orgName: '', // 机构树搜索关键字
      formRules: {
        roleName: [
          { required: true, message: this.$t('roleManage.btx')},
          { max: 33, message: this.$t('roleManage.srzgc')}
        ], // 角色名称校验
        roleCode: [
          {required: true, message: this.$t('roleManage.btx')},
          { max: 33, message: this.$t('roleManage.srzgc'), trigger: 'blur'}
        ], // 角色代码校验
        orgName: {required: true, message: this.$t('roleManage.btx')}, // 必填项校验
        roleSts: {required: true, message: this.$t('roleManage.btx')} // 必填项校验
      }, // 表单验证规则
      userDialogVisible: false, // 角色用户弹框是否显示
      userFormdata: {}, // 角色用户查询表单数据
      userTableParams: {roleId: '', checked: true }, // 角色用户表查询参数
      treeProps: {
        dataId: 'orgId',
        dataLabel: 'orgName',
        dataPid: 'upOrgId',
        dataUrl: backend.appOcaService + '/api/adminsmorg/treequeryauth',
        placeholder: this.$t('roleManage.qxz'),
        searchKey: 'orgName', // 树过滤关键字
        dataParams: {
          orgSts: 'A'
        } // 额外请求参数 默认查询生效的机构
      }, // 机构树选择
      selectValue: '', // 弹出框上级机构显示值
      detailsValue: '', // 搜索树详情字段
      searchOrgName: '', // 查询机构名称
      searchFormdata: {orgId: ''}, // 查询表单数据
      orgTreeInUserListUrl: backend.appOcaService + '/api/adminsmrole/orgtree', // 角色关联用户列表机构树请求 url
      treeParams: {roleId: null},
      userSearchFormdata: {}, // 用户列表查询表单数据
      userTableUrl: backend.appOcaService + '/api/adminsmrole/userlist', // 角色关联用户列表查询地址
      userSelected: true, // 已关联用户列表批量按钮是否禁用
      roleSelected: true, // 角色列表批量按钮是否禁用
      userSearchOrgName: '', // 所属机构
      userParams: {
        orgTreeUrl: backend.appOcaService + '/api/adminsmrole/orgtree', // 角色关联用户列表机构树请求 url
        treeParams: {roleId: null},
        userTableUrl: backend.appOcaService + '/api/adminsmrole/userlist',
        dialogTitle: this.$t('roleManage.xzyh'),
        dataParams: {
          roleId: '',
          checked: false
        }
      },
      roleSts: {}
    };
  },
  computed: {
    ...mapGetters([
      "orgTreeData",
    ])
  },
  watch: {
    orgName(newVal, oldVal) {
      this.$refs.tree.filter(newVal);
    },
    searchOrgName(newVal, oldVal) {
      this.$refs.searchTree.filter(newVal);
    },
    userSearchOrgName(newVal, oldVal) {
      this.$refs.userSearchTree.filter(newVal);
    }
  },
  created () {
    this.$store.dispatch('funData/orgTreeFn').then(res => {
    })
  },
  mounted() {
    this.roleSts = lookup.find('DATA_STS', false);
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
      this.remoteTableData('panel', 'rolesTable', 'searchFormdata')
    },
    searchFn() {
      this.$refs.userTable.remoteData(this.userSearchFormdata);
    },
    /**
    * 勾选表格
    * @param selection 当前勾选数据
    */
    selectFn(selection, disabledName) {
      if (selection.length > 0) {
        this[disabledName] = false;
        return;
      }
      this[disabledName] = true;
    },
    /**
    * 快速查询
    * @param e 模糊查询关键字
    */
    fuzzyQuery(e) {
      var param = {keyWord: e.value, orgId: this.orgName};
      this.$refs.rolesTable.remoteData(param);
      this.resetFn(); // 清空精确查询条件
    },
    /**
    * 通过所属机构查询
    * @param node 点击的节点信息
    * @param immidately 是否立即执行查询，区分快速查询和精确查询
    */
    changeOrgIdFn(node, immidately) {
      if (immidately) {
        this.orgName = node.orgId;
        this.$refs.rolesTable.remoteData({orgId: node.orgId, keyWord: this.$refs.panel.inputVal});
        this.resetFn(); // 清空精确查询条件
        return;
      }
      this.searchFormdata.orgId = node.orgId;
      this.searchOrgName = node.orgId;
    },
    /**
    * 清空所属机构查询条件
    * @param immidately 是否立即执行查询，区分快速查询和精确查询
    */
    clearUpOrgIdFn(immidately) {
      if (immidately) {
        this.$refs.rolesTable.remoteData({orgId: '', keyWord: this.$refs.panel.inputVal});
        return;
      }
      this.searchFormdata.orgId = '';
      this.searchOrgName = '';
    },
    /**
    * 清空快速查询区域查询条件
    */
    clearFuzzyFn() {
      this.orgName = '';
      this.$refs.panel.inputVal = '';
    },
    /**
    * 清空所属机构查询条件
    * @param immidately 是否立即执行查询，区分快速查询和精确查询
    */
    resetFn() {
      this.$refs.searchForm.resetFields();
      this.searchFormdata.orgId = '';
      this.searchOrgName = '';
    },
    /**
    * 关闭弹出框
    */
    cancelFn() {
      this.dialogVisible = false;
    },
    /**
    * 控制保存按钮、xdialog、表单的状态
    * @param viewType 表单类型
    * @param editable 可编辑,默认false
    */
    switchStatus(viewType, editable) {
      if (this.formdata && this.formdata.roleSts === 'A' && viewType === 'EDIT') {
        this.$message({
          message: this.$t('roleManage.znxzsxhdsxdsj'),
          type: 'warning'
        });
        return;
      }
      this.viewType = viewType;
      this.dialogVisible = true;
      this.formType = viewType === 'DETAIL' ? 'details' : 'edit';
      this.formDisabled = !editable;
    },
    editFn(row) {
      if (row.roleSts === 'A') {
        this.$message({
          message: this.$t('roleManage.znxzsxhdsxdsj')
        });
        return;
      }
      this.switchStatus('EDIT', true);
      this.$nextTick(function () {
        this.$refs.roleForm.resetFields();
        clone(row, this.formdata);
        this.selectValue = this.formdata.orgId;
      });
    },
    /**
    * 角色详情
    * @param row 当前角色信息
    */
    orgDetailFn(row) {
      this.switchStatus('DETAIL', true);
      this.$nextTick(function () {
        this.$refs.roleForm.resetFields();
        clone(row, this.formdata);
        this.selectValue = this.formdata.orgId;
        this.detailsValue = this.formdata.orgName;
      });
    },
    /**
    * 新增角色
    */
    addRoleFn() {
      this.switchStatus('ADD', true);
      this.$nextTick(function () {
        this.$refs.roleForm.resetFields();
        this.selectValue = '';
        this.formdata.roleSts = 'W';
        this.formdata.orgId = '';
        this.formdata.roleId = '';
      });
    },
    /**
    * 模糊机构树搜索
    * @param value 当前输入信息
    * @param nodeData 当前节点属性信息
    * @param node 当前节点信息
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
    * @param value 当前输入信息
    * @param nodeData 当前节点属性信息
    * @param node 当前节点信息
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
    * 选择所属机构
    * @param node 当前选中机构信息
    */
    changeOrgFn(node) {
      this.formdata.orgId = node.orgId;
      this.formdata.orgName = node.orgName;
      this.selectValue = node.orgId;
    },
    /**
    * 保存角色
    * @param row 当前角色信息
    */
    saveRoleFn() {
      var _this = this;
      var validate = false;
      _this.$refs.roleForm.validate(function (valid) {
        validate = valid;
      });
      if (!validate) {
        return;
      }
      var url = _this.formdata.roleId ? '/api/adminsmrole/update' : '/api/adminsmrole/add';
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + url,
        data: _this.formdata
      }).then(({code, message, data, total}) => {
        if (code === '0000') {
          _this.$message({
            message: _this.$t('roleManage.sjbccg'),
            type: 'success'
          });
          _this.dialogVisible = false;
          _this.remoteData();
        } else {
          _this.$message({
            message: message,
            type: 'error'
          });
        }
      });
    },
    /**
    * 启用角色
    */
    useRoleFn(row) {
      var _this = this;
      var selections = row.roleId ? [row] : _this.$refs.rolesTable.selections;
      if (selections.length < 1) {
        _this.$message({ message: _this.$t('roleManage.qxzytsj'), type: 'warning' });
        return;
      }
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].roleSts === 'W' || selections[i].roleSts === 'I') {
          // 只能选择启用活 待启用状态的角色
          ids.push(selections[i].roleId);
        } else {
          this.$message({
            message: this.$t('roleManage.znxzsxhdsxdsj'),
            type: 'warning'
          });
          return;
        }
      }
      this.$confirm(this.$t('roleManage.cczjqygjssfjx'), this.$t('roleManage.ts'), {
        confirmButtonText: this.$t('roleManage.qd'),
        cancelButtonText: this.$t('roleManage.qx'),
        type: 'warning',
        callback(action) {
          if (action === 'confirm') {
            // 发起启用用角色请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmrole/batchenable',
              data: ids,
            }).then(({code, message, data, total}) => {
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('roleManage.gxcg'),
                  type: 'success'
                });
                _this.remoteData();
              } else {
                _this.$message({
                  message: message,
                  type: 'error'
                });
              }
            });
          }
        }
      });
    },
    /**
    * 停用角色
    */
    stopRoleFn(row) {
      // 校验是否已选择数据
      var _this = this;
      var selections = row.roleId ? [row] : _this.$refs.rolesTable.selections;
      if (selections.length < 1) {
        this.$message({ message: this.$t('roleManage.qxzytsj'), type: 'warning' });
        return;
      }
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].roleSts === 'A') {
          // 只能停用生效的角色
          ids.push(selections[i].roleId);
        } else {
          this.$message({
            message: this.$t('roleManage.znxzsxdsj'),
            type: 'warning'
          });
          return;
        }
      }
      this.$confirm(this.$t('roleManage.cczjtygjssfjx'), this.$t('roleManage.ts'), {
        confirmButtonText: this.$t('roleManage.qd'),
        cancelButtonText: this.$t('roleManage.qx'),
        type: 'warning',
        callback(action) {
          if (action === 'confirm') {
            // 发起停用角色请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmrole/batchdisable',
              data: ids,
            }).then(({code, message, data, total}) => {
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('roleManage.gxcg'),
                  type: 'success'
                });
                _this.remoteData();
              } else {
                _this.$message({
                  message: message,
                  type: 'error'
                });
              }
            });
          }
        }
      });
    },
    /**
    * 删除角色
    * @param row 当前行数据
    */
    deleteFn(row) {
      var _this = this;
      var selections = row.orgId ? [row] : this.$refs.rolesTable.selections;
      if (selections.length < 1) {
        this.$message({
          message: this.$t('roleManage.qxxzyscdsj'),
          type: 'warning'
        });
        return;
      }
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].roleSts === 'W' || selections[i].roleSts === 'I') {
          // 只能删除待生效停用状态的角色
          ids.push(selections[i].roleId);
        } else {
          this.$message({
            message: this.$t('roleManage.znxzsxhdsxdsj'),
            type: 'warning'
          });
          return;
        }
      }
      this.$confirm(this.$t('roleManage.cczjscgjsxxsfjx'), this.$t('roleManage.ts'), {
        confirmButtonText: this.$t('roleManage.qd'),
        cancelButtonText: this.$t('roleManage.qx'),
        type: 'warning',
        callback(action) {
          if (action === 'confirm') {
            // 发起停用角色请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmrole/batchdelete',
              data: ids,
            }).then(({code, message, data, total}) => {
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('roleManage.gxcg'),
                  type: 'success'
                });
                _this.remoteData();
              } else {
                _this.$message({
                  message: message
                });
              }
            });
          }
        }
      });
    },
    /**
    * 角色用户
    */
    openRoleUserFn(row) {
      this.userDialogVisible = true;
      this.treeParams = {roleId: row.roleId};
      this.$nextTick(function () {
        this.$refs.userSearchForm.resetFields();
        this.userSearchOrgName = '';
        this.userTableParams.roleId = row.roleId;
        this.$refs.userTable.remoteData(this.userTableParams); // todo 1 表格默认查询查询改变 自动触发查询； base-params 和查询表单参数是合并的
        this.userSearchFormdata.roleId = row.roleId;
        this.userParams.dataParams.roleId = row.roleId;
        this.userParams.treeParams = {roleId: row.roleId};
      });
    },
    /**
    * 所属机构表单清空
    * @param type 清空角色机构查询或用户机构查询
    */
    formResetFn(type) {
      if (type === 'role') {
        this.searchFormdata.orgId = '';
        this.searchOrgName = '';
        return;
      }
      this.$refs.userSearchForm.resetFields();
      this.userSearchFormdata.orgId = '';
      this.userSearchOrgName = '';
      this.$refs.userTable.remoteData();
    },
    /**
    * 角色用户机构树搜索
    * @param value 当前输入信息
    * @param nodeData 当前节点属性信息
    * @param node 当前节点信息
    */
    filterNodeUserSearch(value, nodeData, node) {
      if (!this.$refs.userSearchTree.selectedLabel) {
        return true;
      }
      if (nodeData.orgName.indexOf(this.$refs.userSearchTree.selectedLabel) !== -1) {
        return true;
      }
      return checkBelongToChooseNode(this.$refs.userSearchTree.selectedLabel, node, 'orgName');
    },
    /**
    * 通过所属机构查询
    * @param node 点击的节点信息
    */
    changeUserOrgIdFn(node) {
      this.userSearchFormdata.orgId = node.orgId;
      this.userSearchOrgName = node.orgId;
    },
    /**
    * 添加用户
    * @param selections 选中的用户数据
    */
    saveUserRelFn(selections) {
      var _this = this;
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        ids.push(selections[i].userId);
      }
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/adminsmrole/adduserrolerel/' + _this.userSearchFormdata.roleId,
        data: ids,
      }).then(({code, message, data, total}) => {
        if (code === '0000') {
          _this.$message({
            message: this.$t('roleManage.glgxcg'),
            type: 'success'
          });
          _this.remoteTableData('panel', 'userTable', 'searchFormdata');
        } else {
          _this.$message({
            message: message,
            type: 'error'
          });
        }
      });
    },
    /**
    * 移除用户
    * @param row 当前行用户数据
    */
    delUserRelFn(row) {
      // 校验是否已选择数据
      var _this = this;
      var selections = row.userId ? [row] : _this.$refs.userTable.selections;
      if (selections.length < 1) {
        this.$message({ message: this.$t('roleManage.qxzytsj'), type: 'warning' });
        return;
      }
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        ids.push(selections[i].userId);
      }
      this.$confirm(this.$t('roleManage.cczjtcsxyhjssfjx'), this.$t('roleManage.ts'), {
        confirmButtonText: this.$t('roleManage.qd'),
        cancelButtonText: this.$t('roleManage.qx'),
        type: 'warning',
        callback(action) {
          if (action === 'confirm') {
            // 发起剔除用户请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmrole/removeuserrolerel/' + _this.userSearchFormdata.roleId,
              data: ids,
            }).then(({code, message, data, total}) => {
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('roleManage.jcglgxcg'),
                  type: 'success'
                });
                _this.remoteTableData('panel', 'userTable', 'searchFormdata');
              } else {
                _this.$message({
                  message: message
                });
              }
            });
          }
        }
      });
    }
  }
}
</script>
<style>
.dialog-btn{
  margin-bottom: 20px;
}
.compact .dialog-btn{
  margin-bottom: 16px;
}
</style>
