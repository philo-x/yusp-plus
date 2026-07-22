<template>
  <div class="yufp-search-tree">
    <yu-combo-tree v-show="!details" ref="searchTree" v-model="value" :placeholder="placeholder" :data-url="dataUrl" :data-id="dataId" request-type="POST"
                   :data-label="dataLabel" :data-pid="dataPid" :clearable="true" :data-params="dataParams" @node-click="nodeClick" @clear="clearFn" :disabled="disabled" :multiple="multiple"
                   :filter-node-method="filterNode" :all-node-value="true" :max-height="maxHeight" @visible-change="visibleChange">
    </yu-combo-tree>
    <span v-show="details">{{ detailsValue }}</span>
  </div>
</template>

<script>
import { extend } from '@/utils'
import { checkBelongToChooseNode } from '@/utils/util'
export default {
  name: 'YufpSearchTree',
  xtype: 'YufpSearchTree',

  props: {
    detailsValue: {
      type: String,
      default: ''
    },
    details: {
      type: Boolean,
      default: true
    },
    selectValue: {
      type: String || Array,
      default: ''
    },
    disabled: Boolean,
    params: Object,
  },
  data: function () {
    return this.createData();
  },
  watch: {
    details: {
      handler: function (newVal, oldVal) {
        this.value = this.selectValue;
      },
      immediate: true
    },
    selectValue: {
      handler: function (newVal) {
        this.value = newVal;
        if (newVal == '') {
          this.$nextTick(function () {
            this.$refs.searchTree.$refs.selectTreeX.remoteData();
          });
        }
      },
      immediate: false
    },
    value: function (val) {
      if (!this.details) {
        this.$refs.searchTree.filter(val);
      }
    },
    params: function (val) {
      var _this = this;
      var temp = _this.createData();
      extend(true, _this, val);
      _this.dataParams = temp.dataParams;
    }
  },
  methods: {
    /**
      * 机构树搜索
      * @param nodeData 当前输入信息
      */
    filterNode: function (value, nodeData, node) {
      if (!this.$refs.searchTree.selectedLabel) {
        return true;
      }
      if (nodeData.orgName) {
        if (nodeData.orgName.indexOf(this.$refs.searchTree.selectedLabel) !== -1) {
          return true;
        }
      }
      if (nodeData.dptName) {
        if (nodeData.dptName.indexOf(this.$refs.searchTree.selectedLabel) !== -1) {
          return true;
        }
      }
      return nodeData.orgName ? checkBelongToChooseNode(this.$refs.searchTree.selectedLabel, node, 'orgName') : checkBelongToChooseNode(this.$refs.searchTree.selectedLabel, node, 'dptName');
    },
    
    nodeClick: function (nodeData, node, self) {
      this.$emit('node-click', nodeData);
    },
    clearFn: function () {
      this.$refs.searchTree.filter('');
      this.$emit('clear');
    },
    /**
      *清除查询参数,当修改url的时候调用
      */
    clearSearchParams: function () {
      this.dataParams = {};
    },
    createData: function () {
      var _this = this;
      var temp = _this.getDefaultData();
      // 深度拷贝
      extend(true, temp, _this.params);
      return temp;
    },
    visibleChange: function(show) {
      if(!show && this.multiple) {
        var arr = this.$refs.searchTree.getSelectedObjs();
        var ids = [];
        arr.forEach(item => {
          ids.push(item.orgId)
        })
        this.$emit('getSelectIds', ids);
      }
    },
    getDefaultData: function () {
      return {
        // 展示参数
        searchKey: '', // 树过滤参数
        value: '',
        placeholder: '',
        width: 200,
        maxHeight: 400,
        multiple: false,
        // 节点参数属性
        dataId: '',
        dataLabel: '',
        dataPid: '',
        // 数据参数
        dataParams: {
        },
        searchType: 'CUR_ORG', // 所辖或者当前""
        tempNodekeys: [],
        dataUrl: ''
      };
    }
  }
};
</script>