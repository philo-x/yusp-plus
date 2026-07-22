/* eslint max-statements-per-line:0 */
import defaultSettings from '@/config'
import baseFrame from '@/config/frame'
const state = {
  visitedViews: [],
  cachedViews: []
}

const mutations = {
  ADD_VISITED_VIEW: (state, view) => {
    let i = null;
    // 修改处理多开参数更新问题
    if (state.visitedViews.some((v, index) => {
      const isEqual = v.path === view.path;
      if(isEqual) i = index;
      return isEqual;
    })) {
      state.visitedViews[i].title = view.query && view.query.title || state.visitedViews[i].title;
      if(JSON.stringify(view.params || {}) !== '{}') {
        if(!state.visitedViews[i].meta) state.visitedViews[i].meta = {};
        state.visitedViews[i].meta.params = view.params;
      }
      return;
    }
    // 判断存储最大值，并根据条件进行关闭
    const maxOpenTabs = baseFrame.baseTabOptions.maxOpenTabs
    if (maxOpenTabs !== 0 && state.visitedViews.length === maxOpenTabs) {
      for(let i = 0, len = state.visitedViews.length; i < len; i++) {
        if(!(state.visitedViews[i].meta && state.visitedViews[i].meta.affix)) {
          state.visitedViews.splice(i, 1)
          break;
        }
      }
    }
    const curMeta = Object.assign({
      title: view.query && view.query.title || view.meta.title || view.params.title || 'no-name'
    }, view);
    state.visitedViews.push(curMeta);
    const len = state.visitedViews.length - 1;
    if(JSON.stringify(view.params) != '{}') {
      state.visitedViews[len].meta.params = view.params;
    }
  },
  ADD_CACHED_VIEW: (state, view) => {
    let i = null;
    // 修改处理多开参数更新问题
    if (state.cachedViews.some((item, index) => {
      const isEqual = item.name === view.name;
      if(isEqual) {
        i = index;
      }
      return isEqual;
    })) {
      if(JSON.stringify(view.params || {}) !== '{}') { 
        if(!state.cachedViews[i].meta) state.cachedViews[i].meta = {};
        state.cachedViews[i].meta.params = view.params;
      }
      return;
    }
    // if (!view.meta.noCache) {
    //   state.cachedViews.push(view.name)
    // }
    if (view.meta.noCache === false || (view.meta.noCache === undefined && defaultSettings.iscache)) {
      state.cachedViews.push(Object.assign({meta: {
        params: view.params || {}
      }}, view));
      const len = state.cachedViews.length - 1;
      if(JSON.stringify(view.params) != '{}') {
        state.cachedViews[len].meta.params = view.params;
      }
    }
  },

  DEL_VISITED_VIEW: (state, view) => {
    for (const [i, v] of state.visitedViews.entries()) {
      if (v.path === view.path) {
        state.visitedViews.splice(i, 1)
        break
      }
    }
  },
  DEL_CACHED_VIEW: (state, view) => {
    for (const i of state.cachedViews) {
      if (i.name === view.name) {
        const index = state.cachedViews.indexOf(i)
        state.cachedViews.splice(index, 1)
        break
      }
    }
  },

  REPALCE_VISITED_VIEW: (state, views) => {
    for (const [i, v] of state.visitedViews.entries()) {
      const formV = views.from;
      if (v.path === formV.path) {
        state.visitedViews[i] = Object.assign({title: views.to.meta.title || views.to.params.title || 'no-name'}, views.to); //.splice(i, 1)
        break
      }
    }
  },
  REPALCE_CACHED_VIEW: (state, views) => {
    for (const i of state.cachedViews) {
      if (i.name === views.from.name) {
        // const index = state.cachedViews.indexOf(i)
        state.cachedViews[i] = Object.assign({title: views.to.meta.title || views.to.params.title || 'no-name'}, views.to); //.splice(i, 1)
        break
      }
    }
  },

  DEL_OTHERS_VISITED_VIEWS: (state, view) => {
    state.visitedViews = state.visitedViews.filter(v => {
      return v.meta.affix || v.path === view.path
    })
  },
  DEL_OTHERS_CACHED_VIEWS: (state, view) => {
    for (const i of state.cachedViews) {
      if (i.name === view.name) {
        const index = state.cachedViews.indexOf(i)
        state.cachedViews = state.cachedViews.slice(index, index + 1)
        break
      }
    }
  },

  DEL_ALL_VISITED_VIEWS: state => {
    // keep affix tags
    const affixTags = state.visitedViews.filter(tag => tag.meta.affix)
    state.visitedViews = affixTags
  },
  DEL_ALL_CACHED_VIEWS: state => {
    state.cachedViews = []
  },

  UPDATE_VISITED_VIEW: (state, view) => {
    for (let v of state.visitedViews) {
      if (v.path === view.path) {
        v = Object.assign(v, view)
        break
      }
    }
  }
}

const actions = {
  addView({ dispatch }, view) {
    dispatch('addVisitedView', view)
    dispatch('addCachedView', view)
  },
  addVisitedView({ commit }, view) {
    commit('ADD_VISITED_VIEW', view)
  },
  addCachedView({ commit }, view) {
    commit('ADD_CACHED_VIEW', view)
  },

  delView({ dispatch, state }, view) {
    return new Promise(resolve => {
      dispatch('delVisitedView', view)
      dispatch('delCachedView', view)
      resolve({
        visitedViews: [...state.visitedViews],
        cachedViews: [...state.cachedViews]
      })
    })
  },
  delVisitedView({ commit, state }, view) {
    return new Promise(resolve => {
      commit('DEL_VISITED_VIEW', view)
      resolve([...state.visitedViews])
    })
  },
  // 替换路由
  repVisitedView({ commit }, view) {
    commit('REPALCE_VISITED_VIEW', view)
  },
  // 替换路由
  repCachedView({ commit }, view) {
    commit('REPALCE_CACHED_VIEW', view)
  },
  replcaceVisitedView({ dispatch, state }, views) {
    return new Promise(resolve => {
      dispatch('repVisitedView', views)
      dispatch('repCachedView', views)
      resolve({
        visitedViews: [...state.visitedViews],
        cachedViews: [...state.cachedViews]
      })
    })
  },
  delCachedView({ commit, state }, view) {
    return new Promise(resolve => {
      commit('DEL_CACHED_VIEW', view)
      resolve([...state.cachedViews])
    })
  },

  delOthersViews({ dispatch, state }, view) {
    return new Promise(resolve => {
      dispatch('delOthersVisitedViews', view)
      dispatch('delOthersCachedViews', view)
      resolve({
        visitedViews: [...state.visitedViews],
        cachedViews: [...state.cachedViews]
      })
    })
  },
  delOthersVisitedViews({ commit, state }, view) {
    return new Promise(resolve => {
      commit('DEL_OTHERS_VISITED_VIEWS', view)
      resolve([...state.visitedViews])
    })
  },
  delOthersCachedViews({ commit, state }, view) {
    return new Promise(resolve => {
      commit('DEL_OTHERS_CACHED_VIEWS', view)
      resolve([...state.cachedViews])
    })
  },

  delAllViews({ dispatch, state }, view) {
    return new Promise(resolve => {
      dispatch('delAllVisitedViews', view)
      dispatch('delAllCachedViews', view)
      resolve({
        visitedViews: [...state.visitedViews],
        cachedViews: [...state.cachedViews]
      })
    })
  },
  delAllVisitedViews({ commit, state }) {
    return new Promise(resolve => {
      commit('DEL_ALL_VISITED_VIEWS')
      resolve([...state.visitedViews])
    })
  },
  delAllCachedViews({ commit, state }) {
    return new Promise(resolve => {
      commit('DEL_ALL_CACHED_VIEWS')
      resolve([...state.cachedViews])
    })
  },

  updateVisitedView({ commit }, view) {
    commit('UPDATE_VISITED_VIEW', view)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
