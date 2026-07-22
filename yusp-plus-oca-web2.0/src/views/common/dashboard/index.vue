<template>
  <div class="dashboard-container">
    <component :is="currentRole" />
  </div>
</template>

<script>
import DefaultDashboard from './default'

export default {
  name: 'Dashboard',
  components: { DefaultDashboard },
  data() {
    return {
      currentRole: 'DefaultDashboard'
    }
  },
  created() {
    // 此处，可根据用户角色信息，动态绑定对应的组件
    // this.currentRole = 'DefaultDashboard'
  }
}
</script>
