<!--
  @Created by gaoxin5@yusys.com.cn 2021-03-01
  @updated by
  @description 定时任务-日志列表
-->
<template>
  <div class="data-scheduleLog">
    <div class="headerSpan">
      <i
        class="el-icon-arrow-left customize"
        @click="backScheduleFn"
      ><span>{{ $t('base.fh') }} <span style="margin-left:12px;color:#333333">定时任务</span></span></i>
    </div>
    <yu-panel title="日志列表" :collapse-hide="false">
      <template slot="right">
        <yu-toolBar>
          <yu-input style="float:left;line-height:36px;margin-right:10px;" :placeholder="$t('schedule.rwid')" v-model="inputVal" @keyup.enter.native="logQueryFn">
            <i slot="suffix" class="el-input__icon yu-icon-search1" @click="logQueryFn"></i>
          </yu-input>
        </yu-toolBar>
      </template>
      <yu-xtable request-type="POST" :data-url="serviceUrl" row-number ref="scheduleLogTable">
        <yu-xtable-column prop="logId" :label="$t('schedule.rzid')"></yu-xtable-column>
        <yu-xtable-column prop="jobId" :label="$t('schedule.rwid')"></yu-xtable-column>
        <yu-xtable-column prop="beanName" :label="$t('schedule.beanmc')"></yu-xtable-column>
        <yu-xtable-column prop="params" :label="$t('schedule.cs')"></yu-xtable-column>
        <yu-xtable-column :label="$t('schedule.zht')">
          <template slot-scope="scope">
            <yu-tag v-if="scope.row.status == 0" type="success" size="small">成功</yu-tag>
            <yu-tag v-if="scope.row.status == 1" type="danger" size="small">失败</yu-tag>
          </template>
        </yu-xtable-column>
        <yu-xtable-column prop="times" :label="$t('schedule.hs')"></yu-xtable-column>
        <yu-xtable-column prop="createTime" :label="$t('schedule.zxsj')"></yu-xtable-column>
      </yu-xtable>
    </yu-panel>
  </div>
</template>
<script>
export default {
  data() {
    return {
      serviceUrl: backend.appOcaService + '/api/scheduleLog/list', // 数据模板表单查询URL
      inputVal: ''
    };
  },

  methods: {
    /**
    * 简洁搜索框模糊查询
    */
    logQueryFn() {
      var param = {jobId: this.inputVal};
      this.$refs.scheduleLogTable.remoteData(param);
    },

    // 返回定时任务列表
    backScheduleFn () {
      this.$router.go(-1)
    }

  }
}
</script>
<style scoped>
  .headerSpan {
    height: 40px;
    line-height: 40px;
    font-size: 14px;
    font-weight: 500;
    border-bottom: 1px #ededed solid;
    box-sizing: border-box;
  }

  .customize {
    cursor: pointer;
    margin-left: 24px;
    font-family: MicrosoftYaHei;
    font-size: 14px;
    color: #2877ff;
    letter-spacing: 0;
    font-weight: 400;
  }
</style>
