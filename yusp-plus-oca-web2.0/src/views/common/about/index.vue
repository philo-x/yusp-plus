<template>
  <div class="about-containter">
    <h2>当前项目git路径</h2>
    <a href="http://xxx.xxx.xxx.xxx/YUFP/xy-web.git" rel="noopener noreferrer" target="_blank">
      <h4>xy-web</h4>
    </a>
    <h2>当前项目基于vue-cli 3.9构建，主要使用组件</h2>
    <ul>
      <li>
        <a href="http://xxx.xxx.xxx.xxx:9527/xy-guide-v2" rel="noopener noreferrer" target="_blank">
          <h4>使用文档</h4>
        </a>
      </li>
      <li>
        <a href="http://xxx.xxx.xxx.xxx:9527/yufp-widgets-pc-v2" rel="noopener noreferrer" target="_blank">
          <h4>UI组件文件</h4>
        </a>
      </li>
      <li>
        <a href="http://xxx.xxx.xxx.xxx:9527/yufp-widgets-pc-v2" rel="noopener noreferrer" target="_blank">
          <h4>业务组件文档</h4>
        </a>
      </li>
      <li>
        <a href="http://xxx.xxx.xxx.xxx:9527/yufp-widgets-pc-v2" rel="noopener noreferrer" target="_blank">
          <h4>工具类文档</h4>
        </a>
      </li>
      <li>
        <a href="https://router.vuejs.org/zh/" rel="noopener noreferrer" target="_blank">
          <h4>vue-router</h4>
        </a>
      </li>
      <li>
        <a href="https://vuex.vuejs.org/" rel="noopener noreferrer" target="_blank">
          <h4>vuex</h4>
        </a>
      </li>
      <li>
        <a href="http://www.axios-js.com/" rel="noopener noreferrer" target="_blank">
          <h4>axios</h4>
        </a>
      </li>
      <li>
        <a href="https://www.webpackjs.com/" rel="noopener noreferrer" target="_blank">
          <h4>webpack</h4>
        </a>
      </li>
    </ul>
  </div>
</template>
<style lang="scss" scoped>
.about-containter {
  background-color: #ffffff;
  padding: 24px;
  h4,
  li {
    line-height: 36px;
  }
  li {
    list-style-type: decimal;
  }
  a {
    text-decoration: underline;
    color: #5557b9;
    font-weight: bold;
  }
}
</style>
