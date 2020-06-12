import Cookies from 'js-cookie'
import store from '@/store'
import { mySessionStorage } from './sessionStorage.js'
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
  return mySessionStorage.getItem('userInfo')
}

export function setUserInfo (userInfo) {
  mySessionStorage.setItem('userInfo', userInfo)
}

export function removeUserInfo () {
  Cookies.remove('userInfo')
  mySessionStorage.removeItem('userInfo')
}
