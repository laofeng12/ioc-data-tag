import { growthOne,growthTwo,growthThree,labelChange,labelNum } from '@/api/tagPanel'
import { getImage,getImagelist } from '@/api/lableImage'
const state = {
  growth:{
    growthOne:'',
    growthTwo:'',
    growthThree:''
  },
  labelChange:[],
  content:'', // hua内容
  contentArr:[],  // 查询返回的数组
  listArr:[]  // 详情返回的数组
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
  //查询
  async getimageList ({ commit,state }, params) {
    console.log('chaxun',params)
    try{
      const res = await getImage(params)
      console.log('查询',res.rows)
      commit('SET_IMAGEARR', res.rows)
    }catch (e) {
      console.log(e);
    }
  },
  // 详情
  async getarrList ({ commit,state }, params) {
    try{
      const res = await getImagelist(params)
      console.log('详情',res.data)
      commit('SET_LISTARR', res.data)
    }catch (e) {
      console.log(e);
    }

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
  //查询
  SET_IMAGEARR (state, contentArr) {
    state.contentArr = contentArr
  },
  // 详情
  SET_LISTARR (state, listArr) {
    state.listArr = listArr
  },
}
export default {
  namespaced: true,
  state,
  actions,
  mutations
}
