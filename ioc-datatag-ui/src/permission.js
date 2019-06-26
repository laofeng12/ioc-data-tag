import router from './router'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from './utils/auth'
import store from '@/store'

NProgress.configure({ showSpinner: false })// NProgress configuration

const whiteList = ['/login', '/home'] // 不重定向白名单
router.beforeEach((to, from, next) => {
  NProgress.start()

  const access_token = getToken()
  const info = localStorage.getItem('userInfo')
  let userInfo = {}

  if (info) {
    userInfo = JSON.parse(info)
    store.commit('SET_USERINFO', userInfo)
  }

  // 如果有token
  if (access_token && userInfo) {
    if (to.path === '/login' || to.path === '/home') {
      next({ path: '/index' })
      NProgress.done() // if current page is dashboard will not trigger afterEach hook, so manually handle it
    } else {
      // 重置store 的信息
      // let data = { token: access_token, userInfo: userInfo }
      // store.dispatch('SetToken', data)
      next()
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      // next(`/login?redirect=${to.path}`) // 否则全部重定向到登录页
      next('/login')
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
