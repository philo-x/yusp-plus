<template>
  <div class="cust-self-call-menu-template" :class="{'has-child': hasChild}">
    <template v-for="(child,i) in menuChildren" :idx="i">
      <el-submenu v-if="justChildren(child)" :start-padding-level="1" :title="child.mText" :ref="'refsubmenu_'+child.mId" :index="child.mId" :key="child.mId" :class="{'yu-root-level': child.mPid === '0','is-active': child.mId === tileTopMenuId}">
        <template slot="title">
          <i v-if="rootMenu.mode === 'vertical'" :class="[child.mIcon]"></i>
          <span slot="title">{{ child.mText }}</span>
        </template>
        <!-- 组件递归调用 -->
        <yu-base-menu-self-call v-if="submenuMode === 'tree'" :menu-children="child.children" :submenu-mode="submenuMode"></yu-base-menu-self-call>
        <template v-else>
          <!-- 平铺菜单 -->
          <yu-base-menu-tile-item v-for="(tileMenuData, index) in child.children" :key="index">
            <span slot="title" @click="topTileItemClickFn(tileMenuData, $event, tileMenuData)" :class="{'title-item-cursor': tileMenuData.routeId != ''}">{{ tileMenuData.mText }}</span>
            <li :class="['tile-item', {'is-active': activeTileItem(item.id)}]" v-for="(item, idx) in tileMenuData.children" :key="idx" @click="topTileItemClickFn(item, $event, tileMenuData)" :title="item.mText">{{ item.mText }}</li>
          </yu-base-menu-tile-item>
        </template>
      </el-submenu>
      <el-menu-item v-else :index="child.mId" :idx="i" :key="child.mId" :start-padding-level="1" :menu-item-data="child" :menu-right-list-data="menurightListData" @menu-rightlist-click="menurightListClickFn">
        <i v-if="rootMenu.mode === 'vertical'" :class="[child.mIcon]"></i>
        <span slot="title" :title="child.mText">{{ child.mText }}</span>
        <!-- 菜单项右键列表 -->
        <template slot="menuRightList"></template>
      </el-menu-item>
    </template>
  </div>
</template>
<script>
/* eslint vars-on-top:0 */
/* eslint no-inner-declarations:0 */
export default {
  name: "YuBaseMenuSelfCall",
  props: {
    // 菜单数据
    menuChildren: Array,
    // 子菜单模式，树形（tree）/平铺(tile)
    submenuMode: String
  },
  data: function() {
    return {
      shown: false,
      // 菜单项右键列表
      menurightListData: [
        {
          index: 1,
          icon: "el-icon-menu",
          title: "属性1"
        },
        {
          index: 2,
          icon: "el-icon-menu",
          title: "属性2"
        }
      ],
      // 菜单tile 模式时存储当前根几点菜单id
      tileTopMenuId: "",
      // 顶部平铺菜单高亮的菜单项id
      activeId: ""
    };
  },
  computed: {
    /**
     * 找到当前组件的最外层为yu-base-menu的组件
     */
    rootMenu: function() {
      var parent = this.$parent;
      while (parent && parent.$options.componentName !== "YuBaseMenu") {
        parent = parent.$parent;
      }
      return parent;
    },
    hasChild: function() {
      var hasChild = false;
      this.menuChildren.forEach(function(item) {
        if (item.children && item.children.length > 0) {
          hasChild = true;
        }
      });
      return hasChild;
    }
  },
  mounted() {
    console.log('mulu ', this.menuChildren)
  },
  methods: {
    /**
     * 判断树结构数据中的某一项是否有children属性
     */
    justChildren: function(child) {
      return child.children && child.children.length !== 0;
    },
    /**
     * 右击事件处理程序
     */
    rightClickFn: function() {
      this.shown = true;
    },
    /**
     * 菜单项右击列表，列表项的点击事件处理程序
     * @param index 列表项唯一标识
     * @param menuData 菜单项对象
     * @param e 原生DOM事件对象
     */
    menurightListClickFn: function(index, menuData, e) {
      if (index === 1) {
        // this.$message({message: JSON.stringify(menuData)});
      } else {
        // this.$message({message: '列表点击事件' + index});
      }
    },
    /**
     * 顶部平铺菜单，菜单项点击事件
     * @param menu 菜单项
     * @param event 原生事件对象
     * @param menuData 当菜单为二级时数据为菜单数据本身，当为三级菜单时数据为父级菜单数据对象
     */
    topTileItemClickFn: function(menu, event, menuData) {
      this.$refs[
        "refsubmenu_" + menuData.mPid
      ][0].$el.children[1].style.display =
        "none";
      this.tileTopMenuId = menuData.mPid;
      var menuId = menu.id;
      this.activeId = menuId;
      if (menu.routeId !== "") {
        this.rootMenu.$emit("tile-item-click", menuId);
      }
    },
    /**
     * 高亮顶部菜单项
     * @param menuId 菜单id
     */
    activeTileItem: function(menuId) {
      return this.activeId === menuId;
    }
  }
};
</script>
