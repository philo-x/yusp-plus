<!--
  @created by pan
  @updated by helin3 2010-04-06
  @description 通用跳转（重定向）路由
-->
<script>
export default {
  beforeCreate() {
    // const { params, query } = this.$route
    // const { path } = params
    // this.$router.replace({ path: '/' + path, query })
    const { params, query } = this.$route
    const { path } = params
    query._routeRedirectName = '';
    this.$router.replace({ path: '/' + path, query })
  },
  render: function(h) {
    return h() // avoid warning message
  }
}
</script>
