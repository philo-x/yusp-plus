<template>
  <!--
  1.可配置是否展示单选图标
-->
  <section class="yu-drop">
    <h4 class="title" :title="checkTitle">
      <span class="text-overflow">{{ checkTitle }}</span>
      <i class="el-icon-arrow-down"></i>
    </h4>
    <ul class="yu-drop-list">
      <li class="item" v-for="(item,i) in dropDatas" :key="i">
        <YuRadio v-if="dropRadio" :group-name="dropTitle" :name="item.name" :index="i" :check="i==defaultCheck" @on-check="checkChange($event,item)" />
        <span v-else @click.stop="checkChange(i,item)">{{ item.name }}</span>
      </li>
    </ul>
  </section>
</template>
<script>
import YuRadio from "./radio.vue";
export default {
  components: {
    YuRadio
  },
  props: {
    dropTitle: {
      type: String,
      default: "u-drop-check-name"
    },
    dropData: {
      type: Array,
      default: function () {
        return []
      }
    },
    dropRadio: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {
      defaultCheck: 0,
      checkVals: [],
      checkTitle: "",
      dropDatas: [
        {
          name: "2222",
          select: true,
          id: "001"
        },
        {
          name: "4444",
          select: true,
          id: "002"
        },
        {
          name: "5555",
          select: true,
          id: "003"
        }
      ]
    }
  },
  created () {
    this.dropDatas = this.dropData;
    this.checkVals = this.dropDatas[0] || {};
    this.checkTitle = this.dropTitle;
  },
  methods: {
    checkChange (data, item) {
      this.defaultCheck = data;
      this.checkTitle = item.name;
      this.$emit("on-check", item);
    }
  }
}
</script>
<style>
.yu-drop {
  float: left;
  position: relative;
  width: 80px;
  -webkit-transition: 0.3s;
  -moz-transition: 0.3s;
  transition: 0.3s;
}
.yu-drop .title {
  height: 32px;
  line-height: 32px;
  margin: 0;
}
.yu-drop .title span {
  display: inline-block;
  height: 32px;
  width: calc(100% - 30px);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  cursor: pointer;
}
.yu-drop .title i {
  float: right;
  line-height: 32px;
}
.yu-drop:hover .title i {
  transform: rotate(180deg);
  -ms-transform: rotate(180deg);
  -webkit-transform: rotate(180deg);
  -moz-transform: rotate(180deg);
}
.yu-drop:hover .title,
.yu-drop:hover .title i {
  color: #5557b9;
}
.yu-drop:hover .yu-drop-list {
  display: block;
}
.yu-drop .yu-drop-list {
  position: absolute;
  display: none;
  width: 130px;
  left: -14px;
  top: 32px;
  padding: 10px 0;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  z-index: 1000;
  background: #ffffff;
  box-shadow: 0px 3px 6px 0px rgba(0, 0, 0, 0.15);
  -ms-box-shadow: 0px 3px 6px 0px rgba(0, 0, 0, 0.15);
  -webkit-box-shadow: 0px 3px 6px 0px rgba(0, 0, 0, 0.15);
  -moz-box-shadow: 0px 3px 6px 0px rgba(0, 0, 0, 0.15);
}
.yu-drop-list li {
  padding: 5px 15px;
  list-style: none;
  line-height: 1;
}
.yu-drop-list li:hover {
  color: #5557b9;
  background-color: #f0f0f6;
  cursor: pointer;
}
</style>
