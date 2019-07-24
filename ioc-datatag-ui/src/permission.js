import router from './router'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken,getUserInfo } from './utils/auth'
import store from '@/store'
import Cookies from 'js-cookie'

NProgress.configure({ showSpinner: false })// NProgress configuration

const whiteList = ['/login', '/home']
router.beforeEach((to, from, next) => {
  NProgress.start()
  const access_token = getToken()
  const userInfo = getUserInfo()
  // console.log('44444',userInfo)
  store.commit('SET_USERINFO', userInfo)

  if (access_token && userInfo) {
    if (to.path === '/login' || to.path === '/home') {
      next({ path: '/index' })
      NProgress.done()
    } else {
      // next()
      const routerBase = router.options.base
      const toPath = to.fullPath
      console.log('routerBase',routerBase)
      console.log('toPath',toPath)
      const rootPath = toPath.substring(0, toPath.replace('/', '-').indexOf('/') + 1)
      console.log('rootPath',rootPath)
      if (rootPath === routerBase) {
        next(toPath.replace(routerBase, '/'))
      } else if (to.matched.length === 0) {
        if (Cookies.get(rootPath)) {
          next('/404')
        } else {
          Cookies.set(rootPath, 1)
          console.log('toPath2222',toPath)
          window.location.replace(toPath)
        }
      } else {
        console.log('remove',routerBase)
        Cookies.remove(routerBase)
        next()
      }
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
