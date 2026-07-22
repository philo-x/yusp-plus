<template>
  <section class="yu-tags">
    <Horiztags @on-toggle="toggleCurrTag" @on-right-click="toggleShowRight" @on-close="toggleRightTag({type: 'close'})">
      <Sidetags slot="side-tag" @on-jump="linkTo" @on-close-all="toggleRightTag({type: 'closeAll'})" @on-close="toggleRightTag({type: 'close'})"></Sidetags>
      <RightclickTag v-show="showRC" slot="right-tag" @on-right-click="toggleRightTag" :top="rightTagTop" :left="rightTagLeft"></RightclickTag>
    </Horiztags>
  </section>
</template>
<script>
import Horiztags from "./Horiztags";
import RightclickTag from "./RightclickTag";
import Sidetags from "./Sidetags";
export default {
  name: 'Tags',
  components: {
    Horiztags,
    RightclickTag,
    Sidetags
  },
  data () {
    return {
      currTag: {},
      rightTagLeft: 0,
      rightTagTop: 0,
      showRC: false
    }
  },
  watch: {
    showRC (value) {
      document.body[`${value ? 'add' : 'remove'}EventListener`]('click', this.closeRC)
    }
  },
  methods: {
    //切换当前激活tag
    toggleCurrTag (data) {
      console.log('on-toggle-tab--');
    },
    //右键触发事件
    toggleRightTag (data) {
      switch (data.type) {
        case 'refresh': break;
        case 'close': break;
        case 'closeOthers': break;
        case 'closeAll': break;
        default: break;
      }
    },
    //跳转到
    linkTo (data) {
      console.log('linkTo----');
    },
    toggleShowRight (param) {
      const EV = param.e || window.event,
        menuMinWidth = 105,
        offsetLeft = this.$el.getBoundingClientRect().left, // container margin left
        offsetWidth = this.$el.offsetWidth, // container width
        maxLeft = offsetWidth - menuMinWidth, // left boundary
        left = EV.clientX - offsetLeft + 15; // 15: margin right

      this.rightTagLeft = left > maxLeft ? maxLeft : left;
      this.rightTagTop = EV.clientY + 10;
      this.showRC = true;
      this.selectedTag = param.data;
    },
    closeRC () {
      this.showRC = false;
    }
  }
}
</script>
<style lang='scss'>
.yu-tags {
  height: 42px;
  line-height: 42px;
  padding-left: 24px;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  -webkit-box-shadow: 0px 8px 8px -8px rgba(0, 0, 0, 0.25);
  box-shadow: 0px 8px 8px -8px rgba(0, 0, 0, 0.25);
}
</style>
