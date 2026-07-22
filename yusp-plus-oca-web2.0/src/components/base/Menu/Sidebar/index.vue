<template>
  <el-submenu ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body>
    <template slot="title">
      <item v-if="item.meta" :icon="item.meta && item.meta.icon" :title="generateTitle(item.meta.title)" />
    </template>
    <sidebar-item v-for="child in item.children" :key="child.name" :is-nest="true" :item="child" :base-path="resolvePath(child.path)" class="nest-menu" />
  </el-submenu>
</template>
<script>
export default {
  name: 'side-bar',
  data () {
    return {
    }
  }
}
</script>