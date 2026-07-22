<!--
  @Created by zhuly8@yusys.com.cn 2021-03-18
  @updated by
  @description test
-->
<template>
  <div class="concise-form" id="editNotice">
    <yu-panel :title="formTit" is-collapse panel-type="simple">
      <yu-xform :rules="formRules" label-width="180px" ref="refForm" v-model="formdata">
        <yu-col :span="10">
          <yu-xform-item :label="$t('notice.ggbt')" name="noticeTitle" :placeholder="$t('notice.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('notice.yxqjssj')" name="activeDate" :placeholder="$t('notice.qxz')"
                         ctype="datepicker" :picker-options="pickerOptions"></yu-xform-item>
          <yu-xform-item :label="$t('notice.zycd')" name="noticeLevel" :placeholder="$t('notice.qsr')" ctype="select"
                         data-code="NOTICE_LEVEL"></yu-xform-item>
          <yu-xform-item :label="$t('notice.sfzd')" name="isTop" ctype="radio" data-code="YESNO"></yu-xform-item>
          <yu-xform-item :label="$t('notice.zdqz')" name="topActiveDate" :placeholder="$t('notice.qxz')"
                         ctype="datepicker" :picker-options="pickerOptions"></yu-xform-item>
        </yu-col>
        <yu-col :span="24">
          <yu-xform-item :label="$t('notice.ggnr')" ctype="custom" name="context" :rows="6" :colspan="24">
            <yu-tinymce ref="tinymce" v-model="formdata.context" :id="tinymceId" :min-length="tinymceMin"
                        :max-length="tinymceMax" @count-valid="countValid"></yu-tinymce>
          </yu-xform-item>
          <yu-xform-item :label="$t('notice.fj')" ctype="custom">
            <yu-single-upload
              dir-name="notice"
              :action="uploadAction"
              :file="fileListInfo"
              :upload-text="uploadText"
              @uploaded="uploadedFn"
              @delete="deleteFileFn"
              @load-number="loadNumberFn"></yu-single-upload>
          </yu-xform-item>
        </yu-col>
        <yu-col :span="10">
          <yu-xform-item :label="$t('notice.jsjg')" name="reciveOrgId" ctype="yufp-search-tree" :params="treeParams" @getSelectIds="getSelectIds" :selectValue="selectOrg"></yu-xform-item>
          <yu-xform-item :label="$t('notice.jsjs')" name="reciveRoleIdNames" :placeholder="$t('notice.qxz')" clearable @focus="selectRoleFn" @clear="clearRoleFn"></yu-xform-item>
        </yu-col>
      </yu-xform>
    </yu-panel>
    <yu-form-buttons :padding-left="124">
      <yu-button @click="saveFn" type="primary">{{ $t('notice.bc') }}</yu-button>
      <yu-button @click="cancleFn">{{ $t('notice.qx') }}</yu-button>
    </yu-form-buttons>

    <!-- 选择角色-->
    <yu-xdialog :title="$t('notice.jsjs')" :visible.sync="dialogVisible" width="480px" height="490px">
      <div class="dialog-search">
        <yu-input v-model="roleKeyWord" icon="search" @click="searchRoleFn" @keyup.enter.native="searchRoleFn" :placeholder="$t('notice.qsr')"></yu-input>
      </div>
      <yu-xtable request-type="POST" ref="rolesTable" :data-url="rolesTableUrl" selection-type="checkbox" @loaded="getTableSelectedFn">
        <yu-xtable-column :label="$t('sysUserManager.jsdm')" prop="roleCode"></yu-xtable-column>
        <yu-xtable-column :label="$t('sysUserManager.jsmc')" prop="roleName"></yu-xtable-column>
      </yu-xtable>
      <div slot="footer" class="yu-grpButton">
        <yu-button v-norepeat.disabled.noStopPropagation type="primary" @click="saveRoleFn">{{ $t('notice.qd') }}</yu-button>
        <yu-button @click="cancelRoleFn">{{ $t('notice.qx') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import {mapGetters} from 'vuex';
import {clone, extend, lookup, uuid} from '@/utils';
import YufpSearchTree from '@/components/widgets/YufpSearchTree';
import YuTinymce from '@/components/widgets/YuTinymce';
import YuSingleUpload from '@/components/widgets/YuSingleUpload';

lookup.reg('NOTICE_LEVEL,YESNO,PUB_STS,READ_STS');

export default {
  components: {YufpSearchTree, YuTinymce, YuSingleUpload},
  data() {
    return {
      tinymceId: 'Tinymce_' + Date.now(),
      roleKeyWord: '',
      rolesTableUrl: backend.appOcaService + '/api/adminsmrole/page',
      dialogVisible: false,
      formType: 'edit',
      formdata: {},
      noticeId: this.$route.query.noticeId,
      formTits: [this.$t('notice.xzgg'), this.$t('notice.editgg'), this.$t('notice.ggxq')],
      formTit: this.$t('notice.xzgg'),
      readSts: {},
      formRules: {
        noticeTitle: { required: true, message: this.$t('notice.qsrbt')},
        activeDate: {required: true, message: this.$t('notice.qsryxqjs')},
        context: [{required: true, message: this.$t('notice.qsrggnr')}, { validator: this.checkTiny }],
        topActiveDate: [{validator: this.checkActiveDate}]
      },
      treeParams: {
        multiple: true,
        dataId: 'orgId',
        dataLabel: 'orgName',
        dataPid: 'upOrgId',
        dataUrl: backend.appOcaService + '/api/adminsmorg/treequeryauth',
        placeholder: this.$t('notice.qxz'),
        searchKey: 'orgName', // 树过滤关键字
        dataParams: {
          orgSts: 'A'
        } // 额外请求参数 默认查询启用的机构
      }, // 机构树选择
      pickerOptions: {
        disabledDate (time) {
          return time.getTime() < Date.now() - 8.64e7;
        }
      },
      selectOrgMap: {},
      selectRoleMap: {},
      fileList: [],
      fileListInfo: [],
      loadFileNum: 0, // 正在现在的文件数量
      uploadText: this.$t('notice.uploadtext'),
      tinyWordNumber: '', // 正在输入的富文本字数
      tinymceMax: 1000, // 富文本限制的最大字符串
      tinymceMin: 10, // 富文本限制的最小字符串
      uploadAction: yufp.util.addTokenInfo(backend.fileService + '/api/file/provider/uploadfile')// 附件上传地址
      // uploadAction: backend.fileService + '/api/file/provider/uploadfile'// 附件上传地址
    };
  },
  computed: {
    ...mapGetters([
      'userId'
    ]),
    selectOrg () {
      var orgIds = [];
      Object.keys(this.selectOrgMap).forEach((key, item) => {
        orgIds.push(key);
      });
      return orgIds;
    },
    selectRole () {
      var roleIds = [];
      Object.keys(this.selectRoleMap).forEach((key, item) => {
        roleIds.push(key);
      });
      return roleIds;
    },
    selectRoleNames () {
      var roleNames = [];
      Object.keys(this.selectRoleMap).forEach((key, item) => {
        roleNames.push(this.selectRoleMap[key]);
      });
      return roleNames;
    }
  },
  mounted () {
    this.readSts = lookup.find('READ_STS', false);
    this.fileListInfo = [];
    if (this.noticeId) {
      this.formTit = this.formTits[1];
      this.getNotice();
    } else {
      this.$nextTick(function () {
        this.$refs.refForm.resetFields();
        this.formdata.noticeLevel = 'N';
        this.formdata.isTop = '02';
      });
    }
  },
  methods: {
    checkTiny (rule, value, callback) {
      if (!this.tinyWordNumber) {
        callback(new Error(`${this.$t('notice.qsrggnr')}`));
      } else if (this.tinyWordNumber < this.tinymceMin) {
        var errorMessage = this.$store.state.app.language === 'en' ? `${this.$t('notice.zssr')} ${this.tinymceMin} ${this.$t('notice.zfc')}` :
          `${this.$t('notice.zssr')}${this.tinymceMin}${this.$t('notice.zfc')}`;
        callback(new Error(errorMessage));
      } else if (this.tinyWordNumber > this.tinymceMax) {
        var errorMessage = this.$store.state.app.language === 'en' ? `${this.$t('notice.zdsr')} ${this.tinymceMax} ${this.$t('notice.zfc')}` :
          `${this.$t('notice.zdsr')}${this.tinymceMax}${this.$t('notice.zfc')}`;
        callback(new Error(errorMessage));
      } else {
        callback();
      }
    },
    checkActiveDate(rule, value, callback) {
      if (!this.formdata.activeDate) {
        this.formdata.topActiveDate = '';
        return callback(new Error(`${this.$t('notice.qsxzyxq')}`))
      }
      if (new Date(this.formdata.topActiveDate) > new Date(this.formdata.activeDate)) {
        return callback(new Error(`${this.$t('notice.bndy')}`))
      }
      callback()
    },
    countValid (val) {
      if (val) {
        this.tinyWordNumber = val;
      } else {
        this.tinyWordNumber = val;
      }
      this.$refs.refForm.validateField('context');
    },
    deleteFileFn (file) {
      this.fileList.forEach((item, index) => {
        if (item.filePath === file.filePath) {
          this.fileList.splice(index, 1);
        }
      });
    },
    loadNumberFn (val) {
      this.loadFileNum = val;
    },
    uploadedFn (fileItem, num) {
      fileItem.icon && delete fileItem.icon;
      this.fileList.push(fileItem);
    },
    getTableSelectedFn (tableData) {
      for (var i = 0; i < tableData.length; i++) {
        if (this.selectRole.indexOf(tableData[i].roleId) > -1) {
          this.$refs.rolesTable.toggleRowSelection(tableData[i], true);
        }
      }
    },
    saveRoleFn () {
      var selections = this.$refs.rolesTable.selections;
      var ids = [];
      var names = [];
      selections.forEach(item => {
        ids.push(item.roleId);
        names.push(item.roleName);
      });
      this.formdata.reciveRoleId = ids.join(',');
      this.formdata.reciveRoleIdNames = names.join(',');
      this.dialogVisible = false;
    },
    searchRoleFn: function () {
      this.$refs.rolesTable.remoteData({keyWord: this.roleKeyWord});
    },
    cancelRoleFn () {
      this.dialogVisible = false;
    },
    selectRoleFn () {
      this.dialogVisible = true;
    },
    clearRoleFn() {
      this.formdata.reciveRoleId = '';
      this.formdata.reciveRoleIdNames = '';
      this.$refs.rolesTable.clearSelection();
    },
    cancleFn () {
      this.$router.back();
      this.$router.removeTab(this.$route.path);
    },
    getSelectIds (ids) {
      this.formdata.reciveOrgId = ids.join(',');
    },
    getNotice () {
      var _this = this;
      this.$request({
        method: 'POST',
        url: backend.appOcaService + `/api/adminsmnotice/info/${_this.noticeId}`,
        data: {}
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$refs.refForm.resetFields();
          clone(data, _this.formdata);
          _this.selectOrgMap = _this.formdata.reciveOrgMap;
          _this.selectRoleMap = _this.formdata.reciveRoleMap;
          _this.fileList = _this.formdata.fileInfoFormList;
          _this.fileListInfo = extend([], _this.formdata.fileInfoFormList);
          _this.formdata.reciveRoleIdNames = _this.selectRoleNames.join(',');
        } else {
          _this.$message({
            message: message || this.$t('notice.bcsb'),
            type: 'error'
          });
        }
      });
    },
    saveFn () {
      var _this = this;
      var validate = false;
      _this.formdata.fileInfoFormList = _this.fileList;
      if (this.loadFileNum !== this.fileList.length) {
        this.$message({ message: _this.$t('notice.fjwscw'), type: 'error' });
        return;
      }
      _this.$refs.refForm.validate(function (valid) {
        validate = valid;
      });
      if (!validate) {
        return;
      }
      if (_this.formdata.isTop === '01' && (_this.formdata.topActiveDate === '' || _this.formdata.topActiveDate === undefined)) {
        _this.$message({
          message: _this.$t('notice.qxzzdjsq'),
          type: 'warning'
        });
        return;
      }
      if (_this.formdata.reciveOrgId && _this.formdata.reciveOrgId instanceof Array) {
        _this.formdata.reciveOrgId = _this.formdata.reciveOrgId.join(',');
      }
      var url = _this.formdata.noticeId ? '/api/adminsmnotice/update' : '/api/adminsmnotice/add';
      var uuidNum = uuid(); //测试时定义在点击函数时，实际开发中应该为固定时间段生成同一个随机数上送。
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + url,
        data: _this.formdata,
        isCrypto: true, // 加密
        isRepeat: false, //报文防重
        nonce : uuidNum //报文防重验证，如果不上送随机数则是技术防重，如果开发人员上送随机数则是技术及业务防重
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$message({
            message: _this.$t('notice.bccg'),
            type: 'success'
          });
          _this.$router.back();
          _this.$router.removeTab(this.$route.path);
          yufp.globalEventBus.$emit('addNoticeFinish');
        } else {
          _this.$message({
            message: message || this.$t('notice.bcsb'),
            type: 'error'
          });
        }
      });
    }
  }
};
</script>
