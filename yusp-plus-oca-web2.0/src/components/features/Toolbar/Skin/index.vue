<template>
  <Toolbar tool-icon="yu-icon-theme">
    <div class="yu-skins" v-for="item in skinList" :key="item.id">
      <h1>{{ item.text }}</h1>
      <template v-if="item.id=== 'skin'">
        <div class="yu-skins-list">
          <div class="yu-skins-item yu-skins-theme" v-for="it in themesList" :key="it.id" :class="it.id" :title="it.name" @click="swithSkin(it, 'theme')">
            <span :style="{backgroundColor:it.color}"></span>
            <i v-if="it.id === theme.id" class="yu-icon-checked2"></i>
          </div>
        </div>
      </template>
      <template v-if="item.id=== 'model'">
        <div class="yu-skins-list">
          <div class="yu-skins-item yu-skins-model" v-for="it in modelList" :key="it.id" @click="swithSkin(it, 'model')" :class="it.id" :title="it.name">
            <span></span>
            <p></p>
            <b></b>
            <i v-if="it.id===model.id" class="yu-icon-checked2"></i>
          </div>
        </div>
      </template>
      <template v-if="item.id=== 'font'">
        <div class="yu-skins-list">
          <span class="yu-skins-font" v-for="it in fontSizeList" :key="it.id" @click="swithSkin(it,'size')" :title="it.name" :class="[it.id==size.id ?'yu-icon-checked2':'yu-icon-choice-un']">
            {{ it.name }}
          </span>
        </div>
      </template>
    </div>
  </Toolbar>
</template>
<script>
import Toolbar from '@/components/layout1/base/ToolBar/index'
export default {
  components: {
    Toolbar
  },
  data () {
    return {
      skinList: [
        { show: true, text: '皮肤', id: 'skin' },
        { show: true, text: '布局', id: 'model' },
        { show: true, text: '字号', id: 'font' }
      ],
      theme: { id: 'default', color: '#5557b9', name: '紫色（默认）', checked: true },
      model: { id: 'left', name: '左右(左)', checked: true },
      size: { id: 'normal', name: '正常', checked: true },
      themesList: [
        // 默认是紫色,皮肤id需要和主题中皮肤目录相同对应
        { id: 'default', color: '#5557b9', name: '紫色（默认）', checked: true },
        { id: 'blue', color: '#4e97ee', name: '蓝色' },
        { id: 'orange', color: '#ff9451', name: '橙色' }
      ],
      modelList: [
        { id: 'left', name: '左右(左)', checked: true },
        { id: 'right', name: '左右(右)' },
        { id: 'topTitle', name: '上下(平铺菜单)' },
        { id: 'topThree', name: '上下(树形菜单)' }
      ],
      fontSizeList: [
        { id: 'small', name: '较小' },
        { id: 'normal', name: '正常', checked: true },
        { id: 'large', name: '较大' }
      ]
    }
  },
  methods: {
    swithSkin (data, type) {
      this[type] = data;
    }
  }
}
</script>
<style lang="scss">
.yu-skins {
  width: 288px;
  cursor: default;
  max-height: 340px;
  h1 {
    clear: left;
    height: 42px;
    line-height: 42px;
    overflow: hidden;
    color: #666;
    font-weight: normal;
    font-size: 14px;
    padding: 0 24px;
  }
  .yu-skins-list {
    padding-left: 24px;
  }
  .yu-skins-list:after {
    content: "";
    display: block;
    clear: both;
  }
  .yu-skins-item {
    float: left;
    cursor: pointer;
    border-radius: 2px;
    overflow: hidden;
    box-sizing: border-box;
    border: 1px #ededed solid;
    transition: 0.2s;
    background-color: #fff;
    position: relative;
    margin: 8px 16px 8px 0;
  }
  //皮肤主题
  .yu-skins-theme {
    width: 32px;
    height: 32px;
    span {
      float: left;
      width: 28px;
      height: 28px;
      margin: 1px;
      border-radius: 2px;
    }
    i.yu-icon-checked2:before {
      float: left;
      line-height: 1;
      position: absolute;
      font-size: 16px;
      right: 7px;
      bottom: 7px;
      color: #fff;
    }
  }
  .yu-skins-theme:hover,
  .yu-skins-model:hover {
    border: 1px #d0d0d0 solid;
  }
  .yu-skins-theme:active,
  .yu-skins-model:active {
    border: 1px #ccc solid;
  }
  //皮肤模板
  .yu-skins-model:last-child {
    margin-right: 0;
  }
  .yu-skins-model {
    width: 48px;
    height: 36px;
    span {
      float: left;
      height: 32px;
      width: 14px;
      margin: 1px;
      background-color: #5557b9;
      overflow: hidden;
      background-image: url(../../../../../assets/common/images/menu_type.png);
      background-position: 0 0;
      background-repeat: no-repeat;
    }
    p {
      float: left;
      height: 10px;
      width: 30px;
      background-color: #fff;
      overflow: hidden;
      position: absolute;
      top: 1px;
      right: 1px;
    }
    b {
      float: left;
      height: 22px;
      width: 30px;
      background-color: #f5f5f5;
      overflow: hidden;
      position: absolute;
      right: 1px;
      bottom: 1px;
    }
    i {
      float: left;
      line-height: 1;
      position: absolute;
      font-size: 18px;
      right: 3px;
      bottom: 3px;
      color: #5557b9;
      width: 18px;
      height: 18px;
      border-radius: 100%;
      overflow: hidden;
    }
  }
  .yu-skins-model.topTitle {
    span {
      height: 10px;
      width: 44px;
      background-image: none;
    }
    p {
      display: none;
    }
    b {
      width: 44px;
      background-image: url(../../../../../assets/common/images/menu_type.png);
      background-position: 0 -55px;
      background-repeat: no-repeat;
    }
  }
  .yu-skins-model.right {
    span {
      float: right;
    }
    p,
    b {
      right: 15px;
    }
  }
  .yu-skins-model.topThree {
    span {
      height: 10px;
      width: 44px;
      background-image: none;
    }
    p {
      display: none;
    }
    b {
      width: 44px;
      background-image: url(../../../../../assets/common/images/menu_type.png);
      background-position: 0 -33px;
      background-repeat: no-repeat;
    }
  }
  //皮肤字体
  .yu-skins-font {
    float: left;
    line-height: 36px;
    font-size: 14px;
    color: #333;
    margin-right: 24px;
    cursor: pointer;
    transition: 0.2s;
  }
  .yu-icon-checked2::before {
    color: #5557b9;
  }
  .yu-skins-font.yu-icon-checked2::before {
    color: #5557b9;
  }
  .yu-skins-font::before {
    float: left;
    font-size: 18px;
    padding-right: 8px;
    color: #babae3;
  }
  .yu-skins-font:hover {
    color: #636ce4;
  }
  .yu-skins-font:active {
    color: #5557b9;
  }
}
</style>

