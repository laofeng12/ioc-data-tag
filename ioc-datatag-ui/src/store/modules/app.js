import Cookies from 'js-cookie'
import { getMenuList } from '@/api/menu'

const app = {
  state: {
    sidebar: {
      opened: !+Cookies.get('sidebarStatus'),
      withoutAnimation: false
    },
    device: 'desktop',
    menuList: [],
    catalogmenuList: [],
    myRoutes: []
  },
  mutations: {
    TOGGLE_SIDEBAR: state => {
      if (state.sidebar.opened) {
        Cookies.set('sidebarStatus', 1)
      } else {
        Cookies.set('sidebarStatus', 0)
      }
      state.sidebar.opened = !state.sidebar.opened
      state.sidebar.withoutAnimation = false
    },
    CLOSE_SIDEBAR: (state, withoutAnimation) => {
      Cookies.set('sidebarStatus', 1)
      state.sidebar.opened = false
      state.sidebar.withoutAnimation = withoutAnimation
    },
    TOGGLE_DEVICE: (state, device) => {
      state.device = device
    },
    SET_MENU_LIST: (state, menuList) => {
      state.menuList = menuList
    },
    CHANGE_MY_ROUTES: (state, myRoutes) => {
      state.myRoutes = myRoutes
    }
  },
  actions: {
    ToggleSideBar: ({ commit }) => {
      commit('TOGGLE_SIDEBAR')
    },
    CloseSideBar ({ commit }, { withoutAnimation }) {
      commit('CLOSE_SIDEBAR', withoutAnimation)
    },
    ToggleDevice ({ commit }, device) {
      commit('TOGGLE_DEVICE', device)
    },
    GetMenuList ({ commit }) {
      getMenuList()
        .then(res => {
          console.log('88', res)
          // let list = res.resources || []
          let list = [
            {
              'resId': '742782244680195',
              'resName': '数据标签与画像',
              'resURL': '/lableImage',
              'resType': null,
              'resSort': 0,
              'resIcon': 'user',
              'parentResId': '0',
              'subResList': [
                {
                  'resId': '742732609200195',
                  'resName': '模型部署管理',
                  'resURL': '/lableImage',
                  'resType': null,
                  'resSort': 0,
                  'resIcon': 'fa fa-columns',
                  'parentResId': '742732244680195',
                  'subResList': null
                },
                {
                  'resId': '742782609200195',
                  'resName': '标签仪表盘',
                  'resURL': '/tagPanel',
                  'resType': null,
                  'resSort': 0,
                  'resIcon': 'fa fa-columns',
                  'parentResId': '742782244680195',
                  'subResList': null
                },
                {
                  'resId': '742782609200196',
                  'resName': '标签管理',
                  'resURL': '/tagManage',
                  'resType': null,
                  'resSort': 1,
                  'resIcon': 'fa fa-columns',
                  'parentResId': '742782244680195',
                  'subResList': null
                },
                {
                  'resId': '742782609200196',
                  'resName': '画像查询',
                  'resURL': '/portraitQuery',
                  'resType': null,
                  'resSort': 1,
                  'resIcon': 'fa fa-columns',
                  'parentResId': '742782244680195',
                  'subResList': null
                }
              ]
            },
          ]
          commit('SET_MENU_LIST', list)
        })
        .catch(error => {
          console.log(error)
        })
    },
    ChangeMyRoutes ({ commit }, myRoutes) {
      commit('CHANGE_MY_ROUTES', myRoutes)
    }
  }
}

export default app
