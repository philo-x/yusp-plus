<template>
  <ul class="yu-horizontal-tag">
    <li :class="{'yu-horizontal-tag__item':true, 'active': v.title === currTag.title}" v-for="(v,i) in horiTagData" :key="i" @click.stop="toggleActive(v)" @contextmenu.prevent="rightClick($event, v)">
      {{ v.title }}
      <i v-if="!v.affix" @click.stop="$emit('on-close', v)" class="yu-horizontal-tag__close yu-icon-close1"></i>
    </li>
    <slot name="side-tag"></slot>
    <slot name="right-tag"></slot>
  </ul>
</template>
<script>
export default {
  name: 'horizontal-tags',
  props: {
    horiTagData: {
      type: Array,
      default: function () {
        return [
          {
            title: '表格',
            affix: true
          },
          {
            title: '表格+表单'
          },
          {
            title: '表格+表单1'
          }
        ]
      }
    }
  },
  data () {
    return {
      currTag: {
        title: '表格',
        affix: true
      }
    }
  },
  methods: {
    toggleActive (data) {
      this.currTag = data;
      this.$emit('on-toggle', data);
    },
    rightClick (e, v) {
      this.$emit('on-right-click', {
        e: e,
        v: v
      })
    }
  }
}
</script>
<style lang='scss'>
.yu-horizontal-tag {
  width: 100%;
  height: 100%;
  font-size: 14px;
  color: #333;
  transition: 0.2s;
  -webkit-transition: 0.2s;
  -ms-transition: 0.2s;
  -moz-transition: 0.2s;
  .yu-horizontal-tag__item.active {
    color: #5557b9;
    border-bottom: 2px #5557b9 solid;
  }
  .yu-horizontal-tag__item {
    line-height: 42px;
    height: 40px;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    padding: 0 20px;
    border-bottom: 2px transparent solid;
    cursor: pointer;
    position: relative;
    display: inline-block;
  }
  .yu-horizontal-tag__item:hover {
    background-color: #f0f0f6;
    color: #5557b9;
    border-bottom: 2px #5557b9 solid !important;
  }
  .yu-horizontal-tag__item:hover:active {
    background-color: #e7e8fe;
  }
  .yu-horizontal-tag__item:hover .yu-horizontal-tag__close {
    display: inline-block;
  }
  .yu-horizontal-tag__close {
    display: none;
    float: left;
    width: 12px;
    height: 12px;
    line-height: 12px;
    text-align: center;
    font-size: 10px;
    position: absolute;
    top: 4px;
    right: 4px;
    color: #bbb;
  }
}
</style>
