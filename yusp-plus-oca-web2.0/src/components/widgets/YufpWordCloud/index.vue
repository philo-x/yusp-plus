<template>
  <div>
    <canvas :id="id" :class="className" :width="width"></canvas>
  </div>
</template>
<script>
export default {
  name: "YufpWordCloud",
  props: {
    id: String,
    width: {
      type: Number,
      default: 600
    },
    // 自定义class
    className: String,
    // 云图配置参数
    options: Object
  },
  mounted: function () {
    var canvas = document.getElementById(this.id)
    const wd = new window.Imworldcloud(canvas, this.options)
  }
}
</script>
