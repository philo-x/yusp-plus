<template>
  <div class="mod-config">
    <yu-form :inline="true" :model="dataForm" class="demo-form-inline" @keyup.enter.native="getDataList()">
      <yu-form-item>
        <yu-input v-model="dataForm.key" placeholder="参数名" clearable></yu-input>
      </yu-form-item>
      <yu-form-item>
        <yu-button @click="getDataList()">查询</yu-button>
        <yu-button type="primary" @click="addOrUpdateHandle()">新增</yu-button>
        <yu-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</yu-button>
      </yu-form-item>
    </yu-form>

    <yu-xtable
      @cell-click="cellClick"
      selection-type="checkbox"
      :data="dataList"
      :pageable="true"
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
    >
      <yu-xtable-column prop="brandId" label="品牌id" width="120"> </yu-xtable-column>
      <yu-xtable-column prop="name" label="品牌名" width="300"> </yu-xtable-column>
      <yu-xtable-column prop="logo" label="品牌logo地址" width="120">
        <template slot-scope="scope">
          <img :src="scope.row.logo" style="width: 100px; height: 80px" />
        </template>
      </yu-xtable-column>
      <yu-xtable-column prop="descript" label="介绍" width="120"> </yu-xtable-column>
      <yu-xtable-column prop="showStatus" label="显示状态" width="200">
        <template slot-scope="scope">
          <yu-switch
            v-model="scope.row.showStatus"
            on-color="#13ce66"
            off-color="#ff4949"
            on-text="打开"
            off-text="关闭"
            :on-value="1"
            :off-value="0"
            @change="updateBrandStatus(scope.row)"
          ></yu-switch>
        </template>
      </yu-xtable-column>
      <yu-xtable-column fixed="right" label="操作">
        <template slot-scope="scope">
          <yu-button type="text" size="small" @click="updateCatelogHandle(scope.row.brandId)">关联分类</yu-button>
          <yu-button type="text" size="small" @click="addOrUpdateHandle(scope.row.brandId)">修改</yu-button>
          <yu-button type="text" size="small" @click="deleteHandle(scope.row.brandId)">删除</yu-button>
        </template>
      </yu-xtable-column>
    </yu-xtable>

    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>

    <yu-dialog title="关联分类" :visible.sync="cateRelationDialogVisible" width="30%">
      <!--trigger="manual",手动打开popover,避免popover在级联选择器选择的时候消失 -->
      <yu-popover placement="right-end" v-model="popCatelogSelectVisible" trigger="manual">
        <category-cascader :catelogPath.sync="catelogPath"></category-cascader>
        <div style="text-align: right; margin: 0">
          <yu-button size="mini" type="text" @click="popCatelogSelectVisible = false">取消</yu-button>
          <yu-button type="primary" size="mini" @click="addCatelogSelect">确定</yu-button>
        </div>
        <yu-button slot="reference" @click="popCatelogSelectVisible = true">新增关联</yu-button>
      </yu-popover>
      <yu-table :data="cateRelationTableData" style="width: 100%">
        <yu-table-column prop="id" label="#"></yu-table-column>
        <yu-table-column prop="brandName" label="品牌名"></yu-table-column>
        <yu-table-column prop="catelogName" label="分类名"></yu-table-column>
        <yu-table-column fixed="right" header-align="center" align="center" label="操作">
          <template slot-scope="scope">
            <yu-button type="text" size="small" @click="deleteCateRelationHandle(scope.row.id, scope.row.brandId)">移除</yu-button>
          </template>
        </yu-table-column>
      </yu-table>
      <span slot="footer" class="dialog-footer">
        <yu-button @click="cateRelationDialogVisible = false">取 消</yu-button>
        <yu-button type="primary" @click="cateRelationDialogVisible = false">确 定</yu-button>
      </span>
    </yu-dialog>
  </div>
</template>

<script>
import AddOrUpdate from "./brand-add-or-update";
import CategoryCascader from "../common/category-cascader";
export default {
  components: {
    AddOrUpdate,
    CategoryCascader,
  },
  data() {
    return {
      dataForm: {
        key: "",
      },
      brandId: 0,
      catelogPath: [],
      dataList: [],
      cateRelationTableData: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      cateRelationDialogVisible: false,
      popCatelogSelectVisible: false,
    };
  },
  activated() {
    this.getDataList();
  },
  methods: {
    addCatelogSelect() {
      //{"brandId":1,"catelogId":2}
      this.popCatelogSelectVisible = false;
      this.$request({
        method: "post",
        url: "/api/product/categorybrandrelation/save",
        data: { brandId: this.brandId, catelogId: this.catelogPath[this.catelogPath.length - 1] },
      }).then(() => {
        this.getCateRelation();
      });
    },
    deleteCateRelationHandle(id) {
      this.$request({
        method: "post",
        url: "/api/product/categorybrandrelation/delete",
        data: [id],
      }).then(() => {
        this.getCateRelation();
      });
    },
    updateCatelogHandle(brandId) {
      this.cateRelationDialogVisible = true;
      this.brandId = brandId;
      this.getCateRelation();
    },
    getCateRelation() {
      this.$request({
        method: "get",
        url: `/api/product/categorybrandrelation/catelog/list`,
        params: { brandId: this.brandId },
      }).then(({ data }) => {
        this.cateRelationTableData = data;
      });

    },
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;
      this.$request({
        method: "get",
        url: "/api/product/brand/list",
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
    updateBrandStatus(data) {
      // console.log("最新信息", data);
      let { brandId, showStatus } = data;

      //发送请求修改状态
      this.$request({
        method: "post",
        url: "/api/product/brand/update/status",
        data: { brandId, showStatus },
      }).then(() => {
        this.$message({
          type: "success",
          message: "状态更新成功",
        });
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
            return item.brandId;
          });
      this.$confirm(`确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$request({
          method: "post",
          url: "/api/product/brand/delete",
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
