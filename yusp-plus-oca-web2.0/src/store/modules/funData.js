/**
 * @created by zhuly8 2021-04-06
 * @updated by
 * @description 全局公共数据存储
 */
/* eslint camelcase:0 */
import { clone } from '@/utils'
import { getOrgTree } from '@/api/common/oauth'
const state = {
  orgTreeData: [], // 机构树

}
const mutations = {
  SET_ORG_TREE_DATA: (state, data) => {
    state.orgTreeData = clone(data, []);
  }
}
const actions = {
  orgTreeFn({dispatch, commit}, res) {
    return new Promise((resolve, reject) => {
      getOrgTree().then(res => {
        commit('SET_ORG_TREE_DATA', res.data);
        resolve(res);
      }).catch(err => {
        console.log(err)
      })
    })
  }
}
export default {
  namespaced: true,
  state,
  mutations,
  actions
}