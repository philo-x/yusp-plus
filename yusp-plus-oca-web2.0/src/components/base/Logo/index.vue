<!--
 * @Description: In User Settings Edit
 * @Author: your name
 * @Date: 2019-08-05 17:15:07
 * @LastEditTime: 2019-08-14 16:47:06
 * @LastEditors: Please set LastEditors
 -->
<template>
  <div :class="{'collapse':isCollapse,'yu-frame-logo': true}">
    <transition name="sidebarLogoFade">
      <router-link key="expand" class="yu-logo-min" :to="rootLink">
        <div :class="{'img-box': true,'limit-width':limitWidth}">
          <img src="@/assets/common/images/icon.png" class="sidebar-logo">
        </div>
        <h1 v-if="!isCollapse" class="logo-title" :style="'color:'+defineColor+';'">{{ title }} </h1>
      </router-link>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'SidebarLogo',
  props: {
    isCollapse: {
      type: Boolean,
      default: false
    },
    defineColor: {
      type: String,
      default: '#ffffff'
    },
    rootLink: {
      type: String,
      default: '/'
    },
    limitWidth: {
      type: Boolean,
      default: true
    },
    title: {
      type: String,
      default: '应用管理框架'
    }
  }
}
</script>
<style lang='scss'>
.yu-frame-logo {
  margin-right: 100px;
}
.collapse.yu-frame-logo .yu-logo-min {
  padding-left: 12px;
}
.yu-frame-logo .yu-logo-min {
  display: inline-block;
  padding: 10px 0;
  font-size: 22px;
  color: #fff;
  line-height: 64px;
  white-space: nowrap;
  overflow: hidden;
  padding-left: 0;
  background: none;
  padding-left: 30px;
}
.yu-frame-logo .img-box {
  float: left;
  display: inline-block;
  height: 46px;
  margin-right: 8px;
  overflow: hidden;
}
.img-box.limit-width {
  width: 36px;
}
.collapse.yu-frame-logo img {
  height: 36px;
}
.collapse.yu-frame-logo .img-box {
  height: 36px;
  margin-top: 5px;
}

.yu-frame-logo img {
  height: 46px;
  vertical-align: middle;
}
.yu-frame-logo .logo-title {
  height: 42px;
  line-height: 46px;
  float: left;
  color: #fff;
  font-size: 22px;
  white-space: nowrap;
  overflow: hidden;
  font-weight: 400;
}
</style>
