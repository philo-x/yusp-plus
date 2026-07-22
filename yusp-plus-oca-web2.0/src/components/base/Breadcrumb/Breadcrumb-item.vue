<template>
  <span class="yu-breadcrumb_item" @click="handleClick">
    <slot></slot>
    <i v-if="separatorClass" class="yu-breadcrumb_separator" :class="separatorClass"></i>
    <span v-else class="yu-breadcrumb_separator">{{ separator }}</span>
  </span>
</template>

<script>
import pathToRegexp from 'path-to-regexp'
export default {
  name: 'BreadcrumbItem',
  props: {
    to: {
      type: Object,
      default: function () {
        return {}
      }
    },
    replace: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      separator: '',
      separatorClass: ''
    }
  },
  inject: ['Breadcrumb'],
  mounted () {
    this.separator = this.Breadcrumb.separator;
    this.separatorClass = this.Breadcrumb.separatorClass;
  },
  methods: {
    pathCompile (path) {
      // To solve this problem https://github.com/PanJiaChen/vue-element-admin/issues/561
      const { params } = this.$route
      var toPath = pathToRegexp.compile(path)
      return toPath(params)
    },
    handleClick (e) {
      this.$emit('click', e);
      if (this.to) {
        const { redirect, path } = this.to;
        if (redirect) {
          this.$router.push(redirect)
          return
        }
        this.$router.push(this.pathCompile(path))
      }
    }
  }
};
</script>
