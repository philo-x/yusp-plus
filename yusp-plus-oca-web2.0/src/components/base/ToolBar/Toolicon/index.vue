<template>
  <Icon :icon="icon">
    <yu-badge :is-dot="dot"></yu-badge>
  </Icon>
</template>
<script>
import Icon from '@/components/layout1/base/Icon/index'
export default {
  name: 'toolIcon',
  components: {
    Icon
  },
  props: {
    icon: {
      type: String,
      default: ''
    },
    dot: {
      type: Boolean,
      default: false
    }
  }
}
</script>