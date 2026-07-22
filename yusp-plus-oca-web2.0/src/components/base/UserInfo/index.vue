<!--用户信息-->
<template>
  <div class="yu-user">
    <span v-if="!isLogin">
      <a>注册</a>
      &nbsp;|&nbsp;
      <a>登录</a>
    </span>
    <el-dropdown v-else trigger="hover">
      <div class="avatar-wrapper">
        <div class="avator">
          <img class="user-logo" :src="userInfo.userAvatar || defaultAvatar" alt="user-logo">
        </div>
        <div :class="{'name-role': true, 'has-role': currRole.name}">
          <label class="text-ellipsis" :title="userInfo.userName">{{ userInfo.userName }}</label>
          <p v-if="currRole.name">{{ currRole.name }}</p>
        </div>
        <i class="yu-icon-arr-down1" />
      </div>
      <el-dropdown-menu slot="dropdown" class="yu-user-info">
        <slot name="userItem"></slot>
        <el-dropdown-item class="user-role-item">
          <router-link :to="links.info">
            <span>个人资料</span>
          </router-link>
        </el-dropdown-item>
        <el-dropdown-item class="user-role-item">
          <router-link :to="links.pwd">
            <span>修改密码</span>
          </router-link>
        </el-dropdown-item>
        <!--<el-dropdown-item>-->
        <div class="el-dropdown-menu__item user-role-item" @click.stop="logout">退出登录</div>
        <!--</el-dropdown-item>-->
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>
<script>
import defaultAvatar from '@/assets/common/images/photo.svg'
export default {
  name: 'userInfo',
  props: {
    currRole: {
      type: Object,
      default: function () {
        return {}
      }
    },
    userInfo: {
      type: Object,
      default: function () {
        return {
          userName: '小宇'
        }
      }
    },
    links: {
      type: Object,
      default: function () {
        return {
          info: '/common/user/modify-user-info',
          pwd: '/common/user/modify-password'
        }
      }
    }
  },
  data () {
    return {
      isLogin: true,
      defaultAvatar: defaultAvatar
    }
  },
  methods: {
    logout () {
      this.$emit('on-logout');
    }
  }
}
</script>
<style lang='scss'>
.yu-user {
  .avatar-wrapper {
    float: left;
    height: 63px;
    position: relative;
    padding-right: 24px;
  }
  .name-role {
    float: left;
  }
  .name-role.has-role {
    margin-top: 14px;
    line-height: 20px;
  }
  .name-role label,
  .name-role p {
    white-space: nowrap;
    max-width: 112px;
    overflow: hidden;
    text-overflow: ellipsis;
    cursor: default;
  }
  .name-role p {
    font-size: 12px;
    color: #949494;
  }
  .name-role label {
    font-size: 14px;
    color: #444;
    font-weight: 400;
  }
  .avator {
    float: left;
    width: 44px;
    height: 44px;
    border-radius: 22px;
    overflow: hidden;
    background-color: #f0f0f6;
    margin: 10px 10px 0 20px;
  }
  .user-logo {
    float: left;
    width: 44px;
    height: 44px;
    border-radius: 22px;
  }
  .avatar-wrapper i {
    position: absolute;
    right: 0;
    float: left;
    height: 16px;
    line-height: 16px;
    width: 16px;
    text-align: center;
    top: 50%;
    margin-top: -8px;
    color: #949494;
    font-size: 12px;
    -webkit-transition: 0.2s;
    transition: 0.2s;
  }
  .avatar-wrapper:hover i {
    display: block;
    color: #5557b9;
    -webkit-transform: rotate(180deg);
    transform: rotate(180deg);
  }
}
.yu-user-info {
  text-align: left;
  cursor: default;
  position: absolute;
  z-index: 10;
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
  .user-role-item {
    display: block;
    line-height: 36px;
    height: 36px;
    font-size: 14px;
    color: #333;
    cursor: pointer;
    -webkit-transition: 0.2s;
    transition: 0.2s;
    padding: 0 32px 0 24px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    position: relative;
  }
  .user-role-item a {
    font-size: 14px;
  }
  .yu-icon-checked2::before {
    color: #5557b9;
  }
  .check-label.yu-icon-checked2::before {
    color: #5557b9;
  }
  .check-label::before {
    float: left;
    font-size: 18px;
    padding-right: 8px;
    color: #babae3;
  }
  hr {
    height: 1px;
    background-color: #ededed;
    display: block;
    margin: 10px 0;
    overflow: hidden;
    padding: 0;
    border: 0;
  }
}
.yu-user-info .el-dropdown-menu span,
.yu-user-info .el-dropdown-menu__item {
  font-size: 14px;
}
.el-dropdown-menu span:hover,
.el-dropdown-menu__item:hover {
  color: #5557b9;
  background-color: #f0f0f6;
}
.el-dropdown-menu span:active,
.el-dropdown-menu__item:active {
  color: #5557b9;
  background-color: #e7e8fe;
}
</style>

