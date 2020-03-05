import Vue from 'vue'
import App from '@/App.vue'
import router from '@/router'
import store from '@/store'
import 'normalize.css/normalize.css'
// import Portal from 'ch-vue-portal-layout'
import Portal from 'nhc-portal'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// import 'ch-vue-portal-layout/lib/ch-vue-portal-layout.css'
import 'nhc-portal/lib/nhc-portal.css'
import './icons'
import '@/styles/index.scss'
import api from '@/api'
import * as filters from '@/filters'
import Router from 'vue-router'
import singleSpaVue from './single-spa-vue'
import '@/permission' // permission control

const originalPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}
Vue.use(ElementUI)

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.use(Portal)
Vue.config.productionTip = false
Vue.prototype.$api = api

// new Vue({
//   router,
//   store,
//   data: {
//     eventBus: new Vue()
//   },
//   render: h => h(App)
// }).$mount('#app')

// ---------------------------------------------------
const vueOptions = {
  el: '#app',
  router,
  store,
  data: {
    eventBus: new Vue()
  },
  render: h => h(App)
}

// 判断当前页面使用singleSpa应用,不是就渲染
if (!window.singleSpaNavigate) {
  delete vueOptions.el
  new Vue(vueOptions).$mount('#app')
}

// singleSpaVue包装一个vue微前端服务对象
const vueLifecycles = singleSpaVue({
  Vue,
  appOptions: vueOptions
})

export const bootstrap = vueLifecycles.bootstrap // 启动时
export const mount = vueLifecycles.mount // 挂载时
export const unmount = vueLifecycles.unmount // 卸载时

export default vueLifecycles
