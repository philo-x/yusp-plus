<template>
  <Toolbar tool-icon="yu-icon-shutdown" :drop="false" @on-click="logout">
  </Toolbar>
</template>
<script>
import Toolbar from '@/components/layout1/base/ToolBar/index'
export default {
  name: 'tool-logout',
  components: {
    Toolbar
  },
  methods: {
    logout () {
      this.$emit('on-logout');
      console.log('out----');
    }
  }
}
</script>