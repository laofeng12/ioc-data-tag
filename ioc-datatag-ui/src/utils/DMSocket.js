import { getAuthProtocol } from './authHeaders'

export default function DMSocket (url) {
  let scheme
  let socketUrl
  if (url.indexOf('/') === 0) {
    scheme = window.location.protocol === 'http:' ? 'ws://' : 'wss://'
    socketUrl = `${scheme}${location.hostname}:${location.port}${url}`
  } else {
    socketUrl = url
  }
  const authProtocols = getAuthProtocol()
  return new WebSocket(socketUrl, authProtocols)
}
