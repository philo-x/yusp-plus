<!-- 
 * @description 产品选择器 YufpProductSelector
 * @param data-url 表格请求URL,默认值：'',如: /yusp_lp/api/util/getOrg?needFin=true&needManage=true&needDpt=true
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
    <yu-xdialog title="产品列表" :visible.sync="dialogVisible" width="640px">
      <div v-if="showQuery" class="xdialog-query">
        <label>产品列表</label>
        <yu-input placeholder="产品列表" v-model="filterText"></yu-input>
        <el-button type="primary" @click="searchFn">查询</el-button>
        <el-button @click="resetFn">重置</el-button>
      </div>
      <yu-xtable
        v-if="dataUrl"
        ref="productTable"
        :data-url="dataUrl"
        style="width: 100%; margin-bottom: 20px"
        row-key="id"
        border
        lazy
        :load="loadTable"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <yu-xtable-column
          prop="id"
          label="产品编码"
          width="180"
        ></yu-xtable-column>
        <yu-xtable-column
          prop="label"
          label="产品分类"
          width="180"
        ></yu-xtable-column>
        <yu-xtable-column prop="label" label="产品名称"></yu-xtable-column>
      </yu-xtable>
      <yu-xtable
        v-else
        ref="productTable"
        :data="productList"
        style="width: 100%; margin-bottom: 20px"
        row-key="id"
        border
        lazy
        :load="loadTable"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <yu-xtable-column
          prop="id"
          label="产品编码"
          width="180"
        ></yu-xtable-column>
        <yu-xtable-column
          prop="label"
          label="产品分类"
          width="180"
        ></yu-xtable-column>
        <yu-xtable-column prop="label" label="产品名称"></yu-xtable-column>
      </yu-xtable>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmFn">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </yu-xdialog>
  </div>
</template>
<script>
import productData from "./product.json";
import { clone } from '@/utils'
export default {
  name: "YufpProductSelector",
  props: {
    // 下述字段为el-input组件中部分属性，配置文档参见element-ui
    name: {
      type: String,
    },
    value: {
      type: String,
      default: "",
      required: true,
    },
    rawValue: String,
    size: String,
    disabled: {
      type: Boolean,
      default: false,
    },
    placeholder: {
      type: String,
      default: "",
    },
    icon: {
      type: String,
      default: "search",
    },
    showQuery: {
      type: Boolean,
      default: true,
    },
    dataUrl: String,
  },
  data: function () {
    return {
      dataList: [],
      productList: [],
      filterText: "",
      selectedVal: "",
      // 对话框是否可见变量
      dialogVisible: false,
    };
  },
  watch: {
    value: function (val) {
      // 将key转换为对应的value值
      if (!val) {
        this.selectedVal = "";
        return;
      }
      this.selectedVal = this.selectedVal ? this.selectedVal : val;
    },
    rawValue: function (val) {
      this.selectedVal = val;
    },
  },
  created() {
    this.handleData();
  },
  mounted: function () {
    // 有rawValue 时 将
    this.selectedVal = this.rawValue ? this.rawValue : this.value;
  },
  methods: {
    handleData: function () {
      // 处理初始化数据
      productData.forEach((item) => {
        const row = clone(item, {});
        delete row.children;
        row.hasChildren = true;
        this.productList.push(row);
      });
      clone(this.productList, this.dataList);
    },
    loadTable(treeNode, tree, resolve) {
      let tmpNodes = [];
      if (treeNode.pid) {
        tmpNodes = treeNode.children;
      } else {
        // 从数组中获取二级节点
        productData.forEach((item) => {
          if (item.id === treeNode.id) {
            item.children.forEach((n) => {
              const row = clone(n, {});
              row.hasChildren = true;
              tmpNodes.push(row);
            });
          }
        });
      }
      resolve(tmpNodes);
    },
    // input 框原生点击事件
    clickFn: function () {
      this.$emit("click-fn", this);
    },
    // input框上图标点击事件
    onIconClickFn: function (val) {
      if (this.disabled) {
        return;
      }
      this.dialogVisible = true;
    },
    confirmFn: function () {
      const refTable = this.$refs.productTable;
      if (refTable.selections.length === 0) {
        this.$message("请选择产品!");
      }
      const row = refTable.selections[0];
      delete row.children;
      // 获取 选中数据的标题等熟悉
      this.selectedVal = row.id;
      this.$emit("input", this.selectedVal);
      // 这个是你自定义返回的接口事件
      this.$emit("select-fn", row);
      this.dialogVisible = false;
    },
    // 对外提供选择器显示值
    getRawValue: function () {
      return this.selectedVal;
    },
    filterChildren: function (array) {
      if (Array.isArray(array) && array.length > 0) {
        var list = array.filter((item) => {
          if (this.filterText && item.label.indexOf(this.filterText) < 0) {
            return false;
          }
          // if (Array.isArray(item.children) && item.children.length > 0) {
          //   item.children = this.filterChildren();
          // }
          return true;
        });
      }
      return list;
    },
    // 查询表单搜索功能
    searchFn(event) {
      var list = this.filterChildren(this.dataList);
      this.productList = list;
      // this.$refs.productTable.filteTableColumns(this.filterText);
    },
    // 查询表单重置功能
    resetFn(event) {
      this.filterText = "";
    },
  },
};
</script>
<style lang="scss" scoped>
.xdialog-query {
  margin-bottom: 15px;
  > label {
    width: 120px;
    padding-right: 12px;
  }
  > .el-input {
    width: 50%;
  }
}
</style>
