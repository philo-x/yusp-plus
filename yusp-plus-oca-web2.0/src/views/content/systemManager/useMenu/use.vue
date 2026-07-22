<template>
  <div>
    <yu-button @click="goUse">跳转use</yu-button>
  </div>
</template>
<script>
import { request } from 'uadp-utils'
export default {
  data() {
    return {};
  },
  methods: {
    goUse() {
      var _this = this;
      request({
        url: backend.uaaService + "/oauth/authorize?client_id=use&redirect_url=http://localhost:8202&response_type=code",
        method: "get",
        async: false,
      }).then(res => {
        console.log("获取到 use 的授权码：", res);
        window.open(res.data);
      })
    },
  },
};
</script>
