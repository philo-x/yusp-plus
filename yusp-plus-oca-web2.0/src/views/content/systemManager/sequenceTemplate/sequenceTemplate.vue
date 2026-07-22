<template>
  <div class="sequence-template">
    <!--<yu-panel :title="$t('sequenceTemp.xlhmb')" show-search-input @search="searchMsgData" :placeholder="$t('base.gjz')">-->
    <yu-panel :title="$t('sequenceTemp.xlhmb')" :placeholder="$t('base.gjz')">
      <template slot="right">
        <yu-toolBar>
          <yu-button @click="openAddFn">{{ $t('base.xz') }}</yu-button>
          <!--<yu-button @click="createSeqFn">{{ $t('sequenceTemp.scxlh') }}</yu-button>
          <yu-button @click="deleteSeqFn">{{ $t('sequenceTemp.scxl') }}</yu-button>
          <yu-button @click="resetSeqFn">{{ $t('sequenceTemp.czxl') }}</yu-button>-->
        </yu-toolBar>
      </template>
      <template slot="filter">
        <yu-xform v-model="formdata" related-table-name="seqconfigTable" form-type="search" label-width="100px">
          <yu-xform-group :column="4">
            <yu-xform-item name="seqId" :label="$t('sequenceTemp.xlid')" :placeholder="$t('sequenceTemp.xlid')"></yu-xform-item>
            <yu-xform-item name="seqName" :label="$t('sequenceTemp.xlmc')" :placeholder="$t('sequenceTemp.xlmc')"></yu-xform-item>
          </yu-xform-group>
        </yu-xform>
      </template>
      <!--<yu-xtable request-type="POST" ref="seqconfigTable" selection-type="checkbox" :data-url="seqconfigTableUrl" row-number :max-height="frameHeight - 205">-->
      <yu-xtable request-type="POST" ref="seqconfigTable" :data-url="seqconfigTableUrl" row-number :max-height="frameHeight - 205">
        <yu-xtable-column prop="seqId" :label="$t('sequenceTemp.xlid')" width="100px">
          <!-- <template slot-scope="scope">
            <a class="underline" @click="viewMsgDetails(scope.row)">{{ scope.row.seqId }}</a>
          </template> -->
        </yu-xtable-column>
        <yu-xtable-column prop="seqName" :label="$t('sequenceTemp.xlmc')" min-width="220px"></yu-xtable-column>
        <yu-xtable-column prop="startvalue" :label="$t('sequenceTemp.ksz')" width="110px" data-code="MESSAGE_LEVEL"></yu-xtable-column>
        <yu-xtable-column prop="maximumvalue" :label="$t('sequenceTemp.zdz')" width="110px" data-code="MESSAGE_TYPE"></yu-xtable-column>
        <yu-xtable-column prop="incrementvalue" :label="$t('sequenceTemp.zzz')" width="110px"></yu-xtable-column>
        <!--<yu-xtable-column prop="isCycle" :label="$t('sequenceTemp.sfxh')" min-width="80px">
          <template slot-scope="scope">
            {{ scope.row.isCycle | filteTableData }}
          </template>
        </yu-xtable-column>-->
        <yu-xtable-column prop="cachevalue" :label="$t('sequenceTemp.hcz')" min-width="80px"></yu-xtable-column>
        <yu-xtable-column prop="seqTemplet" :label="$t('sequenceTemp.xlmb')" min-width="260px"></yu-xtable-column>
        <yu-xtable-column prop="seqPlace" :label="$t('sequenceTemp.xlws')" min-width="120px"></yu-xtable-column>
        <yu-xtable-column prop="zeroFill" :label="$t('sequenceTemp.bzwsbq')" min-width="120px">
          <template slot-scope="scope">
            {{ scope.row.zeroFill | filteTableData }}
          </template>
        </yu-xtable-column>
        <yu-xtable-column prop="seqCreate" :label="$t('sequenceTemp.yscxl')" min-width="100px">
          <template slot-scope="scope">
            {{ scope.row.seqCreate | filteTableData }}
          </template>
        </yu-xtable-column>
        <yu-xtable-column class="handle-box" :label="$t('base.czuo')" width="160px" fixed="right">
          <template slot-scope="scope">
            <yu-button-drop set-index="0" :show-length="2" type="text">
              <yu-button type="text" @click="openDetailFn(scope.row)">{{ $t('base.xg') }}</yu-button>
              <yu-button v-norepeat.disabled type="text" @click="deleteSeqFn(scope.row)">{{ $t('sequenceTemp.scxl') }}</yu-button>
              <!--<yu-button v-norepeat.disabled type="text" @click="deleteFn(scope.row)">{{ $t('base.sc') }}</yu-button>-->
              <yu-button @click="createSeqFn(scope.row)">{{ $t('sequenceTemp.scxlh') }}</yu-button>
            </yu-button-drop>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
    <!-- 编辑(新增/修改)系统参数弹窗 -->
    <yu-xdialog :title="dialogTitle" :visible.sync="isShowEditMsgDialog">
      <yu-collapse v-model="activeNames">
        <yu-collapse-item :title="$t('sequenceTemp.mbjcxxpz')" name="1">
          <yu-xform ref="editMsgForm" v-model="editMsgFormData" :form-type="msgFormType" :rules="rules" label-width="110px" :disabled="formDisabled">
            <yu-xform-group>
              <yu-xform-item name="seqId" :disabled="false" :label="$t('sequenceTemp.xlid')" :placeholder="$t('sequenceTemp.xlid')"></yu-xform-item>
              <yu-xform-item name="seqName" :label="$t('sequenceTemp.xlmc')" :placeholder="$t('sequenceTemp.xlmc')"></yu-xform-item>
              <yu-xform-item name="startvalue" :label="$t('sequenceTemp.ksz')" :placeholder="$t('sequenceTemp.ksz')"></yu-xform-item>
              <yu-xform-item name="maximumvalue" :label="$t('sequenceTemp.zdz')" :placeholder="$t('sequenceTemp.zdz')"></yu-xform-item>
              <yu-xform-item name="incrementvalue" :label="$t('sequenceTemp.zzz')" :placeholder="$t('sequenceTemp.zzz')"></yu-xform-item>
              <yu-xform-item name="cachevalue" :label="$t('sequenceTemp.hcz')" :placeholder="$t('sequenceTemp.hcz')"></yu-xform-item>
              <yu-xform-item name="isCycle" :label="$t('sequenceTemp.sfxh')" ctype="select" data-code="YESNO" :placeholder="$t('sequenceTemp.sfxh')"></yu-xform-item>
            </yu-xform-group>
          </yu-xform>
        </yu-collapse-item>
        <yu-collapse-item :title="$t('sequenceTemp.mbgspz')" name="2">
          <yu-xtable request-type="POST" :data="tableData" :pageable="false" style="width: 100%" @row-dblclick="dbclickFn">
            <yu-xtable-column prop="variateName" :label="$t('sequenceTemp.blmc')"></yu-xtable-column>
            <yu-xtable-column prop="variateValue" :label="$t('sequenceTemp.blz')"></yu-xtable-column>
            <yu-xtable-column prop="variateDes" :label="$t('sequenceTemp.sm')"></yu-xtable-column>
          </yu-xtable>
          <div style="margin-top: 20px">
            <yu-xform ref="otherForm" v-model="otherFormData" :disabled="formAllDisabled" :form-type="msgFormType" :rules="otherRules" label-width="130px">
              <yu-xform-group>
                <yu-xform-item name="seqTemplet" :label="$t('sequenceTemp.xlmb')" ctype="textarea" colspan="24" :placeholder="$t('sequenceTemp.sjblbgsxksxlmbgspz')"></yu-xform-item>
                <yu-xform-item name="seqPlace" :label="$t('sequenceTemp.xlws')" :placeholder="$t('sequenceTemp.xlws')"></yu-xform-item>
                <yu-xform-item name="zeroFill" :label="$t('sequenceTemp.bzwsbq')" ctype="select" data-code="ZERO_FILL" :placeholder="$t('sequenceTemp.bzwsbq')"></yu-xform-item>
              </yu-xform-group>
            </yu-xform>
          </div>
        </yu-collapse-item>
      </yu-collapse>
      <div slot="footer" class="yu-grpButton">:
        <yu-button v-norepeat.disabled @click="saveFn" key="save">{{ $t('base.bc') }}</yu-button>
        <yu-button @click="isShowEditMsgDialog=false">{{ $t('base.qx') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>

<script>
import { extend } from '@/utils';
/* eslint-disable */
export default {
  data() {
    const me = this;
    const validateNum09 = function (rule, value, callback) {
      if (value) {
        if (!isNaN(value)) {
          if (value < 0 || value > 999999999) {
            callback(new Error(me.$t('sequenceTemp.qsrdsz')));
          } else {
            callback();
          }
        } else {
          callback(new Error(me.$t('sequenceTemp.qsrsz')));
        }
      } else {
        callback(new Error(me.$t('sequenceTemp.qsrsz')));
      }
    };
    const seqIdStr = function (rule, value, callback) {
      const reg = /^[A-Z]{1}[A-Z0-9_]+$/;
      if (value && reg.test(value)) {
        callback();
      } else if (value && !reg.test(value)) {
        callback(new Error(me.$t('sequenceTemp.znydxzmszxhxzcqszmznsdxzm')));
      } else {
        callback();
      }
    };
    const validateNum19 = function (rule, value, callback) {
      if (value) {
        if (!isNaN(value)) {
          if (value < 1 || value > 999999999) {
            callback(new Error(me.$t('sequenceTemp.qsrdsz')));
          } else {
            callback();
          }
        } else {
          callback(new Error(me.$t('sequenceTemp.qsrsz')));
        }
      } else {
        callback(new Error(me.$t('sequenceTemp.qsrsz')));
      }
    };
    const validateNum099 = function (rule, value, callback) {
      if (value) {
        if (!isNaN(value)) {
          if (value <= 1 || value > 99) {
            callback(new Error(me.$t('sequenceTemp.qsrdszmax99')));
          } else {
            callback();
          }
        } else {
          callback(new Error(me.$t('sequenceTemp.qsrdszmax99')));
        }
      } else {
        callback(new Error(me.$t('sequenceTemp.qsrdszmax99')));
      }
    };
    return {
      formDisabled: false,
      formAllDisabled: false,
      formdata: {},
      seqconfigTableUrl: backend.appOcaService + '/api/sequenceconfig/',
      isShowEditMsgDialog: false,
      viewType: 'add',
      dialogTitle: this.$t('sequenceTemp.mbxzym'),
      activeNames: ['1', '2'],
      msgFormType: 'edit', // edit details
      rules: {
        seqId: [
          { required: true, message: this.$t('base.btx'), trigger: 'blur' },
          { validator: seqIdStr, trigger: 'blur' }
        ],
        seqName: [
          { required: true, message: this.$t('base.btx'), trigger: 'blur' }
        ],
        startvalue: [
          { required: true, message: this.$t('base.btx'), trigger: 'blur' },
          { validator: validateNum09, trigger: 'blur' }
        ],
        maximumvalue: [
          { required: true, message: this.$t('base.btx'), trigger: 'blur' },
          { validator: validateNum19, trigger: 'blur' }
        ],
        incrementvalue: [
          { required: true, message: this.$t('base.btx'), trigger: 'blur' },
          { validator: validateNum19, trigger: 'blur' }
        ],
        isCycle: [
          { required: true, message: this.$t('base.btx'), trigger: 'blur' }
        ],
        cachevalue: [
          { required: true, message: this.$t('base.btx'), trigger: 'blur' },
          { validator: validateNum099, trigger: 'blur' }
        ]
      },
      otherRules: {
        seqTemplet: [
          { required: true, message: this.$t('base.btx'), trigger: 'blur' }
        ],
        seqPlace: [
          { required: true, message: this.$t('base.btx'), trigger: 'blur' },
          { validator: validateNum099, trigger: 'blur' }
        ],
        zeroFill: [
          { required: true, message: this.$t('base.btx'), trigger: 'blur' }
        ]
      },
      tableData: [
        { variateName: this.$t('sequenceTemp.nf'), variateValue: 'yyyy', variateDes: this.$t('sequenceTemp.nf') },
        { variateName: this.$t('sequenceTemp.yf'), variateValue: 'MM', variateDes: this.$t('sequenceTemp.yf') },
        { variateName: this.$t('sequenceTemp.yft'), variateValue: 'dd', variateDes: this.$t('sequenceTemp.yft') },
        { variateName: this.$t('sequenceTemp.xls'), variateValue: 'SEQ', variateDes: this.$t('sequenceTemp.xls') }
      ]
    }
  },
  mounted() {
    
  },
  methods: {
    // 关键字快捷搜索
    searchMsgData(params) {
      this.$refs.seqconfigTable.remoteData({ keyWord: params.value });
    },
    // 新增
    openAddFn() {
      this.viewType = 'add';
      this.dialogTitle = this.$t('sequenceTemp.mbxzym');
      this.formAllDisabled = false;
      this.formDisabled = false;
      this.isShowEditMsgDialog = true;
      this.msgFormType = 'edit';
      this.$nextTick(() => {
        this.$refs.editMsgForm.resetFields();
        this.$refs.otherForm.resetFields();
      });
    },
    // 打开修改页面
    openUpdateFn(row) {
      // if (this.$refs.seqconfigTable.selections.length == 1) {
        // const row = this.$refs.seqconfigTable.selections[0];
        // this.formDisabled = true;
        if (row.seqCreate == 'N') {
          // 未生成序列，可编辑
          this.formDisabled = false;
        }
        this.viewType = 'edit';
        this.dialogTitle = this.$t('sequenceTemp.mbxgym');
        this.formAllDisabled = false;
        this.isShowEditMsgDialog = true;

        this.$nextTick(() => {
          extend(this.$refs.form.formModel, row);
          extend(this.$refs.otherForm.formModel, row);

          this.$refs.form.formModel.startvalue = String(row.startvalue);
          this.$refs.form.formModel.maximumvalue = String(row.maximumvalue);
          this.$refs.form.formModel.incrementvalue = String(row.incrementvalue);
          this.$refs.form.formModel.cachevalue = String(row.cachevalue);
          this.$refs.otherForm.formModel.seqPlace = String(row.seqPlace);
        });
      // } else {
      //   this.$message({ message: this.$t('sequenceTemp.qxzytxlsj'), type: 'warning' });
      //   return false;
      // }
    },
    // 双击列表行配置模板
    dbclickFn(row) {
      const value = `{${row.variateValue}}`;
      this.otherFormData.seqTemplet === undefined && (this.otherFormData.seqTemplet = '');
      this.otherFormData.seqTemplet += value;
    },
    // 新增或者修改保存
    saveFn() {
      let validate = false;
      this.$refs.editMsgForm.validate(valid => {
        validate = valid;
      });
      this.$refs.otherForm.validate(valid => {
        validate = validate && valid;        
      });
      if (!validate) {
        return;
      }
      const url = this.viewType === 'add' ? '/api/sequenceconfig/' : '/api/sequenceconfig/update';
      const data = Object.assign({}, this.otherFormData, this.editMsgFormData);
      this.$request({
        method: 'POST',
        url: backend.appOcaService + url,
        data: data
      }).then(({code, message, data}) => {
        if (code === '0000') {
          this.$message({
            message: this.$t('base.bccg'),
            type: 'success'
          });
          this.$refs.seqconfigTable.remoteData();
          this.isShowEditMsgDialog = false;
        } else {
          this.$message({ message: message, type: 'error' });
        }
      });
    },
    // 删除
    deleteFn() {
      if (this.$refs.seqconfigTable.selections.length == 1) {
        const id = this.$refs.seqconfigTable.selections[0].id;
        const vm = this;
        this.$confirm(this.$t('sequenceTemp.cczjscgsjjlsfjx'), this.$t('base.ts'), {
          confirmButtonText: this.$t('base.qd'),
          cancelButtonText: this.$t('base.qx'),
          type: 'warning'
        }).then(() => {
          this.$request({
            method: 'POST',
            url: backend.appOcaService + '/api/sequenceconfig/delete/' + id
          }).then(({code, message, data}) => {
            if (code == 0) {
              if (data.code == 30100025 || data.code == 30100026) {
                this.$message({ message: message, type: 'success' });
                this.$refs.seqconfigTable.remoteData();
              } else {
                this.$message({ message: message, type: 'error' });
              }
            }
          });
        });
      } else {
        this.$message({ message: this.$t('sequenceTemp.qxzytyscdsj'), type: 'warning' });
        return false;
      }
    },
    // 生成序列
    createSeqFn(row) {
      /* if (this.$refs.seqconfigTable.selections.length == 1) {
        const seqId = this.$refs.seqconfigTable.selections[0].seqId; */
        const vm = this;
        this.$request({
          method: 'POST',
          url: backend.appOcaService + '/api/sequenceconfig/createsequence/' + row.seqId
        }).then(({code, message, data}) => {
          if (code == 0) {
              vm.$message({ message: message, type: 'success' });
              vm.$refs.seqconfigTable.remoteData();
          } else {
              vm.$message({ message: message, type: 'error' });
          }
        })
      /* } else {
        this.$message({ message: this.$t('sequenceTemp.qxzytycjxldxlsj'), type: 'warning' });
        return false;
      } */
    },
    // 删除序列
    deleteSeqFn(row) {
      /* if (this.$refs.seqconfigTable.selections.length == 1) {
        const seqId = this.$refs.seqconfigTable.selections[0].seqId; */
        const _this = this;
        this.$request({
          method: 'POST',
          url: backend.appOcaService + '/api/sequenceconfig/deletesequence/' + row.seqId
        }).then(({code, message, data}) => {
          if (code == 0) {
            _this.$message({ message: message, type: 'success' });
            _this.$refs.seqconfigTable.remoteData();
          } else {
            _this.$message({ message: message, type: 'error' });
          }
        })
      /* } else {
        this.$message({ message: this.$t('sequenceTemp.qxzytyscdxlsj'), type: 'warning' });
        return false;
      } */
    },
    // 重置序列
    resetSeqFn() {
      if (this.$refs.seqconfigTable.selections.length == 1) {
        const seqId = this.$refs.seqconfigTable.selections[0].seqId;
        const _this = this;
        this.$request({
          method: 'POST',
          url: backend.appOcaService + '/api/sequenceconfig/recreatesequence/' + seqId
        }).then(({code, message, data}) => {
          if (code == 0) {
            _this.$message({ message: message, type: 'success' });
            _this.$refs.seqconfigTable.remoteData();
          } else {
            _this.$message({ message: message, type: 'error' });
          }
        })
      } else {
        this.$message({ message: this.$t('sequenceTemp.qxzytyzzdxlsj'), type: 'warning' });
        return false;
      }
    },
    // 查看模板
    openDetailFn(row) {
      this.viewType = 'detail';
      // this.formDisabled = true;
      if (row.seqCreate == 'N') {
        // 未生成序列，可编辑
        this.formDisabled = false;
      }
      // this.formAllDisabled = true;
      this.dialogTitle = this.$t('sequenceTemp.mbxgym');
      this.isShowEditMsgDialog = true;
      this.$nextTick(() => {
        this.$request({
          method: 'POST',
          url: backend.appOcaService + '/api/sequenceconfig/getdetail/' + row.id
        }).then(({code, message, data}) => {
          if (code == 0) {
            extend(this.editMsgFormData, row);
            extend(this.otherFormData, row);
          } else {
            this.$message({ message: message, type: 'error' });
          }
        })
      });
    },
  },
  filters: {
    //表格值过滤
    filteTableData(val) {
      switch (val) {
        case '01': return '是';break;
        case '02': return '否';break;
        case 'Y': return '是';break;
        case 'N': return '否';break;
      }
    }
  }
}
</script>
