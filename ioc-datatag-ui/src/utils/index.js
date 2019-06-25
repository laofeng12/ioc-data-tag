export const WEBSOCKET_TOKEN_PREFIX = 'bearer.authorization.platform.dgzsj.com'

export function removeSessionStorage (item) {
  sessionStorage.removeItem(item)
}

export function removeLocalStorage (item) {
  localStorage.removeItem(item)
}

export function setSessionStorage (item, value) {
  let stringValue = typeof value === 'string' ? value : JSON.stringify(value)
  return sessionStorage.setItem(item, stringValue)
}

export function setLocalStorage (item, value) {
  let stringValue = typeof value === 'string' ? value : JSON.stringify(value)
  return localStorage.setItem(item, stringValue)
}

export function GetQueryString (name) {
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
  var r = window.location.search.substr(1).match(reg) // search,查询？后面的参数，并匹配正则
  if (r != null) return unescape(r[2]); return null
}

/*
 防抖，防止快速重复点击
 */
export function throttle (fn, wait = 500, scope) {
  clearTimeout(throttle.timer)
  throttle.timer = setTimeout(function () {
    fn.apply(scope)
  }, wait)
}

// 图片转为base64
export function getBase64 (file) {
  return new Promise(function (resolve, reject) {
    let reader = new FileReader()
    let imgResult = ''
    reader.readAsDataURL(file)
    reader.onload = function () {
      imgResult = reader.result
    }
    reader.onerror = function (error) {
      reject(error)
    }
    reader.onloadend = function () {
      resolve(imgResult)
    }
  })
}

// json格式化 显示到html
export function syntaxHighlight (json) {
  if (typeof json !== 'string') {
    json = JSON.stringify(json, undefined, 2)
  }
  json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>')
  return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\\-]?\d+)?)/g, function (match) {
    var cls = 'number'
    if (/^"/.test(match)) {
      if (/:$/.test(match)) {
        cls = 'key'
      } else {
        cls = 'string'
      }
    } else if (/true|false/.test(match)) {
      cls = 'boolean'
    } else if (/null/.test(match)) {
      cls = 'null'
    }
    return '<span class="' + cls + '">' + match + '</span>'
  })
}
