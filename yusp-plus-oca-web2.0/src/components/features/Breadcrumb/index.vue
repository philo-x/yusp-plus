<template>
  <transition>
    <Breadcrumb :separator="separator">
      <Breadcrumb-item v-for="(v,i) in currRoute" :key="i">
        <Icon v-if="i===0 && showIcon" :icon="showIcon" />
        <span v-if="!isLink || v.redirect==='noRedirect'||i==currRoute.length-1" class="no-redirect">{{ generateTitle(v.meta.title) }}</span>
        <a v-else-if="isLink" @click.prevent="handleLink(v)">{{ generateTitle(v.meta.title) }}</a>
      </Breadcrumb-item>
    </Breadcrumb>
  </transition>
</template>
<script>
import Breadcrumb from '../../base/Breadcrumb/Breadcrumb';
import BreadcrumbItem from '../../base/Breadcrumb/Breadcrumb-item';
import Icon from '../../base/Icon/index';
import { generateTitle } from '@/utils/i18n'
import pathToRegexp from 'path-to-regexp'
export default {
  name: 'bread-crumb',
  components: {
    Breadcrumb,
    BreadcrumbItem,
    Icon
  },
  props: {
    separator: {
      type: String,
      default: '/'
    },
    isLink: {
      type: Boolean,
      default: true
    },
    showIcon: {
      type: [Boolean, String],
      default: 'yu-icon-meun-fold hamburger'
    }
  },
  data () {
    return {
      currRoute: []
    }
  },
  watch: {
    '$route': {
      handler: function () {
        this.getBreadcrumb();
        this.currRoute = this.$route.matched.filter(item => item.name);//this.$route.matched;
      },
      immediate: true
    }
  },
  methods: {
    generateTitle,
    getBreadcrumb () {
      // only show routes with meta.title
      const matched = this.$route.matched.filter(item => item.meta && item.meta.title)
      const first = matched[0]

      // if (!this.isDashboard(first)) {
      //   matched = [{ path: '/dashboard', meta: { title: 'dashboard' } }].concat(matched)
      // }

      this.currRoute = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
    },
    isDashboard (route) {
      const name = route && route.name
      if (!name) {
        return false
      }
      return name.trim().toLocaleLowerCase() === 'Dashboard'.toLocaleLowerCase()
    },
    pathCompile (path) {
      // To solve this problem https://github.com/PanJiaChen/vue-element-admin/issues/561
      const { params } = this.$route
      var toPath = pathToRegexp.compile(path)
      return toPath(params)
    },
    handleLink (item) {
      const { redirect, path } = item
      if (redirect) {
        this.$router.push(redirect)
        return
      }
      this.$router.push(this.pathCompile(path))
    }
  }
}
</script>
<style lang="scss">
.yu-breadcrumb {
  float: left;
  overflow-x: auto;
  padding: 24px 0;
  .yu-breadcrumb_item {
    color: #444;
    font-size: 14px;
    padding-left: 10px;
    .no-redirect {
      color: #97a8be;
      cursor: text;
    }
    .no-redirect,
    a {
      padding-right: 10px;
    }
  }
  .yu-breadcrumb_item:last-child {
    .yu-breadcrumb_separator {
      display: none;
    }
  }
  .define-icon {
    position: relative;
    top: 1px;
  }
}
</style>

