<!--
  @created by tuxw@yusys.com.cn on 2021-01-15
  @description 数据字典配置
  @updated by
-->
<template>
  <div class="lookup-dict" :style="{'min-height': frameHeight - 48 + 'px'}">
    <yu-panel ref="panel" :title="$t('lookupdict.sjzdpz')" panel-type="simple" show-search-input @search="searchLookupData" :placeholder="$t('lookupdict.gjz')">
      <yu-toolBar slot="right">
        <yu-button v-if="checkCtrl('add')" @click="addLookup">{{ $t('lookupdict.xz') }}</yu-button>
        <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled @click="deleteLookup(false)">{{ $t('lookupdict.sc') }}</yu-button>
        <yu-button v-if="checkCtrl('refresh')" v-norepeat.disabled @click="refreshCache">{{ $t('lookupdict.hcsx') }}</yu-button>
      </yu-toolBar>
      <yu-xtable request-type="POST" ref="lookupTable" class="lookup-table" selection-type="checkbox" :data-url="typetableUrl" row-number @expand="expandRow">
        <yu-xtable-column type="expand">
          <template slot-scope="props">
            <div>{{ props.row.lookupItemsString }}</div>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('lookupdict.zdmc')" prop="lookupName"></yu-xtable-column>
        <yu-xtable-column :label="$t('lookupdict.zddm')" prop="lookupCode"></yu-xtable-column>
        <yu-xtable-column :label="$t('lookupdict.zdfl')" prop="lookupTypeName"></yu-xtable-column>
        <yu-xtable-column class="handle-box" :label="$t('lookupdict.cz')" width="160px">
          <template slot-scope="scope">
            <yu-button-drop set-index="0" :show-length="2" type="text">
              <yu-button v-if="checkCtrl('addItem')" type="text" @click="openEditLookupItemsDialog(scope.row)">{{ $t('lookupdict.tjzdx') }}</yu-button>
              <yu-button v-if="checkCtrl('edit')" type="text" @click="modifyLookup(scope.row)">{{ $t('lookupdict.xg') }}</yu-button>
              <yu-button v-if="checkCtrl('delete')" type="text" v-norepeat.disabled @click="deleteLookup(scope.row)">{{ $t('lookupdict.sc') }}</yu-button>
            </yu-button-drop>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
    <!-- 编辑(新增/修改)数据字典 -->
    <yu-xdialog ref="editLookupDialog" class="edit-lookup-dialog" :center="true" :title="viewType === 'ADD' ? $t('lookupdict.xz') : $t('lookupdict.xg')" :visible.sync="isShowEditLookupDialog" size="small" height="480px">
      <yu-xform ref="editLookupForm" v-model="editLookupFormData" label-width="120px" :rules="editLookupFormRules">
        <yu-xform-group :column="1">
          <yu-xform-item name="lookupName" :label="$t('lookupdict.zdmc')" :placeholder="$t('lookupdict.lrxb')"></yu-xform-item>
        </yu-xform-group>
        <yu-xform-group :column="2">
          <yu-xform-item name="lookupCode" :label="$t('lookupdict.zddm')" :disabled="viewType === 'UPDATE'" :placeholder="$t('lookupdict.lrsexType')"></yu-xform-item>
          <yu-xform-item name="lookupTypeId" :label="$t('lookupdict.zdfl')" ctype="select" :options="lookupTypeOptions" @change="lookupTypeIdChange" :props="{key: 'lookupItemId', value: 'lookupItemName'}" :placeholder="$t('lookupdict.qxz ')"></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
      <div class="btn-box">
        <yu-button @click="pushLookupItem">{{ $t('lookupdict.tjzdx') }}</yu-button>
      </div>
      <yu-xtable request-type="POST" ref="editLookupItemTable" row-number :data="currentLookupItems" :pageable="false" :rules="lookupItemRule">
        <yu-xtable-column prop="lookupItemCode" ctype="input" label="key" :placeholder="$t('lookupdict.qsr')"></yu-xtable-column>
        <yu-xtable-column prop="lookupItemName" ctype="input" label="value" :placeholder="$t('lookupdict.qsr')"></yu-xtable-column>
        <yu-xtable-column class="handle-box" :label="$t('lookupdict.cz')" width="180px">
          <template slot-scope="scope">
            <yu-button type="text" @click="deleteLookupItem(scope)" :disabled="currentLookupItems.length === 1">{{ $t('lookupdict.sc') }}</yu-button>
            <yu-button type="text" v-if="scope.$index != 0" @click="moveUp(scope.$index)">{{ $t('lookupdict.sy') }}</yu-button>
            <yu-button type="text" v-if="scope.$index < currentLookupItems.length - 1" @click="moveDown(scope.$index)">{{ $t('lookupdict.xy') }}</yu-button>
          </template>
        </yu-xtable-column>
      </yu-xtable>
      <div slot="footer" class="yu-grpButton">
        <yu-button type="primary" v-norepeat.disabled @click="saveLookup">{{ $t('lookupdict.bc') }}</yu-button>
        <yu-button @click="closeEditLookupDialog">{{ $t('lookupdict.qx') }}</yu-button>
      </div>
    </yu-xdialog>
    <!-- 编辑/新增字典项 -->
    <yu-xdialog ref="editLookupItemsDialog" class="edit-lookup-items-dialog" :center="true" :title="$t('lookupdict.tjzdx') + '（' + editLookupFormData.lookupName + '/' + editLookupFormData.lookupCode +'）'" :visible.sync="isShowEditLookupItemsDialog" size="small" height="360px">
      <div class="btn-box">
        <yu-button @click="pushLookupItem">{{ $t('lookupdict.tjzdx') }}</yu-button>
      </div>
      <yu-xtable request-type="POST" ref="editLookupItemTable" row-number :data="currentLookupItems" :pageable="false" :rules="lookupItemRule">
        <yu-xtable-column prop="lookupItemCode" ctype="input" label="key" :placeholder="$t('lookupdict.qsr')"></yu-xtable-column>
        <yu-xtable-column prop="lookupItemName" ctype="input" label="value" :placeholder="$t('lookupdict.qsr')"></yu-xtable-column>
        <yu-xtable-column class="handle-box" :label="$t('lookupdict.cz')" width="180px">
          <template slot-scope="scope">
            <yu-button type="text" @click="deleteLookupItem(scope)" :disabled="currentLookupItems.length === 1">{{ $t('lookupdict.sc') }}</yu-button>
            <yu-button type="text" v-if="scope.$index != 0" @click="moveUp(scope.$index)">{{ $t('lookupdict.sy') }}</yu-button>
            <yu-button type="text" v-if="scope.$index < currentLookupItems.length - 1" @click="moveDown(scope.$index)">{{ $t('lookupdict.xy') }}</yu-button>
          </template>
        </yu-xtable-column>
      </yu-xtable>
      <div slot="footer" class="yu-grpButton">
        <yu-button type="primary" v-norepeat.disabled @click="addLookupItems">{{ $t('lookupdict.bc') }}</yu-button>
        <yu-button @click="closeEditLookupItemsDialog">{{ $t('lookupdict.qx') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import {clone, sessionStore} from '@/utils'

var frameSize = sessionStore.get('VIEW-SIZE');
export default {
  data() {
    return {
      typetableUrl: backend.appOcaService + '/api/adminsmlookupdict/list', // 数据字典表格url
      frameHeight: frameSize.height,
      isShowEditLookupDialog: false,
      editLookupFormData: {lookupItemsString: ''}, // 编辑(新增/修改)数据字典表单的数据
      editLookupFormRules: {
        lookupCode: [
          { required: true, message: this.$t('lookupdict.btx') },
          { max: 50, message: this.$t('lookupdict.zdcdbcggywzf') }
        ],
        lookupName: [
          { required: true, message: this.$t('lookupdict.btx') },
          { max: 100, message: this.$t('lookupdict.zdcdbcggzwzf') }
        ],
        lookupTypeId: [
          { required: true, message: this.$t('lookupdict.btx') }
        ]
      },
      lookupTypeOptions: [], // 字典分类的选项
      currentLookupItems: [{ lookupItemCode: '', lookupItemName: '' }], // 当前的字典项
      lookupItemRule: {
        lookupItemCode: [{ required: true, message: this.$t('lookupdict.btx') }, { validator: this.codeKeyValid, trigger: 'blur'}],
        lookupItemName: [{ required: true, message: this.$t('lookupdict.btx') }]
      },
      viewType: 'UPDATE',
      isShowEditLookupItemsDialog: false,
      editLookupItemsFormData: {}, // 编辑数据字典项的表单数据
    };
  },

  created() {
    this.getLookupTypeOptions();
  },

  methods: {
    remoteTableData(panelRef, tableRef, searFormVmodel) {
      // panel隐藏的时候
      if (this.$refs[panelRef].hide) {
        this.$refs[tableRef].remoteData({keyWord: this.$refs.panel.inputVal})
      } else {
        this.$refs[tableRef].remoteData(this[searFormVmodel])
      }
    },
    remoteData() {
      this.remoteTableData('panel', 'lookupTable', 'searchFormdata')
    },
    codeKeyValid(rule, value, callbackFn) {
      var _this = this;
      var arr = _this.currentLookupItems.filter(function (item) {
        return item.lookupItemCode === value;
      });
      if (arr.length > 1) {
        callbackFn(new Error(_this.$t('lookupdict.kbncf')));
      } else {
        callbackFn();
      }
    },
    getLookupTypeOptions() {
      var _this = this;
      _this.$request({
        url: backend.appOcaService + '/api/adminsmlookupdict/queryinitdict',
        method: 'POST',
        data: {}
      }).then(({code, message, data}) => {
        //处理请求成功的情况
        if (code === '0000') {
          _this.lookupTypeOptions = data;
        }
      })
    },

    searchLookupData(params) {
      this.$refs.lookupTable.remoteData({ keyWord: params.value });
    },

    // 增加字典类别
    addLookup() {
      this.isShowEditLookupDialog = true;
      this.viewType = 'ADD';
      this.currentLookupItems = [];
      this.$nextTick(function () {
        this.$refs.editLookupForm.setFormData({ lookupName: '', lookupCode: '', lookupTypeId: '' });
        this.$refs.editLookupItemTable.clearValidateMessage();
        this.pushLookupItem();
      });
    },
    checkLookupItems() {
      var array = [];
      this.currentLookupItems.map(item => {
        if(item.lookupItemCode) {
          array.push(item.lookupItemCode);
        }
      });
      const valuesAlreadySeen = [];
      for (var i = 0; i < array.length; i++) {
        if (valuesAlreadySeen.indexOf(array[i]) !== -1) {
          return true
        }
        valuesAlreadySeen.push(array[i])
      }
      return false
    },

    lookupTypeIdChange(val) {
      var _this = this;
      this.lookupTypeOptions.forEach(function (item) {
        if (item.lookupItemId === _this.editLookupFormData.lookupTypeId) {
          _this.editLookupFormData.lookupTypeName = item.lookupItemName;
        }
      });
    },

    // 修改数据字典
    modifyLookup(row) {
      var selections = row ? [row] : this.$refs.lookupTable.selections;
      if (selections.length !== 1) {
        this.$message({ message: this.$t('lookupdict.qxzytsj') });
        return;
      }
      this.viewType = 'UPDATE';
      this.isShowEditLookupDialog = true;
      this.currentLookupItems = [];
      this.$nextTick(function () {
        this.$refs.editLookupForm.setFormData(selections[0]);
        this.getLookupItems(this.editLookupFormData, true);
        this.$refs.editLookupItemTable.clearValidateMessage();
      });
    },

    // 查询某条数据字典下的所有字典项
    getLookupItems(row, isEditLookupFormData) {
      var _this = this;
      _this.$request({
        url: backend.appOcaService + '/api/adminsmlookupdict/info/' + row.lookupItemId,
        method: 'post',
        data: {}
      }).then(({code, message, data}) => {
        //处理请求成功的情况
        if (code === '0000' && data && Array.isArray(data)) {
          if (isEditLookupFormData) {
            _this.currentLookupItems = data;
            _this.$refs.editLookupItemTable && _this.$refs.editLookupItemTable.clearValidateMessage();
          }
          var newLookupItems = data.map(function (item) {
            return { key: item.lookupItemCode, value: item.lookupItemName };
          });
          row.lookupItemsString = JSON.stringify(newLookupItems);
        }
      })
    },

    // 保存字典类别
    saveLookup() {
      var formValidate = true, isLookupItemValidateOk = false;
      this.$refs.editLookupForm.validate(function (valid) {
        formValidate = valid;
      });
      this.$refs.editLookupItemTable.validate(function (fields) {
        if (!fields) { // 如果校验通过, fields为null
          isLookupItemValidateOk = true;
        }
      });
      if (!formValidate || !isLookupItemValidateOk) {
        return;
      }
      if (this.checkLookupItems()) {
        this.$message({message: this.$t('lookupdict.kzbncf'), type: 'warning'});
        return;
      }
      this.editLookupFormData.lookupItemBos = this.currentLookupItems;
      this.viewType === 'ADD' ? this.createdLookup() : this.updateLookup();
      // this.$refs.lookupTable.remoteData();
      // this.isShowEditLookupDialog = false;
    },

    // 新增数据字典
    createdLookup() {
      var _this = this;
      _this.$request({
        url: backend.appOcaService + '/api/adminsmlookupdict/save',
        method: 'post',
        data: this.editLookupFormData
      }).then(({code, message, data}) => {
        //处理请求成功的情况
        if (code === '0000') {
          _this.$message({ message: _this.$t('lookupdict.bccg'), type: 'success' });
          _this.remoteData();
          _this.isShowEditLookupDialog = false;
        } else if (code === '20100002') {
          _this.$message({ message: message, type: 'warning' });
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      })
    },

    // 更新数据字典
    updateLookup() {
      var _this = this;
      _this.$request({
        url: backend.appOcaService + '/api/adminsmlookupdict/update',
        method: 'post',
        data: this.editLookupFormData
      }).then(({code, message, data}) => {
        //处理请求成功的情况
        if (code === '0000') {
          _this.$message({ message: _this.$t('lookupdict.bccg'), type: 'success' });
          _this.remoteData();
          _this.isShowEditLookupDialog = false;
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      })
    },

    // 删除数据字典
    deleteLookup(row) {
      var selections = row ? [row] : this.$refs.lookupTable.selections;
      if (selections.length < 1) {
        this.$message({ message: this.$t('lookupdict.qxxzyscdsj'), type: 'warning' });
        return;
      }
      this.confirmDeleteLookup(selections);
    },

    // 确定删除数据字典
    confirmDeleteLookup(selections) {
      var _this = this;
      var lookupItemIds = selections.map(function (item) {
        return item.lookupItemId;
      });
      this.$confirm(this.$t('lookupdict.qrscgsjm'), this.$t('lookupdict.ts'), {
        type: 'warning',
        confirmButtonText: this.$t('lookupdict.qr'),
        cancelButtonText: this.$t('lookupdict.qx')
      }).then(function () {
        _this.$request({
          url: backend.appOcaService + '/api/adminsmlookupdict/delete',
          method: 'post',
          data: lookupItemIds
        }).then(({code, message, data}) => {
          //处理请求成功的情况
          if (code === '0000') {
            _this.$message({ message: _this.$t('lookupdict.sccg'), type: 'success' });
            _this.remoteData();
          } else {
            _this.$message({ message: message, type: 'error' });
          }
        })
      });
    },

    // 展开 查询并显示该行的数据字典项
    expandRow(row, isExpanded) {
      if (isExpanded && !row.lookupItemsString) {
        this.getLookupItems(row);
      }
    },

    // 刷新缓存
    refreshCache() {
      var _this = this;
      _this.$confirm(this.$t('lookupdict.refreshcachesm'), this.$t('lookupdict.ts'), {
        type: 'warning',
        confirmButtonText: this.$t('lookupdict.qr'),
        cancelButtonText: this.$t('lookupdict.qx')
      }).then(function () {
        _this.$request({
          method: 'POST',
          url: backend.appOcaService + '/api/adminsmlookupdict/refreshdict'
        }).then(({code, message, data}) => {
          //处理请求成功的情况
          if (code === '0000') {
            _this.$message({ message: _this.$t('lookupdict.sjzdhcgxcg'), type: 'success'});
          } else {
            _this.$message({ message: message, type: 'error' });
          }
        })

      });
    },

    closeEditLookupDialog() {
      this.isShowEditLookupDialog = false;
    },

    // 添加数据字典项 打开弹窗
    openEditLookupItemsDialog(row) {
      this.isShowEditLookupItemsDialog = true;
      clone(row, this.editLookupFormData);
      this.getLookupItems(row, true);
      // this.currentLookupItems = [];
      // this.$nextTick(function () {
      //   this.$refs.editLookupItemTable.clearValidateMessage();
      //   this.pushLookupItem();
      // });
    },

    // 添加数据字典项 添加一条空白数据
    pushLookupItem() {
      var isLookupItemValidateOk = false;
      this.$refs.editLookupItemTable.validate(function (fields) {
        if (!fields) { // 如果校验通过, fields为null
          isLookupItemValidateOk = true;
        }
      });
      if (isLookupItemValidateOk) {
        var row = { lookupItemCode: '', lookupItemName: '' };
        this.currentLookupItems.push(row);
        this.$refs.editLookupItemTable.setCurrentRow(row);
      }
    },

    deleteLookupItem(scope) {
      if (this.currentLookupItems.length > 1) {
        this.currentLookupItems.splice(scope.$index, 1);
      }
    },

    moveUp(index) {
      if (index > 0) {
        const update = this.currentLookupItems[index - 1];
        this.currentLookupItems.splice(index - 1, 1);
        this.currentLookupItems.splice(index, 0, update);
      } else {
        this.$message({ message: this.$t('lookupdict.bksy'), type: 'error' });
      }
    },

    moveDown(index) {
      if (this.currentLookupItems.length > index + 1) {
        const downDate = this.currentLookupItems[index + 1];
        this.currentLookupItems.splice(index + 1, 1);
        this.currentLookupItems.splice(index, 0, downDate);
      }else {
        this.$message({ message: this.$t('lookupdict.bkxy'), type: 'error' });
      }
    },

    addLookupItems() {
      var _this = this, isLookupItemValidateOk = false;
      this.$refs.editLookupItemTable.validate(function (fields) {
        if (!fields) { // 如果校验通过, fields为null
          isLookupItemValidateOk = true;
        }
      });
      if (isLookupItemValidateOk) {
        var params = {
          lookupItemId: this.editLookupFormData.lookupItemId,
          lookupItemBos: this.currentLookupItems,
          lookupCode: this.editLookupFormData.lookupCode,
          lookupName: this.editLookupFormData.lookupName,
          lookupTypeId: this.editLookupFormData.lookupTypeId,
          lookupTypeName: this.editLookupFormData.lookupTypeName
        };
        _this.$request({
          url: backend.appOcaService + '/api/adminsmlookupdict/insertdictitem',
          method: 'post',
          data: params
        }).then(({code, message, data}) => {
          //处理请求成功的情况
          if (code === '0000') {
            _this.$message({ message: _this.$t('lookupdict.bccg'), type: 'success' });
            _this.remoteData();
            _this.isShowEditLookupItemsDialog = false;
          } else {
            _this.$message({ message: message, type: 'error' });
          }
        })
      }
    },

    closeEditLookupItemsDialog() {
      this.isShowEditLookupItemsDialog = false;
    }
  }
}
</script>
<style>
  .lookup-dict .lookup-table .el-table__expand-icon>.el-icon {
    margin-top: -11px;
  }
  .lookup-items-box {
    overflow-y: auto;
    margin-bottom: 10px;
  }

  .lookup-items-box .lookup-item-header {
    padding: 0 8px 0 16px;
    margin-bottom: 16px;
    line-height: 30px;
    border-radius: 4px;
    background: #F2F2F2;
  }
  .lookup-items-box .lookup-item-header .yu-icon-close {
    float: right;
    opacity: 0;
    cursor: pointer;
    transition: all 0.3s ease;
  }
  .lookup-items-box:hover .yu-icon-close {
    opacity: 1;
  }
  .edit-lookup-dialog .btn-box, .edit-lookup-items-dialog .btn-box {
    margin-bottom: 22px;
    text-align: right;
  }
  .edit-lookup-dialog .yu-xform .el-form-item {
    height: 36px;
  }
</style>
