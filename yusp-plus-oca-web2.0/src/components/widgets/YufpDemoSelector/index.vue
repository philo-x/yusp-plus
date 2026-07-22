<template>
  <div>
    <el-input v-model="selectedVal" readonly :placeholder="placeholder" :disabled="disabled" :size="size" :name="name" :icon="icon" :on-icon-click="onIconClickFn" @click.native="clickFn">
    </el-input>
    <el-dialog-x title="自定义示例选择器" :visible.sync="dialogVisible">
      <yu-xtable request-type="POST" ref="mytable" :data-url="dataUrl" @row-click="rowClickFn" :max-height="300">
        <yu-xtable-column label="编码" prop="id"></yu-xtable-column>
        <yu-xtable-column label="名称" prop="title" width="260" :sortable="true" :resizable="true"></yu-xtable-column>
        <yu-xtable-column label="类型" prop="type" width="110" dataCode="NATIONALITY"></yu-xtable-column>
        <yu-xtable-column label="作者" prop="author" width="110"></yu-xtable-column>
        <yu-xtable-column label="审核人" prop="auditor" width="110"></yu-xtable-column>
        <yu-xtable-column label="阅读数" prop="pageviews" width="100"></yu-xtable-column>
        <yu-xtable-column label="状态" prop="status" width="80" dataCode="PUBLISH_STATUS">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'deleted' ? 'danger' : 'success'">{{ scope.row.status }}</el-tag>
          </template>
        </yu-xtable-column>
      </yu-xtable>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmFn">确 定</el-button>
      </div>
    </el-dialog-x>
  </div>
</template>
<script>
/* eslint vue/require-prop-types:0 */
export default {
  name: 'YufpDemoSelector',
  props: {
    // 下述字段为el-input组件中部分属性，配置文档参见element-ui
    name: {
      type: String
    },
    value: {
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
      default: ''
    },
    icon: {
      type: String,
      default: 'search'
    },
    params: Object
  },
  data: function () {
    return {
      selectedVal: '',
      // 对话框是否可见变量
      dialogVisible: false,
      // 表格请求的url
      dataUrl: '/trade/example/list',
    };
  },
  watch: {
    value: function (val) {
      // 将key转换为对应的value值
      if (!val) {
        this.selectedVal = '';
        return;
      }
      this.selectedVal = this.selectedVal ? this.selectedVal : val;
    },
    rawValue: function (val) {
      this.selectedVal = val;
    }
  },
  mounted: function () {
    // 有rawValue 时 将
    this.selectedVal = this.rawValue ? this.rawValue : this.value;
  },
  methods: {
    // input 框原生点击事件
    clickFn: function () {
      this.$emit('click-fn', this);
    },
    // input框上图标点击事件
    onIconClickFn: function (val) {
      if (this.disabled) {
        return;
      }
      this.dialogVisible = true;
    },
    // 表格行点击事件
    rowClickFn: function (row) {
      // 将表格上选中的行数据复制给变量
      this.selections = this.$refs.mytable.selections;
    },
    confirmFn: function () {
      if (this.selections.length < 1) {
        this.$message('请先选择一条数据');
      }
      // 获取 选中数据的标题等熟悉
      this.selectedVal = this.selections[0].title;
      this.$emit('input', this.selections[0].id);
      // 这个是你自定义返回的接口事件
      this.$emit('select-fn', this.selections[0].id, this.selections[0]);
      this.dialogVisible = false;
    },
    // 对外提供选择器显示值
    getRawValue: function () {
      return this.selectedVal;
    },
    convertKey: function (val) {
      // 将key转换为对应的value值
      return val;
    }
  }
};
</script>
