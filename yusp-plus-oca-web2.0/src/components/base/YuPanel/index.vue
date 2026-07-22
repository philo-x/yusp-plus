<template>
  <div class="yu-base-panel">
    <div v-if="title" class="yu-base-panel-title">
      <h1>{{ title }}</h1>
      <span v-if="isCollapse" @click="toggleFn" :class="['yu-base-panel-collapse', hide ? 'yu-icon-arr-down' : 'yu-icon-arr-up']">
        <template v-if="!hide">收起</template>
        <template v-else>展开</template>
      </span>
    </div>
    <div :class="['yu-base-panel-content',{hide: hide,'no-margin':!margin}]">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'YuPanel',
  props: {
    // 标题
    title: {
      type: String,
      default: ''
    },
    // 是否支持收缩展开
    isCollapse: {
      default: false,
      type: Boolean
    },
    // 收缩展开状态
    collapse: {
      default: false,
      type: Boolean
    },
    // 收缩展开状态
    margin: {
      default: true,
      type: Boolean
    }
  },
  data: function() {
    return {
      hide: false
    }
  },
  watch: {
    collapse: function(val) {
      this.hide = val
    }
  },
  mounted: function() {
    this.hide = this.collapse
  },
  methods: {
    toggleFn: function() {
      this.hide = !this.hide
    }
  }
}
</script>
