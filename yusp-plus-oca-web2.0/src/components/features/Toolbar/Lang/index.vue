<template>
  <Toolbar class="yu-langs" tool-icon="yu-icon-network">
    <span v-for="(item,i) in langList" :key="item.id" @click="switchLang(item,i)" :class="[item.checked?'yu-icon-checked2':'yu-icon-choice-un']" :title="item.name">
      {{ item.name }}
    </span>
  </Toolbar>
</template>
<script>
import Toolbar from '@/components/layout1/base/ToolBar/index'
export default {
  name: 'tool-lang',
  components: {
    Toolbar
  },
  data () {
    return {
      langList: [
        { id: 'zh_CN', name: '中文-简体', checked: true },
        // { id: 'zh_TW', name: '中文-繁體' },
        { id: 'en', name: 'English' }
      ]
    }
  },
  methods: {
    async switchLang (data, i) {
      await this.resetCheck();
      this.$set(this.langList[i], 'checked', true);
      this.$emit('on-switch', data);
      console.log('switch-----');
    },
    resetCheck () {
      this.langList.forEach((v, i) => {
        this.$set(v, 'checked', false);
      })
    }
  }
}
</script>
<style lang='scss'>
.yu-langs {
  .yu-icon-checked2,
  .yu-icon-choice-un {
    display: block;
    height: 36px;
    line-height: 36px;
    font-size: 14px;
    text-align: left;
    color: #666;
    padding: 0 32px 0 24px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    position: relative;
    cursor: pointer;
    -webkit-transition: 0.2s;
    transition: 0.2s;
  }
  .yu-icon-checked2:before,
  .yu-icon-choice-un:before {
    float: left;
    font-size: 18px;
    padding-right: 8px;
    color: #babae3;
  }
  .yu-icon-checked2:hover,
  .yu-icon-choice-un:hover {
    color: #5557b9;
    background-color: #f0f0f6;
  }
  .yu-icon-checked2:active,
  .yu-icon-choice-un:active {
    color: #5557b9;
    background-color: #e7e8fe;
  }
  .yu-icon-checked2::before {
    color: #5557b9;
  }
}
</style>
