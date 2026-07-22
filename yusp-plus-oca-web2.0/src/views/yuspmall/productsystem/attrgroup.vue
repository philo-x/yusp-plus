<template>
  <yu-row :gutter="20">
    <yu-col :span="6">
      <category @tree-node-click="treenodeclick"></category>
    </yu-col>
    <yu-col :span="18">
      <div class="mod-config">
        <yu-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
          <yu-form-item>
            <yu-input v-model="dataForm.key" placeholder="参数名" clearable></yu-input>
          </yu-form-item>
          <yu-form-item>
            <yu-button @click="getDataList()">查询</yu-button>
            <yu-button type="success" @click="getAllDataList()">查询全部</yu-button>
            <yu-button type="primary" @click="addOrUpdateHandle()">新增</yu-button>
            <yu-button
              type="danger"
              @click="deleteHandle()"
              :disabled="dataListSelections.length <= 0">批量删除</yu-button>
          </yu-form-item>
        </yu-form>
        <yu-table
          :data="dataList"
          border
          v-loading="dataListLoading"
          @selection-change="selectionChangeHandle"
          style="width: 100%;"
        >
          <yu-table-column type="selection" header-align="center" align="center" width="50"></yu-table-column>
          <yu-table-column prop="attrGroupId" header-align="center" align="center" label="分组id"></yu-table-column>
          <yu-table-column prop="attrGroupName" header-align="center" align="center" label="组名"></yu-table-column>
          <yu-table-column prop="sort" header-align="center" align="center" label="排序"></yu-table-column>
          <yu-table-column prop="descript" header-align="center" align="center" label="描述"></yu-table-column>
          <yu-table-column prop="icon" header-align="center" align="center" label="组图标"></yu-table-column>
          <yu-table-column prop="catelogId" header-align="center" align="center" label="所属分类id"></yu-table-column>
          <yu-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
            <template slot-scope="scope">
              <yu-button type="text" size="small" @click="relationHandle(scope.row.attrGroupId)">关联</yu-button>
              <yu-button type="text" size="small" @click="addOrUpdateHandle(scope.row.attrGroupId)">修改</yu-button>
              <yu-button type="text" size="small" @click="deleteHandle(scope.row.attrGroupId)">删除</yu-button>
            </template>
          </yu-table-column>
        </yu-table>
        <yu-pagination
          @size-change="sizeChangeHandle"
          @current-change="currentChangeHandle"
          :current-page="pageIndex"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          :total="totalPage"
          layout="total, sizes, prev, pager, next, jumper"
        ></yu-pagination>
        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>

        <!-- 修改关联关系 -->
        <relation-update v-if="relationVisible" ref="relationUpdate" @refreshData="getDataList"></relation-update>
      </div>
    </yu-col>
  </yu-row>
</template>

<script>
/**
 * 父子组件传递数据
 * 1)、子组件给父组件传递数据，事件机制；
 *    子组件给父组件发送一个事件，携带上数据。
 * // this.$emit("事件名",携带的数据...)
 */
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import Category from "../common/category";
import AddOrUpdate from "./attrgroup-add-or-update";
import RelationUpdate from "./attr-group-relation";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: { Category, AddOrUpdate, RelationUpdate },
  props: {},
  data() {
    return {
      catId: 0,
      dataForm: {
        key: "",
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      relationVisible: false,
    };
  },
  activated() {
    this.getDataList();
  },
  methods: {
    //处理分组与属性的关联
    relationHandle(groupId) {
      this.relationVisible = true;
      this.$nextTick(() => {
        this.$refs.relationUpdate.init(groupId);
      });
    },
    //感知树节点被点击
    treenodeclick(data, node, component) {
      if (node.level == 3) {
        this.catId = data.catId;
        this.getDataList(); //重新查询
      }
    },
    getAllDataList() {
      this.catId = 0;
      this.getDataList();
    },
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;
      this.$request({
        method: "get",
        url: `/api/product/attrgroup/list/${this.catId}`,
        params: { page: this.pageIndex, size: this.pageSize, key: this.dataForm.key },
      }).then(({ code, data, total }) => {
        if (code == "0") {
          this.dataList = data;
          this.totalPage = total;
        } else {
          this.dataList = [];
          this.totalPage = total;
        }
        this.dataListLoading = false;
      });
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true;
      // 当要显示的组件完全渲染以后，再来调用里面的方法
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id);
      });
    },
    // 删除
    deleteHandle(id) {
      var ids = id ? [id] : this.dataListSelections.map((item) => {return item.attrGroupId;});
      this.$confirm(`确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$request({
          method: "post",
          url: "/api/product/attrgroup/delete",
          data: ids,
        }).then(({ code, message }) => {
          if (code == "0") {
            this.$message({
              message: "操作成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.getDataList();
              },
            });
          } else {
            this.$message.error(message);
          }
        });
      });
    },
  },
};
</script>
<style scoped></style>
