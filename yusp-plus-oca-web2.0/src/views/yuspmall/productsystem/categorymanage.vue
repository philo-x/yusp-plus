<template>
  <div>
    <yu-switch v-model="draggable" on-text="ON" off-text="OFF"> </yu-switch>
    <yu-button v-if="draggable" @click="batchSave">批量保存</yu-button>
    <yu-button type="danger" @click="batchDelete">批量删除</yu-button>
    <yu-tree
      :data="menus"
      :props="defaultProps"
      @node-click="handleNodeClick"
      :render-content="renderContent"
      :expand-on-click-node="false"
      show-checkbox
      node-key="catId"
      :default-expanded-keys="expandedKey"
      :draggable="draggable"
      :allow-drop="allowDrop"
      @node-drop="handleDrop"
      ref="menuTree"
    >
    </yu-tree>

    <yu-dialog
      :modal-append-to-body="false"
      :append-to-body="false"
      :title="title"
      :draggable="draggable"
      :visible.sync="dialogVisible"
      size="tiny"
    >
      <yu-form :model="category">
        <yu-form-item label="分类名称">
          <yu-input v-model="category.name" auto-complete="off"></yu-input>
        </yu-form-item>
        <yu-form-item label="图标">
          <yu-input v-model="category.icon" auto-complete="off"></yu-input>
        </yu-form-item>
        <yu-form-item label="计量单位">
          <yu-input v-model="category.productUnit" auto-complete="off"></yu-input>
        </yu-form-item>
      </yu-form>
      <span slot="footer" class="dialog-footer">
        <yu-button @click="dialogVisible = false">取消</yu-button>
        <yu-button type="primary" @click="submitData">确定</yu-button>
      </span>
    </yu-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      pCid: [],
      draggable: true,
      updateNodes: [],
      maxLevel: 0,
      title: "",
      dialogType: "", // edit add
      category: {
        name: "",
        parentCid: 0,
        catLevel: 0,
        showStatus: 1,
        sort: 0,
        catId: null,
        icon: "",
        productUnit: "",
      },
      menus: [],
      expandedKey: [],
      dialogVisible: false,
      defaultProps: {
        children: "children",
        label: "name",
      },
    };
  },
  created() {
    this.getMenus();
  },
  methods: {
    batchDelete() {
      const catIds = [];
      const checkedNodes = this.$refs.menuTree.getCheckedNodes();
      for (let i = 0; i < checkedNodes.length; i++) {
        catIds.push(checkedNodes[i].catId);
      }

      this.$confirm(`是否批量删除【${catIds}】菜单？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$request({
            method: "post",
            url: "/api/product/category/delete",
            data: catIds
          }).then(() => {
            this.$message({
              message: "菜单批量删除成功",
              type: "success",
            });
            // 刷新出新的菜单
            this.getMenus();
          });
        })
        .catch(() => {});
    },
    batchSave() {
      this.$request({
        method: "post",
        url: "/api/product/category/update/sort",
        data: this.updateNodes,
      }).then(() => {
        this.$message({
          message: "菜单顺序修改成功",
          type: "success",
        });
        // 刷新出新的菜单
        this.getMenus();
        // 设置需要默认展开的菜单
        this.expandedKey = this.pCid;
        // 重置数据
        this.updateNodes = [];
        this.maxLevel = 0;
        // this.pCid = 0;
      });
    },
    handleNodeClick(data) {
      console.log(data);
    },
    getMenus() {
      this.$request({
        url: "/api/product/category/list/tree",
        method: "get",
      }).then(({ code, data, message }) => {
        if (code == "0") {
          // console.log("获取菜单数据：", data);
          this.menus = data;
        }
      });
    },
    handleDrop: function(draggingNode, dropNode, dropType, ev) {
      console.log("handleDrop:", draggingNode, dropNode, dropType);
      // 1. 当前节点最新的父节点id
      let pCid = 0;
      let siblings = null;
      if (dropType == "before" || dropType == "after") {
        pCid = dropNode.parent.data.catId == undefined ? 0 : dropNode.parent.data.catId;
        siblings = dropNode.parent.childNodes;
      } else {
        pCid = dropNode.data.catId;
        siblings = dropNode.childNodes;
      }
      this.pCid.push(pCid);

      // 2. 当前拖拽节点最新的顺序
      for (let i = 0; i < siblings.length; i++) {
        if (siblings[i].data.catId == draggingNode.data.catId) {
          // 如果遍历的当前节点是正在拖拽的节点
          let catLevel = draggingNode.level;
          if (siblings[i].level != draggingNode.level) {
            // 当前节点层级发生变化
            catLevel = siblings[i].level;
            // 继续修改子节点层级
            this.updateChildNodeLevel(siblings[i]);
          }
          this.updateNodes.push({
            catId: siblings[i].data.catId,
            sort: i,
            parentCid: pCid,
          });
        } else {
          this.updateNodes.push({ catId: siblings[i].data.catId, sort: i });
        }
      }

      // 3. 当前拖拽节点的最新层级
      console.log("updateNodes:", this.updateNodes);
    },
    updateChildNodeLevel(node) {
      if (node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          var cNode = node.childNodes[i].data;
          this.updateNodes.push({ catId: cNode.catId, catLevel: node.childNodes[i].level });
          this.updateChildNodeLevel(node.childNodes[i]);
        }
      }
    },
    allowDrop(draggingNode, dropNode, type) {
      // 1. 被拖动的当前节点以及所在的父节点总层数不能大于3

      // 1.1 被拖动的当前节点总层数
      console.log("allowDrop:", draggingNode, dropNode, type);
      //
      this.countNodeLevel(draggingNode);
      // 当前正在拖动的节点+父节点所在的深度不大于3即可
      const deep = Math.abs(this.maxLevel - draggingNode.level) + 1;
      // console.log(
      //   `this.maxLevel: ${this.maxLevel}; draggingNode.data.catLevel: ${draggingNode.data.catLevel};dropNode.level: ${
      //     dropNode.level
      //   } `
      // );
      console.log("深度：", deep);

      if (type == "inner") {
        return deep + dropNode.level <= 3;
      } else {
        return deep + dropNode.parent.level <= 3;
      }
    },
    countNodeLevel(node) {
      // 找到所有子节点，求出最大深度
      if (node.childNodes != null && node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          if (node.childNodes[i].level > this.maxLevel) {
            this.maxLevel = node.childNodes[i].level;
          }
          this.countNodeLevel(node.childNodes[i]);
        }
      }
    },
    submitData() {
      if (this.dialogType == "add") {
        this.addCategory();
      }
      if (this.dialogType == "edit") {
        this.editCategory();
      }
    },
    // 修改三级分类
    editCategory() {
      var { catId, name, icon, productUnit } = this.category;
      this.$request({
        method: "post",
        url: "/api/product/category/update",
        data: { catId, name, icon, productUnit },
      }).then(() => {
        this.$message({
          message: "菜单修改成功",
          type: "success",
        });
        // 关闭对话框
        this.dialogVisible = false;
        // 刷新出新的菜单
        this.getMenus();
        // 设置需要默认展开的菜单
        this.expandedKey = [this.category.parentCid];
      });
    },
    edit(node, data) {
      console.log("要修改的数据", node, data);
      this.title = "修改分类";
      this.dialogType = "edit";
      this.dialogVisible = true;

      // 发送请求获取当前节点的最新数据
      this.$request({
        method: "get",
        url: `/api/product/category/info/${data.catId}`,
      }).then(({ data }) => {
        // 请求成功
        console.log("要回显的数据", data);
        this.category.name = data.name;
        this.category.catId = data.catId;
        this.category.icon = data.icon;
        this.category.productUnit = data.productUnit;
        this.category.parentCid = data.parentCid;
        this.category.catLevel = data.catLevel;
        this.category.sort = data.sort;
        this.category.showStatus = data.showStatus;
      });
    },
    // 添加三级分类
    addCategory() {
      console.log("添加的数据", this.category);
      this.$request({
        method: "post",
        url: "/api/product/category/save",
        data: this.category,
      }).then(({ code, message, data }) => {
        if (code == "0") {
          this.$message({
            message: "添加三级分类成功",
            type: "success",
          });
          // 关闭对话框
          this.dialogVisible = false;
          // 刷新出新的菜单
          this.getMenus();
          // 设置需要默认展开的菜单
          this.expandedKey = [this.category.parentCid];
        } else {
          this.$message({
            message: "菜单删除失败",
            type: "error",
          });
        }
      });
    },
    append(node, data) {
      console.log("append", node, data);
      this.dialogVisible = true;
      this.dialogType = "add";
      this.title = "添加分类";

      this.category.parentCid = data.catId;
      this.category.catLevel = data.catLevel * 1 + 1;
      this.category.catId = null;
      this.category.name = "";
      this.category.icon = "";
      this.category.productUnit = "";
      this.category.sort = 0;
      this.category.showStatus = 1;
    },

    remove(node, data) {
      console.log("remove", node, data);
      var ids = [data.catId];
      this.$confirm(`是否删除【${data.name}】菜单？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$request({
            method: "POST",
            url: "/api/product/category/delete",
            data: ids,
          }).then(({ code, message, data }) => {
            if (code == "0") {
              this.$message({
                message: "菜单删除成功",
                type: "success",
              });
              // 刷新出新的菜单
              this.getMenus();
              // 设置需要默认展开的菜单
              this.expandedKey = [node.parent.data.catId];
            } else {
              this.$message({
                message: "菜单删除失败",
                type: "error",
              });
            }
          });
        })
        .catch(() => {
          return;
        });
    },

    renderContent(h, { node, data, store }) {
      var _this = this;

      var btnArray = [];

      if (node.level <= 2) {
        btnArray.push({
          name: "Append",
          callback: function() {
            _this.append(node, data);
          },
        });
      }

      if (node.childNodes.length == 0) {
        btnArray.push({
          name: "Delete",
          callback: function() {
            _this.remove(node, data);
          },
        });
      }

      // 修改按钮在任何情况下都会出现
      btnArray.push({
        name: "Edit",
        callback: function() {
          _this.edit(node, data);
        },
      });

      return h("span", {}, [
        h("span", {}, [h("span", {}, node.label)]),
        h(
          "span",
          { attrs: { style: "float: right; margin-right: 30px" } },
          btnArray.map(function(item) {
            return h("yu-button", { props: { size: "mini" }, on: { click: item.callback } }, item.name);
          })
        ),
      ]);
    },
  },
};
</script>

<style scoped></style>
