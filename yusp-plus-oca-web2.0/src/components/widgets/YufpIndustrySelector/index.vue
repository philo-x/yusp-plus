<!-- 
 * @description 表单行业选择器 YufpIndustrySelector
 * @param data-url 表格请求URL, 默认值：'' , 如: /yusp_lp/api/util/getOrg?needFin=true&needManage=true&needDpt=true
 * @param showQuery 是否显示查询条件框,默认值：true
 * @param icon 输入框图标,默认值：search
 * @param placeholder 输入框提示文本,默认值：''
 * @param disabled 输入框是否禁止输入,默认值：false
 * @param size 输入框大小,默认值：
 * @param rawValue 输入框值,默认值：
 * @param value 输入框值,默认值：
 * @param name 输入框名称,默认值：
 * @param $emit: select-fn / input 发射事件
 * @authors kongqf
 * @date    2020-09-23 19:20:41
 * @version $1.0$
 -->
<template>
  <div>
    <el-input
      v-model="selectedVal"
      readonly
      :placeholder="placeholder"
      :disabled="disabled"
      :size="size"
      :name="name"
      :icon="icon"
      :on-icon-click="onIconClickFn"
      @click.native="clickFn"
    ></el-input>
    <yu-xdialog title="行业分类" :visible.sync="dialogVisible" width="640px">
      <yu-industry-classfy :show-query="showQuery" :xtree-config="xtreeConfig"></yu-industry-classfy>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmFn">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>      
    </yu-xdialog>
  </div>
</template>
<script>
export default {
  name: "YufpIndustrySelector",
  props: {
    // 下述字段为el-input组件中部分属性，配置文档参见element-ui
    name: {
      type: String
    },
    value: {
      type: String,
      default: "",
      required: true
    },
    rawValue: String,
    size: String,
    disabled: {
      type: Boolean,
      default: false
    },
    placeholder: {
      type: String,
      default: ""
    },
    icon: {
      type: String,
      default: "search"
    },
    showQuery: {
      type: Boolean,
      default: true
    },
    dataUrl: String
  },
  data: function() {
    return {
      industryData: [],
      filterText: "",
      selectedVal: "",
      // 对话框是否可见变量
      dialogVisible: false,
      selectNode: null,
      xtreeConfig: {
        dataUrl: this.dataUrl,
        events: {
          'node-click': this.nodeClickFn
        }
      }
    };
  },
  watch: {
    value: function(val) {
      // 将key转换为对应的value值
      if (!val) {
        this.selectedVal = "";
        return;
      }
      this.selectedVal = this.selectedVal ? this.selectedVal : val;
    },
    rawValue: function(val) {
      this.selectedVal = val;
    },
    filterText: function (val) {
      this.$refs.industryTree.filter(val);
    }
  },
  mounted: function() {
    // 有rawValue 时 将
    this.selectedVal = this.rawValue ? this.rawValue : this.value;
  },
  methods: {
    // 树节点单击事件
    nodeClickFn(node) {
      this.selectNode = node;
    },
    // input 框原生点击事件
    clickFn: function() {
      this.$emit("click-fn", this);
    },
    // input框上图标点击事件
    onIconClickFn: function(val) {
      if (this.disabled) {
        return;
      }
      this.dialogVisible = true;
    },
    confirmFn: function() {
      if (!this.selectNode) {
        this.$message("请选择类型!");
      }
      delete this.selectNode.children;
      // 获取 选中数据的标题等熟悉
      this.selectedVal = this.selectNode.id;
      this.$emit("input", this.selectedVal);
      // 这个是你自定义返回的接口事件
      this.$emit("select-fn", this.selectNode);
      this.dialogVisible = false;
    },
    // 对外提供选择器显示值
    getRawValue: function() {
      return this.selectedVal;
    },
    // 查询表单搜索功能
    searchFn(event) {
      this.$refs.industryTree.filter(this.filterText);
    },
    // 查询表单重置功能
    resetFn(event) {
      this.filterText = '';
    }
  }
};
</script>
<style lang="scss" scoped>
.xdialog-query{
  margin-bottom: 15px;
  >label{
    width: 120px;
    padding-right: 12px;
  }
  >.el-input{
    width: 50%;
  }
}
</style>
