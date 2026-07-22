<!--
  @Created by panglx@yusys.com.cn 2020-12-22
  @updated by
  @description test
-->
<template>
  <div id="notice">
    <yu-panel ref="panel" :title="$t('notice.xtgg')" class="adjust-height" show-search-input :placeholder="$t('notice.gjz')" @search="fuzzyQueryFn">
      <template slot="right">
        <yu-toolBar>
          <yu-button @click="addNoticeFn">{{ $t('notice.xz') }}</yu-button>
          <yu-button @click="pusblishFn">{{ $t('notice.fb') }}</yu-button>
          <yu-button v-norepeat.disabled @click="deleteFn">{{ $t('notice.sc') }}</yu-button>
        </yu-toolBar>
      </template>
      <!--公告列表查询条件-->
      <template slot="filter">
        <yu-xform ref="searchForm" related-table-name="noticeTable" v-model="searchFormdata" form-type="search">
          <yu-xform-group :column="4">
            <yu-xform-item name="noticeTitle" :label="$t('notice.ggbt')" :placeholder="$t('notice.qsr')"></yu-xform-item>
            <yu-xform-item name="noticeLevel" :label="$t('notice.zycd')" :placeholder="$t('notice.qxz')" ctype="select" data-code="NOTICE_LEVEL"></yu-xform-item>
            <yu-xform-item name="pubSts" :label="$t('notice.fbzt')" :placeholder="$t('notice.qxz')" ctype="select" data-code="PUB_STS"></yu-xform-item>
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
        <yu-xtable-column :label="$t('notice.fbzt')" prop="pubSts" data-code="PUB_STS"></yu-xtable-column>>
        <yu-xtable-column :label="$t('notice.cjsj')" prop="creatorTime" width="200">
        </yu-xtable-column>
        <yu-xtable-column fixed="right" :label="$t('component.operate')" width="160">
          <template slot-scope="scope">
            <yu-button-drop set-index="0" :show-length="2" type="text">
              <yu-button v-if="scope.row.pubSts === 'C'" type="text" @click="editNoticeFn(scope.row, true)">{{ $t('notice.xg') }}</yu-button>
              <yu-button v-if="scope.row.pubSts === 'C'" type="text" @click="pusblishFn(scope.row)">{{ $t('notice.fb') }}</yu-button>
              <yu-button v-if="scope.row.pubSts === 'C'" v-norepeat.disabled type="text" @click="deleteFn(scope.row)">{{ $t('notice.sc') }}</yu-button>
            </yu-button-drop>
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
      tableUrl: backend.appOcaService + '/api/adminsmnotice/control/list',
      readSts: {}
    }
  },
  mounted() {
    this.readSts = lookup.find('READ_STS', false);
    yufp.globalEventBus.$on('addNoticeFinish', this.remoteData);
  },
  destroyed() {
    yufp.globalEventBus.$off('addNoticeFinish', this.remoteData);
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

    /**
    * 发布公告
    */
    pusblishFn(row) {
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
      var unpubNum = 0;
      for(let i = 0; i < selections.length; i++) {
        ids.push(selections[i].noticeId);
        selections[i].pubSts === 'C' && unpubNum++
      }
      if(!unpubNum) {
        _this.$message({
          message:  _this.$t('notice.nsxdtzysfbzt'),
          type: 'warning'
        });
        return;
      }
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/adminsmnotice/pub',
        data: ids
      }).then(({code, message, data}) => {
        if (code === '0000') {
          const message = `${this.$t('notice.ycgfb')}${unpubNum}${this.$t('notice.tgg')}`;
          _this.$message({
            message: message,
            type: 'success'
          });
          _this.remoteData();
        } else {
          _this.$message({
            message: message || this.$t('notice.scsb'),
            type: 'error'
          });
        }
      });
    },

    /**
    * 已读公告
    */
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
      for(let i = 0; i < selections.length; i++) {
        ids.push(selections[i].noticeId);
      }
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/notice/adminsmnoticeread/save',
        data: {
          noticeIds: ids.join()
        }
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$message({
            message: _this.$t('notice.szcg'),
            type: 'success'
          });
          _this.$refs.noticeTable.remoteData();
        } else {
          _this.$message({
            message: message || this.$t('notice.szsb'),
            type: 'error'
          });
        }
      });
    },

    /**
    * 删除公告
    */
    deleteFn: function(row) {
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
      for(let i = 0; i < selections.length; i++) {
        ids.push(selections[i].noticeId);
      }
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/adminsmnotice/delete',
        data: ids
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$message({
            message: _this.$t('notice.sccg'),
            type: 'success'
          });
          _this.remoteData();
        } else {
          _this.$message({
            message: message || this.$t('notice.scsb'),
            type: 'error'
          });
        }
      });
    },

    /**
    * 新增公告
    */
    addNoticeFn() {
      const route = 'content/systemManager/notice/editNotice';
      const title = this.$t('notice.xzgg');
      const path = '/' + 'addNotice';
      this.$router.addRoute(route, title, {}, path); // 第三个参数meta详情见VUE官方的router参数
      this.$router.push({ path: path, query: {} }); // query 可传递新页面初始化加载的参数
    },

    /**
    * 修改公告
    */
    editNoticeFn(row, flag) {
      const route = 'content/systemManager/notice/editNotice';
      this.$router.addRoute(route, this.$t('notice.editgg'), {}, '/editNotice'); // 第三个参数meta详情见VUE官方的router参数
      this.$router.push({ path: '/editNotice', query: {noticeId: row.noticeId, isEdit: 'edit'} }); // query 可传递新页面初始化加载的参数

    },
    infoNoticeFn(row) {
      // const route = 'content/systemManager/notice/noticeInfo';
      // this.$router.addRoute(route, this.$t('notice.ggxq'), {}, '/infoNotice'); // 第三个参数meta详情见VUE官方的router参数
      // this.$router.push({ path: '/infoNotice', query: {noticeId: row.noticeId} }); // query 可传递新页面初始化加载的参数

      const route = 'content/systemManager/notice/noticeInfo';
      this.$router.addTab({
        name: route, // 路由名称
        key: "infoNotice", // 自定义唯一页签key,请统一使用custom_前缀开头
        title: this.$t('notice.ggxq'), // 页签名称
        data: { noticeId: row.noticeId }, // 传递的业务数据，可选配置
        noCache: true // 是否默认缓存
      });
    },

    /**
    * 刷新列表
    */
    remoteData() {
      if (this.$refs.panel.hide) {
        this.$refs.noticeTable.remoteData({keyWord: this.$refs.panel.inputVal})
      } else {
        this.$refs.noticeTable.remoteData(this.searchFormdata);
      }
    }
  },
}
</script>
