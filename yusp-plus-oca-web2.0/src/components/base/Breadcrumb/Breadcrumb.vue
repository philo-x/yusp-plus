<template>
  <div class="yu-breadcrumb" aria-label="breadcrumb">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: 'Breadcrumb',
  props: {
    separator: {
      type: String,
      default: '/'
    },
    separatorClass: {
      type: String,
      default: ''
    }
  },
  provide () {
    return {
      Breadcrumb: this
    };
  },
  mounted () {
    const items = this.$el.querySelectorAll('.yu-breadcrumb_item');
    if (items.length) {
      items[items.length - 1].setAttribute('aria-current', 'page');
    }
  }
};
</script>