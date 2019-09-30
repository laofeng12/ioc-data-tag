import { getAuthProtocol } from './authHeaders'

export default function DMSocket (url) {
  let scheme
  let socketUrl
  const path = process.env.BASE_URL.substring(0, process.env.BASE_URL.length - 1)
  console.log(path)
  if (url.indexOf('/') === 0) {
    scheme = window.location.protocol === 'http:' ? 'ws://' : 'wss://'
    socketUrl = `${scheme}${location.hostname}:${location.port}${path}${url}`
  } else {
    socketUrl = url
  }
  console.log(socketUrl)
  // const authProtocols = getAuthProtocol()
  return new WebSocket(socketUrl)
}
