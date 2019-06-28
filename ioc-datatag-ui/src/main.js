import Vue from 'vue'
import App from '@/App.vue'
import router from '@/router'
import store from '@/store'
import 'normalize.css/normalize.css'
import Portal from 'ch-vue-portal-layout'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'ch-vue-portal-layout/lib/ch-vue-portal-layout.css'
import '@/permission' // permission control
import './icons'
import '@/styles/index.scss'
import api from '@/api'
import * as filters from '@/filters'
import echarts from 'echarts'
Vue.prototype.$echarts = echarts
require('echarts-wordcloud')
Vue.use(ElementUI)

Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
  console.log('key', key)
  console.log('filters', filters)
})

Vue.use(Portal)
Vue.config.productionTip = false
Vue.prototype.$api = api

new Vue({
  router,
  store,
  data: {
    eventBus: new Vue()
  },
  render: h => h(App)
}).$mount('#app')
