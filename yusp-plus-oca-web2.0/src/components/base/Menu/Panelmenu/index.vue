<template>
  <el-submenu ref="subMenu" class="yu-root-level" :index="resolvePath(item.path)" popper-append-to-body>
    <template slot="title">
      <item v-if="item.meta" :icon="item.meta && item.meta.icon" :title="generateTitle(item.meta.title)" />
    </template>
    <template>
      <yu-base-menu-tile-item v-for="tileMenuData in item.children" :key="tileMenuData.path">
        <span slot="title" class="no-children">
          <template v-if="tileMenuData.children && tileMenuData.children.length>0">
            {{ generateTitle(tileMenuData.meta.title) }}
          </template>
          <template v-else>
            <app-link v-if="tileMenuData.meta" :to="resolvePath(tileMenuData.path)">
              <span :index="resolvePath(tileMenuData.path)" class="title-menu">
                <item :icon="tileMenuData.meta.icon||(tileMenuData.meta&&tileMenuData.meta.icon)" :title="generateTitle(tileMenuData.meta.title)" />
              </span>
            </app-link>
          </template>
        </span>
        <li :class="['tile-item']" v-for="(it, idx) in getChildrenFilter(tileMenuData)" :key="'li-' + idx">
          <app-link v-if="it.meta" :to="resolvePath(it.path)">
            <span :index="resolvePath(it.path)" class="title-menu">
              <item :icon="it.meta.icon||(it.meta&&it.meta.icon)" :title="generateTitle(it.meta.title)" />
            </span>
          </app-link>
        </li>
      </yu-base-menu-tile-item>
    </template>
  </el-submenu>
</template>
<script>
import YuBaseMenuTileItem from './YuBaseMenuTileItem'
export default {
  name: "panel-menu",
  components: {
    YuBaseMenuTileItem
  },
  data () {
    return {

    }
  }
}
</script>