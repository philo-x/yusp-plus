<!--
  @Created by zhuly8@yusys.com.cn 2021-2-20
  @updated by
  @description 日志管理
-->
<template>
  <div id="logManager">
    <yu-panel :title="$t('logManager.rzgl')">
      <template slot="right">
        <yu-toolBar>
          <yu-button @click="deleteLogInfo">{{ $t('logManager.sc') }}</yu-button>
          <yufp-excel-export :export-url="excelExportUrl" :exportParam="exportParam" title="导出日志"></yufp-excel-export>
        </yu-toolBar>
      </template>
      <template slot="filter">
        <yu-xform v-model="formdata" related-table-name="logtable" form-type="search">
          <yu-xform-group :column="4">
            <yu-xform-item :label="$t('logManager.rzlx')" :placeholder="$t('logManager.qxz')" ctype="select" name="logTypeId" data-code="LOG_TYPE" @change="selectChange"></yu-xform-item>
            <yu-xform-item :label="$t('logManager.czyh')" :placeholder="$t('logManager.qsr')" ctype="input" name="userName" @blur="inputBlur"></yu-xform-item>
            <yu-xform-item :label="$t('logManager.czdx')" :placeholder="$t('logManager.qsr')" ctype="input" name="operObjId" @blur="inputBlur"></yu-xform-item>
            <yu-xform-item :label="$t('logManager.czsjc')" :placeholder="$t('logManager.qxz')" ctype="datepicker" name="beginTime" value-format="yyyy-MM-dd" @change="selectChange"></yu-xform-item>
            <yu-xform-item :label="$t('logManager.czsjz')" :placeholder="$t('logManager.qxz')" ctype="datepicker" name="endTime" value-format="yyyy-MM-dd" @change="selectChange"></yu-xform-item>
          </yu-xform-group>
        </yu-xform>
      </template>
      <yu-xtable request-type="POST" ref="logtable" row-number :data-url="dataUrl" selection-type="checkbox" reserve-selection row-key="logId" @selection-change="getSelectedRow">
        <yu-xtable-column :label="$t('logManager.czzxm')" prop="userName"></yu-xtable-column>
        <yu-xtable-column :label="$t('logManager.czzjg')" prop="orgName"> </yu-xtable-column>
        <yu-xtable-column :label="$t('logManager.rzlx')" prop="logTypeId" data-code="LOG_TYPE"></yu-xtable-column>
        <yu-xtable-column :label="$t('logManager.czdx')" prop="operObjId"></yu-xtable-column>
        <yu-xtable-column :label="$t('logManager.dl')" prop="loginIp" width="300px"></yu-xtable-column>
        <yu-xtable-column :label="$t('logManager.czsj')" prop="operTime" width="160px"></yu-xtable-column>
        <yu-xtable-column :label="$t('logManager.cznr')" prop="content" width="460px"></yu-xtable-column>
      </yu-xtable>
    </yu-panel>
  </div>
</template>

<script>
import {lookup} from '@/utils';
import YufpExcelExport from '@/components/widgets/YufpExcelExport';
import YufpExcelImport from '@/components/widgets/YufpExcelImport';

lookup.reg('LOG_TYPE');
export default {
  components: { YufpExcelExport, YufpExcelImport },
  data () {
    return {
      formdata: {},
      pickerStartOptions: {
        disabledDate (time) {
          if (this.formdata.endTime) {
            return (
              time.getTime() > new Date(this.formdata.endTime).getTime()
            );
          }
        }
      },
      pickerEndOptions: {
        disabledDate (time) {
          if (this.formdata.beginTime) {
            return (
              time.getTime() < new Date(this.formdata.beginTime).getTime()
            );
          }
        }
      },
      dialogVisible: false,
      checkbox: true,
      dataUrl: backend.appOcaService + '/api/monitor/auditlogdata/list',
      excelExportUrl: backend.appOcaService + '/api/monitor/translatefile', // excel导出
      exportParam: { // 文件导出参数
        logTypeId: '', // 日志类型
        userName: '', // 操作用户
        operObjId: '', // 操作对象ID
        beginTime: '',
        endTime: '',
        logIds: []
      }
    };
  },
  methods: {
    selectChange () { // 获取日志类型与日期
      this.exportParam.logTypeId = this.formdata.logTypeId;
      this.exportParam.beginTime = this.formdata.beginTime;
      this.exportParam.endTime = this.formdata.endTime;
    },
    inputBlur () { // 获取操作用户与对象
      this.exportParam.userName = this.formdata.userName;
      this.exportParam.operObjId = this.formdata.operObjId;
    },
    getSelectedRow () { // 获取选中行
      this.exportParam.logIds = [];
      if (this.$refs.logtable.selections.length !== 0) {
        for (var item of this.$refs.logtable.selections) {
          this.exportParam.logIds.push(item.logId);
        }
      }
    },
    deleteLogInfo () { // 删除日志信息
      var _this = this;
      var selects = _this.$refs.logtable.selections;
      if (selects.length == 0) {
        _this.$message(_this.$t('logManager.qzsxzytsj'), _this.$t('logManager.jg'));
        return false;
      }
      _this.$confirm(_this.$t('logManager.sfqdscsxsj'), _this.$t('logManager.ts'), {
        confirmButtonText: _this.$t('logManager.qd'),
        cancelButtonText: _this.$t('logManager.qx'),
        type: 'warning',
        callback (action) {
          if (action === 'confirm') {
            _this.$request({
              url: backend.appOcaService + '/api/monitor/auditlogdata/batchdelete',
              method: 'post',
              data: selects
            }).then(({code, message, data}) => {
              if (code === '0000') {
                _this.$message({
                  message: _this.$t('logManager.scjlcg'),
                  type: 'success'
                });
                _this.$refs.logtable.remoteData();
              } else {
                _this.$message({
                  message: message || _this.$t('logManager.scjlsb'),
                  type: 'warning'
                });
              }
            });
          }
        }
      });
    },
    // 导入成功更新表格
    successImport () {
      this.$refs.logtable.remoteData();
    }
  }
};
</script>
