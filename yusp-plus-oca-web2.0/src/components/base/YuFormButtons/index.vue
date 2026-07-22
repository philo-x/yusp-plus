<template>
  <div
    class="yu-form-buttons"
    :style="style"
  >
    <slot />
  </div>
</template>
<script>
export default {
  name: 'YuFormButtons',
  props: {
    paddingLeft: {
      type: Number,
      default: 0
    },
    align: {
      type: String,
      default: ''
    }
  },
  data: function() {
    return {};
  },
  computed: {
    style: function() {
      if (this.align) {
        return {
          textAlign: this.align,
          paddingLeft: this.align === "center" ? "24px" : `${this.paddingLeft + 24}px`
        };
      } else {
        return {
          paddingLeft: `${this.paddingLeft + 24}px`
        };
      }
    }
  }
};
</script>
