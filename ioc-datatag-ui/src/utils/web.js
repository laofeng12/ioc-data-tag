import DMSocket from '@/utils/DMSocket'
function longSock(url, fn, id) {
  let lockReconnect = false //避免重复连接
  let timeoutFlag = true
  let timeoutSet = null
  let reconectNum = 0
  const timeout = 30000 //超时重连间隔
  let ws

  function reconnect() {
    if (lockReconnect) return
    lockReconnect = true
    //没连接上会一直重连，设置延迟避免请求过多
    if (reconectNum < 3) {
      setTimeout(function () {
        timeoutFlag = true
        createWebSocket()
        reconectNum++
        lockReconnect = false
      }, 5000) //这里设置重连间隔(ms)
    }
  }

  //心跳检测
  const heartCheck = {
    timeout: 5000, //毫秒
    timeoutObj: null,
    serverTimeoutObj: null,
    reset: function () {
      clearInterval(this.timeoutObj)
      clearTimeout(this.serverTimeoutObj)
      return this
    },
    start: function () {
      const self = this
      let count = 0
      this.timeoutObj = setInterval(() => {
        if (count < 3) {
          if (ws.readyState === 1) {
            // ws.send('392846190550001')
            ws.send(id)
          }
          count++
        } else {
          clearInterval(this.timeoutObj)
          count = 0
          if (ws.readyState === 0 && ws.readyState === 1) {
            ws.close()
          }
        }
      }, self.timeout)
    }
  }
  const createWebSocket = () => {
    timeoutSet = setTimeout(() => {
      if (timeoutFlag && reconectNum < 3) {
        reconectNum++
        createWebSocket()
      }
    }, timeout)
    // ws = new WebSocket(url)  // DMSocket
    ws = DMSocket(url)  // DMSocket

    ws.onopen = () => {
      reconectNum = 0
      timeoutFlag = false
      clearTimeout(timeoutSet)
      heartCheck.reset().start()
    }
    ws.onmessage = evt => {
      heartCheck.reset().start()
      //console.info('543',evt);
      //if (evt.data === '已收到来自：392846190550001,的信息') return
      fn(evt, ws)
    }
    ws.onclose = e => {
      if (e.code !== 1000) {
        timeoutFlag = false
        clearTimeout(timeoutSet)
        reconnect()
      } else {
        clearInterval(heartCheck.timeoutObj)
        clearTimeout(heartCheck.serverTimeoutObj)
      }
    }
    ws.onerror = function () {
      reconnect() //重连
    }
  }
  createWebSocket()
  return ws
}

export default longSock
