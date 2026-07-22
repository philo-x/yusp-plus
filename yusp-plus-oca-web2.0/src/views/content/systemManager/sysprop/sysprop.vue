<!--
  @created by tuxw@yusys.com.cn on 2021-01-12
  @description 系统参数
  @updated by
-->
<template>
  <div class="sys-prop" :style="{'min-height': frameHeight - 48 + 'px'}">
    <yu-panel ref="panel" :title="$t('sysprop.xtcs')" show-search-input @search="searchSysPropData" :placeholder="$t('sysprop.gjz')">
      <template slot="filter">
        <yu-xform v-model="sysPropFormdata" related-table-name="sysPropTable" form-type="search">
          <yu-xform-group :column="4">
            <yu-xform-item name="propName" :label="$t('sysprop.csm')" :placeholder="$t('sysprop.qsr')"></yu-xform-item>
            <yu-xform-item name="propDesc" :label="$t('sysprop.csms')" :placeholder="$t('sysprop.qsr')"></yu-xform-item>
          </yu-xform-group>
        </yu-xform>
      </template>
      <template slot="right">
        <yu-toolBar>
          <yu-button v-if="checkCtrl('add')" @click="addSysProp">{{ $t('sysprop.xz') }}</yu-button>
          <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled @click="deleteSysProp(false)">{{ $t('sysprop.sc') }}</yu-button>
        </yu-toolBar>
      </template>
      <yu-xtable
        ref="sysPropTable"
        selection-type="checkbox"
        :data-url="sysPropTableUrl"
        request-type="POST"
        row-number
        :max-height="frameHeight - 205"
        :utrace="true"
        :u-pk-value="uPkValue"
        :ukey-field="uKeyField"
        :trace-get-interface="traceGetInterface"
        :trace-get-list-interface="traceGetListInterface"
        :trace-save-interface="traceSaveInterface">
        <yu-xtable-column prop="propName" :label="$t('sysprop.csm')" min-width="110px"></yu-xtable-column>
        <yu-xtable-column prop="propValue" :label="$t('sysprop.csz')" min-width="120px"></yu-xtable-column>
        <yu-xtable-column prop="propDesc" :label="$t('sysprop.csms')" min-width="120px"></yu-xtable-column>
        <yu-xtable-column :label="$t('sysprop.zjgx')" min-width="260px">
          <template slot-scope="scope">
            <span>{{ scope.row.userName }}（{{ scope.row.lastChgDt }}）</span>
          </template>
        </yu-xtable-column>
        <yu-xtable-column class="handle-box" :label="$t('sysprop.cz')" width="160px">
          <template slot-scope="scope">
            <yu-button v-if="checkCtrl('edit')" type="text" @click="modifySysProp(scope.row)">{{ $t('sysprop.xg') }}</yu-button>
            <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled type="text" @click="deleteSysProp(scope.row)">{{ $t('sysprop.sc') }}</yu-button>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
    <!-- 编辑(新增/修改)系统参数弹窗 -->
    <yu-xdialog class="edit-sys-prop-dialog" :title="viewTitle[viewType]" :visible.sync="isShowEditSysPropDialog" :center="true" :close-on-click-modal="false" size="tiny">
      <yu-xform
        ref="editSysPropForm"
        v-model="editSysPropFormData"
        :label-width="$store.getters.language==='en'?'168px':'100px'"
        :rules="editSysPropFormRules"
        :column="1"
        :utrace="isUtrace"
        :u-pk-value="uPkValue"
        :ukey-field="uKeyField"
        :utrace-usr-id="traceUserId"
        :utrace-org-id="orgId"
        :utrace-menu-id="menuId"
        :trace-get-interface="traceGetInterface"
        :trace-get-list-interface="traceGetListInterface"
        :trace-save-interface="traceSaveInterface">
        <yu-xform-item name="propName" :label="$t('sysprop.csm')" :placeholder="$t('sysprop.qsr')"></yu-xform-item>
        <yu-xform-item name="propValue" :label="$t('sysprop.csz')" :placeholder="$t('sysprop.qsr')"></yu-xform-item>
        <yu-xform-item name="propDesc" :label="$t('sysprop.csms')" :placeholder="$t('sysprop.qsr')" ctype="textarea"></yu-xform-item>
      </yu-xform>
      <div slot="footer" class="yu-grpButton">
        <yu-button type="primary" v-norepeat.disabled @click="saveSysProp">{{ $t('sysprop.bc') }}</yu-button>
        <yu-button @click="closeEditSysPropDialog">{{ $t('sysprop.qx') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import {mapGetters} from "vuex"
import {extend, lookup, sessionStore} from '@/utils'
import router from '@/router'

var frameSize = sessionStore.get('VIEW-SIZE');
export default {
  data() {
    const tab = this.$route || router.history.current;
    return {
      uKeyField: 'propId', // 表格数据主键
      uPkValue: 'sysPropTable', // 随机值，用于标注当前表单唯一
      menuId: tab.meta.id, // 菜单ID
      orgId: '', // 机构ID
      isUtrace: false, // 是否开启小u留痕
      oldSysPropFormData: {}, // 小U留存报存接口参数
      traceUserId: '', // userId
      traceGetInterface: backend.appOcaService + '/api/utrace/listByPk', // 显示小U提示获取数据接口
      traceGetListInterface: backend.appOcaService + '/api/utrace/listPage', // 点击小U图标展示留痕列表获取数据接口
      traceSaveInterface: backend.appOcaService + '/api/utrace/save', // 小U保存数据接口,需要在表单保存数据时，手工调用表单的saveUTrace方法
      sysPropTableUrl: backend.appOcaService + '/api/adminsmprop/page',
      frameHeight: frameSize.height,
      sysPropFormdata: {}, // 查询系统参数table的过滤条件表单数据
      editSysPropFormData: {}, // 编辑(新增/修改)系统参数表单数据
      editSysPropFormRules: {
        propName: [{required: true, message: this.$t('sysprop.btx')}],
        propValue: [{required: true, message: this.$t('sysprop.btx')}],
        propRemark: [{ max: 950, message: this.$t('sysprop.srzgc') }]
      },
      isShowEditSysPropDialog: false, // 是否显示编辑(即新增或修改)系统参数的弹窗
      viewType: 'DETAIL', // 表单操作状态
      viewTitle: lookup.find('CRUD_TYPE', false), // 弹出框类型
    };
  },
  computed: {
    ...mapGetters([
      "userId",
      "roles"
    ])
  },
  mounted() {
    this.orgId = this.roles.length && this.roles[0].id;
    this.traceUserId = this.userId;
  },

  methods: {
    remoteTableData(panelRef, tableRef, searFormVmodel) {
      // panel隐藏的时候
      if (this.$refs[panelRef].hide) {
        this.$refs[tableRef].remoteData({keyWord: this.$refs[panelRef].inputVal})
      } else {
        this.$refs[tableRef].remoteData(this[searFormVmodel])
      }
    },
    remoteData() {
      this.remoteTableData('panel', 'sysPropTable', 'sysPropFormdata')
    },
    searchSysPropData(params) {
      this.$refs.sysPropTable.remoteData({ keyWord: params.value });
    },

    // 点击新增
    addSysProp() {
      this.isShowEditSysPropDialog = true;
      this.isUtrace = false;
      this.viewType = 'ADD';
      this.$nextTick(function () {
        this.$refs.editSysPropForm.resetFields();
        this.$refs.editSysPropForm.setFormData({});
      });
    },

    // 保存系统参数
    saveSysProp() {
      var validate = false;
      this.$refs.editSysPropForm.validate(function (valid) {
        validate = valid;
      });
      if (validate) {
        // this.isShowEditSysPropDialog = false;
        this.editSysPropFormData.lastChgUsr = this.userId;
        if (this.viewType === 'ADD') {
          this.createdSysProp();
        } else {
          this.updateSysProp();
        }
      }
    },

    // 新增系统参数
    createdSysProp() {
      var _this = this;
      _this.$request({
        url: backend.appOcaService + '/api/adminsmprop/save',
        method: 'post',
        data: _this.editSysPropFormData,
      }).then(({code, message, data}) => {
        if (code == '0000') {
          _this.$message({message: _this.$t('sysprop.bccg')});
          this.isShowEditSysPropDialog = false;
          _this.remoteData();
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      });
    },

    // 点击修改
    modifySysProp(row) {
      this.viewType = 'EDIT';
      this.isShowEditSysPropDialog = true;
      this.isUtrace = false;
      this.$nextTick(function () {
        this.$refs.editSysPropForm.setFormData(row);
        this.isUtrace = true;
        this.oldSysPropFormData = extend([], row);
      });
    },

    // 修改系统参数
    updateSysProp() {
      var _this = this;
      _this.$request({
        url: backend.appOcaService + '/api/adminsmprop/update',
        method: 'post',
        data: _this.editSysPropFormData,
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$refs.editSysPropForm.saveUTrace(_this.oldSysPropFormData);
          _this.$message({message: _this.$t('sysprop.xgcg'), type: 'success'});
          this.isShowEditSysPropDialog = false;
          _this.remoteData();
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      });
    },

    // 删除系统参数
    deleteSysProp: function(row) {
      var _this = this;
      var selecttions = row ? [row] : this.$refs.sysPropTable.selections;
      if (selecttions.length === 0) {
        this.$message({ message: this.$t('sysprop.qxxzyscdsj'), type: 'warning' });
        return;
      }
      var ids = selecttions.map(function (item) {
        return item.propId;
      });
      this.$confirm(this.$t('sysprop.qrscgsjm'), this.$t('sysprop.ts'), {
        confirmButtonText: this.$t('sysprop.qr'),
        cancelButtonText: this.$t('sysprop.qx'),
        type: 'warning'
      }).then(function () {
        _this.$request({
          url: backend.appOcaService + '/api/adminsmprop/delete',
          method: 'post',
          data: ids,
        }).then(({code, message, data}) => {
          if (code === '0000') {
            _this.$message({ message: _this.$t('sysprop.sccg') });
            _this.remoteData();
          } else {
            _this.$message({ message: message, type: 'error' });
          }
        });
      });
    },

    closeEditSysPropDialog() {
      this.isShowEditSysPropDialog = false;
    }
  }
}
</script>
