<!--
  @Created by zhuly8@yusys.com.cn 2021-01-11
  @updated by
  @description 用户管理
-->
<template>
  <div id="sysUserManager">
    <yu-panel ref="panel" :title="$t('sysUserManager.yhgl')" class="adjust-height" show-search-input :placeholder="$t('sysUserManager.gjz')" @search="fuzzyQuery">
      <!--用户列表操作按钮-->
      <template slot="right">
        <yu-toolBar>
          <yu-button v-if="checkCtrl('add')" @click="addUserFn">{{ $t('sysUserManager.xz') }}</yu-button>
          <yu-button v-if="checkCtrl('batchenable')" v-norepeat.disabled @click="useUserFn">{{ $t('sysUserManager.qy') }}</yu-button>
          <yu-button v-if="checkCtrl('batchdisable')" v-norepeat.disabled @click="stopUserFn">{{ $t('sysUserManager.ty') }}</yu-button>
          <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled @click="deleteFn">{{ $t('sysUserManager.sc') }}</yu-button>
        </yu-toolBar>
      </template>
      <!-- 快速查询-机构查询 -->
      <template slot="search">
        <yu-combo-tree
          ref="tree"
          v-model="orgName"
          :placeholder="$t('sysUserManager.qbjg')"
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
      <!--用户列表查询条件-->
      <template slot="filter">
        <yu-xform ref="searchForm" related-table-name="usersTable" v-model="searchFormdata" form-type="search" @form-reset="formResetFn">
          <yu-xform-group :column="4">
            <yu-xform-item :label="$t('sysUserManager.yhmc')" name="userName" :placeholder="$t('sysUserManager.qsr')"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.zh')" name="loginCode" :placeholder="$t('sysUserManager.qsr')"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.gh')" name="userCode" :placeholder="$t('sysUserManager.qsr')"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.zt')" name="userSts" :placeholder="$t('sysUserManager.qxz')" ctype="select" data-code="DATA_STS"></yu-xform-item>
            <yu-xform-item name="orgId" :label="$t('sysUserManager.ssjg')" ctype="custom">
              <yu-combo-tree
                ref="searchTree"
                v-model="searchOrgName"
                :placeholder="$t('sysUserManager.qxz')"
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
      <!--用户列表-->
      <yu-xtable request-type="POST" ref="usersTable" row-number :data-url="tableUrl" selection-type="checkbox">
        <yu-xtable-column :label="$t('sysUserManager.yhmc')" :width="$store.getters.language==='en'?'110px':'80px'"
        >
          <template slot-scope="scope">
            <a class="underline" @click="userDetailFn(scope.row)">{{ scope.row.userName }}</a>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :width="$store.getters.language==='en'?'110px':'80px'" :label="$t('sysUserManager.zh')" prop="loginCode"></yu-xtable-column>
        <yu-xtable-column :width="$store.getters.language==='en'?'110px':'80px'" :label="$t('sysUserManager.gh')" prop="userCode"></yu-xtable-column>
        <yu-xtable-column :label="$t('sysUserManager.jgbm')" width="250">
          <template slot-scope="scope">
            <span>{{ scope.row.orgName }}</span>
            <span v-if="scope.row.dptName">{{ '（' + scope.row.dptName + '）' }}</span>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('sysUserManager.zt')">
          <template slot-scope="scope">
            <yu-tag v-if="scope.row.userSts=== 'A'" type="success">{{ userSts[scope.row.userSts] }}</yu-tag>
            <yu-tag v-if="scope.row.userSts=== 'I'" type="danger">{{ userSts[scope.row.userSts] }}</yu-tag>
            <yu-tag v-if="scope.row.userSts=== 'W'" type="warning">{{ userSts[scope.row.userSts] }}</yu-tag>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('sysUserManager.xb')" prop="userSex" data-code="SEX_TYPE"></yu-xtable-column>
        <yu-xtable-column :label="$t('sysUserManager.zxbg')" width="260">
          <template slot-scope="scope" v-if="scope.row.lastChgName">
            <span>{{ scope.row.lastChgName + '（' + scope.row.lastChgDt + '）' }}</span>
          </template>
        </yu-xtable-column>
        <yu-xtable-column fixed="right" :label="$t('component.operate')" width="160">
          <template slot-scope="scope">
            <yu-button-drop set-index="0" :show-length="2" type="text">
              <yu-button v-if="checkCtrl('userRel')" @click="userRelFn(scope.row)" type="text">{{ $t('sysUserManager.yhglxx') }}</yu-button>
              <yu-button v-if="checkCtrl('edit')" @click="editFn(scope.row)" type="text">{{ $t('sysUserManager.xg') }}</yu-button>
              <yu-button v-if="checkCtrl('batchenable') && scope.row.userSts != 'A'" v-norepeat.disabled @click="useUserFn(scope.row)" type="text">{{ $t('sysUserManager.qy') }}</yu-button>
              <yu-button v-if="checkCtrl('batchdisable') && scope.row.userSts != 'I'" v-norepeat.disabled @click="stopUserFn(scope.row)" type="text">{{ $t('sysUserManager.ty') }}</yu-button>
              <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled @click="deleteFn(scope.row)" type="text">{{ $t('sysUserManager.sc') }}</yu-button>
              <yu-button v-if="checkCtrl('resetpassword')" @click="resetPassword(scope.row)" type="text">{{ $t('sysUserManager.zzmm') }}</yu-button>
            </yu-button-drop>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
    <!--用户 新增 修改 详情-->
    <yu-xdialog :title="viewTitle[viewType]" :visible.sync="dialogVisible" class="no-body-padding" :width="$store.getters.language==='en'?'1000px':'800px'">
      <yu-xform ref="userForm" v-model="formdata" :form-type="formType" :label-width="$store.getters.language==='en'?'230px':'120px'" :rules="formRules">
        <yu-panel :title="$t('sysUserManager.gjxx')" panel-type="simple" is-collapse :collapse-title="collapseTitle">
          <yu-row>
            <yu-col :span="12">
              <yu-xform-item :label="$t('sysUserManager.yhmc')" name="userName" :placeholder="$t('sysUserManager.qsr')"></yu-xform-item>
              <yu-xform-item :label="$t('sysUserManager.zh')" name="loginCode" :placeholder="$t('sysUserManager.qsr')" :disabled="viewType != 'ADD'"></yu-xform-item>
              <yu-xform-item :label="$t('sysUserManager.gh')" name="userCode" :placeholder="$t('sysUserManager.qsr')" :disabled="viewType != 'ADD'"></yu-xform-item>
              <yu-xform-item :label="$t('sysUserManager.ssjg')" name="orgId" @clear="orgClear" ctype="yufp-search-tree" :params="treeParams" :select-value="selectValue" :details="viewType==='DETAIL'" :details-value="detailsValue" @node-click="changeOrgFn"></yu-xform-item>
              <yu-xform-item :label="$t('sysUserManager.ssbm')" name="dptId" @clear="dptClear" :placeholder="$t('sysUserManager.ssbm')" ctype="yufp-search-tree" :params="dptTreeParams" :select-value="selectedDptId" @node-click="changeDptFn" :details="viewType==='DETAIL'" :details-value="detailsDptValue"></yu-xform-item>
              <yu-xform-item :label="$t('sysUserManager.zt')" name="userSts" ctype="select" :placeholder="$t('sysUserManager.qsr')" data-code="DATA_STS"></yu-xform-item>
            </yu-col>
            <yu-col :span="12">
              <div class="yu-user-pic search-form yu-user-pic-cust">
                <div class="yu-user-pic-box">
                  <img v-if="userAvatar" :src="userAvatar" />
                  <template v-else>
                    <div class="yu-icon-user"></div>
                    <label>头像照片</label>
                  </template>
                </div>
                <!--<yu-upload v-if="viewType != 'DETAIL'" :action="uploadAction" :headers="uploadHeader" :show-file-list="false" :data="uploadParams" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">-->
                <yu-upload v-if="viewType != 'DETAIL'" :action="uploadAction" :headers="uploadHeader" :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                  <yu-button type="text">{{ $t('sysUserManager.qxz') }}</yu-button>
                  <div class="el-upload__tip" slot="tip">{{ $t('sysUserManager.txscts') }}</div>
                </yu-upload>
              </div>
              <yu-xform-item :label="$t('sysUserManager.mm')" name="passwordShow" v-if="viewType == 'ADD'" disabled :notice="$t('sysUserManager.mmmrwxtcsmm')"></yu-xform-item>
              <yu-xform-item :label="$t('sysUserManager.xb')" name="userSex" ctype="radio" data-code="SEX_TYPE"></yu-xform-item>
            </yu-col>
          </yu-row>
        </yu-panel>
        <yu-panel :title="$t('sysUserManager.kzxx')" panel-type="simple" is-collapse :collapse-title="collapseTitle">
          <yu-xform-group :column="2">
            <yu-xform-item :label="$t('sysUserManager.yxq')" name="deadline" ctype="datepicker" :placeholder="$t('sysUserManager.qxz')" value-format="yyyy-MM-dd HH:mm:ss" :picker-options="pickerOptions"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.zjlx')" name="certType" ctype="select" :placeholder="$t('sysUserManager.qxz')" data-code="IDENT_TYPE" @change="checkCertNoFn"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.zjhm')" name="certNo" :placeholder="$t('sysUserManager.qsr')" @blur="checkCertNoFn"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.yddh')" name="userMobilephone" :placeholder="$t('sysUserManager.qsr')"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.zgxl')" name="userEducation" ctype="select" data-code="HIGHEST_EDU"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.yx')" name="userEmail" :placeholder="$t('sysUserManager.qsr')"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.zgzs')" name="userCertificate" :placeholder="$t('sysUserManager.qsr')"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.bgdh')" name="userOfficetel" :placeholder="$t('sysUserManager.qsr')"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.cysj')" name="financialJobTime" ctype="datepicker" :placeholder="$t('sysUserManager.qxz')" value-format="yyyy-MM-dd HH:mm:ss"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.zj')" name="positionDegree" ctype="select" :placeholder="$t('sysUserManager.qsr')" data-code="RANK_LEVEL"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.rzsj')" name="entrantsDate" ctype="datepicker" :placeholder="$t('sysUserManager.qxz')" value-format="yyyy-MM-dd HH:mm:ss"></yu-xform-item>
            <yu-xform-item :label="$t('sysUserManager.renzsj')" name="positionTime" ctype="datepicker" :placeholder="$t('sysUserManager.qxz')" value-format="yyyy-MM-dd HH:mm:ss"></yu-xform-item>
          </yu-xform-group>
        </yu-panel>
      </yu-xform>
      <div slot="footer" class="yu-grpButton">
        <yu-button type="primary" v-if="formType==='edit'" key="edit" v-norepeat.disabled.noStopPropagation @click="saveUser">{{ $t('sysUserManager.bc') }}</yu-button>
        <yu-button type="primary" v-if="viewType === 'DETAIL'" @click="switchStatus('EDIT', true)">{{ $t('sysUserManager.xg') }}</yu-button>
        <yu-button v-if="formType === 'details'" @click="cancelFn">{{ $t('sysUserManager.fh') }}</yu-button>
        <yu-button v-else @click="cancelFn">{{ $t('sysUserManager.qx') }}</yu-button>
      </div>
    </yu-xdialog>
    <!-- 用户的角色 岗位 机构 关联信息 -->
    <yu-xdialog :title="$t('sysUserManager.yhglxx')" :visible.sync="relDialogVisible" width="480px" height="490px">
      <yu-tabs v-model="activeName">
        <yu-tab-pane :label="$t('sysUserManager.sqjgxx')" name="org">
          <yu-input v-model="orgKeyWord" icon="search" @click="searchRelOrgFn" @keyup.enter.native="searchRelOrgFn" :placeholder="$t('sysUserManager.qsr')" style="margin-bottom: 10px;"></yu-input>
          <yu-xtree
            ref="orgTree"
            v-model="orgName"
            :placeholder="$t('sysUserManager.ssjg')"
            request-type="POST"
            :data-url="orgTreeUrl"
            data-id="orgId"
            data-label="orgName"
            data-pid="upOrgId"
            :default-expand-all="true"
            :show-checkbox="true"
            :data-params="orgTreeParams"
            node-key="orgId"
            :default-checked-keys="checkedOrg"
            @load-all-data="getSelectOrgFn"
            @check-change="relSelectFn('orgSelect')"
            :filter-node-method="filterOrgSearch"
            :key="treeKey"></yu-xtree>
        </yu-tab-pane>
        <yu-tab-pane :label="$t('sysUserManager.yhjsxx')" name="role">
          <div class="dialog-search">
            <yu-input v-model="roleKeyWord" icon="search" @click="searchRelFn('rolesTable', 'roleKeyWord')" @keyup.enter.native="searchRelFn('rolesTable', 'roleKeyWord')" :placeholder="$t('sysUserManager.qsr')"></yu-input>
          </div>
          <yu-xtable
            request-type="POST" ref="rolesTable"
            :data-url="rolesTableUrl" selection-type="checkbox"
            :pageable="false" :default-load="false" @loaded="getTableSelectedFn($event, 'rolesTable')"
            @select="relSelectFn('roleSelect')" @select-all="relSelectFn('roleSelect')"
          >
            <yu-xtable-column :label="$t('sysUserManager.jsdm')" prop="roleCode"></yu-xtable-column>
            <yu-xtable-column :label="$t('sysUserManager.jsmc')" prop="roleName"></yu-xtable-column>
          </yu-xtable>
        </yu-tab-pane>
        <yu-tab-pane :label="$t('sysUserManager.yhgwxx')" name="duty">
          <div class="dialog-search">
            <yu-input v-model="dutyKeyWord" icon="search" @click="searchRelFn('dutysTable', 'dutyKeyWord')" @keyup.enter.native="searchRelFn('dutysTable', 'dutyKeyWord')" :placeholder="$t('sysUserManager.qsr')"></yu-input>
          </div>
          <yu-xtable
            request-type="POST" ref="dutysTable"
            :data-url="dutysTableUrl" selection-type="checkbox" :pageable="false"
            :default-load="false" @loaded="getTableSelectedFn($event, 'dutysTable')"
            @select="relSelectFn('dutySelect')" @select-all="relSelectFn('dutySelect')"
          >
            <yu-xtable-column :label="$t('sysUserManager.gwdm')" prop="dutyCode"></yu-xtable-column>
            <yu-xtable-column :label="$t('sysUserManager.gwmc')" prop="dutyName"></yu-xtable-column>
          </yu-xtable>
        </yu-tab-pane>
      </yu-tabs>
      <div slot="footer" class="yu-grpButton">
        <yu-button v-norepeat.disabled type="primary" @click="saveUserRelFn">{{ $t('sysUserManager.bc') }}</yu-button>
        <yu-button @click="cancelRelFn">{{ $t('sysUserManager.qx') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import {clone, extend, lookup} from '@/utils';
import YufpSearchTree from '@/components/widgets/YufpSearchTree';
import {getAccessToken} from '@/utils/oauth';
import {validator} from '@/utils/validate';
import {checkBelongToChooseNode} from '@/utils/util';
import {mapGetters} from 'vuex';

lookup.reg('DATA_STS,SEX_TYPE,IDENT_TYPE,HIGHEST_EDU');
export default {
  components: {YufpSearchTree},
  data: function () {
    return {
      formdata: {
        avator: '1',
        passwordShow: '******'
      }, // 表单数据
      tableUrl: backend.appOcaService + '/api/adminsmuser/page', // 用户列表查询地址
      dialogVisible: false, // 弹出框是否展示
      viewType: 'ADD', // 弹出框默认新增
      viewTitle: lookup.find('CRUD_TYPE', false), // 弹出框类型
      formDisabled: false, // 表单是否禁用
      treeUrl: backend.appOcaService + '/api/adminsmorg/treequeryauth', // 用户树请求 url
      props: {label: 'orgName', children: 'children'},
      formType: 'details',
      orgName: '', // 机构树搜索关键字
      formRules: {
        userName: [
          { required: true, message: this.$t('sysUserManager.btx')},
          { max: 33, message: this.$t('sysUserManager.srzgc')}
        ], // 用户名称校验
        loginCode: [
          {required: true, message: this.$t('sysUserManager.btx')},
          { max: 33, message: this.$t('sysUserManager.srzgc'), trigger: 'blur'}
        ], // 用户代码校验
        userCode: [
          {required: true, message: this.$t('sysUserManager.btx')},
          { max: 33, message: this.$t('sysUserManager.srzgc'), trigger: 'blur'}
        ], // 用户代码校验
        orgId: {required: true, message: this.$t('sysUserManager.btx'), trigger: 'blur'},
        userSex: {required: true, message: this.$t('sysUserManager.btx')},
        orgName: {required: true, message: this.$t('sysUserManager.btx')}, // 必填项校验
        userSts: {required: true, message: this.$t('sysUserManager.btx')}, // 必填项校验
        userMobilephone: {validator: validator.mobile},
        // userOfficetel: {validator: validator.telephone},
        userEmail: {validator: validator.email}
      }, // 表单验证规则
      userDialogVisible: false, // 用户用户弹框是否显示
      userFormdata: {}, // 用户用户查询表单数据
      userTableParams: {userId: ''}, // 用户用户表查询参数
      treeParams: {
        dataId: 'orgId',
        dataLabel: 'orgName',
        dataPid: 'updptId',
        dataUrl: backend.appOcaService + '/api/adminsmorg/treequeryauth',
        placeholder: this.$t('sysUserManager.qxz'),
        searchKey: 'orgName', // 树过滤关键字
        dataParams: {
          orgSts: 'A'
        } // 额外请求参数 默认查询生效的机构
      }, // 机构树选择
      selectValue: '', // 弹出框上级机构显示值
      detailsValue: '', // 搜索树详情字段
      searchOrgName: '', // 查询机构名称
      searchFormdata: {}, // 查询表单数据
      userSts: {}, // 用户状态
      dptTreeParams: {
        dataId: 'dptId',
        dataLabel: 'dptName',
        dataPid: 'upDptId',
        // localData: [],
        dataUrl: backend.appOcaService + '/api/adminsmdpt/tree',
        dataParams: {},
        placeholder: this.$t('sysUserManager.qxz'),
        searchKey: 'dptName' // 树过滤关键字
      },
      selectedDptId: '', // 选中的部门ID
      detailsDptValue: '', // 部门详情字段
      relDialogVisible: false, // 用户关联信息弹出框
      treeKey: '',
      activeName: 'org', // 关联信息默认展示角色
      rolesTableUrl: backend.appOcaService + '/api/adminsmuser/queryuserrole', // 角色关联查询地址
      roleKeyWord: '', // 角色查询关键字
      currentUser: {}, // 查询关联信息的当前用户
      dutysTableUrl: backend.appOcaService + '/api/adminsmuser/queryuserduty', // 岗位关联查询地址
      dutyKeyWord: '', // 岗位查询关键字
      orgKeyWord: '', // 关联机构查询关键字
      orgTreeUrl: backend.appOcaService + '/api/adminsmuser/queryuserorg', // 管理机构查询
      orgTreeParams: {userId: ''},
      checkedOrg: [], // 已关联的机构数据
      dutySelect: false, // 岗位关联表是否勾选过
      roleSelect: false, // 角色关联表是否勾选过
      orgSelect: false, // 关联机构树是否勾选过
      dutysTable: [], // 岗位关联表默认勾选数据
      rolesTable: [], // 角色关联表默认勾选数据
      userAvatar: '', // 用户头像
      uploadHeader: {Authorization: 'Bearer ' + getAccessToken()},
      // uploadParams: {ticket: ''},
      uploadAction: yufp.util.addTokenInfo(backend.fileService + '/api/file/provider/uploadfile'), // 头像上传地址
      pickerOptions: {
        disabledDate: function (time) {
          return time.getTime() < Date.now() - 8.64e7;
        } // 用户有效期禁用当前之前的日期
      },
      collapseTitle: [this.$t('sysUserManager.zk'), this.$t('sysUserManager.sq')]
    };
  },
  computed: {
    ...mapGetters(['orgTreeData'])
  },
  watch: {
    orgName: function (newVal, oldVal) {
      this.$refs.tree.filter(newVal);
    },
    searchOrgName: function (newVal, oldVal) {
      this.$refs.searchTree.filter(newVal);
    }
  },
  mounted: function () {
    this.userSts = lookup.find('DATA_STS', false);
    this.$store.dispatch('funData/orgTreeFn');
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
      this.remoteTableData('panel', 'usersTable', 'searchFormdata')
    },
    /**
    * 勾选表格
    * @param selection 当前勾选数据
    */
    selectFn: function (selection, disabledName) {
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
    fuzzyQuery: function (e) {
      var param = {keyWord: e.value, orgId: this.orgName};
      this.$refs.usersTable.remoteData(param);
    },
    /**
    * 通过所属机构查询
    * @param node 点击的节点信息
    * @param immidately 是否立即执行查询，区分快速查询和精确查询
    */
    changeOrgIdFn: function (node, immidately) {
      if (immidately) {
        this.orgName = node.orgId;
        this.$refs.usersTable.remoteData({orgId: node.orgId, keyWord: this.$refs.panel.inputVal});
        return;
      }
      this.searchFormdata.orgId = node.orgId;
      this.searchOrgName = node.orgId;
    },
    /**
    * 清空所属机构查询条件
    * @param immidately 是否立即执行查询，区分快速查询和精确查询
    */
    clearUpOrgIdFn: function (immidately) {
      if (immidately) {
        this.$refs.usersTable.remoteData({orgId: '', keyWord: this.$refs.panel.inputVal});
        return;
      }
      this.searchFormdata.orgId = '';
      this.searchOrgName = '';
    },
    /**
    * 清空所属机构查询条件
    * @param immidately 是否立即执行查询，区分快速查询和精确查询
    */
    formResetFn: function () {
      this.$refs.searchForm.resetFields();
      this.searchFormdata.orgId = '';
      this.searchOrgName = '';
    },
    /**
    * 关闭弹出框
    */
    cancelFn: function () {
      this.dialogVisible = false;
    },
    /**
    * 控制保存按钮、xdialog、表单的状态
    * @param viewType 表单类型
    * @param editable 可编辑,默认false
    */
    switchStatus: function (viewType, editable) {
      if (this.formdata && this.formdata.userSts === 'A' && viewType === 'EDIT') {
        this.$message({
          message: this.$t('sysUserManager.znxgtyhdqydjg'),
          type: 'warning'
        });
        return;
      }
      this.formdata.passwordShow = '******';
      this.viewType = viewType;
      this.dialogVisible = true;
      this.formType = viewType === 'DETAIL' ? 'details' : 'edit';
      this.formDisabled = !editable;
    },
    editFn: function (row) {
      var _this = this;
      if (row.userSts === 'A') {
        this.$message({
          message: _this.$t('sysUserManager.znxzsxhzdsxdsj'),
          type: 'warning'
        });
        return;
      }
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/adminsmuser/info/suborg/' + row.userId
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.switchStatus('EDIT', true);
          _this.$nextTick(function () {
            this.$refs.userForm.resetFields();
            clone(data, this.formdata);
            _this.selectValue = _this.formdata.orgId;
            _this.selectedDptId = _this.formdata.dptId;
            _this.userAvatar = yufp.util.addTokenInfo(backend.fileService + '/api/file/provider/download?fileId=' + _this.formdata.userAvatar);
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
    * 用户详情
    * @param row 当前用户信息
    */
    userDetailFn: function (row) {
      var _this = this;
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/adminsmuser/info/suborg/' + row.userId
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.switchStatus('DETAIL', true);
          _this.$nextTick(function () {
            _this.$refs.userForm.resetFields();
            clone(data, _this.formdata);
            _this.selectValue = _this.formdata.orgId;
            _this.selectedDptId = _this.formdata.dptId;
            _this.detailsValue = _this.formdata.orgName;
            _this.detailsDptValue = _this.formdata.dptName;
            if (_this.formdata.userAvatar) {
              _this.userAvatar = yufp.util.addTokenInfo(backend.fileService + '/api/file/provider/download?fileId=' + _this.formdata.userAvatar);
            } else {
              _this.userAvatar = ''
            }
          });
        } else {
          _this.$message({
            message: message
          });
        }
      });
    },
    /**
    * 新增用户
    * @param row 当前用户信息
    */
    addUserFn: function () {
      this.switchStatus('ADD', true);
      this.$nextTick(function () {
        this.$refs.userForm.resetFields();
        this.selectValue = '';
        this.selectedDptId = '';
        this.formdata.userSts = 'W';
        this.userId = '';
        this.searchOrgName = '';
        this.formdata.passwordShow = '******';
        this.$refs.userForm.setFormData({userSts: 'W', passwordShow: '******'});
      });
    },
    /**
    * 模糊机构树搜索
    * @param nodeData 当前节点信息
    */
    filterNode: function (value, nodeData, node) {
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
    filterNodeSearch: function (value, nodeData, node) {
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
    * @param value 当前输入信息
    * @param nodeData 当前节点属性信息
    * @param node 当前节点信息
    */
    changeOrgFn: function (node) {
      this.formdata.orgId = node.orgId;
      this.selectValue = node.orgId;
      // 部门树联动
      var temp = extend(true, {}, this.dptTreeParams);
      temp.dataParams = {orgId: node.orgId, dptSts: 'A'};
      this.dptTreeParams = temp;
      this.selectedDptId = '';
    },
    /**
    * 选择所属部门
    * @param node 当前选中机构信息
    */
    changeDptFn: function (node) {
      this.formdata.dptId = node.dptId;
      this.selectedDptId = node.dptId;
    },
    /**
    * 校验证件类型为
    */
    checkCertNoFn: function () {
      var _this = this;
      var flag = true;
      // 证件类型为 台湾居民身份证 境内居民身份证 港澳居民身份证 校验身份证号码
      if (_this.formdata.certType === 'D' || _this.formdata.certType === 'C' || _this.formdata.certType === 'E') {
        validator.IDCard('IDCard', _this.formdata.certNo, function (valid) {
          if (valid && valid.message) {
            _this.$message({
              message: valid.message,
              type: 'warning'
            });
            flag = false;
          }
        });
      }
      return flag;
    },
    orgClear() {
      this.formdata.orgId = ''
    },
    dptClear() {
      this.formdata.dptId = '';
      this.formdata.dptName = ''
    },
    /**
    * 保存用户
    */
    saveUser: function () {
      var _this = this;
      var validate = false;
      _this.$refs.userForm.validate(function (valid) {
        validate = valid;
      });
      if (!validate || !_this.checkCertNoFn()) {
        return;
      }
      var url = _this.formdata.userId ? '/api/adminsmuser/update' : '/api/adminsmuser/add';
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + url,
        data: _this.formdata
        // isCrypto:true
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$message({
            message: _this.$t('sysUserManager.sjbccg'),
            type: 'success'
          });
          _this.remoteData();
          _this.dialogVisible = false;
        } else {
          _this.$message({
            message: message || _this.$t('sysUserManager.sjczsb'),
            type: 'error'
          });
        }
      });
    },
    /**
    * 上传头像成功
    */
    handleAvatarSuccess: function (res, file) {
      console.log(res.body);
      this.formdata.userAvatar = res.body;
      this.userAvatar = yufp.util.addTokenInfo(backend.fileService + '/api/file/provider/download?fileId=' + res.body);
    },
    /**
    * 文件上传之前获取上传校验
    */
    beforeAvatarUpload: function (file) {
      var _this = this;
      var type = file.type;
      var size = file.size / 1024 / 1024;
      if (type !== 'image/jpeg' && type !== 'image/png' && type !== 'image/jpg') {
        this.$message.error(this.$t('sysUserManager.sctxtpznsgs'));
      }
      if (size > 1) {
        this.$message.error(this.$t('sysUserManager.sctxtpdxbncg'));
        return;
      }
      // _this.$request({
      //   method: 'POST',
      //   async: false,
      //   url: backend.appOcaService + '/api/filecode/codegenerate'
      // }).then(({code, message, data}) => {
      //   if (data.code && data.code === '0000') {
      //     _this.uploadParams.ticket = data.data;
      //   }
      // });
      return type && size;
    },
    /**
    * 启用用户
    */
    useUserFn: function (row) {
      var _this = this;
      var selections = row.userId ? [row] : _this.$refs.usersTable.selections;
      if (selections.length < 1) {
        _this.$message({ message: _this.$t('sysUserManager.qxzytsj'), type: 'warning' });
        return;
      }
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].userSts === 'W' || selections[i].userSts === 'I') {
          // 只能选择停用或待启用状态的用户
          ids.push(selections[i].userId);
        } else {
          this.$message({
            message: this.$t('sysUserManager.znxzsxhzdsxdsj'),
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
            // 发起启用用用户请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmuser/batchenable',
              data: ids
            }).then(({code, message, data}) => {
              if (code === '0000') {
                _this.$message({
                  // message: _this.$t('sjczcg.sjczcg'),
                  message: _this.$t('sysUserManager.sjczcg'),
                  type: 'success'
                });
                _this.remoteData();
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
    * 停用用户
    * @param row 当前用户信息
    */
    stopUserFn: function (row) {
      // 校验是否已选择数据
      var _this = this;
      var selections = row.userId ? [row] : _this.$refs.usersTable.selections;
      if (selections.length < 1) {
        this.$message({ message: this.$t('sysUserManager.qxzytsj'), type: 'warning' });
        return;
      }
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].userSts === 'A') {
          // 只能停用生效的用户
          ids.push(selections[i].userId);
        } else {
          this.$message({
            message: this.$t('sysUserManager.znxzysxdsj'),
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
            // 发起停用用户请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmuser/batchdisable',
              data: ids
            }).then(({code, message, data}) => {
              if (code === '0000') {
                _this.$message({
                  // message: _this.$t('sysUserManager.sjczcg')
                  message: _this.$t('sysUserManager.sjczcg'),
                  type: 'success'
                });
                _this.remoteData();
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
    * 重置密码
    * @param row 当前用户信息
    */
    resetPassword: function (row) {
      var _this = this;
      this.$confirm(this.$t('sysUserManager.qdjcyhdmmczwxtcsmm'), this.$t('sysUserManager.ts'), {
        confirmButtonText: this.$t('sysUserManager.qd'),
        cancelButtonText: this.$t('sysUserManager.qx'),
        type: 'warning',
        callback: function (action) {
          if (action === 'confirm') {
            // 发起停用用户请求
            _this.$request({
              url: backend.appOcaService + '/api/password/resetpassword',
              method: 'POST',
              data: {
                loginCode: row.loginCode
              }
            }).then(({code, message, data}) => {
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('sysUserManager.mmczcg'),
                  type: 'success'
                });
                _this.remoteData();

              } else {
                _this.$message({
                  message: _this.$t('sysUserManager.mmczsb'),
                  type: 'error'
                });
              }
            });
          }
        }
      });
    },
    /**
    * 删除用户
    * @param row 当前行数据
    */
    deleteFn: function (row) {
      var _this = this;
      var selections = row.orgId ? [row] : this.$refs.usersTable.selections;
      if (selections.length < 1) {
        this.$message({
          message: this.$t('sysUserManager.qxxzyscdsj'),
          type: 'warning'
        });
        return;
      }
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].userSts === 'W' || selections[i].userSts === 'I') {
          // 只能删除待生效停用状态的用户
          ids.push(selections[i].userId);
        } else {
          this.$message({
            message: this.$t('sysUserManager.znsctydsj'),
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
            // 发起停用用户请求
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmuser/batchdelete',
              data: ids
            }).then(({code, message, data}) => {
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('sysUserManager.sjsccg'),
                  type: 'success'
                });
                _this.remoteData();
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
    * 用户关联信息
    * @param row 当前行数据
    */
    userRelFn: function (row) {
      this.relDialogVisible = true;
      this.orgTreeParams.userId = row.userId;
      this.currentUser = row;
      this.activeName = 'org'; // 默认打开角色管理TAB
      this.$nextTick(function () {
        this.roleSelect = false;
        this.orgSelect = false;
        this.dutySelect = false;
        this.rolesTable = [];
        this.dutysTable = [];
        this.treeKey = row.userId;
        this.checkedOrg.splice(0);
        this.$refs.rolesTable.remoteData({userId: row.userId});
        this.$refs.dutysTable.remoteData({userId: row.userId});
        this.$refs.orgTree.remoteData(); // todo 参数改变默认刷新？
      });
    },
    searchRelOrgFn: function () {
      this.$refs.orgTree.filter(this.orgKeyWord);
    },
    filterOrgSearch: function (value, nodeData, node) {
      return nodeData.orgName.indexOf(this.orgKeyWord) !== -1;
    },
    /**
    * 查询角色 岗位
    * @param refname 表格ref名称
    * @param keyWord 查询关键字
    */
    searchRelFn: function (refname, keyWord) {
      if (this.$refs[refname]) {
        this.$refs[refname].remoteData({userId: this.currentUser.userId, keyWord: this[keyWord]});
      }
    },
    /**
    * 显示角色 岗位关联关系
    * @param tableData 表格加载完的数据
    * @param refname 表格Ref 名称
    */
    getTableSelectedFn: function (tableData, refname) {
    // 启用的用户 relSts 关联状态为已关联A时为关联，待启用和停用的用户relSts为 待启用时W为关联
      // var flag = this.currentUser.userSts === 'A' ? 'A' : 'W';
      for (var i = 0; i < tableData.length; i++) {
        if (tableData[i].checked) {
          this[refname].push(tableData[i]);
          this.$refs[refname].toggleRowSelection(tableData[i], true);
        }
      }
    },
    /**
    * 将树形结构数据转换成数组结构
    * @param treeData 表格加载完的数据
    * @param nodeId 树节点 ID // todo 是否提供公共方法
    */
    getListTreeData: function (data, nodeId) {
      var arr = [];
      var loopFun = function (treeData, nodeId) {
        for (var i = 0; i < treeData.length; i++) {
          if (treeData[i].children && Array.isArray(treeData[i].children) && treeData[i].children.length > 0) {
            arr.push(loopFun(treeData[i].children, nodeId));
            delete treeData[i].children;
            if (treeData[i].hasOwnProperty(nodeId)) {
              arr.push(treeData[i]);
            }
          } else {
            arr.push(treeData[i]);
          }
        }
      };
      loopFun(data, nodeId);
      return arr;
    },
    /**
    * 用户关联机构信息
    * @param treeData 表格加载完的数据
    */
    getSelectOrgFn: function (treeData) {
      // 启用的用户 relSts 关联状态为已关联A时为关联，待启用和停用的用户relSts为 待启用时W为关联
      // 将树形结构数据转换成数组结构
      var treeList = this.getListTreeData(treeData, 'orgId');
      this.checkedOrg.splice(0);
      // var flag = this.currentUser.userSts === 'A' ? 'A' : 'W';
      for (var i = 0; i < treeList.length; i++) {
        if (treeList[i] && treeList[i].checked) {
          this.checkedOrg.push(treeList[i].orgId);
        }
      }
    },
    /**
    * 用户关联机构信息
    * @param relType 机构 岗位 角色 是否操作过
    */
    relSelectFn: function (relType) {
      if (relType === 'roleSelect') {
        if (this.rolesTable.length === this.$refs.rolesTable.selections.length) {
          if (this.rolesTable.length === 0) {
            this[relType] = false;
          } else {
            for (var item of this.rolesTable) {
              if (this.$refs.rolesTable.selections.indexOf(item) !== -1) {
                this[relType] = false;
                return;
              }
            }
            this[relType] = true;
          }
        } else {
          this[relType] = true;
        }
      }
      if (relType === 'dutySelect') {
        if (this.dutysTable.length === this.$refs.dutysTable.selections.length) {
          if (this.dutysTable.length === 0) {
            this[relType] = false;
          } else {
            for (var item of this.dutysTable) {
              if (this.$refs.dutysTable.selections.indexOf(item) !== -1) {
                this[relType] = false;
                return;
              }
            }
            this[relType] = true;
          }
        } else {
          this[relType] = true;
        }
      }
      if (relType === 'orgSelect') {
        this[relType] = true;
      }
    },
    /**
    * 保存用户关联机构 角色 岗位
    */
    saveUserRelFn: function () {
      var _this = this;
      // 判断关联信息是否更改过
      if (_this.orgSelect || _this.dutySelect || _this.roleSelect) {
        _this.$confirm(_this.$t('sysUserManager.qdbcglxx'), _this.$t('sysUserManager.ts'), {
          confirmButtonText: _this.$t('sysUserManager.qd'),
          cancelButtonText: _this.$t('sysUserManager.qx'),
          type: 'warning',
          callback: function (action) {
            if (action === 'confirm') {
              if (_this.orgSelect) {
                _this.saveRel('/api/adminsmuser/saveusermgrorg/', _this.$refs.orgTree.getCheckedKeys(), _this.$t('sysUserManager.jgsjczcg')); // 保存机构关联信息
              }
              if (_this.roleSelect) {
                // 获取角色IDS
                var roleIds = _this.getSelectionsIds('rolesTable', 'roleId');
                _this.saveRel('/api/adminsmuser/saveuserrole/', roleIds, _this.$t('sysUserManager.jssjczcg')); // 保存角色关联信息
              }
              if (_this.dutySelect) {
                // 获取岗位IDS
                var dutyIds = _this.getSelectionsIds('dutysTable', 'dutyId');
                _this.saveRel('/api/adminsmuser/saveuserduty/', dutyIds, _this.$t('sysUserManager.gwsjczcg')); // 保存岗位关联信息
              }
            } else {
              _this.orgSelect = false;
              _this.dutySelect = false;
              _this.roleSelect = false;
            }
          }
        });
      } else {
        _this.$message({
          message: _this.$t('sysUserManager.zwxgrhglxx'),
          type: 'warning'
        });
      }
    },
    /**
    * 获取表格选中数据 IDS
    * @param refname 表格ref名称
    *  @param key 表格数据唯一关键字
    */
    getSelectionsIds: function (refname, key) {
      var selections = this.$refs[refname].selections;
      var ids = [];
      for (var i = 0; i < selections.length; i++) {
        ids.push(selections[i][key]);
      }
      return ids;
    },
    /**
    * 保存用户关联信息
    * @param url 保存地址
    * @param ids 保存的数据IDS
    * @param msg 保存成功后的提示消息
    */
    saveRel: function (url, ids, msg) {
      var _this = this;
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + url + _this.currentUser.userId,
        data: ids
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$message({
            message: msg,
            type: 'success'
          });
          _this.relDialogVisible = false;
        } else {
          _this.$message({
            message: message || _this.$t('sysUserManager.sjczsb'),
            type: 'error'
          });
        }
      });
    },
    cancelRelFn: function () {
      this.relDialogVisible = false;
    }
  }
};
</script>
<style>
.yu-user-pic-cust{
  padding-bottom: 20px;
}
.el-form-details .yu-user-pic-cust{
  padding-bottom: 12px;
}
.compact .el-form-details .yu-user-pic-cust{
  padding-bottom: 21px;
}
.compact .yu-user-pic-cust{
  padding-bottom: 10px;
}
.compact .yu-user-pic-box{
  height: 120px;
  width: 130px;
}
.compact .yu-icon-user{
  font-size: 60px !important;
}
.el-form-details .yu-user-pic-cust{
  padding-bottom: 13px;

}
</style>

