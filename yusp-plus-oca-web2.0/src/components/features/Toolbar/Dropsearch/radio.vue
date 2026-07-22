
<style>
.yu-radio input {
  display: none;
}
.yu-radio .radio-text {
  display: inline-block;
  border: 1px solid #dcdfe6;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  vertical-align: middle;
  margin-right: 5px;
  background: #ffffff;
  position: relative;
  top: 0;
}
.radio-text i {
  display: inline-block;
  color: #ffffff;
  transform: scale(0.6);
  -ms-transform: scale(0.6);
  -webkit-transform: scale(0.6);
  -moz-transform: scale(0.6);
  -o-transform: scale(0.6);
  position: relative;
  left: 1px;
  top: 3px;
}
.yu-radio input:checked ~ .radio-text {
  background: #636ce4;
}
.yu-radio span {
  display: inline-block;
  width: calc(100% - 30px);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  cursor: pointer;
  position: relative;
  top: 4px;
}
</style>

<template>
  <label class="yu-radio" :for="index">
    <input type="radio" :name="name" :id="index" :checked="checked" @change.stop="changeCheck">
    <label class="radio-text" @click.stop="changeCheck">
      <i class="el-icon-check"></i>
    </label>
    <span class="text-overflow" :title="name">{{ name }}</span>
  </label>
</template>
<script>
export default {
  props: {
    index: {
      type: [String, Number],
      default: 1
    },
    name: {
      type: [String, Number],
      default: '测试名字'
    },
    check: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      checked: false
    }
  },
  watch: {
    check () {
      this.checked = this.check;
    }
  },
  created () {
    this.checked = this.check;
  },
  methods: {
    changeCheck () {
      this.$emit('on-check', this.index);
    }
  }
}
</script>
