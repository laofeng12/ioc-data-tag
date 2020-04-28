import router from './router'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken, getUserInfo } from './utils/auth'
import store from '@/store'
import Cookies from 'js-cookie'

NProgress.configure({ showSpinner: false }) // NProgress configuration

const whiteList = ['/login', '/home']

// single-spa 模式下，路由拦截只设置用户信息，路由判断由外围统一处理
if (window.singleSpaNavigate) {
  router.beforeEach((to, from, next) => {
    const objectList = Object.keys(window.singleSpaConfig.spaProjects)
    for (let obj of objectList) {
      if (to.path.match(new RegExp('^' + obj))) {
        return
      }
    }
    // 把路由挂在 window，让外围拿到数据
    window.singleSpaConfig.router = { to, baseUrl: '/datatagweb' }
    const userInfo = getUserInfo()
    store.commit('SET_USERINFO', userInfo)
    next()
  })
} else {
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
        const rootPath = toPath.substring(0, toPath.replace('/', '-').indexOf('/') + 1)
        if (rootPath === routerBase) {
          next(toPath.replace(routerBase, '/'))
        } else if (to.matched.length === 0) {
          if (Cookies.get(rootPath)) {
            next('/404')
          } else {
            Cookies.set(rootPath, 1)
            window.location.replace(toPath)
          }
        } else {
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
}
