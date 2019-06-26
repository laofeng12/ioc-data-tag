import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '@/store'
import qs from 'qs'

// formData格式的请求开始
// 创建axios实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // api 的 base_url
  timeout: 15000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(
  config => {
    config = setRequestHeader(config)
    if (config.method.toLowerCase() === 'post') {
      config.data = qs.stringify(config.data)
    }
    return config
  },
  error => {
    // Do something with request error
    console.log('66',error) // for debug
    Promise.reject(error)
  }
)

// forData格式的response 拦截器
service.interceptors.response.use(
  response => {
    return responseCode(response)
  },
  error => {
    return errorCode(error)
  }
)

// 上传图片设置的请求头
const requestUpload = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // api 的 base_url
  timeout: 15000, // 请求超时时间
  headers: {
    'Content-type': 'multipart/form-data'
  }
})

// 上传图片设置的请求头的请求拦截器
requestUpload.interceptors.request.use(
  config => {
    return setRequestHeader(config)
  },
  error => {
    // Do something with request error
    console.log('77',error) // for debug
    Promise.reject(error)
  }
)

// 上传图片设置的请求头 response 拦截器
requestUpload.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    return errorCode(error)
  }
)

// formData格式的请求结束

// json 格式的请求开始
const requestJson = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // api 的 base_url
  timeout: 15000, // 请求超时时间
  headers: {
    'Content-type': 'application/json'
  }
})

// json格式的请求拦截器
requestJson.interceptors.request.use(
  config => {
    return setRequestHeader(config)
  },
  error => {
    // Do something with request error
    console.log('8888888888',error) // for debug
    Promise.reject(error)
  }
)

// josn格式的 response 拦截器
requestJson.interceptors.response.use(
  response => {
    return responseCode(response)
  },
  error => {
    return errorCode(error)
  }
)

// json 格式并且为HTTP返回状态码的请求开始
const requestJsonHttpCode = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // api 的 base_url
  timeout: 15000 // 请求超时时间
})

// json格式的请求拦截器
requestJsonHttpCode.interceptors.request.use(
  config => {
    return setRequestHeader(config)
  },
  error => {
    // Do something with request error
    console.log('98',error) // for debug
    Promise.reject(error)
  }
)

// josn格式的 response 拦截器
requestJsonHttpCode.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    return errorHttpCode(error)
  }
)
// json 格式并且为HTTP返回状态码的请求结束
/**
 * 设置请求头
 * @param {object} config axios的config
 */
function setRequestHeader (config) {
  let token = store.getters.token || localStorage.getItem('token')
  let apiv1Token = store.getters.apiv1Token || localStorage.getItem('apiv1Token')
  if (token) {
    config.headers['authority-token'] = token // 让每个请求携带自定义token 请根据实际情况自行修改
  }

  if (apiv1Token) {
    config.headers['Authorization'] = `Bearer ${apiv1Token}` // 让每个请求携带自定义token 请根据实际情况自行修改
    // config.headers['namespace'] = 'ioc-paas-platform'
  }

  return config
}

/**
 * 后端有返回code的成功的响应回调
 * @param {object} response 响应信息
 */
function responseCode (response) {
  if (response.status === 200 && !response.data.code) {
    return response.data
  }
  /**
   * code为非200是抛错
   */
  const res = response.data
  if (res.code !== 200) {
    // 20019:20020: 重新登陆;
    if (res.code === 20019 || res.code === 20020) {
      authFailure(res)
    } else {
      console.log('99')
      Message({
        message: res.message,
        type: 'error',
        duration: 5 * 1000
      })
    }
    /* eslint-disable-next-line */
    return Promise.reject('new error')
  } else {
    return response.data
  }
}

/**
 * 后端返回的code为错误时的回调
 * @param {object} error 后端返回的错误信息
 */
function errorCode (error) {
  console.log('err44' + error) // for debug
  let errorMsg = error.code === 'ECONNABORTED' ? '请求超时' : error.message

  errorMsg = error.message
  Message({
    message: errorMsg,
    type: 'error',
    duration: 5 * 1000
  })
  return Promise.reject(error)
}

/**
 * http状态码返回的status为错误时的回调
 * @param {object} error 后端返回的错误信息
 */
function errorHttpCode (error) {
  /**
   * code大于400小于500
   */
  const response = error.response || {}
  const data = response ? response.data : {}
  if (response.status >= 400 && response.status < 500) {
    if (response.status === 401) {
      authFailure(data)
    } else {
      Message({
        message: data.message,
        type: 'error',
        duration: 5 * 1000
      })
    }
  } else if (response.status >= 500) {
    Message({
      message: '服务器错误',
      type: 'error',
      duration: 5 * 1000
    })
  } else {
    Message({
      message: '请求超时',
      type: 'error',
      duration: 5 * 1000
    })
  }

  return Promise.reject(error.response)
}
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
// export default service
export {
  service as request,
  requestUpload,
  requestJson,
  requestJsonHttpCode
}
