import { login } from '@/api/login'
import { removeToken } from '@/utils/auth'
import { setLocalStorage } from '@/utils'

const defaultAvatar = require('../../../public/img/administrator.png')

const user = {
  state: {
    token: '',
    apiv1Token: '',
    name: '',
    userInfo: {},
    roles: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_APIV1_TOKEN: (state, token) => {
      state.apiv1Token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_USERINFO: (state, userInfo) => {
      state.userInfo = userInfo
    }
  },

  actions: {
    SetUserInfo ({ commit, dispatch }, userInfo) {
      commit('SET_USERINFO', userInfo)
      setLocalStorage('userInfo', userInfo)
    },

    // 登录
    Login ({ commit, dispatch }, userInfo) {
      const userAccount = userInfo.userAccount.trim()
      const userPwd = userInfo.userPwd
      return new Promise((resolve, reject) => {
        login(userAccount, userPwd).then(res => {
          res.user.avatar = res.user.avatar ? res.user.avatar : defaultAvatar
          const user = res.user
          const token = user.tokenId

          console.log('token', token)
          console.log('user', user)
          dispatch('SetToken', token)
          dispatch('SetUserInfo', user)

          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    SetToken ({ commit, dispatch, state }, token) {
      return new Promise((resolve, reject) => {
        console.log('token0000000', token)

        if (token) {
          commit('SET_TOKEN', token)
          setLocalStorage('token', token)

          commit('SET_APIV1_TOKEN', token)
          setLocalStorage('apiv1Token', token)
          return resolve(token)
        }

        return reject(new Error('new error'))
      })
    },

    // 获取用户信息
    GetInfo ({ commit }) {
      // let userInfo = {}
      // const infoItem = ['access_token', 'userInfo']

      // infoItem.map(item => {
      //   let paramsItem = GetQueryString(item)
      //   userInfo[item] = paramsItem
      // })
    },

    // 登出
    LogOut ({ commit, state }) {
      return new Promise((resolve, reject) => {
        // 暂时注释，不用请求退出登陆接口
        // logout(state.token).then(() => {
        //   commit('SET_TOKEN', '')
        //   removeToken()
        //   resolve()
        // }).catch(error => {
        //   reject(error)
        // })

        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    },

    // 前端 登出
    FedLogOut ({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        resolve()
      })
    }
  }
}

export default user
