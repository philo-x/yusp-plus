<template>
  <div id="yufpCustomManage">
    <div class="custom-wrap" :class="{'list-mode-custom-wrap': listMode}">
      <div v-for="(item, index) in fliterData" :key="index" class="custom-data-wrap" :class="{'list-mode': listMode}">
        <div class="custom-pic-wrap"><img :src="item.pic"></div>
        <div class="custom-name-wrap">
          <span class="name" v-if="item.name">{{ item.name }}</span>
          <span v-if="item.id">({{ item.id }}/</span>
          <span v-if="item.sex"> {{ item.sex }}/</span>
          <span v-if="item.age">{{ item.age }})</span>
          <span class="status" :style="getStatusStyle(item)" v-if="item.status">{{ item.status }}</span>
        </div>
        <div class="custom-other-wrap" v-for="(infoValue,infoKey) in otherInfoFiltered(item.otherInfo)" :key="infoKey">
          <span class="custom-info">{{ infoKey }}：</span>
          <span class="custom-info custom-info-value" :title="infoValue">{{ infoValue }}</span>
        </div>
        <div v-if="handleBtn" class="handle-btn">
          <button type="primary" @click="editFn(item)">编辑</button>
          <button type="primary" @click="deleteFn(item)">删除</button>
        </div>
      </div>
      <yu-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage" :page-sizes="pageSizes" :total="total" layout="total, sizes, prev, pager, next, jumper" :page-size="pageSize" v-if="pagination">
      </yu-pagination>
    </div>
  </div>
</template>
<script>
/* eslint prefer-rest-params:0 */
export default {
  name: 'YufpCustomManage',
  componentName: 'YufpCustomManage',
  props: {
    // 组件数据对象
    data: {
      type: Array,
      default: function () {
        return [];
      }
    },
    // 是否显示按钮
    handleBtn: {
      type: Boolean,
      default: true
    },
    // 是否开启列表模式
    listMode: {
      type: Boolean,
      default: false
    },
    // 分页设置
    pageSizes: {
      type: Array,
      default: function () {
        return [8, 6, 4];
      }
    },
    // 列表查询参数对象
    searchParams: {
      type: Object,
      default: function () {
        return {};
      }
    },
    // 是否开启分页
    pagination: {
      type: Boolean,
      default: true
    }
  },
  data: function () {
    return this.createData();
  },
  computed: {
    fliterData: function () {
      var _this = this;
      // 根据插叙条件过滤数据
      var defaultData = _this.searchData();
      var pageCount = Math.ceil(_this.total / _this.pageSize);
      var temp = [];
      // 计算当前页展示的数据条数
      if (_this.currentPage === pageCount) {
        temp = defaultData.slice(
          _this.pageSize * (_this.currentPage - 1),
          defaultData.length
        );
      } else {
        temp = defaultData.slice(
          _this.pageSize * (_this.currentPage - 1),
          _this.pageSize * _this.currentPage
        );
      }
      return temp;
    },
    total: function () {
      // 计算客户数据条数
      var _this = this;
      var defaultData = _this.searchData();
      return defaultData.length;
    }
  },
  methods: {
    otherInfoFiltered (otherInfo) {
      let count = 0;
      const tmp = {};
      for (var k in otherInfo) {
        if (count++ < 4 || this.listMode) {
          tmp[k] = otherInfo[k];
        }
      }
      return tmp;
    },
    filterParams: function () {
      // 去除查询参数中的空属性 无效属性
      var _this = this;
      for (var key in _this.searchParams) {
        if (
          _this.searchParams[key] === '' ||
          _this.searchParams[key] === 'undefined'
        ) {
          delete _this.searchParams[key];
        }
      }
    },
    searchData: function () {
      // 根据 searchParams 过滤数据
      var _this = this;
      var temp = [];
      _this.filterParams();
      const length = Object.keys(_this.searchParams).length; // 查询参数个数
      if (length === 0) {
        return _this.defaultData;
      }
      for (var i = 0, l = _this.defaultData.length; i < l; i++) {
        var item = _this.defaultData[i];
        var count = 0;
        for (var paramsKey in _this.searchParams) {
          if (
            _this.searchParams[paramsKey] &&
            item.hasOwnProperty(paramsKey) &&
            item[paramsKey] === _this.searchParams[paramsKey]
          ) {
            count++;
          } else if (
            item.otherInfo.hasOwnProperty(paramsKey) &&
            item.otherInfo[paramsKey] === _this.searchParams[paramsKey]
          ) {
            count++;
          }
        }
        if (count === length) {
          temp.push(item);
        }
      }
      return temp;
    },
    editFn: function (item) {
      this.$emit('edit-custom', item); // 编辑按钮
    },
    deleteFn: function (item) {
      this.$emit('delete-custom', item); // 删除按钮
    },
    handleSizeChange: function (size) {
      var _this = this;
      _this.pageSize = size;
      _this.$emit('size-change-custom', size); // pageSize 改变时会触发
    },
    handleCurrentChange: function (currentPage) {
      var _this = this;
      _this.currentPage = currentPage;
      _this.$emit('current-change-custom', currentPage); // currentPage改变时会触发
    },
    createData: function () {
      // 初始化客户列表数据
      var _this = this;
      var temp = _this.getDefaultData();
      if (_this.data) {
        temp.defaultData = _this.data; // 传入数据覆盖默认数据
      }
      return temp;
    },
    getDefaultData: function () {
      return {
        // 组件默认数据
        currentPage: 1,
        pageSize: this.listMode ? 8 : 8,
        defaultData: []
      };
    },
    getStatusStyle: function (item) {
      var normal = { 'background-color': '#e8faf0', color: '#13CE66' };
      var abnormal = { 'background-color': '#ffdbdb', color: '#F52C36' };
      return item.status === '正常' ? normal : abnormal;
    }
  }
};
</script>
<style lang="scss">
#yufpCustomManage {
  .custom-wrap {
    padding: 0px 24px 0 24px;
    box-sizing: border-box;
  }
  .list-mode-custom-wrap {
    padding: 0;
  }
  .list-mode-custom-wrap .custom-data-wrap {
    margin-right: 0;
  }
  .custom-data-wrap {
    position: relative;
    width: calc(50% - 12px);
    height: 130px;
    box-sizing: border-box;
    display: inline-block;
    border: 1px solid rgba(224, 224, 224, 1);
    margin-bottom: 24px;
  }
  .custom-data-wrap:hover {
    background: #fbfbfb;
    cursor: pointer;
  }
  .custom-data-wrap:nth-child(2n + 1) {
    margin-right: 23px;
  }
  .custom-data-wrap:nth-last-child(3) {
    margin-bottom: 0px;
  }
  .custom-data-wrap:nth-last-child(2) {
    margin-bottom: 0px;
  }

  .custom-data-wrap .custom-pic-wrap {
    display: inline-block;
    height: 130px;
    float: left;
  }
  .custom-pic-wrap {
    padding: 0 24px;
  }
  .custom-pic-wrap img {
    width: 72px;
    height: 72px;
    border-radius: 50%;
    position: relative;
    margin: 0 auto;
    top: 50%;
    margin-top: -40px;
    border: 4px solid rgba(239, 239, 245, 1);
  }
  .custom-name-wrap {
    padding-top: 16px;
    display: block !important;
  }
  .custom-name-wrap span {
    font-size: 16px;
    font-weight: bold;
    color: #333333;
  }
  .custom-name-wrap .status {
    width: 38px;
    height: 24px;
    line-height: 24px;
    font-weight: 400;
    font-size: 12px;
    border-radius: 4px;
    display: inline-block;
    text-align: center;
    margin-left: 10px;
  }
  .custom-other-wrap {
    padding-top: 12px;
    min-width: 30%;
    display: inline-block;
  }
  .custom-other-wrap:last-child {
    margin-bottom: 24px;
  }
  .custom-info {
    display: inline-block;
    font-size: 14px;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #999999;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
    word-break: break-all;
  }
  .custom-info-value {
    color: #444444;
    max-width: 120px;
  }
  .handle-btn {
    display: none;
    position: absolute;
    top: 14px;
    right: 24px;
  }
  .custom-data-wrap:hover .handle-btn {
    display: inline-block;
  }
  .handle-btn button {
    width: 60px;
    height: 36px;
    margin-top: 10px;
    background: #fff;
    border: 1px solid rgba(224, 224, 224, 1);
    box-shadow: 0px 2px 2px 0px rgba(1, 1, 1, 0.06);
    border-radius: 4px;
    display: block;
    font-size: 14px;
  }
  .handle-btn button:hover {
    cursor: pointer;
  }
  .list-mode {
    width: 100%;
    border: none;
    border-bottom: 1px solid #ededed;
    margin-top: 0;
    margin-bottom: 0;
  }
  .list-mode .custom-other-wrap {
    min-width: 21%;
  }
  .list-mode:first-child {
    border-top: 1px solid #ededed;
    margin-top: -1px;
  }
  .list-mode .handle-btn {
    top: 60px;
  }
  .list-mode button {
    display: inline-block;
    margin-right: 5px;
  }
  .custom-wrap.list-mode-custom-wrap .el-pagination {
    margin-right: 16px;
    padding-right: 8px;
  }
  .custom-wrap .el-pagination {
    padding-right: 0px;
  }
}
</style>
