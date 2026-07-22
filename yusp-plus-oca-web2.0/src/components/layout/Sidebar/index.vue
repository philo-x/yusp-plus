<template>
  <div class="yu-frame-menu" @mouseenter="handleMouseEnter" @mouseleave="handleMouseLeave">
    <logo v-if="showLogo && !isTop" :is-collapse="isCollapse" />
    <span class="yu-frame-tabs-unfold" @click="unfoldClickFn()" title="展开">
      <i class="yu-frame-tabs-unfold-icon yu-icon-down"></i>
    </span>
    <div
      v-if="(listW > menuScrollW)&&isTop"
      class="yu-top-menu-left-icon yu-icon-arr-left1"
      @click="rightArrowClickFn"
      title="向前"
    ></div>
    <div
      ref="menuScrollarea"
      class="yu-menu-horizontal-scrollarea"
      :class="{'yu-menu-horizontal-scrollbar':menuModel.id==='topTree'||menuModel.id==='topTile'}"
    >
      <el-scrollbar>
        <!-- 去掉手动收起功能:collapse="isCollapse"-->
        <el-menu
          :default-active="activeMenu"
          :class="menuClass"
          :text-color="variables.menuText"
          :active-text-color="variables.menuActiveText"
          :collapse-transition="false"
          :unique-opened="true"
          :mode="mode"
          :style="{width: menuSize.menuWidth, height: menuSize.menuHeight}"
        >
          <sidebar-item
            v-for="route in routes"
            :key="route.name"
            :item="route"
            :base-path="route.path"
            :submenu-maxheight="sidebarMaxHeight"
          />
        </el-menu>
      </el-scrollbar>
    </div>
    <div
      v-if="(listW > menuScrollW)&&isTop"
      class="yu-top-menu-right-icon yu-icon-arr-right1"
      @click="leftArrowClickFn"
      title="向后"
    ></div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Logo from "./Logo.vue";
// import YuBaseMenu from './YuBaseMenu.vue'
import SidebarItem from "./SidebarItem";
import variables from "@/assets/styles/variables.scss";
import { clone } from '@/utils'

export default {
  components: { SidebarItem, Logo },
  props: {
    // 设置每次只展开一个子菜单
    unique: Boolean,
    // 所有菜单项是否默认展开，true：默认展开，false：默认收缩
    defaultOpen: Boolean,
    // 菜单项是否可拖动
    draggable: Boolean,
  },
  data() {
    return {
      // menuModel: {},
      listW: 0,
      // 顶部菜单可见区域宽度
      menuScrollW: 0,
      // 顶部菜单，可滑动区域内菜单的left值
      topMenuLeft: 0,
      menuSize: {
        // 菜单宽度
        menuWidth: "",
        // 菜单高度
        menuHeight: "",
      },
      sidebarMaxHeight: "",
    };
  },
  computed: {
    ...mapGetters([
      "routes",
      "originalMenus",
      "menuModel",
      "menuShowStat",
      "sidebar",
    ]),
    activeMenu() {
      const route = this.$route;
      const { meta, path, params} = route;
      // if set path, the sidebar will highlight the path you set
      if (params.activeMenu) {
        return params.activeMenu;
      }
      if(meta.activeMenu) {
        return meta.activeMenu
      }
      return path;
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo;
    },
    variables() {
      return variables;
    },
    isCollapse() {
      if (this.isTop) return true;
      return !this.sidebar.opened;
    },
    // 判断菜单模式是否是左侧
    isLeft() {
      return this.menuModel.id === "left";
    },
    // 判断菜单模式是否是右侧菜单
    isRight() {
      return this.menuModel.id === "right";
    },
    // 判断菜单模式是否是垂直的
    isVertical() {
      return this.menuModel.id === "left" || this.menuModel.id === "right";
    },
    // 是否是顶部菜单
    isTop() {
      return this.menuModel.id === "topTile" || this.menuModel.id === "topTree";
    },
    menuClass() {
      if (this.menuModel.id === "topTile" || this.menuModel.id === "topTree") {
        const cls = ["el-menu--horizontal"];
        this.menuModel.id === "topTile" && cls.push("tile-menu");
        this.menuModel.id === "topTree" && cls.push("tree-menu");
        return cls.join(" ");
      }
      if(this.menuModel.id === 'left' || this.menuModel.id === 'right') {
        const cls = [this.isCollapse ? 'el-menu--hide-sub-menu' : ''];
        return cls.join(" ");
      }
      return "";
    },
    mode() {
      const m =
        this.menuModel.id === "topTile" || this.menuModel.id === "topTree"
          ? "horizontal"
          : "vertical";
      return m;
    },
  },
  mounted: function () {
    if (this.menuModel.id === "topTile" || this.menuModel.id === "topTree") {
      this.calMenuSize(this.menuModel.id);
      this.getMenuSize(this.menuModel);
    }
  },
  methods: {
    handleMouseEnter() {
      if (
        (this.menuModel.id === "left" || this.menuModel.id === "right") &&
        this.menuShowStat === 2
      ) {
        this.$store.commit("app/TOGGLE_SIDEBAR");
        this.$store.commit("app/SET_MENU_SHOW_STAT", 3);
      }
      this.sidebarMaxHeight =
        Math.round((document.body.clientHeight - 160) * 0.9) + "px";
    },
    handleMouseLeave() {
      if (
        (this.menuModel.id === "left" || this.menuModel.id === "right") &&
        this.menuShowStat === 3
      ) {
        this.$store.commit("app/TOGGLE_SIDEBAR");
        this.$store.commit("app/SET_MENU_SHOW_STAT", 2);
      }
    },
    /**
     * 折叠事件
     */
    unfoldClickFn: function () {
      // 显示折叠按钮
      var toolbar = document.getElementsByClassName("yu-frame-top-bar")[0];
      toolbar.setAttribute("class", "yu-frame-top-bar ");
      // 设置高度
      var tab = document.getElementsByClassName("yu-frame-tab")[0];
      tab.setAttribute("class", "yu-frame-tab");
      // 恢复div初始class
      var tabs = document.getElementsByClassName("yu-frame-tabs")[0];
      tabs.setAttribute("class", "yu-frame-tabs");
      // 设置菜单高度样式
      const frameMenu = document.getElementsByClassName("yu-frame-menu")[0];
      frameMenu.setAttribute("class", "yu-frame-menu");
      // 触发pickup事件处理页面吸顶操作
      this.$emit("pickup", false);
    },
    /**
     * 顶部菜单左箭头点击事件处理程序
     */
    leftArrowClickFn: function () {
      // 顶部菜单，所有菜单项的宽度总和
      let listW = 0;
      let domLi;
      const menuRootList = document.querySelectorAll(
        ".el-menu.el-menu--horizontal.tree-menu > .menu-wrapper > a > li,.el-menu.el-menu--horizontal.tree-menu > .menu-wrapper > li"
      );
      for (let i = 0, len = menuRootList.length; i < len; i++) {
        domLi = menuRootList[i];
        listW += domLi.clientWidth;
      }
      if (listW > this.menuScrollW) {
        for (let i = 0, len = menuRootList.length; i < len; i++) {
          domLi = menuRootList[i];
          if (domLi.style.display !== "none") {
            domLi.style.display = "none";
            break;
          }
        }
      }
    },
    /**
     * 顶部菜单左箭头点击事件处理程序
     */
    rightArrowClickFn: function () {
      let domLi;
      const menuRootList = document.querySelectorAll(
        ".el-menu.el-menu--horizontal.tree-menu > .menu-wrapper > a > li,.el-menu.el-menu--horizontal.tree-menu > .menu-wrapper > li"
      );
      for (let i = menuRootList.length - 1; i >= 0; i--) {
        domLi = menuRootList[i];
        if (domLi.style.display === "none") {
          domLi.style.display = "block";
          break;
        }
      }
    },
    getMenuSize: function (val) {
      this.$store.commit("app/SET_MENU_MODEL", val);

      this.listW = 0;
      if (val.id === "topTile" || val.id === "topTree") {
        // 水平状态下的
        this.$nextTick(function () {
          var menuRootList = document.querySelectorAll(
            ".el-menu.el-menu--horizontal.tree-menu > .menu-wrapper > a > li,.el-menu.el-menu--horizontal.tree-menu > .menu-wrapper > li"
          );
          this.menuScrollW = this.$refs.menuScrollarea.clientWidth - 24 * 2; // #TODO 此处需要根据是否紧凑模式调整值
          for (var i = 0, len = menuRootList.length; i < len; i++) {
            var domLi = menuRootList[i];
            this.listW += domLi.clientWidth;
          }
        });
      }
    },
    calMenuSize: function (menuMode) {
      if (menuMode === "topTile" || menuMode === "topTree") {
        // 需要减掉左右两侧得边距
        this.menuSize.menuWidth = window.innerWidth - 24 * 2 + "px"; // #TODO 此处需要根据是否紧凑模式调整值
        this.menuSize.menuHeight = "100%";
      } else {
        this.menuSize.menuWidth = "100%";
        this.menuSize.menuHeight = window.innerHeight + "px";
      }
    },
  },
};
</script>
<style lang='scss'>
.yu-frame-menu .el-menu {
  background-color: rgba(0, 0, 0, 0);
}

.yu-menu-horizontal-scrollarea
  > .el-scrollbar
  .el-menu.tree-menu.el-menu--horizontal {
  display: flex;
  white-space: nowrap;
}

.yu-menu-horizontal-scrollarea {
  height: 100%;
}

.yu-menu-horizontal-scrollbar > .el-scrollbar {
  margin: 0 24px;
  overflow: visible;
  > .el-scrollbar__wrap {
    overflow: visible;
  }
  .el-scrollbar__bar.is-horizontal {
    display: none;
  }
}

.yu-frame-menu-pickup:hover .yu-frame-tabs-unfold {
  display: inline-block;
  background: rgba(255, 255, 255, 0.15);
  color: rgba(255, 255, 255, 0.8);
}
</style>