import { WEBSOCKET_TOKEN_PREFIX } from './index'
import { getToken, getUserInfo } from './auth.js'

export function getAuthProtocol () {
  let currentNamespace = 'ioc-paas-platform'
  const accessToken = getToken()
  const userInfo = getUserInfo()

  const authProtocol = `${WEBSOCKET_TOKEN_PREFIX}.${accessToken}`
  return [currentNamespace, authProtocol, WEBSOCKET_TOKEN_PREFIX, userInfo.orgcode]
}
