<template>
  <div class="authorize-manage clear">
    <!-- 左侧角色或用户选择 -->
    <yu-auth-list @chooseUserRoleId="getUserRoleId" @chooseAuthObjId="getAuthObjId"></yu-auth-list>
    <!-- 右侧权限树选择 -->
    <div class="tree-box">
      <div class="tree-header" v-if="showTree">
        <yu-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">{{ $t('roleDataPowerManager.allcheck') }}</yu-checkbox>
        <div class="handle-btn">
          <yu-button v-if="checkCtrl('save')" type="primary" v-norepeat.disabled="saveDisabled" @click="saveFn">{{ $t('roleDataPowerManager.bc') }}
          </yu-button>
          <yu-button v-if="checkCtrl('copy')" v-norepeat.disabled @click="copyAuthFn">{{ $t('roleDataPowerManager.fzsq') }}</yu-button>
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
      <div class="tree-content" v-if="showTree">
        <!-- <yu-xtree :data-url="treeUrl" request-type="POST" :data-params="treeParams" data-id="authresId" node-key="authresId" data-label="nodeName" data-pid="upTreeId"
      :filter-node-method="filterNodeFn" :render-content="renderContent" ref="treeRef" show-checkbox @check-change="checkChange" @load-all-data="loadDataFn"></yu-xtree> -->
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
      <div class="no-choose" v-if="!showTree">
        <img src="@/assets/common/images/no-data.svg">
        <p>{{ $t('roleDataPowerManager.nochoosetext') }}</p>
      </div>
    </div>

    <!-- 粘贴授权dialog -->
    <yu-xdialog :title="$t('roleDataPowerManager.ztsq')" :visible.sync="pasteDialogVisible" height="540px">
      <div class="dialog-search">
        <yu-input
          v-model="tableKeyword"
          :placeholder="$t('roleDataPowerManager.gjz')"
          suffix-icon="yu-icon-search1"
          class="form-item"
          @suffix-click="tableSearchFn"
          @keyup.enter.native="tableSearchFn"
          maxlength="32"
          @clear="tableSearchFn"
          clearable></yu-input>
      </div>
      <yu-xtable request-type="POST" v-if="isRoleTab" ref="rolesTable" row-number :data-url="pasteRoleUrl" :base-params="pasteRoleParam" selection-type="checkbox" :default-load="false" key="rolesTable">
        <yu-xtable-column prop="roleName" :label="$t('roleDataPowerManager.jsmc')"></yu-xtable-column>
        <yu-xtable-column prop="roleCode" :label="$t('roleDataPowerManager.jsdmmc')"></yu-xtable-column>
        <yu-xtable-column prop="orgName" :label="$t('roleDataPowerManager.ssjg')"></yu-xtable-column>
      </yu-xtable>
      <yu-xtable request-type="POST" v-else ref="usersTable" row-number :data-url="pasteUserUrl" :base-params="pasteUserParam" selection-type="checkbox" :default-load="false" key="usersTable">
        <yu-xtable-column prop="userName" :label="$t('roleDataPowerManager.yhmc')"></yu-xtable-column>
        <yu-xtable-column prop="loginCode" :label="$t('roleDataPowerManager.yhzh')"></yu-xtable-column>
        <yu-xtable-column prop="orgName" :label="$t('roleDataPowerManager.ssjg')"></yu-xtable-column>
      </yu-xtable>
      <div slot="footer" class="dialog-footer">
        <yu-button type="primary" v-norepeat.disabled="pasteDisabled" @click="pasteAuthFn">{{ $t('roleDataPowerManager.bc') }}</yu-button>
        <yu-button @click="pasteDialogVisible = false">{{ $t('roleDataPowerManager.qx') }}</yu-button>
      </div>
    </yu-xdialog>

  </div>
</template>
<script>
import { mapGetters } from "vuex"
import { clone, extend } from '@/utils'
import { checkBelongToChooseNode } from '@/utils/util'
import YuAuthList from '@/components/widgets/YuAuthList';
export default {
  components: { YuAuthList },
  data: function () {
    return {
      activeName: "R", // 当前被激活的页签
      chooseRoleId: '', // 已选择的角色/用户id
      isRoleTab: true, // 当前页签是否是角色授权
      copyObj: {}, // 复制授权对象
      isIndeterminate: false, // 全选/半选状态
      checkAll: false, // 是否全选
      initCheckedList: [], // 权限初始化选中的数组
      childCheckedList: [], // 权限初始化最后一级选中的数组
      treeSearchVal: '', // 树的搜索值
      showTree: false, // 是否显示树
      treeUrl: backend.appOcaService + '/api/authorization/treeQuery', // 权限树Url
      pasteRoleUrl: backend.appOcaService + '/api/adminsmrole/waitpasterolepage',
      pasteUserUrl: backend.appOcaService + '/api/adminsmuser/waitpasteuserpage',
      treeParams: {
        authObjId: '',
        userRoleId: ''
      },
      treeData: [],
      pasteDialogVisible: false, // 粘贴授权弹框是否显示
      pasteRoleParam: {expectedRoleId: ''},
      pasteUserParam: {expectedUserId: ''},
      expectedRoleId: '',
      expectedUserId: '',
      isCopeSave: false,
      tableKeyword: '', // 粘贴弹框关键字
      saveDisabled: { show: false }, // 保存按钮防重复提交
      pasteDisabled: { show: false } // 粘贴按钮防重复提交
    };
  },
  computed: {
    ...mapGetters(["roles"]),
  },
  methods: {
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
          authObjId: _this.chooseRoleId,
          userRoleId: this.roles.length && this.roles[0].id
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
        * 点击搜索框模糊查询树结构
        */
    searchTreeFn: function () {
      this.$refs.treeRef.filter(this.treeSearchVal);
    },

    /**
    * 对树节点进行筛选时执行的方法，返回 true 表示这个节点可以显示，返回 false 则表示这个节点会被隐藏
    * @param value 当前输入信息
    * @param nodeData 当前节点属性信息
    * @param node 当前节点信息
    */
    filterNodeFn: function (value, nodeData, node) {
      if (!value) {
        return true;
      }
      if (nodeData.nodeName.indexOf(value) !== -1) {
        return true;
      }
      return checkBelongToChooseNode(value, node, 'nodeName');
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
    checkChange: function () {
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
        * 重置树
        */
    resetTreeFn: function () {
      this.$refs.treeRef.setCheckedKeys([]);
    },

    /**
        * 获取树变化的值
        */
    getChangeNodes: function () {
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
        * 保存授权
        */
    saveFn: function () {
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
          authObjType: _this.activeName,
          authObjId: _this.chooseRoleId,
          authFormList: authFormList
        }
      }).then(({code, message, data}) => {
        _this.saveDisabled.show = false;
        _this.saveDisabled = clone(_this.saveDisabled, {});
        if (code === '0000') {
          // var checkNodes = _this.$refs.treeRef.getCheckedNodes();
          // var checkHalfNodes = _this.$refs.treeRef.getHalfCheckedNodes();
          // _this.initCheckedList = extend([], checkNodes.concat(checkHalfNodes));
          _this.$message({type: 'success', message: _this.$t('roleDataPowerManager.bccg') });
          _this.getTreeData();
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      });
    },

    /**
        * 返回是否包含某个项
        * obj比较的对象，arr被比较的数组
        */
    isInclude: function (obj, arr) {
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
    renderContent: function (h, obj) {
      var nodeData = obj.data;
      return h('span', {attrs: {class: 'custom-tree-label'}}, [
        h('span', {attrs: {class: 'yu-treeMenuType' + nodeData.authresType}}, nodeData.authresType),
        h('span', {}, [h('span', {}, nodeData.nodeName)])
      ]);
    },

    /**
        * 数据成功请求且处理完触发的事件
        * 共两个参数，依次为：orginalData（请求的原始数据），res（请求响应的内容）
        */
    loadDataFn: function (orginalData, res) {
      this.initCheckedList = [];
      this.childCheckedList = [];
      this.getChildChecked(orginalData);
      this.$nextTick(function () {
        this.$refs.treeRef.setCheckedKeys(this.childCheckedList);
      });
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

    /**
        * 复制授权
        */
    copyAuthFn: function () {
      var _this = this;
      var checkNodes = _this.$refs.treeRef.getCheckedNodes();
      var changeArr = _this.getChangeNodes();
      if (!checkNodes.length) {
        _this.$message({
          message: _this.$t('roleDataPowerManager.bnfzqx'),
          type: 'warning'
        });
        return;
      }
      // 复制的权限有勾选但没保存时需要用户选择复制并保存还是仅保存原有数据
      if (changeArr.length) {
        _this.$customConfirm(_this.$t('roleDataPowerManager.sfbcxghdsq'), _this.$t('roleDataPowerManager.ts'), {
          buttons: [{text: _this.$t('roleDataPowerManager.bcbfz'), type: 'primary', key: 'btn1'}, {text: _this.$t('roleDataPowerManager.fzyysj'), key: 'btn2'}],
          type: 'warning',
          callback: function (action) {
            if (action === 'btn1') {
              _this.$request({
                method: 'POST',
                url: backend.appOcaService + '/api/authorization/saveAuth',
                data: {
                  authObjType: _this.activeName,
                  authObjId: _this.chooseRoleId,
                  authFormList: changeArr
                }
              }).then(({code, message, data}) => {
                if (code === '0000') {
                  var checkNodes = _this.$refs.treeRef.getCheckedNodes();
                  var checkHalfNodes = _this.$refs.treeRef.getHalfCheckedNodes();
                  _this.initCheckedList = extend([], checkNodes.concat(checkHalfNodes));
                  // _this.$message({type: 'success', message: _this.$t('roleDataPowerManager.bccg') });
                  _this.showPasteTable();
                } else {
                  _this.$message({ message: message, type: 'error' });
                }
              });
            } else if (action === 'btn2') {
              _this.showPasteTable();
            }
          }
        });
      } else {
        _this.showPasteTable();
      }
    },

    /**
        * 显示粘贴授权表格
        */
    showPasteTable: function () {
      if(this.isRoleTab && !this.expectedRoleId) {
        this.$message({ message: '请先选择一个角色！', type: 'warning' });
        return;
      }
      if(!this.isRoleTab && !this.expectedUserId) {
        this.$message({ message: '请先选择一个用户！', type: 'warning' });
        return;
      }
      this.pasteDialogVisible = true;
      this.$nextTick(function () {
        this.pasteRoleParam.keyWord = '';
        this.pasteUserParam.keyWord = '';
        this.pasteRoleParam.expectedRoleId = this.expectedRoleId;
        this.pasteUserParam.expectedUserId = this.expectedUserId;
        this.isRoleTab && this.$refs.rolesTable.remoteData(this.pasteRoleParam);
        !this.isRoleTab && this.$refs.usersTable.remoteData(this.pasteUserParam);
      });
    },

    /**
        * 搜索粘贴授权表格
        */
    tableSearchFn: function () {
      console.log(111);
      this.isRoleTab && this.$refs.rolesTable.remoteData({keyWord: this.tableKeyword});
      !this.isRoleTab && this.$refs.usersTable.remoteData({keyWord: this.tableKeyword});
    },

    /**
        * 粘贴授权
        */
    pasteAuthFn: function () {
      var _this = this;
      var copyForAuthObj = [];
      var copyForAuthObjIds = [];
      // 判断被粘贴的用户或角色，并获取他们的ids
      var selections = _this.isRoleTab ? _this.$refs.rolesTable.selections : _this.$refs.usersTable.selections;
      if (!selections.length) {
        this.$message({ message: _this.$t('roleDataPowerManager.qxxzytdxjxsq'), type: 'warning' });
        return;
      }
      for (var i = 0; i < selections.length; i++) {
        var id = _this.isRoleTab ? selections[i].roleId : selections[i].userId;
        copyForAuthObjIds.push(id);
      }
      // 被复制的权限对象
      _this.initCheckedList.forEach(function (item) {
        copyForAuthObj.push({
          authRecoId: item.authRecoId,
          authresId: item.authresId,
          state: 1,
          upTreeId: item.upTreeId,
          nodeName: item.nodeName,
          authresType: item.authresType
        });
      });
      _this.pasteDisabled.show = true;
      _this.pasteDisabled = clone(_this.pasteDisabled, {});
      // 发起请求
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/authorization/copyAuth',
        data: {
          copyForAuthObjIds: copyForAuthObjIds.join(),
          authObjId: this.chooseRoleId,
          authObjType: this.activeName,
          authFormList: copyForAuthObj
        },
      }).then(({code, message, data}) => {
        _this.pasteDisabled.show = false;
        _this.pasteDisabled = clone(_this.pasteDisabled, {});
        if (code === '0000') {
          _this.$message({type: 'success', message: _this.$t('roleDataPowerManager.ztsqcg') });
          _this.pasteDialogVisible = false;
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      });
    },

    /**
    * 用户/角色id选择
    * @param val-当前用户/角色id
    */
    getUserRoleId(val) {
      this.treeParams.userRoleId = val;
    },

    /**
    * 用户/角色id选择项发生变化时改变结构树
    * @param param1-当前用户/角色id
    * @param param2-当前页签是否是角色授权
    * @param param3-当前被激活的页签
    */
    getAuthObjId(param1, param2, param3) {
      this.chooseRoleId = param1;
      this.isRoleTab = param2;
      this.activeName = param3;
      if(this.isRoleTab) {
        this.expectedRoleId = this.chooseRoleId;
        this.expectedUserId = "";
      } else {
        this.expectedUserId = this.chooseRoleId;
        this.expectedRoleId = "";
      }
      this.showTree = true;
      this.$nextTick(function () {
        this.treeSearchVal = '';
        this.getTreeData();
      });
    }
  }
}
</script>
<style>
.yu-frame-tab-box>div>div.authorize-manage {
  -webkit-box-shadow: none;
  box-shadow: none;
  background: #f2f2f2;
}

.tree-box {
  background: #ffffff!important;
  float: right;
  width: calc(100% - 424px);
  min-height: calc(100vh - 156px);
  position: relative;
}

.compact .tree-box {
  width: calc(100% - 416px);
  min-height:calc(100vh - 115px);
}

/* 右侧css */

.handle-btn {
  float: right;
}

.handle-btn .search-input {
  width: 200px;
  margin-left: 12px;
}

.handle-btn .el-input__suffix-inner {
  line-height: 36px;
}
.compact .handle-btn .el-input__suffix-inner {
  line-height: 36px;
}

.authorize-manage .el-button--primary:focus, .el-button--primary:hover{
 color: #ffffff!important;
}

.tree-box .tree-header {
  height: 56px;
  border-bottom: 1px solid #EDEDED;
  line-height: 56px;
  padding: 0 24px 0 60px;
}

.tree-header .el-checkbox__label {
  padding-left: 8px;
  vertical-align: middle;
}

.authorize-manage .tree-content {
  padding: 24px;
  height: calc(100% - 102px);
}

.tree-content .el-tree-x {
  border: none;
  height: 100%;
}

.custom-tree-label span {
  vertical-align: middle;
}

.tree-content .el-tree-node__content>.el-tree-node__expand-icon {
  padding: 0 12px;
}

.authorize-manage .el-tree-node__content>.el-checkbox {
  margin-right: 12px;
  vertical-align: middle;
}

.auth-btn {
  color: #2877ff;
  cursor: pointer;
  display: none;
}

.auth-btn:hover {
  text-decoration: underline;
}

.custom-tree-label {
  font-size: 14px;
}

.authorize-manage .el-tree-node__content:hover .auth-btn {
  display: inline-block;
}
</style>
