<template>
  <div class="navbar yu-frame-top-bar yu-frame-top-left-bar">
    <div ref="leftContent" class="left-content">
      <span class="yu-logo-mini"></span>
      <div class="yu-app-box-top-button-wrap">
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
    <div class="right-content clear">
      <div class="yu-frame-top-bar-right" :style="{width : topBarRightWidth + 'px'}">
        <div class="sidebar-box" :style="{width : topBarRightWidth - 48 + 'px'}">
          <topMenu ref="topMenu" class="top-menu" />
        </div>
        <span class="yu-icon-more1"></span>
        <div class="sys-tools-box">
          <template v-for="(item,index) in sysToolsFiltered">
            <span
              v-if="item.id !== 'pickup'"
              class="sys-tools"
              :key="index"
              :title="(item.event && item.event.click)?item.text:''"
              :class="[item.icon, item.className, { 'serach-show' : (showSearchInput && item.id==='search'), click: (item.event && item.event.click) } ]"
              @click="toolClick(item,$event)"
              @mouseenter="toolMouseEnter(item,$event)"
              @mouseout="toolMouseOut(item,$event)">
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
                <yu-input placeholder="关键字" v-model="searchValue" @input="searchDataFilter"></yu-input>
                <i class="yu-frame-search-button" :class="item.icon" @click="searchBarClick"></i>
              </div>
              <!--search end-->
              <!--message sta-->
              <div v-if="item.id==='message'" class="yu-frame-dropdown-menu yu-frame-message" title>
                <yu-tabs value="all">
                  <yu-tab-pane label="全部" name="all">
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
                              <template v-if="it.type===0">处理</template>
                              <template v-else>查看</template>
                            </a>
                          </span>
                        </p>
                      </li>
                    </ul>
                    <div class="yu-frame-message-buttons">
                      <yu-button type="text">清空全部</yu-button>
                      <yu-button type="text">查看更多</yu-button>
                    </div>
                  </yu-tab-pane>
                  <yu-tab-pane label="待办" name="to">
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
                              <template v-if="it.type===0">处理</template>
                              <template v-else>查看</template>
                            </a>
                          </span>
                        </p>
                      </li>
                    </ul>
                    <div class="yu-frame-message-buttons">
                      <yu-button type="text">清空全部</yu-button>
                      <yu-button type="text">查看更多</yu-button>
                    </div>
                  </yu-tab-pane>
                  <yu-tab-pane label="消息" name="message">
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
                              <template v-if="it.type===0">处理</template>
                              <template v-else>查看</template>
                            </a>
                          </span>
                        </p>
                      </li>
                    </ul>
                    <div class="yu-frame-message-buttons">
                      <yu-button type="text">清空全部</yu-button>
                      <yu-button type="text">查看更多</yu-button>
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
              <div v-if="item.id=== 'language'&&languageList.length>1" class="yu-frame-dropdown-menu" title>
                <span
                  v-for="(it,idx) in languageList"
                  :key="idx"
                  @click="switchLanguage(it.id)"
                  :class="[it.checked?'yu-icon-checked2':'yu-icon-choice-un']"
                  :title="it.name"
                >{{ it.name }}</span>
              </div>
            </span>
          </template>
        </div>
      </div>
      <div ref="topUserBox" class="yu-frame-top-user">
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
          <span @click="personalData">{{ $t('component.personalData') }}</span>
          <span @click="modifyPwdFn">{{ $t('component.changePassword') }}</span>
          <span @click="logoutFn">{{ $t('component.logout') }}</span>
        </div>
      </div>
      <yufp-password-modify :dialog-visible.sync="pwdDialogVisible" :first-login="false">
      </yufp-password-modify>
      <yufp-personal-data :dialog-visible.sync="personInfoDialogVisible">
      </yufp-personal-data>
    </div>
  </div>
</template>

<script>
import topMenu from '../TopLeft/topMenu.vue';
import navbarMixin from './navbar.mixin';
import YufpPasswordModify from "@/components/widgets/YufpPasswordModify"
import YufpPersonalData from "@/components/widgets/YufpPersonalData"
export default {
  components: { topMenu, YufpPasswordModify, YufpPersonalData },

  mixins: [navbarMixin],

  data() {
    return {
      topBarRightWidth: 880,
      pwdDialogVisible: false,
      personInfoDialogVisible: false
    }
  },

  computed: {},

  mounted() {
    this.getTopBarRightWidth();
    window.onresize = () => {
      this.getTopBarRightWidth();
    };
  },

  destroyed: function() {
    window.onresize = null;
  },

  methods: {
    getTopBarRightWidth() {
      this.$nextTick(() => {
        const bodyWidth = document.body.clientWidth;
        this.topBarRightWidth = bodyWidth - this.$refs.leftContent.offsetWidth - this.$refs.topUserBox.offsetWidth - 24 - 10;
        this.$refs.topMenu.getMenuBoxWidth();
      })
    },
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
<style lang="scss" scoped>
  .yu-frame-top-bar.yu-frame-top-left-bar {
    .yu-frame-top-user {
      .name-role {
        color: rgba(255, 255, 255, 0.7);
        &>b {
          color: #fff;
        }
      }
      i, &:hover i {
        color: rgba(255, 255, 255, 0.5);
      }
    }
  }
</style>