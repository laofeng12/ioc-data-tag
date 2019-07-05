import { getBaseData, getImagesList } from '@/api/common'

const dictionary = {
  state: {
    industryList: [],
    imagesList: []
  },
  mutations: {
    GET_LIST: (state, list) => {
      state.industryList = list
    },
    SET_IMAGES_LIST: (state, list) => {
      state.imagesList = list
    }
  },
  actions: {
    // 字典查询
    getIndustryList ({ commit }, code) {
      return new Promise((resolve, reject) => {
        const params = {
          codetype: code
        }
        getBaseData(params).then(res => {
          if (res.code === 200) {
            resolve(res.rows)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    GetImagesList ({ commit }, params) {
      return new Promise((resolve, reject) => {
        getImagesList(params).then(res => {
          commit('SET_IMAGES_LIST', res.tags)
          resolve()
        }).catch(err => {
          console.log(err)
          reject(err)
        })
      })
    }
  }
}

export default dictionary
