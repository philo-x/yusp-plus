<!--
  @Created by gaoxin5@yusys.com.cn 2021-03-01
  @updated by
  @description 定时任务
-->
<template>
  <div class="data-schedule">
    <yu-panel :title="$t('schedule.dsrw')" show-search-input :collapse-hide="false" :placeholder="$t('schedule.beanmc')" @search="fuzzyQueryFn" ref="panelRef">
      <template slot="right">
        <yu-button-drop :showLength="5">
          <yu-button @click="scheduleLogFn" v-norepeat.disabled>{{ $t('schedule.rzlb') }}</yu-button>
          <yu-button v-if="checkCtrl('add')" @click="scheduleAddFn">{{ $t('schedule.xz') }}</yu-button>
          <yu-button v-if="checkCtrl('pause')" @click="schedulePauseFn()" v-norepeat.disabled>{{ $t('schedule.zt') }}</yu-button>
          <yu-button v-if="checkCtrl('resume')" @click="scheduleResumeFn()" v-norepeat.disabled>{{ $t('schedule.hf') }}</yu-button>
          <yu-button v-if="checkCtrl('delete')" @click="scheduleDelFn()" v-norepeat.disabled>{{ $t('schedule.sc') }}</yu-button>
          <yu-button v-if="checkCtrl('run')" @click="scheduleRunFn()" v-norepeat.disabled>{{ $t('schedule.ljzx') }}</yu-button>
        </yu-button-drop>
      </template>
      <yu-xtable request-type="POST" :data-url="serviceUrl" row-number selection-type="checkbox" ref="scheduleTable">
        <yu-xtable-column prop="beanName" :label="$t('schedule.beanmc')"></yu-xtable-column>
        <yu-xtable-column prop="params" :label="$t('schedule.cs')"></yu-xtable-column>
        <yu-xtable-column prop="cronExpression" :label="$t('schedule.bds')"></yu-xtable-column>
        <yu-xtable-column prop="remark" :label="$t('schedule.bz')"></yu-xtable-column>
        <yu-xtable-column :label="$t('schedule.dszx')">
          <template slot-scope="scope">
            <yu-tag v-if="scope.row.status == 1" type="danger" size="small">{{ $t('schedule.zt') }}</yu-tag>
            <yu-tag v-if="scope.row.status == 0" type="success" size="small">{{ $t('schedule.hf') }}</yu-tag>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('schedule.cz')" width="120">
          <template slot-scope="scope">
            <yu-button-drop set-index="0" :show-length="2" type="text">
              <yu-button v-if="checkCtrl('edit')" @click="scheduleEditFn(scope.row)" type="text">{{ $t('schedule.xg') }}</yu-button>
              <yu-button v-if="checkCtrl('delete')" @click="scheduleDelFn(scope.row)" type="text" v-norepeat.disabled>{{ $t('schedule.sc') }}</yu-button>
              <yu-button v-if="checkCtrl('pause') && scope.row.status == 0" @click="schedulePauseFn(scope.row)" type="text" v-norepeat.disabled>{{ $t('schedule.zt') }}</yu-button>
              <yu-button v-if="checkCtrl('resume') && scope.row.status == 1" @click="scheduleResumeFn(scope.row)" type="text" v-norepeat.disabled>{{ $t('schedule.hf') }}</yu-button>
              <yu-button v-if="checkCtrl('run')" @click="scheduleRunFn(scope.row)" type="text" v-norepeat.disabled>{{ $t('schedule.ljzx') }}</yu-button>
            </yu-button-drop>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
    <!-- 定时任务dialog -->
    <yu-xdialog :title="dialogTitle" :visible.sync="dialogVisible" size="tiny">
      <yu-xform ref="scheduleForm" label-width="148px" v-model="scheduleFormData" :rules="scheduleRules">
        <yu-xform-group :column="1">
          <yu-xform-item :label="$t('schedule.beanmc')" name="beanName" :placeholder="$t('schedule.qsrmc')"></yu-xform-item>
          <yu-xform-item :label="$t('schedule.cs')" name="params" :placeholder="$t('schedule.cs')"></yu-xform-item>
          <yu-xform-item :label="$t('schedule.bds')" name="cronExpression" :placeholder="$t('schedule.qsrbds')"></yu-xform-item>
          <yu-xform-item :label="$t('schedule.bz')" name="remark" :placeholder="$t('schedule.bz')" maxlength="30" show-word-limit></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
      <div slot="footer" class="dialog-footer" key="add">
        <yu-button type="primary" v-norepeat.disabled="saveDisabled" @click="saveFn">{{ $t('schedule.bc') }}</yu-button>
        <yu-button @click="dialogVisible = false">{{ $t('schedule.qx') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import { clone, extend } from '@/utils'
import { validator } from "@/utils/validate"
export default {
  data () {
    const checkCron = (rule, value, callback) => {
      if (!value) {
        return callback(new Error(this.$t('schedule.btx')));
      }
      const regEx = /^\s*($|#|\w+\s*=|(\?|\*|(?:[0-5]?\d)(?:(?:-|\/|\,)(?:[0-5]?\d))?(?:,(?:[0-5]?\d)(?:(?:-|\/|\,)(?:[0-5]?\d))?)*)\s+(\?|\*|(?:[0-5]?\d)(?:(?:-|\/|\,)(?:[0-5]?\d))?(?:,(?:[0-5]?\d)(?:(?:-|\/|\,)(?:[0-5]?\d))?)*)\s+(\?|\*|(?:[01]?\d|2[0-3])(?:(?:-|\/|\,)(?:[01]?\d|2[0-3]))?(?:,(?:[01]?\d|2[0-3])(?:(?:-|\/|\,)(?:[01]?\d|2[0-3]))?)*)\s+(\?|\*|(?:0?[1-9]|[12]\d|3[01])(?:(?:-|\/|\,)(?:0?[1-9]|[12]\d|3[01]))?(?:,(?:0?[1-9]|[12]\d|3[01])(?:(?:-|\/|\,)(?:0?[1-9]|[12]\d|3[01]))?)*)\s+(\?|\*|(?:[1-9]|1[012])(?:(?:-|\/|\,)(?:[1-9]|1[012]))?(?:L|W)?(?:,(?:[1-9]|1[012])(?:(?:-|\/|\,)(?:[1-9]|1[012]))?(?:L|W)?)*|\?|\*|(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(?:(?:-)(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?(?:,(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(?:(?:-)(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?)*)\s+(\?|\*|(?:[0-6])(?:(?:-|\/|\,|#)(?:[0-6]))?(?:L)?(?:,(?:[0-6])(?:(?:-|\/|\,|#)(?:[0-6]))?(?:L)?)*|\?|\*|(?:MON|TUE|WED|THU|FRI|SAT|SUN)(?:(?:-)(?:MON|TUE|WED|THU|FRI|SAT|SUN))?(?:,(?:MON|TUE|WED|THU|FRI|SAT|SUN)(?:(?:-)(?:MON|TUE|WED|THU|FRI|SAT|SUN))?)*)(|\s)+(\?|\*|(?:|\d{4})(?:(?:-|\/|\,)(?:|\d{4}))?(?:,(?:|\d{4})(?:(?:-|\/|\,)(?:|\d{4}))?)*))$/
      if (regEx.test(value)) {
        callback()
      } else {
        callback(new Error(this.$t('schedule.qsrzqdbds')))
      }
    };
    return {
      serviceUrl: backend.appOcaService + '/api/schedule/list', // 数据模板表单查询URL
      addUrl: backend.appOcaService + '/api/schedule/save', // 新增保存
      editUrl: backend.appOcaService + '/api/schedule/update', // 修改保存
      delUrl: backend.appOcaService + '/api/schedule/delete', // 删除
      pauseUrl: backend.appOcaService + '/api/schedule/pause', // 暂停
      resumeUrl: backend.appOcaService + '/api/schedule/resume', // 恢复
      runUrl: backend.appOcaService + '/api/schedule/run', // 立即执行
      scheduleFormData: {}, // 新增修改定时任务表单数据
      // 新增修改权限模板表单验证规则
      scheduleRules: {
        beanName: [{ required: true, message: this.$t('schedule.btx')}],
        cronExpression: [{ required: true, message: this.$t('schedule.qsrbds')}, {validator: checkCron}]
      },
      dialogVisible: false, // 表单弹框是否显示
      dialogTitle: '', // 弹框标题
      formType: '',
      saveDisabled:{ show: false }, // 保存按钮防重复提交
    };
  },

  methods: {
    remoteTableData(panelRef, tableRef, searFormVmodel) {
      // panel隐藏的时候
      if (this.$refs[panelRef].hide) {
        this.$refs[tableRef].remoteData({beanName: this.$refs[panelRef].inputVal})
      } else {
        this.$refs[tableRef].remoteData(this[searFormVmodel])
      }
    },
    remoteData() {
      this.remoteTableData('panelRef', 'scheduleTable', 'searchFormdata')
    },
    /**
    * 简洁搜索框模糊查询
    * @param dataObj.value 搜索框的值
    */
    fuzzyQueryFn (dataObj) {
      var param = {beanName: dataObj.value};
      this.$refs.scheduleTable.remoteData(param);
    },

    // 新增定时任务
    scheduleAddFn () {
      this.formType = 'add'
      this.dialogTitle = this.$t('schedule.xz')
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.scheduleForm.resetFields()
      });
    },

    // 修改定时任务
    scheduleEditFn (row) {
      // var selections = this.$refs.scheduleTable.selections;
      // if (selections.length != 1) {
      //   this.$message({ message: this.$t('schedule.qxzytjlxg'), type: 'warning' });
      //   return;
      // }
      this.formType = 'edit'
      this.dialogTitle = this.$t('schedule.xg');
      this.dialogVisible = true
      this.$nextTick(function () {
        this.$refs.scheduleForm.setFormData(row)
      })
    },

    /**
    * 保存定时任务
    * 先进行表单校验，再保存
    */
    saveFn () {
      var _this = this;
      var validate = false;
      _this.$refs.scheduleForm.validate(function (valid) {
        validate = valid;
      });
      if (!validate) {
        return;
      }
      var param = extend({}, this.scheduleFormData);
      // 判断表单数据是否存在id,有id为修改，没有为新增数据。
      var jobId = this.scheduleFormData.jobId || '';
      _this.saveDisabled.show = true;
      _this.saveDisabled = clone(_this.saveDisabled, {});
      this.$request({
        url: jobId ? _this.editUrl : _this.addUrl,
        method: 'post',
        data: param
      }).then(({code, message, data}) => {
        //处理请求成功的情况
        _this.saveDisabled.show = false;
        _this.saveDisabled = clone(_this.saveDisabled, {});
        if (code === '0000') {
          !jobId && _this.$message({ type: 'success', message: _this.$t('schedule.bccg') });
          jobId && _this.$message({ type: 'success', message: _this.$t('schedule.xgcg') });
          _this.remoteData();
          _this.dialogVisible = false;
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      })
    },

    /**
    * 删除定时任务
    */
    scheduleDelFn (row) {
      var _this = this;
      var ids = [];
      var selections = row ? [row] : this.$refs.scheduleTable.selections;
      if (!selections.length) {
        this.$message({ message: this.$t('schedule.qxzxyscddsrw'), type: 'warning' });
        return;
      }
      for (var i = 0; i < selections.length; i++) {
        ids.push(selections[i].jobId)
      }
      _this.$confirm(_this.$t('schedule.qdyscxzdjl'), _this.$t('schedule.ts'), {
        confirmButtonText: _this.$t('schedule.qd'),
        cancelButtonText: _this.$t('schedule.qx'),
        type: 'warning'
      }).then(function () {
        _this.$request({
          url: _this.delUrl,
          method: 'post',
          data: { jobIds: ids.join(',') }
        }).then(({code, message, data}) => {
        //处理请求成功的情况
          if (code === '0000') {
            _this.$message({type: 'success', message: _this.$t('schedule.sccg') });
            _this.remoteData();
          } else {
            _this.$message({ type: 'error', message: message });
          }
        })
      });
    },

    /**
    * 暂停定时任务
    */
    schedulePauseFn (row) {
      var _this = this;
      var ids = [];
      var selections = row ? [row] : this.$refs.scheduleTable.selections;
      if (!selections.length) {
        this.$message({ message: this.$t('schedule.qxzxyztddsrw'), type: 'warning' });
        return;
      }
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].status == 1) {
          this.$message({ message: this.$t('schedule.znxzhhztsj'), type: 'warning' });
          return
        }
        ids.push(selections[i].jobId)
      }
      _this.$confirm(_this.$t('schedule.qdyztxzdjl'), _this.$t('schedule.ts'), {
        confirmButtonText: _this.$t('schedule.qd'),
        cancelButtonText: _this.$t('schedule.qx'),
        type: 'warning'
      }).then(function () {
        _this.$request({
          url: _this.pauseUrl,
          method: 'post',
          data: { jobIds: ids.join(',') }
        }).then(({code, message, data}) => {
        //处理请求成功的情况
          if (code === '0000') {
            _this.$message({type: 'success', message: _this.$t('schedule.ztcg') });
            _this.remoteData();
          } else {
            _this.$message({ type: 'error', message: message });
          }
        })
      });
    },

    /**
    * 恢复定时任务
    */
    scheduleResumeFn (row) {
      var _this = this;
      var ids = [];
      var selections = row ? [row] : this.$refs.scheduleTable.selections;
      if (!selections.length) {
        this.$message({ message: this.$t('schedule.qxzxyhfddsrw'), type: 'warning' });
        return;
      }
      for (var i = 0; i < selections.length; i++) {
        if (selections[i].status == 0) {
          this.$message({ message: this.$t('schedule.znxzztztsj'), type: 'warning' });
          return
        }
        ids.push(selections[i].jobId)
      }
      _this.$confirm(_this.$t('schedule.qdyhfxzdjl'), _this.$t('schedule.ts'), {
        confirmButtonText: _this.$t('schedule.qd'),
        cancelButtonText: _this.$t('schedule.qx'),
        type: 'warning'
      }).then(function () {
        _this.$request({
          url: _this.resumeUrl,
          method: 'post',
          data: { jobIds: ids.join(',') }
        }).then(({code, message, data}) => {
        //处理请求成功的情况
          if (code === '0000') {
            _this.$message({type: 'success', message: _this.$t('schedule.hfcg') });
            _this.remoteData();
          } else {
            _this.$message({ type: 'error', message: message });
          }
        })
      });
    },

    /**
    * 立即执行定时任务
    */
    scheduleRunFn (row) {
      var _this = this;
      var ids = [];
      var selections = row ? [row] : this.$refs.scheduleTable.selections;
      if (!selections.length) {
        this.$message({ message: this.$t('schedule.qxzxyljzxddsrw'), type: 'warning' });
        return;
      }
      for (var i = 0; i < selections.length; i++) {
        ids.push(selections[i].jobId)
      }
      _this.$confirm(_this.$t('schedule.qdyljzxxzdjl'), _this.$t('schedule.ts'), {
        confirmButtonText: _this.$t('schedule.qd'),
        cancelButtonText: _this.$t('schedule.qx'),
        type: 'warning'
      }).then(function () {
        _this.$request({
          url: _this.runUrl,
          method: 'post',
          data: { jobIds: ids.join(',') }
        }).then(({code, message, data}) => {
        //处理请求成功的情况
          if (code === '0000') {
            _this.$message({type: 'success', message: _this.$t('schedule.ljzxcg') });
            _this.remoteData();
          } else {
            _this.$message({ type: 'error', message: message });
          }
        })
      });
    },

    // 日志列表
    scheduleLogFn () {
      this.$router.push('/scheduleLog');
    }
  }
}
</script>
