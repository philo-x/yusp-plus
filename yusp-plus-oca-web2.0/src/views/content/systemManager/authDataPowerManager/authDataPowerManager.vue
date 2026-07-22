<template>
  <div class="authorize-manage clear">
    <!-- 左侧角色或用户选择 -->
    <yu-auth-list @chooseUserRoleId="getUserRoleId" @chooseAuthObjId="getAuthObjId"></yu-auth-list>
    <div :class="['tree-box', {'frame-height': !chooseRoleIdFlag}]">
      <yu-panel v-if="chooseRoleIdFlag" ref="simplePanel" :title="$t('authDataPowerManager.sjsqlb')" panel-type="simple" show-search-input :placeholder="$t('authDataPowerManager.kzdmc')" @search="fuzzyQueryFn">
        <yu-xtable request-type="POST" row-number ref="refTable" :data-url="authDataUrl" :base-params="tableParams" :default-load="false">
          <yu-xtable-column prop="menuPath" :label="$t('authDataPowerManager.sjmkcd')"></yu-xtable-column>
          <yu-xtable-column prop="cornName" :label="$t('authDataPowerManager.kzdmc')"></yu-xtable-column>
          <yu-xtable-column prop="authTmplName" :label="$t('authDataPowerManager.ysqdsjqxmb')" :formatter="formatter"></yu-xtable-column>
          <yu-xtable-column :label="$t('authDataPowerManager.cz')" width="120">
            <template slot-scope="scope">
              <yu-button @click="handleEdit(scope.row)" type="text">{{ $t("authDataPowerManager.sjsq") }}</yu-button>
            </template>
          </yu-xtable-column>
        </yu-xtable>
      </yu-panel>
      <div v-if="!chooseRoleIdFlag" class="no-choose">
        <img src="@/assets/common/images/no-data.svg">
        <p>{{ $t('authDataPowerManager.nochoosetext') }}</p>
      </div>
    </div>
    <yu-xdialog :title="$t('authDataPowerManager.xzsjmbsq')" :visible.sync="authDialogVisible" class="control-dialog">
      <yu-xtable request-type="POST" :data-url="authTmplUrl" selection-type="checkbox" ref="dataAuthTable" @loaded="authLoadFn" @selection-change="authChangeFn">
        <yu-xtable-column prop="authTmplName" :label="$t('authDataPowerManager.mbmc')"></yu-xtable-column>
        <yu-xtable-column prop="sqlName" :label="$t('authDataPowerManager.zwfmc')"></yu-xtable-column>
        <yu-xtable-column prop="sqlString" :label="$t('authDataPowerManager.sjqxtj')"></yu-xtable-column>
      </yu-xtable>
      <div slot="footer" class="dialog-footer">
        <yu-button type="primary" @click="saveFn" v-norepeat.disabled>{{ $t('authDataPowerManager.qd') }}</yu-button>
        <yu-button @click="authDialogVisible = false">{{ $t('authDataPowerManager.qx') }}</yu-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import YuAuthList from '@/components/widgets/YuAuthList';
export default {
  components: { YuAuthList },
  data: function () {
    return {
      authDataUrl: backend.appOcaService + "/api/authorization/authList",
      authTmplUrl: '',
      authDialogVisible: false,
      chooseRoleId: '', // 已选择的角色/用户id
      chooseRoleIdFlag: false, // 是否选择的角色/用户id
      isRoleTab: true, // 当前页签是否是角色授权
      chooseAuthId: '', // 已授权的数据模板Id
      chooseContrId: '',
      // 数据授权列表阐述
      tableParams:{
        userRoleId: '',
        authObjId: ''
      }
    };
  },
  methods: {
    // 格式化已授权模板名称
    formatter(row, column) {
      if(!row.tmplAndRecoVo) {
        return this.$t('authDataPowerManager.zwglmb');
      }
      return row.tmplAndRecoVo.authTmplName;
    },

    //编辑列
    handleEdit(row) {
      const isChange = this.chooseContrId === row.resContrId;
      this.authDialogVisible = true;
      this.chooseContrId = row.resContrId;
      this.chooseAuthId = (row.tmplAndRecoVo && row.tmplAndRecoVo.authTmplId) || '';
      this.authTmplUrl = backend.appOcaService + `/api/adminsmdataauthtmpl/associated/${row.resContrId}`;
      this.$nextTick(function () {
        isChange && this.$refs.dataAuthTable.remoteData();
      });
    },

    /**
    * 点击搜索框模糊查
    * @param e.value 搜索框的值
    */
    fuzzyQueryFn: function (e) {
      this.tableParams.keyWord = e.value;
      this.$refs.refTable.remoteData(this.tableParams);
    },

    /**
    * 选择数据权限模板当选择项发生变化时会触发的事件
    * @param param selection
    */
    authChangeFn(selection) {
      if (selection.length > 1) {
        this.$refs.dataAuthTable.clearSelection();
        this.$refs.dataAuthTable.toggleRowSelection(selection.pop());
      }
    },

    // 选择菜单表格数据加载完, (data, total, response),data为表格加载完成后表格数据，total为表格数据总数,response 为整个返回报文
    authLoadFn(d) {
      var _this = this;
      d.forEach(function (item) {
        item.authTmplId === _this.chooseAuthId && _this.$refs.dataAuthTable.toggleRowSelection(item, true);
      });
    },

    // 保存授权
    saveFn() {
      const _this = this;
      const selections = this.$refs.dataAuthTable.selections;
      _this.$request({
        url: backend.appOcaService + '/api/authorization/saveTmplAuth',
        method: 'post',
        data: {
          lastAuthresId: _this.chooseAuthId, // 之前的数据模板授权的数据
          authresId: (selections.length && selections[0].authTmplId) || '', //新授权的数据模板 id
          contrId: _this.chooseContrId, // 相关联的控制点 id
          authobjType: _this.isRoleTab ? 'R' : 'U', // 授权对象 id
          authobjId: _this.chooseRoleId // 授权对象类型
        }
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$message({type: 'success', message: _this.$t('authDataPowerManager.bcsqcg') });
          _this.authDialogVisible = false;
          _this.$refs.refTable.remoteData();
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
      this.tableParams.userRoleId = val;
    },

    /**
    * 数据授权列表
    * @param param1-当前用户/角色id
    * @param param2-当前页签是否是角色授权
    */
    getAuthObjId(param1, param2) {
      this.chooseRoleIdFlag = true;
      this.chooseRoleId = param1;
      this.$nextTick(function () {
        this.tableParams.authObjId = param1;
        this.$refs.refTable.remoteData(this.tableParams);
      });
    }
  },
};
</script>
<style>
.yu-frame-tab-box>div>div.authorize-manage {
  -webkit-box-shadow: none;
  box-shadow: none;
  background: #f2f2f2;
}

.tree-box {
  background: #ffffff !important;
  float: right;
  width: calc(100% - 424px);
  min-height: calc(100vh - 156px);
  position: relative;
}

.compact .tree-box {
  width: calc(100% - 416px);
  min-height:calc(100vh - 115px);
}

</style>

