<!--
  @Created by panglx@yusys.com.cn 2020-12-22
  @updated by
  @description 数据权限模板配置
-->
<template>
  <div class="data-filter">
    <yu-panel :title="$t('dataFilter.sjqxmb')" show-search-input :placeholder="$t('dataFilter.gjz')" @search="fuzzyQueryFn" ref="panelRef">
      <template slot="right">
        <yu-toolBar>
          <yu-button v-if="checkCtrl('add')" @click="dataFilterAddFn">{{ $t('dataFilter.xz') }}</yu-button>
          <yu-button v-if="checkCtrl('delete')" @click="dataFiltertDelFn" v-norepeat.disabled>{{ $t('dataFilter.sc') }}</yu-button>
        </yu-toolBar>
      </template>
      <template slot="filter">
        <yu-xform v-model="searchFormdata" related-table-name="filterTable" ref="searchForm" form-type="search">
          <yu-xform-group :column="4">
            <yu-xform-item :label="$t('dataFilter.mbmc')" :placeholder="$t('dataFilter.qsr')" ctype="input" name="authTmplName" maxlength="32" label-width="84px"></yu-xform-item>
            <yu-xform-item :label="$t('dataFilter.zwfmc')" :placeholder="$t('dataFilter.qsr')" ctype="input" name="sqlName" maxlength="100" label-width="130px"></yu-xform-item>
          </yu-xform-group>
        </yu-xform>
      </template>
      <yu-xtable request-type="POST" :data-url="serviceUrl" row-number selection-type="checkbox" ref="filterTable">
        <yu-xtable-column :label="$t('dataFilter.mbmc')" sortable prop="authTmplName">
          <template slot-scope="scope">
            <a class="underline" @click="dataFiltertInfoFn(scope.row)">{{ scope.row.authTmplName }}</a>
          </template>
        </yu-xtable-column>
        <yu-xtable-column prop="sqlName" :label="$t('dataFilter.zwfmc')"></yu-xtable-column>
        <yu-xtable-column prop="sqlString" :label="$t('dataFilter.sjqxtj')"></yu-xtable-column>
        <yu-xtable-column prop="priority" :label="$t('dataFilter.yxj')"></yu-xtable-column>
        <yu-xtable-column :label="$t('dataFilter.zjgx')" width="268">
          <template slot-scope="scope" v-if="scope.row.lastChgName">
            <span>{{ scope.row.lastChgName + '（' + scope.row.lastChgDt + '）' }}</span>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('dataFilter.cz')" fixed="right" width="120px">
          <template slot-scope="scope">
            <yu-button v-if="checkCtrl('edit')" type="text" @click="dataFilterEditFn(scope.row)">{{ $t('dataFilter.xg') }}</yu-button>
            <yu-button v-if="checkCtrl('delete')" type="text" @click="dataFiltertDelFn(scope.row)" v-norepeat.disabled>{{ $t('dataFilter.sc') }}</yu-button>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
    <!-- 数据权限模板dialog -->
    <yu-xdialog :title="dialogTitle" :visible.sync="dialogVisible" size="tiny">
      <yu-xform ref="datafilterForm" label-width="148px" v-model="datafilterFormData" :form-type="formType" :rules="datafilterRules">
        <yu-xform-group :column="1">
          <yu-xform-item :label="$t('dataFilter.mbmc')" name="authTmplName" :placeholder="$t('dataFilter.qsr')" :disabled="authTmplNameDisabled"></yu-xform-item>
          <yu-xform-item :label="$t('dataFilter.zwfmc')" name="sqlName" :placeholder="$t('dataFilter.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('dataFilter.sjqxtj')" name="sqlString" :placeholder="$t('dataFilter.qsr')"></yu-xform-item>
          <yu-xform-item :label="$t('dataFilter.yxj')" name="priority" :placeholder="$t('dataFilter.qsr')"></yu-xform-item>
        </yu-xform-group>
      </yu-xform>
      <div v-if="formType == 'edit'" slot="footer" class="dialog-footer" key="edit">
        <yu-button type="primary" v-norepeat.disabled="saveDisabled" @click="saveFn">{{ $t('dataFilter.bc') }}</yu-button>
        <yu-button @click="dialogVisible = false">{{ $t('dataFilter.qx') }}</yu-button>
      </div>
      <div v-else slot="footer" class="dialog-footer" key="info">
        <yu-button v-if="checkCtrl('edit')" type="primary" @click="swithEditFn">{{ $t('dataFilter.xg') }}</yu-button>
        <yu-button @click="dialogVisible = false">{{ $t('dataFilter.fh') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import { clone, extend } from '@/utils'
import { validator } from "@/utils/validate"
export default {
  data() {
    return {
      serviceUrl: backend.appOcaService + '/api/adminsmdataauthtmpl/list', // 数据模板表单查询URL
      addUrl: backend.appOcaService + '/api/adminsmdataauthtmpl/add', // 新增保存数据模板
      editUl: backend.appOcaService + '/api/adminsmdataauthtmpl/updates', // 修改保存数据模板
      searchFormdata: {}, // 查询表单数据
      datafilterFormData: {}, // 新增修改数据权限模板表单数据
      // 新增修改权限模板表单验证规则
      datafilterRules: {
        authTmplName: [{ required: true, message: this.$t('dataFilter.qsrmbmc')}, {max: 32, message: this.$t('dataFilter.zdcdbcgse')}],
        sqlName: [{ required: true, message: this.$t('dataFilter.qsrSQL')}, {max: 100, message: this.$t('dataFilter.zdcdbcgyb')}],
        priority: [{ message: this.$t('dataFilter.qsrsz'), validator: validator.number}, {max: 100, message: this.$t('dataFilter.zdcdbcgyb')}],
        sqlString: [{ required: true, message: this.$t('dataFilter.qsrSQLtj')}, {max: 100, message: this.$t('dataFilter.zdcdbcgyb')}]
      },
      dialogVisible: false, // 表单弹框是否显示
      authTmplNameDisabled: false, // 模板名称是否禁用
      dialogTitle: '', // 弹框标题
      formType: 'edit',
      saveDisabled:{ show: false } // 保存按钮防重复提交
    };
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
      this.remoteTableData('panelRef', 'filterTable', 'searchFormdata')
    },
    /**
    * 简洁搜索框模糊查询
    * @param dataObj.value 搜索框的值
    */
    fuzzyQueryFn(dataObj) {
      var param = {keyWord: dataObj.value};
      this.$refs.filterTable.remoteData(param);
    },

    /**
      * 控制保存按钮、xdialog、表单的状态
    * @param formType 表单类型 edit--可编辑，detail--详情
    * @param isAdd 是否是新增,默认false
    */
    switchStatus(formType, isAdd) {
      this.dialogVisible = true;
      this.formType = formType;
      this.authTmplNameDisabled = !isAdd;
    },

    /**
    * 查看数据权限模板
    * @param row 选择当前行的数据
    */
    dataFiltertInfoFn(row) {
      this.switchStatus('details');
      this.dialogTitle = this.$t('dataFilter.xq');
      this.$nextTick(function () {
        this.$refs.datafilterForm.setFormData(row);
      });
    },

    // 新增数据权限模板
    dataFilterAddFn() {
      this.switchStatus('edit', true);
      this.dialogTitle = this.$t('dataFilter.xz');
      this.$nextTick(function () {
        this.$refs.datafilterForm.resetFields();
      });
    },

    /**
    * 修改数据权限模板
    * @param row 修改当前行的数据
    */
    dataFilterEditFn(row) {
      this.switchStatus('edit');
      this.dialogTitle = this.$t('dataFilter.xg');
      this.$nextTick(function () {
        this.$refs.datafilterForm.setFormData(row);
      });
    },

    /**
    * 从详情转变为修改状态
    */
    swithEditFn() {
      this.switchStatus('edit');
      this.dialogTitle = this.$t('dataFilter.xg');
    },

    /**
    * 保存数据权限模板
    * 先进行表单校验，再保存
    */
    saveFn() {
      var _this = this;
      var validate = false;
      _this.$refs.datafilterForm.validate(function (valid) {
        validate = valid;
      });
      if (!validate) {
        return;
      }
      var param = extend({}, this.datafilterFormData);
      // 判断表单数据是否存在id,有id为修改，没有为新增数据。
      var authTmplId = this.datafilterFormData.authTmplId || '';
      _this.saveDisabled.show = true;
      _this.saveDisabled = clone(_this.saveDisabled, {});
      this.$request({
        url: authTmplId ? _this.editUl : this.addUrl,
        method: 'post',
        data: param
      }).then(({code, message, data}) => {
        //处理请求成功的情况
        _this.saveDisabled.show = false;
        _this.saveDisabled = clone(_this.saveDisabled, {});
        if (code === '0000') {
          !authTmplId && _this.$message({ type: 'success', message: _this.$t('dataFilter.bccg') });
          authTmplId && _this.$message({ type: 'success', message: _this.$t('dataFilter.xgcg') });
          _this.remoteData();
          _this.dialogVisible = false;
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      })
    },

    /**
    * 删除数据权限模板，可批量删除
    * @param 有row 删除当前行的数据，无参数时表示批量删除
    */
    dataFiltertDelFn(row) {
      var _this = this;
      var ids = [];
      if (row && row.authTmplId) {
        ids.push(row.authTmplId);
      } else {
        var selections = this.$refs.filterTable.selections;
        if (!selections.length) {
          this.$message({ message: this.$t('dataFilter.qxzxyscdmb'), type: 'warning' });
          return;
        }
        for (var i = 0; i < selections.length; i++) {
          ids.push(selections[i].authTmplId);
        }
      }
      _this.$confirm(_this.$t('dataFilter.scmbjhscqsjqxyyyjsqgxqrplscsjqxmbqjscz'), _this.$t('dataFilter.ts'), {
        confirmButtonText: _this.$t('dataFilter.qd'),
        cancelButtonText: _this.$t('dataFilter.qx'),
        type: 'warning'
      }).then(function () {
        _this.$request({
          url: backend.appOcaService + '/api/adminsmdataauthtmpl/deletes',
          method: 'post',
          data: ids
        }).then(({code, message, data}) => {
        //处理请求成功的情况
          if (code === '0000') {
            // var resData = data.body || '';
            // if (resData && resData.length) {
            //   var arr = [];
            //   var errMsg = '';
            //   resData.forEach(function (item) {
            //     arr.push(item.authTmplName);
            //   });
            //   arr.length === 1 && (errMsg = arr.join('、') + message);
            //   arr.length > 1 && (errMsg = arr.join('、') + '，以上' + message);
            //   _this.$message({type: 'error', message: errMsg });
            // } else {
            _this.$message({type: 'success', message: _this.$t('dataFilter.sccg') });
            //}
            _this.remoteData();
          } else {
            _this.$message({ type: 'error', message: message });
          }
        })
      });
    }
  }
}
</script>
