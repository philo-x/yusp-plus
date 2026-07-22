<template>
  <div class="ck" :id="'tabBox_' + containterID">
    <transition name="fade-transform" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view :key="key" />
      </keep-alive>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'AppMain',
  computed: {
    cachedViews() {
      const cvs = [];
      this.$store.state.tagsView.cachedViews.forEach(element => {
        cvs.push(element.name);
      });
      // console.log(cvs);
      return cvs;
    },
    key() {
      return this.$route.fullPath
    },
    containterID() {
      return this.$route.name
    }
  },
  mounted() {
  }
}
</script>
