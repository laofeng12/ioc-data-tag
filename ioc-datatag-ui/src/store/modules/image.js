export default {
  state: {
    content:'',
    contentArr:[]
  },
  mutations: {
    saveContent (state, params) {
      state.content = params
    },
    arrContent (state, params) {
      state.contentArr = params
    }
  },
  actions: {
    getimageContent ({commit,state}, params) {
      commit('saveContent', params)
    },
    getArr ({commit,state}, params) {
      commit('arrContent', params)
    },
  }
}
