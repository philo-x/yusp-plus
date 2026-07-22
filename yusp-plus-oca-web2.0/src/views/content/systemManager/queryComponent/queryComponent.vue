<template>
  <div class="queryComponent">
    <div class="queryComponent-left">
      <yu-xtree class="queryTree" :local-data="treeData" data-id="id" data-label="label" data-pid="pid" data-root="0" @node-click="treeNodeClick"></yu-xtree>
    </div>
    <div class="queryComponent-right">
      <div v-show="!currentView" class="no-data" :style="{'height': menuTreeHeight + 'px'}">
        <div class="no-choose">
          <img src="@/assets/common/images/no-data.svg" />
          <p class="text">请选择左侧菜单</p>
        </div>
      </div>
      <div>
        <component :is="currentView" :componentData="componentData"></component>
      </div>
    </div>
  </div>
</template>
<script>
// import { resolve } from "url";
import { loadView } from '@/utils/loadView'
/* import queryTable from '@/views/content/systemManager/queryComponent/queryTable.vue'
import queryForm from '@/views/content/systemManager/queryComponent/queryForm.vue' */
export default {
  /* components: {
    queryTable,
    queryForm
  }, */
  data() {
    return {
      treeData: [
        { id: '0', label: '根节点', pid: '-1', children:[
          { id: '1', label: '一级-1', pid: '0', children:[
            { id: '4', label: '表单', pid: '1' },
            { id: '5', label: '表格', pid: '1' }]
          },
          { id: '2', label: '一级-2', pid: '0', children:[
            { id: '6', label: '表单', pid: '2'},
            { id: '7', label: '表格', pid: '2' }]
          },
          { id: '3', label: '一级-3', pid: '0', children:[
            { id: '8', label: '表格', pid: '3' }]
          }
        ]}
      ],
      currentView: '', //当前视图组件
      componentData: '' //视图组件绑定参数
    }
  },
  methods: {
    // 获取require缓存内容
    getRequireCache (route) {
      this.currentView = loadView(`content/systemManager/queryComponent/${route}`);
    },
    //点击树形节点
    treeNodeClick(nodeData, node, self) {
      var _this = this;
      if(nodeData.children && nodeData.children.length > 0) {
        return;
      }
      if(nodeData.label === '表单') {
        this.getRequireCache('queryForm');
        this.componentData = nodeData.pid;
      } else if(nodeData.label === '表格') {
        this.getRequireCache('queryTable')
        this.componentData = nodeData.pid;
      } else {
        this.currentView = '';
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.queryComponent {
  width: 100%;
  &-left {
    background-color: #fff;
    height: calc(100vh - 186px);
    padding: 15px;
    float: left;
    width: 400px;
    .queryTree {
      border: none;
    }
  }
  &-right {
    float: left;
    margin-left: 20px;
    width: calc(100% - 480px);
    height: calc(100vh - 186px);
    background-color: #fff;
    padding: 15px;
    .no-choose > img {
      float: none;
      width: 25%;
      margin-bottom: 16px;
    }
  }
}
</style>
