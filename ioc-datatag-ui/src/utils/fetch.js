import axios from 'axios'
import qs from 'qs'
import { Message, MessageBox } from 'element-ui'
import store from '@/store'

const service = axios.create({
  baseURL: process.env.BASE_URL, // api 的 base_url
  timeout: 14000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(
  config => {
    const token = store.getters.token || localStorage.getItem('token')
    const apiv1Token = store.getters.apiv1Token || localStorage.getItem('apiv1Token')
    if (token) {
      config.headers['authority-token'] = token // 让每个请求携带自定义token 请根据实际情况自行修改
    }

    if (apiv1Token) {
      config.headers['Authorization'] = `Bearer ${apiv1Token}` // 让每个请求携带自定义token 请根据实际情况自行修改
      config.headers['namespace'] = 'ioc-paas-platform'
      // config.headers['Content-type'] = 'application/json'
    }
    if (!config.contentType) {
      config.headers['Content-type'] = 'application/json'
    } else {
      config.headers['Content-type'] = config.contentType
    }
    // form
    if (config.contentType === 'application/x-www-form-urlencoded' && config.method.toLowerCase() === 'post') {
      config.data = qs.stringify(config.data)
    }
    // DELETE
    if (config.method.toLowerCase() === 'delete') {
      config.params = JSON.stringify(config.params)
    }
    return config
  },
  error => {
    // Do something with request error
    Promise.reject(error)
  }
)

// forData格式的response 拦截器
service.interceptors.response.use(
  response => {
    /**
     * code为非200是抛错
     */
    const { status, data } = response
    if (status === 200) {
      const { code, message } = data
      if (code) {
        if (code !== 200) {
          // 20019:20020: 重新登陆;
          if (code === 20019 || code === 20020) {
            authFailure(data)
          } else {
            Message({
              message: message || '请求错误',
              type: 'error',
              duration: 5 * 1000
            })
          }
          /* eslint-disable-next-line */
          return Promise.reject('new error')
        }
      }
      return data
    }
  },
  error => {
    if (!error.response) {
      Message({
        message: '请求超时',
        type: 'error',
        duration: 5 * 1000
      })
      return
    }
    const { status, data } = error.response
    if (!data) {
      Message({
        message: '服务器错误',
        type: 'error',
        duration: 5 * 1000
      })
    }
    if (status === 401 || status === 403) {
      authFailure(data)
    } else if (status > 403 && status < 500) {
      Message({
        message: data.message,
        type: 'error',
        duration: 5 * 1000
      })
    } else {
      Message({
        message: data.message || '服务器错误',
        type: 'error',
        duration: 5 * 1000
      })
    }
    return Promise.reject(error.response)
  }
)

function authFailure (data) {
  MessageBox.alert(
    data.message,
    '警告',
    {
      confirmButtonText: '重新登录',
      type: 'warning',
      showClose: false
    }
  ).then(() => {
    store.dispatch('FedLogOut').then(() => {
      location.reload() // 为了重新实例化vue-router对象 避免bug
    })
  })
}

const fetch = function (url) {
  return service({
    url,
    method
  })
}

// export default service
export {
  service as fetch
}
