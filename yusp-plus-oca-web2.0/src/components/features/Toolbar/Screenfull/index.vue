<template>
  <Screenfull class="screen-full" />
</template>
<script>
import Screenfull from '@/components/base/Screenfull'
export default {
  components: {
    Screenfull
  }
}
</script>
<style lang='scss'>
.screen-full {
  float: left;
  width: 20px;
  padding: 0 10px;
  height: 64px;
  font-size: 20px;
}
</style>
