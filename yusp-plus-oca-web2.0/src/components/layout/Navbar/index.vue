<template>
  <div class="navbar yu-frame-top-bar">
    <div v-if="isLeft||isRight" class="yu-frame-top-bar-left">
      <hamburger
        id="hamburger-container"
        :is-active="sidebar.opened"
        class="hamburger-container"
        @toggleClick="toggleSideBar"
      />
      <breadcrumb
        v-if="baseFrameOptions.breadCrumb"
        id="breadcrumb-container"
        class="breadcrumb-container"
      />
    </div>
    <div v-else class="yu-logo">{{ appName }}</div>
    <div class="yu-app-box-top" v-if="false">
      <label class="yu-app-box-top-label">{{ $t('component.currentSystem') }}</label>
      <div class="yu-app-box-top-button-wrap">
        <div class="yu-app-box-top-button">
          {{ appName }}
          <i class="yu-icon-arr-down1"></i>
          <div ref="appList" class="yu-frame-dropdown-menu">
            <span v-for="(item, index) in appOptions" :key="index" @click="changeApp(item, index)">
              <i :class="[item.checked ? 'yu-icon-checked2' : 'yu-icon-choice-un']"></i>
              {{ item.applicationName }}
            </span>
          </div>
        </div>
      </div>
    </div>
    <!--<logo v-else-if="showLogo && isTop" :is-collapse="false" />-->
    <div class="yu-frame-top-bar-right">
      <span
        class="sys-tools"
        v-for="(item,index) in sysToolsFiltered"
        :key="index"
        :title="(item.event && item.event.click)?item.text:''"
        :class="[item.icon, item.className, { 'serach-show' : (showSearchInput && item.id==='search'), click: (item.event && item.event.click) } ]"
        @click="toolClick(item,$event)"
        @mouseenter="toolMouseEnter(item,$event)"
        @mouseout="toolMouseOut(item,$event)"
      >
        <yu-badge v-if="item.badgeDot" is-dot></yu-badge>
        <!--search sta-->
        <div v-if="item.id==='search'" class="yu-frame-search" title>
          <div class="yu-frame-search-type">
            <span>
              {{ searchType.name }}
              <i class="yu-icon-arr-down1"></i>
            </span>
            <ul>
              <li
                v-for="(it,idx ) in searchTypeList"
                :key="idx"
                @click="searchTypeSwitch(it.id,$event)"
                :class="[it.checked?'yu-icon-checked2':'yu-icon-choice-un']"
                :title="it.name"
              >{{ it.name }}</li>
            </ul>
          </div>
          <yu-input :placeholder="searchPlaceholder" v-model="searchValue" @input="searchDataFilter"></yu-input>
          <i class="yu-frame-search-button" :class="item.icon" @click="searchBarClick"></i>
        </div>
        <!--search end-->
        <!--message sta-->
        <div v-if="item.id==='message'" class="yu-frame-dropdown-menu yu-frame-message" title>
          <yu-tabs value="all" @tab-click="msgTabClick">
            <yu-tab-pane :label="messageTitle1" name="all">
              <ul class="yu-frame-message-list">
                <li v-for="(it,idx) in userMessages" :key="idx">
                  <i :class="[it.type===0?'yu-icon-finish todo':'yu-icon-message3 msg']"></i>
                  <p>
                    <span :title="it.from+it.msg">
                      <b>{{ it.from }}</b>
                      {{ it.msg }}
                    </span>
                    <span>
                      <i>{{ it.dateTime }}</i>
                      <i v-if="it.state">{{ it.state }}</i>
                      <a href="javascript:void(0);">
                        <template v-if="it.type===0">{{ messageBtn1 }}</template>
                        <template v-else>{{ messageBtn2 }}</template>
                      </a>
                    </span>
                  </p>
                </li>
              </ul>
              <div class="yu-frame-message-buttons">
                <yu-button type="text">{{ messageBtn3 }}</yu-button>
                <yu-button type="text">{{ messageBtn4 }}</yu-button>
              </div>
            </yu-tab-pane>
            <yu-tab-pane :label="messageTitle2" name="to">
              <ul class="yu-frame-message-list">
                <li v-for="(it,idx) in userMessagesNoReadFiltered" :key="idx">
                  <i :class="[it.type===0?'yu-icon-finish todo':'yu-icon-message3 msg']"></i>
                  <p>
                    <span :title="it.from+item.msg">
                      <b>{{ it.from }}</b>
                      {{ it.msg }}
                    </span>
                    <span>
                      <i>{{ it.dateTime }}</i>
                      <i v-if="it.state">{{ it.state }}</i>
                      <a href="javascript:void(0);">
                        <template v-if="it.type===0">{{ messageBtn1 }}</template>
                        <template v-else>{{ messageBtn2 }}</template>
                      </a>
                    </span>
                  </p>
                </li>
              </ul>
              <div class="yu-frame-message-buttons">
                <yu-button type="text">{{ messageBtn3 }}</yu-button>
                <yu-button type="text">{{ messageBtn4 }}</yu-button>
              </div>
            </yu-tab-pane>
            <yu-tab-pane :label="messageTitle3" name="message">
              <ul class="yu-frame-message-list">
                <li v-for="(it,idx) in userMessagesFiltered" :key="idx">
                  <i :class="[it.type===0?'yu-icon-finish todo':'yu-icon-message3 msg']"></i>
                  <p>
                    <span :title="it.from+it.msg">
                      <b>{{ it.from }}</b>
                      {{ it.msg }}
                    </span>
                    <span>
                      <i>{{ it.dateTime }}</i>
                      <i v-if="it.state">{{ it.state }}</i>
                      <a href="javascript:void(0);">
                        <template v-if="it.type===0">{{ messageBtn1 }}</template>
                        <template v-else>{{ messageBtn2 }}</template>
                      </a>
                    </span>
                  </p>
                </li>
              </ul>
              <div class="yu-frame-message-buttons">
                <yu-button type="text">{{ messageBtn3 }}</yu-button>
                <yu-button type="text">{{ messageBtn4 }}</yu-button>
              </div>
            </yu-tab-pane>
            <yu-tab-pane :label="noticeTit" name="notice">
              <ul class="yu-frame-message-list">
                <li v-for="(it,idx) in noticeList" :key="idx">
                  <i :class="[it.type===0?'yu-icon-finish todo':'yu-icon-message3 msg']"></i>
                  <p>
                    <span :title="it.creatorName+it.noticeTitle">
                      <b>{{ it.creatorName }}{{ $t('notice.fb') }}</b>
                      {{ it.noticeTitle }}
                    </span>
                    <span>
                      <i>{{ it.creatorTime }}</i>
                      <a class="custom-a" @click="noticeInfoFn(it)">
                        <template> {{ messageBtn2 }}</template>
                      </a>
                    </span>
                  </p>
                </li>
                <li v-if="!noticeList.length" class="no-notice-data">
                  <img src="@/assets/common/images/no-data.svg">
                  <p>{{ $t('notice.zwwdgg') }}</p>
                </li>
              </ul>
              <div class="yu-frame-message-buttons">
                <yu-button type="text" @click="clearNoticeFn">{{ messageBtn3 }}</yu-button>
                <yu-button type="text" @click="moreNoticeFn">{{ messageBtn4 }}</yu-button>
              </div>
            </yu-tab-pane>
          </yu-tabs>
        </div>
        <!--message sta-->
        <!--themes sta-->
        <div v-if="item.id === 'themes'" class="yu-frame-dropdown-menu yu-frame-themes" title>
          <template v-for="(itm,i) in themeToolFiltered">
            <h1 :key="itm.id + '_' + i">
              {{ itm.text }}
              <span v-if="itm.extend==='true'" class="yu-frame-themes-right">
                自动
                <yu-switch
                  @change="changeModelFn"
                  :width="48"
                  v-model="autoSizeModel"
                  on-text="是"
                  off-text="否"
                ></yu-switch>
              </span>
            </h1>
            <template v-if="itm.id=== 'skin'">
              <div class="yu-frame-themes-list" :key="itm.id">
                <div
                  v-for="(it,idx) in themesList"
                  :key="idx"
                  :class="it.id"
                  :title="it.name"
                  @click="switchThemes(it.id)"
                >
                  <span :style="{backgroundColor:it.color}"></span>
                  <i v-if="it.checked" class="yu-icon-checked2"></i>
                </div>
              </div>
            </template>
            <template v-if="itm.id=== 'model'">
              <div class="yu-frame-model-list" :key="itm.id">
                <div
                  v-for="(it,idx) in menuModelList"
                  :key="idx"
                  @click="switchMenuModel(it.id)"
                  :class="it.id"
                  :title="it.name"
                >
                  <span :class="it.id"></span>
                  <p :class="it.id"></p>
                  <b></b>
                  <i v-if="it.checked" class="yu-icon-checked2"></i>
                </div>
              </div>
            </template>
            <template v-if="itm.id=== 'font'">
              <div class="yu-frame-font-list" :key="itm.id">
                <span
                  v-for="(it,idx) in fontSizeList"
                  :key="idx"
                  @click="switchFontSize(it.id)"
                  :title="it.name"
                  :class="[it.checked?'yu-icon-checked2':'yu-icon-choice-un']"
                >{{ it.name }}</span>
              </div>
            </template>
            <template v-if="itm.id==='sizeModel'">
              <div class="yu-frame-font-list" :key="itm.id">
                <span
                  v-for="(it,idx) in sizeModelList"
                  :key="idx"
                  @click="switchFontSizeModel(it.id)"
                  :title="it.name"
                  :class="[it.id === currentSizeModeId?'yu-icon-checked2':'yu-icon-choice-un']"
                >{{ it.name }}</span>
              </div>
            </template>
          </template>
        </div>
        <div
          class="yu-frame-dropdown-menu"
          title
          v-if="item.id=== 'language'&&languageList.length>1"
        >
          <span
            v-for="(it,idx) in languageList"
            :key="idx"
            @click="switchLanguage(it.id)"
            :class="[it.checked?'yu-icon-checked2':'yu-icon-choice-un']"
            :title="it.name"
          >{{ it.name }}</span>
        </div>
      </span>
      <div class="yu-frame-top-user">
        <i class="yu-icon-arr-down1"></i>
        <div class="pic">
          <img :src="userInfo.pic" :title="userInfo.name+'，'+selectedRoles.name" />
        </div>
        <div class="name-role" :title="userInfo.name+'，'+selectedRoles.name">
          <b>{{ userInfo.name }}</b>
          <span>{{ selectedRoles.name }}</span>
        </div>
        <div class="yu-frame-dropdown-menu" title>
          <template v-if="userInfo.roles.length>1">
            <span
              v-for="(item, index) in userInfo.roles"
              :key="index"
              @click="switchRole(item)"
              :class="selectedRoles.id === item.id ? 'yu-icon-checked2' : 'yu-icon-choice-un'"
            >{{ item.name }}</span>
            <hr />
          </template>
          <!-- <span @click="personalData">{{ $t('component.personalData') }}</span> -->
          <span @click="modifyPwdFn">{{ $t('component.changePassword') }}</span>
          <span @click="logoutFn">{{ $t('component.logout') }}</span>
        </div>
      </div>
    </div>
    <yufp-password-modify :dialog-visible.sync="pwdDialogVisible" :first-login="false">
    </yufp-password-modify>
    <yufp-personal-data :dialog-visible.sync="personInfoDialogVisible">
    </yufp-personal-data>
  </div>
</template>

<script>
import navbarMixin from './navbar.mixin';
import YufpPasswordModify from "@/components/widgets/YufpPasswordModify"
import YufpPersonalData from "@/components/widgets/YufpPersonalData"
export default {
  components: { YufpPasswordModify, YufpPersonalData },
  mixins: [navbarMixin],

  data() {
    return {
      pwdDialogVisible: false,
      personInfoDialogVisible: false
    }
  },

  computed: {},

  beforeMount() {},

  methods: {
    /**
    * 修改密码
    */
    modifyPwdFn() {
      this.pwdDialogVisible = true; // 打开修改密码弹出框
    },
    logoutFn() {
      const _this = this;
      _this.$confirm(_this.$t('component.qdsftcdl'), _this.$t('component.ts'), {
        confirmButtonText: _this.$t('component.qd'),
        cancelButtonText: _this.$t('component.qx'),
        type: 'warning'
      }).then(function () {
        _this.logout();
      });
    },
    // 查看个人资料
    personalData() {
      this.personInfoDialogVisible = true;
    }
  }
};
</script>
<style>
.yu-frame-message-list .no-notice-data{
  text-align: center;
  font-size: 14px;
  position: absolute;
  top: 36%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%)
}
</style>
