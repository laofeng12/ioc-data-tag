import router from './router'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken,getUserInfo } from './utils/auth'
import store from '@/store'

NProgress.configure({ showSpinner: false })// NProgress configuration

const whiteList = ['/login', '/home']
router.beforeEach((to, from, next) => {
  NProgress.start()
  const access_token = getToken()
  const userInfo = getUserInfo()
  console.log('44444',userInfo)
  store.commit('SET_USERINFO', userInfo)

  if (access_token && userInfo) {
    if (to.path === '/login' || to.path === '/home') {
      next({ path: '/index' })
      NProgress.done()
    } else {
      next()
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      // next(`/login?redirect=${to.path}`) // 否则全部重定向到登录页
      // next('/home')
      next('/login')
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
