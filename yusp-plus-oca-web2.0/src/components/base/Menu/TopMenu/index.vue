<template>
  <div ref="menuScrollarea" class="navbar-menu">
    <el-menu mode="horizontal" :default-active="activeMenu" :collapse-transition="false" :unique-opened="true" @select="menuClick">
      <!--菜单item-->
      <el-menu-item index="/dashboard">
        <span slot="title">首页</span>
      </el-menu-item>
      <el-menu-item v-for="(item, i) in topMenus" :key="i" :index="item.name || item.path">
        <Item :icon="item.meta.icon" :title="item.meta.title || '主机'" :msg="item.meta.hasMsg || ''" />
      </el-menu-item>
      <!--更多内容-->
      <el-menu-item index="100000" v-if="hideMenus.length>0">
        <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="hover">
          <div class="avatar-wrapper yu-frame-top-user">
            <span>{{ moreText }}</span>
            <i :class="moreIcon" />
          </div>
          <el-dropdown-menu ref="refAvatarContainerUser" slot="dropdown" class="avatar-container-user">
            <el-menu-item :index="child.name ? child.name : child.path" v-for="child in hideMenus" :key="child.meta.title || ''">
              <span>{{ child.meta.title || '' }}</span>
            </el-menu-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-menu-item>
    </el-menu>
  </div>
</template>
<script>
import Item from '../Item.vue'
export default {
  components: { Item },
  props: {
    sideBar: {
      type: Boolean,
      default: false
    },
    moreText: {
      type: String,
      default: '更多'
    },
    moreIcon: {
      type: String,
      default: 'el-submenu__icon-arrow yu-icon-down'
    },
    isDynamicWidth: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      activeMenu: '/dashboard',
      itemWidth: 96,
      navWidth: 732,
      rootMenus: [
        {
          meta: {
            alone: 0,
            mId: 'M00',
            title: '主机',
            hasMsg: false,
            name: '/dashboard',
            path: '/dashboard',
            children: []
          },
          title: '主机',
          name: '/dashboard0',
          path: '/dashboard'
        },
        {
          meta: {
            alone: 0,
            mId: 'M00',
            title: '主机2',
            hasMsg: false,
            name: '/dashboard1',
            path: '/dashboard1',
            children: []
          },
          title: '主机2',
          name: '/dashboard1',
          path: '/dashboard1'
        }
      ],
      topMenus: [],
      hideMenus: [],
      len: 6
    }
  },
  mounted () {
    this.rootMenus = [...this.rootMenus, ...this.rootMenus, ...this.rootMenus];
    this.changeMenuNum();
    if (this.isDynamicWidth) {
      window.onresize = () => {
        this.changeMenuNum();
      }
    }
  },
  methods: {
    changeMenuNum () {
      this.getNavWith();
      this.getMenuNum();
      this.cutMenu();
    },
    menuClick (index) {
      this.activeMenu = index;
      this.$emit('hideSidebar', index === '/dashboard' ? false : true);
    },
    getNavWith () {
      const RIGHT_MENU_ELEM_WIDTH = parseFloat(window.getComputedStyle(document.querySelector('.yu-header-content-right')).width);
      const LEFT_LOGO_WIDTH = parseFloat(window.getComputedStyle(document.querySelector('.yu-frame-logo')).width);
      const WINDOW_WIDTH = document.documentElement.clientWidth || document.body.clientWidth;
      this.navWidth = parseInt(WINDOW_WIDTH - RIGHT_MENU_ELEM_WIDTH - LEFT_LOGO_WIDTH);
    },
    //获取可展示菜单个数
    getMenuNum () {
      this.len = parseInt(this.navWidth / this.itemWidth) - 2;
    },
    //切割菜单
    cutMenu () {
      const MENUS = [...this.rootMenus];
      const MENU_LEN = MENUS.length;
      this.topMenus = MENUS.slice(0, this.len);
      this.hideMenus = MENUS.slice(this.len, MENU_LEN);
    }
  }
}
</script>
<style>
.navbar-menu {
  float: left;
  width: auto;
  height: 100%;
}
.navbar-menu .el-menu.el-menu--horizontal {
  height: 100%;
}
.navbar-menu .el-menu.el-menu--horizontal .el-menu-item {
  height: 100%;
  line-height: 100%;
  background-color: none;
  border-bottom: none;
}
.navbar-menu .el-menu.el-menu--horizontal .el-menu-item span {
  display: inline-block;
  line-height: 60px;
}
.navbar-menu .el-menu.el-menu--horizontal .el-menu-item.is-active {
  background-color: #5557b9;
}
</style>


