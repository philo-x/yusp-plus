<!--
  @created by tuxw@yusys.com.cn on 2020-12-22
  @description 菜单配置
  @updated by
-->
<template>
  <div class="yu-menu-config clear">
    <div class="left-content frame-height">
      <div class="content-title">
        <span>{{ $t('menuConfig.cdpz') }}</span>
        <yu-button type="text" @click="openFunctionDialog('menuConfig.ywgngl')">{{ $t('menuConfig.ywgngl') }}</yu-button>
      </div>
      <div class="content-box">
        <yu-toolBar>
          <div class="clear">
            <yu-input class="filter-text" v-model="filterText" :placeholder="$t('menuConfig.gjz')" size="small" icon="search"></yu-input>
            <div class="handle-box">
              <yu-button v-if="checkCtrl('add')" plain icon="yu-icon-plus5" :title="$t('menuConfig.xz')" @click="confirmAddMenu({menuId: '0', menuName: ''}, true)"></yu-button>
              <yu-button v-if="checkCtrl('delete') && isManaging" v-norepeat.disabled plain icon="yu-icon-delete2" :title="$t('menuConfig.sc')" @click="deleteMenu(false)"></yu-button>
              <yu-button v-if="isManaging" v-norepeat.disabled plain icon="yu-icon-cancel" :title="$t('menuConfig.qx')" @click="cancelManage"></yu-button>
              <yu-button v-if="checkCtrl('delete') && !isManaging" v-norepeat.disabled plain icon="yu-icon-setting3" :title="$t('menuConfig.gl')" @click="manageMenu"></yu-button>
              <yu-tooltip :content="$t('menuConfig.menuFormTips3')" placement="top" effect="light" popper-class="border-grey">
                <i class="icon yu-icon-question"></i>
              </yu-tooltip>
            </div>
          </div>
        </yu-toolBar>
        <yu-xtree ref="menuTree" class="menu-tree" :local-data="menuTreeData" data-id="menuId" data-pid="upMenuId" icon="menuIcon" :props="menuTreeProps" node-key="menuId" :current-node-key="currentCheckedNodeId" :default-expanded-keys="expandedKeys" :show-checkbox="isShowMenuTreeCheckbox" @node-click="menuTreeNodeClick" @node-drag-end="handleDragEnd" :render-content="menuTreeRenderContent" :filter-node-method="filterMenuTreeNode" :draggable="isMenuTreeDraggable"></yu-xtree>
      </div>
    </div>
    <div class="right-content frame-height">
      <div class="content-title">{{ rightContentTitle }}</div>
      <div v-show="!currentCheckedNodeId" class="no-data" :style="{'height': menuTreeHeight + 'px'}">
        <div class="no-choose">
          <img src="@/assets/common/images/no-data.svg" />
          <p class="text">{{ $t('menuConfig.noSelectText') }}</p>
        </div>
      </div>
      <div class="content-box">
        <!--菜单详情表单-->
        <yu-xform v-show="currentCheckedNodeId" ref="menuForm" class="menu-form" v-model="menuFormData" :label-width="language==='en'?'185px':'150px'" :form-type="menuFormType">
          <div v-if="currentCheckedNodeId">
            <yu-xform-group :column="1">
              <!--<yu-xform-item :label="$t('menuConfig.sjjd')" name="upMenuId" ctype="custom" rules="required">
                <div v-show="menuFormType === 'details'">{{ menuFormData.upMenuName }}</div>
                <yu-combo-tree v-show="menuFormType === 'edit'" ref="upMenuComboTree" v-model="menuFormData.upMenuId" :local-data="directoryTreeData" data-id="menuId" data-label="menuName" data-pid="upMenuId" data-root="-1" :all-node-value="true" :editable="false" @change="upMenuIdChange" :placeholder="$t('menuConfig.qxzsjjd')"></yu-combo-tree>
              </yu-xform-item>-->
              <yu-xform-item ref="upMenuComboTree" :label="$t('menuConfig.sjjd')" name="upMenuId" ctype="yu-combo-tree" rules="required" :local-data="directoryTreeData" data-id="menuId" data-label="menuName" data-pid="upMenuId" data-root="-1" :all-node-value="true" :editable="false" @change="upMenuIdChange" :placeholder="$t('menuConfig.qxzsjjd')"></yu-xform-item>
              <yu-xform-item :label="$t('menuConfig.cdlx')" name="menuClassify" ctype="custom" rules="required">
                <div v-if="menuFormType === 'details'">{{ menuFormData.menuClassify === '0' ? $t('menuConfig.ywgn') : $t('menuConfig.cjml') }}</div>
                <div v-else>
                  <yu-radio class="radio" v-model="menuFormData.menuClassify" label="0" @change="menuClassifyChange" :disabled="menuFormData.childrenLength > 0">{{ $t('menuConfig.ywgn') }}</yu-radio>
                  <yu-radio class="radio" v-model="menuFormData.menuClassify" label="1" @change="menuClassifyChange" :disabled="menuFormData.childrenLength > 0">{{ $t('menuConfig.cjml') }}</yu-radio>
                  <div class="form-tips">{{ $t('menuConfig.menuFormTips1') }}</div>
                </div>
              </yu-xform-item>
              <yu-xform-item v-if="menuFormData.menuClassify !== '1'" :label="$t('menuConfig.ywgn')" name="funcUrl" :maxlength="23" :rules="funcUrlRule" :placeholder="$t('menuConfig.qxz')" icon="search" @focus="openFunctionDialog('menuConfig.xzywgn')" readonly @click="openFunctionDialog('menuConfig.xzywgn')"></yu-xform-item>
              <yu-xform-item v-if="menuFormData.menuClassify !== '1'" :label="$t('menuConfig.cdmc')" name="menuName" :maxlength="23" :rules="menuNameRule" :placeholder="$t('menuConfig.qsr')"></yu-xform-item>
              <yu-xform-item v-if="menuFormData.menuClassify !== '0'" :label="$t('menuConfig.cjml')" name="menuName" :maxlength="23" :rules="menuNameRule" :placeholder="$t('menuConfig.qsr')"></yu-xform-item>
              <yu-xform-item :label="$t('menuConfig.tb')" name="menuIcon" ctype="custom">
                <div v-if="menuFormType === 'details' && menuFormData.menuIcon">
                  <span class="menu-icon"><i :class="menuFormData.menuIcon"></i></span>
                </div>
                <div v-else>
                  <span v-if="menuFormData.menuIcon" class="pointer" @click="openIconDialog(true)"><i :class="menuFormData.menuIcon"></i></span>
                  <span v-if="menuFormData.menuIcon" class="select-icon" @click="clearMenuIcon">{{ $t('menuConfig.sc') }}</span>
                  <span v-else-if="menuFormType === 'edit'" class="select-icon" @click="openIconDialog(true)">{{ $t('menuConfig.select') }}</span>
                  <span class="form-tips">{{ $t('menuConfig.menuFormTips2') }}</span>
                </div>
              </yu-xform-item>
              <yu-xform-item :label="$t('menuConfig.bz')" name="menuTip" ctype="textarea" :placeholder="$t('menuConfig.qsr')"></yu-xform-item>
            </yu-xform-group>
            <div v-if="menuFormData.upMenuId !== '-1'">
              <div v-if="menuFormType === 'details'" class="button-group">
                <yu-button v-if="checkCtrl('edit')" type="primary" @click="modifyMenu">{{ $t('menuConfig.xg') }}</yu-button>
                <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled @click="deleteMenu(true)">{{ $t('menuConfig.sc') }}</yu-button>
              </div>
              <div v-else class="button-group">
                <yu-button v-norepeat.disabled type="primary" @click="saveMenu">{{ $t('menuConfig.bc') }}</yu-button>
                <yu-button @click="resetMenuFormData">{{ $t('menuConfig.cz') }}</yu-button>
                <yu-button @click="cancelEditFormData">{{ $t('menuConfig.qx') }}</yu-button>
              </div>
            </div>
          </div>
        </yu-xform>
      </div>
    </div>
    <!--图标选择-->
    <yu-xdialog :title="$t('menuConfig.tbxz')" :center="true" :visible.sync="isShowIconDialog" size="small" height="400px">
      <div class="yu-icons">
        <a :key="index" v-for="(icon, index) in icons" href="javascript:void(0)" :class="icon.id" :title="icon.id" @click="selectIcon($event, true)"></a>
      </div>
    </yu-xdialog>
    <!--业务功能选择-->
    <yu-xdialog class="yu-menu-function-dialog" :title="xdialogTit" :visible.sync="isShowFunctionDialog" size="large" height="620px" destroy-on-close @close="functionDialogClose">
      <div class="dialog-search">
        <yu-button v-if="checkCtrl('addFunc')" @click="addFunc">{{ $t('menuConfig.xz') }}</yu-button>
        <yu-button v-if="checkCtrl('deleteFunc')" v-norepeat.disabled @click="deleteFunc(false)">{{ $t('menuConfig.sc') }}</yu-button>
        <yu-button v-if="checkCtrl('manageModel')" @click="openModelDialog">{{ $t('menuConfig.mkgl') }}</yu-button>
        <yu-xselect class="form-item" v-model="functionTableParams.modId" :options="funcModels" @change="searchFuncData" :placeholder="$t('menuConfig.qxz')"></yu-xselect>
        <yu-input class="form-item" v-model="functionTableParams.keyWord" icon="search" @keyup.enter.native="searchFuncData" :on-icon-click="searchFuncData" :placeholder="$t('menuConfig.gjz')"></yu-input>
      </div>
      <yu-xtable request-type="POST" ref="funcTable" class="function-table" selection-type="checkbox" :data-url="funcDataUrl" :base-params="functionTableParams">
        <yu-xtable-column prop="funcName" :label="$t('menuConfig.ywgnmc')" min-width="100px">
          <template slot-scope="scope">
            <a class="underline" @click="showFuncDetail(scope.row)">{{ scope.row.funcName }}</a>
          </template>
        </yu-xtable-column>
        <yu-xtable-column prop="modName" :label="$t('menuConfig.mk')" min-width="80px"></yu-xtable-column>
        <yu-xtable-column :label="$t('menuConfig.tb')" width="50px" align="center">
          <template slot-scope="scope">
            <i :class="scope.row.funcIcon"></i>
          </template>
        </yu-xtable-column>
        <yu-xtable-column prop="funcUrl" :label="$t('menuConfig.lj')" min-width="240px" :placeholder="$t('menuConfig.lr') + '：pages/test/test'"></yu-xtable-column>
        <yu-xtable-column class="handle-box" :label="$t('menuConfig.caozuo')" width="160px">
          <template slot-scope="scope">
            <yu-button-drop set-index="0" :show-length="2" type="text">
              <yu-button v-if="xdialogTit === $t('menuConfig.xzywgn')" type="text" @click="selectMenuFunc(scope.row)">{{ $t('menuConfig.select') }}</yu-button>
              <yu-button v-if="checkCtrl('editFunc')" type="text" @click="modifyFuncDetail(scope.row)">{{ $t('menuConfig.xg') }}</yu-button>
              <yu-button v-if="checkCtrl('deleteFunc')" type="text" v-norepeat.disabled @click="deleteFunc(true, scope.row)">{{ $t('menuConfig.sc') }}</yu-button>
            </yu-button-drop>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-xdialog>
    <!--业务功能详情-->
    <yu-xdialog ref="funcDetailDialog" class="func-detail-dialog" :title="funcDetailDialogTitle" :visible.sync="isShowFuncDetailDialog" size="tiny" :min-height="240" @close="funcDetailDialogClose">
      <yu-xform ref="funcForm" class="func-form" v-model="funcDetailFormData" label-width="120px" :form-type="funcDetailFormType" :rules="funcDetailFormRules">
        <yu-xform-group :column="1">
          <yu-xform-item :label="$t('menuConfig.ywgnmc')" name="funcName" :placeholder="$t('menuConfig.qsr')"></yu-xform-item>
        </yu-xform-group>
        <yu-xform-group class="select-icon-group" :column="1">
          <yu-xform-item :label="$t('menuConfig.mk')" name="modId" ctype="yu-xselect" :options="funcModels" :clearable="false" :placeholder="$t('menuConfig.qxz')"></yu-xform-item>
          <yu-button v-if="funcDetailFormType === 'edit'" plain slot="custom" class="icon yu-icon-setting3" :title="$t('menuConfig.mkgl')" @click="openModelDialog"></yu-button>
        </yu-xform-group>
        <yu-xform-group :column="1">
          <yu-xform-item :label="$t('menuConfig.lj')" name="funcUrl" :placeholder="$t('menuConfig.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('menuConfig.tb')" name="funcIcon" ctype="custom" :placeholder="$t('menuConfig.qxz')">
            <div v-if="funcDetailFormType === 'details'">
              <span class="menu-icon" v-if="funcDetailFormData.funcIcon"><i :class="funcDetailFormData.funcIcon"></i></span>
            </div>
            <div v-else>
              <span v-if="funcDetailFormData.funcIcon" class="menu-icon pointer" @click="openIconDialog(false)"><i :class="funcDetailFormData.funcIcon"></i></span>
              <span v-if="funcDetailFormData.funcIcon" class="select-icon" @click="clearfuncIcon">{{ $t('menuConfig.sc') }}</span>
              <span v-else class="select-icon" @click="openIconDialog(false)">{{ $t('menuConfig.select') }}</span>
              <span class="form-tips">{{ $t('menuConfig.menuFormTips2') }}</span>
            </div>
          </yu-xform-item>
          <yu-xform-item :label="$t('menuConfig.bz')" name="funcDesc" ctype="textarea" :placeholder="$t('menuConfig.qsr')"></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
      <div slot="footer" class="yu-grpButton">
        <div v-if="funcDetailFormType === 'details'">
          <yu-button v-if="checkCtrl('editFunc')" type="primary" @click="modifyFunc">{{ $t('menuConfig.xg') }}</yu-button>
          <yu-button v-if="checkCtrl('deleteFunc')" v-norepeat.disabled @click="deleteFunc(true)">{{ $t('menuConfig.sc') }}</yu-button>
          <yu-button @click="closeFuncDetailDialog">{{ $t('menuConfig.fh') }}</yu-button>
        </div>
        <div v-else>
          <yu-button v-if="checkCtrl('editFunc')" v-norepeat.disabled type="primary" @click="saveFunc">{{ $t('menuConfig.bc') }}</yu-button>
          <yu-button @click="closeFuncDetailDialog">{{ $t('menuConfig.qx') }}</yu-button>
        </div>
      </div>
    </yu-xdialog>
    <!--模块管理-->
    <yu-xdialog class="yu-model-manage-dialog" :title="$t('menuConfig.mkgl')" :visible.sync="isShowModelDialog" size="small">
      <div class="dialog-search">
        <yu-button v-if="checkCtrl('addModel')" @click="addModel">{{ $t('menuConfig.xz') }}</yu-button>
        <yu-button v-if="checkCtrl('deleteModel')" v-norepeat.disabled @click="deleteModel(false)">{{ $t('menuConfig.sc') }}</yu-button>
        <yu-input class="form-item" v-model="modelTableParams.modelName" icon="search" @keyup.enter.native="searchModelData" :on-icon-click="searchModelData" :placeholder="$t('menuConfig.gjz')"></yu-input>
      </div>
      <yu-xtable request-type="POST" ref="modelTable" class="function-table" selection-type="checkbox" :data="modelTableData" :pageable="false" :height="360">
        <yu-xtable-column prop="modName" :label="$t('menuConfig.mk')" :ctype="modelTableColumnCtype" min-width="100px" rules="required"></yu-xtable-column>
        <yu-xtable-column prop="modDesc" :label="$t('menuConfig.ms')" :ctype="modelTableColumnCtype" min-width="120px"></yu-xtable-column>
        <yu-xtable-column class="handle-box" :label="$t('menuConfig.caozuo')" width="160px">
          <template slot-scope="scope">
            <yu-button-drop set-index="0" :show-length="2" type="text">
              <yu-button v-if="checkCtrl('editModel')" type="text" @click="modifyModel(scope.row)">{{ $t('menuConfig.xg') }}</yu-button>
              <yu-button v-if="checkCtrl('deleteModel')" type="text" v-norepeat.disabled @click="deleteModel(true, scope.row)">{{ $t('menuConfig.sc') }}</yu-button>
            </yu-button-drop>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-xdialog>
    <!--新增/修改 模块-->
    <yu-xdialog :title="modifyModelTitle" :visible.sync="isShowModifyModelDialog" size="tiny">
      <yu-xform ref="modifyModelForm" label-width="80px" v-model="currentModel" :rules="modelFormRules">
        <yu-xform-group :column="1">
          <yu-xform-item :label="$t('menuConfig.mk')" name="modName" :placeholder="$t('menuConfig.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('menuConfig.ms')" name="modDesc" ctype="textarea" :placeholder="$t('menuConfig.qsr')"></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
      <div slot="footer" class="yu-grpButton">
        <yu-button type="primary" v-norepeat.disabled @click="saveModel">{{ $t('menuConfig.bc') }}</yu-button>
        <yu-button @click="closeModifyModelDialog">{{ $t('menuConfig.qx') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import { mapGetters } from "vuex"
import { clone, sessionStore } from '@/utils'
import { validator } from "@/utils/validate"
import { icons } from "@/config/constant/app.data.icon"
var frameSize = sessionStore.get('VIEW-SIZE')
export default {
  name: 'MenuConfig',
  data: function () {
    return {
      createCheck: this.checkCtrl('add'), // 新增按钮控制 (this.checkCtrl检查是否有控制点权限，有权限返回false，无权限返回true)
      menuTreeHeight: frameSize.height - 138,
      isManaging: false, // 是否正在管理
      isShowMenuTreeCheckbox: false, // 是否显示menuTree的复选框
      isShowIconDialog: false, // 是否显示选择图标的弹窗
      isShowFunctionDialog: false, // 是否显示业务功能的弹窗
      isShowModelDialog: false, // 是否显示模块管理的弹窗
      // isMenuTreeDraggable: this.checkCtrl('order'), // 弹窗树是否可以拖拽排序
      isMenuTreeDraggable: true,
      rightContentTitle: this.$t('menuConfig.wxz'), // 右侧内容的title
      currentCheckedNodeId: '', // 当前选中的节点id
      expandedKeys: [], // 默认展开的节点
      icons: icons,
      isSelectMenuIcon: true,
      filterText: '',
      menuTreeData: [],
      oldMenuTreeData: [],
      menuTreeProps: {
        children: 'childrenList',
        label: 'menuName'
      },
      menuFormData: {
        upMenuId: '',
        upMenuName: '',
        menuName: '',
        menuClassify: '0', // 菜单分类： 0 - 菜单， 1 - 目录
        menuIcon: '',
        menuOrder: '',
        textarea: '',
        menuTip: ''
      },
      oldMenuFormData: {},
      menuFormType: 'details',
      menuNameRule: [
        { required: true, message: this.$t('menuConfig.btx') },
        { validator: validator.speChar, message: this.$t('menuConfig.srxxbhtszf') }
      ],
      funcUrlRule: {required: true, message: this.$t('menuConfig.qsr')},
      funcDataUrl: backend.appOcaService + '/api/adminsmbusifunc/queryfunc', // 业务功能的数据查询接口
      functionTableParams: { modId: '', keyWord: '' },
      funcModels: [], // 作为下拉选择项的模块
      modelTableData: [], // 模块管理table的数据
      isShowFuncDetailDialog: false,
      funcDetailDialogTitle: this.$t('menuConfig.xq'),
      funcDetailFormType: 'details',
      funcDetailFormData: {},
      funcDetailFormRules: {
        funcName: [
          { required: true, message: this.$t('menuConfig.btx') },
          { max: 50, message: this.$t('menuConfig.srzgc') },
          { validator: validator.speChar, message: this.$t('menuConfig.srxxbhtszf') }
        ],
        modId: [
          { required: true, message: this.$t('menuConfig.btx') }
        ],
        funcUrl: [
          { required: true, message: this.$t('menuConfig.btx') }
          // { validator: this.urlValidate }
        ]
      },
      modelTableParams: {},
      modelTableColumnCtype: '',
      isShowModifyModelDialog: false, // 是否显示新增/修改模块 的弹窗
      modifyModelTitle: this.$t('menuConfig.xz'), // 新增修改模块弹窗的title
      currentModel: {}, // 当前新增/修改模块的数据
      modelFormRules: {
        modName: [
          { required: true, message: this.$t('menuConfig.btx') },
          { max: 20, message: this.$t('menuConfig.srzgc') },
          { validator: validator.speChar, message: this.$t('menuConfig.srxxbhtszf') }
        ],
        modDesc: [
          { max: 100, message: this.$t('menuConfig.srzgc') }
        ]
      },
      directoryTreeData: [], // 菜单目录树数据
      xdialogTit: this.$t('menuConfig.xzywgn')
    };
  },
  computed: {
    ...mapGetters([
      "logicSys",
      'language'
    ])
  },
  watch: {
    filterText: function (val) {
      this.$refs.menuTree.filter(val);
    },

    menuFormData: {
      deep: true,
      handler: function (val) {
        // 使用yu-combo-tree组件的filter方法过滤时 若需要过滤的节点本身下面有子节点 则无法成功过滤, 所以先注释此行代码
        // this.$refs.upMenuComboTree && this.$refs.upMenuComboTree.filter(val.menuId);
      }
    }
  },

  created: function () {
    this.getMenuTreeData();
    this.getFuncModels();
  },

  methods: {

    /**
    * 请求菜单树接口
    */
    getMenuTreeData: function () {
      var _this = this;
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/adminsmmenu/tree',
        data: { sysId: _this.logicSys.id, upMenuId: -1, treeDeep: -1 },
      }).then(({ code, message, data }) => {
        if (code === '0000') {
          _this.menuTreeData = data;
          _this.oldMenuTreeData = JSON.parse(JSON.stringify(data));
          _this.directoryTreeData = _this.getDirectoryTreeData(JSON.parse(JSON.stringify(data)));
        }
      }).catch(err => {
        console.log(err)
      })
      ;
    },
    functionDialogClose() {
      this.functionTableParams.modId = ''
      this.functionTableParams.keyWord = ''
    },

    getDirectoryTreeData: function (array) {
      var _this = this, newArray = [];
      array.forEach(function (item) {
        if (item.menuClassify === '1') {
          newArray.push(item);
          if (item.childrenList && Array.isArray(item.childrenList) && item.childrenList.length > 0) {
            item.children = _this.getDirectoryTreeData(item.childrenList);
            delete item.childrenList;
          }
        }
      });
      return newArray;
    },

    upMenuIdChange: function (val, oldVal) {
      if (this.menuFormData.upMenuId === this.menuFormData.menuId) {
        this.$message({ message: this.$t('menuConfig.sjjdbnwtzj'), type: 'success' });
        this.$nextTick(function () {
          this.menuFormData.upMenuId = '';
          this.$refs.upMenuComboTree.selectedLabel = '';
          this.$refs.upMenuComboTree.clear();
        });
        return;
      }
    },

    // 选择上级节点时 不能选择它自己
    filterUpMenuComboTreeNode: function (menuId, data) {
      return data.menuId !== menuId;
    },

    menuTreeRenderContent: function (h, obj) {
      var _this = this, data = obj.data, store = obj.store, node = obj.node;
      var btnArray = [];
      if (node.data.menuClassify === '1' && this.createCheck) { // menuClassify === '1' - 该节点菜单类型是目录, createCheck - 是否有新增的按钮权限
        btnArray.push({name: _this.$t('menuConfig.xz'),
          class: 'icon yu-icon-plus5',
          callback: function (event) {
            _this.addMenu(store, data, event);
          }
        });
      }
      return h('span', { attrs: { class: 'label-box' } }, [
        h('span', {}, [h('span', {}, node.label)]),
        h('span', { attrs: { class: 'handle-box' } }, btnArray.map(function (item) {
          return h('i', { attrs: { class: item.class, title: item.name }, on: { click: item.callback } });
        }))
      ]);
    },

    filterMenuTreeNode: function (value, data) {
      if (!value) {
        return true;
      }
      return data.menuName.indexOf(value) !== -1;
    },

    addMenu: function (store, menuData, e) {
      var _this = this;
      // 如果当前表单为编辑模式 且 已改动过部分字段数据
      if (this.menuFormType === 'edit' && this.isMenuFormDataChanged()) {
        this.$confirm(this.$t('menuConfig.confirmText1'), this.$t('menuConfig.ts'), {
          type: 'warning',
          callback: function (action) {
            if (action === 'confirm') {
              _this.confirmAddMenu(menuData);
            }
          }
        });
      } else {
        _this.confirmAddMenu(menuData);
      }
      e.stopPropagation();
    },

    // menuFormData 是否已经改变
    isMenuFormDataChanged: function () {
      var _this = this;
      var isChanged = false;
      for (var key in _this.menuFormData) {
        if (_this.menuFormData[key] !== _this.oldMenuFormData[key]) {
          isChanged = true;
        }
      }
      return isChanged;
    },

    confirmAddMenu: function (menuData, isTopAdd) { // menuData - 菜单数据, isTopAdd - 为true表示是点击顶部的图标添加菜单
      var currentNode = this.$refs.menuTree.getCurrentNode();
      if (isTopAdd) {
        // this.buttonLogs(null, this.$t('menuConfig.xz'));
        if (currentNode && currentNode.menuClassify === '1') {
          menuData = currentNode;
        }
      }
      this.rightContentTitle = this.$t('menuConfig.xz');
      this.menuFormType = 'edit';
      
      var menuFormData = {
        upMenuId: menuData.menuId,
        upMenuName: menuData.menuName,
        menuName: '',
        menuClassify: '0',
        menuIcon: '',
        menuOrder: '',
        textarea: '',
        menuTip: ''
      };
      this.$refs.menuForm.setFormData(menuFormData);
      
      this.oldMenuFormData = clone(this.menuFormData, {});
    },

    menuClassifyChange: function () {
      // 业务功能是选择的, 层级目录是输入的, 切换后需要重新输入或选择
      this.menuFormData.menuName = '';
    },

    menuTreeNodeClick: function (nodeData, node, self) {
      var _this = this;
      // 如果当前表单为编辑模式 且 已改动过部分字段数据
      if (this.menuFormType === 'edit' && this.isMenuFormDataChanged()) {
        this.$confirm(this.$t('menuConfig.confirmText1'), this.$t('menuConfig.ts'), {
          type: 'warning',
          callback: function (action) {
            if (action === 'confirm') {
              _this.showNodeData(nodeData);
            }
          }
        });
      } else {
        _this.showNodeData(nodeData);
      }
    },

    showNodeData: function (nodeData) { // 显示节点菜单数据
      var childrenLength = nodeData.childrenList ? nodeData.childrenList.length : 0;
      
      nodeData.menuId && (this.expandedKeys = [nodeData.menuId]);
      this.rightContentTitle = this.$t('menuConfig.xq');
      this.menuFormType = 'details';
      this.getMenuFormData(childrenLength, nodeData.menuId);
      this.$refs.menuForm && this.$refs.menuForm.clearValidate();
    },

    getMenuFormData: function (childrenLength, menuId) {
      var _this = this;
      _this.$request({
        method: 'POST',
        data: { menuId: menuId},
        url: backend.appOcaService + '/api/adminsmmenu/menuinfoquery',
      }).then(({ code, message, data }) => {
        if (code === '0000' && data) {
          data.childrenLength = childrenLength;
          // clone(data, _this.menuFormData);
          _this.$refs.menuForm.setFormData(data);
          _this.oldMenuFormData = clone(_this.menuFormData, {});
          _this.currentCheckedNodeId = menuId;
        }
      });
    },

    manageMenu: function () {
      this.isManaging = true;
      this.isShowMenuTreeCheckbox = true;
      // this.buttonLogs(null, this.$t('menuConfig.gl'));
    },

    cancelManage: function () {
      this.isManaging = false;
      this.isShowMenuTreeCheckbox = false;
      // this.buttonLogs(null, this.$t('menuConfig.qx'));
    },

    modifyMenu: function () {
      this.menuFormType = 'edit';
      this.rightContentTitle = this.$t('menuConfig.xg');
    },

    saveMenu: function () {
      var _this = this, validate = false;
      this.$refs.menuForm.validate(function (valid) {
        validate = valid;
      });
      if (validate) {
        var url = backend.appOcaService + '/api/adminsmmenu/editmenu';
        if (!this.menuFormData.menuId) { // 不存在menuId 就是新增
          url = backend.appOcaService + '/api/adminsmmenu/createmenu';
          this.menuFormData.sysId = this.logicSys.id;
          this.menuFormData.upMenuId = this.menuFormData.upMenuId || '0';
        }
        if (this.menuFormData.menuClassify === '1') { // 如果是层级目录 则需要把funcId清空
          this.menuFormData.funcId = null;
        }
        _this.$request({
          method: 'POST',
          url: url,
          data: this.menuFormData,
        }).then(({ code, message, data }) => {
          if (code === '0000') {
            var nodeData = clone(_this.menuFormData, {});
            !_this.menuFormData.menuId && (nodeData.menuId = data && data.menuId);
            _this.$message({ message: _this.$t('menuConfig.sjbccg'), type: 'success' });
            _this.rightContentTitle = _this.$t('menuConfig.xq');
            _this.menuFormType = 'details';
            _this.oldMenuFormData = clone(_this.menuFormData, {});
            _this.getMenuTreeData();
            _this.menuTreeNodeClick(nodeData);
            _this.$store.dispatch('oauth/getAccessInfo')
          } else {
            _this.$message({ message: message, type: 'error' });
          }
        });
      }
    },
    deleteMenu(isDeleteMenuFormData) { // isDeleteMenuFormData为true 表示删除menuForm展示的数据的菜单, 否则是批量删除checkbox选中的菜单
      var _this = this;
      // this.buttonLogs(null, this.$t('menuConfig.sc'));
      if (isDeleteMenuFormData) {
        this.$refs.menuTree.setChecked(this.menuFormData, true, true);
      }
      // var checkedNodes = isDeleteMenuFormData ? [this.menuFormData] : this.$refs.menuTree.getCheckedNodes();
      var checkedNodes = this.$refs.menuTree.getCheckedNodes();

      if (checkedNodes.length < 1) {
        this.$message({ message: this.$t('menuConfig.qxxzyscdsj '), type: 'warning' });
        return;
      }
      var menuIds = checkedNodes.map(function (item) {
        return item.menuId;
      });
      this.$confirm(this.$t('menuConfig.confirmText2'), this.$t('menuConfig.ts'), {
        type: 'warning',
        confirmButtonText: _this.$t('menuConfig.qr'),
        cancelButtonText: _this.$t('menuConfig.qx'),
        callback: function (action) {
          if (action === 'confirm') {
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmmenu/deletemenu',
              data: menuIds,
            }).then(({ code, message, data }) => {
              if (code === '0000') {
                _this.$message({ message: _this.$t('menuConfig.sccg'), type: 'success' });
                _this.getMenuTreeData();
                _this.currentCheckedNodeId = '';
                _this.$store.dispatch('oauth/getAccessInfo')
              } else {
                _this.$message({ message: message, type: 'error' });
              }
            });
          }
        }
      });
    },

    resetMenuFormData: function () {
      clone(this.oldMenuFormData, this.menuFormData);
      this.$refs.menuForm.clearValidate()
    },

    cancelEditFormData: function () {
      this.$refs.menuForm && this.$refs.menuForm.clearValidate();
      if (!this.menuFormData.menuId) {
        this.currentCheckedNodeId = '';
        this.rightContentTitle = this.$t('menuConfig.wxz');
        this.menuTreeData = JSON.parse(JSON.stringify(this.oldMenuTreeData));
      } else {
        this.rightContentTitle = this.$t('menuConfig.xq');
        // this.menuFormData = clone(this.oldMenuFormData, {});
        this.$refs.menuForm.setFormData(this.oldMenuFormData);
        // this.menuFormType = 'details';
      }
      this.menuFormType = 'details';
    },


    openFunctionDialog: function (title) {
      this.isShowFunctionDialog = true;
      this.$nextTick(function() {
        this.xdialogTit = this.$t(title);
      })
    },

    openIconDialog: function (isSelectMenuIcon) {
      this.isShowIconDialog = true;
      this.isSelectMenuIcon = isSelectMenuIcon;
    },

    selectIcon: function (event) {
      var iconName = event.target.className;
      if (this.isSelectMenuIcon) {
        this.menuFormData.menuIcon = iconName;
      } else {
        this.funcDetailFormData.funcIcon = iconName;
      }
      this.isShowIconDialog = false;
    },

    clearMenuIcon: function () {
      this.menuFormData.menuIcon = '';
    },

    selectMenuFunc: function (funcData) {
      this.menuFormData.funcId = funcData.funcId;
      this.menuFormData.funcUrl = funcData.funcUrl;
      this.menuFormData.funcName = funcData.funcName;
      this.menuFormData.menuName = funcData.funcName;
      this.menuFormData.menuIcon = funcData.funcIcon || this.menuFormData.menuIcon;
      // clone(this.menuFormData, funcData);
      this.isShowFunctionDialog = false;
    },

    handleDragEnd: function (draggingNode, dropNode, dropType, event) { // dropType 可能为 before、after、inner
      if (!draggingNode || !dropNode) {
        return;
      }
      if (dropType === 'inner' || draggingNode.data.upMenuId !== dropNode.data.upMenuId) { // 目前仅允许同一父节点下的同级子节点之间拖拽
        this.$message({ message: this.$t('menuConfig.tzgzts') });
        this.menuTreeData = JSON.parse(JSON.stringify(this.oldMenuTreeData));
      } else {
        this.saveMenuOrder({ dragMenuId: draggingNode.data.menuId, refMenuId: dropNode.data.menuId, menuOrder: dropType });
      }
    },

    saveMenuOrder: function (params) {
      var _this = this;
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/adminsmmenu/update/specificmenutree',
        data: params,
      }).then(({ code, message, data }) => {
        if (code === '0000') {
          _this.$message({ message: _this.$t('menuConfig.sjbccg'), type: 'success' });
          _this.getMenuTreeData();
        } else {
          _this.$message({ message: _this.$t('menuConfig.sjbcsb'), type: 'error' });
        }
      });
    },

    getFuncModels: function (isSearch) {
      var _this = this;
      var params = clone(this.modelTableParams, { page: 1, size: 1000 });
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/adminsmfuncmod/querymod',
        data: params,
      }).then(({ code, message, data }) => {
        if (code === '0000') {
          _this.modelTableData = data.map(function (item) {
            item.key = item.modId;
            item.value = item.modName;
            return item;
          });
          if (!isSearch) {
            _this.funcModels = JSON.parse(JSON.stringify(_this.modelTableData));
          }
          _this.$refs.modelTable && _this.$refs.modelTable.clearSelection();
        }
      });
    },

    searchFuncData: function () {
      this.$refs.funcTable.remoteData();
      this.buttonLogs(null, this.$t('menuConfig.ywgncx'));
    },

    showFuncDetail: function (funcDetail) {
      var _this = this;
      this.isShowFuncDetailDialog = true;
      this.funcDetailFormType = 'details';
      this.funcDetailDialogTitle = this.$t('menuConfig.xq');
      this.$nextTick(function () {
        this.$set(_this.funcDetailFormData, 'funcIcon', funcDetail.funcIcon);
        clone(funcDetail, _this.funcDetailFormData);
        this.$refs.funcForm.clearValidate();
      });
    },

    // 新增业务功能
    addFunc: function () {
      this.isShowFuncDetailDialog = true;
      this.funcDetailFormType = 'edit';
      this.funcDetailDialogTitle = this.$t('menuConfig.xz');
      // var funcDetailFormData = {
      //   funcName: '',
      //   funcUrl: '',
      //   modId: ''
      // };
      this.$nextTick(function () {
        this.$refs.funcForm.resetFields();
        // this.$refs.funcForm.setFormData(funcDetailFormData);
      });
    },

    // 修改业务功能 (点击funcTable的修改)
    modifyFuncDetail: function (funcDetail) {
      // this.buttonLogs(null, this.$t('menuConfig.xg'));
      this.showFuncDetail(funcDetail);
      this.modifyFunc();
    },

    // 修改业务功能 (点击funcForm中的修改)
    modifyFunc: function () {
      this.funcDetailFormType = 'edit';
      this.funcDetailDialogTitle = this.$t('menuConfig.xg');
    },

    // 删除业务功能
    deleteFunc: function (isSingleHandle, funcDetail) {
      // this.buttonLogs(null, this.$t('menuConfig.sc'));
      // isSingleHandle为true 表示删除funcDetailForm展示的数据的业务功能 或 点击table操作区域的删除, 否则是批量删除checkbox选中的业务功能
      funcDetail = funcDetail || this.funcDetailFormData;
      var selections = isSingleHandle ? [funcDetail] : this.$refs.funcTable.selections;
      if (selections.length < 1) {
        this.$message({ message: this.$t('menuConfig.qxxzyscdsj '), type: 'warning' });
        return;
      }
      this.confirmDeleteFunc(selections);
    },


    confirmDeleteFunc: function (selections) {
      var _this = this;
      var funcIds = selections.map(function (item) {
        return item.funcId;
      });
      this.$confirm(this.$t('menuConfig.qrscgsjm'), this.$t('menuConfig.ts'), {
        type: 'warning',
        confirmButtonText: _this.$t('menuConfig.qr'),
        cancelButtonText: _this.$t('menuConfig.qx'),
        callback: function (action) {
          if (action === 'confirm') {
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmbusifunc/deletefunc',
              data: funcIds,
            }).then(({ code, message, data }) => {
              if (code === '0000') {
                _this.$message({ message: _this.$t('menuConfig.sccg'), type: 'success' });
                _this.$refs.funcTable.remoteData();
                _this.isShowFuncDetailDialog = false;
              } else {
                _this.$message({ message: message, type: 'error' });
              }
            });
          }
        }
      });
    },

    urlValidate: function (rule, value, callback) {
      var urlReg = /^pages\/([a-zA-Z0-9_]+\/)+[a-zA-Z0-9_]+$/;
      if (!urlReg.test(value)) {
        callback(new Error(this.$t('menuConfig.qsrhfgs') + '，' + this.$t('menuConfig.lr') + '：pages/test/test'));
        return;
      }
      callback();
    },

    // 保存业务功能
    saveFunc: function () {
      var _this = this, validate = false;
      this.$refs.funcForm.validate(function (valid) {
        validate = valid;
      });
      if (validate) {
        // 存在funcId是修改, 否则是新增
        var url = this.funcDetailFormData.funcId ? backend.appOcaService + '/api/adminsmbusifunc/editfunc' : backend.appOcaService + '/api/adminsmbusifunc/createfunc';
        _this.$request({
          method: 'POST',
          url: url,
          data: this.funcDetailFormData,
        }).then(({ code, message, data }) => {
          if (code === '0000') {
            _this.$message({ message: _this.$t('menuConfig.sjbccg'), type: 'success' });
            _this.isShowFuncDetailDialog = false;
            _this.$refs.funcTable.remoteData();
          } else {
            _this.$message({ message: message, type: 'error' });
          }
        });
      }
    },

    clearfuncIcon: function () {
      this.funcDetailFormData.funcIcon = '';
    },

    closeFuncDetailDialog: function () {
      this.$refs.funcForm.resetFields()
      this.isShowFuncDetailDialog = false;
    },
    funcDetailDialogClose() {
      this.$refs.funcForm.resetFields();
      // this.$refs.funcForm.clearValidate();
      // console.log('清除======', this.funcDetailFormData);
      // this.funcDetailFormData = {}
      // this.$refs.funcForm.resetFields();
      // this.$refs.funcForm.clearValidate();
    },
    openModelDialog: function () {
      this.isShowModelDialog = true;
      this.$nextTick(function () {
        this.$refs.modelTable.clearSelection();
      });
    },

    searchModelData: function () {
      this.getFuncModels(true);
    },

    // 添加模块
    addModel: function () {
      var model = {
        modName: '',
        modDesc: ''
      };
      this.modifyModel(model);
    },

    modifyModel: function (model) {
      this.isShowModifyModelDialog = true;
      this.modifyModelTitle = model.modId ? this.$t('menuConfig.xg') : this.$t('menuConfig.xz');
      this.$nextTick(function () {
        this.$refs.modifyModelForm.resetFields();
        this.$refs.modifyModelForm.setFormData(model);
      });
    },

    saveModel: function () {
      var _this = this, validate = false;
      this.$refs.modifyModelForm.validate(function (valid) {
        validate = valid;
      });
      if (validate) {
        // 存在modId就是修改, 否则就是新增
        var url = this.currentModel.modId ? backend.appOcaService + '/api/adminsmfuncmod/editmod' : backend.appOcaService + '/api/adminsmfuncmod/createmod';
        _this.$request({
          method: 'POST',
          url: url,
          data: this.currentModel,
        }).then(({ code, message, data }) => {
          if (code === '0000') {
            _this.$message({ message: _this.$t('menuConfig.sjbccg'), type: 'success' });
            _this.getFuncModels();
            _this.isShowModifyModelDialog = false;
          } else {
            _this.$message({ message: message, type: 'error' });
          }
        });
      }
    },

    // 删除模块
    deleteModel: function (isSingleHandle, model) {
      // isSingleHandle为true 表示点击table操作区域的删除, 否则是批量删除checkbox选中的业务功能
      var selections = isSingleHandle ? [model] : this.$refs.modelTable.selections;
      if (selections.length < 1) {
        this.$message({ message: this.$t('menuConfig.qxxzyscdsj'), type: 'warning' });
        return;
      }
      this.confirmDeleteModel(selections);
    },


    confirmDeleteModel: function (selections) {
      var _this = this;
      var modIds = selections.map(function (item) {
        return item.modId;
      });
      this.$confirm(this.$t('menuConfig.qrscgsjm'), this.$t('menuConfig.ts'), {
        type: 'warning',
        confirmButtonText: _this.$t('menuConfig.qr'),
        cancelButtonText: _this.$t('menuConfig.qx'),
        callback: function (action) {
          if (action === 'confirm') {
            _this.$request({
              method: 'POST',
              url: backend.appOcaService + '/api/adminsmfuncmod/deletemod',
              data: modIds,
            }).then(({ code, message, data }) => {
              if (code === '0000') {
                _this.$message({ message: _this.$t('menuConfig.sccg'), type: 'success' });
                _this.getFuncModels();
                _this.isShowModifyModelDialog = false;
              } else {
                _this.$message({ message: message, type: 'error' });
              }
            });
          }
        }
      });
    },

    closeModifyModelDialog: function () {
      this.isShowModifyModelDialog = false;
      this.$refs.modifyModelForm.resetFields();
    }
  }
}
</script>
<style>
.yu-menu-config {
  height: 100%;
  font-size: 14px;
  box-shadow: none !important;
  background: #f2f2f2 !important;
}
.yu-menu-config .no-choose {
  text-align: center;
  font-size: 14px;
  width: 100%;
  position: absolute;
  top: 36%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}

.yu-menu-config .no-choose > img {
  float: none;
  width: 25%;
  margin-bottom: 16px;
}
.yu-menu-config .left-content {
  float: left;
  width: 400px;
  margin-right: 24px;
  background: #fff;
  overflow: hidden;
}
.yu-menu-config .right-content {
  float: left;
  width: calc(100% - 424px);
  background: #fff;
}
.yu-menu-config .content-box {
  padding-top: 24px;
}
.yu-menu-config .left-content .content-box {
  height: calc(100% - 66px);
}
.yu-menu-config .right-content .content-box {
  padding-right: 24px;
}

/* 紧凑模式start */
.compact .yu-menu-config .left-content {
  margin-right: 16px;
}
.compact .yu-menu-config .right-content {
  width: calc(100% - 416px);
}
.compact .yu-menu-config .content-box {
  padding-top: 16px;
}
.compact .yu-menu-config .left-content .content-box {
  height: calc(100% - 52px);
}
.compact .yu-menu-config .right-content .content-box {
  padding-right: 16px;
}
/* 紧凑模式end */
.yu-menu-config .filter-text {
  float: left;
  width: 200px;
}
.yu-menu-config .handle-box {
  float: left;
  margin-left: 10px;
}
.yu-menu-config .content-title {
  height: 42px;
  line-height: 42px;
  padding: 0 20px;
  font-weight: bold;
  font-size: 16px;
  color: #444;
  box-sizing: border-box;
  border-bottom: 1px solid #ededed;
}
.compact .yu-menu-config .content-title {
  font-size: 14px;
  padding: 0 16px;
  height: 36px;
  line-height: 36px;
}
.yu-menu-config .yu-base-toolbar {
  padding-left: 24px;
  padding-right: 24px;
  text-align: center;
}
.yu-menu-config .yu-base-toolbar .icon {
  margin: 0 0 0 10px;
}
.yu-menu-config .yu-base-toolbar .handle-box .el-button,
.func-detail-dialog .el-button.icon {
  width: 30px;
  height: 30px;
  padding: 0;
}
.compact .yu-menu-config .handle-box .el-button,
.compact .func-detail-dialog .el-button.icon {
  width: 28px;
  height: 28px;
  padding: 0;
}
.compact .yu-menu-config .yu-base-toolbar {
  padding-left: 16px;
  padding-right: 16px;
}

.yu-menu-config .menu-tree {
  overflow-y: auto;
  border: none;
  padding: 0 24px;
  height: calc(100% - 54px);
}
.yu-menu-config .menu-tree .el-tree-node .handle-box {
  position: absolute;
  right: 24px;
  opacity: 0;
  color: #666;
  transition: all 0.15s;
}
.compact .yu-menu-config .menu-tree {
  padding: 0 16px;
  height: calc(100% - 44px);
}
.compact .yu-menu-config .menu-tree .el-tree-node .handle-box {
  right: 16px;
}
.blue .yu-menu-config .menu-tree .el-tree-node .handle-box .icon:hover {
  color: #2877ff;
}
.orange .yu-menu-config .menu-tree .el-tree-node .handle-box .icon:hover {
  color: #fb8d12;
}
.purple .yu-menu-config .menu-tree .el-tree-node .handle-box .icon:hover {
  color: #5557b9;
}
.yu-menu-config
  .menu-tree
  .el-tree-node
  .el-tree-node__content:hover
  > .label-box
  .handle-box {
  opacity: 1;
}
.yu-menu-config .menu-form .el-popover {
  right: 0;
  left: 0 !important;
}
.yu-menu-config .menu-form .form-tips,
.func-detail-dialog .func-form .form-tips {
  color: #999;
  font-size: 13px;
  line-height: 18px;
}
.compact .yu-menu-config .menu-form .form-tips,
.func-detail-dialog .func-form .form-tips {
  font-size: 12px;
  line-height: 13px;
}
.yu-menu-config .menu-form .button-group {
  margin: 22px 0 0 170px;
  font-size: 0;
}
.yu-menu-config .menu-form .menu-icon {
  vertical-align: middle;
}
.yu-menu-config .menu-form .menu-icon::before,
.func-detail-dialog .func-form .menu-icon::before {
  font-size: 18px;
  /*font-family: 'icomoon' !important;*/
}
.yu-menu-config .menu-form .menu-icon.pointer,
.func-detail-dialog .func-form .menu-icon.pointer {
  cursor: pointer;
  transition: 0.2s;
  vertical-align: middle;
}
.blue .yu-menu-config .menu-form .menu-icon.pointer:hover,
.blue .func-detail-dialog .func-form .menu-icon.pointer:hover {
  color: #2877ff;
}
.orange .yu-menu-config .menu-form .menu-icon.pointer:hover,
.orange .func-detail-dialog .func-form .menu-icon.pointer:hover {
  color: #fb8d12;
}
.purple .yu-menu-config .menu-form .menu-icon.pointer:hover,
.purple .func-detail-dialog .func-form .menu-icon.pointer:hover {
  color: #5557b9;
}
.yu-menu-config .menu-form .menu-order {
  display: inline-block;
  margin-right: 10px;
}
.yu-menu-config .menu-form .select-icon,
.func-detail-dialog .func-form .select-icon {
  display: inline-block;
  margin: 0 8px;
  color: #2877ff;
  cursor: pointer;
}
.yu-menu-config .menu-form .el-col-24:last-of-type .el-form-item {
  margin-bottom: 0;
}
.yu-menu-function-dialog .dialog-search {
  overflow: hidden;
}

.yu-menu-function-dialog .function-table .cell .el-button ~ .el-button {
  /**margin-left: 0;**/
}
.func-detail-dialog .el-button.icon {
  width: 36px;
  height: 36px;
  line-height: 36px;
  margin-left: 10px;
}
.compact .func-detail-dialog .el-button.icon {
  width: 28px;
  height: 28px;
  line-height: 26px;
}
.func-detail-dialog .select-icon-group .el-col-24 {
  width: calc(100% - 46px);
}
.compact .func-detail-dialog .select-icon-group .el-col-24 {
  width: calc(100% - 38px);
}
.yu-model-manage-dialog .el-table .cell .el-button ~ .el-button {
  margin-left: 15px;
}
.func-detail-dialog .yu-xform.el-form-details .el-form-item__content {
  word-break: break-all;
}
.content-title .el-button{
  float: right;
  padding: 13px 0px;
}
.compact .content-title .el-button{
  padding: 11px 0px;
}
.compact .yu-menu-config .content-title {
  padding: 0 16px;
}
.compact .yu-base-toolbar {
  padding-bottom: 16px !important;
}
/* .compact .yu-menu-config .menu-form .form-tips{
    margin-bottom: 16px;
  } */
.compact .yu-menu-config .menu-form .button-group {
  margin-top: 16px;
}
</style>
