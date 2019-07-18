import { growthOne,growthTwo,growthThree,labelChange,labelNum } from '@/api/tagPanel'

const state = {
  growth:{
    growthOne:'',
    growthTwo:'',
    growthThree:''
  },
 labelChange:[]
}
const actions = {
  async getgrowthOne ({ commit,state }, params) {
    const { data,code } = await growthOne(params)
    commit('SET_GROWTHONE', data.growth_rate)
  },
  async getgrowthTwo ({ commit,state }, params) {
    const { data,code } = await growthTwo(params)
    commit('SET_GROWTHTWO', data.numbers)
  },
  async getgrowthThree ({ commit,state }, params) {
    const { data,code } = await growthThree(params)
    commit('SET_GROWTHTHREE', data.tagsum)
  },
  async getlabelChange ({ commit,state }, params) {
    const res = await labelChange(params)
    console.log('labelChange',res.data)
    commit('SET_GROWTHCHANGE', res.data)
  },
}
const mutations = {
  SET_GROWTHONE (state, growthOne) {
    state.growth.growthOne = growthOne
  },
  SET_GROWTHTWO (state, growthTwo) {
    state.growth.growthTwo = growthTwo
  },
  SET_GROWTHTHREE (state, growthThree) {
    state.growth.growthThree = growthThree
  },
  SET_GROWTHCHANGE (state, labelChange) {
    state.labelChange = labelChange
  },
}
export default {
  namespaced: true,
  state,
  actions,
  mutations
}
