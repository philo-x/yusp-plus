<template>
  <div class="mod-config">
    <yu-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <yu-form-item>
        <yu-input v-model="dataForm.key" placeholder="参数名" clearable></yu-input>
      </yu-form-item>
      <yu-form-item>
        <yu-button @click="getDataList()">查询</yu-button>
        <yu-button type="primary" @click="addOrUpdateHandle()">新增</yu-button>
        <yu-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</yu-button>
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
      <yu-table-column prop="id" header-align="center" align="center" label="id"></yu-table-column>
      <yu-table-column prop="name" header-align="center" align="center" label="等级名称"></yu-table-column>
      <yu-table-column prop="growthPoint" header-align="center" align="center" label="所需成长值"></yu-table-column>
      <yu-table-column prop="defaultStatus" header-align="center" align="center" label="默认等级">
        <template slot-scope="scope">
          <i class="el-icon-circle-check" v-if="scope.row.defaultStatus == 1"></i>
          <i class="el-icon-circle-cross" v-else></i>
        </template>
      </yu-table-column>
      <yu-table-column
        prop="freeFreightPoint"
        header-align="center"
        align="center"
        label="免运费标准"
      ></yu-table-column>
      <yu-table-column
        prop="commentGrowthPoint"
        header-align="center"
        align="center"
        label="每次评价获取的成长值"
      ></yu-table-column>
      <yu-table-column label="特权">
        <yu-table-column prop="priviledgeFreeFreight" header-align="center" align="center" label="免邮特权">
          <template slot-scope="scope">
            <i class="el-icon-circle-check" v-if="scope.row.priviledgeFreeFreight == 1"></i>
            <i class="el-icon-circle-cross" v-else></i>
          </template>
        </yu-table-column>
        <yu-table-column prop="priviledgeMemberPrice" header-align="center" align="center" label="会员价格特权">
          <template slot-scope="scope">
            <i class="el-icon-circle-check" v-if="scope.row.priviledgeMemberPrice == 1"></i>
            <i class="el-icon-circle-cross" v-else></i>
          </template>
        </yu-table-column>
        <yu-table-column prop="priviledgeBirthday" header-align="center" align="center" label="生日特权">
          <template slot-scope="scope">
            <i class="el-icon-circle-check" v-if="scope.row.priviledgeBirthday == 1"></i>
            <i class="el-icon-circle-cross" v-else></i>
          </template>
        </yu-table-column>
      </yu-table-column>
      <yu-table-column prop="note" header-align="center" align="center" label="备注"></yu-table-column>
      <yu-table-column fixed="right" header-align="center" align="center" width="150" label="操作">
        <template slot-scope="scope">
          <yu-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</yu-button>
          <yu-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</yu-button>
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
  </div>
</template>

<script>
import AddOrUpdate from "./memberlevel-add-or-update";
export default {
  components: {
    AddOrUpdate,
  },
  data() {
    return {
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
    };
  },
  activated() {
    this.getDataList();
  },
  methods: {
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;

      this.$request({
        method: "get",
        url: "/api/member/memberlevel/list",
        params: { page: this.pageIndex, size: this.pageSize, key: this.dataForm.key },
      }).then(({ code, data, total }) => {
        if (code == "0") {
          this.dataList = data;
          this.totalPage = total;
        } else {
          this.dataList = [];
          this.totalPage = 0;
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
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id);
      });
    },
    // 删除
    deleteHandle(id) {
      var ids = id
        ? [id]
        : this.dataListSelections.map((item) => {
          return item.id;
        });
      this.$confirm(`确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$request({
          method: "post",
          url: "/api/member/memberlevel/delete",
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
