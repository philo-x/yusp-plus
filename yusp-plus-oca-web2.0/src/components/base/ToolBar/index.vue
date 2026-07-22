<template>
  <section class="yu-toolbar">
    <div class="yu-toolbar-item" :title="title" @click.stop="trigger">
      <Toolicon class="toolbar-item-title" :icon="toolIcon" :dot="isDot" />
      <section v-if="drop" class="toolbar-item-content">
        <slot></slot>
      </section>
    </div>
  </section>
</template>
<script>
import Toolicon from "./Toolicon/index"
export default {
  name: 'toolbar',
  components: {
    Toolicon
  },
  props: {
    toolIcon: {
      type: String,
      default: "yu-icon-message"
    },
    isDot: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: ""
    },
    drop: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    trigger () {
      this.$emit('on-click');
    }
  }
}
</script>
<style lang='scss'>
.yu-toolbar {
  float: left;
  .yu-toolbar-item {
    position: relative;
    padding: 0 10px;
    height: 64px;
  }
  .yu-toolbar-item:hover {
    background-color: #f0f0f6;
  }
  .yu-toolbar-item:active {
    background-color: #eaeaea;
  }
  .yu-toolbar-item:hover .toolbar-item-content {
    display: block;
  }
  .toolbar-item-title {
    color: #949494;
    .define-icon {
      font-size: 20px;
    }
  }
  //toolbar内容
  .toolbar-item-content {
    display: none;
    position: absolute;
    background: #fff;
    top: 100%;
    right: -100px;
    padding: 10px 0;
    z-index: 3000;
    min-width: 150px;
    // height: 514px;
    // max-height: 514px;
    box-shadow: 0px 3px 6px 0px rgba(0, 0, 0, 0.15);
    border-radius: 4px;
  }
  .toolbar-item-content:after {
    content: "";
    display: block;
    clear: both;
  }
}
</style>

