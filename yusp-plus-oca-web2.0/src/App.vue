<!--
  @created by pan
  @updated by helin3
  @description 入口页面
-->
<template>
  <div id="app" class="yu-frame-root">
    <div :id="containterID">
      <transition>
        <router-view />
      </transition>
    </div>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      containterID: 'RT_frame'
    }
  },
  watch: {
    $route(val) {
      /* eslint prefer-rest-params:0 */
      this.containterID = val.path === '/login' ? 'RT_login' : 'RT_frame'
    }
  }
}
</script>
