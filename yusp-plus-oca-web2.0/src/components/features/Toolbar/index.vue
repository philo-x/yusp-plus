<template>
  <div class="toolbar-box">
    <component v-for="(app,i) in comps" :is="app" :key="i"></component>
  </div>
</template>
<script>
import Msgbox from './Msgbox/index'
import Lang from './Lang/index'
import Logout from './Logout/index'
import Skin from './Skin/index'
import Dropsearch from './Dropsearch/index'
import Screenfull from './Screenfull/index'
export default {
  components: {
    Msgbox,
    Lang,
    Logout,
    Skin,
    Dropsearch,
    Screenfull
  },
  props: {
    showObj: {
      type: Object,
      default: function () {
        return {
          Dropsearch: true,
          Msgbox: true,
          Lang: true,
          Logout: true,
          Skin: true,
          Screenfull: true
        }
      }
    }
  },
  data () {
    return {
      comps: []
    }
  },
  created () {
    this.comps = [];
    Object.keys(this.showObj).forEach((i) => {
      this.comps.push(i);
    })
  }
}
</script>
<style lang="scss">
.toolbar-box {
  display: inline-block;
  .yu-search-btn {
    float: left;
    margin-top: 15px;
    margin-right: 10px;
  }
  .yu-input i {
    padding: 0 6px;
  }
  sup {
    top: -0.8em;
  }
}
</style>
