import store from '@/store'
import backend from '@/config/constant/app.data.service'
import screenfull from 'screenfull'
import { MessageBox, Message } from 'yuwp-ui'
import { sessionStore } from 'uadp-utils'
import { logout } from '@/utils/oauth'
import { lang } from '@/config'
import { LANGUAGE } from '@/config/constant/app.data.common'
import { getI18nMessage } from "@/locale";
const getters = store && store.getters || {}

function searchToogle (event) {
  event.target.classList.add('search-show');
}
const frameConfig = {
  // 基础页签属性
  baseTabOptions: {
    // 页签位置显示，支持 top 和 bottom
    position: 'top',
    // 页签展示方式, part：首页/非首页分组，multi：默认分组，single：单页模式
    model: 'multi',
    // tab页签打开数量默认10, 0 为不限制
    maxOpenTabs: 10,
    // 是否开启页签右键刷新功能
    rightClickRefresh: true,
    // 重复打开是否刷新
    openDuplicateRefresh: false,
    // 双击页签刷新页面
    doubleClickRefresh: true,
    // 单页签模式时是否显示面包屑
    breadCrumb: false
  },
  // 基础菜单属性
  baseMenuOptions: {
    // 菜单的菜单项是否允许拖拽
    draggable: true,
    // 是否能展开多个子菜单
    unique: true,
    // 是否默认展开所有子菜单，true：默认展开，false：默认收缩
    defaultOpen: false,
    // 菜单左右时，是否显示非一级菜单图标
    showMenuIcon: false
  },
  // 基础框架属性
  baseFrameOptions: {
    moreSystem: true, // 是否配置多系统
    isFooter: false, // 是否显示底部条
    // 默认用户信息
    defaultUserInfo: {
      name: 'YUFP',
      roles: [
        { id: 'modelAdmin', code: 'modelAdmin', name: '模型管理员' },
        { id: 'cstManager', code: 'cstManager', name: '客户经理' }
      ],
      picUrl: require('@/assets/common/images/photo.svg')
    },
    // 主题下的菜单，只可以修改是否可见(show)和文本（text）
    themeTool: [
      { show: true, text: getI18nMessage('component.skin'), id: 'skin' },
      { show: true, text: getI18nMessage('component.layout'), id: 'model' },
      // { show: false, text: '字号', id: 'font' },
      { show: true, text: getI18nMessage('component.model'), id: 'sizeModel', extend: true} // extend标注是否显示自动滑块
    ],
    // 皮肤
    themesList: [
      // 默认是紫色,皮肤id需要和主题中皮肤目录相同对应
      { id: 'blue', color: '#2877FF', name: getI18nMessage('component.skinBlue') + '（' + getI18nMessage('component.mr') + '）', checked: true },
      { id: 'orange', color: '#FB8D12', name: getI18nMessage('component.skinOrange') },
      { id: 'purple', color: '#5557b9', name: getI18nMessage('component.skinPurple') }
    ],
    // 字号
    // fontSizeList: [
    //   { id: 'small', name: '较小' },
    //   { id: 'normal', name: '正常', checked: true },
    //   { id: 'large', name: '较大' }
    // ],
    // 紧凑模式
    sizeModel: [
      {id: 'normal', name: getI18nMessage('component.normal') },
      {id: 'compact', name: getI18nMessage('component.compact') }
    ],
    // 菜单模式(left/right/topTree/topLeft)
    modelList: [
      { id: 'left', name: getI18nMessage('component.left'), checked: true},
      { id: 'right', name: getI18nMessage('component.right') },
      { id: 'topTree', name: getI18nMessage('component.topTree')},
      { id: 'topLeft', name: getI18nMessage('component.topLeft') },
    ],
    // 语言
    languageList: [
      { id: 'zh_CN', name: getI18nMessage('component.lang_z') },
      // { id: 'zh_TW', name: '中文-繁體' },
      { id: 'en', name: getI18nMessage('component.lang_e') }
    ],
    searchPlaceholder: getI18nMessage('component.gjz'),
    messageTitle: {
      all: getI18nMessage('component.all'),
      todo: getI18nMessage('component.todo'),
      xx: getI18nMessage('component.message')
    },
    logoTitle: getI18nMessage('component.ptglxt'),
    // 搜索的类型
    searchType: [
      { id: 'cst', name: getI18nMessage('component.cst'), checked: true },
      { id: 'bus', name: getI18nMessage('component.bus') },
      { id: 'cpy', name: getI18nMessage('component.cpy') }
    ],
    // 应用切换列表
    appOptions: [
      // { applicationId: '1', applicationName: '应用管理框架'},
      { applicationId: '1', applicationName: getI18nMessage('component.ptglxt')},
      { applicationId: '2', applicationName: getI18nMessage('component.yysc')},
      { applicationId: '3', applicationName: getI18nMessage('component.zxyy')}
    ],
    // #TODO 语言翻译
    messageBtn: {
      handle: getI18nMessage('component.handle'), // 处理
      checkInfo: getI18nMessage('component.checkInfo'), // 查看
      cleanAll: getI18nMessage('component.cleanAll'), // 清空全部
      checkMore: getI18nMessage('component.checkMore'), // 查看更多
      currentSystem: getI18nMessage('component.currentSystem'), // 当前应用
      rightArrow: getI18nMessage('component.rightArrow'), // 向前
      leftArrow: getI18nMessage('component.leftArrow'), // 向后
      unfold: getI18nMessage('component.unfold'), // 展开
      menuShow1: getI18nMessage('component.menuShow1'), // 收起
      menuShow2: getI18nMessage('component.menuShow2'), // 展开
      auto: getI18nMessage('component.auto'), // 自动
      yes: getI18nMessage('component.yes'), // 是
      no: getI18nMessage('component.no'), // 否
      personalData: getI18nMessage('component.personalData'), // 个人资料
      changePassword: getI18nMessage('component.changePassword'), // 修改密码
      logout: getI18nMessage('component.logout') // 注销登录
    },
    // 直接显示搜索框
    showSearchInput: false,
    // 是否后台存储配置信息(菜单模式，字号，皮肤)
    saveInfo: {
      // 用户返回数据节点默认为返回的response,如：'data'
      jsonRoot: '',
      // 查询或保存时传入的附加参数如用户名
      baseParams: { userId: getters.userId },
      // 保存字段的映射关系
      configMapping: {
        // 菜单模式
        menuModel: 'menuModel',
        // 字号
        fontSize: 'fontSize',
        // 皮肤
        themes: 'themes'
      }
    },
    // 可以添加类似主题的工具项， 每个工具支持event属性，包括click(event)，mouseenter(event)，mouseout(event)事件
    sysTools: [
      { show: true,
        text: getI18nMessage('component.cx'),
        icon: 'yu-icon-search1',
        className: 'yu-frame-search-icon',
        id: 'search',
        event: {
          click: function ($event) {
            searchToogle($event);
          }
        }
      },
      { show: true, text: getI18nMessage('component.message'), icon: 'yu-icon-message', className: '', id: 'message', badgeDot: false },
      { show: true, text: getI18nMessage('component.themes'), icon: 'yu-icon-theme', className: '', id: 'themes' },
      { show: false, text: getI18nMessage('component.language'), icon: 'yu-icon-network', className: '', id: 'language' },
      { show: true,
        text: getI18nMessage('component.fullscreen'),
        icon: 'yu-icon-full-screen',
        className: '',
        id: 'fullscreen',
        event: {
          click: function (event) {
            if (!screenfull.enabled) {
              MessageBox({
                message: '浏览器不支持全屏',
                type: 'warning'
              });
              return false;
            }
            const className = event.target.getAttribute('class');
            event.target.setAttribute('class', screenfull.isFullscreen ? className.replace('-unfull-', '-full-') : className.replace('-full-', '-unfull-'));
            screenfull.toggle();
          }
        }
      },
      { show: true,
        text: getI18nMessage('component.pickup'),
        icon: 'yu-icon-arr-up',
        className: '',
        id: 'pickup',
        event: {
          click: function (item, menuModel) {
            // 隐藏工具栏
            const toolbar = document.getElementsByClassName('yu-frame-top-bar')[0];
            toolbar.setAttribute('class', 'yu-frame-top-bar yu-frame-top-bar-pickup');
            if (menuModel.id === 'left' || menuModel.id === 'right') {
              // yu-frame-tabs增加class 用于控制hover
              const tabs = document.getElementsByClassName('yu-frame-tabs')[0];
              tabs.setAttribute('class', 'yu-frame-tabs yu-frame-tabs-div');
              // 设tab高度，以调整内容区域高度
              const tab = document.getElementsByClassName('yu-frame-tab')[0];
              tab.setAttribute('class', 'yu-frame-tab yu-frame-tab-unfold');
            } else if (menuModel.id === 'topTile' || menuModel.id === 'topTree') {
              // 设置菜单高度样式
              const frameMenu = document.getElementsByClassName('yu-frame-menu')[0];
              frameMenu.setAttribute('class', 'yu-frame-menu yu-frame-menu-pickup');
              const tabTop = document.getElementsByClassName('yu-frame-tab')[0];
              tabTop.setAttribute('class', 'yu-frame-tab yu-frame-tab-unfold');
            } else if (menuModel.id === 'topLeft') {
              //const frameMenu = document.getElementsByClassName('yu-frame-menu')[0];
              //frameMenu.setAttribute('class', 'yu-frame-menu yu-frame-menu-pickup');
              //const tabTop = document.getElementsByClassName('yu-frame-tab')[0];
              //tabTop.setAttribute('class', 'yu-frame-tab yu-frame-tab-unfold');
            }
            // 触发pickup事件处理页面吸顶操作
            // globalVm.$emit('pickup', true);
          }
        }
      },
      { show: true,
        text: getI18nMessage('component.logOut'),
        icon: 'yu-icon-shutdown',
        className: '',
        id: 'logOut',
        event: {
          click: async function () {
            await logout()
          }
        }
      }
    ],
    // 例如：example/log/menu 。默认为空时不发送日志请求
    viewMenuLogUrl: '',
    userMessages: [
      /* { type: 0, from: getI18nMessage('component.ll'), msg: getI18nMessage('component.msg1'), dateTime: getI18nMessage('component.dateTime1'), state: getI18nMessage('component.state') },
      { type: 1, from: getI18nMessage('component.lyz'), msg: getI18nMessage('component.msg2'), dateTime: getI18nMessage('component.dateTime2'), state: undefined },
      { type: 0, from: getI18nMessage('component.ckf'), msg: getI18nMessage('component.msg3'), dateTime: getI18nMessage('component.dateTime3'), state: getI18nMessage('component.state') },
      { type: 1, from: getI18nMessage('component.lw'), msg: getI18nMessage('component.msg4'), dateTime: getI18nMessage('component.dateTime4'), state: undefined },
      { type: 0, from: getI18nMessage('component.wcy'), msg: getI18nMessage('component.msg5'), dateTime: getI18nMessage('component.dateTime5'), state: getI18nMessage('component.state') } */
    ]
  },
  // 首页框架对象
  baseFrame: null,
  // 菜单初始化完成前调用  返回 false 表示不执行菜单初始化，否则初始化菜单结构
  beforeInit: function () {
  },
  // 鼠标移到页签上的事件后触发
  tabMouseEnter: function (tab, event) {
  },
  // tab点击事件后触发
  tabClick: function (tab, event) {
  },
  // 角色切换 role 角色对象
  switchRole: function (role) {
  },
  // 搜索事件.type 搜索类型对象，value 查询的条件
  searchBarClick: function (type, value) {
  },
  // 搜索过滤返回的数据集. type 搜索类型对象， value 过滤方法返回值为包括label，value 对象的数组
  searchDataFilter: function (type, value) {
    // return [{label: '测试', value: 'test'}];
  },
  // 过滤后数据项的点击事件. type 搜索类型对象，item过滤后选中的数据项
  searchItemClick: function (type, item) {
  },
  // 菜单点击后回调
  menuItemClick: function (menuId) {
  },
  // 注销登录
  logOut: async function () {
    await store.dispatch('oauth/logout')
  },
  /**
   * 登出后重新登录返回路径,为空即返回首页
   */
  redirect: '',
  // 搜索框显示切换
  searchToogle: searchToogle
}
// 处理保存默认选择语言
// 改变标题
const chooseLanguage = sessionStore.get(LANGUAGE)
if (!chooseLanguage) {
  frameConfig.baseFrameOptions.languageList.filter(item => {
    if(item.id === lang) {
      sessionStore.set(LANGUAGE, item.id)
    }
  })
}
export default frameConfig
