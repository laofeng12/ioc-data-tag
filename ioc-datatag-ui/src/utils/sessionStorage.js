export function getItem (key) {
  let res = sessionStorage.getItem(key)
  try {
    res = JSON.parse(res)
  } catch {}
  return res
}

export function setItem (key, value) {
  if (!key || !value) {
    return
  }
  if (typeof value === 'object') {
    value = JSON.stringify(value)
  }
  sessionStorage.setItem(key, value)
}

export function removeItem (key) {
  sessionStorage.removeItem(key)
}

export function clear () {
  sessionStorage.clear()
}

export const mySessionStorage = {
  getItem,
  setItem,
  removeItem,
  clear
}
