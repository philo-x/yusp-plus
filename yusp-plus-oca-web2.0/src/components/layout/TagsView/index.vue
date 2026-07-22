<template>
  <div class="yu-frame-tab app-main">
    <div class="yu-frame-tabs" id="yu-frame-tabs" ref="idxTabs" v-if="groupModel" v-show="!partFlag">
      <div class="yu-tab-content">
        <div>
          <router-link v-for="tag in visitedViews" :key="tag.path" :ref="'tag_'+tag.meta.id" :class="isActive(tag)?'active ck':''" :to="{ name: tag.name, params:tag.params, path: tag.path, query: tag.query, fullPath: tag.fullPath }" tag="span" @click.native="clickTag(tag)" @click.middle.native="closeSelectedTag(tag)" @dblclick.prevent.native="dblclickReloadFn(tag)" @contextmenu.prevent.native="openMenu(tag,$event)">
            {{ generateTitle(tag.title) }}
            <i v-if="!tag.meta.affix" class="yu-icon-close1" @click.prevent.stop="closeSelectedTag(tag)"></i>
          </router-link>
        </div>
        <span class="yu-tab-drop-down-menu-button" v-if="showTabDMenuBtn" href="javascript:void(0)" style="display: block;" @mouseenter="tabDMenuBtMouseEnter($event)">
          <i class="yu-icon-more1"></i>
          <div ref="idxTabDMenuBt">
            <span v-if="homePageTab.title" :data-key="homePageTab.menuId" @click="handleClick(homePageTab,$event)">{{ generateTitle(homePageTab.title) }}</span>
            <span :title="$t('component.btn_close_all')" @click="handleCloseAllTabs()">{{ $t('component.btn_close_all') }}</span>
            <hr class="split-hr" />
            <span v-for="(item, index) in notIndexTabs" :key="index" :title="item.meta.title" :data-key="item.name" @click="handleClick(item,$event)">{{ item.meta.title }}
              <i :title="$t('component.btn_close')" @click.prevent.stop="closeSelectedTag(item)" class="yu-icon-close1"></i>
            </span>
          </div>
        </span>
      </div>
      <span class="yu-frame-tabs-unfold" @click="unfoldClickFn()" :title="$t('component.expand')"><i class="yu-frame-tabs-unfold-icon yu-icon-down"></i></span>
    </div>
    <div class="yu-frame-tab-box" ref="idxTabBox">
      <div v-for="item in data" :key="item.id" v-show="item.show" :class="{'ck':item.checked}" :data-key="item.menuId" :id="'tabBox_'+item.menuId">
        <iframe v-if="item.frame" :src="item.url" :style="commStyle" border="0"></iframe>
      </div>
      <app-main />
    </div>
    <div :style="{left:left+'px',top:top+'px','display': visible?'block':'none'}" class="yu-tab-contextmenu contextmenu" ref="tabContextMenu" @mouseleave="handleMouseLeave">
      <span @click="handleRefreshTab" v-if="rightClickRefresh">{{ $t('component.btn_refresh') }}</span>
      <span @click="handleCloseCurrentTab">{{ $t('component.btn_close_current') }}</span>
      <span @click="handleCloseOtherTabs">{{ $t('component.btn_close_others') }}</span>
      <span @click="handleCloseAllTabs">{{ $t('component.btn_close_all') }}</span>
      <!-- <span @click="handleGetPageProperty">页面属性</span> -->
    </div>
  </div>
</template>

<script>
/* eslint vars-on-top:0 */
/* eslint no-inner-declarations:0 */
/* eslint no-lonely-if:0 */
/* eslint no-loop-func:0 */

// import ScrollPane from './ScrollPane'
import AppMain from "@/components/layout/AppMain";
import { generateTitle } from "@/utils/i18n";
import path from "path";
import { sessionStore, clone } from '@/utils'
import { mapGetters } from "vuex";

export default {
  components: { AppMain },
  props: {
    // 显示位置
    position: {
      default: "top",
      type: String
    },
    // 显示模式 页签展示方式 part multi 分组,单页签模式 single
    model: {
      default: "multi",
      type: String
    },
    // 最大tab 打开个数
    maxOpenTabs: {
      default: 10,
      type: Number
    },
    // 重复打开是否刷新，默认false
    openDuplicateRefresh: {
      type: Boolean
    },
    // 是否开启页签右键刷新功能，默认true
    rightClickRefresh: {
      default: true,
      type: Boolean
    },
    // 是否开启页签双击刷新功能，默认true
    doubleClickRefresh: {
      default: true,
      type: Boolean
    }
  },
  data() {
    return {
      visible: false,
      top: 0,
      left: 0,
      selectedTag: {},
      tabPosition: "bottom",
      affixTags: [],
      data: [],
      // 激活的tab
      activedTab: {},
      // 首页对象
      homePage: {},
      // 获取临时tab（右键时）
      tmpTab: "",
      // 右键的点击的标志
      rightClickFlag: false,
      // 单组模式时 首页时标志
      partFlag: true,
      // 页签下拉菜单所占空间宽度
      tabDropdownMenuWidth: 180, // #TODO 此处需要根据是否紧凑模式调整值
      // 页签按钮内外边距宽
      tabButtonOutWidth: 34, // #TODO 此处需要根据是否紧凑模式调整值
      // 记录tab 右键菜单高度的临时变量
      tmpContextMenuHeight: -1,
      // 菜单的层级关系
      menuPath: [],
      // 页面区域高度
      clientHeight: "",
      // 单页模式时的一个计数器
      singleCount: 0,
      // tab页签上的功能块的高度
      oprBarHieght: [60, 38], // #TODO 此处需要根据是否紧凑模式调整值
      // 是否显示页签右侧的下拉
      showTabDMenuBt: false,
      // iframe 时的样式
      commStyle: {
        overflow: "auto hidden",
        width: "100%",
        height: "calc(100% - 10px)",
        border: 0
      }
    };
  },
  computed: {
    ...mapGetters(['routes', 'currentTopMenu']),
    menusData() {
      return this.routes.filter(item => {
        return !item.hidden;
      })
    },
    menuModel() {
      return this.$store.state.app.menuModel;
    },
    visitedViews() {
      return this.$store.state.tagsView.visitedViews;
    },
    showTabDMenuBtn() {
      let showtab = false;
      const visitedViews = this.$store.state.tagsView.visitedViews;
      for (let i = 0, len = visitedViews.length; i < len; i++) {
        if (!visitedViews[i].meta.affix) {
          showtab = true;
          break;
        }
      }
      return showtab;
    },
    showCloseTabItem() {
      return this.$store.state.tagsView.visitedViews.filter(item => {
        return !item.meta.affix;
      });
    },
    routes() {
      return this.$store.state.oauth.routes;
    },
    cachedViews() {
      const views = this.$store.state.tagsView.cachedViews.filter(
        view => !!view && !!view.name && view.name === this.$route.name
      );
      const cvs = [];
      views.forEach(element => {
        cvs.push(element.name)
      });
      return cvs.length === 0 ? cvs : undefined;
    },
    key() {
      return this.$route.fullPath;
    },
    notIndexTabs() {
      return this.$store.state.tagsView.cachedViews;
    },
    homePageTab() {
      const affixTab = this.$store.state.tagsView.visitedViews.filter(item => {
        return item.meta && item.meta.affix
      });
      return affixTab && affixTab.length > 0 && affixTab[0].meta
    },
    /**
     * 计算tab single或part 模式 就显示
     */
    singleModelShow: function() {
      if (this.model === "single" || this.model === "part") {
        return true;
      } else {
        return false;
      }
    },
    /**
     * 不是single 模式就显示
     */
    groupModel: function() {
      if (this.model !== "single") {
        return true;
      } else {
        return false;
      }
    }
  },
  watch: {
    $route() {
      // vue-router如果提供了 path，params 会被忽略，故从path，获取query参数
      var params = this.$route && this.$route.params
      var query = this.$route && this.$route.query
      if(params._routeType === 'replace' || query._routeType === 'replace') {
        // 替换路由,更改当前标题及路径
      } else {
        // 当路由参数为push时，不添加tab页面，直接替换当前页面，可支持路由回退
        const isHideTag = params._routeType === 'push' || query._routeType === 'push';
        if(isHideTag) {
          return;
        }
        this.addTags(isHideTag);
      }
    },
    visible(value) {
      if (value) {
        document.body.addEventListener("click", this.closeMenu);
      } else {
        document.body.removeEventListener("click", this.closeMenu);
      }
    },
    /**
     * 监听tab页签的变化
     */
    data: function(val) {
      if (this.model === "multi") {
        if (val.length > 1) {
          this.showTabDMenuBt = true;
        } else {
          this.showTabDMenuBt = false;
        }
      } else if (this.model === "part") {
        this.showTabDMenuBt = true;
      }
    }
  },
  created: function() {
    var _this = this;
    window.onresize = function() {
      // 如果tab对象不存在不进行checkTabs 处理
      if (!_this.$refs || !_this.$refs.idxTabs) {
        return;
      }
      _this.checkTabs();
      _this.getContentSize()
    };
  },
  mounted() {
    // 只支持 bottom和其他变量
    if (this.position === "bottom") {
      this.tabPosition = this.position;
    } else {
      this.tabPosition = "top";
    }
    // 分组模式时，单组标志位false
    if (this.model !== "part") {
      this.partFlag = false;
    }
    this.initTags();
    this.addTags();
    // 计算区域大小
    this.getContentSize()
  },
  destory: function() {
    window.onresize = null;
  },
  methods: {
    generateTitle, // generateTitle by vue-i18n
    dblclickReloadFn(view) {
      this.refreshSelectedTag(view);
    },
    isActive(route) {
      return route.path === this.$route.path;
    },
    filterAffixTags(routes, basePath = "/") {
      let tags = [];
      routes.forEach(route => {
        if (route.meta && route.meta.affix) {
          const tagPath = path.resolve(basePath, route.path);
          tags.push({
            fullPath: tagPath,
            path: tagPath,
            name: route.name,
            meta: { ...route.meta }
          });
        }
        if (route.children) {
          const tempTags = this.filterAffixTags(route.children, route.path);
          if (tempTags.length >= 1) {
            tags = [...tags, ...tempTags];
          }
        }
      });
      return tags;
    },
    initTags() {
      const affixTags = (this.affixTags = this.filterAffixTags(this.routes));
      for (const tag of affixTags) {
        // Must have tag name
        if (tag.name) {
          this.$store.dispatch("tagsView/addVisitedView", tag);
        }
      }
    },
    addTags(isHideTag) {
      const { name } = this.$route;
      if (name) {
        this.$store.dispatch("tagsView/addView", this.$route);
        this.$nextTick(function() {
          const view = this.visitedViews[this.visitedViews.length - 1];
          view.width = this.$refs["tag_" + view.meta.id][0]
            ? this.$refs["tag_" + view.meta.id][0].$el.offsetWidth
            : this.$refs["tag_undefined"][0].$el.offsetWidth;
          view.hideTag = isHideTag;
          console.log('view++++++', view);
          this.$store.dispatch("tagsView/updateVisitedView", view);
          this.checkTabs();
        })
      }
      return false;
    },
    refreshSelectedTag(view) {
      this.$store.dispatch("tagsView/delCachedView", view).then(() => {
        const { fullPath } = view;
        this.$nextTick(() => {
          this.$router.replace({
            path: "/redirect" + fullPath,
            query: {
              _routeType: 'replace',
              _routeRedirectName: 'sysMiddleRedirectRoute' // 用于标识是否为刷新跳转中间路由
            }
          });
        });
      });
    },
    clickTag(tag) {
      this.menusData.forEach(menu => {
        menu.children.forEach(item => {
          if (item.path === tag.path) {
            this.$store.commit('oauth/SET_CURRENT_TOP_MENU', menu);
          } else if (item.children && item.children.length > 0) {
            item.children.forEach(child => {
              if (child.path === tag.path) {
                this.$store.commit('oauth/SET_CURRENT_TOP_MENU', menu);
              }
            })
          }
        })
      })
      if (this.currentTopMenu && this.currentTopMenu.path === '/') {
        this.$store.commit('oauth/SET_SHOW_LEFT_MENU', false);
      }
    },
    closeSelectedTag(view) {
      this.$store
        .dispatch("tagsView/delView", view)
        .then(({ visitedViews }) => {
          if (this.isActive(view)) {
            this.toLastView(visitedViews, view);
          }
        });
    },
    closeOthersTags() {
      this.$router.push(this.selectedTag);
      this.$store.dispatch("tagsView/delOthersViews", this.selectedTag);
    },
    closeAllTags(view) {
      this.$store.dispatch("tagsView/delAllViews").then(({ visitedViews }) => {
        if (this.affixTags.some(tag => view && tag.path === view.path)) {
          return;
        }
        this.toLastView(visitedViews, view);
      });
    },
    toLastView(visitedViews, view) {
      const latestView = visitedViews.slice(-1)[0];
      if (latestView) {
        this.$router.push(latestView);
      } else if (view.name === "Dashboard") {
        // now the default is to redirect to the home page if there is no tags-view,
        // you can adjust it according to your needs.
        // to reload home page
        this.$router.replace({ path: "/redirect" + view.fullPath });
      } else {
        this.$router.push("/");
      }
    },
    openMenu(tag, e) {
      this.tmpTab = tag;
      if(tag && tag.meta && tag.meta.affix) {
        return;
      }
      const menuMinWidth = 105;
      const offsetLeft = this.$el.getBoundingClientRect().left; // container margin left
      const offsetWidth = this.$el.offsetWidth; // container width
      const maxLeft = offsetWidth - menuMinWidth; // left boundary
      const left = e.clientX - offsetLeft; // 15: margin right

      if (left > maxLeft) {
        this.left = maxLeft;
      } else {
        this.left = left;
      }

      if(this.menuModel.id === 'topTree') { // 竖向菜单模式
        this.top = e.clientY - 103;
      } else {
        this.top = e.clientY - 63;
      }
      // 紧凑模式加上17
      if(sessionStore.get('currentSizeModeId') === 'compact') {
        this.top += 17;
      }
      this.visible = true;
      this.selectedTag = tag;
    },
    closeMenu() {
      this.visible = false;
    },
    /**
     * 折叠事件
     */
    unfoldClickFn: function() {
      // 显示折叠按钮
      const toolbar = document.getElementsByClassName("yu-frame-top-bar")[0];
      toolbar.setAttribute("class", "yu-frame-top-bar ");
      // 设置高度
      const tab = document.getElementsByClassName("yu-frame-tab")[0];
      tab.setAttribute("class", "yu-frame-tab");
      // 恢复div初始class
      const tabs = document.getElementsByClassName("yu-frame-tabs")[0];
      tabs.setAttribute("class", "yu-frame-tabs");
      // 触发pickup事件处理页面吸顶操作
      this.$emit("pickup", false);
    },
    /**
     * 刷新表格
     * @param tab 页签
     */
    refreshTab: function(tab) {
      // 获取到数据然后才跳转
      if (tab.routeId) {
        this.$nextTick(function() {
          if (!tab.frame) {
            this.$router.push(tab.routeId, tab.data, "tabBox_" + tab.menuId); // 路由跳转
          } else {
            var url = tab.url;
            tab.url = "";
            this.$nextTick(function() {
              tab.url = url;
            });
          }
        });
      }
    },

    /**
     * 激活页签
     * @param tabRoute 菜单路由信息对象
     */
    activeTab: function(tabRoute) {
      for (var i = 0, length = this.notIndexTabs.length; i < length; i++) {
        var tmp = this.notIndexTabs[i];
        if (tmp.name === tabRoute.name) {
          // 存储当前激活的tab
          this.activedTab = tmp;
          this.$router.replace(tmp)
          this.$store.dispatch("tagsView/addVisitedView", tmp);
          this.$set(tmp, "checked", true);
          this.$emit("active-tab", tmp);
          // 渲染完成后再计算宽度
          this.$nextTick(function() {
            this.checkTabs();
          });
        } else {
          this.$set(tmp, "checked", false);
        }
      }
    },
    /**
     *  获取页签参数
     * @param id  页签标识，无id时，返回当前激活的tab id
     */
    getTab: function(id) {
      var _data = this.data;
      id = id || this.activedTab.id;
      for (var i = 0, length = _data.length; i < length; i++) {
        if (_data[i].id === id) {
          return _data[i];
        }
      }
    },
    /**
     * 获取时间
     */
    getTimestamp: function() {
      return new Date().valueOf();
    },
    /**
     * tab 的点击事件
     * @param tab 对象
     * @param event 事件对象
     */
    handleClick: function(tab, event) {
      this.activeTab(tab);
      // 对外暴露 tab 点击事件
      this.$emit("tab-click", tab, event);
    },
    /**
     * 双击刷新
     * @param tab 对象
     * @param event 事件对象
     */
    handleDblClick: function(tab, event) {
      if (this.doubleClickRefresh) {
        this.refreshTab(tab);
      }
    },
    /**
     * 右击事件
     * @param tab tab对象
     * @param event 事件对象
     */
    handleContextMenu: function(tab, event) {
      this.tmpTab = tab;
      this.preventDefault(event);
      // 首页没有右键功能
      if (tab.id === this.homePage.id) {
        return;
      }
      // 获取我们自定义的右键菜单
      var menu = this.$refs.tabContextMenu;
      // 改变自定义菜单的宽，让它显示出来
      menu.style.display = "block";
      menu.style.position = "fixed";
      // 根据事件对象中鼠标点击的位置，进行定位
      menu.style.left = event.clientX + "px";
      if (this.tabPosition === "bottom") {
        // tmpContextMenuHeight 为-1 表示没有计算过右键菜单高度，否则计算过，后面直接用值
        if (this.tmpContextMenuHeight === -1) {
          this.$nextTick(function() {
            this.tmpContextMenuHeight = menu.clientHeight;
            var tmp = event.clientY - this.tmpContextMenuHeight + "px";
            menu.style.top = tmp;
          });
        } else {
          // 计算过，后面直接用值
          menu.style.top = event.clientY - this.tmpContextMenuHeight + "px";
        }
      } else {
        menu.style.top = event.clientY + "px";
      }
      this.rightClickFlag = true;
    },
    /**
     * 刷新事件
     */
    handleRefreshTab: function() {
      this.refreshSelectedTag(this.selectedTag);
    },
    /**
     * 关闭当前页签
     */
    handleCloseCurrentTab: function() {
      this.closeSelectedTag(this.selectedTag)
      this.sessionChange(this.selectedTag)
    },
    /**
     * 关闭其他页签
     */
    handleCloseOtherTabs: function() {
      this.closeOthersTags(this.tmpTab)
      this.sessionChange(this.tmpTab, "other")
      this.setDisplyNone();
    },
    /**
     * 关闭全部页签
     */
    handleCloseAllTabs: function() {
      this.closeAllTags();
      this.sessionChange(this.tmpTab, "all")
    },
    /*同步sessionStorage */ 
    sessionChange(view, flag) {
      if(window.sessionStorage.getItem("dynamicRoutes")) {
        const dynamicRoutes = JSON.parse(window.sessionStorage.getItem("dynamicRoutes").slice(2)).dRoute; 
        const newdynamicRoutes = {} ;
        for(let i = 0;i < dynamicRoutes.length;i++) {
          if("/" + dynamicRoutes[i].children[0].commentsRouter === view.path) {
            for(var key in dynamicRoutes[i].children[0].meta) {
              if(dynamicRoutes[i].children[0].meta[key] === view.params[key]) {
                if(flag === "other") {
                  newdynamicRoutes.dRoute = clone(dynamicRoutes[i])
                }else if(flag === "all") {
                  newdynamicRoutes.dRoute = [];
                }else {
                  dynamicRoutes.splice(i, 1)
                  newdynamicRoutes.dRoute = clone(dynamicRoutes)
                }
                window.sessionStorage.setItem("dynamicRoutes", JSON.stringify(newdynamicRoutes))
              }
            }
          }  
        }
      }
    },
    /**
     * 获取tab页属性
     */
    // handleGetPageProperty: function() {
    //   var tab = this.getTab(this.tmpTab.id);
    //   var router = yufp.router.getRoute(tab.routeId);
    //   var h = this.$createElement;
    //   var s1 = {
    //     style:
    //       "display:block;clear:left;padding: 3px 0;font-size:14px;position: relative;height: auto;"
    //   };
    //   var s2 = {
    //     style:
    //       "float:left;text-align:right;width:100px;left:0;top:3px;position: absolute;"
    //   };
    //   var s3 = {
    //     style:
    //       "float:left;display:block;margin-left:100px;word-break: break-all;"
    //   };
    //   var _this = this;
    //   this.$msgbox({
    //     title: "页面属性",
    //     confirmButtonText: "关闭",
    //     callback: function() {},
    //     message: h("div", [
    //       h("p", s1, [h("label", s2, "tab title："), h("span", s3, tab.title)]),
    //       h("p", s1, [h("label", s2, "tab key："), h("span", s3, tab.menuId)]),
    //       h("p", s1, [h("label", s2, "tab id："), h("span", s3, tab.routeId)]),
    //       h("p", s1, [
    //         h("label", s2, "router html："),
    //         h("span", { style: s3.style }, [
    //           router.html,
    //           h(
    //             "el-button",
    //             {
    //               props: { type: "primary", size: "mini" },
    //               attrs: { id: "htmlBtn" },
    //               on: {
    //                 click: function() {}
    //               }
    //             },
    //             "复制"
    //           )
    //         ])
    //       ]),
    //       h("p", s1, [h("label", s2, "router js："), h("span", s3, router.js)])
    //     ])
    //   });
    //   setTimeout(function() {
    //     yufp.util.setClipBoardData(
    //       "#htmlBtn",
    //       router.html,
    //       function() {
    //         _this.$message({
    //           type: "info",
    //           message: "复制成功！"
    //         });
    //       },
    //       function() {
    //         _this.$message({
    //           type: "error",
    //           message: "当前浏览器不支持此功能，请手动复制。"
    //         });
    //       }
    //     );
    //   }, 500);

    //   this.setDisplyNone();
    // },
    /**
     * 鼠标离开事件
     * @param tab tab对象
     * @param event 事件对象
     */
    handleMouseLeave: function(tab, event) {
      // mouseleave事件，右键也会触发（未查明），所以添加标志处理
      if (!this.rightClickFlag) {
        this.$refs.tabContextMenu.style.display = "none";
      } else {
        this.rightClickFlag = false;
      }
      this.visible = false;
    },
    /**
     * 取消默认事件
     */
    preventDefault: function(e) {
      var event = window.event || event || e;
      if (document.all) {
        // 支持IE
        event.returnValue = false;
      } else {
        // IE不支持
        event.preventDefault();
      }
    },
    /**
     * 设置右键菜单的样式
     */
    setDisplyNone: function() {
      this.$refs.tabContextMenu.style.display = "none";
    },
    /**
     * 鼠标移到页签上的事件
     * @param tab tab对象
     * @param event 事件对象
     */
    mouseEnter: function(tab, event) {
      this.$emit("mouse-enter", tab, event);
    },
    /**
     * 菜单右侧下拉框点击事件
     */
    tabDMenuBtMouseEnter: function() {
      if (this.tabPosition === "bottom") {
        var menuBt = this.$refs.idxTabDMenuBt;
        menuBt.style.top = -menuBt.clientHeight + "px";
      }
    },
    /**
     * 页签按钮显示适配
     */
    checkTabs: function() {
      var _this = this;
      // 单页签模式时不用计算页签适配
      if (this.model === "single") {
        return;
      }
      // 获取当前tab按钮容器宽度
      var _getSW = function() {
        return _this.$refs.idxTabs.children[0].offsetWidth;
      };
      // 获取tab按钮可显示的最大空间宽度
      var _getMaxW = function() {
        // tabs条两边的边距
        return (
          _this.$refs.idxTabs.offsetWidth - _this.tabDropdownMenuWidth - 5
        ); // #TODO 此处需要根据是否紧凑模式调整值
      };
      var maxw = _getMaxW();
      var sw = _getSW();
      if (sw > maxw && maxw > 0) {
        this.calcEnlarge(sw, maxw);
      } else {
        this.calReduce(sw, maxw);
      }
    },
    /**
     * tab 页签增加或者已经有页签隐藏然后被激活后
     * 递归计算当前页签width是否大于最大宽度
     * @param sw tab页签的总宽度
     * @param maxwidth tab按钮可显示的最大空间宽度
     */
    calcEnlarge: function(sw, maxwidth) {
      for (var i = 0, element; i < this.visitedViews.length; i++) {
        element = this.visitedViews[i];
        // show 为true 且id 不为当前激活的页签（如果不加id判断，可能导致第一个激活的一直隐藏）,且非首页
        if (!element.meta.affix && this.$route.path !== element.path) {
          this.$store.dispatch("tagsView/delVisitedView", element);
          sw = sw - element.width;
          break;
        }
      }
    },
    /**
     * tab页签较少时调用
     * 递归计算
     * @param sw tab页签的总宽度
     * @param maxwidth tab按钮可显示的最大空间宽度
     */
    calReduce: function(sw, maxwidth) {
      for (var i = 0, element; i < this.visitedViews.length; i++) {
        element = this.visitedViews[i];
        if (!element.show) {
          sw = sw + element.width;
          if (sw <= maxwidth) {
            element.show = true;
          }
          break;
        }
      }
    },
    /**
     * 获取页面内容的size
     */
    getContentSize: function() {
      // tab 页时高度计算
      var content = {};
      if (this.model !== "single" && this.visitedViews.length !== 0) {
        // 计算高度 页面高度
        content = this.$refs.idxTabBox;
      } else {
        // 单页签时的高度计算
        content = this.$refs.singleTab;
      }
      var size = {
        height: content.clientHeight,
        width: content.clientWidth
      }
      // 页面高度,宽度
      return size;
    }
  }
};
</script>
