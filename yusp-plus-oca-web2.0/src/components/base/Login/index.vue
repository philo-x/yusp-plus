<!--
  @Created by zhuly8@yusys.com.cn 2021-01-30
  @updated by
  @description 登录页面
 -->
<template>
  <div class="yu-login" :class="lightModel ? 'yu-login-light' : ''">
    <div class="yu-login-box" @keyup.13="loginFn" v-if="!isSingleLogin">
      <div class="yu-login-logo" v-if="logoinfo">
        <img :src="logoinfo.url" />
        <span>{{ logoinfo.assisttext }}</span>
      </div>
      <ul class="yu-login-form">
        <li>
          <input id="username" v-model="username" type="text" :placeholder="$t('login.zh')" />
          <i class="yu-icon-user"></i>
        </li>
        <li>
          <input id="password" v-model="password" type="password" :placeholder="$t('login.mm')" />
          <i class="yu-icon-lock"></i>
        </li>
        <li>
          <input id="code" type="text" :placeholder="$t('login.yzm')" ref="imageCode" v-model="imageCode" />
          <i class="yu-icon-details"></i>
          <img class="yu-login-code" :title="$t('login.tpkbqdjhyz')" @click="freshImageCodeFn" :src="imageCodePicture" />
        </li>
        <!--<li class="checkbox_li">
          <yu-checkbox id="remember" type="checkbox"></yu-checkbox>
          <label for="remember">{{ $t('login.jzyh') }}</label>
          <span>{{ $t('login.wjmm') }}</span>
        </li>-->
        <li>
          <yu-radio-group v-model="language" @change="switchLanguage">
            <yu-radio v-for="item in languageList" :label="item.id" :key="item.id">{{ item.name }}</yu-radio>
          </yu-radio-group>
        </li>
        <li class="submit-btn">
          <yu-button id="submitBtn" type="primary" v-loading="btnLoginLoading.show" @click="loginFn">{{ $t('login.dl') }}</yu-button>
        </li>
        <li class="msg" v-show="message">
          <span class="yu-icon-warning1"></span>
          <span class="yu-login-msg" id="msg">{{ message }}</span>
        </li>
      </ul>
    </div>
    <div class="single-login" v-if="isSingleLogin">跳转中，请稍等...</div>
    <div class="yu-login-footer" v-html="$t('login.yxkj')"></div>
    <!-- 首次登录 修改密码 -->
    <yufp-password-modify :dialog-visible.sync="dialogVisible" :login-code-first="loginCode" :old-passWord="password" :first-login-res="firstLoginRes" :first-login="true">
    </yufp-password-modify>
  </div>
</template>
<script>
/* eslint camelcase:0 */
/* eslint vars-on-top:0 */
/* eslint no-inner-declarations:0 */
import {loginFn, replaySeqFn} from '@/api/common/oauth';
import { IS_REPLAY } from '@/config/constant/app.data.common'
import {genUUID} from '@/utils';
import {JSEncrypt} from 'jsencrypt';
import {getRSAPublicKey, getSystemName} from '@/utils/util';
import {request} from 'uadp-utils'
import navbarMixin from '@/components/layout/Navbar/navbar.mixin'

const isSingleServer = process.env.VUE_APP_SINGLE_SERVER === 'true';
const isIdentifingCode = process.env.VUE_APP_IDENTIFING_CODE === 'true';

export default {
  name: 'YuLogin',
  mixins: [navbarMixin],
  props: {
    // 登录页logo
    logoinfo: {
      type: Object,
      default() {
        return {
          url: require('@/assets/common/images/icon.png'),
          assisttext: getSystemName()
        };
      }
    },
    // 公司名称
    corpName: {
      type: String,
      default: '宇信银行'
    }
  },
  data: function () {
    return {
      lightModel: false, // 浅色模式开关
      username: '',
      password: '',
      localToken: null,
      message: null,
      borderColor: null,
      imageCode: '', // 验证码
      imageUUID: genUUID(16, 16) + Date.parse(new Date()),
      imageCodePicture: '',
      // imageCodePicture: this.freshImageCodeFn(),
      btnLoginLoading: { // 控制登录按钮loading
        show: false,
        timeout: 12000
      },
      resetPwdFormData: {
        oldPassWord: null,
        newPassWord: null,
        confirmPassWord: null
      },
      dialogVisible: false,
      firstLoginRes: {}, // 首次登录返回对象
      isSingleLogin: false,
      language: this.$store.state.app.language,
    };
  },
  computed: {
    loginCode: function() {
      return this.username;
    }
  },
  mounted: function () {
    console.log(this.language)
    if (process.env.VUE_APP_SINGLE_LOGING === 'true') {
      // 计划暂时移除 单点登录
      this.isSingleLogin = true;
      this.singleLoginFn();
    } else {
      this.imageCodePicture = this.freshImageCodeFn();
    }
  },
  destroyed: function () {
  },
  methods: {
    singleLoginFn() {
      var loc = window.location.href; // 获取当前地址栏信息
      var n1 = loc.indexOf("?"); // 找到地址栏中？的位置
      if (loc.indexOf('success=true') === -1) { // 没有success=true标志
        request({
          url: backend.uaaService + '/api/index',
          method: 'POST',
          needToken: false
        })
      } else {
        request({
          url: backend.uaaService + '/api/sso/login',
          method: 'POST',
          needToken: false
        }).then(res => {
          this.$store.commit('oauth/SET_TOKEN', {
            access_token: res.access_token,
            expires_in: res.expires_in
          });
          this.$store.dispatch('oauth/getAccessInfo').then((resData) => {
            var newUrl = decodeURI(loc.substr(0, n1 - 1));
            window.location.href = newUrl;
          });
        })
      }
    },
    freshImageCodeFn: function () {
      this.imageCodePicture = backend.uaaService + '/api/codeimage/' + this.imageUUID + '?t=' + new Date().getTime();
      this.imageCode = '';
      return this.imageCodePicture;
    },
    loginFn: function () {
      this.message = '';
      var nameEl = document.getElementById('username');
      var pwdEl = document.getElementById('password');
      var codeEl = document.getElementById('code');
      if (this.username === '') {
        this.message = this.$t('login.qsryhm');
        nameEl.focus();
        return;
      }
      if (this.password === '') {
        this.message = this.$t('login.qsrmm');
        pwdEl.focus();
        return;
      }
      if(isIdentifingCode && this.imageCode === '') {
        this.message = this.$t('login.qsryzm');
        codeEl.focus();
        return;
      }
      var _this = this;
      this.btnLoginLoading.show = true;
      const timer = setTimeout(() => {
        clearInterval(timer);
        this.btnLoginLoading.show = false;
        this.message = '请求超时，请重试！';
        // this.$message({message: '请求超时，请重试！', type: 'warning'});
      }, this.btnLoginLoading.timeout);
      this.borderColor = 'lightgreen';
      if(!IS_REPLAY) {
        _this.login(timer);
      }else{
        replaySeqFn().then(response => {
          _this.login(timer, response.data);
        }).catch((e) => {
          console.log(e)
          const response = e.response.data;
          this.btnLoginLoading.show = false;
          clearInterval(timer);
          this.message = response.message || this.$t('login.dlsbqlxxtgly');
          this.freshImageCodeFn();
        });
      }
    },
    login: function (timer, seqInfo) {
      var seq = '';
      var num = '';
      if(seqInfo) {
        seq = seqInfo.seq;
        num = seqInfo.num + "-";
      }
      loginFn(
        {
          imageUUID: this.imageUUID,
          imageCode: this.imageCode,
          username: this.username,
          seq: seq,
          password: this.encryptPassword(num + this.password),
          grant_type: 'oca'
        }
      ).then(response => {
        this.btnLoginLoading.show = false;
        clearInterval(timer);
        if (response && response.access_token && response.code === '0000') { // 1、登录成功
          this.message = null;
          this.$store.commit('oauth/SET_TOKEN', {
            access_token: response.access_token,
            expires_in: response.expires_in,
            refresh_token: response.refresh_token
          });
          this.$store.dispatch('oauth/getAccessInfo').then((resData) => {
            this.redirectToFrame();
          });
        }
        if (response && response.code === '10000002') { // 2、首次登录
          var _this = this;
          _this.$confirm(_this.$t('login.scdlmmxg'), _this.$t('component.msg_title'), {
            confirmButtonText: _this.$t('component.btn_confirm'),
            cancelButtonText: _this.$t('component.btn_cancel'),
            type: 'warning'
          }).then(function () {
            _this.freshImageCodeFn();
            // 保存 token信息用于修改密码后登出
            _this.$store.commit('oauth/SET_TOKEN', {
              access_token: "response.access_token",
              expires_in: response.expires_in
            });
            const { access_token, expires_in } = response;
            _this.localToken = { access_token, expires_in }; // 暂存，此时还不能访问需要认证的资源
            _this.firstLoginRes = response;
            _this.dialogVisible = true;
            // _this.message = response.message ? response.message : _this.$t('login.scdlmmxg');
          })
        }
      }).catch((e) => {
        console.log(e)
        const response = e.response.data;
        this.btnLoginLoading.show = false;
        clearInterval(timer);
        this.message = response.message || this.$t('login.dlsbqlxxtgly');
        this.freshImageCodeFn();
      });
    },
    redirectToFrame: function () {
      this.$router.push({ path: this.redirect || '/' });
    },
    // 登录密码加密
    encodePassword: function (pwd) {
      var encryptPwd = this.encryptPassword(pwd);
      // #TODO 不进行encodeURIComponent编码
      // var encodePwd = encodeURIComponent(encryptPwd);
      // return encodePwd;
      return encryptPwd;
    },
    // 匹配密码加密
    encryptPassword: function (pwd) {
      var encrypt = new JSEncrypt();
      encrypt.setPublicKey(getRSAPublicKey());
      var encryptPwd = encrypt.encrypt(pwd);
      return encryptPwd;
      // return pwd;
    }
  }
};
</script>

<style lang="scss">
@import "~~assets/common/login.css";
.single-login {
  color: #fff;
  font-size: 20px;
  width: 200px;
  position: absolute;
  top: 50%;
  text-align: center;
  transform: translateY(-50%);
  -ms-transform: translateY(-50%);
  right: calc(50% - 100px);
}
</style>
