<!--
 * @Description: In User Settings Edit
 * @Author: your name
 * @Date: 2019-08-05 17:15:07
 * @LastEditTime: 2019-08-14 16:47:06
 * @LastEditors: Please set LastEditors
 -->
<template>
  <!-- <div class="sidebar-logo-container yu-frame-menu" :class="{'collapse':collapse}">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img src="@/assets/common/images/icon.png" class="sidebar-logo">
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link yu-logo-min" to="/">
        <img src="@/assets/common/images/icon.png" class="sidebar-logo">
        <h1 class="sidebar-title">{{ title }} </h1>
      </router-link>
    </transition>
  </div>-->
  <div :class="{ 'yu-logo-min': !isCollapse, 'yu-logo-collapse': isCollapse }">
    <i style="display:inline-block">
      <img src="@/assets/common/images/icon.png" />
      <span>{{ title }}</span>
    </i>
  </div>
</template>

<script>
export default {
  name: "SidebarLogo",
  props: {
    isCollapse: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      title: this.$t('component.ptglxt'),
    };
  },
};
</script>
