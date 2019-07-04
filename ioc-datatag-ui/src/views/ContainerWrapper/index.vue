<template>
  <div>
    <ch-layout
      :toggleSideBar="toggleSideBar"
      @handleClickOutside="handleClickOutside"
      :logout="logout"
      :sidebar="sidebar"
      :device="device"
      :routes="menuList"
      :userInfo="userInfo"
      :dropdownItemList="dropdownItemList"
      :portalInfo="portalInfo" @getResId="getResId"
      :svgArr="svgArr"
    >
      <div class="container-wrapper">
        <transition name="fade-transform" mode="out-in">
          <router-view></router-view>
        </transition>
      </div>
    </ch-layout>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import ResizeMixin from '../../mixin/ResizeHandler'

export default {
  // 这个mixins是为了初始化状态栏的状态，具体见原文件
  mixins: [ResizeMixin],
  data () {
    return {
      dropdownItemList: [{ icon: 'notice', itemName: '公告', itemChild: [{ itemChildName: '消息', itemChildNum: 3, path: '/', link: 'https://www.baidu.com' }] }],
      portalInfo: { logo: require('../../assets/img/logo.png'), portalTitle: '大数据服务平台' },
      svgArr: []
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'device', // 设备类型，mobile或者desktop
      'menuList', // 后台请求回来的菜单
      'userInfo' // 用户信息
    ])
  },
  mounted () {
  },
  created () {
    this.getMenuList()
    const requireAll = requireContext => requireContext.keys().map(requireContext => {
      const startI = requireContext.indexOf('/') + 1
      const endI = requireContext.indexOf('svg') - 1
      return requireContext.substring(startI, endI)
    })

    const req = require.context('../../icons/svg', false, /\.svg$/)
    this.svgArr = requireAll(req)
  },
  methods: {
    getResId (res) {
      // console.log(res)
    },
    toggleSideBar () {
      this.$store.dispatch('ToggleSideBar')
    },
    getMenuList () {
      this.$store.dispatch('GetMenuList')
    },
    handleClickOutside () {
      this.$store.dispatch('CloseSideBar', { withoutAnimation: false })
    },
    logout () {
      this.$store.dispatch('LogOut').then(() => {
        this.$message.success('退出成功')
        location.reload() // 为了重新实例化vue-router对象 避免bug
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.container-wrapper {
  padding: 15px;
  background: #f2f2f2;
  min-height: calc(100vh - 116px);
  overflow: auto;
  height: 100%;
}
</style>
