import Cookies from 'js-cookie'
import store from '@/store'
// import { GetQueryString } from './index'

const TokenKey = 'authority-token'

export function getToken () {
  // Cookies上带token进来 或者从本地获取token
  const authorityToken = Cookies.get(TokenKey)

  authorityToken && store.dispatch('SetToken', authorityToken)

  return authorityToken
}

export function setToken (token) {
  Cookies.set(TokenKey, token)
}

export function removeToken () {
  Cookies.remove(TokenKey)
}

export function getUserInfo () {
  const cookiesUserInfo = Cookies.get('userInfo') ? JSON.parse(Cookies.get('userInfo')) : null
  console.log('cookiesUserInfo',cookiesUserInfo)
  const userInfo = store.getters.userInfo || cookiesUserInfo
  return userInfo
}

export function setUserInfo (userInfo) {
  Cookies.set('userInfo', userInfo)
}

export function removeUserInfo () {
  Cookies.remove('userInfo')
}
