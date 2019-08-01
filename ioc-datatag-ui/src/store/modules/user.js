import { login, logout } from '@/api/login'
import { setToken, removeToken, setUserInfo, removeUserInfo } from '@/utils/auth'

const defaultAvatar = require('@/assets/img/administrator.png')

const user = {
  state: {
    token: '',
    apiv1Token: '',
    name: '',
    userInfo: null,
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
      setUserInfo(userInfo)
    },

    // 登录
    Login ({ commit, dispatch }, userInfo) {
      const userAccount = userInfo.userAccount.trim()
      const userPwd = userInfo.userPwd
      return new Promise((resolve, reject) => {
        login(userAccount, userPwd).then(res => {
          const user = res.user
          const token = user.tokenId
          user.avatar = user.headImg ? user.headImg : defaultAvatar
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
        if (token) {
          commit('SET_TOKEN', token)
          commit('SET_APIV1_TOKEN', token)

          setToken(token)
          return resolve(token)
        }

        return reject(new Error('new error'))
      })
    },

    // 登出
    logout ({ dispatch }) {
      return new Promise((resolve, reject) => {
        logout().then(res => {
          dispatch('resetToken')
          return resolve(res)
        }).catch(err => {
          return reject(err)
        })
      })
    },
    resetToken ({ commit }) {
      return new Promise(resolve => {
        removeToken()
        removeUserInfo()

        commit('SET_APIV1_TOKEN', '')
        commit('SET_TOKEN', '')
        commit('SET_USERINFO', {})
        resolve()
      })
    }
  }
}

export default user
