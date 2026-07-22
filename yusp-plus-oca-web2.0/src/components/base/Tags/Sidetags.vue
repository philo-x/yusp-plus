<template>
  <section class="yu-tags-side">
    <div class="yu-tags-side__btn">
      {{ tagBtnTitle }}
      <i :class="tagBtnIcon"></i>
    </div>
    <!--下拉按钮组-->
    <ul class="yu-tags-side__drop">
      <li class="tags-side-drop__item" v-for="(v,i) in sideTags" :key="i" @click="sideDropClick(v)">
        {{ v.title }}
        <i v-if="v.type != 'closeAll'" class="yu-tags-close yu-icon-close1" @click="closeSeletedTag(v)"></i>
      </li>
    </ul>
  </section>
</template>
<script>
export default {
  name: 'side-tags',
  props: {
    tagBtnIcon: {
      type: String,
      default: "yu-icon-more1"
    },
    tagBtnTitle: {
      type: String,
      default: ''
    },
    sideTagData: {
      type: Array,
      default: function () {
        return [
          {
            title: '表格'
          },
          {
            title: '表格+查询'
          }
        ]
      }
    }
  },
  data () {
    return {
      defaultSide: [
        {
          title: '关闭全部',
          type: 'closeAll'
        }
      ],
      sideTags: []
    }
  },
  created () {
    this.sideTags = [...this.defaultSide, ...this.sideTagData];
  },
  methods: {
    //侧边栏点击
    sideDropClick (data) {
      //全部关闭方法
      if (data.type === 'closeAll') {
        this.$emit('on-closeAll');
      }
      //点击跳转
      this.$emit('on-jump', data);
    },
    //关闭当前选中tag
    closeSeletedTag (data) {
      this.$emit('on-close', data);
    }
  }
}
</script>
<style lang='scss'>
.yu-tags-side {
  position: relative;
  display: inline-block;
  cursor: pointer;
  .yu-tags-side__btn {
    padding-left: 4px;
    padding-right: 4px;
  }
  .yu-tags-side__drop {
    display: none;
    text-align: left;
    cursor: default;
    position: absolute;
    z-index: 10;
    top: 100%;
    left: 0;
    width: 180px;
    max-height: 292px;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    padding: 10px 0;
    overflow: auto;
    background: #fff;
    -webkit-box-shadow: 0 3px 6px 0 rgba(0, 0, 0, 0.15);
    box-shadow: 0 3px 6px 0 rgba(0, 0, 0, 0.15);
    border-radius: 4px;
  }
  .tags-side-drop__item {
    height: 36px;
    line-height: 36px;
    font-size: 14px;
    text-align: left;
    color: #666;
    padding: 0 32px 0 24px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    position: relative;
    cursor: pointer;
    -webkit-transition: 0.2s;
    transition: 0.2s;
  }
  .tags-side-drop__item:hover,
  .tags-side-drop__item:active {
    color: #5557b9;
  }
  .tags-side-drop__item:hover {
    background-color: #f0f0f6;
  }
  .tags-side-drop__item:active {
    background-color: #e7e8fe;
  }
  .yu-tags-close {
    display: none;
    width: 16px;
    height: 16px;
    line-height: 16px;
    text-align: center;
    font-size: 12px;
    color: #bbb;
    position: absolute;
    right: 10px;
    top: 10px;
  }
  .tags-side-drop__item:hover i {
    display: block;
  }
  .yu-tags-side__btn i:before {
    display: inline-block;
    -webkit-transform: rotate(90deg);
    transform: rotate(90deg);
  }
  .yu-tags-side__btn:hover {
    background-color: #f0f0f6;
  }
  .yu-tags-side__btn:active {
    background-color: #eaeaea;
  }
}
.yu-tags-side:hover .yu-tags-side__drop {
  display: block;
}
</style>

