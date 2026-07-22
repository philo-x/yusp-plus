<!--
  @Created by panglx@yusys.com.cn 2020-12-22
  @updated by
  @description test
-->
<template>
  <div id="notice">
    <yu-panel ref="panel" :title="$t('notice.qbgg')" class="adjust-height" show-search-input :placeholder="$t('notice.gjz')" @search="fuzzyQueryFn">
      <template slot="right">
        <yu-toolBar>
          <yu-button v-norepeat.disabled @click="readFn">{{ $t('notice.swyd') }}</yu-button>
        </yu-toolBar>
      </template>
      <!--公告列表查询条件-->
      <template slot="filter">
        <yu-xform ref="searchForm" related-table-name="noticeTable" v-model="searchFormdata" form-type="search">
          <yu-xform-group :column="4">
            <yu-xform-item name="noticeTitle" :label="$t('notice.ggbt')" :placeholder="$t('notice.qsr')"></yu-xform-item>
            <yu-xform-item name="noticeLevel" :label="$t('notice.zycd')" :placeholder="$t('notice.qxz')" ctype="select" data-code="NOTICE_LEVEL"></yu-xform-item>
            <yu-xform-item name="readSts" :label="$t('notice.ydzt')" :placeholder="$t('notice.qxz')" ctype="select" data-code="READ_STS"></yu-xform-item>
          </yu-xform-group>
        </yu-xform>
      </template>
      <!--公告列表-->
      <yu-xtable request-type="POST" ref="noticeTable" row-number :data-url="tableUrl" selection-type="checkbox">
        <yu-xtable-column :label="$t('notice.ggbt')" show-overflow-tooltip min-width="200">
          <template slot-scope="scope">
            <a class="underline" @click="infoNoticeFn(scope.row)">{{ scope.row.noticeTitle }}</a>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('notice.zycd')" prop="noticeLevel" data-code="NOTICE_LEVEL"> </yu-xtable-column>
        <yu-xtable-column :label="$t('notice.ydbz')">
          <template slot-scope="scope">
            <yu-tag v-if="scope.row.readSts=== '1'" type="success">{{ readSts[scope.row.readSts] }}</yu-tag>
            <yu-tag v-if="scope.row.readSts=== '0'" type="warning">{{ readSts[scope.row.readSts] }}</yu-tag>
          </template>
        </yu-xtable-column>
        <yu-xtable-column :label="$t('notice.fbr')" width="260">
          <template slot-scope="scope" v-if="scope.row.creatorName">
            <span>{{ scope.row.creatorName }}（{{ scope.row. pubTime }}）</span>
          </template>
        </yu-xtable-column>
      </yu-xtable>
    </yu-panel>
  </div>
</template>
<script>
import {lookup} from '@/utils'

lookup.reg('NOTICE_LEVEL,YESNO,PUB_STS,READ_STS');
export default {
  components: {},
  data() {
    return {
      searchFormdata: {},
      tableUrl: backend.appOcaService + '/api/adminsmnotice/view/list',
      readSts: {}
    }
  },
  mounted() {
    this.readSts = lookup.find('READ_STS', false);
    yufp.globalEventBus.$on('GetNoticeList', this.remoteData);
  },
  destroyed() {
    yufp.globalEventBus.$off('GetNoticeList', this.remoteData);
  },
  methods: {
    /**
    * 点击搜索框模糊查
    * @param e.value 搜索框的值
    */
    fuzzyQueryFn(e) {
      var param = {keyWord: e.value};
      this.$refs.noticeTable.remoteData(param);
    },
    readFn(row) {
      var _this = this;
      var selections = row.noticeId ? [row] : _this.$refs.noticeTable.selections;
      if(selections.length < 1) {
        _this.$message({
          message: _this.$t('notice.qxxzjl'),
          type: 'warning'
        });
        return;
      }
      var ids = [];
      var unReadNum = 0;
      for(let i = 0; i < selections.length; i++) {
        ids.push(selections[i].noticeId);
        selections[i].readSts !== '1' && unReadNum++
      }
      if(!unReadNum) {
        _this.$message({
          message:  _this.$t('notice.nsxdtzysyyzt'),
          type: 'warning'
        });
        return;
      }
      ids = ids.join(',');
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + `/api/notice/adminsmnoticeread/save?noticeIds=${ids}`,
        data: {},
      }).then(({code, message, data}) => {
        if (code === '0000') {
          const message = `${this.$t('notice.cgj')}${unReadNum}${this.$t('notice.gtzbjwyd')}`;
          _this.$message({
            message: _this.$t('notice.bccg'),
            type: 'success'
          });
          _this.$refs.noticeTable.remoteData();
          yufp.globalEventBus.$emit('readNoticeFinish');
        } else {
          _this.$message({
            message: message || this.$t('notice.scsb'),
            type: 'error'
          });
        }
      });
    },
    infoNoticeFn(row) {
      // const route = 'content/systemManager/notice/noticeInfo';
      // this.$router.addRoute(route, this.$t('notice.ggxq'), {}, '/infoNotice'); // 第三个参数meta详情见VUE官方的router参数
      // this.$router.push({ path: '/infoNotice', query: {noticeId: row.noticeId, isGetNotice: 'true'} }); // query 可传递新页面初始化加载的参数

      const route = 'content/systemManager/notice/noticeInfo';
      this.$router.addTab({
        name: route, // 路由名称
        key: "infoNotice", // 自定义唯一页签key,请统一使用custom_前缀开头
        title: this.$t('notice.ggxq'), // 页签名称
        data: { noticeId: row.noticeId, isGetNotice: 'true' }, // 传递的业务数据，可选配置
        noCache: true // 是否默认缓存
      });
    },
    /**
    * 刷新列表
    */
    remoteData() {
      this.$refs.noticeTable.remoteData();
    }
  },
}
</script>
