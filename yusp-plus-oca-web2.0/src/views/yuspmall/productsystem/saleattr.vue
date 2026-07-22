<template>
  <div>
    <base-attr :attrtype="0"></base-attr>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';
import BaseAttr from "./baseattr";
export default {
  //import引入的组件需要注入到对象中才能使用
  components: { BaseAttr },
  props: {},
  data() {
    //这里存放数据
    return {};
  },
  //计算属性 类似于data概念
  computed: {},
  //监控data中的数据变化
  watch: {},
  //方法集合
  methods: {},
};
</script>
<style scoped></style>
