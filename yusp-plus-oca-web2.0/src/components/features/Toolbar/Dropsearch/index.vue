<style scoped>
.yu-search-btn {
  position: relative;
  display: inline-block;
  border: 1px solid #dcdfe6;
  padding-left: 15px;
  height: 32px;
  border-radius: 4px;
  -webkit-border-radius: 10em;
  -moz-border-radius: 10em;
  border-radius: 10em;
  -webkit-transition: 0.5s;
  -moz-transition: 0.5s;
  transition: 0.5s;
}
.hide-drop {
  width: 100%;
}
</style>

<template>
  <!--
   1.可设置是否展示下拉列表
   2.可配置图标
   @click.stop="move"
 -->
  <section class="yu-search-btn" :style="'width: ' + (isShow ? width : boxWidth) + 'px;'">

    <dropList v-if="dropData.length>0 && isShow" :drop-title="title" :drop-data="dropData" :drop-radio="dropRadio" @on-check="checkChange"></dropList>
    <searchBox :class="{'hide-drop': !dropData.length }" :is-show="isShow" :icon="icon" @on-search="toSearch" />

  </section>
</template>
<script>
import dropList from "./drop-list.vue";
import searchBox from "./search-box.vue";
export default {
  name: "Searchbutton",
  xtype: "YuSearchbutton",
  components: {
    dropList,
    searchBox
  },
  props: {
    dropData: {
      type: Array,
      default: function () {
        return [
          {
            id: 1111,
            name: '测试数据1'
          },
          {
            id: 2,
            name: '测试数据2'
          }
        ]
      }
    },
    dropRadio: {
      type: Boolean,
      default: true
    },
    dropTitle: {
      type: String,
      default: '请选择'
    },
    width: {
      type: [Number, String],
      default: 300
    },
    icon: {
      type: String,
      default: 'el-icon-search'
    }
  },
  data () {
    return {
      boxWidth: 40,
      isShow: false,
      title: '',
      checkDrop: {}
    }
  },
  created () {
    this.title = this.dropTitle;
  },
  methods: {
    // move (data) {
    //   this.isShow = !this.isShow;
    // },
    toSearch (data) {
      this.isShow = !this.isShow;
      const res = {
        drop: this.checkDrop,
        input: data
      };
      this.$emit('on-search', res);
    },
    checkChange (data) {
      this.checkDrop = data;
      this.title = data.name;
    }
  }
}
</script>
