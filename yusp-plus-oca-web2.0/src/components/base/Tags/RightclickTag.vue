<template>
  <ul class="yu-tags-rc" :style="'top:' + top + 'px;left:' + left + 'px;'">
    <li class="yu-tags-rc__item" v-for="v in rcDatas" :key="v.type" @click="rightClickHandle(v)">{{ v.title }}</li>
  </ul>
</template>
<script>
export default {
  name: 'right-click-tags',
  props: {
    tagBtnIcon: {
      type: String,
      default: "yu-icon-more1"
    },
    tagBtnTitle: {
      type: String,
      default: '更多'
    },
    left: {
      type: [Number, String],
      default: 0
    },
    top: {
      type: [Number, String],
      default: 0
    }
  },
  data () {
    return {
      rcDatas: [
        {
          title: '刷新',
          type: 'refresh'
        },
        {
          title: '关闭',
          type: 'close'
        },
        {
          title: '关闭其它',
          type: 'closeOthers'
        },
        {
          title: '关闭所有',
          type: 'closeAll'
        }
      ]
    }
  },
  methods: {
    //右击事件处理
    rightClickHandle (v) {
      this.$emit('on-right-click', v);
    }
  }
}
</script>
<style lang='scss'>
.yu-tags-rc {
  margin: 0;
  background: #fff;
  z-index: 3000;
  position: absolute;
  top: 102px;
  list-style-type: none;
  padding: 5px 0;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 400;
  color: #333;
  -webkit-box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
  box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
  .yu-tags-rc__item {
    margin: 0;
    padding: 7px 16px;
    cursor: pointer;
    line-height: 22px;
    font-size: 14px;
  }
  .yu-tags-rc__item:hover {
    background: #eee;
  }
  .yu-tags-rc__item:active {
    background-color: #e7e8fe;
  }
}
</style>

