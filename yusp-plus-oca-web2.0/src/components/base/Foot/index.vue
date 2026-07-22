<template>
  <section :class="{'yu-footer': true, 'yu-footer-fixed': fixed}">
    <div :class="{'yu-footer-ec': footType==='EC'}">
      <ul class="footer-info" v-if="footInfo.length>0">
        <li class="footer-info-item" v-for="(v,i) in footInfo" :key="i">
          <span v-if="i!==0">|</span>
          <a target="_blank" :title="v.title" :href="v.link">{{ v.title }}</a>
        </li>
      </ul>
      <p class="footer-copyright">CopyRight &copy;{{ copyRight }}</p>
    </div>
  </section>
</template>
<script>
export default {
  name: 'Foot',
  props: {
    footType: {
      type: String,
      default: 'MIS'//1.EC:电商系统 2：MIS 管理信息系统类别
    },
    fixed: {
      type: Boolean,
      default: false
    },
    footInfo: {
      type: Array,
      default: function () {
        return [
          // {
          //   link: 'http://yusys.com.cn',
          //   title: '关于宇信'
          // },
          // {
          //   link: 'http://yusys.com.cn',
          //   title: '关于宇信'
          // },
          // {
          //   link: 'http://yusys.com.cn',
          //   title: '关于宇信'
          // },
          // {
          //   link: 'http://yusys.com.cn',
          //   title: '关于宇信'
          // }
        ]
      }
    },
    copyRight: {
      type: String,
      default: 'Yusys. All Rights Reserved'
    }
  }
}
</script>
<style lang="scss" scoped>
.yu-footer {
  padding: 20px;
  background-color: #f5f5f5;
  text-align: center;
  .yu-footer-ec {
    width: 1240px;
    margin: 0 auto;
  }
  .footer-info-item {
    display: inline-block;
    font-size: 0;
    a,
    span {
      font-size: 14px;
      line-height: 30px;
    }
    a {
      margin: 0 10px;
    }
  }
  .footer-copyright {
    line-height: 30px;
    font-size: 14px;
  }
}
.yu-footer.yu-footer-fixed {
  position: fixed;
  width: 100%;
  left: 0;
  bottom: 0;
  z-index: 3000;
}
</style>

