<template>
  <div class="yu-frame-left-menu" :class="{'hidden' : !showLeftMenu}">
    <div class="yu-frame-left-menu-content">
      <i class="yu-switch-show-left-menu el-icon-caret-left" @click="closeLeftMenu" title="收起子菜单"></i>
      <div class="top-menu">
        <i v-if="topMenuIcon" :class="topMenuIcon"></i>{{ topMenuTitle }}
      </div>
      <yu-menu v-if="leftMenus.length > 0" class="left-menu-box" :default-active="activeMenu" :unique-opened="true">
        <menu-item v-for="route in leftMenus" :item="route" :base-path="route.path" :key="route.name" />
      </yu-menu>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { ROUTER_STORE_KEY } from '@/config/constant/app.data.common.js'
import { sessionStore } from 'uadp-utils'
import menuItem from "./menuItem";

export default {
  components: { menuItem },

  data() {
    return {
      menuHiddenIndex: 0,
      menusWidth: 0,
      menuBoxWidth: 0,
      isShow: true,
      leftMenus: []
    }
  },

  computed: {
    ...mapGetters(['routes', 'currentTopMenu', 'showLeftMenu']),
    topMenuTitle() {
      if (!this.currentTopMenu) {
        return '';
      } else if (this.currentTopMenu.path === '/') {
        return '首页';
      } else {
        return this.currentTopMenu.meta ? this.currentTopMenu.meta.title : '';
      }
    },

    topMenuIcon() {
      if (!this.currentTopMenu) {
        return '';
      } else if (this.currentTopMenu.path === '/') {
        return this.currentTopMenu.children.length > 0 && this.currentTopMenu.children[0].meta ? this.currentTopMenu.children[0].meta.icon : '';
      } else {
        return this.currentTopMenu.meta ? this.currentTopMenu.meta.icon : '';
      }
    },

    activeMenu() {
      const route = this.$route;
      const { meta, path, params} = route;
      if (params.activeMenu) {
        return params.activeMenu;
      }
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path;
    },
  },

  watch: {
    currentTopMenu(val) {
      this.isShow = false;
      const menus = sessionStore.get(ROUTER_STORE_KEY) || [];
      for(let i = 0, len = menus.length; i < len; i++) {
        const item = menus[i];
        // console.log(item.name, this.currentTopMenu.name)
        if (item.name === this.currentTopMenu.name) {
          this.leftMenus = item.children || [];
          break;
        }
      }
      this.leftMenus = this.leftMenus.filter(item => {
        return !item.hidden;
      })
      // console.log(this.leftMenus);
      if (this.leftMenus.length > 0 && this.currentTopMenu.path !== '/') {
        this.$store.commit('oauth/SET_SHOW_LEFT_MENU', true);
      } else {
        this.$store.commit('oauth/SET_SHOW_LEFT_MENU', false);
      }
      this.$nextTick(() => {
        this.isShow = true;
      })
    },
  },

  created() {},

  methods: {
    closeLeftMenu() {
      this.$store.commit('oauth/SET_SHOW_LEFT_MENU', false);
    },
  }
};
</script>
<style lang="scss" scope>
  .yu-frame-left-menu {
    width: 240px;
    height: calc(100% - 64px);
    position: absolute;
    top: 64px;
    left: 0;
    z-index: 9;
    opacity: 1;
    color: #333;
    background: #fff;
    box-shadow: 0px 0px 8px 0px rgba(1, 1, 1, 0.25);
    transition: width 0.3s ease, opacity 0.15s ease;
    &.hidden {
      width: 0;
      opacity: 0;
      overflow: hidden;
    }
    .yu-frame-left-menu-content {
      position: relative;
      height: 100%;
      font-size: 14px;
      line-height: 42px;
      .yu-switch-show-left-menu {
        position: absolute;
        top: 0;
        right: 0;
        bottom: 0;
        margin: auto;
        width: 16px;
        height: 32px;
        line-height: 32px;
        color: #fff;
        z-index: 998;
        text-align: center;
        cursor: pointer;
        background: #2877FF;
        border-radius: 4px 0 0 4px;
      }
      .top-menu {
        padding-left: 20px;
        line-height: 42px;
        color: rgba(51, 51, 51, 0.6);
        border-bottom: 1px solid #EDEDED;
        >i {
          font-size: 14px;
          margin-right: 8px;
        }
      }
      .left-menu-box {
        overflow-y: auto;
        height: calc(100vh - 107px);
        background: #fff;
      }
    }
  }
</style>