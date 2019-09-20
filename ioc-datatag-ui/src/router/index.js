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
    {path: '/login', component: Login, hidden: true},
    {path: '/404', component: () => import(/* webpackChunkName: "404" */ '@/views/404'), hidden: true},
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
      meta: {title: '标签与画像'},
      children: [
        {
          path: '/lableImage',
          name: 'lableImage',
          meta: {title: '模型部署'},
          component: () => import(/* webpackChunkName: "mysql" */ '@/views/taggedImage/lableImage.vue'),
        },
        {
          path: '/cooperationModel',
          name: 'cooperationModel',
          meta: {title: '协作模型'},
          component: () => import('@/views/taggedImage/cooperationModel.vue')
        },
        {
          path: '/tagPanel',
          name: 'tagPanel',
          meta: {title: '标签仪表盘'},
          component: () => import(/* webpackChunkName: "mysql" */ '@/views/taggedImage/tagPanel.vue')
        }, {
          path: '/tagManage',
          name: 'tagManage',
          meta: {title: '标签管理'},
          component: () => import(/* webpackChunkName: "mysql" */ '@/views/taggedImage/tagManage.vue'),
        },
        {
          path: '/shareLabel',
          name: 'shareLabel',
          meta: {title: '共享标签组'},
          component: () => import(/* webpackChunkName: "mysql" */ '@/views/taggedImage/shareLabel.vue')
        },
        {
          path: '/lookTree/:tagsId/:tagsName',
          name: 'lookTree',
          meta: {title: '查看标签'},
          component: () => import(/* webpackChunkName: "mysql" */ '@/views/taggedImage/tree.vue')
        },
        {
          path: '/editTree/:tagsId/:tagsName',
          name: 'editTree',
          meta: {title: '编辑标签'},
          component: () => import(/* webpackChunkName: "mysql" */ '@/views/taggedImage/tree.vue')
        },
        {
          path: '/labelcreatTree/:tagsId/:tagsName',
          name: 'labelcreatTree',
          meta: {title: '创建标签组'},
          component: () => import(/* webpackChunkName: "mysql" */ '@/views/taggedImage/tree.vue')
        },
        {
          path: '/tree/:tagsId/:tagsName',
          name: 'tree',
          meta: {title: '创建标签'},
          component: () => import(/* webpackChunkName: "mysql" */ '@/views/taggedImage/tree.vue')
        },
        {
          path: '/portraitQuery',
          name: 'portraitQuery',
          meta: {title: '画像查询'},
          component: () => import(/* webpackChunkName: "mysql" */ '@/views/taggedImage/portraitQuery.vue')
        },
        {
          path: '/queryImage',
          name: 'queryImage',
          meta: {title: '画像查询'},
          component: () => import(/* webpackChunkName: "mysql" */ '@/components/image/queryImage.vue')
        },
        {
          path: '/detailImage',
          name: 'detailImage',
          meta: {title: '画像查询'},
          component: () => import(/* webpackChunkName: "mysql" */ '@/components/image/detailImage.vue')
        },
      ]
    },
    {
      path: '/modelEdit/:id',
      name: 'modelEdit',
      meta: {title: '编辑模型'},
      component: () => import('@/views/taggedImage/modelEdit.vue')
    },
    {
      path: '/ImageDetail',
      name: 'ImageDetail',
      meta: {title: '画像模型'},
      component: () => import('@/views/taggedImage/ImageDetail.vue')
    },
    {
      path: '/creatModel',
      name: 'creatModel',
      meta: {title: '创建模型'},
      component: () => import('@/views/taggedImage/creatModel.vue')
    },
    {
      path: '/editModel/:id',
      name: 'editModel',
      meta: {title: '编辑模型'},
      component: () => import('@/views/taggedImage/creatModel.vue')
    },
    {
      path: '/marking',
      name: 'marking',
      meta: {title: '协作打标'},
      component: () => import('@/views/taggedImage/marking.vue')
    },
    {
      path: '/lookImagedetail',
      name: 'lookImagedetail',
      meta: {title: '查看画像'},
      component: () => import('@/components/image/lookImagedetail.vue')
    }
  ]
})
