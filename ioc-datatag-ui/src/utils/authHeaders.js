import { WEBSOCKET_TOKEN_PREFIX } from './index'
import store from '@/store'
/* eslint no-underscore-dangle: 0 */
export function getAuthHeaders () {
  // const currentNamespace = localStorage.getItem('currentNamespace')
  // if (currentNamespace) {
  //   headers.namespace = currentNamespace.substring(currentNamespace.indexOf('/') + 1)
  // }
  // 暂时写死
  const currentNamespace = 'ioc-paas-platform'

  const accessToken = localStorage.getItem('apiv1Token')
  const headers = {}
  const userInfo = store.getters.userInfo
  headers.namespace = currentNamespace
  if (accessToken) {
    headers.Authorization = `Bearer ${accessToken},${userInfo.orgcode}`
  }
  return { headers }
}
export function getAuthProtocol () {
  // let currentNamespace = localStorage.getItem('currentNamespace')
  // if (currentNamespace) {
  //   currentNamespace = currentNamespace.substring(currentNamespace.indexOf('/') + 1)
  // }
  // 暂时写死
  let currentNamespace = 'ioc-paas-platform'
  const accessToken = localStorage.getItem('apiv1Token')
  const userInfo = store.getters.userInfo

  const authProtocol = `${WEBSOCKET_TOKEN_PREFIX}.${accessToken}`
  return [currentNamespace, authProtocol, WEBSOCKET_TOKEN_PREFIX, userInfo.orgcode]
}
