<template>
  <div v-show="!item.hidden" class="menu-wrapper" :class="{'yu-root-menu-wrapper':item.path==='/'|| (item.meta && item.meta.pid==0)}">
    <template
      v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)&&!item.alwaysShow"
    >
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)" @click.native.stop="clickTag(item)">
        <el-menu-item
          :index="resolvePath(onlyOneChild.path)"
          :class="{'submenu-title-noDropdown':!isNest}"
        >
          <item
            v-if="menuModel.id==='topTile' || menuModel.id==='topTree'"
            :level="!isNest ? 1 : -1"
            :title="generateTitle(onlyOneChild.meta.title)"
          />
          <item
            v-else
            :icon="onlyOneChild.meta.icon||(item.meta&&item.meta.icon)"
            :level="!isNest ? 1 : -1"
            :title="generateTitle(onlyOneChild.meta.title)"
            :titleclass="onlyOneChild.meta.title==='dashboard'?'yu-sidebar-item-title-vertical':''"
          />
        </el-menu-item>
      </app-link>
    </template>
    <el-submenu
      v-else-if="menuModel.id === 'topTile'"
      ref="subMenu"
      class="yu-root-level"
      :index="resolvePath(item.path)"
      popper-append-to-body
    >
      <template slot="title">
        <item
          v-if="item.meta"
          :icon="item.meta && item.meta.icon"
          :title="generateTitle(item.meta.title)"
        />
      </template>
      <template>
        <yu-base-menu-tile-item v-for="tileMenuData in item.children" :key="tileMenuData.path">
          <span slot="title" class="no-children">
            <template
              v-if="tileMenuData.children && tileMenuData.children.length>0"
            >{{ generateTitle(tileMenuData.meta.title) }}</template>
            <template v-else>
              <app-link v-if="tileMenuData.meta" :to="resolvePath(tileMenuData.path)">
                <span :index="resolvePath(tileMenuData.path)" class="title-menu">
                  <item
                    :icon="tileMenuData.meta.icon||(tileMenuData.meta&&tileMenuData.meta.icon)"
                    :title="generateTitle(tileMenuData.meta.title)"
                  />
                </span>
              </app-link>
            </template>
          </span>
          <li
            :class="['tile-item']"
            v-for="(it, idx) in getChildrenFilter(tileMenuData)"
            :key="'li-' + idx"
          >
            <app-link v-if="it.meta" :to="resolvePath(it.path)">
              <span :index="resolvePath(it.path)" class="title-menu">
                <item
                  :icon="it.meta.icon||(it.meta&&it.meta.icon)"
                  :title="generateTitle(it.meta.title)"
                />
              </span>
            </app-link>
          </li>
        </yu-base-menu-tile-item>
      </template>
    </el-submenu>
    <el-submenu
      v-else-if="menuModel.id === 'topTree'"
      ref="subMenu"
      :index="resolvePath(item.path)"
      :class="{'has-child':hasChild(item)}"
      popper-append-to-body
    >
      <template slot="title">
        <item
          v-if="item.meta"
          :icon="item.meta && item.meta.icon"
          :title="generateTitle(item.meta.title)"
        />
      </template>
      <div :style="{maxHeight:submenuMaxheight}">
        <sidebar-item
          v-for="child in item.children"
          :key="child.name"
          :is-nest="true"
          :item="child"
          :base-path="resolvePath(child.path)"
          class="tree-menu"
        />
      </div>
    </el-submenu>
    <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body>
      <template slot="title">
        <item
          v-if="item.meta"
          :icon="item.meta && item.meta.icon"
          :level="!isNest ? 1 : -1"
          :title="generateTitle(item.meta.title)"
        />
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.name"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />
    </el-submenu>
  </div>
</template>

<script>
import path from "path";
import { generateTitle, getLanguage } from "@/utils/i18n";
import { isExternal } from "@/utils/validate";
import { mapGetters } from "vuex";
import Item from "./Item";
import AppLink from "./Link";
import FixiOSBug from "./FixiOSBug";
import YuBaseMenuTileItem from "./YuBaseMenuTileItem";
import { logInfo } from '@/utils/util'

export default {
  name: "SidebarItem",
  components: { Item, AppLink, YuBaseMenuTileItem },
  mixins: [FixiOSBug],
  props: {
    item: {
      type: Object,
      required: true,
    },
    isNest: {
      type: Boolean,
      default: false,
    },
    basePath: {
      type: String,
      default: "",
    },
    submenuMaxheight: {
      type: String,
      default: "",
    },
  },
  data() {
    // To fix https://github.com/PanJiaChen/vue-admin-template/issues/237
    // TODO: refactor with render function
    this.onlyOneChild = null;
    return {};
  },
  computed: {
    ...mapGetters(['menuModel', 'routes', 'currentTopMenu']),
    menusData() {
      return this.routes.filter(item => {
        return !item.hidden;
      })
    },
  },
  methods: {
    hasChild(item) {
      return item.children.some(function (child) {
        return child.children.length > 0;
      });
    },
    getChildrenFn(list) {
      let routerList = [];
      list &&
        list.children &&
        list.children.forEach((item) => {
          if (
            item.component &&
            (item.children === undefined ||
              (item.children && item.children.length === 0))
          ) {
            routerList.push(item);
          } else if (item.children && item.children.length > 0) {
            routerList = routerList.concat(this.getChildrenFn(item));
          }
        });
      return routerList;
    },
    getChildrenFilter(list) {
      return this.getChildrenFn(list);
    },
    clickTag(tag) {
      this.menusData.forEach(menu => {
        menu.children.forEach(item => {
          if (item.path === tag.path) {
            this.$store.commit('oauth/SET_CURRENT_TOP_MENU', menu);
          } else if (item.children && item.children.length > 0) {
            item.children.forEach(child => {
              if (child.path === tag.path) {
                this.$store.commit('oauth/SET_CURRENT_TOP_MENU', menu);
              }
            })
          }
        })
      })
      if (this.currentTopMenu && this.currentTopMenu.path === '/') {
        this.$store.commit('oauth/SET_SHOW_LEFT_MENU', false);
      }
    },
    hasOneShowingChild(children = [], parent) {
      const showingChildren = children.filter((item) => {
        if (item.hidden) {
          return false;
        } else {
          // Temp set(will be used if only has one showing child)
          this.onlyOneChild = item;
          return true;
        }
      });

      // When there is only one child router, the child router is displayed by default
      if (showingChildren.length === 1) {
        return true;
      }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ...parent, path: "", noShowingChildren: true };
        return true;
      }

      return false;
    },
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath;
      }
      if (isExternal(this.basePath)) {
        return this.basePath;
      }
      return path.resolve(this.basePath, routePath);
    },

    generateTitle,
  },
};
</script>
<style lang="scss">
$height-48: 48px;
$height-36: 36px;
$height-38: 38px;

.el-menu-item > i {
  margin-right: 8px;
}

//紧凑模式
.compact .yu-frame-menu-top .yu-menu-horizontal-scrollarea {
  .el-scrollbar {
    padding: 0 16px;
  }
  .menu-wrapper {
    > a {
      height: $height-38;
      > .el-menu-item {
        height: $height-38;
        line-height: $height-38;
        font-size: 14px;
      }
    }

    > li {
      height: $height-38;
      > .el-menu {
        top: $height-38!important;
      }
      > .el-submenu__title {
        height: $height-38;
        line-height: $height-38;
        font-size: 14px;
      }
    }

    ul.el-menu {
      >div>.menu-wrapper {
        height: 28px;
        li.el-menu-item {
          height: 28px;
          line-height: 28px;
          span {
            font-size: 12px;
          }
        }

        li.el-submenu {
          height: 28px;
          .el-submenu__title {
            height: 28px;
            line-height: 28px;
            span {
              font-size: 12px;
            }
          }
        }
      }
    }
  }
}

//侧边栏首页菜单项
.yu-menu-horizontal-scrollarea {
  .menu-wrapper {
    > a {
      .el-menu-item {
        .yu-sidebar-item-title-vertical {
          position: relative;
          bottom: 1px;
        }
      }
    }
  }
}

//侧边菜单栏
.yu-frame-menu-vertical .sidebar-container .yu-menu-horizontal-scrollarea {
  .el-scrollbar__view > .el-menu {
    background-color: rgba(0, 0, 0, 0);
    .menu-wrapper .el-menu {
      .menu-wrapper {
        .el-submenu__title,
        .el-menu-item {
          // padding-left: 44px !important; // 影响四级菜单及更深层的层级展示样式，先注释 2021年3月17日 gaoxin5
        }

        .el-menu {
          background-color: rgba(0, 0, 0, 0);
          .menu-wrapper {
            .el-menu-item {
              // padding-left: 64px !important; // 影响四级菜单及更深层的层级展示样式，先注释 2021年3月17日 gaoxin5
            }
          }
        }
      }
    }
  }
}

//顶部菜单
.yu-frame-menu-top .sidebar-container .yu-menu-horizontal-scrollarea {
  .el-scrollbar > .el-scrollbar__wrap {
    overflow-x: visible;
    .yu-root-menu-wrapper {
      >.el-submenu {
        >.el-submenu__title {
          border: none;
        }
      }
    }
  }
}

//top模式横向菜单
.el-menu--horizontal > .menu-wrapper {
  .is-active > .el-submenu__title {
    border: none;
    background-color: rgba(255, 255, 255, 0.15);
  }
  //首页菜单项
  > a {
    margin-right: 1px;
    .el-menu-item {
      font-size: 16px;
      height: $height-48;
      line-height: $height-48;
      color: #fff;
      &:hover {
        background-color: rgba(255, 255, 255, 0.15);
      }
    }
  }
  // 普通菜单项
  > li {
    margin-right: 1px;
    height: $height-48;
    .el-submenu__title {
      font-size: 16px;
      height: $height-48;
      line-height: $height-48;
      color: #fff;
      &:hover {
        color: #fff;
        background-color: rgba(255, 255, 255, 0.15);
      }
      .el-submenu__icon-arrow {
        display: none;
      }
    }
    .el-menu {
      top: $height-48 !important;
      overflow: auto;
      //一级下拉菜单项
      .menu-wrapper.tree-menu {
        height: $height-36;
        line-height: $height-36;
        &:hover {
          background-color: rgba(0, 0, 0, 0.05);
        }
        > a {
          height: $height-36;
          line-height: $height-36;
          .el-menu-item {
            height: $height-36;
            line-height: $height-36;
            color: #000;
            padding: 0 20px;
          }
        }
        //有二级菜单的一级下拉菜单项
        > .el-submenu {
          height: $height-36;
          line-height: $height-36;
          width: 100%;
          .el-submenu__title {
            height: $height-36;
            line-height: $height-36;
            color: #000;
            border: none;
            font-size: 14px;
            .el-submenu__icon-arrow {
              display: block;
              position: absolute;
              transform: rotate(270deg);
              height: 12px;
              width: 12px;
            }
          }
          //二级下拉菜单
          .el-menu {
            left: 100%;
            top: 0!important;
          }
        }
      }
    }
  }
  .has-child > .el-menu {
    overflow: visible;
  }
}
.el-menu--horizontal.el-menu.tree-menu
  > .menu-wrapper
  > .el-submenu.is-opened
  > .el-submenu__title
  > span {
  color: #fff;
}
</style>
