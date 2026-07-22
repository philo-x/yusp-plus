<template>
  <div v-if="!item.hidden" class="menu-item">
    <template v-if="showItem">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown':!isNest}">
          <template slot="title" v-if="item.meta">
            <item :icon="itemIcon" :level="!isNest ? 1 : -1" :title="generateTitle(item.meta.title || '-')" />
          </template>
        </el-menu-item>
      </app-link>
    </template>
    <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body>
      <template slot="title">
        <item v-if="item.meta" :icon="itemIcon" :level="!isNest ? 1 : -1" :title="generateTitle(item.meta.title)" />
      </template>
      <template v-if="item.children && item.children.length > 0">
        <menu-item
          v-for="child in item.children"
          :key="child.name"
          :is-nest="true"
          :item="child"
          :base-path="resolvePath(child.path)"
        />
      </template>
    </el-submenu>
  </div>
</template>

<script>
import path from "path";
import { generateTitle } from "@/utils/i18n";
import { isExternal } from "@/utils/validate";
import AppLink from "../Sidebar/Link";
import Item from "../Sidebar/Item";

export default {
  name: "MenuItem",

  components: { AppLink, Item },

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
    return {
      onlyOneChild: {},
    }
  },

  computed: {
    showItem() {
      return this.hasOneShowingChild(this.item.children, this.item) && (!this.onlyOneChild.children || this.onlyOneChild.noShowingChildren) && !this.item.alwaysShow;
    },
    itemIcon () {
      return this.item.meta ? (this.item.meta.icon || '') : '';
    }
  },
  created() {},

  mounted() {},

  methods: {
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
  }
};
</script>
<style lang="scss" scoped>
  .menu-item {
    background-color: #fff;
    .el-submenu__title, .el-menu-item, .el-submenu .el-menu-item {
      color: #333;
      transition: background-color .3s;
      &:not(.is-active):hover {
        color: #333;
        background-color:  rgba(40, 119, 255, 0.1);
      }
      i {
        font-size: 14px;
      }
      .el-submenu__icon-arrow {
        color: #999;
      }
    }
    .el-menu-item.is-active {
      color: #fff;
      background: linear-gradient(90deg, #2877FF, #1162ED);
    }
  }
</style>