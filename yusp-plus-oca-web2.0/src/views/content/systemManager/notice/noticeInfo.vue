<!--
  @Created by zhuly8@yusys.com.cn 2021-03-18
  @updated by
  @description test
-->
<template>
  <div class="notice-info">
    <yu-panel :title="$t('notice.ggxq')" panel-type="simple" is-collapse>
      <h3 class="title">{{ formdata.noticeTitle }}</h3>
      <div class="status">
        <span>{{ $t('notice.zycd') }}:<i>{{ formdata.noticeLevel | formatLevel }}</i></span>
        <span>{{ $t('notice.zt') }}:<i>{{ formdata.pubSts | formatPubSts }}</i></span>
        <span v-if="formdata.pubTime">{{ $t('notice.fb') }}: <i>{{ formdata.pubTime }}</i></span>
      </div>
      <div class="content" v-html="formdata.context"></div>
      <div class="org">{{ $t('notice.yxq') }}: <span>{{ formdata.activeDate }}</span></div>
      <div class="org">{{ $t('notice.sfzd') }}: <span>{{ formdata.isTop }}</span></div>
      <div class="org">{{ $t('notice.zdqz') }}: <span>{{ formdata.topActiveDate }}</span></div>
      <div class="org">{{ $t('notice.jsjg') }}: <span>{{ formdata.orgNames || $t('notice.qbjg') }}</span></div>
      <div class="org">{{ $t('notice.jsjs') }}: <span>{{ formdata.roleNames || $t('notice.qbjs') }}</span></div>
      <div class="file-list" v-if="formdata.fileInfoFormList.length">
        <span class="file-list-title">{{ $t('notice.fjlb') }}:</span>
        <ul class="loaded-file-list">
          <li v-for="(item, key) in formdata.fileInfoFormList" :key="item.fileId" @click="downloadFile(item)">
            <span :class="[item.iconType,'file-icon']"></span>
            <div class="file-info">
              <p class="elli">{{ item.fileName }}</p>
              <span class="elli">{{ item.fileSize | formatFileSize }}</span>
            </div>
            <p v-if="formdata.pubSts === 'C'" class="delete" @click.stop="deleteFile(item, key)"><span
              class="yu-icon-delete"></span></p>
          </li>
        </ul>
      </div>
    </yu-panel>
    <yu-form-buttons :padding-left="16">
      <yu-button v-if="formdata.pubSts === 'C'" type="primary" @click="editFn">{{ $t('notice.xg') }}</yu-button>
      <yu-button v-if="formdata.pubSts === 'C'" @click="pusblishFn">{{ $t('notice.fb') }}</yu-button>
      <yu-button v-if="formdata.pubSts === 'C'" @click="deleteFn">{{ $t('notice.sc') }}</yu-button>
      <yu-button @click="cancleFn">{{ $t('notice.qx') }}</yu-button>
    </yu-form-buttons>
  </div>
</template>
<script>
import {lookup} from '@/utils';
import {download} from '@/utils/util';

lookup.reg('NOTICE_LEVEL, PUB_STS');

export default {
  filters: {
    formatLevel(val) {
      if (val) {
        return lookup.convertKey('NOTICE_LEVEL', val);
      }
    },
    formatPubSts (val) {
      if (val) {
        return lookup.convertKey('PUB_STS', val);
      }
    }
  },
  data () {
    return {
      noticeId: this.$route.meta.params && this.$route.meta.params.noticeId,
      formdata: {
        activeDate: '',
        isTop: '',
        topActiveDate: '',
        noticeTitle: '',
        noticeLevel: '',
        pubSts: '',
        pubTime: '',
        context: '',
        reciveOrgMap: '',
        reciveRoleMap: '',
        fileInfoFormList: []
      }
    };
  },
  mounted () {
    this.getNotice();
  },
  methods: {
    cancleFn () {
      this.$route.query.isGetNotice === 'true' && yufp.globalEventBus.$emit('GetNoticeList');
      this.$router.back();
      this.$router.removeTab(this.$route.path);
    },

    /**
    * 发布公告
    */
    pusblishFn (row) {
      const _this = this;
      if (!_this.noticeId) {
        return;
      }
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/adminsmnotice/pub',
        data: [_this.noticeId]
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$message({
            message: _this.$t('notice.fbcg'),
            type: 'success'
          });
          yufp.globalEventBus.$emit('addNoticeFinish');
          _this.$router.back();
          _this.$router.removeTab(this.$route.path);
        } else {
          _this.$message({
            message: message || this.$t('notice.fbsb'),
            type: 'error'
          });
        }
      });
    },

    editFn () {
      const route = 'content/systemManager/notice/editNotice';
      this.$router.removeTab(this.$route.path);
      this.$router.addRoute(route, this.$t('notice.editgg'), {}, '/editNotice'); // 第三个参数meta详情见VUE官方的router参数
      this.$router.push({ path: '/editNotice', query: {noticeId: this.noticeId, isEdit: 'edit'} }); // query 可传递新页面初始化加载的参数
      // this.$router.replace({ path: '/editNotice', query: {noticeId: this.noticeId, isEdit: 'edit'} }); // query 可传递新页面初始化加载的参数
    },

    getNotice () {
      var _this = this;
      if (!_this.noticeId) {
        return;
      }
      this.$request({
        method: 'POST',
        url: backend.appOcaService + `/api/adminsmnotice/info/${_this.noticeId}`,
        data: {}
      }).then(({code, message, data}) => {
        if (code === '0000') {
          const roleNames = [], orgNames = [];
          Object.keys(data.reciveRoleMap).forEach((key, item) => {
            roleNames.push(data.reciveRoleMap[key]);
          });
          Object.keys(data.reciveOrgMap).forEach((key, item) => {
            orgNames.push(data.reciveOrgMap[key]);
          });
          this.formdata = data
          this.formdata.isTop = data.isTop === '01' ? '是' : '否'
          this.formdata.roleNames = roleNames.join();
          this.formdata.orgNames = orgNames.join();
          this.formdata.noticeTitle = data.noticeTitle;
          this.formdata.context = data.context;
          this.formdata.pubSts = data.pubSts;
          this.formdata.noticeLevel = data.noticeLevel;
          this.formdata.pubTime = data.pubTime && (data.creatorName + '（' + data.pubTime + '）') || '';
          this.formdata.fileInfoFormList = data.fileInfoFormList;
          this.formdata.fileInfoFormList.map(item => {
            item.iconType = this.getICon(item.extName);
          });
        } else {
          _this.$message({
            message: message || this.$t('notice.bcsb'),
            type: 'error'
          });
        }
      });
    },

    /**
    * 删除公告
    */
    deleteFn: function (row) {
      const _this = this;
      if (!_this.noticeId) {
        return;
      }
      _this.$request({
        method: 'POST',
        url: backend.appOcaService + '/api/adminsmnotice/delete',
        data: [_this.noticeId]
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.$message({
            message: _this.$t('notice.sccg'),
            type: 'success'
          });
          yufp.globalEventBus.$emit('addNoticeFinish');
          _this.$router.back();
          _this.$router.removeTab(this.$route.path);
        } else {
          _this.$message({
            message: message || this.$t('notice.scsb'),
            type: 'error'
          });
        }
      });
    },
    /**
    * 根据文件类型显示icon
    * @param type 文件后缀名
    */
    getICon (type) {
      var icon = '';
      if (type === 'xls' || type === 'xlsx') {
        icon = 'yu-icon-read';
      } else if (type === 'doc' || type === 'docx') {
        icon = 'yu-icon-word';
      } else if (type === 'ppt' || type === 'pptx') {
        icon = 'yu-icon-data';
      } else if (type === 'pdf') {
        icon = 'yu-icon-pdf';
      } else if (type === 'txt') {
        icon = 'yu-icon-details';
      } else if (type === 'zip') {
        icon = 'yu-icon-zip';
      } else if (type === 'rar') {
        icon = 'yu-icon-zip';
      } else if (type === 'png' || type === 'jpg' || type === 'jpeg' || type === 'svg' || type === 'gif') {
        icon = 'yu-icon-img';
      } else {
        icon = 'yu-icon-infofile';
      }
      return icon;
    },
    // 附件下载
    downloadFile (val) {
      const downloadUrl = yufp.util.addTokenInfo(backend.fileService + '/api/file/provider/download?fileId=' + val.filePath);
      download(downloadUrl);
    },
    // 删除已上传的附件
    deleteFile (item, index) {
      const _this = this;
      if (!_this.noticeId) {
        return;
      }
      _this.$request({
        url: backend.appOcaService + '/api/adminsmricheditfileinfo/del?fileId=' + item.fileId,
        method: 'POST'
      }).then(({code, message, data}) => {
        if (code === '0000') {
          _this.formdata.fileInfoFormList.splice(index, 1);
          _this.$message({ type: 'success', message: _this.$t('notice.scwjcc') });
        } else {
          _this.$message({ message: message, type: 'error' });
        }
      });
    }
  }
};
</script>
<style scoped>
 .notice-info .title{
   text-align: center;
   font-size: 16px;
   color: #333333;
 }
.notice-info .status{
  background: #eeeeee;
  height: 48px;
  margin: 16px;
  text-align: center;
  line-height: 48px;
}
.notice-info .status span {
  padding: 0 10px;
}
.notice-info .status>span i{
  color: #333333;
  font-style: normal;
}
.notice-info .content{
  margin: 0 16px;
  padding-bottom: 24px;
  padding-top: 12px;
  border-bottom: 1px solid #eee;
}
.notice-info .org{
  margin: 16px;
}
.notice-info .org>span{
  color: #333333;
}
.file-list {
  margin: 16px;
  color: #333333;
}
.file-info .elli {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.file-list .file-list-title {
  color: #333333;
}
</style>

