<template>
  <div>
    <div :class="classObj" class="app-wrapper yu-frame-div">
      <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
      <sidebar v-if="menuModel.id === 'left' || menuModel.id === 'right'" class="sidebar-container" />
      <left-menu v-if="menuModel.id === 'topLeft'" class="sidebar-container" />
      <i v-if="showSwitchIcon" class="yu-switch-show-left-menu el-icon-caret-right" @click="openLeftMenu" title="展开子菜单"></i>
      <div :class="{hasTagsView:needTagsView}" class="yu-main-box main-container">
        <navbar-top-left v-if="menuModel.id === 'topLeft'" class="sidebar-container" ref="elMenu" />
        <navbar v-else />
        <sidebar v-if="menuModel.id === 'topTree' || menuModel.id === 'topTile'" class="sidebar-container2" ref="elMenu" />
        <tags-view v-if="needTagsView" />
        <!-- <yu-footer v-if="isFooter" /> -->
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from './Navbar'
import NavbarTopLeft from './Navbar/topLeft'
import LeftMenu from './TopLeft/leftMenu.vue';
import Sidebar from './Sidebar/index.vue'
import TagsView from './TagsView/index.vue'
import YuFooter from '../base/YuFooter'
import { getI18nMessage } from "@/locale";
// import AppMain from './AppMain'

import ResizeMixin from './mixin/ResizeHandler'
import { mapState, mapGetters } from 'vuex'
import frameConfig from '@/config/frame'

export default {
  name: 'Layout',
  components: {
    Navbar,
    NavbarTopLeft,
    LeftMenu,
    Sidebar,
    // AppMain,
    TagsView,
    // YuFooter
  },
  mixins: [ResizeMixin],
  computed: {
    ...mapState({
      sidebar: state => state.app.sidebar,
      device: state => state.app.device,
      showSettings: state => state.settings.showSettings,
      needTagsView: state => state.settings.tagsView,
      fixedHeader: state => state.settings.fixedHeader
    }),
    ...mapGetters([
      'menuModel',
      'menuShowStat',
      'currentTopMenu',
      'showLeftMenu',
    ]),
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile',
        'yu-frame-menu-vertical': this.menuModel.id === 'left' || this.menuModel.id === 'right',
        'yu-frame-menu-left': this.menuModel.id === 'left',
        'yu-frame-menu-right': this.menuModel.id === 'right',
        'yu-frame-menu-top': this.menuModel.id === 'topTree' || this.menuModel.id === 'topTile',
        'yu-frame-menu-top-left': this.menuModel.id === 'topLeft',
        'yu-frame-menu-show-left-menu': this.showLeftMenu,
        'yu-frame-menu-hover': this.menuShowStat === 3,
        'yu-frame-has-footer': frameConfig.baseFrameOptions.isFooter,
        'yu-frame-menu-collapse': !this.sidebar.opened && !(this.menuModel.id === 'topTree' || this.menuModel.id === 'topTile')
      }
    },
    isFooter() {
      return frameConfig.baseFrameOptions.isFooter
    },
    showSwitchIcon() {
      if (this.menuModel.id === 'topLeft' && this.currentTopMenu && this.currentTopMenu.children.length > 0) {
        if (this.currentTopMenu.path === '/') {
          return false;
        }
        const needShowChildren = this.currentTopMenu.children.filter(item => {
          return !item.hidden;
        })
        return needShowChildren.length > 0;
      }
      return false;
    }
  },
  watch:{
    /**
     * 监听菜单模式
     */
    menuModel: function (val, oldVal) {
      if (val.id === "topTree" || val.id === "topTile") {
        this.$nextTick(() => {
          this.$refs.elMenu.getMenuSize(val);
        });
      }
    },
  },
  mounted() {
    const frameDom = document.body.querySelector('.yu-frame-tab-box')
    this.$store.dispatch('app/setViewSize', { width: frameDom.clientWidth, height: frameDom.clientHeight})
    window.watermark && window.watermark.createWatermark()
    const title = getI18nMessage('component.document_title')
    title && (document.title = title);
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    },

    openLeftMenu() {
      this.$store.commit('oauth/SET_SHOW_LEFT_MENU', true);
    }
  }
}
</script>
<style lang="scss" scoped>
  .yu-switch-show-left-menu {
    position: absolute;
    top: 64px;
    left: 0;
    bottom: 0;
    margin: auto;
    width: 16px;
    height: 32px;
    line-height: 32px;
    color: #fff;
    z-index: 8;
    text-align: center;
    cursor: pointer;
    background: #2877FF;
    border-radius: 0 4px 4px 0;
  }
</style>
