<!--
  @created by tuxw@yusys.com.cn on 2021-01-13
  @description 系统消息管理
  @updated by
-->
<template>
  <div class="message-manager" :style="{'min-height': frameHeight - 48 + 'px'}">
    <yu-panel ref="panel" :title="$t('messageManager.xttsxxgl')" show-search-input @search="searchMsgData" :placeholder="$t('messageManager.gjz')">
      <template slot="right">
        <yu-toolBar>
          <yu-button v-if="checkCtrl('add')" @click="addMsg">{{ $t('messageManager.xz') }}</yu-button>
          <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled @click="deleteMsg(false)">{{ $t('messageManager.sc') }}</yu-button>
        </yu-toolBar>
      </template>
      <template slot="filter">
        <yu-xform v-model="msgFormdata" related-table-name="msgTable" form-type="search" label-width="100px">
          <yu-xform-group :column="4">
            <yu-xform-item name="code" :label="$t('messageManager.xxm')" :placeholder="$t('messageManager.qsr')"></yu-xform-item>
            <yu-xform-item name="message" :label="$t('messageManager.tsnr')" :placeholder="$t('messageManager.qsr')"></yu-xform-item>
          </yu-xform-group>
        </yu-xform>
      </template>
      <yu-xtable request-type="POST" ref="msgTable" selection-type="checkbox" :data-url="msgTableUrl" row-number :max-height="frameHeight - 205">
        <yu-xtable-column :label="$t('messageManager.xxm')" min-width="150">
          <template slot-scope="scope">
            <a class="underline" @click="viewMsgDetails(scope.row)">{{ scope.row.code }}</a>
          </template>
        </yu-xtable-column>
        <yu-xtable-column prop="message" :label="$t('messageManager.tsnr')" min-width="220px"></yu-xtable-column>
        <yu-xtable-column prop="messageLevel" :label="$t('messageManager.xxjb')" min-width="150" data-code="MESSAGE_LEVEL"></yu-xtable-column>
        <yu-xtable-column prop="messageType" :label="$t('messageManager.xxlb')" width="120px" data-code="MESSAGE_TYPE"></yu-xtable-column>
        <yu-xtable-column prop="funcName" :label="$t('messageManager.ssmkmc')" width="160px"></yu-xtable-column>
        <yu-xtable-column :label="$t('messageManager.zjgx')" min-width="160px">
          <template slot-scope="scope">
            <span>{{ scope.row.userName }}（{{ scope.row.lastChgDt }}）</span>
          </template>
        </yu-xtable-column>
        <yu-xtable-column class="handle-box" :label="$t('messageManager.cz')" width="160px" fixed="right">
          <template slot-scope="scope">
            <yu-button v-if="checkCtrl('edit')" type="text" @click="modifyMsg(scope.row)">{{ $t('messageManager.xg') }}</yu-button>
            <yu-button v-if="checkCtrl('delete')" v-norepeat.disabled type="text" @click="deleteMsg(scope.row)">{{ $t('messageManager.sc') }}</yu-button>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
    <!-- 编辑(新增/修改)系统参数弹窗 -->
    <yu-xdialog class="edit-message-manager-dialog" :title="viewType === 'ADD' ? $t('messageManager.xz') : msgFormType === 'details' ? $t('messageManager.xq') : $t('messageManager.xg')" :visible.sync="isShowEditMsgDialog" :center="true" :close-on-click-modal="false" size="tiny">
      <yu-xform ref="editMsgForm" v-model="editMsgFormData" :form-type="msgFormType" :column="1" :rules="editMsgFormRules" :label-width="$store.getters.language==='en'?'148px':'110px'">
        <yu-xform-item :disabled="viewType != 'ADD'" :label="$t('messageManager.xxm')"
                       :placeholder="$t('messageManager.qsr')" name="code"></yu-xform-item>
        <yu-xform-item name="messageLevel" :label="$t('messageManager.xxjb')" ctype="select" data-code="MESSAGE_LEVEL" :placeholder="$t('messageManager.qxz')"></yu-xform-item>
        <yu-xform-item name="messageType" :label="$t('messageManager.xxlb')" ctype="select" data-code="MESSAGE_TYPE" :placeholder="$t('messageManager.qxz')"></yu-xform-item>
        <yu-xform-item name="funcName" :label="$t('messageManager.ssmkmc')" :placeholder="$t('messageManager.qsr')"></yu-xform-item>
        <yu-xform-item name="message" :label="$t('messageManager.tsnr')" ctype="textarea" :placeholder="$t('messageManager.qsr')"></yu-xform-item>
      </yu-xform>
      <div slot="footer" class="yu-grpButton">
        <yu-button v-if="msgFormType === 'details'" type="primary" @click="editMsg">{{ $t('messageManager.xg') }}</yu-button>
        <yu-button v-else type="primary" v-norepeat.disabled @click="saveMsg" key="save">{{ $t('messageManager.bc') }}</yu-button>
        <yu-button v-if="viewType === 'DETAIL'" @click="closeEditMsgDialog">{{ $t('messageManager.fh') }}</yu-button>
        <yu-button v-else @click="closeEditMsgDialog">{{ $t('messageManager.qx') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import {mapGetters} from "vuex"
import {lookup, sessionStore} from '@/utils'

lookup.reg('MESSAGE_LEVEL,MESSAGE_TYPE');
var frameSize = sessionStore.get('VIEW-SIZE');
export default {
  data: function () {
    return {
      msgTableUrl: backend.appOcaService + '/api/adminsmmessage/page',
      frameHeight: frameSize.height,
      msgFormdata: {}, // 查询消息table的过滤条件表单数据
      editMsgFormData: {}, // 编辑(新增/修改)消息表单数据
      msgFormType: 'details',
      editMsgFormRules: {
        code: [{ required: true, message: this.$t('messageManager.btx') }],
        message: [{ required: true, message: this.$t('messageManager.btx') }],
        messageLevel: [{ required: true, message: this.$t('messageManager.btx') }],
        messageType: [{ required: true, message: this.$t('messageManager.btx') }]
      },
      isShowEditMsgDialog: false, // 是否显示编辑(即新增或修改)消息的弹窗
      viewType: 'DETAIL' // 表单操作状态
    };
  },
  computed: {
    ...mapGetters([
      "userId"
    ])
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
      this.remoteTableData('panel', 'msgTable', 'msgFormdata')
    },
    searchMsgData: function (params) {
      this.$refs.msgTable.remoteData({ keyWord: params.value });
    },

    // 点击新增
    addMsg: function () {
      this.isShowEditMsgDialog = true;
      this.viewType = 'ADD';
      this.msgFormType = 'edit';
      this.$nextTick(function () {
        this.$refs.editMsgForm.resetFields();
        this.$refs.editMsgForm.setFormData({});
      });
    },

    // 保存消息
    saveMsg: function () {
      var validate = false;
      this.$refs.editMsgForm.validate(function (valid) {
        validate = valid;
      });
      if (validate) {
        // this.isShowEditMsgDialog = false;
        this.editMsgFormData.lastChgUsr = this.userId;
        if (this.viewType === 'ADD') {
          this.createdMsg();
        } else {
          this.updateMsg();
        }
      }
    },

    // 新增消息
    createdMsg: function () {
      var _this = this;
      _this.$request({
        url: backend.appOcaService + '/api/adminsmmessage/save',
        method: 'post',
        data: _this.editMsgFormData,
      }).then(({code, message, data}) => {
        if (code == '0000') {
          _this.$message({ message: _this.$t('messageManager.bccg') });
          _this.remoteData();
          _this.isShowEditMsgDialog = false;
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      });
    },

    // 点击查看详情
    viewMsgDetails: function (row) {
      this.modifyMsg(row);
      this.viewType = 'DETAIL';
      this.msgFormType = 'details';
    },

    // 点击修改
    modifyMsg: function (row) {
      this.viewType = 'UPDATE';
      this.isShowEditMsgDialog = true;
      this.msgFormType = 'edit';
      this.$nextTick(function () {
        this.$refs.editMsgForm.setFormData(row);
      });
    },

    editMsg: function () {
      this.msgFormType = 'edit';
    },

    // 修改消息
    updateMsg: function () {
      var _this = this;
      _this.$request({
        url: backend.appOcaService + '/api/adminsmmessage/update',
        method: 'post',
        data: _this.editMsgFormData,
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$message({ message: _this.$t('messageManager.xgcg') });
          _this.isShowEditMsgDialog = false;
          _this.remoteData();
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      });
    },

    // 删除消息
    deleteMsg: function (row) {
      var _this = this;
      var selecttions = row ? [row] : this.$refs.msgTable.selections;
      if (selecttions.length === 0) {
        this.$message({ message: this.$t('messageManager.qxxzyscdsj'), type: 'warning' });
        return;
      }
      var ids = selecttions.map(function (item) {
        return item.messageId;
      });
      this.$confirm(this.$t('messageManager.qrscgsjm'), this.$t('messageManager.ts'), {
        confirmButtonText: this.$t('messageManager.qr'),
        cancelButtonText: this.$t('messageManager.qx'),
        type: 'warning'
      }).then(function () {
        _this.$request({
          url: backend.appOcaService + '/api/adminsmmessage/delete',
          method: 'post',
          data: ids,
        }).then(({code, message, data}) => {
          if (code === '0000') {
            _this.$message({ message: _this.$t('messageManager.sccg') });
            _this.remoteData();
          } else {
            _this.$message({ message: message, type: 'error' });
          }
        });
      });
    },

    closeEditMsgDialog: function () {
      this.isShowEditMsgDialog = false;
    }
  }
}
</script>
