<!--
  @created by helin3 2010-04-18
  @updated by
  @description 通用加载外链页面,菜单配置
  { menuId: 'dm-22010', menuName: 'iframeLink', upMenuId: 'dm-22000', menuIcon: '', funcId: 'iframeLink', funcUrl: 'iframelink=http%3A%2F%2Fwww.yusys.com.cn' },
-->
<template>
  <div class="iframe-container">
    <iframe ref="refIframe" v-if="src" :src="src" frameborder="no" width="100%" :height="height" :allowfullscreen="allowfullscreen" />
  </div>
</template>

<script>
export default {
  name: 'IframeContainer',
  data() {
    const bcr = document.body.getBoundingClientRect()
    return {
      height: (bcr.height - 88), // 减去头部区域高度
      src: '',
      allowfullscreen: false
    }
  },
  created() {
    
    let { routeUrl } = this.$route.meta
    routeUrl = routeUrl.split('=')[1] || ''
    routeUrl = decodeURIComponent(routeUrl)
    this.src = routeUrl
    //this.src = 'WebProject2/yuxp/framework/pages/YuXPMenu.html'
  },
  mounted() {
    console.log('内部外链地址', this.$route, this.$refs.refIframe)
    //const param = this.$route.meta.routeUrl.replace('iframelink=', '').split('&')
    //const _menuUrl = param[0].split('=')[1]
    //const _menuType = param[1].split('=')[1]
    //const domIframe = this.$refs.refIframe
    //domIframe.contentWindow['MenuURL'] = _menuUrl; //
    //domIframe.contentWindow['menuType'] = _menuType.toUpperCase();
  }
}
</script>

