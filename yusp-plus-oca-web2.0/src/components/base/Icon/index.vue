<template>
  <span>
    <i v-if="icon.indexOf('yu-icon') > -1" :class="'define-icon '+icon">
      <slot></slot>
    </i>
    <svg-icon v-else class="define-icon" :icon-class="icon">
      <slot></slot>
    </svg-icon>
  </span>
</template>

<script>
export default {
  name: 'icon',
  props: {
    icon: {
      type: String,
      default: ''
    }
  }
}
</script>
