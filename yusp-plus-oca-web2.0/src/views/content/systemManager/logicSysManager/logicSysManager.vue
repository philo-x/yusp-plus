
<template>
  <!--
 * @Authoer: dusong
 * @Description:逻辑系统管理
 * @Date 2017/12/5 14:16
 * @Modified By:
 *
-->
  <div class="logicSys">
    <yu-panel :title="$t('logicSysManager.clgl')" :collapse-hide="false">
      <yu-xtable request-type="POST" ref="strategyTable" row-number :data-url="strategyTableUrl" :pageable="false" @loaded="loadedFn">
        <yu-xtable-column :label="$t('logicSysManager.clmc')" prop="crelName"></yu-xtable-column>
        <yu-xtable-column :label="$t('logicSysManager.clms')" prop="crelDescribe"></yu-xtable-column>
        <yu-xtable-column :label="$t('logicSysManager.clfl')" width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.crelKey.startsWith('LOGIN_')">{{ $t('logicSysManager.dlcl') }}</span>
            <span v-else>{{ $t('logicSysManager.xgmmcl') }}</span>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('logicSysManager.sfqy')" width="200">
          <template slot-scope="scope">
            <yu-switch v-model="scope.row.enableFlag" text-inside on-value="1" off-value="2" :on-text="$t('logicSysManager.ty')" :off-text="$t('logicSysManager.qy')" @change="enableFn(scope.row)"></yu-switch>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('logicSysManager.clxq')" width="320">
          <template slot-scope="scope">
            <!--<<div v-if="scope.row.crelKey==='PASSWORD_COMPEL_CHANGE'">密码有效期<yu-input v-model="scope.row.crelDetail" class="detail-input" size="small" ctype="num" :disabled="ValidDayDisabled"></yu-input>天</div>
            <yu-time-picker v-if="scope.row.crelKey==='LOGIN_TIMES_B'" size="small" is-range range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间" v-model="timePicker" value-format="HH:mm:ss" :disabled="scope.row.enableFlag==='2'"></yu-time-picker>
            <div v-else-if="scope.row.crelKey==='PASSWD_LENGTH_RULE'">口令最短长度<yu-input v-model="scope.row.crelDetail" class="detail-input" size="small" ctype="num" :disabled="ValidDayDisabled"></yu-input></div>
            <div v-else-if="scope.row.crelKey==='PASSWORD_WRONG'">最大错误次数<yu-input v-model="scope.row.crelDetail" class="detail-input" size="small" ctype="num" :disabled="errCountDisabled"></yu-input></div>
            <div v-else-if="scope.row.crelKey==='PASSWD_REPETNUMBER_RULE'">口令禁止重复字符数<yu-input v-model="scope.row.crelDetail" class="detail-input" size="small" ctype="num" :disabled="ValidDayDisabled"></yu-input></div>
            <div v-else-if="scope.row.crelKey==='PASSWD_SEQUNNUMBER_RULE'">口令禁止连续字符数<yu-input v-model="scope.row.crelDetail" class="detail-input" size="small" ctype="num" :disabled="ValidDayDisabled"></yu-input></div>-->
            <div v-if="scope.row.crelKey==='PASSWD_COMPLEX_RULE'"><yu-xcheckbox ref="refCheckbox" v-model="scope.row.crelDetail" data-code="PASSWD_COMPLEX_RULE" :option-button="false" :disabled="scope.row.enableFlag==='2'"></yu-xcheckbox></div>
            <div v-if="scope.row.crelKey==='LOGIN_PERSON_LIMIT'">登录人数限制<yu-input v-model="scope.row.crelDetail" class="detail-input" size="small" ctype="num" :disabled="scope.row.enableFlag==='2'"></yu-input></div>
            <div v-if="scope.row.crelKey==='LOGIN_TIMES_B'" class="detail-select">
              <yu-time-select :placeholder="$t('logicSysManager.qssj')" v-model="startTime" :picker-options="{ start: '00:00',step: '00:15',end: '23:44', maxTime: endTime}" size="small" value-format="HH:mm" :disabled="scope.row.enableFlag==='2'"> </yu-time-select>
              <span>{{ $t('logicSysManager.to') }}</span>
              <yu-time-select :placeholder="$t('logicSysManager.qssj')" v-model="endTime" :picker-options="{start: '00:15',step: '00:15',end: '23:59', minTime: startTime}" size="small" value-format="HH:mm" :disabled="scope.row.enableFlag==='2'"></yu-time-select>
            </div></template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('logicSysManager.xqsj')" width="120">
          <template slot-scope="scope">
            <yu-button v-if="scope.row.crelDetail" type="text" @click="saveDetailFn(scope.row)" :disabled="scope.row.enableFlag==='2'">{{ $t('logicSysManager.bc') }}</yu-button>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
  </div>
</template>
<script>
export default {
  data() {
    return {
      strategyTableUrl: backend.appOcaService + '/api/adminsmcrelstra/getall',
      startTime: '',
      endTime: ''
    };
  },
  methods: {
    loadedFn(data) {
      data.forEach(ele => {
        if(ele.crelKey === 'LOGIN_TIMES_B') {
          this.startTime = ele.crelDetail && ele.crelDetail.split(',')[0];
          this.endTime = ele.crelDetail && ele.crelDetail.split(',')[1];
        }
      });
    },
    saveDetailFn(row) {
      const _this = this;
      if(row.crelKey === 'PASSWD_COMPLEX_RULE') {
        if(!row.crelDetail.length) {
          _this.$message({ message: _this.$t('logicSysManager.qxzmmfzcl'), type: 'error' });
          return;
        }
        row.crelDetail = row.crelDetail.join();
      }else if(row.crelKey === 'LOGIN_TIMES_B') {
        if(!_this.startTime || !_this.endTime) {
          _this.$message({ message: _this.$t('logicSysManager.qxzdlsj'), type: 'error' });
          return;
        }
        row.crelDetail = _this.startTime + ',' + _this.endTime;
      };
      _this.$request({
        url: backend.appOcaService + '/api/adminsmcrelstra/update',
        method: 'post',
        data: {
          crelId: row.crelId,
          enableFlag: row.enableFlag,
          crelDetail: row.crelDetail
        }
      }).then(({code, message, data}) => {
        //处理请求成功的情况
        if (code === '0000') {
          _this.$message({ type: 'success', message: _this.$t('logicSysManager.bccg') })
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      })
    },
    // 启用/通用策略
    enableFn(row) {
      const _this = this;
      this.$request({
        url: backend.appOcaService + '/api/adminsmcrelstra/update',
        method: 'post',
        data: {
          crelId: row.crelId,
          enableFlag: row.enableFlag
        }
      }).then(({code, message, data}) => {
        //处理请求成功的情况
        if (code === '0000') {
          row.enableFlag == '1' && _this.$message({ type: 'success', message: _this.$t('logicSysManager.qycg') });
          row.enableFlag == '2' && _this.$message({ type: 'success', message: _this.$t('logicSysManager.tycg') });
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      })
    }
  }

}
</script>
<style>
.detail-input{
  width: 50px;
  margin-left: 4px;
}
.detail-select .el-date-editor{
  width: 120px!important;
}
</style>
