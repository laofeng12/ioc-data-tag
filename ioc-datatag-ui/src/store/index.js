import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import user from './modules/user'
import dictionary from './modules/dictionary'
import getters from './getters'
import tagPanel from './modules/tagPanel'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    user,
    dictionary,
    tagPanel
  },
  getters
})

export default store
