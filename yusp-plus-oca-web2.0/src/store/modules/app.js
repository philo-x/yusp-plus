/* eslint no-implicit-coercion:0 */

import { sessionStore } from '@/utils'
import { getLanguage } from '@/utils/i18n'
import { getDefaultTheme, getDefaultMenuModel } from '@/utils/util'
import { LANGUAGE, VIEW_SIZE } from '@/config/constant/app.data.common'
import { sysLogicName } from '@/config'

const state = {
  sidebar: {
    opened: sessionStore.get('sidebarStatus') ? !!+sessionStore.get('sidebarStatus') : true,
    withoutAnimation: false
  },
  device: 'desktop',
  language: getLanguage(),
  theme: sessionStore.get('theme') || getDefaultTheme(),
  menuModel: sessionStore.get('menumodel') || getDefaultMenuModel(),
  menuShowStat: sessionStore.get('menushowstat') || 1,
  menuSize: sessionStore.get('menusize') || {
    // 菜单宽度
    menuWidth: '',
    // 菜单高度
    menuHeight: ''
  },
  defaultActive: '',
  currentSizeModeId: sessionStore.get('currentSizeModeId') || 'normal', // 紧凑模式
  size: sessionStore.get('size') || 'medium',
  viewSize: null,
  title: sessionStore.get('title') || sysLogicName,
}

const mutations = {
  TOGGLE_SIDEBAR: state => {
    state.sidebar.opened = !state.sidebar.opened
    state.sidebar.withoutAnimation = false
    if (state.sidebar.opened) {
      sessionStore.set('sidebarStatus', 1)
    } else {
      sessionStore.set('sidebarStatus', 0)
    }
  },
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    sessionStore.set('sidebarStatus', 0)
    state.sidebar.opened = false
    state.sidebar.withoutAnimation = withoutAnimation
  },
  OPEN_SIDEBAR: (state, withoutAnimation) => {
    sessionStore.set('sidebarStatus', 1)
    state.sidebar.opened = true
    state.sidebar.withoutAnimation = withoutAnimation
  },
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
  },
  SET_LANGUAGE: (state, language) => {
    state.language = language
    sessionStore.set(LANGUAGE, language)
  },
  SET_THEME: (state, theme) => {
    state.theme = theme
    sessionStore.set('theme', theme)
  },
  SET_MENU_MODEL: (state, menuModel) => {
    state.menuModel = menuModel
    sessionStore.set('menumodel', menuModel)
  },
  SET_MENU_SHOW_STAT: (state, menuShowStat) => {
    state.menuShowStat = menuShowStat
  },
  SET_MENU_SIZE: (state, menuSize) => {
    state.menuSize = menuSize
  },
  SET_DEFAULT_ACTIVE: (state, defaultActive) => {
    state.defaultActive = defaultActive
    sessionStore.set('defaultactive', defaultActive)
  },
  SET_SIZE: (state, size) => {
    state.size = size
    sessionStore.set('size', size)
  },
  SET_CURRENT_SIZE_MODE_ID: (state, mode) => {
    state.currentSizeModeId = mode;
    sessionStore.set('currentSizeModeId', mode);
  },
  SET_VIEWSIZEE: (state, viewsize) => {
    state.viewsize = viewsize
    sessionStore.set(VIEW_SIZE, viewsize)
  },
  SET_TITLE: (state, title) => {
    state.title = title
    sessionStore.set('title', title)
  }
}

const actions = {
  toggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  closeSideBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  toggleDevice({ commit }, device) {
    commit('TOGGLE_DEVICE', device)
  },
  setLanguage({ commit }, language) {
    commit('SET_LANGUAGE', language)
  },
  setSize({ commit }, size) {
    commit('SET_SIZE', size)
  },
  setViewSize({ commit }, viewsize) {
    commit('SET_VIEWSIZEE', viewsize)
  },
  setTitle({ commit }, title) {
    commit('SET_TITLE', title)
  },
  setCurrentSizeModeId({ commit }, mode) {
    commit('SET_CURRENT_SIZE_MODE_ID', mode)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
