import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/views/home/index'
import ContainerWrapper from '@/views/ContainerWrapper'
import Login from '@/views/login/index'
Vue.use(Router)
export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    { path: '/login', component: Login, hidden: true },
    { path: '/404', component: () => import(/* webpackChunkName: "404" */ '@/views/404'), hidden: true },
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/home',
      name: 'home',
      component: Home
    },
    {
      path: '/index',
      name: 'index',
      // meta: { title: '首页' },
      component: ContainerWrapper,
      children: [
        {
          path: '/index',
          name: 'indexHome',
          // meta: { title: '首页' },
          component: () => import(/* webpackChunkName: "index" */ '@/views/index/Index.vue')
        },
      ]
    },
    {
      path: '/taggedImage',
      name: 'taggedImage',
      component: ContainerWrapper,
      redirect: '/lableImage',
      meta: { title: '数据标签与画像' },
      children: [
        {
          path: '/lableImage',
          name: 'lableImage',
          meta: { title: '模型部署管理' },
          component: () => import(/* webpackChunkName: "mysql" */ '@/views/taggedImage/lableImage.vue')
        },
        {
          path: '/tagPanel',
          name: 'tagPanel',
          meta: { title: '标签仪表盘' },
          component: () => import(/* webpackChunkName: "mysql" */ '@/views/taggedImage/tagPanel.vue')
        }, {
          path: '/tagManage',
          name: 'tagManage',
          meta: { title: '标签管理' },
          component: () => import(/* webpackChunkName: "mysql" */ '@/views/taggedImage/tagManage.vue')
        },{
          path: '/portraitQuery',
          name: 'portraitQuery',
          meta: { title: '画像查询' },
          component: () => import(/* webpackChunkName: "mysql" */ '@/views/taggedImage/portraitQuery.vue')
        }]
    }
  ]
})
