export default {
  state: {
    content:'',
    contentArr:[],
    tableArr:[]
  },
  mutations: {
    saveContent (state, params) {
      state.content = params
    },
    arrContent (state, params) {
      state.contentArr = params
    },
    listArr(state,params){
      state.tableArr = params
    }
  },
  actions: {
    getimageContent ({commit,state}, params) {
      commit('saveContent', params)
    },
    getArr ({commit,state}, params) {
      commit('arrContent', params)
    },
    getlistArr ({commit,state}, params) {
      commit('listArr', params)
    },
  }
}
