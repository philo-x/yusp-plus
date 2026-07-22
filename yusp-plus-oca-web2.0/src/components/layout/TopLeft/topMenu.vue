<template>
  <div class="yu-frame-top-menu" :style="{padding : showArrowIcon ? '0 24px' : '0'}">
    <div v-if="showArrowIcon" class="left-icon el-icon-caret-left" @click="showLeftMenu" title="向前"></div>
    <div ref="menuBox" class="menu-box">
      <template v-for="(item, index) in menusData">
        <div ref="menu" class="menu" :class="{'active' : currentTopMenu && item.path === currentTopMenu.path, 'hidden' : index < menuHiddenIndex}" @click="menuClick(item)" :key="item.path">
          <span class="menu-text">{{ item.path === '/' ? '首页' : item.meta && item.meta.title }}</span>
        </div>
      </template>
    </div>
    <div v-if="showArrowIcon" class="right-icon el-icon-caret-right" @click="showRightMenu" title="向后"></div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  components: {},

  data () {
    return {
      menuHiddenIndex: 0,
      menusWidth: 0,
      menuBoxWidth: 0
    };
  },

  computed: {
    ...mapGetters(['routes', 'currentTopMenu']),

    menusData () {
      return this.routes.filter(item => {
        // console.log(item.path)
        return !item.hidden && (item.meta || (item.path == '/' && item.redirect));
      });
    },

    showArrowIcon () {
      return this.menuBoxWidth < this.menusWidth || this.menuHiddenIndex > 0;
    }
  },

  mounted () {
    this.initCurrentTopMenu();
    this.getMenuBoxWidth();
    this.getMenusWidth();
  },

  methods: {
    initCurrentTopMenu () {
      // modify by luyq1@2021-02-02 解决topLeft布局刷新内存溢出问题，导致页面崩溃；
      const curMenu = {...this.currentTopMenu};
      if (!this.currentTopMenu) {
        for (let i = 0, len = this.menusData.length; i < len; i++) {
          const item = this.menusData[i];
          if (item.path === '/') {
            this.$store.commit('oauth/SET_CURRENT_TOP_MENU', item);
            break;
          }
        }
      } else {
        this.$store.commit('oauth/SET_CURRENT_TOP_MENU', curMenu);
      }
    },

    getMenuBoxWidth () {
      this.$nextTick(() => {
        this.menuBoxWidth = this.$refs.menuBox.offsetWidth;
      });
    },

    getMenusWidth () {
      this.$nextTick(() => {
        let menusWidth = 0;
        const menus = this.$refs.menu || [];
        // console.log(menus);
        menus.forEach(mune => {
          menusWidth = menusWidth + mune.offsetWidth || 0;
        });
        this.menusWidth = menusWidth;
      });
    },

    showLeftMenu () {
      this.menuHiddenIndex > 0 && this.menuHiddenIndex--;
      this.getMenusWidth();
    },

    showRightMenu () {
      this.menuBoxWidth < this.menusWidth && this.menuHiddenIndex++;
      this.getMenusWidth();
    },

    menuClick (item) {
      console.log('menuClick===', item);
      this.$store.commit('oauth/SET_CURRENT_TOP_MENU', item);
      const needShowChildren = item.children.filter(item => {
        return !item.hidden;
      });
      console.log('needShowChildren===', item, item.children);
      if (needShowChildren.length === 1 && (!needShowChildren[0].children || needShowChildren[0].children.length === 0)) {
        this.$router.push(needShowChildren[0].path);
      }
    }
  }
};
</script>
<style lang="scss" scoped>
  .yu-frame-top-menu {
    position: relative;
    height: 63px;
    line-height: 63px;
    padding: 0 24px;
    overflow: hidden;
    .left-icon, .right-icon {
      position: absolute;
      width: 24px;
      height: 63px;
      line-height: 63px;
      text-align: center;
      cursor: pointer;
    }
    .left-icon {left: 0;}
    .right-icon {right: 0;}
    .menu-box {
      float: left;
      width: 100%;
      height: 63px;
      &::after {
        content: '';
        display: block;
        clear: both;
      }
      .menu {
        float: left;
        height: 63px;
        &.hidden {
          display: none;
        }
        .menu-text {
          display: block;
          padding: 0 19px;
          line-height: 41px;
          margin: 11px 0 11px 1px;
          border-radius: 4px;
          font-size: 16px;
          cursor: pointer;
          transition: all 0.3s;
        }
        &.active .menu-text, &:hover .menu-text {
          background: rgba(255, 255, 255, 0.2);
        }
      }
    }
  }
</style>
