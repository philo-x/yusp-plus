<style>
.yu-input {
  float: left;
  height: 32px;
  line-height: 32px;
  width: calc(100% - 80px);
}
.yu-input input {
  border: none;
  outline: none;
  padding-left: 15px;
  width: calc(100% - 62px);
}
.yu-input i {
  padding: 0 15px;
  cursor: pointer;
}
.yu-input i:hover {
  color: #636ce4;
}
</style>

<template>
  <div :class="{'yu-input': true,'border': false}">
    <input type="text" v-if="isShow" v-model="inputVal" @keyup.enter="toSearch">
    <i :class="icon" @click.stop="toSearch"></i>
  </div>
</template>
<script>
export default {
  props: {
    isShow: {
      type: Boolean,
      default: false
    },
    icon: {
      type: String,
      default: 'el-icon-search'
    }
  },
  data () {
    return {
      inputVal: ''
    }
  },
  methods: {
    toSearch () {
      this.$emit('on-search', this.inputVal);
    }
  }
}
</script>
