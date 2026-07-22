
<template>
  <!-- @description 租户管理-->
  <div class="tenantManage">
    <yu-panel :title="$t('tenantManager.zhgl')" :collaspase-text="$t('tenantManager.gjcx')">
      <template slot="right">
        <yu-toolbar>
          <yu-button @click="addTenantFn">{{ $t('sysUserManager.xz') }}</yu-button>
          <yu-button v-norepeat.disabled @click="useTenantFn">{{ $t('tenantManager.jh') }}</yu-button>
          <yu-button v-norepeat.disabled @click="stopTenantFn">{{ $t('tenantManager.zx') }}</yu-button>
          <!-- <yu-button v-norepeat.disabled @click="deleteFn">{{ $t('sysUserManager.sc') }}</yu-button> -->
        </yu-toolbar>
      </template>
      <template slot="filter">
        <yu-xform ref="searchForm" related-table-name="tenantTable" v-model="searchFormdata" form-type="search" @form-reset="formResetFn">
          <yu-xform-group :column="4">
            <yu-xform-item :label="$t('tenantManager.zhmc')" :placeholder="$t('tenantManager.qsr')" ctype="input" name="tenantName"></yu-xform-item>
            <yu-xform-item :label="$t('tenantManager.dwmc')" :placeholder="$t('tenantManager.qsr')" ctype="input" name="companyName"></yu-xform-item>
            <yu-xform-item :label="$t('tenantManager.zt')" :placeholder="$t('tenantManager.qxz')" ctype="select" name="tenantSts" :options="TENANT_STS"></yu-xform-item>
          </yu-xform-group>
        </yu-xform>
      </template>
      <yu-xtable request-type="POST" ref="tenantTable" row-number :data-url="tableUrl" v-model="tenantTable" selection-type="checkbox">
        <yu-xtable-column :label="$t('tenantManager.zhmc')">
          <template slot-scope="scope">
            <a class="underline" @click="editFn(scope.row, 'detail')">{{ scope.row.tenantName }}</a>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('tenantManager.dwmc')" prop="companyName"></yu-xtable-column>
        <yu-xtable-column :label="$t('tenantManager.zhzt')">
          <template slot-scope="scope">
            <yu-tag v-if="scope.row.tenantSts=== '0'" type="success">{{ $t('tenantManager.hy') }}</yu-tag>
            <yu-tag v-if="scope.row.tenantSts=== '1'" type="danger">{{ $t('tenantManager.zx') }}</yu-tag>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('tenantManager.zxbgsj')" prop="lastChgDt"></yu-xtable-column>
        <yu-xtable-column :label="$t('tenantManager.cz')" width="110">
          <template slot-scope="scope">
            <yu-button-drop set-index="0" :show-length="2" type="text">
              <yu-button @click="editFn(scope.row)" type="text">{{ $t('sysUserManager.xg') }}</yu-button>
              <yu-button v-norepeat.disabled @click="useTenantFn(scope.row)" type="text">{{ $t('sysUserManager.jh') }}</yu-button>
              <yu-button v-norepeat.disabled @click="stopTenantFn(scope.row)" type="text">{{ $t('sysUserManager.zx') }}</yu-button>
              <!-- <yu-button v-norepeat.disabled @click="deleteFn(scope.row)" type="text">{{ $t('sysUserManager.sc') }}</yu-button> -->
            </yu-button-drop>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
    <yu-xdialog :title="dialogTittle" :visible.sync="relDialogVisible" width="800px" height="600px" class="no-body-padding" @close="activeName = 'info'">
      <yu-tabs v-model="activeName">
        <yu-tab-pane :label="$t('tenantManager.zhxgxx')" name="info">
          <yu-xform ref="tenantForm" v-model="tenantForm" :form-type="formType" label-width="120px" :rules="formRules">
            <yu-panel :title="$t('tenantManager.zhxx')" panel-type="simple" is-collapse :collapse-title="collapseTitle">
              <yu-xform-group>
                <yu-xform-item :label="$t('tenantManager.zhmc')" name="tenantName" ctype="input" :placeholder="$t('tenantManager.qsr')" :disabled="isSave"></yu-xform-item>
                <yu-xform-item :label="$t('tenantManager.dwmc')" name="companyName" ctype="input" :placeholder="$t('tenantManager.qsr')"></yu-xform-item>
                <yu-xform-item :label="$t('tenantManager.zhzt')" name="tenantSts" ctype="select" :placeholder="$t('tenantManager.qxz')" :options="TENANT_STS"></yu-xform-item>
              </yu-xform-group>
            </yu-panel>
            <yu-panel :title="$t('tenantManager.zhdjjg')" panel-type="simple" is-collapse :collapse-title="collapseTitle">
              <yu-xform-group>
                <yu-xform-item :label="$t('tenantManager.jgmc')" name="orgName" ctype="input" :placeholder="$t('sysUserManager.qsr')"></yu-xform-item>
                <yu-xform-item :label="$t('tenantManager.jgdm')" name="orgCode" ctype="input" :placeholder="$t('sysUserManager.qsr')" :disabled="isSave"></yu-xform-item>
              </yu-xform-group>
            </yu-panel>
            <yu-panel :title="$t('tenantManager.zhglyjs')" panel-type="simple" is-collapse :collapse-title="collapseTitle">
              <yu-xform-group>
                <yu-xform-item :label="$t('tenantManager.jsmc')" name="roleName" ctype="input" :placeholder="$t('sysUserManager.qsr')"></yu-xform-item>
                <yu-xform-item :label="$t('tenantManager.jsdm')" name="roleCode" ctype="input" :placeholder="$t('sysUserManager.qsr')" :disabled="isSave"></yu-xform-item>
              </yu-xform-group>
            </yu-panel>
            <yu-panel :title="$t('tenantManager.zhglyyh')" panel-type="simple" is-collapse :collapse-title="collapseTitle">
              <yu-xform-group>
                <yu-xform-item :label="$t('tenantManager.glymc')" name="userName" ctype="input" :placeholder="$t('sysUserManager.qsr')"></yu-xform-item>
                <yu-xform-item :label="$t('tenantManager.glyzh')" name="loginCode" ctype="input" :notice="$t('sysUserManager.xzhglyzhbnxg')" :placeholder="$t('sysUserManager.qsr')" :disabled="isSave" @change="codeChange"></yu-xform-item>
                <yu-xform-item :label="$t('tenantManager.mm')" name="userPass" disabled :notice="$t('sysUserManager.mmmrwxtcsmm')"></yu-xform-item>
                <yu-xform-item :label="$t('tenantManager.glygh')" name="userCode" ctype="input" :placeholder="$t('sysUserManager.qsr')" :disabled="isSave"></yu-xform-item>
              </yu-xform-group>
            </yu-panel>
          </yu-xform>
          <div class="foot-btn">
            <yu-button v-if="!isDetail" v-norepeat.disabled type="primary" @click="saveInfoFn">{{ $t('sysUserManager.bc') }}</yu-button>
            <yu-button v-else type="primary" @click="handleClickEdit">{{ $t('orgInfoManager.xg') }}</yu-button>
            <yu-button @click="nextFn" v-if="dialogTittle == this.$t('sysUserManager.xz')" :disabled="isNextFn">{{ $t('tenantManager.xyb') }}</yu-button>
            <yu-button @click="cancelFn">{{ $t('sysUserManager.qx') }}</yu-button>
          </div>
        </yu-tab-pane>
        <yu-tab-pane :label="$t('tenantManager.zhgnsq')" name="auth" :disabled="authDisable">
          <div class="auth-tree-box">
            <div class="auth-tree-header">
              <yu-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">{{ $t('roleDataPowerManager.allcheck') }}</yu-checkbox>
              <div class="handle-btn" style="float: right;" v-if="!isDetail">
                <yu-input
                  v-model="treeSearchVal"
                  :placeholder="$t('roleDataPowerManager.gjz')"
                  suffix-icon="yu-icon-search1"
                  @suffix-click="searchTreeFn"
                  @keyup.enter.native="searchTreeFn"
                  class="search-input"
                  @clear="searchTreeFn"
                  clearable></yu-input>
              </div>
            </div>
            <div class="auth-tree-content">
              <yu-xtree
                :local-data="treeData"
                data-id="authresId"
                node-key="authresId"
                data-label="nodeName"
                data-pid="upTreeId"
                :filter-node-method="filterNodeFn"
                :render-content="renderContent"
                ref="treeRef"
                show-checkbox
                @check-change="checkChange"></yu-xtree>
            </div>
            <div class="foot-btn">
              <yu-button v-if="!isDetail" type="primary" v-norepeat.disabled="saveDisabled" @click="saveFn">{{ $t('roleDataPowerManager.bc') }}</yu-button>
              <yu-button v-else type="primary" @click="handleClickEdit">{{ $t('orgInfoManager.xg') }}</yu-button>
              <yu-button @click="nextDataFn" v-if="dialogTittle == this.$t('tenantManager.xz')" :disabled="isNextDataFn">{{ $t('tenantManager.xyb') }}</yu-button>
              <yu-button @click="cancelFn">{{ $t('sysUserManager.qx') }}</yu-button>
            </div>
          </div>
        </yu-tab-pane>
        <yu-tab-pane :label="$t('tenantManager.zhsjmbgl')" name="templ" :disabled="templDisable">
          <yu-panel :title="$t('resContrManage.kzdgl')" show-search-input :placeholder="$t('resContrManage.gjz')" @search="contrQueryFn" ref="contrPanel" panel-type="simple" search-key="searchKey">
            <template slot="search">
              <yu-combo-tree
                v-model="nodeName"
                :placeholder="$t('resContrManage.cd')"
                :local-data="authedTreeData"
                data-id="authresId"
                data-label="nodeName"
                data-child="children"
                data-pid="upTreeId"
                ref="menuTree"
                :max-height="400"
                @node-click="menuNodeClickFn($event, true)"
                @clear="clearMenuIdFn(true)"
                clearable>
              </yu-combo-tree>
            </template>
            <yu-xtable request-type="POST" :data-url="contrDataUrl" row-number ref="pointTable" :base-params="contrParams" :default-load="false">
              <yu-xtable-column prop="contrName" :label="$t('resContrManage.kzdmc')" sortable></yu-xtable-column>
              <yu-xtable-column prop="menuName" :label="$t('resContrManage.cdmc')"></yu-xtable-column>
              <yu-xtable-column prop="contrCode" :label="$t('resContrManage.czdm')"></yu-xtable-column>
              <yu-xtable-column prop="contrUrl" :label="$t('resContrManage.czUrl')" width="280px"></yu-xtable-column>
              <yu-xtable-column prop="methodType" :label="$t('resContrManage.HTTPff')"></yu-xtable-column>
              <yu-xtable-column :label="$t('resContrManage.cz')" fixed="right" width="150px" v-if="!isDetail">
                <template slot-scope="scope">
                  <yu-button-drop :show-length="2" type="text">
                    <yu-button @click="openDataTemplFn(scope.row)" type="text" v-norepeat.disabled>{{ $t('resContrManage.glsjmb') }}</yu-button>
                  </yu-button-drop>
                </template>
              </yu-xtable-column>
            </yu-xtable>
          </yu-panel>
          <div v-if="isDetail" class="foot-btn">
            <yu-button type="primary" @click="handleClickEdit">{{ $t('orgInfoManager.xg') }}</yu-button>
          </div>
        </yu-tab-pane>
        <yu-tab-pane :label="$t('tenantManager.zhsjsq')" name="power" :disabled="dataDisable">
          <yu-panel ref="simplePanel" :title="$t('authDataPowerManager.sjsqlb')" panel-type="simple" show-search-input :placeholder="$t('authDataPowerManager.kzdmc')" @search="fuzzyQueryFn">
            <yu-xtable request-type="POST" row-number ref="refTable" :data-url="authDataUrl" :base-params="tableParams" :default-load="false">
              <yu-xtable-column prop="menuPath" :label="$t('authDataPowerManager.sjmkcd')"></yu-xtable-column>
              <yu-xtable-column prop="cornName" :label="$t('authDataPowerManager.kzdmc')"></yu-xtable-column>
              <yu-xtable-column prop="authTmplName" :label="$t('authDataPowerManager.ysqdsjqxmb')" :formatter="formatter"></yu-xtable-column>
              <yu-xtable-column :label="$t('authDataPowerManager.cz')" width="120" v-if="!isDetail">
                <template slot-scope="scope">
                  <!-- <yu-button size="mini" @click="handleEdit(scope.row)" type="text">{{ $t("authDataPowerManager.sjsq") }}</yu-button> -->
                  <yu-button-drop :show-length="2" type="text">
                    <yu-button @click="handleEdit(scope.row)" type="text" v-norepeat.disabled>{{ $t("authDataPowerManager.sjsq") }}</yu-button>
                  </yu-button-drop>
                </template>
              </yu-xtable-column>
            </yu-xtable>
          </yu-panel>
          <div class="foot-btn">
            <yu-button v-if="isDetail" type="primary" @click="handleClickEdit">{{ $t('orgInfoManager.xg') }}</yu-button>
            <yu-button @click="cancelFn">{{ $t('sysUserManager.qx') }}</yu-button>
          </div>
        </yu-tab-pane>
      </yu-tabs>
    </yu-xdialog>
    <yu-xdialog :title="$t('authDataPowerManager.xzsjmbsq')" :visible.sync="authDialogVisible" class="control-dialog">
      <yu-xtable request-type="POST" :data-url="authTmplUrl" selection-type="checkbox" ref="dataAuthTable" @loaded="authLoadFn" @selection-change="authChangeFn">
        <yu-xtable-column prop="authTmplName" :label="$t('authDataPowerManager.mbmc')"></yu-xtable-column>
        <yu-xtable-column prop="sqlName" :label="$t('authDataPowerManager.zwfmc')"></yu-xtable-column>
        <yu-xtable-column prop="sqlString" :label="$t('authDataPowerManager.sjqxtj')"></yu-xtable-column>
      </yu-xtable>
      <div slot="footer" class="dialog-footer">
        <yu-button type="primary" @click="saveDataFn" v-norepeat.disabled>{{ $t('authDataPowerManager.qd') }}</yu-button>
        <yu-button @click="authDialogVisible = false">{{ $t('authDataPowerManager.qx') }}</yu-button>
      </div>
    </yu-xdialog>

    <yu-xdialog :title="$t('resContrManage.xzsjmb')" :visible.sync="dataTemplVisible" height="540px" class="control-dialog">
      <div class="dialog-search">
        <yu-checkbox v-model="checkDataTmpl" @change="checkAuthChangeFn">{{ $t('resContrManage.jkygl') }}</yu-checkbox>
        <yu-input
          v-model="dataTemplVal"
          :placeholder="$t('resContrManage.gjz')"
          suffix-icon="yu-icon-search1"
          @suffix-click="dataTemplSearchFn"
          @keyup.enter.native="dataTemplSearchFn"
          maxlength="32"
          class="form-item"
          clearable></yu-input>
      </div>
      <yu-xtable
        :data-url="dataTemplUrl"
        request-type="POST"
        :base-params="dataTemplParams"
        row-number
        selection-type="checkbox"
        ref="dataTemplTable"
        @loaded="dataTemplLoadFn"
        :default-load="false">
        <yu-xtable-column prop="authTmplName" :label="$t('resContrManage.mbmc')"></yu-xtable-column>
        <yu-xtable-column prop="sqlName" :label="$t('resContrManage.zwfmc')"></yu-xtable-column>
        <yu-xtable-column prop="sqlString" :label="$t('resContrManage.sjqxtj')"></yu-xtable-column>
        <yu-xtable-column :label="$t('resContrManage.sfysq')">
          <template slot-scope="scope">
            <span>{{ scope.row.authRecoId ? '是': '否' }}</span>
          </template>
        </yu-xtable-column>
      </yu-xtable>
      <div slot="footer" class="dialog-footer">
        <yu-button type="primary" @click="saveRelateFn" v-norepeat.disabled>{{ $t('resContrManage.qd') }}</yu-button>
        <yu-button @click="dataTemplVisible = false">{{ $t('resContrManage.qx') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import { clone, extend, lookup } from '@/utils'
import { mapGetters } from "vuex"
import { checkBelongToChooseNode } from '@/utils/util'
// lookup.reg('TENANT_STS');

export default {
  data () {
    return {
      dialogTittle: this.$t('tenantManager.xz'),
      formType: 'edit',
      formRules: {
        tenantName: [
          { required: true, message: this.$t('sysUserManager.btx')},
          { max: 33, message: this.$t('sysUserManager.srzgc'), trigger: 'blur'}], // 租户名称校验
        companyName: [
          {required: true, message: this.$t('sysUserManager.btx')},
          { max: 33, message: this.$t('sysUserManager.srzgc'), trigger: 'blur'}], // 租户单位名称校验
        tenantSts: {required: true, message: this.$t('sysUserManager.btx')}, // 租户状态校验
        orgCode: [
          {required: true, message: this.$t('sysUserManager.btx')},
          { max: 33, message: this.$t('sysUserManager.srzgc'), trigger: 'blur'}], // 机构名称校验
        orgName: [
          {required: true, message: this.$t('sysUserManager.btx')},
          { max: 33, message: this.$t('sysUserManager.srzgc'), trigger: 'blur'}], // 机构代码校验
        roleName: [
          {required: true, message: this.$t('sysUserManager.btx')},
          { max: 33, message: this.$t('sysUserManager.srzgc'), trigger: 'blur'}], // 角色名称校验
        roleCode: [
          {required: true, message: this.$t('sysUserManager.btx')},
          { max: 33, message: this.$t('sysUserManager.srzgc'), trigger: 'blur'}], // 角色代码校验
        userName: [
          {required: true, message: this.$t('sysUserManager.btx')},
          { max: 33, message: this.$t('sysUserManager.srzgc'), trigger: 'blur'}], // 用户名称校验
        userCode: [
          {required: true, message: this.$t('sysUserManager.btx')},
          { max: 33, message: this.$t('sysUserManager.srzgc'), trigger: 'blur'}], // 工号校验
        loginCode: [
          {required: true, message: this.$t('sysUserManager.btx')},
          { max: 33, message: this.$t('sysUserManager.srzgc'), trigger: 'blur'}], // 账号校验
      },
      tenantForm: {
        userPass: '******'
      },
      tableUrl: backend.appOcaService + '/api/adminsmtenant/page', // 租户列表查询地址
      searchFormdata: {}, // 查询表单数据
      collapseTitle: [this.$t('sysUserManager.zk'), this.$t('sysUserManager.sq')],
      TENANT_STS: [
        {key: '0', value: this.$t('tenantManager.hy')},
        {key: '1', value: this.$t('tenantManager.zx')}
      ],
      tenantTable: [],
      relDialogVisible: false,
      activeName: 'info', // 当前被激活的页签
      authDisable: true,
      dataDisable: true,
      templDisable: true,
      isNextFn:true,
      isNextDataFn:true,
      isIndeterminate: false, // 全选/半选状态
      checkAll: false, // 是否全选
      initCheckedList: [], // 权限初始化选中的数组
      childCheckedList: [], // 权限初始化最后一级选中的数组
      saveDisabled: { show: false }, // 保存按钮防重复提交
      treeSearchVal: '', // 树的搜索值
      authObjId: '', // 授权id
      authTenantId: '', // 授权租户id
      treeData: [],
      isHandleSave: false, // 是否保存过信息
      authDataUrl: backend.appOcaService + "/api/authorization/authList",
      contrDataUrl: backend.appOcaService + '/api/adminsmrescontr/getAuthedContrList', // 控制点表格查询数据url
      authTmplUrl: '',
      tableParams: { // 数据授权列表参数
        userRoleId: '',
        authObjId: '',
        dataTenantId: ''
      },
      contrParams: { // 数据模板关联表参数
        authObjId: '',
        dataTenantId: '',
        menuId: '',
        keyWord: ''
      },
      searchKey:'',
      nodeName: '',
      authDialogVisible: false, // 数据授权弹窗
      chooseAuthId: '', // 已授权的数据模板Id
      chooseContrId: '',
      searchMenuName: '', // 查询表单下拉菜单树查询绑定值
      authedTreeData: [], // 下拉菜单树数据
      checkDataTmpl: '', // 是否仅查看已选择的数据模板选项
      dataTemplVal: '', // 选择数据权限模板
      dataTemplVisible: false, //数据模板选择弹出框是否显示
      isRelateTmpl: false, // 是否是直接打开选择数据权限模板
      currentMenuId: '',
      dataTemplParams:{
        keyWord: '',
        check: this.checkAuthTmpl ? 1 : 0
      },
      dataTemplUrl: backend.appOcaService + '/api/adminsmrescontr/datatmpllist', // 数据模板表单查询URL
      dataTmplId: '', // 选择后的模板数版id
      isSave: false, //置灰保存信息
      isDetail: false // 是否详情
    };
  },
  computed: {
    ...mapGetters(["roles"]),
  },
  watch: {
    isDetail(val) {
      this.formType = val ? 'details' : 'edit'
      this.dialogTittle = val ? '详情' : '修改'
    }
  },
  created() {
    this.tableParams.userRoleId = this.roles.length && this.roles[0].id;
  },
  methods: {
    // 打开新增页面
    addTenantFn (tab, event) {
      this.relDialogVisible = true;
      this.authDisable = true;
      this.dataDisable = true;
      this.templDisable = true;
      this.activeName = 'info';
      this.dialogTittle = this.$t('tenantManager.xz');
      this.isNextFn = true;
      this.isSave = false;
      this.isNextDataFn = true;
      this.$nextTick(function () {
        this.$refs.tenantForm.resetFields();
      });
    },
    // 点击详情界面的修改
    handleClickEdit() {
      if(this.tenantForm.tenantSts === '0') {
        this.$message({
          message: this.$t('tenantManager.zzxgzxzzsj'),
          type: 'warning'
        });
        return
      }
      this.isDetail = false
    },
    // 打开修改页面  type存在时为详情
    editFn (row, type) {
      console.log(this.activeName)
      var _this = this;
      this.formType = type ? 'details' : 'edit';
      this.isDetail = !!type;
      if(row.tenantSts === '0' && !type) {
        _this.$message({
          message: this.$t('tenantManager.zzxgzxzzsj'),
          type: 'warning'
        });
        return
      }
      _this.authDisable = false;
      _this.dataDisable = false;
      _this.templDisable = false;
      _this.activeName = 'info';
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/adminsmtenant/info/' + row.tenantId
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.relDialogVisible = true;
          _this.dialogTittle = this.$t(`${type ? 'orgInfoManager.xq' : 'tenantManager.xg'}`);
          // _this.dialogTittle = this.$t('');
          _this.isSave = true;
          _this.$nextTick(function () {
            _this.$refs.tenantForm.resetFields();
            clone(data, _this.tenantForm);
            _this.authObjId = data.userId;
            _this.authTenantId = data.tenantId;
            _this.treeSearchVal = '';
            _this.getTreeData();
            _this.queryAuthedMenuTree();
            _this.contrParams.authObjId = data.userId;
            _this.contrParams.dataTenantId = data.tenantId;
            _this.$refs.pointTable.remoteData(_this.contrParams);
            _this.tableParams.authObjId = data.userId;
            _this.tableParams.dataTenantId = data.tenantId;
            _this.$refs.refTable.remoteData(_this.tableParams);
          });
        } else {
          _this.$message({
            message: message
          });
          return {};
        }
      });
    },
    // 关闭弹窗
    cancelFn () {
      this.relDialogVisible = false;
      this.isHandleSave = false;
    },
    // 点击基本信息页面下一步
    nextFn () {
      this.activeName = 'auth';

    },
    // 保存租户信息
    /* saveInfoFn () {
      if (!this.isHandleSave) {
        this.saveInfo();
      }
    }, */
    // 新增、修改保存
    saveInfoFn () {
      var _this = this;
      var url = _this.tenantForm.tenantId ? '/api/adminsmtenant/update' : '/api/adminsmtenant/saveBaseInfo';
      _this.$refs.tenantForm.validate(function (valid) {
        if (valid) {
          _this.$request({
            method: 'POST',
            url: backend.appOcaService + url,
            data: _this.tenantForm
          }).then(({code, message, data}) => {
            if (code === '0000') {
              _this.$message({
                message: _this.$t('sysUserManager.sjbccg'),
                type: 'success'
              });
              _this.$refs.tenantTable.remoteData();
              _this.authObjId = data.userId;
              _this.authTenantId = data.tenantId;
              _this.isHandleSave = true;
              if (_this.dialogTittle == _this.$t('tenantManager.xg')) {
                _this.relDialogVisible = false;
                _this.isHandleSave = false;
              }
              _this.isNextFn = false;
              _this.isSave = true;
            } else {
              _this.$message({
                message: message || _this.$t('sysUserManager.sjczsb'),
                type: 'error'
              });
            }
          }).then(() => {
            _this.treeSearchVal = '';
            _this.getTreeData();
          }).then(() => {
            if (_this.authDisable) {
              _this.authDisable = false;
            }
          });
        }
      });
    },
    // 激活
    useTenantFn (row) {
      var _this = this;
      var selections = row.tenantId ? [row] : _this.$refs.tenantTable.selections;
      if (selections.length < 1) {
        _this.$message({ message: _this.$t('sysUserManager.qxzytsj'), type: 'warning' });
        return;
      }
      var tenantIds = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].tenantSts === '1') {
          // 只能选择注销租户
          tenantIds.push(selections[i].tenantId);
        } else {
          this.$message({
            message: _this.$t('tenantManager.zzxyzxsj'),
            type: 'warning'
          });
          return;
        }
      }
      this.$confirm(this.$t('sysUserManager.cczjqygsjsfjx'), this.$t('sysUserManager.ts'), {
        confirmButtonText: this.$t('sysUserManager.qd'),
        cancelButtonText: this.$t('sysUserManager.qx'),
        type: 'warning',
        callback: function (action) {
          if (action === 'confirm') {
            // 发起激活用租户请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmtenant/enable',
              data: tenantIds,
            }).then(({code, message, data}) => {
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('sysUserManager.sjczcg'),
                  type: 'success'
                });
                _this.$refs.tenantTable.remoteData();
              } else {
                _this.$message({
                  message: message || _this.$t('sysUserManager.sjczsb'),
                  type: 'error'
                });
              }
            });
          }
        }
      });
    },
    // 注销
    stopTenantFn (row) {
      var _this = this;
      var selections = row.tenantId ? [row] : _this.$refs.tenantTable.selections;
      if (selections.length < 1) {
        _this.$message({ message: _this.$t('sysUserManager.qxzytsj'), type: 'warning' });
        return;
      }
      var tenantIds = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].tenantSts === '0') {
          // 只能选择激活租户
          tenantIds.push(selections[i].tenantId);
        } else {
          this.$message({
            message: _this.$t('tenantManager.zzxzjhsj'),
            type: 'warning'
          });
          return;
        }
      }
      this.$confirm(this.$t('sysUserManager.cczjtygsjsfjx'), this.$t('sysUserManager.ts'), {
        confirmButtonText: this.$t('sysUserManager.qd'),
        cancelButtonText: this.$t('sysUserManager.qx'),
        type: 'warning',
        callback: function (action) {
          if (action === 'confirm') {
            // 发起激活租户请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmtenant/disEnable',
              data: tenantIds,
            }).then(({code, message, data}) => {
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('sysUserManager.sjczcg'),
                  type: 'success'
                });
                _this.$refs.tenantTable.remoteData();
              } else {
                _this.$message({
                  message: message || _this.$t('sysUserManager.sjczsb'),
                  type: 'error'
                });
              }
            });
          }
        }
      });
    },
    /**
    * 删除租户
    * @param row 当前行数据
    */
    deleteFn: function (row) {
      var _this = this;
      var selections = row.tenantId ? [row] : this.$refs.tenantTable.selections;
      if (selections.length < 1) {
        this.$message({
          message: this.$t('sysUserManager.qxxzyscdsj'),
          type: 'warning'
        });
        return;
      }
      var tenantIds = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].tenantSts === '1') {
          // 只能删除注销状态的用户
          tenantIds.push(selections[i].tenantId);
        } else {
          this.$message({
            message: '只能选择注销的数据',
            type: 'warning'
          });
          return;
        }
      }
      this.$confirm(this.$t('sysUserManager.cczjyjscgwjsfjx'), this.$t('sysUserManager.ts'), {
        confirmButtonText: this.$t('sysUserManager.qd'),
        cancelButtonText: this.$t('sysUserManager.qx'),
        type: 'warning',
        callback: function (action) {
          if (action === 'confirm') {
            // 发起注销用户请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmtenant/delete',
              data: tenantIds,
            }).then(({code, message, data}) => {
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('sysUserManager.sjsccg'),
                  type: 'success'
                });
                _this.$refs.tenantTable.remoteData();
              } else {
                _this.$message({
                  message: message || _this.$t('sysUserManager.sjczsb'),
                  type: 'error'
                });
              }
            });
          }
        }
      });
    },
    // 获取树的数据
    getTreeData() {
      var _this = this;
      // if (!this.roles.length) {
      //   this.$message({ type: 'warning', message: 'role id is null' });
      // } else {
      _this.$request({
        url: backend.appOcaService + '/api/authorization/treeQuery',
        method: 'post',
        data: {
          authObjId: _this.authObjId,
          userRoleId: this.roles.length && this.roles[0].id,
          dataTenantId: _this.authTenantId
        }
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.initCheckedList = [];
          _this.childCheckedList = [];
          // _this.treeData = extend([], data);
          _this.treeData = _this.filterTreeData(data);
          _this.getChildChecked(data);
          _this.$nextTick(function () {
            _this.$refs.treeRef.setCheckedKeys(_this.childCheckedList);
          });
        } else {
          _this.$message({ type: 'error', message: message });
        }
      });
      // }
    },
    /**
    * 获取没有children的数据权限节点和全部有权限的节点
    * @param 需要遍历的节点
    */
    getChildChecked: function (child) {
      for (var i = 0; i < child.length; i++) {
        if (child[i].children && child[i].children.length) {
          this.getChildChecked(child[i].children);
        } else if (child[i].state === 1) {
          this.childCheckedList.push(child[i].authresId);
        }
        child[i].state === 1 && this.initCheckedList.push(child[i]);
      }
    },
    filterTreeData(arrData) {
      for (var i = arrData.length - 1; i >= 0; i--) {
        if (arrData[i].authresType === "D") {
          arrData.splice(i, 1)
        } else if(arrData[i].children && arrData[i].children.length) {
          this.filterTreeData(arrData[i].children);
        }
      }
      return arrData;
    },
    /**
    * 是否全选绑定值变化时触发的事件
    */
    handleCheckAllChang (event) {
      var nodes = this.$refs.treeRef.data;
      for (var i = 0; i < nodes.length; i++) {
        this.$refs.treeRef.setChecked(nodes[i].authresId, event.target.checked, true);
      }
      this.isIndeterminate = false;
    },

    /**
    * 点击搜索框模糊查询树结构
    */
    searchTreeFn () {
      this.$refs.treeRef.filter(this.treeSearchVal);
    },
    /**
    * 对树节点进行筛选时执行的方法，返回 true 表示这个节点可以显示，返回 false 则表示这个节点会被隐藏
    * @param value 当前输入信息
    * @param nodeData 当前节点属性信息
    * @param node 当前节点信息
    */
    filterNodeFn (value, nodeData, node) {
      if (!value) {
        return true;
      }
      if (nodeData.nodeName.indexOf(value) !== -1) {
        return true;
      }
      return checkBelongToChooseNode(value, node, 'nodeName');
    },
    /**
    * 返回是否包含某个项
    * obj比较的对象，arr被比较的数组
    */
    isInclude (obj, arr) {
      var flag = false;
      for (var i = 0; i < arr.length; i++) {
        if (obj.authresId === arr[i].authresId) {
          flag = true;
        }
      }
      return flag;
    },
    /**
    * 树节点的内容区的渲染 Function
    */
    renderContent (h, obj) {
      var nodeData = obj.data;
      return h('span', {attrs: {class: 'custom-tree-label'}}, [
        h('span', {attrs: {class: 'yu-treeMenuType' + nodeData.authresType}}, nodeData.authresType),
        h('span', {}, [h('span', {}, nodeData.nodeName)])
      ]);
    },

    /**
     * 是否全选绑定值变化时触发的事件
     */
    handleCheckAllChange: function (event) {
      var nodes = this.$refs.treeRef.data;
      for (var i = 0; i < nodes.length; i++) {
        this.$refs.treeRef.setChecked(nodes[i].authresId, event.target.checked, true);
      }
      this.isIndeterminate = false;
    },

    /**
    * 节点选中状态发生变化时的回调
    */
    checkChange () {
      var checkNode = this.$refs.treeRef.getCheckedKeys();
      var nodes = this.$refs.treeRef.data;
      var checkedCount = 0;
      for (var i = 0; i < nodes.length; i++) {
        for (var j = 0; j < checkNode.length; j++) {
          if (nodes[i].authresId == checkNode[j]) {
            checkedCount++;
          }
        }
      }
      this.checkAll = checkedCount === nodes.length;
      this.isIndeterminate = checkNode.length > 0 && checkedCount < nodes.length;
    },
    /**
    * 获取树变化的值
    */
    getChangeNodes () {
      var _this = this;
      var checkNodes = _this.$refs.treeRef.getCheckedNodes();
      var checkHalfNodes = _this.$refs.treeRef.getHalfCheckedNodes();
      var allChecked = extend([], checkNodes.concat(checkHalfNodes));
      var changeNodes = [];
      // 取消的选项
      _this.initCheckedList.forEach(function (item) {
        if (!_this.isInclude(item, allChecked)) {
          changeNodes.push({
            authRecoId: item.authRecoId,
            authresId: item.authresId,
            state: 0,
            upTreeId: item.upTreeId,
            nodeName: item.nodeName,
            authresType: item.authresType
          });
        }
      });
      // 增加的勾选项
      allChecked.forEach(function (item) {
        if (!_this.isInclude(item, _this.initCheckedList)) {
          changeNodes.push({
            authresId: item.authresId,
            authRecoId: item.authRecoId,
            state: 1,
            upTreeId: item.upTreeId,
            nodeName: item.nodeName,
            authresType: item.authresType
          });
        }
      });
      return changeNodes;
    },
    /**
    * 保存功能授权
    */
    saveFn () {
      var _this = this;
      var authFormList = _this.getChangeNodes();
      if (!authFormList.length) {
        _this.$message({
          message: _this.$t('roleDataPowerManager.sjwgg'),
          type: 'warning'
        });
        return;
      };
      _this.saveDisabled.show = true;
      _this.saveDisabled = clone(_this.saveDisabled, {});
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/authorization/saveAuth',
        data: {
          authObjType: 'U',
          authObjId: _this.authObjId,
          authFormList: authFormList,
          dataTenantId: _this.authTenantId
        }
      }).then(({code, message, data}) => {
        _this.saveDisabled.show = false;
        _this.saveDisabled = clone(_this.saveDisabled, {});
        if (code === '0000') {
          // var checkNodes = _this.$refs.treeRef.getCheckedNodes();
          // var checkHalfNodes = _this.$refs.treeRef.getHalfCheckedNodes();
          // _this.initCheckedList = extend([], checkNodes.concat(checkHalfNodes));
          // _this.$message({type: 'success', message: _this.$t('roleDataPowerManager.bccg') });
          // _this.dataDisable = false;
          _this.getTreeData();
          _this.$message({type: 'success', message: _this.$t('roleDataPowerManager.bccg') });
          this.isNextDataFn = false;
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      }).then(() => {
        _this.queryAuthedMenuTree();
        _this.contrParams.authObjId = this.authObjId;
        _this.contrParams.dataTenantId = this.authTenantId;
        _this.$refs.pointTable.remoteData(_this.contrParams);
        _this.tableParams.authObjId = this.authObjId;
        _this.tableParams.dataTenantId = this.authTenantId;
        _this.$refs.refTable.remoteData(this.tableParams);
      }).then(() => {
        _this.templDisable = false;
        _this.dataDisable = false;
      });
    },
    // 新增时查看数据授权标签
    nextDataFn () {
      this.activeName = 'templ';
      // this.tableParams.authObjId = this.authObjId;
      // this.tableParams.authTenantId = this.authTenantId;
      // this.$refs.refTable.remoteData(this.tableParams);
      // this.$nextTick(function () {
      //   this.treeSearchVal = '';
      //   this.getTreeData();
      // });
    },
    /**
    * 点击数据授权搜索框模糊查
    * @param e.value 搜索框的值
    */
    fuzzyQueryFn: function (e) {
      this.tableParams.keyWord = e.value;
      this.$refs.refTable.remoteData(this.tableParams);
    },
    // 格式化已授权模板名称
    formatter(row, column) {
      if(!row.tmplAndRecoVo) {
        return this.$t('authDataPowerManager.zwglmb');
      }
      return row.tmplAndRecoVo.authTmplName;
    },
    //编辑列
    handleEdit(row) {
      const isChange = this.chooseContrId === row.resContrId;
      this.authDialogVisible = true;
      this.chooseContrId = row.resContrId;
      this.chooseAuthId = (row.tmplAndRecoVo && row.tmplAndRecoVo.authTmplId) || '';
      this.authTmplUrl = backend.appOcaService + `/api/adminsmdataauthtmpl/associated/${row.resContrId}?dataTenantId=${this.authTenantId}`;
      this.$nextTick(function () {
        isChange && this.$refs.dataAuthTable.remoteData();
      });
    },
    // 选择菜单表格数据加载完, (data, total, response),data为表格加载完成后表格数据，total为表格数据总数,response 为整个返回报文
    authLoadFn(d) {
      var _this = this;
      console.log(11);
      d.forEach(function (item) {
        item.authTmplId === _this.chooseAuthId && _this.$refs.dataAuthTable.toggleRowSelection(item, true);
      });
    },
    /**
    * 选择数据权限模板当选择项发生变化时会触发的事件
    * @param param selection
    */
    authChangeFn(selection) {
      if (selection.length > 1) {
        this.$refs.dataAuthTable.clearSelection();
        this.$refs.dataAuthTable.toggleRowSelection(selection.pop());
      }
    },
    // 保存数据授权
    saveDataFn() {
      const _this = this;
      const selections = this.$refs.dataAuthTable.selections;
      _this.$request({
        url: backend.appOcaService + '/api/authorization/saveTmplAuth',
        method: 'post',
        data: {
          lastAuthresId: _this.chooseAuthId, // 之前的数据模板授权的数据
          authresId: (selections.length && selections[0].authTmplId) || '', //新授权的数据模板 id
          contrId: _this.chooseContrId, // 相关联的控制点 id
          authobjType: 'U', // 授权对象类
          authobjId: _this.authObjId, // 授权对象 id
          dataTenantId: _this.authTenantId
        }
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$message({type: 'success', message: _this.$t('authDataPowerManager.bcsqcg') });
          _this.authDialogVisible = false;
          _this.$refs.refTable.remoteData();
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      });
    },
    cancelRelFn: function () {
      this.relDialogVisible = false;
      this.authDisable = true;
      this.dataDisable = true;
    },
    /**
     * 查询已经授权的菜单
     */
    queryAuthedMenuTree () {
      const _this = this;
      _this.$request({
        url: backend.appOcaService + '/api/authorization/getAuthedMenuTree',
        method: 'post',
        data: {
          authObjId: _this.authObjId,
          userRoleId: this.roles.length && this.roles[0].id,
          dataTenantId: _this.authTenantId
        }
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.authedTreeData = data;
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      })
    },
    /**
    * 选择菜单树查询
    * @param node 点击的节点信息
    * @param immidately 是否立即执行查询，区分快速查询和精确查询
    */
    menuNodeClickFn(node, immidately) {
      if (!node.children || node.children.length === 0) {
        this.contrParams.menuId = node.authresId || '';
        this.contrParams.keyWord = this.$refs.contrPanel.inputVal;
        this.$refs.pointTable.remoteData(this.contrParams);
      }

      // this.contrParams.keyWord = this.$refs.contrPanel.inputVal || '';

      // return;
    },
    contrQueryFn(e) {
      this.contrParams.keyWord = e.value;
      this.$refs.pointTable.remoteData(this.contrParams);
    },
    clearMenuIdFn(immidately) {
      this.contrParams.menuId = '';
      this.$refs.pointTable.remoteData(this.contrParams);
    },
    openDataTemplFn(row) {
      var _this = this;
      _this.dataTemplVisible = true;
      _this.isRelateTmpl = true;
      _this.dataTemplVal = '';
      _this.checkDataTmpl = false;
      _this.currentMenuId = row.menuId;
      _this.dataTemplParams.contrId = row.contrId;
      _this.dataTemplParams.lastTmplIds = [];
      this.dataTemplParams.dataTenantId = _this.authTenantId;
      _this.$nextTick(function () {
        _this.$refs.dataTemplTable.remoteData(_this.dataTemplParams);
      });
    },
    // 数据模板仅看已选择事件 1：复选框选择 0：复选框未选
    checkAuthChangeFn(e) {
      this.$refs.dataTemplTable.remoteData({keyWord: this.dataTemplVal, check: e.target.checked ? 1 : 0});
    },
    // 选择数据模板关键字搜索
    dataTemplSearchFn() {
      this.$refs.dataTemplTable.remoteData({keyWord: this.dataTemplVal, check: this.checkAuthTmpl ? 1 : 0});
    },
    // 选择菜单表格数据加载完, (data, total, response),data为表格加载完成后表格数据，total为表格数据总数,response 为整个返回报文
    dataTemplLoadFn(d) {
      var _this = this;
      d.forEach(function (item) {
        if (_this.isRelateTmpl) {
          item.mark === 1 && _this.$refs.dataTemplTable.toggleRowSelection(item, true);
        } else {
          _this.dateTmplId.indexOf(item.dateTmplId) > -1 && _this.$refs.dataTemplTable.toggleRowSelection(item, true);
        }
      });
    },
    /**
    * 保存关联数据模板
    */
    saveRelateFn() {
      const _this = this;
      const selections = this.$refs.dataTemplTable.selections;
      const authTmplId = [];
      if(selections.length) {
        selections.forEach(item => {
          authTmplId.push(item.authTmplId);
        });
      }
      _this.relateAuthTmplFn({
        contrId: _this.dataTemplParams.contrId,
        authDataTmplIdList: authTmplId,
        dataTenantId: _this.authTenantId
      });
      _this.dataTemplVisible = false;
      _this.isRelateTmpl = false;
    },
    /**
    * 关联数据权限模板
    * @param param 关联的信息
    */
    relateAuthTmplFn(param) {
      var _this = this;
      _this.$request({
        url: backend.appOcaService + '/api/adminsmrescontr/relationtmpl',
        method: 'post',
        data: param
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$message({type: 'success', message: _this.$t('resContrManage.glsjmbcg') });
          _this.$refs.pointTable.remoteData();
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      });
    },
    codeChange() {
      this.tenantForm.userCode = this.tenantForm.loginCode;
    }
  }
};
</script>
<style>
.foot-btn {
  border-top: 1px #EDEDED solid;
  text-align: center;
  padding-top: 24px;
}
.auth-tree-box .auth-tree-header {
  height: 56px;
  border-bottom: 1px solid #EDEDED;
  line-height: 56px;
  padding: 0 24px 0 60px;
}
.auth-tree-content {
  min-height: 250px;
}
.auth-tree-header .el-checkbox__label {
  padding-left: 8px;
  vertical-align: middle;
}
.el-dialog-x__wrapper.no-body-padding .el-tabs__content{
  padding: 0!important;
}
.no-body-padding .el-dialog-x__body{
  overflow: hidden;
}
.no-body-padding .el-dialog-x__body .el-tabs__content {
  height: 500px;
  overflow: auto;
}
</style>
