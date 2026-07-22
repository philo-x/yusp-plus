<!--
  @Created by panglx@yusys.com.cn 2020-12-22
  @updated by
  @description 控制点管理
-->
<template>
  <div class="control-Manage">
    <yu-panel :title="$t('resContrManage.kzdgl')" show-search-input :placeholder="$t('resContrManage.gjz')" @search="fuzzyQueryFn" ref="panelRef">
      <template slot="right">
        <yu-toolBar>
          <yu-button v-if="checkCtrl('add')" @click="pointAddFn">{{ $t('resContrManage.xz') }}</yu-button>
          <yu-button v-if="checkCtrl('delete')" @click="pointDeleteFn" v-norepeat.disabled>{{ $t('resContrManage.sc') }}</yu-button>
        </yu-toolBar>
      </template>
      <!--快速查询-自定义查询区域-->
      <template slot="search">
        <yu-combo-tree
          v-model="menuName"
          :placeholder="$t('resContrManage.cd')"
          :local-data="menuTreeData"
          data-id="menuId"
          data-label="menuName"
          data-child="childrenList"
          data-pid="upMenuId"
          ref="menuTree"
          :max-height="400"
          @node-click="menuNodeClickFn($event, true)"
          @clear="clearMenuIdFn(true)"
          :filter-node-method="menuFilterNodeFn"
          clearable>
        </yu-combo-tree>
      </template>
      <template slot="filter">
        <yu-xform v-model="searchFormdata" related-table-name="pointTable" ref="searchForm" form-type="search" @form-reset="resetFn">
          <yu-xform-group :column="4">
            <yu-xform-item :label="$t('resContrManage.kzdmc')" :placeholder="$t('resContrManage.qsr')" name="contrName" maxlength="200"></yu-xform-item>
            <yu-xform-item :label="$t('resContrManage.cd')" name="menuId" ctype="custom" class="fix-height">
              <yu-combo-tree
                v-model="searchMenuName"
                :placeholder="$t('resContrManage.qxz')"
                :local-data="menuTreeData"
                ref="searchTree"
                data-id="menuId"
                data-label="menuName"
                data-pid="upMenuId"
                data-child="childrenList"
                :max-height="400"
                :filter-node-method="filterNodeSearchFn"
                @node-click="menuNodeClickFn($event, false)"
                @clear="clearMenuIdFn(false)"
                clearable>
              </yu-combo-tree>
            </yu-xform-item>
            <yu-xform-item :label="$t('resContrManage.czdm')" :placeholder="$t('resContrManage.qsr')" name="contrCode" maxlength="100"></yu-xform-item>
            <yu-xform-item :label="$t('resContrManage.czUrl')" :placeholder="$t('resContrManage.qsr')" name="contrUrl" maxlength="100"></yu-xform-item>
            <yu-xform-item :label="$t('resContrManage.HTTPff')" :placeholder="$t('resContrManage.HTTPff')" name="methodType" ctype="select" data-code="HTTP_METHOD_TYPE"></yu-xform-item>
            <yu-xform-item :label="$t('resContrManage.qzjm')" :placeholder="$t('resContrManage.qzjm')" name="encodeCheck" ctype="select" data-code="YESNO"></yu-xform-item>
            <yu-xform-item :label="$t('resContrManage.qzfc')" :placeholder="$t('resContrManage.qzfc')" name="nonceCheck" ctype="select" data-code="YESNO"></yu-xform-item>
          </yu-xform-group>
        </yu-xform>
      </template>
      <!-- 控制点列表 -->
      <yu-xtable request-type="POST" :data-url="contrDataUrl" row-number selection-type="checkbox" ref="pointTable">
        <yu-xtable-column prop="contrName" :label="$t('resContrManage.kzdmc')" sortable>
          <template slot-scope="scope"><a class="underline" @click="contrInfoFn(scope.row)">{{ scope.row.contrName }}</a></template>
        </yu-xtable-column>
        <yu-xtable-column prop="menuName" :label="$t('resContrManage.cdmc')"></yu-xtable-column>
        <yu-xtable-column prop="contrCode" :label="$t('resContrManage.czdm')"></yu-xtable-column>
        <yu-xtable-column prop="contrUrl" :label="$t('resContrManage.czUrl')" width="280px"></yu-xtable-column>
        <yu-xtable-column prop="methodType" :label="$t('resContrManage.HTTPff')" width="120px"></yu-xtable-column>
        <yu-xtable-column :label="$t('resContrManage.cz')" fixed="right" width="180px">
          <template slot-scope="scope">
            <yu-button-drop :show-length="2" type="text">
              <yu-button v-if="checkCtrl('relate')" @click="openAuthFn(scope.row)" type="text" v-norepeat.disabled>{{ $t('resContrManage.glsjmb') }}</yu-button>
              <yu-button v-if="checkCtrl('edit')" @click="pointEditFn(scope.row)" type="text">{{ $t('resContrManage.xg') }}</yu-button>
              <yu-button v-if="checkCtrl('delete')" @click="pointDeleteFn(scope.row)" type="text" v-norepeat.disabled>{{ $t('resContrManage.sc') }}</yu-button>
            </yu-button-drop>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>

    <!-- 控制点dialog -->
    <yu-xdialog :title="dialogTitle" :visible.sync="dialogVisible">
      <yu-xform ref="pointForm" :label-width="language==='en'?'130px':'120px'" v-model="pointFormData" :form-type="formType" :rules="pointrRules">
        <yu-xform-group :column="2">
          <yu-xform-item :label="$t('resContrManage.cd')" name="menuName" :placeholder="$t('resContrManage.qxz')" suffix-icon="yu-icon-search1" @focus="menuNameFocusFn" @suffix-click="menuNameFocusFn" @clear="clearMenuName" clearable></yu-xform-item>
          <yu-xform-item :label="$t('resContrManage.czdm')" name="contrCode" :placeholder="$t('resContrManage.qsr')" :disabled="codeDisabled"></yu-xform-item>
          <yu-xform-item :label="$t('resContrManage.kzdmc')" name="contrName" :placeholder="$t('resContrManage.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('resContrManage.HTTPff')" name="methodType" :placeholder="$t('resContrManage.qxz')" ctype="select" data-code="HTTP_METHOD_TYPE"></yu-xform-item>
          <yu-xform-item :label="$t('resContrManage.czUrl')" name="contrUrl" :placeholder="$t('resContrManage.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('resContrManage.sjmb')" name="authTmplName" :placeholder="$t('resContrManage.qxz')" suffix-icon="yu-icon-search1" @focus="authNameFocusFn" @suffix-click="authNameFocusFn" clearable></yu-xform-item>
          <yu-xform-item :label="$t('resContrManage.qzjm')" name="encodeCheck" ctype="select" data-code="YESNO" :placeholder="$t('resContrManage.qzjm')"></yu-xform-item>
          <yu-xform-item :label="$t('resContrManage.qzfc')" name="nonceCheck" ctype="select" data-code="YESNO" :placeholder="$t('resContrManage.qzfc')"></yu-xform-item>
          <yu-xform-item :label="$t('resContrManage.zjgxr')" name="lastChgName" :hidden="formType == 'edit'"></yu-xform-item>
          <yu-xform-item :label="$t('resContrManage.zjgxsj')" name="lastChgDt" :hidden="formType == 'edit'"></yu-xform-item>
          <yu-xform-item :label="$t('resContrManage.bz')" name="contrRemark" ctype="textarea" colspan="24" :placeholder="$t('resContrManage.qsr')" maxlength="200" show-word-limit></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
      <div v-if="formType == 'edit'" slot="footer" class="dialog-footer" key="edit">
        <yu-button type="primary" v-norepeat.disabled="saveDisabled" @click="saveFn">{{ $t('resContrManage.bc') }}</yu-button>
        <yu-button @click="dialogVisible = false">{{ $t('resContrManage.qx') }}</yu-button>
      </div>
      <div v-else slot="footer" class="dialog-footer" key="info">
        <yu-button v-if="checkCtrl('edit')" type="primary" @click="swithEditFn">{{ $t('resContrManage.xg') }}</yu-button>
        <yu-button @click="dialogVisible = false">{{ $t('resContrManage.fh') }}</yu-button>
      </div>
    </yu-xdialog>

    <!-- 选择菜单dialog -->
    <yu-xdialog :title="$t('resContrManage.xzcd')" :visible.sync="menuDialogVisible" height="540px" class="control-dialog">
      <div class="dialog-search">
        <yu-checkbox v-model="checkMenu" @change="checkMenuChangeFn">{{ $t('resContrManage.jkygl') }}</yu-checkbox>
        <yu-input
          v-model="menuVal"
          :placeholder="$t('resContrManage.gjz')"
          suffix-icon="yu-icon-search1"
          @suffix-click="menuSearchFn"
          @keyup.enter.native="menuSearchFn"
          maxlength="32"
          class="form-item"
          clearable></yu-input>
      </div>
      <yu-xtable
        :data-url="menulUrl"
        request-type="POST"
        :base-params="menuParams"
        row-number
        selection-type="checkbox"
        ref="menuTable"
        @loaded="menuLoadFn"
        @selection-change="menuChangeFn"
        :default-load="false">
        <yu-xtable-column prop="menuName" :label="$t('resContrManage.cdmc')"></yu-xtable-column>
        <yu-xtable-column prop="upMenuName" :label="$t('resContrManage.fjcdmc')"></yu-xtable-column>
      </yu-xtable>
      <div slot="footer" class="dialog-footer">
        <yu-button type="primary" @click="saveMenuFn" v-norepeat.disabled>{{ $t('resContrManage.qd') }}</yu-button>
        <yu-button @click="menuDialogVisible = false">{{ $t('resContrManage.qx') }}</yu-button>
      </div>
    </yu-xdialog>

    <!-- 选择数据模板dialog -->
    <yu-xdialog
      :title="$t('resContrManage.xzsjmb')"
      :visible.sync="authDialogVisible"
      height="540px"
      :width="language==='en'?'950px':''"
      class="control-dialog">
      <div class="dialog-search">
        <yu-checkbox v-model="checkAuthTmpl" @change="checkAuthChangeFn">{{ $t('resContrManage.jkygl') }}</yu-checkbox>
        <yu-input
          v-model="authVal"
          :placeholder="$t('resContrManage.gjz')"
          suffix-icon="yu-icon-search1"
          @suffix-click="authSearchFn"
          @keyup.enter.native="authSearchFn"
          maxlength="32"
          class="form-item"
          clearable></yu-input>
      </div>
      <yu-xtable
        :data-url="authTmplUrl"
        request-type="POST"
        :base-params="dataAuthParams"
        row-number
        selection-type="checkbox"
        ref="dataAuthTable"
        @loaded="authLoadFn"
        :default-load="false">
        <yu-xtable-column prop="authTmplName" :label="$t('resContrManage.mbmc')"></yu-xtable-column>
        <yu-xtable-column prop="sqlName" :label="$t('resContrManage.zwfmc')"></yu-xtable-column>
        <yu-xtable-column prop="sqlString" :label="$t('resContrManage.sjqxtj')" width="250"></yu-xtable-column>
        <yu-xtable-column :label="$t('resContrManage.sfysq')">
          <template slot-scope="scope">
            <span>{{ scope.row.authRecoId ? '是': '否' }}</span>
          </template>
        </yu-xtable-column>
      </yu-xtable>
      <div slot="footer" class="dialog-footer">
        <yu-button type="primary" @click="saveRelateFn" v-norepeat.disabled>{{ $t('resContrManage.qd') }}</yu-button>
        <yu-button @click="authDialogVisible = false">{{ $t('resContrManage.qx') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import { mapGetters } from "vuex"
import { checkBelongToChooseNode } from '@/utils/util'
import { clone, extend, lookup } from '@/utils'
import { validator } from "@/utils/validate"
lookup.reg('HTTP_METHOD_TYPE');
export default {
  data() {
    return {
      searchMenuName: '', // 查询表单下拉菜单树查询绑定值
      searchFormdata: {}, // 查询表单数据
      menuName: '', // 自定义菜单查询Id
      menuTreeUrl: backend.appOcaService + '/api/adminsmmenu/tree', // 下拉菜单树查询条件
      menuTreeParam: {sysId: '', upMenuId: -1, treeDeep: -1}, // 下拉菜单树查询条件
      menuTreeData: [], // 下拉菜单树数据
      contrDataUrl: backend.appOcaService + '/api/adminsmrescontr/getcontrinfo', // 控制点表格查询数据url
      addUrl: backend.appOcaService + '/api/adminsmrescontr/createcontr', // 新增保存控制点url
      editUl: backend.appOcaService + '/api/adminsmrescontr/editcontr', // 修改保存控制点url
      menulUrl: backend.appOcaService + '/api/adminsmrescontr/getmenulist', // 选择菜单查询URL
      authTmplUrl: backend.appOcaService + '/api/adminsmrescontr/datatmpllist', // 数据模板表单查询URL
      pointFormData: {}, // 控制点表单数据
      codeDisabled: true, // 控制代码是否禁用
      pointParam: {},
      currentMenuId: '', // 当前行的menuId
      currentContrId: '', // 当前行的menuId
      // 新增修改控制点表单验证规则
      pointrRules: {
        menuName: [{ required: true, message: this.$t('resContrManage.qxzcd') }],
        contrName: [
          { required: true, message: this.$t('resContrManage.qsrkzdmc') },
          { validator: validator.speChar, message: this.$t('resContrManage.srxxbhtszf') },
          {max: 200, message: this.$t('resContrManage.srzdcdbcgeb')}
        ],
        contrCode: [
          { required: true, message: this.$t('resContrManage.qsrczdm') },
          { validator: validator.speChar, message: this.$t('resContrManage.srxxbhtszf') },
          { validator: this.codeValidFn, trigger: 'blur'},
          {max: 100, message: this.$t('resContrManage.srzdcdbcgyb')}
        ],
        contrUrl: [{validator: this.urlValidFn}, {max: 100, message: this.$t('resContrManage.srzdcdbcgyb')}]
      },
      dialogVisible: false, // 表单弹框是否显示
      dialogTitle: '', // 弹框标题
      formType: 'edit',
      checkMenu: '', // 是否仅查看已选择的菜单选项
      menuVal: '', // 菜单列表搜索关键词
      // 菜单查询条件
      menuParams: {
        keyWord: '',
        check: this.checkMenu ? 1 : 0
      },
      menuDialogVisible: false, // 选择菜单Dialog
      authDialogVisible: false, // 数据权限模板
      authVal: '', // 选择数据权限模板
      isRelateTmpl: false, // 是否是直接打开选择数据权限模板
      checkAuthTmpl: '', // 是否仅查看已选择的数据模板选项
      // 数据模板列表参数
      dataAuthParams: {
        keyWord: '',
        check: this.checkAuthTmpl ? 1 : 0
      },
      authTmplId: '', // 选择后的模板数版id
      saveDisabled: { show: false } // 保存按钮防重复提交
    };
  },
  computed: {
    ...mapGetters([
      "logicSys",
      'language'
    ])
  },
  watch: {
    menuName(newVal, oldVal) {
      this.$refs.menuTree && this.$refs.menuTree.filter(newVal);
    },
    searchMenuName(newVal, oldVal) {
      this.$refs.searchTree && this.$refs.searchTree.filter(newVal);
    }
  },
  created() {
    const _this = this;
    _this.$request({
      method:'POST',
      url: backend.appOcaService + '/api/adminsmmenu/tree',
      data: {
        sysId: _this.logicSys.id,
        upMenuId: -1,
        treeDeep: -1
      }
    }).then(({code, message, data}) => {
      if (code === '0000') {
        _this.menuTreeData = extend([], data)
      } else {
        _this.$message({ message: message, type: 'error' });
      }
    })
  },
  methods: {
    remoteTableData(panelRef, tableRef, searFormVmodel) {
      // panel隐藏的时候
      if (this.$refs[panelRef].hide) {
        this.pointParam.keyWord = this.$refs[panelRef].inputVal
        this.$refs[tableRef].remoteData(this.pointParam)
      } else {
        this.$refs[tableRef].remoteData(this[searFormVmodel])
      }
    },
    remoteData() {
      this.remoteTableData('panelRef', 'pointTable', 'searchFormdata')
    },
    /**
    * 控制代码唯一性校验
    * @param rule 规则，value 输入框值， callbackFn回调函数
    */
    codeValidFn(rule, value, callbackFn) {
      var _this = this;
      if (value === '' || value === null || value === undefined) {
        callbackFn(new Error(_this.$t('resContrManage.qsrczdm')));
        return;
      }
      _this.$request({
        url: backend.appOcaService + '/api/adminsmrescontr/checkcode',
        method: 'post',
        async: false,
        data: {
          contrCode: _this.pointFormData.contrCode || '',
          funcId: _this.pointFormData.funcId || '',
          contrId: _this.pointFormData.contrId || ''
        }
      }).then(({code, message, data}) => {
        if (data.data > 0) {
          callbackFn(new Error(_this.$t('resContrManage.cywgnybhgkzczdm')));
        } else {
          callbackFn();
        }
      })
    },

    /**
    * 操作url唯一性校验
    * @param rule 规则，value 输入框值， callback回调函数
    */
    urlValidFn(rule, value, callback) {
      if (value && /[\u4E00-\u9FA5]/g.test(value)) {
        callback(new Error(this.$t('resContrManage.urlbnsrzw')));
      } else if (value && !/^\//g.test(value)) {
        callback(new Error(this.$t('resContrManage.urlbxyxkks')));
      } else {
        callback();
      }
    },

    /**
    * 点击搜索框模糊查
    * @param e.value 搜索框的值
    */
    fuzzyQueryFn(e) {
      this.pointParam.keyWord = e.value;
      this.$refs.pointTable.remoteData(this.pointParam);
    },

    /**
    * 清空精确查询表单查询参数
    */
    resetFn() {
      this.searchMenuName = '';
    },

    /**
    * 选择菜单树查询
    * @param node 点击的节点信息
    * @param immidately 是否立即执行查询，区分快速查询和精确查询
    */
    menuNodeClickFn(node, immidately) {
      if (node.childrenList && node.childrenList.length > 0) {
        return;
      }
      if (immidately) {
        this.pointParam = {
          keyWord: this.$refs.panelRef.inputVal || '',
          menuId: node.menuId || '',
          funcId: node.funcId || ''
        };
        this.$refs.pointTable.remoteData(this.pointParam);
        return;
      }
      this.searchFormdata.menuId = node.menuId || '';
      this.searchFormdata.funcId = node.funcId || '';
    },

    /**
    * 清空菜单树查询
    * @param immidately 是否立即执行查询，区分快速查询和精确查询
    */
    clearMenuIdFn(immidately) {
      if (immidately) {
        this.pointParam = {
          keyWord: this.$refs.panelRef.inputVal,
          menuId: '',
          funcId: ''
        };
        this.$refs.pointTable.remoteData(this.pointParam);
        return;
      }
      this.searchFormdata.menuId = '';
      this.searchFormdata.funcId = '';
    },

    /**
    * 表单查询菜单树对树节点进行筛选时执行的方法，返回 true 表示这个节点可以显示，返回 false 则表示这个节点会被隐藏
    * @param value 当前输入信息
    * @param nodeData 当前节点属性信息
    * @param node 当前节点信息
    */
    filterNodeSearchFn(value, nodeData, node) {
      if (!this.$refs.searchTree.selectedLabel) {
        return true;
      }
      // 如果传入的value和data中的label相同说明是匹配到了
      if (nodeData.menuName.indexOf(this.$refs.searchTree.selectedLabel) !== -1) {
        return true;
      }
      // 否则要去判断它是不是选中节点的子节点
      return checkBelongToChooseNode(this.$refs.searchTree.selectedLabel, node, 'menuName');
    },

    /**
    * 自定义菜单树对树节点进行筛选时执行的方法，返回 true 表示这个节点可以显示，返回 false 则表示这个节点会被隐藏
    * @param value 当前输入信息
    * @param nodeData 当前节点属性信息
    * @param node 当前节点信息
    */
    menuFilterNodeFn(value, nodeData, node) {
      if (!this.$refs.menuTree.selectedLabel) {
        return true;
      }
      if (nodeData.menuName.indexOf(this.$refs.menuTree.selectedLabel) !== -1) {
        return true;
      }
      // 否则要去判断它是不是选中节点的子节点
      return checkBelongToChooseNode(this.$refs.menuTree.selectedLabel, node, 'menuName');
    },

    // 控制点表单菜单项获取焦点事件
    menuNameFocusFn() {
      this.menuDialogVisible = true;
      this.menuVal = '';
      this.checkMenu = false;
      this.$nextTick(function () {
        this.menuParams.lastMenuId = this.pointFormData.menuId || '';
        this.$refs.menuTable.remoteData(this.menuParams);
      });
    },

    // 选择菜单表格数据加载完 (data, total, response),data为表格加载完成后表格数据，total为表格数据总数,response 为整个返回报文
    menuLoadFn (d) {
      var _this = this;
      d.forEach(function (item) {
        if (item.menuId === _this.pointFormData.menuId) {
          _this.$refs.menuTable.toggleRowSelection(item, true);
        }
      });
    },

    /**
    * 选择数据权限模板当选择项发生变化时会触发的事件
    * @param param selection
    */
    menuChangeFn(selection) {
      if (selection.length > 1) {
        this.$refs.menuTable.clearSelection();
        this.$refs.menuTable.toggleRowSelection(selection.pop());
      }
    },

    // 菜单仅看已选择 1：复选框选择 0：复选框未选
    checkMenuChangeFn(e) {
      this.$refs.menuTable.remoteData({keyWord: this.menuVal, check: e.target.checked ? 1 : 0});
    },

    // 选择菜单关键字搜索
    menuSearchFn() {
      this.$refs.menuTable.remoteData({keyWord: this.menuVal, check: this.checkMenu ? 1 : 0});
    },

    // 保存选择菜单
    saveMenuFn() {
      var selections = this.$refs.menuTable.selections;
      this.menuDialogVisible = false;
      this.pointFormData.menuName = (selections.length && selections[0].menuName) || '';
      this.pointFormData.menuId = (selections.length && selections[0].menuId) || '';
      this.pointFormData.funcId = (selections.length && selections[0].funcId) || '';
      this.codeDisabled = !this.pointFormData.funcId;
    },

    // 清空菜单名称
    clearMenuName() {
      this.pointFormData.menuId = '';
      this.pointFormData.funcId = '';
      this.codeDisabled = true;
    },

    // 控制点表单数据模板获取焦点事件
    authNameFocusFn() {
      this.authDialogVisible = true;
      this.isRelateTmpl = false;
      this.authVal = '';
      this.checkAuthTmpl = false;
      this.$nextTick(function () {
        this.dataAuthParams.contrId = this.dialogTitle == this.$t('resContrManage.xg') ? this.currentContrId : '';
        this.dataAuthParams.lastTmplIds = this.dialogTitle == this.$t('resContrManage.xz') ? this.authTmplId : [];
        this.$refs.dataAuthTable.remoteData(this.dataAuthParams);
      });
    },

    // 选择菜单表格数据加载完, (data, total, response),data为表格加载完成后表格数据，total为表格数据总数,response 为整个返回报文
    authLoadFn(d) {
      var _this = this;
      d.forEach(function (item) {
        if (_this.isRelateTmpl) {
          item.mark === 1 && _this.$refs.dataAuthTable.toggleRowSelection(item, true);
        } else {
          _this.authTmplId.indexOf(item.authTmplId) > -1 && _this.$refs.dataAuthTable.toggleRowSelection(item, true);
        }
      });
    },

    // 数据模板仅看已选择事件 1：复选框选择 0：复选框未选
    checkAuthChangeFn(e) {
      this.$refs.dataAuthTable.remoteData({keyWord: this.authVal, check: e.target.checked ? 1 : 0});
    },

    // 选择数据模板关键字搜索
    authSearchFn() {
      this.$refs.dataAuthTable.remoteData({keyWord: this.authVal, check: this.checkAuthTmpl ? 1 : 0});
    },

    /**
    * 保存关联数据模板
    */
    saveRelateFn() {
      const _this = this;
      const selections = this.$refs.dataAuthTable.selections;
      const authTmplId = [], authTmplName = [];
      if(selections.length) {
        selections.forEach(item => {
          authTmplId.push(item.authTmplId);
          authTmplName.push(item.authTmplName);
        });
      }
      if (this.dialogVisible) {
        this.pointFormData.authTmplName = authTmplName.join();
        this.authTmplId = authTmplId;
      } else {
        this.relateAuthTmplFn({
          contrId: this.dataAuthParams.contrId,
          authDataTmplIdList: authTmplId
        });
      }
      this.authDialogVisible = false;
      this.isRelateTmpl = false;
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

    /**
    * 选择数据权限模板当前行发生变化的时候会触发的事件
    * @param param curRow oldCurRow
    */
    authCurChangeFn(curRow, oldCurRow) {
      curRow && this.$refs.dataAuthTable.toggleRowSelection(curRow);
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
          _this.remoteData();
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      });
    },

    /**
    * 打开关联数据权限模板
    * @param row 选择当前行的数据
    */
    openAuthFn(row) {
      this.authDialogVisible = true;
      this.isRelateTmpl = true;
      this.authVal = '';
      this.checkAuthTmpl = false;
      this.currentMenuId = row.menuId;
      this.dataAuthParams.contrId = row.contrId;
      this.dataAuthParams.lastTmplIds = [];
      this.$nextTick(function () {
        this.$refs.dataAuthTable.remoteData(this.dataAuthParams);
      });
    },

    /**
    * 根据contrId获取关联数据权限模板信息
    */
    getAuthTmplFn(contrId) {
      var _this = this;
      _this.$request({
        url: backend.appOcaService + '/api/adminsmrescontr/associatedtmpl',
        method: 'post',
        data: {
          contrId: contrId
        }
      }).then(({code, message, data}) => {
        if (code === '0000') {
          if (data.length) {
            const authTmplId = [];
            const authTmplName = [];
            data.forEach(function (item) {
              authTmplId.push(item.authTmplId);
              authTmplName.push(item.authTmplName);
            });
            _this.authTmplId = extend([], authTmplId);
            _this.pointFormData.authTmplName = authTmplName.join();
          } else {
            _this.authTmplId = [];
            _this.pointFormData.authTmplName = '';
          }
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      });
    },

    /**
      * 控制保存按钮、xdialog、表单的状态
    * @param formType 表单类型 edit--可编辑，details--详情
    */
    switchStatus(formType) {
      this.dialogVisible = true;
      this.formType = formType;
    },

    /**
    * 查看控制点数据
    * @param row 选择当前行的数据
    */
    contrInfoFn(row) {
      this.switchStatus('details');
      this.dialogTitle = this.$t('resContrManage.xq');
      this.$nextTick(function () {
        this.$refs.pointForm.setFormData(row);
        this.getAuthTmplFn(row.contrId);
      });
    },

    // 新增控制点数据
    pointAddFn() {
      this.switchStatus('edit');
      this.dialogTitle = this.$t('resContrManage.xz');
      this.authTmplId = [];
      this.$nextTick(function () {
        this.$refs.pointForm.resetFields();
      });
    },

    /**
    * 修改控制点数据
    * @param row 修改当前行的数据
    */
    pointEditFn(row) {
      this.switchStatus('edit');
      this.currentContrId = row.contrId;
      this.dialogTitle = this.$t('resContrManage.xg');
      this.$nextTick(function () {
        this.$refs.pointForm.setFormData(row);
        this.getAuthTmplFn(row.contrId);
        this.codeDisabled = !row.funcId;
      });
    },

    /**
    * 删除数据权限模板，可批量删除
    * @param 有row 删除当前行的数据，无参数时表示批量删除
    */
    pointDeleteFn(row) {
      var _this = this;
      var ids = [];
      if (row && row.contrId) {
        ids.push(row.contrId);
      } else {
        var selections = _this.$refs.pointTable.selections;
        if (!selections.length) {
          this.$message({ message: this.$t('resContrManage.qxxzytjl'), type: 'warning' });
          return;
        }
        // 选择数据多行删除
        for (var i = 0; i < selections.length; i++) {
          ids.push(selections[i].contrId);
        }
      }
      _this.$confirm(_this.$t('resContrManage.sckzdjscqsjqxjsqsjqdsc'), _this.$t('resContrManage.ts'), {
        confirmButtonText: _this.$t('resContrManage.qd'),
        cancelButtonText: _this.$t('resContrManage.qx'),
        type: 'warning'
      }).then(function () {
        _this.$request({
          url: backend.appOcaService + '/api/adminsmrescontr/deletecontr',
          method: 'post',
          data:  ids
        }).then(({code, message, data}) => {
          if (code === '0000') {
            _this.$message({type: 'success', message: _this.$t('resContrManage.sjsccg') });
            _this.remoteData();
            // _this.$refs.pointTable.remoteData({keyWord: _this.$refs.panelRef.inputVal});
          } else {
            _this.$message({ message: message, type: 'error' });
          }
        });
      });
    },

    /**
    * 从详情转变为修改状态
    */
    swithEditFn() {
      this.formType = 'edit';
      this.dialogTitle = this.$t('resContrManage.xg');
      this.codeDisabled = !this.pointFormData.funcId;
    },

    /**
    * 保存数据权限模板
    * 先进行表单校验，再保存
    */
    saveFn() {
      var _this = this;
      var validate = false;
      _this.$refs.pointForm.validate(function (valid) {
        validate = valid;
      });
      if (!validate) {
        return;
      }
      var param = extend({}, this.pointFormData);
      // 判断表单数据是否存在id,有id为修改，没有为新增数据。
      var contrId = _this.pointFormData.contrId || '';
      // 有数据模板数据的才传authDataTmplIdList
      _this.authTmplId && (param.authDataTmplIdList = _this.authTmplId);
      delete param.authTmplName;
      delete param.menuName;
      _this.saveDisabled.show = true;
      _this.saveDisabled = clone(_this.saveDisabled, {});
      _this.$request({
        url: contrId ? _this.editUl : this.addUrl,
        method: 'post',
        data: param
      }).then(({code, message, data}) => {
        _this.saveDisabled.show = false;
        _this.saveDisabled = clone(_this.saveDisabled, {});
        if (code === '0000') {
          !contrId && _this.$message({ type: 'success', message: _this.$t('resContrManage.bccg') });
          contrId && _this.$message({ type: 'success', message: _this.$t('resContrManage.xgcg') });
          _this.remoteData();
          _this.dialogVisible = false;
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      });
    }
  }
}
</script>
