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
    async GetMenuList ({ commit }) {
      try{
        const res = await getMenuList()
        if(res.code == 200){
          const list = res.resources || []
          commit('SET_MENU_LIST', list)
        }
      }catch (e) {
        console.log(e)
      }
    },

    ChangeMyRoutes ({ commit }, myRoutes) {
      commit('CHANGE_MY_ROUTES', myRoutes)
    }
  }
}

export default app
